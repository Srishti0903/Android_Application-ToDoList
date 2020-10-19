package com.example.todoapp.ROOM;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DAO {
    @Insert
    public void addUsers(Users users);

    @Insert
    public void addTasks(Tasks tasks);

    @Query("Select * from Users")
    List<Users> getUsernamesHere();

    @Query("Select * from Tasks")
    List<Tasks> getTasks();

    @Query("Update Tasks set task = :task,date = :date,time = :time,status = :status where id = :taskId")
    void updateTask(String task, String date, String time, int status, int taskId);

    @Query("Delete from Tasks where id = :taskId")
    void deleteTask(int taskId);

}
