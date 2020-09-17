/**
 * @author banbao
 */

import java.util.*;
public class TestPrintMap{
    public static void main(String...args){
        HashMap<String, String> map = new HashMap<>();
        map.put("FE6", "The Binding Blade");
        map.put("FE7", "The Blazing Sword");
        map.put("FE8", "The Sacred Stone");
        System.out.println(map);
        HashSet<String> set = new HashSet<>();
        set.add("The Binding Blade");
        set.add("The Blazing Sword");
        set.add("The Sacred Stone");
        System.out.println(set);
    }
}

/*
 * output
 * 
 * {FE6=The Binding Blade, FE8=The Sacred Stone, FE7=The Blazing Sword}
 * [The Binding Blade, The Sacred Stone, The Blazing Sword]
 */
