package com.example.eventmap.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;

import java.util.List;

@Dao
public interface EventDao {
    @Insert
    void insert(Event event);

    @Query("SELECT * FROM events")
    List<Event> getAll();

    @Query("DELETE FROM events")
    void deleteAll();

    @Delete  // Это аннотация для удаления конкретного объекта
    void delete(Event event);  // Удаляет переданный объект
}
