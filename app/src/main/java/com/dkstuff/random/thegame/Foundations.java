package com.dkstuff.random.thegame;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by dk8704 on 4/14/16.
 */
public class Foundations {
    int startingPosition;
    boolean increaseDirection;
    int currentValue = startingPosition;
    UUID idOne = UUID.randomUUID();
    int id = 0;
    boolean easyMode = false;

    ArrayList<Cards> playedCards = new ArrayList<>();

    public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }

    public void setStartingPosition(int startingPosition){
        this.startingPosition = startingPosition;
    }
    public void setIncreaseDirection(boolean increaseDirection){
        this.increaseDirection = increaseDirection;
    }
    public void setEasyMode(boolean easyMode){
        this.easyMode = easyMode;
    }

    public UUID getIdOne(){
        return idOne;
    }

    public int getCurrentValue(){
        return currentValue;
    }
    /**
     * Determine if a card being played is a legal move
     * @param cardPlayed the card which the user is attempting to play
     * @param eval {boolean} if evaluating only do not set the value
     * @return true if the card played is legal and false if the play is illegal
     */
    public boolean isValidPlay(Cards cardPlayed, boolean eval){
        boolean setValue = false;
        //ascending (1,2,3)
        if(increaseDirection){
            if(cardPlayed.getValue() > currentValue){
                setValue = true;
            }else if(cardPlayed.getValue() == currentValue-10){
                setValue = true;
            }else if(easyMode
                    && (currentValue-cardPlayed.getValue())%10==0
                    && cardPlayed.getValue() < currentValue){
                setValue = true;
            }
        }
        //descending (99, 98, etc)
        else{
            if(cardPlayed.getValue() < currentValue){
                setValue = true;
            }else if(cardPlayed.getValue() == currentValue+10){
                setValue = true;
            }else if(easyMode
                    && cardPlayed.getValue() > currentValue
                    && (cardPlayed.getValue()-currentValue)%10 == 0){
                setValue = true;
            }
        }

        if(setValue && !eval){
            //update currentValue
            currentValue = cardPlayed.getValue();
            playedCards.add(cardPlayed);
        }

        return setValue;
    }

    public void playCard(Cards cardPlayed){
        //set currentValue
        this.currentValue = cardPlayed.getValue();

        //set the location (not sure why)
        cardPlayed.setLocation(idOne.toString());

        //Add to ArrayList
        playedCards.add(cardPlayed);
    }

    /**
     * Logic used to reset the game
     */
    public void resetGame(){
        currentValue = startingPosition;
        playedCards.removeAll(playedCards);
    }

}
