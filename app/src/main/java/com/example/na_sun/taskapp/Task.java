package com.example.na_sun.taskapp;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Task extends RealmObject implements Serializable, RealmModel {
    private String title;
    private String contents;
    private Date date;
    private Category category;
    //idをプライマリキーとして設定
    @PrimaryKey
    private int id;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title){
        this.title =title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date){
        this.date = date;
    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id = id;
    }
    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
