package com.dkstuff.random.thegame;
import java.util.UUID;

/**
 * Player Class
 * This is the information around the player including which cards they currently have.  It also
 * contains the logic necessary to play, remove, add a card to the players hand
 */
public class Player {
    private static final int MAX_CARDS = 8;
    private final UUID idOne = UUID.randomUUID();
    private Cards[] handCards = new Cards[MAX_CARDS];

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

    public String getPlayerID(){
        return idOne.toString();
    }

    public void removeCard(int position){
        handCards[position] = null;
    }

    public Cards[] getPlayerCards(){
        int size = MAX_CARDS-getEmptySlots();
        //Cards[] tempCards = new Cards[size];
        for(Cards c: handCards){
            if(c != null){
                //tempCards[size-1] = c;
                size = size - 1;
            }
        }

        return handCards;
    }

    /**
     * Get the number of empty slots within a player's hand.
     * @return {int} empty spot location within the hand
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
