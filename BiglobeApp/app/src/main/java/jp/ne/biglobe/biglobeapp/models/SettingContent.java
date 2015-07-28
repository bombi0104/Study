package jp.ne.biglobe.biglobeapp.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.ne.biglobe.biglobeapp.BLApplication;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class SettingContent {

    /**
     * An array of sample (dummy) items.
     */
    public static ArrayList<SettingItem> ITEMS = new ArrayList<SettingItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, SettingItem> ITEM_MAP = new HashMap<String, SettingItem>();

    static {
        // Add 3 sample items.
        addItem(new SettingItem("1", "All Setting", "", true));
        addItem(new SettingItem("2", "NEWS-Morning", "", false));
        addItem(new SettingItem("3", "NEWS-Noon", "", true));
        addItem(new SettingItem("4", "NEWS-Night", "", false));
        addItem(new SettingItem("5", "NEWS-Dialog", "", true));
        addItem(new SettingItem("6", "Baseball Schedule", "", false));
        addItem(new SettingItem("7", "Baseball Start battle", "", true));
        addItem(new SettingItem("8", "Baseball End battle", "", false));
        addItem(new SettingItem("9", "Osusume", "", true));
        addItem(new SettingItem("10", "Osusume dialog", "", true));


//        SettingModel settingModel = BLApplication.getSetting();

//        addItem(new SettingItem("1", ""));

    }

    private static void addItem(SettingItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class SettingItem {
        public String id;
        public String content;
        public String description;
        public boolean value;

        public SettingItem(String id, String content, String description, boolean value) {
            this.id = id;
            this.content = content;
            this.description = description;
            this.value = value;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
