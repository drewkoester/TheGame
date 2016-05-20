package com.dkstuff.random.thegame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.widget.RadioButton;
import android.view.View;

public class SettingsActivity extends Activity {
    private static final String PREFS_NAME = "theGamePrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setPreferences();

        /* MainScreen. */
        findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
    }

    /**
     * Set the defaults based on the saved preferences
     */
    private void setPreferences() {
        // Restore preferences
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        int gameDifficulty = settings.getInt("gameDifficulty", 3);
        boolean displayAd = settings.getBoolean("displayAd", true);

        /* Used for Game Difficulty */
        RadioButton r = (RadioButton) findViewById(R.id.mode_normal);
        if (gameDifficulty == 2) {
            r = (RadioButton) findViewById(R.id.mode_quick);
        } else if (gameDifficulty == 1) {
            r = (RadioButton) findViewById(R.id.mode_easy);
        }
        r.setChecked(true);

        /* Used for displaying Ads */
        r = (RadioButton) findViewById(R.id.ad_yes);
        if (!displayAd) {
            r = (RadioButton) findViewById(R.id.ad_no);
        }
        r.setChecked(true);
    }

    /**
     * Save the setting when a RadioButton is clicked
     *
     * @param view the view which executed the click
     */
    public void onRadioButtonClicked(View view) {
        int difficulty = 3;
        boolean displayAds = true;
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            /* For Game Difficulty */
            case R.id.mode_normal:
                if (checked)
                    difficulty = 3;
                break;
            case R.id.mode_quick:
                if (checked)
                    difficulty = 2;
                break;
            case R.id.mode_easy:
                if (checked)
                    difficulty = 1;
                break;
            /* For Ad Settings */
            case R.id.ad_yes:
                if (checked)
                    displayAds = true;
                break;
            case R.id.ad_no:
                if (checked)
                    displayAds = false;
                break;
        }

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("gameDifficulty", difficulty);
        editor.putBoolean("displayAd", displayAds);

        // Commit the edits!
        editor.apply();
    }
}
