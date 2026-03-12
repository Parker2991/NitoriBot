package land.chipmunk.parker2991.nitoribot.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.kyori.adventure.text.*;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// credits to chayapak, i ported this from a source of chomens java have because i do not want to write a ansi parser

// totallynotskidded™ from chipmunkbot and added colors (ignore the ohio code please,..,.)
public class ComponentUtil {
    // is this the best way to check?
    public static boolean isEqual (Component component1, Component component2) {
        return component1.toString().equals(component2.toString());
    }

    // component parsing
    public static final Map<String, String> language = loadJsonStringMap("language.json");
    private static final Map<String, String> keybinds = loadJsonStringMap("keybinds.json");

    public static final Pattern ARG_PATTERN = Pattern.compile("%(?:(\\d+)\\$)?([s%])");

    public static final Map<String, String> ansiMap = new HashMap<>();
    static {
        // map totallynotskidded™ from https://github.com/PrismarineJS/prismarine-chat/blob/master/index.js#L10
        ansiMap.put("0", "\u001b[30m");
        ansiMap.put("1", "\u001b[34m");
        ansiMap.put("2", "\u001b[32m");
        ansiMap.put("3", "\u001b[36m");
        ansiMap.put("4", "\u001b[31m");
        ansiMap.put("5", "\u001b[35m");
        ansiMap.put("6", "\u001b[33m");
        ansiMap.put("7", "\u001b[37m");
        ansiMap.put("8", "\u001b[90m");
        ansiMap.put("9", "\u001b[94m");
        ansiMap.put("a", "\u001b[92m");
        ansiMap.put("b", "\u001b[96m");
        ansiMap.put("c", "\u001b[91m");
        ansiMap.put("d", "\u001b[95m");
        ansiMap.put("e", "\u001b[93m");
        ansiMap.put("f", "\u001b[97m");
        ansiMap.put("l", "\u001b[1m");
        ansiMap.put("o", "\u001b[3m");
        ansiMap.put("n", "\u001b[4m");
        ansiMap.put("m", "\u001b[9m");
        ansiMap.put("k", "\u001b[6m");
        ansiMap.put("r", "\u001b[0m");
    }

    public record PartiallyStringified(
        String output,
        String lastColor
    ) {}

    private ComponentUtil () {
    }

    private static Map<String, String> loadJsonStringMap (String name) {
        Map<String, String> map = new HashMap<>();

        InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(name);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();

        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            map.put(entry.getKey(), json.get(entry.getKey()).getAsString());
        }

        return map;
    }

    private static String getOrReturnFallback (TranslatableComponent component) {
        final String key = component.key();

        final String minecraftKey = language.get(key);
        if (minecraftKey != null) return minecraftKey;
        else return component.fallback() != null ? component.fallback() : key;
    }

    public static String stringify (Component message) { return stringify(message, null); }
    private static String stringify (Component message, String lastColor) {
        try {
            final StringBuilder builder = new StringBuilder();

            final PartiallyStringified output = stringifyPartially(message, false, false, lastColor, false);

            builder.append(output.output);

            for (Component child : message.children()) builder.append(stringify(child, output.lastColor));

            return builder.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static String stringifyMotd (Component message) { return stringifyMotd(message, null); }
    private static String stringifyMotd (Component message, String lastColor) {
        try {
            final StringBuilder builder = new StringBuilder();

            final PartiallyStringified output = stringifyPartially(message, true, false, lastColor, false);

            builder.append(output.output);

            for (Component child : message.children()) builder.append(stringifyMotd(child, output.lastColor));

            return builder.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static String stringifyAnsi (Component message) { return stringifyAnsi(message, null, false); }
    public static String stringifyAnsi (Component message, boolean noHex) { return stringifyAnsi(message, null, noHex); }
    private static String stringifyAnsi (Component message, String lastColor, boolean noHex) {
        try {
            final StringBuilder builder = new StringBuilder();

            final PartiallyStringified output = stringifyPartially(message, false, true, lastColor, noHex);

            builder.append(output.output);

            for (Component child : message.children()) builder.append(stringifyAnsi(child, output.lastColor, noHex));

            return builder.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public static PartiallyStringified stringifyPartially (Component message, boolean motd, boolean ansi, String lastColor, boolean noHex) {
        if (message instanceof TextComponent) return stringifyPartially((TextComponent) message, motd, ansi, lastColor, noHex);
        else if (message instanceof TranslatableComponent) return stringifyPartially((TranslatableComponent) message, motd, ansi, lastColor, noHex);
        else if (message instanceof SelectorComponent) return stringifyPartially((SelectorComponent) message, motd, ansi, lastColor, noHex);
        else if (message instanceof KeybindComponent) return stringifyPartially((KeybindComponent) message, motd, ansi, lastColor, noHex);

        return new PartiallyStringified("", null);
    }

    public static String getStyle (Style textStyle, boolean motd) {
        if (textStyle == null) return null;

        StringBuilder style = new StringBuilder();

        for (Map.Entry<TextDecoration, TextDecoration.State> decorationEntry : textStyle.decorations().entrySet()) {
            final TextDecoration decoration = decorationEntry.getKey();
            final TextDecoration.State state = decorationEntry.getValue();

            if (state == TextDecoration.State.NOT_SET || state == TextDecoration.State.FALSE) continue;

            if (!motd) {
                switch (decoration) {
                    case BOLD -> style.append(ansiMap.get("l"));
                    case ITALIC -> style.append(ansiMap.get("o"));
                    case OBFUSCATED -> style.append(ansiMap.get("k"));
                    case UNDERLINED -> style.append(ansiMap.get("n"));
                    case STRIKETHROUGH -> style.append(ansiMap.get("m"));
                }
            } else {
                switch (decoration) {
                    case BOLD -> style.append("§l");
                    case ITALIC -> style.append("§o");
                    case OBFUSCATED -> style.append("§k");
                    case UNDERLINED -> style.append("§n");
                    case STRIKETHROUGH -> style.append("§m");
                }
            }
        }

        return style.toString();
    }

    public static String getColor (TextColor color, boolean motd, boolean ansi, boolean noHex) {
        if (color == null) return null;

        // map totallynotskidded™ too from https://github.com/PrismarineJS/prismarine-chat/blob/master/index.js#L299
        String code;
        if (color == NamedTextColor.BLACK) code = "0";
        else if (color == NamedTextColor.DARK_BLUE) code = "1";
        else if (color == NamedTextColor.DARK_GREEN) code = "2";
        else if (color == NamedTextColor.DARK_AQUA) code = "3";
        else if (color == NamedTextColor.DARK_RED) code = "4";
        else if (color == NamedTextColor.DARK_PURPLE) code = "5";
        else if (color == NamedTextColor.GOLD) code = "6";
        else if (color == NamedTextColor.GRAY) code = "7";
        else if (color == NamedTextColor.DARK_GRAY) code = "8";
        else if (color == NamedTextColor.BLUE) code = "9";
        else if (color == NamedTextColor.GREEN) code = "a";
        else if (color == NamedTextColor.AQUA) code = "b";
        else if (color == NamedTextColor.RED) code = "c";
        else if (color == NamedTextColor.LIGHT_PURPLE) code = "d";
        else if (color == NamedTextColor.YELLOW) code = "e";
        else if (color == NamedTextColor.WHITE) code = "f";
        else {
            try {
                code = color.asHexString();
            } catch (NullPointerException e) {
                code = ""; // mabe...,,.,..,
            }
        }

        if (motd) {
            return "§" + code;
        } else if (ansi) {
            String ansiCode = ansiMap.get(code);
            if (ansiCode == null) {
                if (noHex) {
                    final int rgb = Integer.parseInt(code.substring(1), 16);

                    final String chatColor = ColorUtil.getClosestChatColor(rgb);

                    ansiCode = ansiMap.get(chatColor);
                } else {
                    ansiCode = "\u001b[38;2;" +
                            color.red() +
                            ";" +
                            color.green() +
                            ";" +
                            color.blue() +
                            "m";
                }
            }

            return ansiCode;
        } else return null;
    }

    public static PartiallyStringified stringifyPartially (TextComponent message, boolean motd, boolean ansi, String lastColor, boolean noHex) {
        if ((motd || ansi) && /* don't color big messages -> */ message.content().length() < 25_000) {
            final String color = getColor(message.color(), motd, ansi, noHex);
            final String style = getStyle(message.style(), motd);

            String replacedContent = message.content();
            // seems very mabe mabe
            if (ansi && replacedContent.contains("§")) {
                // is try-catch a great idea?
                try {
                    replacedContent = Pattern
                            .compile("(§.)")
                            .matcher(message.content())
                            .replaceAll(m -> ansiMap.get(m.group(0).substring(1)));
                } catch (Exception ignored) {}
            }

            // messy af
            return new PartiallyStringified((lastColor != null ? lastColor : "") + (color != null ? color : "") + (style != null ? style : "") + replacedContent + (ansi ? ansiMap.get("r") : ""), color);
        }

        return new PartiallyStringified(message.content(), null);
    }

    public static PartiallyStringified stringifyPartially (TranslatableComponent message, boolean motd, boolean ansi, String lastColor, boolean noHex) {
        String format = getOrReturnFallback(message);

        // totallynotskidded™️ from HBot (and changed a bit)
        Matcher matcher = ARG_PATTERN.matcher(format);
        StringBuilder sb = new StringBuilder();

        final String style = getStyle(message.style(), motd);
        final String _color = getColor(message.color(), motd, ansi, noHex);
        String color;
        if (_color == null) color = "";
        else color = _color;

        int i = 0;
        while (matcher.find()) {
            if (matcher.group().equals("%%")) {
                matcher.appendReplacement(sb, "%");
            } else {
                String idxStr = matcher.group(1);
                int idx = idxStr == null ? i++ : (Integer.parseInt(idxStr) - 1);
                if (idx >= 0 && idx < message.arguments().size()) {
                    matcher.appendReplacement(
                            sb,
                            Matcher.quoteReplacement(
                                    motd ?
                                            stringifyMotd(message.arguments().get(idx).asComponent()) + color :
                                            (
                                                    ansi ?
                                                            stringifyAnsi(message.arguments().get(idx).asComponent()) + color :
                                                            stringify(message.arguments().get(idx).asComponent())
                                            )
                            )
                    );
                } else {
                    matcher.appendReplacement(sb, "");
                }
            }
        }
        matcher.appendTail(sb);

        return new PartiallyStringified((lastColor != null ? lastColor : "") + color + (style != null && ansi ? style : "") + sb + (ansi ? ansiMap.get("r") : ""), _color);
    }

    public static PartiallyStringified stringifyPartially (SelectorComponent message, boolean motd, boolean ansi, String lastColor, boolean noHex) {
        final String style = getStyle(message.style(), motd);
        final String _color = getColor(message.color(), motd, ansi, noHex);
        String color;
        if (_color == null) color = "";
        else color = _color;
        return new PartiallyStringified((lastColor != null ? lastColor : "") + color + (style != null && ansi ? style : "") + message.pattern(), _color); // * Client-side selector components are equivalent to text ones, and do NOT list entities.
    }

    public static PartiallyStringified stringifyPartially (KeybindComponent message, boolean motd, boolean ansi, String lastColor, boolean noHex) {
        String keybind = message.keybind();
        Component component = keybinds.containsKey(keybind) ? Component.translatable(keybind) : Component.text(keybind); // TODO: Fix some keys like `key.keyboard.a`
        return stringifyPartially(component, motd, ansi, lastColor, noHex);
    }
}
