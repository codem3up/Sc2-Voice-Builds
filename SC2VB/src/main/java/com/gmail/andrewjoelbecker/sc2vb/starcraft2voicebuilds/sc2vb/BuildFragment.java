package com.gmail.andrewjoelbecker.sc2vb.starcraft2voicebuilds.sc2vb;

import android.app.Fragment;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by drew on 3/22/15.
 */
public class BuildFragment extends Fragment{
    MyBuild build;
    ArrayList<String> strings;
    ArrayAdapter myarrayAdapter;
    int race, countdown = 3;
    ListView lv;
    String name;
    Button stopButton, startButton;
    Handler handler;
    Runnable runnable;
    Boolean running = false, retrieved = false;
    int minutes, seconds, currentNode, colorPointer;
    DecimalFormat f;
    TextView minutesTV, secondsTV;
    View newView, oldView;
    Node temp;
    ImageView background;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_build, container, false);
        Bundle bundle = this.getArguments();
        strings = new ArrayList<String>();
        strings = bundle.getStringArrayList("build");
        race = bundle.getInt("race");
        name = bundle.getString("buildName");

        colorPointer = Color.parseColor("#8030ACD6");
        createBuild();
        initializeThread();

        currentNode = 0;

        myarrayAdapter = new CustomAdapter(getActivity().getBaseContext(), R.layout.row, build.display);

        lv = (ListView)v.findViewById(R.id.listView);
        lv.setAdapter(myarrayAdapter);

        background = (ImageView)v.findViewById(R.id.backgroundView);
        minutesTV = (TextView)v.findViewById(R.id.minutesLabel);
        secondsTV = (TextView)v.findViewById(R.id.secondsLabel);
        stopButton = (Button) v.findViewById(R.id.stop);
        stopButton.setOnClickListener(new View.OnClickListener(){
            @Override
        public void onClick(View v){
                stopTimer();
            }
        });

        startButton = (Button) v.findViewById(R.id.start);
        startButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startTimer();
            }
        });

        if(race == 1){
            background.setImageResource(R.drawable.tbg);
        }
        else if(race == 2){
            background.setImageResource(R.drawable.pbg);
        }
        else if (race == 3){
            background.setImageResource(R.drawable.zbg);
        }



        return v;
    }

    public void stopTimer(){
        running = false;
        seconds--;
    }

    public void startTimer(){
        if (!running) {
            new Thread(runnable).start();
            running = true;
        }
    }


    public void initializeThread(){
        handler = new Handler();
        runnable = new Runnable() {

            @Override
            public void run() {
                while (running) {
                    try {
                        Thread.sleep(727);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (seconds == 59) {
                                seconds = 0;
                                minutes++;
                                minutesTV.setText(String.valueOf(f.format(minutes)));
                                secondsTV.setText(String.valueOf(f.format(seconds)));

                            }
                            f = new DecimalFormat("00");
                            seconds++;
                            secondsTV.setText(String.valueOf(f.format(seconds)));
                            checkForUpdates(minutes, seconds);

                        }
                    });
                }
            }

        };
    }

    public void checkForUpdates(int m, int s){
        if (currentNode < build.getSize()) {
            if(!retrieved) {
                if (currentNode == 0) {
                    newView = lv.getChildAt(currentNode);
                    newView.setBackgroundColor(colorPointer);
                }
                else if(currentNode > 0){
                    oldView = lv.getChildAt(currentNode-1);
                    oldView.setBackgroundColor(Color.TRANSPARENT);
                    newView = lv.getChildAt(currentNode);
                    newView.setBackgroundColor(colorPointer);
                }
                temp = build.getNode(currentNode);
                retrieved = true;
            }
            if(temp.seconds == s && temp.minutes == m){
                playSound();
                currentNode++;
                retrieved = false;
            }

        }
        if(currentNode >= build.getSize()){
            if (countdown > 0){
                countdown--;
            }
            else if(countdown == 0){
                stopTimer();
                final MediaPlayer mp = MediaPlayer.create(getActivity().getBaseContext(), R.raw.buildfinished);
                mp.start();
                countdown--;
            }
        }
    }

    public void playSound(){
        String item = build.getNode(currentNode).item.toLowerCase();

        if (item.equals("barracks")) {
            final MediaPlayer mp = MediaPlayer.create(getActivity().getBaseContext(), R.raw.barracks);
            mp.start();
        }

        if (item.equals("supplydepot")) {
            final MediaPlayer mp = MediaPlayer.create(getActivity().getBaseContext(), R.raw.supplydepot);
            mp.start();
        }


        if (item.equals("armory")) {
            final MediaPlayer mp = MediaPlayer.create(getActivity().getBaseContext(), R.raw.armory);
            mp.start();
        }

        if (item.equals("banshee")) {
            final MediaPlayer mp = MediaPlayer.create(getActivity().getBaseContext(), R.raw.banshee);
            mp.start();
        }

        if (item.equals("battlecruiser")) {
            final MediaPlayer mp = MediaPlayer.create(getActivity().getBaseContext(), R.raw.battlecruiser);
            mp.start();
        }

        if (item.equals("commandcenter")) {
            final MediaPlayer mp = MediaPlayer.create(getActivity().getBaseContext(), R.raw.commandcenter);
            mp.start();
        }

        if (item.equals("engineeringbay")) {
            final MediaPlayer mp = MediaPlayer.create(getActivity().getBaseContext(), R.raw.engineeringbay);
            mp.start();
        }

        if (item.equals("factory")) {
            final MediaPlayer mp = MediaPlayer.create(getActivity().getBaseContext(), R.raw.factory);
            mp.start();
        }

        if (item.equals("ghost")) {
            final MediaPlayer mp = MediaPlayer.create(getActivity().getBaseContext(), R.raw.ghost);
            mp.start();
        }

        if (item.equals("ghostacademy")) {
            final MediaPlayer mp = MediaPlayer.create(getActivity().getBaseContext(), R.raw.ghostacademy);
            mp.start();
        }

        if (item.equals("hellion")) {
            final MediaPlayer mp = MediaPlayer.create(getActivity().getBaseContext(), R.raw.hellion);
            mp.start();
        }

        if (item.equals("marine")) {
            final MediaPlayer mp = MediaPlayer.create(getActivity().getBaseContext(), R.raw.marine);
            mp.start();
        }

        if (item.equals("missileturret")) {
            final MediaPlayer mp = MediaPlayer.create(getActivity().getBaseContext(), R.raw.missileturret);
            mp.start();
        }

        if (item.equals("orbitalcommand")) {
            final MediaPlayer mp = MediaPlayer.create(getActivity().getBaseContext(), R.raw.orbitalcommand);
            mp.start();
        }

        if (item.equals("planetaryfortress")) {
            final MediaPlayer mp = MediaPlayer.create(getActivity().getBaseContext(), R.raw.planetaryfortress);
            mp.start();
        }

        if (item.equals("raven")) {
            final MediaPlayer mp = MediaPlayer.create(getActivity().getBaseContext(), R.raw.raven);
            mp.start();
        }

        if (item.equals("reactor")) {
            final MediaPlayer mp = MediaPlayer.create(getActivity().getBaseContext(), R.raw.reactor);
            mp.start();
        }

        if (item.equals("reaper")) {
            final MediaPlayer mp = MediaPlayer.create(getActivity().getBaseContext(), R.raw.reaper);
            mp.start();
        }

        if (item.equals("refinery")) {
            final MediaPlayer mp = MediaPlayer.create(getActivity().getBaseContext(), R.raw.refinery);
            mp.start();
        }

        if (item.equals("scv")) {
            final MediaPlayer mp = MediaPlayer.create(getActivity().getBaseContext(), R.raw.scv);
            mp.start();
        }

        if (item.equals("sensortower")) {
            final MediaPlayer mp = MediaPlayer.create(getActivity().getBaseContext(), R.raw.sensortower);
            mp.start();
        }

        if (item.equals("starport")) {
            final MediaPlayer mp = MediaPlayer.create(getActivity().getBaseContext(), R.raw.starport);
            mp.start();
        }

        if (item.equals("tank")) {
            final MediaPlayer mp = MediaPlayer.create(getActivity().getBaseContext(), R.raw.tank);
            mp.start();
        }

        if (item.equals("techlab")) {
            final MediaPlayer mp = MediaPlayer.create(getActivity().getBaseContext(), R.raw.techlab);
            mp.start();
        }

        if (item.equals("thor")) {
            final MediaPlayer mp = MediaPlayer.create(getActivity().getBaseContext(), R.raw.thor);
            mp.start();
        }

        if (item.equals("viking")) {
            final MediaPlayer mp = MediaPlayer.create(getActivity().getBaseContext(), R.raw.viking);
            mp.start();
        }

        if (item.equals("widowmine")) {
            final MediaPlayer mp = MediaPlayer.create(getActivity().getBaseContext(), R.raw.widowmine);
            mp.start();
        }
    }

    public void createBuild(){
        build = new MyBuild(race);

        for(String s : strings){
            int first = s.indexOf("{");
            int middle = s.indexOf(":");
            int last = s.indexOf("}");
            build.addNode(s.substring(0, first), Integer.parseInt(s.substring(first + 1, middle)), Integer.parseInt(s.substring(middle + 1, last)));
        }
    }

}
