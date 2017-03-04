package hackernews.android.dummy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample author for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Story> ITEMS = new ArrayList<Story>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Story> ITEM_MAP = new HashMap<String, Story>();

    private static final int COUNT = 25;

    static {
        // Add some sample items.
        for (int i = 1; i <= COUNT; i++) {
            addItem(createDummyItem(i));
        }
    }

    private static void addItem(Story item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.title, item);
    }

    private static Story createDummyItem(int position) {
        return new Story(String.valueOf(position), "Item " + position, makeDetails(position), "3 hours ago" );
    }

    private static String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore points information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of author.
     */
    public static class Story {
        public final String title;
        public final String author;
        public final String points;
        public final String time;

        public Story(String title, String author, String points, String time) {
            this.title = title;
            this.author = author;
            this.points = points;
            this.time = time;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
