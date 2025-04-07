package com.example.eventmap.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Index;

@Entity(tableName = "users", indices = {@Index(value = {"login"}, unique = true)})
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String login;
    public String password;

    public User(String login, String password){
        this.login = login;
        this.password = password;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getLogin(){
        return login;
    }

    public void setLogin(String login){
        this.login = login;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }
}
