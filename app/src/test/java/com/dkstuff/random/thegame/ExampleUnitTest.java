package com.dkstuff.random.thegame;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import android.content.Context;
import android.util.Log;
import org.junit.Before;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;
//import android.support.test.runner.AndroidJUnit4;
//import android.support.test.runner.AndroidJUnitRunner;
import android.test.ActivityInstrumentationTestCase2;
import com.dkstuff.random.thegame.MainActivity;
import com.dkstuff.random.thegame.R;
//import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;
import android.widget.Button;
import android.widget.TextView;

import com.dkstuff.random.thegame.MainActivity;
import com.dkstuff.random.thegame.R;



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
        for(Cards c: myObjectUnderTest.cards){
            assertEquals("DECK", c.getLocation());
            assertNotEquals(0, c.getValue());
            assertNotEquals(100, c.getValue());
        }
    }

    @Test
    public void testValidateReturnTimePlayed() throws Exception {
        myObjectUnderTest.startTime = System.currentTimeMillis();
        assertEquals(myObjectUnderTest.returnTimePlayed(), "0:00");

        try {
            Thread.sleep(1000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        assertEquals("0:01", myObjectUnderTest.returnTimePlayed());
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