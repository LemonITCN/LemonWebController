package net.lemonsoft.lwc.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LiuRi on 16/8/16.
 */
public class MainManagerPool{

    private static Map<String , MainManager> mainManagerPool;

    static {
        mainManagerPool = new HashMap<>();
    }

    public static void addMainManager(MainManager mainManager){
        mainManagerPool.put(mainManager.getId() , mainManager);
    }

    public static void removeMainManagerById(String id){
        mainManagerPool.remove(id);
    }

    public static MainManager getMainManagerById(String id){
        return mainManagerPool.get(id);
    }

}
