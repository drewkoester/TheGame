package com.dkstuff.random.thegame;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

//import com.example.android.activityinstrumentation.MainActivity;
//import com.example.android.activityinstrumentation.R;
import com.dkstuff.random.thegame.MainActivity;


/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ActivityInstrumentationTestCase2<MainActivity> {
    public ApplicationTest() {
        //super(Application.class);
        super("com.dkstuff.random.thegame", MainActivity.class);
    }

    /**
     * Start the activity
     */
    public void setUp() {
        Activity activity = getActivity();

        //test that the initial setup is correct for the foundation cards
        assertEquals(100, MainActivity.onehundred_one.getCurrentValue());
        assertEquals(100, MainActivity.onehundred_two.getCurrentValue());
        assertEquals(0, MainActivity.zero_one.getCurrentValue());
        assertEquals(0, MainActivity.zero_two.getCurrentValue());
    }

    /**
     * Finish the activity
     */
    public void tearDown() {
        getActivity().finish();
    }

    public void testGame() {
        /** test loss **/
        MainActivity.onehundred_one.test_setValue(0);
        MainActivity.onehundred_two.test_setValue(0);
        MainActivity.zero_one.test_setValue(100);
        MainActivity.zero_two.test_setValue(100);
        assertFalse(MainActivity.checkForValidPlay(MainActivity.player1));


        /** test win **/
        MainActivity.onehundred_one.test_setValue(100);
        MainActivity.onehundred_two.test_setValue(100);
        MainActivity.zero_one.test_setValue(0);
        MainActivity.zero_two.test_setValue(0);
        Foundations foundationPileTest = MainActivity.zero_one;

        //"play" 80 cards
        dumpPlayingCards(85);

        //check how many cards the player has
        assertEquals(8, MainActivity.player1.getPlayerCards().length);


        //play 4 cards
        playPlayersCard(4, foundationPileTest);
        assertEquals(4, MainActivity.player1.getEmptySlots());

        //draw cards
        drawCardsTest();

        assertEquals(97, MainActivity.deckPosition); //start 85, hand 8, draw 4
        assertEquals(8, MainActivity.player1.getPlayerCards().length);

        //check that there are any valid plays left; if none left alert the player.
        assertTrue(MainActivity.checkForValidPlay(MainActivity.player1));

        //verify that our foundationPile's value is "4"
        assertEquals(4, foundationPileTest.getCurrentValue());

        //"play" cards - 4
        Cards[] testCards = MainActivity.player1.getPlayerCards();
        assertTrue(foundationPileTest.isValidPlay(testCards[4], false));
        assertTrue(foundationPileTest.isValidPlay(testCards[5], false));
        assertTrue(foundationPileTest.isValidPlay(testCards[6], false));
        assertTrue(foundationPileTest.isValidPlay(testCards[7], false));

        //remove cards manually
        playPlayerIndividualCard(4, foundationPileTest);
        playPlayerIndividualCard(5, foundationPileTest);
        playPlayerIndividualCard(6, foundationPileTest);
        playPlayerIndividualCard(7, foundationPileTest);

        //at this point we have 4 empty spots, but only 2 cards
        assertEquals(2, MainActivity.deckSize - MainActivity.deckPosition);
        drawCardsTest();
        assertEquals(2, MainActivity.player1.getEmptySlots());

        //Cards in hand: 94-99
        playPlayersCard(8, foundationPileTest);

        //All cards have been playes so alert
        assertFalse(MainActivity.checkForValidPlay(MainActivity.player1));
    }

    /**
     * Dups a certain number of cards within the deck
     * @param cardCount {int} number of cards to dump from the deck
     */
    private void dumpPlayingCards(int cardCount) {
        for (Cards c : MainActivity.cards) {
            if (c != null) {
                if (c.getPosition() < cardCount) {
                    c.setLocation("Not DECK");
                    MainActivity.remainingCards = MainActivity.remainingCards - 1;
                    MainActivity.deckPosition++;
                }
                //put the cards into a standard order (i.e. un-shuffle the deck)
                c.setValue(1 + c.getPosition());
            }
        }
    }

    /**
     * Play a certain number of cards from the player's hand
     * @param cardsToPlay {int} cards to play
     * @param foundationPileTest {Foundations} pile to play the cards on
     */
    private void playPlayersCard(int cardsToPlay, Foundations foundationPileTest) {
        int testPlayCards = 0;
        for (Cards c : MainActivity.player1.getPlayerCards()) {
            if (c != null) {
                //"play" an 4 actual cards
                if (testPlayCards < cardsToPlay) {
                    //check the Card Value vs Foundation Pile
                    boolean isValidMove = foundationPileTest.isValidPlay(c, false);
                    assertTrue(isValidMove);

                    //play the card
                    foundationPileTest.playCard(c);

                    //remove from hand
                    MainActivity.player1.removeCard(c.getPosition());

                    //Log.d("Test-Cards", c.getValue()+"");
                    testPlayCards = testPlayCards + 1;
                }
            }
        }
    }

    /**
     * Draw a card to the player's hand
     */
    private void drawCardsTest() {
        while (MainActivity.player1.getEmptySlots() > 0 && (MainActivity.deckSize != MainActivity.deckPosition)) {
            //draw card
            MainActivity.drawCard(MainActivity.player1);
        }
    }

    /**
     * Play an individual card within the player's hand
     * @param playerCardPosition {int} card position
     * @param foundationPileTest {Foundations} pile to play the card on
     */
    private void playPlayerIndividualCard(int playerCardPosition, Foundations foundationPileTest) {
        Cards[] testCards = MainActivity.player1.getPlayerCards();
        //remove cards manually
        foundationPileTest.playCard(testCards[playerCardPosition]);
        MainActivity.player1.removeCard(testCards[playerCardPosition].getPosition());
    }
}