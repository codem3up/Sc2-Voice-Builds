package com.gmail.andrewjoelbecker.sc2vb.starcraft2voicebuilds.sc2vb;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Class: MyBuild.java
 * Function: This class functions as the backbone for the app and handles all of the build functionality
 *           It allows users to add Nodes to it, it displays said nodes and it stores the display for Items as well
 */
public class MyBuild {
    private ArrayList<Node> build = new ArrayList<Node>();
    ArrayList<DisplayItem> display = new ArrayList<DisplayItem>();
    int race, size = 0;
    DecimalFormat formatter;

    //Create a new build with a race; Terran = 1, Protoss = 2, Zerg = 3
    public MyBuild(int race){
        this.race = race;
        formatter = new DecimalFormat("00");
    }

    public void addNode(String item, int minutes, int seconds){
        display.add(new DisplayItem(item, String.valueOf(formatter.format(minutes)) + ":" + String.valueOf(formatter.format(seconds))));
        build.add(new Node(item, minutes, seconds));
        size++;
    }

    public void removeNode(int index){
        display.remove(index);
        build.remove(index);
        size = size-1;
    }

    public Node getNode(int i){
        return build.get(i);
    }

    public String toString(){
        String temp = "";
        for(Node n : build){
            temp += n.toString() + "\n";
        }
        return temp;
    }

    public int getSize(){
        return build.size();
    }
}