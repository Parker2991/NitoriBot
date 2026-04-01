package land.chipmunk.parker2991.nitoribot.modules;

import land.chipmunk.parker2991.nitoribot.Bot;
import land.chipmunk.parker2991.nitoribot.data.CommandCoreAreaData;
import land.chipmunk.parker2991.nitoribot.data.PositionData;

import org.geysermc.mcprotocollib.protocol.data.game.level.block.CommandBlockMode;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory.ServerboundSetCommandBlockPacket;

import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.math.vector.Vector3d;

public class CommandCoreModule extends PositionModule.Listener {
  private Bot bot;

  public CommandCoreAreaData area;

  @Override
  public boolean botMoved () {
    move();
    return true;
  }

  public Vector3i position;

  public void move () {
    Vector3d botPos = bot.position.positionAsVector;
    position = Vector3i.from(
      Math.floor(botPos.getX() / 16) * 16,
      0,
      Math.floor(botPos.getZ() / 16) * 16
    );

    refill();
  };

  public void refill () {
    Vector3i pos = position;
    CommandCoreAreaData coreArea = area;

    int startPosX = pos.getX() - coreArea.start.getX();
    int startPosY = pos.getY() - coreArea.start.getY();
    int startPosZ = pos.getZ() - coreArea.start.getZ();
    int endPosX = pos.getX() - coreArea.end.getX();
    int endPosY = pos.getY() - coreArea.end.getY();
    int endPosZ = pos.getZ() - coreArea.end.getZ();

    String command = String.format(
      "minecraft:fill %s %s %s %s %s %s command_block destroy",
      startPosX,
      startPosY,
      startPosZ,
      endPosX,
      endPosY,
      endPosZ
    );

    bot.chat.command(command);
  }

  public Vector3i currentBlockRelative = Vector3i.from(0, 0, 0);

  public PositionData currentBlock () {
    Vector3i relativePosition = currentBlockRelative;
    Vector3i corePosition = position;

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
    System.out.println(bot.config.core.area.start.x);
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

    bot.position.addListener(this);
  }
}