package com.example.todoapp.Model;

public class UserHelperClass {
    String fullname,username,email,phoneno,pass,gender;

    public UserHelperClass() {
    }

    public UserHelperClass(String fullname, String username, String email, String phoneno, String pass,String gender) {
        this.fullname = fullname;
        this.username = username;
        this.email = email;
        this.phoneno = phoneno;
        this.pass = pass;
        this.gender = gender;

    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
