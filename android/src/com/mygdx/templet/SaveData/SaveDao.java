package com.mygdx.templet.SaveData;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SaveDao {
    //Returns all of the rows
    @Query("SELECT * FROM saveentry")
    List<SaveEntry> getAll();

    //Updates the provided rows
    @Update
    void updateEntry(SaveEntry... saveEntries);

    //Adds in an entry
    @Insert
    void insertAll(SaveEntry... saveEntries);
}
