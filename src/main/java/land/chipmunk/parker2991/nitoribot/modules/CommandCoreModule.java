package land.chipmunk.parker2991.nitoribot.modules;

import java.util.HashMap;
import java.util.Map;

import land.chipmunk.parker2991.nitoribot.logger.Logger;
import org.cloudburstmc.math.vector.Vector3d;
import org.cloudburstmc.math.vector.Vector3i;

import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtType;
import org.cloudburstmc.nbt.NbtMapBuilder;

import org.geysermc.mcprotocollib.protocol.data.game.entity.object.Direction;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.BlockEntityType;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.Hand;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.PlayerAction;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.DataComponent;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.DataComponentType;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.DataComponentTypes;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.DataComponents;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.TypedEntityData;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.TypedEntityData.TypedEntityDataBuilder;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.CommandBlockMode;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory.ServerboundSetCommandBlockPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory.ServerboundSetCreativeModeSlotPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player.ServerboundPlayerActionPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player.ServerboundUseItemOnPacket;

import net.kyori.adventure.text.Component;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.data.CommandCoreAreaData;
import land.chipmunk.parker2991.nitoribot.data.PositionData;
import land.chipmunk.parker2991.nitoribot.listeners.Listener;
import land.chipmunk.parker2991.nitoribot.util.ComponentUtil;

public class CommandCoreModule extends Listener {
  private Bot bot;

  public CommandCoreAreaData area;

  @Override
  public void botMoved () {
    move();
  }

  public Vector3i position;

  public Vector3i itemPosition;

  public void move () {
    Vector3d botPos = bot.position.positionAsVector;
    position = Vector3i.from(
      Math.floor(botPos.getX() / 16) * 16,
      0,
      Math.floor(botPos.getZ() / 16) * 16
    );

    itemPosition = Vector3i.from(
      botPos.getX(),
      botPos.getY() - 1,
      botPos.getZ()
    );

    chatRefill();
  };

  public void chatRefill () {
    Vector3i pos = position;
    CommandCoreAreaData coreArea = area;

    int startPosX = pos.getX() - coreArea.start.getX();
    int startPosY = pos.getY() - coreArea.start.getY();
    int startPosZ = pos.getZ() - coreArea.start.getZ();
    int endPosX = pos.getX() - coreArea.end.getX();
    int endPosY = pos.getY() - coreArea.end.getY();
    int endPosZ = pos.getZ() - coreArea.end.getZ();

    String command = String.format(
      "minecraft:fill %s %s %s %s %s %s command_block{CustomName:%s} destroy",
      startPosX,
      startPosY,
      startPosZ,
      endPosX,
      endPosY,
      endPosZ,
      bot.config.core.coreName
    );

    bot.chat.command(command);
  }

  public void itemRefill () {
    final NbtMapBuilder blockEntityTagBuilder = NbtMap.builder();

    Vector3i pos = position;
    CommandCoreAreaData coreArea = area;

    int startPosX = pos.getX() - coreArea.start.getX();
    int startPosY = pos.getY() - coreArea.start.getY();
    int startPosZ = pos.getZ() - coreArea.start.getZ();
    int endPosX = pos.getX() - coreArea.end.getX();
    int endPosY = pos.getY() - coreArea.end.getY();
    int endPosZ = pos.getZ() - coreArea.end.getZ();

    String command = String.format(
      "minecraft:fill %s %s %s %s %s %s command_block{CustomName:%s} replace",
      startPosX,
      startPosY,
      startPosZ,
      endPosX,
      endPosY,
      endPosZ,
      bot.config.core.coreName
    );

    blockEntityTagBuilder.putString("Command", command)
      .putByte("auto", (byte) 1)
      .putByte("TrackOutput", (byte) 1);

    final NbtMap blockEntityTag = blockEntityTagBuilder.build();

    final Map<DataComponentType<?>, DataComponent<?, ?>> map = new HashMap<>();

    map.put(
      DataComponentTypes.BLOCK_ENTITY_DATA,
      DataComponentTypes.BLOCK_ENTITY_DATA
      .getDataComponentFactory()
      .create(
        DataComponentTypes.BLOCK_ENTITY_DATA,
        TypedEntityData.<BlockEntityType>builder()
          .type(BlockEntityType.COMMAND_BLOCK)
          .tag(blockEntityTag)
          .build()
      )
    );

    map.put(
      DataComponentTypes.ITEM_NAME,
      DataComponentTypes.ITEM_NAME
      .getDataComponentFactory()
      .create(
        DataComponentTypes.ITEM_NAME,
        ComponentUtil.componentFromJSON(bot.config.core.coreName)
      )
    );

    map.put(
      DataComponentTypes.CUSTOM_NAME,
      DataComponentTypes.CUSTOM_NAME
      .getDataComponentFactory()
      .create(
        DataComponentTypes.CUSTOM_NAME,
        ComponentUtil.componentFromJSON(bot.config.core.coreName)
      )
    );

    final DataComponents dataComponents = new DataComponents(map);

    bot.session.send(
      new ServerboundSetCreativeModeSlotPacket(
        (short) 36,
        new ItemStack(
          482,
          64,
          dataComponents
        )
      )
    );

    bot.session.send(
      new ServerboundPlayerActionPacket(
        PlayerAction.START_DIGGING,
        itemPosition,
        Direction.NORTH,
        0
      )
    );

    bot.session.send(
      new ServerboundUseItemOnPacket(
        itemPosition,
        Direction.NORTH,
        Hand.MAIN_HAND,
        0.5f,
        0.5f,
        0.5f,
        false,
        false,
        1
      )
    );
  }

  public Vector3i currentBlockRelative = Vector3i.from(0, 0, 0);

  public PositionData currentBlock () {
    Vector3i relativePosition = currentBlockRelative;
    Vector3i corePosition = position;

    if (corePosition == null) return null;
    return new PositionData(
      relativePosition.getX() + corePosition.getX(),
      relativePosition.getY() + corePosition.getY(),
      relativePosition.getZ() + corePosition.getZ()
    );
  }

  public void incrementCurrentBlock () {
    Vector3i relativePosition = currentBlockRelative;
    CommandCoreAreaData coreArea = area;

    int posX = relativePosition.getX();
    int posY = relativePosition.getY();
    int posZ = relativePosition.getZ();

    posX++;

    if (posX > coreArea.end.getX()) {
      posX = coreArea.start.getX();
      posZ++;
    }

    if (posZ > coreArea.end.getZ()) {
      posZ = coreArea.start.getZ();
      posY++;
    }

    if (posY > coreArea.end.getY()) {
      posX = coreArea.start.getX();
      posY = coreArea.start.getY();
      posZ = coreArea.start.getZ();
    }
  }

  public void commandBlock (String command, boolean doesTrackOutput, boolean conditional, boolean automatic) {
    if (command.length() > 32767 || position == null) return;
    bot.session.send(
      new ServerboundSetCommandBlockPacket(
        position,
        command,
        CommandBlockMode.AUTO,
        doesTrackOutput,
        conditional,
        automatic
      )
    );
  }

  public void run (String command) {

    commandBlock(command, true, false, true);

    incrementCurrentBlock();
  }

  public CommandCoreModule (Bot bot) {
    this.bot = bot;

    this.area = new CommandCoreAreaData(
      Vector3i.from(
        bot.config.core.area.start.x,
        bot.config.core.area.start.y,
        bot.config.core.area.start.z
      ),
      Vector3i.from(
        bot.config.core.area.end.x,
        bot.config.core.area.end.y,
        bot.config.core.area.end.z
      )
    );

    bot.listenerManager.addListener(this);
  }
}
