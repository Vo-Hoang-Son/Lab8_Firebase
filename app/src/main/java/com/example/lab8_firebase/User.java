package com.example.lab8_firebase;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class User {
    public String name;
    public String image;
    public int normal;
    public int happy;
    public int unhappy;
    public String email;

    public User() {
    }

    public User(String name, String image, String email ) {
        this.name = name;
        this.image = image;
        this.email = email;
    }
    public User(String name, String image, int normal, int happy, int unhappy, String email) {
        this.name = name;
        this.image = image;
        this.normal = normal;
        this.happy = happy;
        this.unhappy = unhappy;
        this.email = email;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public int getNormal() {
        return normal;
    }

    public void setNormal(int normal) {
        this.normal = normal;
    }

    public int getHappy() {
        return happy;
    }

    public void setHappy(int happy) {
        this.happy = happy;
    }

    public int getUnhappy() {
        return unhappy;
    }

    public void setUnhappy(int unhappy) {
        this.unhappy = unhappy;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
