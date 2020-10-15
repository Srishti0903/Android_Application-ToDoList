package com.example.todoapp.Model;

//define the structure of individual task
public class ToDoModel {
    private String task;
    private int status;
    private String state;
    private String date;
    private String time;
    UserHelperClass userHelperClass;

    public ToDoModel(){}

    public ToDoModel(int status,String state, String task, String date,String time,UserHelperClass userHelperClass) {
        this.status = status;
        this.task = task;
        this.date = date;
        this.time = time;
        this.state = state;
        this.userHelperClass = userHelperClass;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }

    public UserHelperClass getUserHelperClass() {
        return userHelperClass;
    }

    public void setUserHelperClass(UserHelperClass userHelperClass) {
        this.userHelperClass = userHelperClass;
    }
}
