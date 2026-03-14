package land.chipmunk.parker2991.nitoribot.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.kyori.adventure.text.*;
import net.kyori.adventure.text.flattener.ComponentFlattener;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.object.PlayerHeadObjectContents;
import net.kyori.adventure.text.object.SpriteObjectContents;
import net.kyori.adventure.text.serializer.ComponentEncoder;
import net.kyori.adventure.text.serializer.ansi.ANSIComponentSerializer;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.ansi.ColorLevel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Pattern;

// credits to Werkku for letting me use this

public final class ComponentUtil {

    private static final Map<String, String> LANGUAGE = loadJsonStringMap("language.json");
    private static final Map<String, String> KEYBINDINGS = loadJsonStringMap("keybinds.json");

    private static final Pattern ARG_PATTERN = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");

    private static final ThreadLocal<Integer> TOTAL_DEPTH = ThreadLocal.withInitial(() -> 0); //used to track the depth of translate components

    private static final int MAX_DEPTH = 512; //translate depth limit

    private static Map<String, String> loadJsonStringMap(String name) {
        Map<String, String> map = new HashMap<>();

        var is = ClassLoader.getSystemClassLoader().getResourceAsStream(name);
        if (is == null) throw new IllegalArgumentException("File not found: " + name);
        var reader = new BufferedReader(new InputStreamReader(is));
        var json = JsonParser.parseReader(reader).getAsJsonObject();

        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
            map.put(entry.getKey(), entry.getValue().getAsString());
        }

        return map;
    }

    private static final GsonComponentSerializer GSON_COMPONENT_SERIALIZER = GsonComponentSerializer.builder() // 1.21.5+ gson serializer
            .build();

    private static final ANSIComponentSerializer ANSI_COMPONENT_SERIALIZER = ANSIComponentSerializer.builder()
            .flattener(getFlattener(true))
            .colorLevel(ColorLevel.TRUE_COLOR)
            .build();

    private static final PlainTextComponentSerializer PLAIN_TEXT_COMPONENT_SERIALIZER = PlainTextComponentSerializer.builder()
            .flattener(getFlattener(false))
            .build();

    private static ComponentFlattener getFlattener(boolean parseSectionSigns) {
        return ComponentFlattener.builder()
                .mapper(TextComponent.class, component -> mapText(
                        component, parseSectionSigns
                ))
                .mapper(ObjectComponent.class, ComponentUtil::mapObject)
                .complexMapper(KeybindComponent.class, ComponentUtil::mapKeybind)
                .mapper(SelectorComponent.class, SelectorComponent::pattern)
                .complexMapper(TranslatableComponent.class, ComponentUtil::mapTranslatable)
                .unknownMapper(_ -> "")
                .nestingLimit(MAX_DEPTH) //max depth for nested mapper calls
                .build();
    }


    private static String getOrReturnFallback(TranslatableComponent component) {
        var key = component.key();
        var fallback = component.fallback();

        return LANGUAGE.getOrDefault(
                key,
                fallback == null ? key : fallback
        );
    }

    private static String guardedStringify(ComponentEncoder<Component, String> serializer, Component message) {
        try {
            return serializer.serialize(message);
        } catch (Exception e) {
            return serializer.serialize(Component.translatable(
                    "<Failed to parse component: %s>",
                    NamedTextColor.RED,
                    Component.text(e.toString())));
        } finally {
            TOTAL_DEPTH.set(0); //set translate depth to 0 after component is parsed
        }
    }

    /**
     * Convert Component to a JSON string
     */
    public static String componentToJSON(Component component) {
        return GSON_COMPONENT_SERIALIZER.serialize(component);
    }

    /**
     * Convert JSON string to a Component
     */
    public static Component componentFromJSON(String json) {
        return GSON_COMPONENT_SERIALIZER.deserialize(json);
    }

    /**
     * Convert Component to a string
     */
    public static String componentToString(Component component) {
        return guardedStringify(PLAIN_TEXT_COMPONENT_SERIALIZER, component);
    }

    /**
     * Convert Component to an ANSI string
     */
    public static String componentToAnsi(Component component) {
        return guardedStringify(ANSI_COMPONENT_SERIALIZER, component);
    }

    private static String mapObject(ObjectComponent objectComponent) {
        var content = objectComponent.contents();
        return switch (content) {
            case SpriteObjectContents c -> "<Sprite:" + c.sprite() + ">";
            case PlayerHeadObjectContents c ->
                    "<PlayerHead:" + componentToString(Component.translatable("block.minecraft.player_head.named").arguments(Component.text((c.name() != null) ? c.name() : "Unknown"))) + ">";
            default -> "<Unknown Sprite>";
        };
    }

    private static String mapText(TextComponent component, boolean parseSectionSigns) {
        var content = component.content();

        //if no section sign is found, return the original content
        if (!parseSectionSigns || !content.contains("§")) {
            return component.content();
        } else {
            //TODO: make it parse section signs properly
            return component.content();
        }
    }

    private static void mapKeybind(KeybindComponent component, Consumer<Component> consumer) {
        consumer.accept(Component.translatable(KEYBINDINGS.getOrDefault(component.keybind(), component.keybind())));
    }

    private static void mapTranslatable(TranslatableComponent component, Consumer<Component> consumer) {
        var format = getOrReturnFallback(component);
        var matcher = ARG_PATTERN.matcher(format);
        List<Component> result = new ArrayList<>();

        try {
            var i = 0;
            var lastIndex = 0;

            while (matcher.find(lastIndex)) {
                var start = matcher.start();
                var end = matcher.end();

                //add text before the match
                if (start > lastIndex) {
                    var formatSegment = format.substring(lastIndex, start);
                    //ensure that theres no % in the formatSegment (if there is, we have a problem with escaping)
                    if (formatSegment.indexOf('%') != -1) throw new IllegalArgumentException();
                    result.add(Component.text(formatSegment));
                }

                var full = format.substring(start, end);

                //handle the escaped percentage sign '%%'
                if (full.equals("%%")) {
                    result.add(Component.text("%"));
                } else if (Objects.equals(matcher.group(2), "s")) {
                    //handle placeholders like %s
                    var idxStr = matcher.group(1);
                    var idx = (idxStr == null) ? i++ : (Integer.parseInt(idxStr) - 1);

                    if (idx < 0 || idx > component.arguments().size()) throw new IllegalArgumentException();

                    var currentTotalDepth = TOTAL_DEPTH.get();
                    if (currentTotalDepth > MAX_DEPTH) {
                        //TODO: make this better
                        //consumer.accept(Component.text("**Translate Crash**").color(NamedTextColor.DARK_RED))
                        //return
                        throw new IllegalArgumentException("Translate too deep");
                    }
                    TOTAL_DEPTH.set(currentTotalDepth + 1);

                    result.add(component.arguments().get(idx).asComponent());
                } else {
                    //if there are other invalid patterns throw an exception
                    throw new IllegalArgumentException("Unsupported placeholder format: $full");
                }

                lastIndex = end;
            }

            //handle any remaining part of the string after the last match
            if (lastIndex < format.length()) {
                var remaining = format.substring(lastIndex);
                if (remaining.indexOf('%') != -1)
                    throw new IllegalArgumentException(); // Make sure no unescaped '%' remains
                result.add(Component.text(remaining));
            }
        } catch (Exception _) {
            //fallback: clear result and return the original string if any exception occurs
            result.clear();
            result.add(Component.text(format));
        }

        //join all parts and pass to the consumer
        consumer.accept(Component.join(JoinConfiguration.noSeparators(), result));
    }
}

