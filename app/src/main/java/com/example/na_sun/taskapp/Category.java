package com.example.na_sun.taskapp;

import java.io.Serializable;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Category extends RealmObject implements Serializable, RealmModel {

    private String category;

    //idをプライマリキーとして設定
    @PrimaryKey
    private int id;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
