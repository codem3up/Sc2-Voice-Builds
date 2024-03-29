package com.gmail.andrewjoelbecker.sc2vb.starcraft2voicebuilds.sc2vb;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.parse.*;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class: DownloadFragment.java
 * Function: The DownloadFragment is a class that allows users to view and save builds VIA the parse API
 * and connected parse database.  This class is called inside the BuildLoader class
 *
 */
public class DownloadFragment extends Fragment {
    ArrayList<Object> entities= new ArrayList<Object>();
    ArrayList<TitleItem> originalTitles = new ArrayList<TitleItem>();
    ArrayList<TitleItem> displayTitles = new ArrayList<TitleItem>();
    ArrayList<String> buildString;
    ArrayAdapter myarrayAdapter;
    ListView lv;
    LinearLayout ll;
    EditText searchInput;
    ImageButton back, save;
    Button searchBtn;
    int race, index = 0;
    View v;
    MyBuild build;
    int currentPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_download, container, false);
        Parse.initialize(getActivity().getBaseContext(), "v9D4hN8qNtXWTE4z4aNOHZsXkhBlVW29Iucw1Ll9", "bw5EcOR0neExSVWiMzrd8xdj1sqeSAmTSWlnZTdC");
        Bundle bundle = this.getArguments();
        race = bundle.getInt("race");

        back = (ImageButton)v.findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayView();
            }
        });
        save = (ImageButton)v.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBuild();
            }
        });
        searchInput = (EditText)v.findViewById(R.id.input);
        searchBtn = (Button)v.findViewById(R.id.search);

        searchBtn.setOnClickListener(new View.OnClickListener(){
            @Override
        public void onClick(View v){
                InputMethodManager imm = (InputMethodManager)getActivity().getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(searchInput.getWindowToken(), 0);

                String temp = searchInput.getText().toString();
                if(temp.equals("") || temp.equals(" ") || temp.equals("  ")){
                    displayTitles.clear();
                    for(TitleItem t : originalTitles){
                        displayTitles.add(t);
                    }
                    updateView();
                }
                else {
                    displayTitles.clear();
                    for (TitleItem t : originalTitles) {
                        if (t.getItem().toLowerCase().contains(temp.toLowerCase())) {
                            displayTitles.add(t);
                        }
                    }
                    myarrayAdapter = new TitleAdapter(getActivity().getBaseContext(), R.layout.items, displayTitles);
                    lv.setAdapter(myarrayAdapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            currentPosition = displayTitles.get(position).getPosition();
                            displayBuild(currentPosition);
                        }
                    });
                    updateView();
                }

            }
        });

        ll = (LinearLayout)v.findViewById(R.id.ll);
        displayView();
        String parseClass = "";

        if(race == 1){
            parseClass = "Build";
            ImageView background = (ImageView)v.findViewById(R.id.downloadImage);
            background.setImageResource(R.drawable.tbg);
        }
        else if(race == 2){
            parseClass = "PBuild";
            ImageView background = (ImageView)v.findViewById(R.id.downloadImage);
            background.setImageResource(R.drawable.pbg);
        }
        else if(race == 3){
            parseClass = "ZBuild";
            ImageView background = (ImageView)v.findViewById(R.id.downloadImage);
            background.setImageResource(R.drawable.zbg);
        }

        Log.i("TAG", Integer.toString(race));
        ParseQuery<ParseObject> query = ParseQuery.getQuery(parseClass);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objects, ParseException e) {
                if (objects == null) {
                    Toast.makeText(getActivity().getBaseContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
                } else {
                    postResults(objects);
                }
            }
        });

        return v;
    }

    public void saveBuild(){
        FileOutputStream outputStream;
        String string = "";
        String filename = "";
        if (race == 1) {
            filename = "terran.dat";
        } else if (race == 2) {
            filename = "protoss.dat";
        } else if (race == 3) {
            filename = "zerg.dat";
        }
        string = "$";
            string += originalTitles.get(currentPosition).getItem();
        string += "**" + "\n";
        string += build.toString();
        Toast.makeText(getActivity().getBaseContext(), string, Toast.LENGTH_LONG).show();

        try {
            outputStream = getActivity().openFileOutput(filename, getActivity().MODE_APPEND);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(getActivity().getBaseContext(), "Saved", Toast.LENGTH_LONG).show();

        string = "";
    }
    public void displayView(){
        ll.setVisibility(View.INVISIBLE);
        myarrayAdapter = new TitleAdapter(getActivity().getBaseContext(), R.layout.items, displayTitles);

        lv = (ListView)v.findViewById(R.id.listView);
        lv.setAdapter(myarrayAdapter);


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentPosition = originalTitles.get(position).getPosition();
                displayBuild(currentPosition);
            }
        });
    }

    public void displayBuild(int pos){
        buildString = new ArrayList<String>();
        ll.setVisibility(View.VISIBLE);
        String temp = entities.get(pos).toString();
        temp = temp.replace("[", "");
        temp = temp.replace("]", "");
        buildString.addAll(Arrays.asList(temp.split("\\s*,\\s")));
        constructBuild();

        myarrayAdapter = new CustomAdapter(getActivity().getBaseContext(), R.layout.row, build.display);

        lv = (ListView)v.findViewById(R.id.listView);
        lv.setAdapter(myarrayAdapter);
        lv.setOnItemClickListener(null);
    }

    public void constructBuild(){
        build = new MyBuild(race);

        for(String s : buildString){
            int first = s.indexOf("{");
            int middle = s.indexOf(":");
            int last = s.indexOf("}");
            build.addNode(s.substring(0, first), Integer.parseInt(s.substring(first + 1, middle)), Integer.parseInt(s.substring(middle + 1, last)));
        }
    }

    public void updateView(){
        myarrayAdapter.notifyDataSetChanged();
    }

    public void postResults(List<ParseObject> ob){
        for(ParseObject p : ob){
            String name = p.getString("Name");
            name = name.replace("$", "");
            originalTitles.add(new TitleItem(index, name + " "));
            entities.add(p.get("Entity"));
            index++;
            displayTitles.add(new TitleItem(index, name + " "));
        }

        updateView();
    }

    public void printObjects(List<ParseObject> obj){
        Toast.makeText(getActivity().getBaseContext(), "Success", Toast.LENGTH_SHORT).show();
    }
}
