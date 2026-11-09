package land.chipmunk.parker2991.nitoribot.nightofnights.commands.trusted;

import land.chipmunk.parker2991.nitoribot.nightofnights.NitoriBot;
import land.chipmunk.parker2991.nitoribot.nightofnights.command.*;
import land.chipmunk.parker2991.nitoribot.nightofnights.data.PlayerProfileData;

import static land.chipmunk.parker2991.nitoribot.nightofnights.util.ComponentUtil.convertColorString;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.List;

public class ValidateCommand implements CommandInfo {
    @Override
    public CommandTrustLevels getTrustLevel () {
        return CommandTrustLevels.TRUSTED;
    }

    @Override
    public List<String> getAliases () {
        return List.of(
            "validate",
            "val"
        );
    }

    @Override
    public String getDescription () {
        return "validate within the bot";
    }

    @Override
    public List<String> getUsages () {
        return List.of("<hash>");
    }

    @Override
    public void execute (CommandContext context) throws CommandError {
        NitoriBot bot = context.bot();
        CommandSource source = context.source();
        PlayerProfileData player = source.sender();
        Component component = null;

        if (source.source().equals(CommandMessageSource.DISCORD)) throw new CommandError(Component.text("this command is meant to be ran in game!").color(NamedTextColor.RED));
        else {
            if (!player.alreadyValidated) {
                switch (player.trustLevel) {
                    case CommandTrustLevels.TRUSTED -> component = Component.text("Validated as Trusted")
                        .color(convertColorString(bot.config.colors.help.trusted));

                    case CommandTrustLevels.ADMIN -> component = Component.text("Validated as Admin")
                        .color(convertColorString(bot.config.colors.help.admin));
                    case CommandTrustLevels.OWNER -> component = Component.text("Validated as Owner")
                        .color(convertColorString(bot.config.colors.help.owner));
                }

                player.alreadyValidated = true;
            } else {
                component = Component.translatable("Validated as %s", Component.text(player.trustLevel.toString()))
                    .color(convertColorString(bot.config.colors.commands.primary));
            }

            source.sendFeedback(bot, component);
        }

    }
}
