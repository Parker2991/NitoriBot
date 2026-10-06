package land.chipmunk.parker2991.nitoribot.util;

import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.Map;

public class TranslationsUtil {
    private static final String TAG = "nitoribot";

    private static final Map<String, String> TRANSLATIONS = ComponentUtil.loadJsonStringMap("translations.json");

  /*public static void customTranslations (String translation, Component component) {
    List<Component> list = new ArrayList<>();


    if (component != null) list = List.of(
      component
    );

    customTranslations(translation, list);
  }*/

    public static Component customTranslations (String translation, List<Component> component) {
        Component fallback;
        if (component == null)
            fallback = Component.translatable(TAG + "." + translation).fallback(getTranslation(translation));
        else
            fallback = Component.translatable(TAG + "." + translation, component).fallback(getTranslation(translation));

        return fallback;
    }

    private static String getTranslation (String translation) {
        for (Map.Entry<String, String> translations : TRANSLATIONS.entrySet()) {
            if (translation.equals(translations.getKey())) return translations.getValue();
        }
        return null;
    }
}