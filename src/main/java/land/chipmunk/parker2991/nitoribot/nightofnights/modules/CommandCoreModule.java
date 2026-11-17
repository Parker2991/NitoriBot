package land.chipmunk.parker2991.nitoribot.nightofnights.modules;

import land.chipmunk.parker2991.nitoribot.nightofnights.NitoriBot;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.CommandCoreAreaData;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.blocks.BlockRegistries;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.world.ChunkPosData;
import land.chipmunk.parker2991.nitoribot.nightofnights.listeners.Listener;
import land.chipmunk.parker2991.nitoribot.nightofnights.util.ChunkColumn;
import land.chipmunk.parker2991.nitoribot.nightofnights.util.ComponentUtil;
import land.chipmunk.parker2991.nitoribot.nightofnights.util.SleepUtil;
import net.kyori.adventure.text.BlockNBTComponent;
import net.kyori.adventure.text.Component;
import org.cloudburstmc.math.vector.Vector3d;
import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;
import org.geysermc.mcprotocollib.network.packet.Packet;
import org.geysermc.mcprotocollib.protocol.data.game.entity.object.Direction;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.Hand;
import org.geysermc.mcprotocollib.protocol.data.game.entity.player.PlayerAction;
import org.geysermc.mcprotocollib.protocol.data.game.item.ItemStack;
import org.geysermc.mcprotocollib.protocol.data.game.item.component.*;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.BlockChangeEntry;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.BlockEntityType;
import org.geysermc.mcprotocollib.protocol.data.game.level.block.CommandBlockMode;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.ClientboundBlockUpdatePacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.clientbound.level.ClientboundSectionBlocksUpdatePacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory.ServerboundSetCommandBlockPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.inventory.ServerboundSetCreativeModeSlotPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player.ServerboundPlayerActionPacket;
import org.geysermc.mcprotocollib.protocol.packet.ingame.serverbound.player.ServerboundUseItemOnPacket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CommandCoreModule implements Listener {
    private final NitoriBot bot;
    public Vector3i position;
    public Vector3i itemPosition;
    public final CommandCoreAreaData area;
    public Vector3i lastBlockPosition;
    public List<Vector3i> commandBlocks = new ArrayList<>();
    public Vector3i currentBlockRelative = Vector3i.from(
        0,
        0,
        0
    );

    public CommandCoreModule (NitoriBot bot) {
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

    @Override
    public void packetReceived (Packet packet) {
        switch (packet) {
            case ClientboundSectionBlocksUpdatePacket p -> multiBlockChange(p);
            case ClientboundBlockUpdatePacket p -> blockChange(p);
            default -> {}
        }
    }

    private void blockChange (ClientboundBlockUpdatePacket packet) {
        BlockChangeEntry entry = packet.getEntry();
        Vector3i pos = entry.getPosition();

        for (Vector3i blocks : commandBlocks) {
            if (pos.equals(blocks)) {
                run(
                    String.format(
                        "minecraft:setblock %s %s %s command_block",
                        blocks.getX(),
                        blocks.getY(),
                        blocks.getZ()
                    )
                );
            }
        }
    }

    private void multiBlockChange (ClientboundSectionBlocksUpdatePacket packet) {
        BlockChangeEntry[] entries = packet.getEntries();
        int chunkX = packet.getChunkX();
        int chunkY = packet.getChunkY();
        int chunkZ = packet.getChunkZ();

        for (BlockChangeEntry entry : entries) {
            BlockRegistries block = bot.registry.getBlockByStateId(entry.getBlock());

            if (block.block.equals("minecraft:command_block")) {
                if (commandBlocks.size() < 256) {
                    commandBlocks.add(entry.getPosition());
                }
            }
            //System.out.println(entry.toString());

            for (Vector3i pos : commandBlocks) {
                if (pos.equals(entry.getPosition()) && !block.block.equals("minecraft:command_block")) {
                    move();
                }
                //if (commandBlocks.size() < 256 && pos.equals(entry.getPosition())) move();
            }
        }
    }


    @Override
    public void botMoved () {
        commandBlocks.clear();
        move();
    }

    public void move () {
        Vector3d botPos = bot.position.positionData;
        position = Vector3i.from(
            Math.floor(botPos.getX() / 16) * 16,
            0,
            Math.floor(botPos.getZ() / 16) * 16
        );

        itemPosition = Vector3i.from(
            Math.floor(botPos.getX()),
            Math.floor(botPos.getY() - 1),
            Math.floor(botPos.getZ())
        );

        if (bot.config.core.itemRefill)
            itemRefill();
        else
            chatRefill();
    }

    public void chatRefill () {
        bot.chat.command(getCommand());
    }

    public void itemRefill () {
        final NbtMapBuilder blockEntityTagBuilder = NbtMap.builder();

        String command = getCommand();

        final NbtMap blockEntityTag = blockEntityTagBuilder.build();

        final Map<DataComponentType<?>, DataComponent<?, ?>> map = new HashMap<>();

        map.put(
            DataComponentTypes.BLOCK_ENTITY_DATA,
            DataComponentTypes.BLOCK_ENTITY_DATA
                .getDataComponentFactory()
                .create(
                    DataComponentTypes.BLOCK_ENTITY_DATA,
                    TypedEntityData
                        .<BlockEntityType>builder()
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

        bot.session.send(new ServerboundSetCreativeModeSlotPacket(
            (short) 36,
            new ItemStack(
                bot.registry.getBlockRegistry("minecraft:command_block").itemId,
                64,
                dataComponents
            )
        ));

        bot.session.send(new ServerboundPlayerActionPacket(
            PlayerAction.START_DIGGING,
            itemPosition,
            Direction.NORTH,
            0
        ));

        bot.session.send(new ServerboundUseItemOnPacket(
            itemPosition,
            Direction.UP,
            Hand.MAIN_HAND,
            0.5f,
            0.5f,
            0.5f,
            false,
            false,
            420
        ));

        commandBlock(
            itemPosition,
            command,
            true,
            false,
            true
        );

        /*bot.executor.schedule(() -> {
            bot.world.getBlock(itemPosition).thenAccept(reply -> {
                if (reply == null) chatRefill();
            });
        }, 100, TimeUnit.MILLISECONDS);*/
    }

    private String getCommand () {
        Vector3i areaStart = area.start();
        Vector3i areaEnd = area.end();

        return String.format(
            "minecraft:fill %s %s %s %s %s %s command_block{CustomName:%s} strict",
            position.getX() + areaStart.getX(),
            position.getY() + areaStart.getY(),
            position.getZ() + areaStart.getZ(),
            position.getX() + areaEnd.getX(),
            position.getY() + areaEnd.getY(),
            position.getZ() + areaEnd.getZ(),
            bot.config.core.coreName
        );
    }

    public Vector3i currentBlock () {
        if (position == null)
            return null;
        return Vector3i.from(
            currentBlockRelative.getX() + position.getX(),
            currentBlockRelative.getY() + position.getY(),
            currentBlockRelative.getZ() + position.getZ()
        );
    }

    private void incrementCurrentBlock () {
        currentBlockRelative = currentBlockRelative.add(
            1,
            0,
            0
        );

        if (currentBlockRelative.getX() > area
            .end()
            .getX()) {
            currentBlockRelative = currentBlockRelative.add(
                area
                    .start()
                    .getX() - area
                    .end()
                    .getX(),
                0,
                0
            );
            currentBlockRelative = currentBlockRelative.add(
                0,
                0,
                1
            );
        }

        if (currentBlockRelative.getZ() > area
            .end()
            .getZ()) {
            currentBlockRelative = currentBlockRelative.add(
                0,
                0,
                area
                    .start()
                    .getZ() - area
                    .end()
                    .getZ()
            );
            currentBlockRelative = currentBlockRelative.add(
                0,
                1,
                0
            );
        }

        if (currentBlockRelative.getX() > area
            .end()
            .getX()) {
            currentBlockRelative = currentBlockRelative.add(
                area
                    .start()
                    .getX() - area
                    .end()
                    .getX(),
                area
                    .start()
                    .getY() - area
                    .end()
                    .getY(),
                area
                    .start()
                    .getZ() - area
                    .end()
                    .getZ()
            );
        }
    }

    private void commandBlock (
        Vector3i pos,
        String command,
        boolean doesTrackOutput,
        boolean conditional,
        boolean automatic

    ) {
        if (command.length() > 32767 || position == null)
            return;
        bot.session.send(new ServerboundSetCommandBlockPacket(
            pos,
            command,
            CommandBlockMode.AUTO,
            doesTrackOutput,
            conditional,
            automatic
        ));
    }

    public void run (String command) {
        commandBlock(
            currentBlock(),
            command,
            true,
            false,
            true
        );

        incrementCurrentBlock();
    }

    public Component runTrackedWithTellraw (
        String tag,
        String selector,
        String command
    ) {
        commandBlock(
            currentBlock(),
            command,
            true,
            false,
            true
        );

        lastBlockPosition = Vector3i.from(
            currentBlock().getX(),
            currentBlock().getY(),
            currentBlock().getZ()
        );

        Component nbtComponent = Component
            .blockNBT(
                "LastOutput.extra",
                true,
                BlockNBTComponent.WorldPos.worldPos(
                    BlockNBTComponent.WorldPos.Coordinate.absolute(lastBlockPosition.getX()),
                    BlockNBTComponent.WorldPos.Coordinate.absolute(lastBlockPosition.getY()),
                    BlockNBTComponent.WorldPos.Coordinate.absolute(lastBlockPosition.getZ())
                )
            )
            .insertion(tag);

        Component component = Component
            .empty()
            .append(nbtComponent)
            .append(Component
                .text()
                .insertion(selector));

        incrementCurrentBlock();

        return component;
    }

    public CompletableFuture<Component> runTrackedWithPacket (String command) {
        Vector3i location = currentBlock();
        commandBlock(
            location,
            command,
            true,
            false,
            true
        );

        incrementCurrentBlock();

        try {
            Thread.sleep(100);
        } catch (Exception ignore) {}

        return bot.world.getBlock(location);
    }
}
