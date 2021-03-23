package com.mygdx.templet.SaveData;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class SaveEntry {

    //Gets all the data to the entry
    public SaveEntry(){
        uid = 0;
    }

    @PrimaryKey
    public int uid;

}
