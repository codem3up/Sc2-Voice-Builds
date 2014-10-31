 package com.example.sc2voicebuilds;


import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.text.DecimalFormat;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class TCreator extends Base_Activity {

    ViewGroup parent;
    TextView txt;
	Spinner s, s1;
    View convertView;
	String structures_spinner[], units_spinner[], TAG = "TCreator", string = "", myList[], tempstr;
	private String str = "", str2 = "", str3 = "", strx;
	Build b;
	private int number = 0, newtemp = 0, minutes = 0, current = -1, pos;
	private TextView textfield, textfield2, display, displaySeconds, displayMinutes;
	private Handler handler;
	private boolean Running = false;
	Runnable runnable;
	Boolean alreadySaved = false;
	StringBuffer fileContent;
	DecimalFormat formatter, f;
	ArrayAdapter myarrayAdapter;
	ListView lv;
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.base__creator, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
				{
			case R.id.home:
				Log.i(TAG, "Home Clicked");
				Intent intent = new Intent(this, MainActivity.class);
				this.startActivity(intent);
				return true;
		    //save
			case R.id.save:

				Log.i(TAG, "Save Clicked");
				AlertDialog.Builder editalert = new AlertDialog.Builder(this);
				
				editalert.setTitle("Save");
				editalert.setMessage("Enter Name of Build to Save");


				final EditText input = new EditText(this);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				        LinearLayout.LayoutParams.FILL_PARENT,
				        LinearLayout.LayoutParams.FILL_PARENT);
				input.setLayoutParams(lp);
				editalert.setView(input);

				editalert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int whichButton) {
				    	inputString = new String(input.getText().toString());
				    	  String filename = "terranBuilds.dat";
					        FileOutputStream outputStream;
					        string = "";
					        string += inputString + "\n";
					        string+= b.toString();
					        string+= "end"+"\n";

					        try {
					          outputStream = openFileOutput(filename, MODE_APPEND);
					          outputStream.write(string.getBytes());
					          outputStream.close();
					        } catch (Exception e) {
					          e.printStackTrace();
					        }
				    	Toast.makeText(getBaseContext(), "Saved", Toast.LENGTH_LONG).show();
				    //	Toast.makeText(getBaseContext(), string, Toast.LENGTH_LONG).show();

				    	string = "";
				    }
				});
				
				editalert.show();
				return true;
				
				
			default:
				return super.onOptionsItemSelected(item);
				}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tcreator);
			myList = new String[50];
			for(int x =0; x < 50; x++)
			{
				myList[x] = "";
			}
        myarrayAdapter = new ArrayAdapter<String>(this, R.layout.items, myList);

        lv = (ListView)findViewById(R.id.listView1);
        lv.setAdapter(myarrayAdapter);

         //   int d = lv.getSelectedItemPosition();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                if (myList[pos] != "") {
                    AlertDialog.Builder deleteNode = new AlertDialog.Builder(parent.getContext());
                    deleteNode.setMessage("Delete this node?");

                    //checks for android version - basically makes sure that ok button is always in correct spot, regardless of android version
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        deleteNode.setNegativeButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                b.removeItem(pos);
                                while (pos < newtemp) {
                                    myList[pos] = myList[pos + 1];
                                    pos++;
                                }
                                newtemp--;
                                myList[newtemp] = "";
                                myarrayAdapter.notifyDataSetChanged();
                            }
                        });

                        deleteNode.setPositiveButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                    } else {
                        deleteNode.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                b.removeItem(pos);
                                while (pos < newtemp) {
                                    myList[pos] = myList[pos + 1];
                                    pos++;
                                }
                                newtemp--;
                                myList[newtemp] = "";
                                myarrayAdapter.notifyDataSetChanged();
                            }
                        });

                        deleteNode.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                    }

                    deleteNode.show();
                }
            }
        });
		 f = new DecimalFormat("00");
		
		b = new Build(100);
		s = (Spinner) findViewById(R.id.spinner1);
	    structures_spinner = new String[16];
	    structures_spinner[0] = "Structures---";
	    structures_spinner[1] = "Barracks";
	    structures_spinner[2] = "Factory";
	    structures_spinner[3] = "Starport";
	    structures_spinner[4] = "Supply Depot";
	    structures_spinner[5] = "Command Center";
	    structures_spinner[6] = "Orbital Command";
	    structures_spinner[7] = "Planetary Fortress";
	    structures_spinner[8] = "Refinery";
	    structures_spinner[9] = "Engineering Bay";
	    structures_spinner[10] = "Armory";
	    structures_spinner[11] = "Ghost Academy";
	    structures_spinner[12] = "Missile Turret";
	    structures_spinner[13] = "Sensor Tower";
	    structures_spinner[14] = "Reactor";
	    structures_spinner[15] = "Tech lab";
	    
	    ArrayAdapter adapter = new ArrayAdapter(this,
	    android.R.layout.simple_spinner_item, structures_spinner);
	    s.setAdapter(adapter);
	    
	    s1 = (Spinner) findViewById(R.id.spinner2);
	    units_spinner = new String[15];
	    units_spinner[0] = "Units---";
	    units_spinner[1] = "SCV";
	    units_spinner[2] = "Marine";
	    units_spinner[3] = "Marauder";
	    units_spinner[4] = "Reaper";
	    units_spinner[5] = "Ghost";
	    units_spinner[6] = "Hellion";
	    units_spinner[7] = "Tank";
	    units_spinner[8] = "Widow Mine";
	    units_spinner[9] = "Thor";
	    units_spinner[10] = "Banshee";
	    units_spinner[11] = "Viking";
	    units_spinner[12] = "Raven";
	    units_spinner[13] = "BattleCruiser";
	    units_spinner[14] = "2 Workers on Gas";
	    units_spinner[14] = "3 workers on gas";
	    
	    ArrayAdapter adapt = new ArrayAdapter(this,
	    android.R.layout.simple_spinner_item, units_spinner);
	    s1.setAdapter(adapt);
	    
	    
	    textfield = (TextView)findViewById(R.id.secondsLabel);
	    textfield2 = (TextView)findViewById(R.id.minutesLabel);
	  //  display = (TextView)findViewById(R.id.display);
	  // displaySeconds = (TextView)findViewById(R.id.secondsDisplay);
	  //  displayMinutes = (TextView)findViewById(R.id.minutesDisplay);
	    
	    handler = new Handler();
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
	    						if (number == 60)
	    						{
	    							number = 0;
		    						formatter = new DecimalFormat("00");
	    							minutes++;
	    							textfield2.setText(String.valueOf(formatter.format(minutes)));
	    						
	    						}
	    						formatter = new DecimalFormat("00");
	    						number++;
	    						textfield.setText(String.valueOf(formatter.format(number)));
	    						
	    					}
	    				});
	    			}
	    		}
	    			
	    
	};
	//new Thread(runnable).start();
	
	}
	
	public void startTimer(View v)
	{
        if (!Running) {
        new Thread(runnable).start();
        Running = true;
        }
	}
	
	public void stopTimer(View v)
	{
		Running = false;
		number--;
	}
					
	
	public void addUnit(View v)
	{
		int temp = s1.getSelectedItemPosition();
            b.addItem(units_spinner[temp], minutes, number);
            int sec = b.getSeconds();
            int min = b.getMinutes();
            String nam = b.getName();
            if (current != sec) {
                str += nam + "\n";
                str2 += min + "\n";
                str3 += sec + "\n";
                current = sec;
                tempstr = nam + "\t" + f.format(min) + ":" + f.format(sec);
                myList[newtemp] = tempstr;
                myarrayAdapter.notifyDataSetChanged();
                newtemp++;
            }
            else{
                b.removeLastItem();
            }
		//display.setText(str);
		//displaySeconds.setText(str3);
		//displayMinutes.setText(str2);
		
		
		
	}
	
	public void addStructure(View v)
	{
		int temp = s.getSelectedItemPosition();
		String s = Integer.toString(temp);
	//	Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
		b.addItem(structures_spinner[temp], minutes, number);
        int sec = b.getSeconds();
        int min = b.getMinutes();
        String nam = b.getName();
		if (current != sec) {
            str += nam + "\n";
            str2 += min + "\n";
            str3 += sec + "\n";
            current = sec;
            tempstr = nam + "\t" + f.format(min) + ":" + f.format(sec);
            myList[newtemp] = tempstr;
            myarrayAdapter.notifyDataSetChanged();
            newtemp++;
        }
        else{
            b.removeLastItem();
        }
	
		//display.setText(str);
		//displaySeconds.setText(str3);
		//displayMinutes.setText(str2);
		
	}

		
}
