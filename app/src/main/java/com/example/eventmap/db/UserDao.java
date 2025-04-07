package com.example.eventmap.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Query("SELECT * FROM users")
    List<User> getAll();

    @Query("DELETE FROM users")
    void deleteAll();

    @Query("SELECT * FROM users WHERE login = :login")
    User getByLogin(String login);
}
