package com.dkstuff.random.thegame;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import java.util.Random;

public class MainActivity extends Activity implements OnDragListener, View.OnLongClickListener {

    private static final String TAG = "theGameActivity";
    public static final String PREFS_NAME = "theGamePrefs";

    //card deck
    public int deckSize = 98;
    public int deckPosition = 0;
    public boolean easyMode = false;
    private Cards[] cards = new Cards[deckSize];
    private boolean cardsCreated = false;
    public Cards movedCard;
    public Cards[] playersCards;
    private int movingId;

    private Player player1 = new Player();
    private Foundations onehundred_one = new Foundations();
    private Foundations onehundred_two = new Foundations();
    private Foundations zero_one = new Foundations();
    private Foundations zero_two = new Foundations();

    //timer for how long you have played
    public long startTime = 0L;
    long timeInMilliseconds = 0L;

    private int turnsPlayed = 0;
    private int remainingCards = deckSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setGameDefaults();

        //register a long click listener for the balls
        findViewById(R.id.hand_card_1).setOnLongClickListener(this);
        findViewById(R.id.hand_card_2).setOnLongClickListener(this);
        findViewById(R.id.hand_card_3).setOnLongClickListener(this);
        findViewById(R.id.hand_card_4).setOnLongClickListener(this);
        findViewById(R.id.hand_card_5).setOnLongClickListener(this);

        //register drag event listeners for the target layout containers
        findViewById(R.id.stock_cards).setOnDragListener(this);
        findViewById(R.id.foundation_piles).setOnDragListener(this);

        /* End Turn. */
        findViewById(R.id.end_turn_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //check hand size
                while (player1.getEmptySlots() > 0 && (deckSize != deckPosition)) {
                    //draw card
                    drawCard(player1);

                    //set to view
                    playersCards = player1.getPlayerCards();
                    setValuesToStock();
                }

                //check that there are any valid plays left; if none left alert the player.
                if (!checkForValidPlay(player1)) {
                    Log.d("TAG", "END THE GAME");
                    alertMessage();
                }

                //increase turn count
                increaseTurn();
            }
        });

        initializeNewGame();
        //this starts the game
        startNewGame();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    /**
     * Set the game defaults determined by the SharedPreferences
     */
    private void setGameDefaults() {
        // Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        int gameDifficulty = settings.getInt("gameDifficulty", 3);

        //Reset the rules based on preferences
        switch (gameDifficulty) {
            case 3:
                Log.d(TAG, "NORMAL");
                deckSize = 99;
                easyMode = false;
                break;

            case 2:
                Log.d(TAG, "Quick");
                deckSize = 49;
                easyMode = false;
                break;

            case 1:
                Log.d(TAG, "Easy");
                deckSize = 9; //48
                easyMode = true;
                break;
        }

        //set the card object based on the new deck size
        cards = new Cards[deckSize];

        //Set the foundation piles text.
        String foundationPileText = deckSize + 1 + " " + getString(R.string.foundation_pile_down);
        TextView t = (TextView) findViewById(R.id.hundred_one_key);
        t.setText(foundationPileText);
        t = (TextView) findViewById(R.id.hundred_two_key);
        t.setText(foundationPileText);
    }

    /**
     * Takes the player's cards and sets the visible displays
     */
    private void setValuesToStock() {
        //spot 1
        TextView localTextView = (TextView) findViewById(R.id.hand_card_1);
        localTextView.setText(String.valueOf(playersCards[0].getValue()));
        localTextView.setVisibility(View.VISIBLE);

        //spot 2
        localTextView = (TextView) findViewById(R.id.hand_card_2);
        localTextView.setText(String.valueOf(playersCards[1].getValue()));
        localTextView.setVisibility(View.VISIBLE);

        //spot 3
        localTextView = (TextView) findViewById(R.id.hand_card_3);
        localTextView.setText(String.valueOf(playersCards[2].getValue()));
        localTextView.setVisibility(View.VISIBLE);

        //spot 4
        localTextView = (TextView) findViewById(R.id.hand_card_4);
        localTextView.setText(String.valueOf(playersCards[3].getValue()));
        localTextView.setVisibility(View.VISIBLE);

        //spot 5
        localTextView = (TextView) findViewById(R.id.hand_card_5);
        localTextView.setText(String.valueOf(playersCards[4].getValue()));
        localTextView.setVisibility(View.VISIBLE);
    }

    /**
     * Menu Options
     *
     * @param item MenuItem selected
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new_game:
                startNewGame();

                return true;

            case R.id.action_rules:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Log.d(TAG, "Rules");
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //executed when a card is clicked
    @Override
    public boolean onLongClick(View imageView) {
        //todo: get the actual Card object being moved - use the imageView.getId to get the card and associate it to the image - magic
        //Log.d(TAG, ""+imageView.getId());
        movingId = imageView.getId();
        //spot 1
        if (imageView.getId() == R.id.hand_card_1) {
            movedCard = playersCards[0];
        }
        //spot 2
        else if (imageView.getId() == R.id.hand_card_2) {
            movedCard = playersCards[1];
        }
        //spot 3
        else if (imageView.getId() == R.id.hand_card_3) {
            movedCard = playersCards[2];
        }
        //spot 4
        else if (imageView.getId() == R.id.hand_card_4) {
            movedCard = playersCards[3];
        }
        //spot 5
        else if (imageView.getId() == R.id.hand_card_5) {
            movedCard = playersCards[4];
        }

        if (movedCard == null || movedCard.getValue() == 0) {
            return false;
        }
        //Log.d(TAG, "Moving Card Value: " + movedCard.getValue());

        //create clip data holding data of the type MIMETYPE_TEXT_PLAIN
        ClipData clipData = ClipData.newPlainText("", "");

        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(imageView);
        /*start the drag - contains the data to be dragged,
          metadata for this data and callback for drawing shadow*/
        imageView.startDrag(clipData, shadowBuilder, imageView, 0);

        //we're dragging the shadow so make the view invisible
        imageView.setVisibility(View.INVISIBLE);
        return true;
    }

    // called when the ball starts to be dragged
    // used by top and bottom layout containers
    @Override
    public boolean onDrag(View receivingLayoutView, DragEvent dragEvent) {
        //this looks to be the starting position
        View draggedImageView = (View) dragEvent.getLocalState();

        // Handles each of the expected events
        switch (dragEvent.getAction()) {

            case DragEvent.ACTION_DRAG_STARTED:
                // Determines if this View can accept the dragged data
                if (dragEvent.getClipDescription()
                        .hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                    // returns true to indicate that the View can accept the dragged data.
                    return true;
                }

                // Returns false. During the current drag and drop operation, this View will
                // not receive events again until ACTION_DRAG_ENDED is sent.
                return false;

            case DragEvent.ACTION_DROP:

                //Get the Card Value
                if (movedCard == null) {
                    return false;
                }
                //Log.d(TAG, "Moving to: "+movedCard.getValue());

                //Get the Foundation Pile (value, direction)
                Foundations foundationPile = checkLocation(dragEvent);

                if (foundationPile != null) {
                    //check the Card Value vs Foundation Pile
                    boolean isValidMove = foundationPile.isValidPlay(movedCard, false);

                    //If Value - move the card
                    if (isValidMove) {
                        //Log.d(TAG, "wasMoved: "+isValidMove);

                        //play the card
                        foundationPile.playCard(movedCard);

                        //update the foundation pile value
                        updateDisplay(movedCard, foundationPile);

                        //remove from hand
                        player1.removeCard(movedCard.getPosition());

                        //set the movedCard to null indicating it is done
                        movedCard = null;

                        // reset to 0
                        if (movingId != 0) {
                            TextView localTextView = (TextView) findViewById(movingId);
                            localTextView.setText("");
                        }

                        setRemainingCards();
                    }
                }

                return false;

            case DragEvent.ACTION_DRAG_ENDED:
                //if the drop was not successful, set the card to visible
                if (!dragEvent.getResult()) {
                    //Log.i(TAG, "setting visible");
                    if (movedCard != null) {
                        draggedImageView.setVisibility(View.VISIBLE);
                    }
                }

                return true;
        }
        return false;
    }

    /**
     * Update the display of a foundation card
     *
     * @param c {Card} Card object that is being played on the foundation
     * @param f {Foundations} Foundation object where the card is being played
     */
    private void updateDisplay(final Cards c, final Foundations f) {
        TextView localTextView = (TextView) findViewById(f.getId());
        localTextView.setText(Integer.toString(c.getValue()));
    }

    /**
     * The steps necessary to initialize the game
     */
    public void initializeNewGame() {
        onehundred_one.setStartingPosition(100);
        onehundred_one.setIncreaseDirection(false);
        onehundred_one.setId(R.id.hundred_one);
        onehundred_one.setEasyMode(easyMode);

        onehundred_two.setStartingPosition(100);
        onehundred_two.setIncreaseDirection(false);
        onehundred_two.setId(R.id.hundred_two);
        onehundred_two.setEasyMode(easyMode);

        zero_one.setStartingPosition(0);
        zero_one.setIncreaseDirection(true);
        zero_one.setId(R.id.zero_one);
        zero_one.setEasyMode(easyMode);

        zero_two.setStartingPosition(0);
        zero_two.setIncreaseDirection(true);
        zero_two.setId(R.id.zero_two);
        zero_two.setEasyMode(easyMode);

        createCards();
    }

    /**
     * Create a new deck of cards.  This should handle setting up the objects
     */
    public void createCards() {
        for (int i = 0; i < deckSize; i++) {
            cards[i] = new Cards();
            cards[i].setValue(1 + i);
            cards[i].setLocation("DECK");
            cards[i].setPosition(i);
        }
        cardsCreated = true;
    }

    /**
     * For a given player, draw cards until the limit is reached
     *
     * @param player the player who is going to draw cards
     */
    public void drawCard(Player player) {
        Cards drawCard = null;
        boolean found = false;

        //check to see if we can keep drawing
        if (deckPosition != deckSize) {
            if (player.keepDrawing()) {
                //get the top card and remove it from the deck
                for (Cards c : cards) {
                    if (!found && c.getPosition() == deckPosition) {
                        drawCard = c;
                        c.setLocation(player.getPlayerID());
                        found = true;
                    }
                }

                //add it to the person's hand
                player.addCard(drawCard);

                //need to move the deck position so we can keep drawing
                deckPosition++;

                //check the limit and repeat
                if (player.keepDrawing()) {
                    drawCard(player);
                }
            } else {
                Log.i(TAG, "ENOUGH CARDS");
            }
        } else {
            Log.i(TAG, "NO MORE CARDS");
        }

    }

    /**
     * The steps necessary to start a new game
     * * Reset the player(s) hands
     * * Reset the played decks
     * * Shuffle the deck
     */
    public void startNewGame() {
        //player(s)
        player1.resetGame();

        //board
        onehundred_one.resetGame();
        onehundred_two.resetGame();
        zero_one.resetGame();
        zero_two.resetGame();

        //set the labels
        String deckDown = deckSize + 1 + "";
        TextView localTextView = (TextView) findViewById(R.id.hundred_one);
        localTextView.setText(deckDown);

        //spot 2
        localTextView = (TextView) findViewById(R.id.hundred_two);
        localTextView.setText(deckDown);

        //spot 3
        localTextView = (TextView) findViewById(R.id.zero_one);
        localTextView.setText("0");

        //spot 4
        localTextView = (TextView) findViewById(R.id.zero_two);
        localTextView.setText("0");


        //deck
        shuffleDeck(cards);

        //reset counts
        turnsPlayed = 0;
        remainingCards = deckSize + 1;

        //start the play counter
        startTime = System.currentTimeMillis();//SystemClock.uptimeMillis();

        //reset last few things
        setRemainingCards();
        findViewById(R.id.end_turn_btn).performClick();
    }

    /**
     * Shuffle the deck of cards and reset the game
     *
     * @param ar array of cards
     */
    public void shuffleDeck(Cards[] ar) {
        //if cards haven't been created do that now.
        if (!cardsCreated) {
            createCards();
        }

        //shuffle the cards by putting them in new position
        Random rnd = new Random(); //ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index].getPosition();
            //ar[index] = ar[i];
            ar[index].setPosition(ar[i].getPosition());
            ar[i].setLocation("DECK");
            ar[i].setPosition(a);
        }
    }

    /**
     * Check the location of the card vs the playable spots\
     *
     * @param dragEvent the card that the player is moving around
     * @return the spot that the card is likely to be played at
     */
    public Foundations checkLocation(final DragEvent dragEvent) {
        final View onehundred_one_view = findViewById(R.id.hund_one_lay);
        final View onehundred_two_view = findViewById(R.id.hund_two_lay);
        final View zero_one_view = findViewById(R.id.zero_one_lay);
        final View zero_two_view = findViewById(R.id.zero_two_lay);

        //used to determine the correct foundation to apply the dropped card to
        if (checkPileMatch(onehundred_one_view, dragEvent)) {
            return onehundred_one;
        } else if (checkPileMatch(onehundred_two_view, dragEvent)) {
            return onehundred_two;
        } else if (checkPileMatch(zero_one_view, dragEvent)) {
            return zero_one;
        } else if (checkPileMatch(zero_two_view, dragEvent)) {
            return zero_two;
        }
        return null;
    }

    /**
     * Determines the percentage of coverage between two views
     *
     * @param playSpot  the static view (where the card will be placed)
     * @param dragEvent the dynamic view (the card that is being moved)
     * @return true if the point exists within the pile; else false
     */
    public boolean checkPileMatch(final View playSpot, final DragEvent dragEvent) {
        //card coordinates
        int x = (int) dragEvent.getX();
        int y = (int) dragEvent.getY();
        //Log.d(TAG, "Card: " + x + " " + y);

        //foundation pile coordinates
        int x21 = playSpot.getLeft();
        int y21 = playSpot.getTop();
        int x22 = playSpot.getRight();
        int y22 = playSpot.getBottom();
        //Log.d(TAG, "Spot: " + x21 + " " + y21 + " " + x22 + " " + y22);

        //math stuff
        if ((x21 <= x) && (x <= x22) && (y21 <= y) && (y <= y22)) {
            return true;
        }

        return false;
    }


    /**
     * Set the remaining deck cards (for display)
     */
    public void setRemainingCards() {
        remainingCards = remainingCards - 1;

        String tempDisplay = getString(R.string.remaining_cards_label) + " " + remainingCards;
        TextView localTextView = (TextView) findViewById(R.id.remaining_cards);
        localTextView.setText(tempDisplay);
    }


    /**
     * Increase the count for the number of turns played (for display)
     */
    public void increaseTurn() {
        turnsPlayed = turnsPlayed + 1;

        String tempDisplay = getString(R.string.turns_played_label) + " " + turnsPlayed;
        TextView localTextView = (TextView) findViewById(R.id.turns_played);
        localTextView.setText(tempDisplay);
    }


    /**
     * Check the player's hand vs the foundation piles to determine if a valid play exists.  If
     * no play exists indicate end game.
     *
     * @return a value of true indicates a valid play exists for the player.
     */
    public boolean checkForValidPlay(Player p) {
        boolean validPlayExists = false;

        for (Cards c : p.getPlayerCards()) {
            if (onehundred_one.isValidPlay(c, true)) {
                validPlayExists = true;
            }
            if (onehundred_two.isValidPlay(c, true)) {
                validPlayExists = true;
            }
            if (zero_one.isValidPlay(c, true)) {
                validPlayExists = true;
            }
            if (zero_two.isValidPlay(c, true)) {
                validPlayExists = true;
            }
        }
        return validPlayExists;
    }

    /**
     * Display a message to the user that contains 'end-game' information.
     */
    public void alertMessage() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("No More Moves!");

        // set dialog message
        alertDialogBuilder
                .setMessage("Click yes to play again!" +
                        "\nCards Played: " + (deckSize-remainingCards) +
                        "\nTurns Played: " + turnsPlayed +
                        "\nTime Played: " + returnTimePlayed())
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Button (Yes) clicked: startNewGame and close
                        startNewGame();
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Button (No) clicked: go to main screen and close
                        //Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                        //startActivity(i);
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    /**
     * Determine the amount of time that the user has been playing for
     *
     * @return message containing the amount of time
     */
    public String returnTimePlayed() {
        String message = "";
        timeInMilliseconds = System.currentTimeMillis() - startTime;

        int secs = (int) (timeInMilliseconds / 1000);
        int mins = secs / 60;
        secs = secs % 60;

        message = "" + mins + ":" + String.format("%02d", secs);

        return message;
    }

    /**
     * Increase the statistics for the game
     *
     * @param win {boolean} if the game was successfully won or not
     */
    private void setGameStatistics(boolean win) {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        int gameDifficulty = settings.getInt("gameDifficulty", 3);

        if (win) {
            int winCount = settings.getInt("winCount" + gameDifficulty, 0);
            editor.putInt("winCount" + gameDifficulty, winCount + 1);
        }

        int gameCount = settings.getInt("gameCount" + gameDifficulty, 0);
        editor.putInt("gameCount" + gameDifficulty, gameCount + 1);

        // Commit the edits
        editor.apply();
    }

    /**
     * Returns the statistics associated with the supplied difficulty.
     *
     * @param gameDifficulty {int} difficulty level (1-3)
     * @return {Object} element[0]=winCount, element[1]=gameCount
     */
    private Object getGameStatistics(int gameDifficulty) {
        Object[] tempStats = new Object[2];
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        //int gameDifficulty = settings.getInt("gameDifficulty", 3);
        int winCount = settings.getInt("winCount" + gameDifficulty, 0);
        int gameCount = settings.getInt("gameCount" + gameDifficulty, 0);

        tempStats[0] = winCount;
        tempStats[1] = gameCount;

        return tempStats;
    }

}
