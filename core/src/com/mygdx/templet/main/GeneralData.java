package com.mygdx.templet.main;

import java.util.HashMap;

public class GeneralData {

    HashMap<String, Boolean> LevelCollected = new HashMap<>();

    GeneralData(){
        LevelCollected.put("Tiled/Town.tmx", false);
        LevelCollected.put("Tiled/Farm.tmx", false);
        LevelCollected.put("Tiled/Cavern.tmx", false);
        LevelCollected.put("Tiled/Archery.tmx", false);
    }

    public boolean getCollected(String levelName){return LevelCollected.get(levelName);}

    public void setCollected(String levelName){ LevelCollected.put(levelName, true); }


}
