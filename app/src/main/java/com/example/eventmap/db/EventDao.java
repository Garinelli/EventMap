package com.example.eventmap.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EventDao {
    @Insert
    void insert(Event event);

    @Query("SELECT * FROM Event")
    List<User> getAll();

    @Query("DELETE FROM Event")
    void deleteAll();
}
