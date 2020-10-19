package com.example.todoapp.ROOM;


import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Tasks.class, Users.class}, version = 1, exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
    public abstract DAO dao();
}
