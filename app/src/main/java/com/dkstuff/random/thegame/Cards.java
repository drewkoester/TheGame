package com.dkstuff.random.thegame;

/**
 * Created by dk8704 on 4/13/16.
 */
public class Cards {
    private int value;
    private String location;
    private int position;

    public void setValue(int value){
        this.value = value;
    }

    public void setCard(String location, int position){
        this.location = location;
        this.position = position;
    }

    public void setLocation(String location){
        this.location = location;
    }
    public void setPosition(int position){
        this.position = position;
    }

    public int getValue(){
        return value;
    }

    public int getPosition(){
        return position;
    }

    public String getLocation() { return location; }
}
