package land.chipmunk.parker2991.nitoribot.nightofnights.data;

import java.util.ArrayList;
import java.util.List;

public class ChatTranslationsData {
    public static class Narration {
        public String translation_key;
        public List<String> parameters = new ArrayList<>();
    }
    public static class Chat {
        public String translation_key;
        public List<String> parameters = new ArrayList<>();
        public Narration narration = new Narration();
    }

    public final Chat chat = new Chat();
}