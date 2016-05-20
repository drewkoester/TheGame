package com.dkstuff.random.thegame;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import android.content.Context;

import org.junit.Before;
//import android.support.test.runner.AndroidJUnit4;
//import android.support.test.runner.AndroidJUnitRunner;
//import android.support.test.runner.AndroidJUnit4;

import com.dkstuff.random.thegame.MainActivity;


import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {
    MainActivity myObjectUnderTest = new MainActivity();

    @Mock
    Context mMockContext;

    @Before
    public void init(){
        myObjectUnderTest.initializeNewGame();
        //myObjectUnderTest.startNewGame();

        //ensure that all cards are within the DECK
        for(Cards c: MainActivity.cards){
            assertEquals("DECK", c.getLocation());
            assertNotEquals(0, c.getValue());
            assertNotEquals(100, c.getValue());
        }
    }

    @Test
    public void testValidateReturnTimePlayed() throws Exception {
        MainActivity.startTime = System.currentTimeMillis();
        assertEquals(MainActivity.returnTimePlayed(), "0:00");

        try {
            Thread.sleep(1000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        assertEquals("0:01", MainActivity.returnTimePlayed());
    }

//    @Test
//    public void testNewGame(){
//        //ensure that we are playing with 98 cards
//        assertEquals(98, myObjectUnderTest.cards.length);
//        assertEquals(98, myObjectUnderTest.remainingCards);
//
//        //"play" 75 cards
//        for(Cards c : myObjectUnderTest.cards){
//            if(c.getPosition() < 75){
//                c.setLocation("Not DECK");
//                //myObjectUnderTest.setRemainingCards();
//            }
//        }
//
//        assertEquals(100, myObjectUnderTest.onehundred_one.getCurrentValue());
//        //assert size
//        //assertEquals(myObjectUnderTest.remainingCards, 98);
//    }
}