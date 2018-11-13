package com.example.jason.finalproj;

import android.graphics.Bitmap;

/**
 * Created by BH Qin on 2017/12/27.
 */

public class Users {
    private int id;
    private Bitmap photo;
    private String name;
    private int sex;
    private Integer[] emblems;
    private String Githubname;
    private String description;
    private String email;
    private int best_jump;
    public Users(Bitmap bitmap){
        photo = bitmap;
        name = "";
        sex = 0;
        emblems = new Integer[]{0,0,0};
        Githubname = "";
        description ="";
        email ="";
        best_jump = -1;//可以用来标识是不是无意义的User
    }
    public Users(int id,Bitmap photo,String name,int sex,Integer[] emblems,String Githubname,String description,String email,int best_jump){
        this.id = id;
        this.photo = photo;
        this.name = name;
        this.sex = sex;
        this.emblems = emblems;
        this.Githubname = Githubname;
        this.description = description;
        this.email = email;
        this.best_jump = best_jump;
    }

    public int getId() {
        return id;
    }

    public Bitmap getPhoto(){
        return photo;
    }
    public String getName(){
        return name;
    }

    public int getSex() {
        return sex;
    }

    public Integer[] getEmblems() {
        return emblems;
    }

    public String getGithubname() {
        return Githubname;
    }

    public String getDescription() {
        return description;
    }

    public String getEmail() {
        return email;
    }

    public int getBest_jump() {
        return best_jump;
    }

}
