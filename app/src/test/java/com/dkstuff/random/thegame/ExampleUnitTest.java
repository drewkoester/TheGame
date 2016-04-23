package com.dkstuff.random.thegame;

import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import android.content.SharedPreferences;
import android.content.Context;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {
    @Mock
    Context mMockContext;

    MainActivity myObjectUnderTest = new MainActivity();

    @Test
    public void validateReturnTimePlayed() {
        myObjectUnderTest.startTime = System.currentTimeMillis();
        assertEquals(myObjectUnderTest.returnTimePlayed(), "0:00");

        try {
            Thread.sleep(1000);                 //1000 milliseconds is one second.
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
        assertEquals(myObjectUnderTest.returnTimePlayed(), "0:01");
    }

//    @Test
//    public void testNewGame(){
//        myObjectUnderTest.startNewGame();
//
//    }
}