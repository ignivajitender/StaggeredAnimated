package com.igniva.staggeredanimated.model;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by igniva-android-21 on 16/6/17.
 */

public class GallaryItemPojo {
    ArrayList<ItemObjects> galaryList ;
    ArrayList<ItemObjects> gamesList;
    ArrayList<ItemObjects> fanstasyList;
    ArrayList<ItemObjects> abstractList;
    ArrayList<ItemObjects> foodList;
    private static GallaryItemPojo gallaryItemPojo;

    private GallaryItemPojo gallaryItemPojo(){
        return gallaryItemPojo;
    }

    public static GallaryItemPojo getInstance(){
        if(gallaryItemPojo == null){
            gallaryItemPojo = new GallaryItemPojo();
        }
        return gallaryItemPojo;
    }
    public ArrayList<ItemObjects> getGalaryList() {
        return galaryList;
    }

    public void setGalaryList(ArrayList<ItemObjects> galaryList) {
        this.galaryList = galaryList;
    }

    public ArrayList<ItemObjects> getFoodList() {
        return foodList;
    }

    public void setFoodList(ArrayList<ItemObjects> foodList) {
        this.foodList = foodList;
    }

    public ArrayList<ItemObjects> getAbstractList() {
        return abstractList;
    }

    public void setAbstractList(ArrayList<ItemObjects> abstractList) {
        this.abstractList = abstractList;
    }

    public ArrayList<ItemObjects> getFanstasyList() {
        return fanstasyList;
    }

    public void setFanstasyList(ArrayList<ItemObjects> fanstasyList) {
        this.fanstasyList = fanstasyList;
    }

    public ArrayList<ItemObjects> getGamesList() {
        return gamesList;
    }

    public void setGamesList(ArrayList<ItemObjects> gamesList) {
        this.gamesList = gamesList;
    }
}
