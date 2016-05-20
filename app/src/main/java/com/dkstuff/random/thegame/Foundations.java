package com.dkstuff.random.thegame;

import java.util.ArrayList;

/**
 * class Foundations
 * These are the spots that the player places their cards on.  They contain the logic necessary
 * to determine if the card played is valid.
 */
public class Foundations {
    private int startingPosition;
    private boolean increaseDirection;
    private int currentValue = startingPosition;
    private int id = 0;
    private boolean easyMode = false;

    private final ArrayList<Cards> playedCards = new ArrayList<>();

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

        //todo: fix this since I don't like the try
        try{
            cardPlayed.getValue();
        }catch(Exception e){
            return false;
        }
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

        //Add to ArrayList
        playedCards.add(cardPlayed);
    }

    /**
     * Logic used to reset the game
     */
    public void resetGame(){
        currentValue = startingPosition;
        //playedCards.removeAll(playedCards);
        playedCards.clear();
    }

    public void test_setValue(int cardValue){
        currentValue = cardValue;
    }

}
