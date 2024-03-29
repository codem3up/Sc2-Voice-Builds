package com.gmail.andrewjoelbecker.sc2vb.starcraft2voicebuilds.sc2vb;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.FileOutputStream;
import java.text.DecimalFormat;

/**
 * Class: BuildCreator.java
 * Function: This class allows users to create builds via a timer and threads.  It uses the MyBuild class in order
 * to add Nodes to a build which are then used in other classes to listen to and view a build
 */
public class BuildCreator extends Base_Activity {
    MyBuild build;
    int race, seconds, minutes, lastSecond, lastMinute, setupInitial = 0;
    Spinner structuresSpinner, unitsSpinner;
    String[] structuresArray, unitsArray;
    String string;
    Handler handler;
    Runnable runnable;
    Boolean Running;
    TextView secondsTV, minutesTV;
    CustomAdapter myarrayAdapter;
    ListView lv;
    DecimalFormat formatter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_creator);
      //  getActionBar().setDisplayHomeAsUpEnabled(true);

        AdView adView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("839B631B02A7FF2AC27C272F634E7E3E")
                .build();

        adView.loadAd(adRequest);

        Intent i = getIntent();

        race = i.getIntExtra("race", 0);
        build = new MyBuild(race);

        if(race == 1){
            terranSetup();
        }
        else if(race == 2){
           protossSetup();
        }
        else if (race == 3){
            zergSetup();
        }
        else{
            Toast.makeText(getBaseContext(), "Error Retrieving Race!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save_menu, menu);
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

            case R.id.save:
                saveBuild();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveBuild(){
        final Dialog dialog = new Dialog(BuildCreator.this);
        dialog.setContentView(R.layout.save_dialog);
        dialog.setTitle("Save Build");
        final  EditText nameET = (EditText)dialog.findViewById(R.id.name);
        final EditText descET = (EditText)dialog.findViewById(R.id.desc);
        final EditText createET = (EditText)dialog.findViewById(R.id.creator);
        Button submit = (Button) dialog.findViewById(R.id.button1);
        Button cancel = (Button) dialog.findViewById(R.id.button2);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputString = nameET.getText().toString();
                if (!(inputString.contains("$") || inputString.equals("") || inputString.contains("*"))) {
                    String filename = "";
                    if (race == 1) {
                        filename = "terran.dat";
                    } else if (race == 2) {
                        filename = "protoss.dat";
                    } else if (race == 3) {
                        filename = "zerg.dat";
                    }
                    FileOutputStream outputStream;
                    string = "$";
                    string += inputString;
                    string += "*" + descET.getText() + "*" + createET.getText() + "\n";
                    string += build.toString();

                    try {
                        outputStream = openFileOutput(filename, MODE_APPEND);
                        outputStream.write(string.getBytes());
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getBaseContext(), "Saved", Toast.LENGTH_LONG).show();

                    string = "";
                    dialog.dismiss();
                }
                else{
                    Toast.makeText(getBaseContext(), "Invalid Name", Toast.LENGTH_LONG).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        /*
        AlertDialog.Builder editAlert = new AlertDialog.Builder(this);

        editAlert.setTitle("Save");
        editAlert.setMessage("Enter Name of Build");
        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        input.setLayoutParams(lp);
        editAlert.setView(findViewById(R.layout.save_dialog));
        editAlert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String inputString = new String(input.getText().toString());
                if (!(inputString.contains("$") || inputString.equals(""))) {
                    String filename = "";
                    if (race == 1) {
                        filename = "terran.dat";
                    } else if (race == 2) {
                        filename = "protoss.dat";
                    } else if (race == 3) {
                        filename = "zerg.dat";
                    }
                    FileOutputStream outputStream;
                    string = "$";
                    string += inputString + "\n";
                    string += build.toString();

                    try {
                        outputStream = openFileOutput(filename, MODE_APPEND);
                        outputStream.write(string.getBytes());
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getBaseContext(), "Saved", Toast.LENGTH_LONG).show();

                    string = "";
                }
                else{
                    Toast.makeText(getBaseContext(), "Invalid Name", Toast.LENGTH_LONG).show();
                }

            }
        });
        editAlert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        editAlert.show();*/
    }
    //Perform basic zerg Setup
    public void zergSetup(){
        build = new MyBuild(race);
        setupThread();
        setupZergUI();
    }

    //Perform basic protoss Setup
    public void protossSetup(){
        SharedPreferences settings = getSharedPreferences("Settings", 0);
        boolean mode = settings.getBoolean("Mode", false);
        build = new MyBuild(race);
        setupThread();
        setupProtossUI();
        if(mode){
            setupRealTimeUI();
        }
        else{
            setupStandardUI();
        }
    }

    //Perform basic terran Setup
    public void terranSetup(){
        SharedPreferences settings = getSharedPreferences("Settings", 0);
        boolean mode = settings.getBoolean("Mode", false);
        build = new MyBuild(race);
        setupThread();
        setupTerranUI();
        if(mode){
            setupRealTimeUI();
        }
        else{
            setupStandardUI();
        }
    }

    public void setupRealTimeUI(){
        LinearLayout v = (LinearLayout)findViewById(R.id.realTime);
        LinearLayout v2 = (LinearLayout)findViewById(R.id.standard);
        v.setVisibility(View.VISIBLE);
        v2.setVisibility(View.GONE);
    }

    public void setupStandardUI(){
        LinearLayout v = (LinearLayout)findViewById(R.id.standard);
        LinearLayout v2 = (LinearLayout)findViewById(R.id.realTime);
        NumberPicker minutes = (NumberPicker)findViewById(R.id.minutesPicker);
        NumberPicker seconds = (NumberPicker)findViewById(R.id.secondsPicker);
        minutes.setMinValue(0);
        minutes.setMaxValue(60);
        seconds.setMinValue(0);
        seconds.setMaxValue(59);
        v.setVisibility(View.VISIBLE);
        v2.setVisibility(View.GONE);
    }


    //Set up UI components for zerg race
    public void setupZergUI(){
        TextView racePrompt = (TextView)findViewById(R.id.createRacePrompt);
        racePrompt.setText("Create Zerg Build");
    }

    //Set up UI components for protoss race
    public void setupProtossUI(){
        TextView racePrompt = (TextView)findViewById(R.id.createRacePrompt);
        racePrompt.setText("Create Protoss Build");

        myarrayAdapter = new CustomAdapter(this, R.layout.row, build.display);

        lv = (ListView)findViewById(R.id.buildListView);
        lv.setAdapter(myarrayAdapter);

        structuresSpinner = (Spinner) findViewById(R.id.structuresSpinner);
        structuresArray = new String[17];
        structuresArray[0] = "Structures";
        structuresArray[1] = "Gateway";
        structuresArray[2] = "Robotics Facility";
        structuresArray[3] = "Stargate";
        structuresArray[4] = "Pylon";
        structuresArray[5] = "Nexus";
        structuresArray[6] = "Cybernetics Core";
        structuresArray[7] = "Fleet Beacon";
        structuresArray[8] = "Assimilator";
        structuresArray[9] = "Forge";
        structuresArray[10] = "Fleet Beacon";
        structuresArray[11] = "Twilight Council";
        structuresArray[12] = "Photon Cannon";
        structuresArray[13] = "Templar Archives";
        structuresArray[14] = "Warpgate Research";
        structuresArray[15] = "Robotics Bay";
        structuresArray[16] = "Dark shrine";
        ArrayAdapter structuresAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, structuresArray);
        structuresSpinner.setAdapter(structuresAdapter);

        //Setup Unit Spinner
        unitsSpinner = (Spinner) findViewById(R.id.unitsSpinner);
        unitsArray = new String[19];
        unitsArray[0] = "Units---";
        unitsArray[1] = "Probe";
        unitsArray[2] = "Zealot";
        unitsArray[3] = "Stalker";
        unitsArray[4] = "Sentry";
        unitsArray[5] = "Observer";
        unitsArray[6] = "Immortal";
        unitsArray[7] = "Warp Prism";
        unitsArray[8] = "Colossus";
        unitsArray[9] = "Pheonix";
        unitsArray[10] = "Void Ray";
        unitsArray[11] = "High Templar";
        unitsArray[12] = "Dark Templar";
        unitsArray[13] = "Archon";
        unitsArray[14] = "Carrier";
        unitsArray[15] = "Mother Ship Core";
        unitsArray[16] = "MotherShip";
        unitsArray[17] = "2 Workers on Gas";
        unitsArray[18] = "3 workers on gas";

        ArrayAdapter unitsAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, unitsArray);
        unitsSpinner.setAdapter(unitsAdapter);

    }

    public void displayOptions(int pos){
        stop();
        Log.i("TAG", "DISPLAY");
        AlertDialog.Builder editAlert = new AlertDialog.Builder(this);

        editAlert.setTitle("Delete Node");
        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        input.setLayoutParams(lp);
        editAlert.setView(input);
        editAlert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                build.removeNode(which);
                updateUI();
                lastSecond--;
                start();
            }
        });
        editAlert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                start();
            }
        });
        editAlert.show();
    }

    //Set up UI components for terran race
    public void setupTerranUI(){
        //Setup Structure Spinner
        TextView racePrompt = (TextView)findViewById(R.id.createRacePrompt);
        racePrompt.setText("Create Terran Build");

        myarrayAdapter = new CustomAdapter(this, R.layout.row, build.display);

        lv = (ListView)findViewById(R.id.buildListView);
        lv.setAdapter(myarrayAdapter);

        structuresSpinner = (Spinner) findViewById(R.id.structuresSpinner);
        structuresArray = new String[16];
        structuresArray[0] = "Structures";
        structuresArray[1] = "Barracks";
        structuresArray[2] = "Factory";
        structuresArray[3] = "Starport";
        structuresArray[4] = "Supply Depot";
        structuresArray[5] = "Command Center";
        structuresArray[6] = "Orbital Command";
        structuresArray[7] = "Planetary Fortress";
        structuresArray[8] = "Refinery";
        structuresArray[9] = "Engineering Bay";
        structuresArray[10] = "Armory";
        structuresArray[11] = "Ghost Academy";
        structuresArray[12] = "Missile Turret";
        structuresArray[13] = "Sensor Tower";
        structuresArray[14] = "Reactor";
        structuresArray[15] = "Tech lab";
        ArrayAdapter structuresAdapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, structuresArray);
        structuresSpinner.setAdapter(structuresAdapter);

        //Setup Unit Spinner
        unitsSpinner = (Spinner) findViewById(R.id.unitsSpinner);
        unitsArray = new String[15];
        unitsArray[0] = "Units";
        unitsArray[1] = "SCV";
        unitsArray[2] = "Marine";
        unitsArray[3] = "Marauder";
        unitsArray[4] = "Reaper";
        unitsArray[5] = "Ghost";
        unitsArray[6] = "Hellion";
        unitsArray[7] = "Tank";
        unitsArray[8] = "Widow Mine";
        unitsArray[9] = "Thor";
        unitsArray[10] = "Banshee";
        unitsArray[11] = "Viking";
        unitsArray[12] = "Raven";
        unitsArray[13] = "BattleCruiser";
        unitsArray[14] = "2 Workers on Gas";
        unitsArray[14] = "3 workers on gas";

        ArrayAdapter unitsAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, unitsArray);
        unitsSpinner.setAdapter(unitsAdapter);

    }

    public void updateUI(){
        myarrayAdapter.notifyDataSetChanged();
    }

    public void start(View v){
        if (!Running) {
            new Thread(runnable).start();
            Running = true;
        }
    }

    public void start(){
        if(!Running){
            new Thread(runnable).start();
            Running = true;
        }
    }

    public void stop(View v){
        Running = false;
        seconds--;
    }

    public void stop(){
        Running = false;
        seconds--;
    }

    public void setupThread(){
        secondsTV = (TextView)findViewById(R.id.secondsOutput);
        minutesTV = (TextView)findViewById(R.id.minutesLabel);
        Running = false;

        handler = new Handler();
        formatter = new DecimalFormat("00");
        runnable = new Runnable(){

            @Override
            public void run(){
                while(Running){
                    try{
                        Thread.sleep(727);}
                    catch(InterruptedException e){
                        e.printStackTrace();
                    }
                    handler.post(new Runnable(){
                        @Override
                        public void run(){
                            if (seconds == 60)
                            {
                                seconds = 0;
                                minutes++;
                                minutesTV.setText(String.valueOf(formatter.format(minutes)));

                            }
                            seconds++;
                            secondsTV.setText(String.valueOf(formatter.format(seconds)));

                        }
                    });
                }
            }


        };
    }

    public void addStructure(View v) {
        if (setupInitial == 0) {
            Log.i("TAG", "LOL");
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    displayOptions(position);
                }
            });
        }
            int position = structuresSpinner.getSelectedItemPosition();
            if (position > 0 && (seconds > 0 || minutes > 0) && (lastSecond != seconds || lastMinute != minutes)) {
                build.addNode(structuresArray[position], minutes, seconds);
                updateUI();
                lastSecond = seconds;
                lastMinute = minutes;
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        displayOptions(position);
                    }
                });

            }
        }
    public void addUnit(View v){
        if (setupInitial == 0) {
            Log.i("TAG", "LOL");
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    displayOptions(position);
                }
            });
        }        if (setupInitial == 0) {
            Log.i("TAG", "LOL");
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    displayOptions(position);
                }
            });
        }
        int position = unitsSpinner.getSelectedItemPosition();
        if(position > 0 && (seconds > 0 || minutes > 0) && (lastSecond != seconds || lastMinute != minutes)) {
            build.addNode(unitsArray[position], minutes, seconds);
            updateUI();
            lastSecond = seconds;
            lastMinute = minutes;
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    displayOptions(position);
                }
            });
        }
    }

}