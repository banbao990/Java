/**
 * @author banbao
 * @comment 修改自示例代码
 */

import java.util.prefs.*;

public class PreferencesDemo {
    public static void main(String[] args) throws Exception {
        Preferences prefs = Preferences
            .userNodeForPackage(PreferencesDemo.class);
        // prefs.put("Location", "Oz");
        // prefs.put("Footwear", "Ruby Slippers");
        // prefs.putInt("Companions", 4);
        // prefs.putBoolean("Are there witches?", true);
        // int usageCount = prefs.getInt("UsageCount", 0); // 0 为默认值
        // usageCount++;
        // prefs.putInt("UsageCount", usageCount);
        print(prefs);
        prefs.clear();
        print(prefs);
        // You must always provide a default value:
        // System.out.println(
            // "How many companions does Dorothy have? " +
            // prefs.getInt("Companions", 0));
    }
    
    private static void print(Preferences prefs) 
    throws BackingStoreException {
        for(String key : prefs.keys()) {
            System.out.println(key + ": "+ prefs.get(key, null));
        }
        // The returned array will be of size zero if this node has no preferences.
        if(prefs.keys().length == 0) {
            System.out.println("null");
        }
    }
}