package com.dkstuff.random.thegame;
import java.util.UUID;
import android.util.Log;
/**
 * Created by dk8704 on 4/14/16.
 */
public class Player {
    private static final int MAX_CARDS = 5;
    UUID idOne = UUID.randomUUID();
    Cards[] handCards = new Cards[MAX_CARDS];

    public void addCard(Cards addCard){
        boolean cardAdded = false;
        for(int i = 0; i < MAX_CARDS; i++){
            if(!cardAdded && handCards[i] == null){
                addCard.setPosition(i);
                handCards[i] = addCard;

                //Log.d("PLAYER", handCards[i].getValue()+" | "+ i);
                cardAdded = true;
            }
        }
    }

    public Cards returnFirstCard(){
        return handCards[0];
    }

    public String getPlayerID(){
        return idOne.toString();
    }

    public void removeCard(int position){
        handCards[position] = null;
    }

    public Cards[] getPlayerCards(){
        return handCards;
    }

    public int getPlayersCardCount(){
        return handCards.length;
    }

    /**
     * Get the number of empty slots within a player's hand.
     * @return
     */
    public int getEmptySlots(){
        int emptySpots = 0;
        for(int i = 0; i < MAX_CARDS; i++){
            if(handCards[i] == null || handCards[i].getValue() == 0){
                emptySpots++;
            }
        }
        return emptySpots;
    }

    /**
     * Function to determine if the player can draw more cards
     * @return {boolean} if the player can draw more cards or not.  True draw more; False indicates stop drawing
     */
    public boolean keepDrawing(){
        boolean drawMore = false;
        if(getEmptySlots() != 0){
            drawMore = true;
        }
        return drawMore;
    }

    /**
     * Logic used to reset the game
     */
    public void resetGame(){
        handCards = new Cards[MAX_CARDS];
    }

}
