package com.dkstuff.random.thegame;

/**
 * constructor Cards
 * This is the basic playing cards that contains the values necessary for it
 * value: The value {int} of the card.
 * location: Where the card is currently location
 * position: The spot {int} that the card is within the deck (this handles the shuffle)
 */
public class Cards {
    private int value;
    private String location;
    private int position;

    public void setValue(int value){
        this.value = value;
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
