package com.gmail.andrewjoelbecker.sc2vb.starcraft2voicebuilds.sc2vb;

import android.app.Notification;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by drew on 3/21/15.
*/
public class Base_Activity extends ActionBarActivity {

    Intent i;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.home:
                Log.i("MENU", "Home Clicked");
                i = new Intent(this, MainActivity.class);
                this.startActivity(i);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

