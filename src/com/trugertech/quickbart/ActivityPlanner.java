/**
 *  Copyright (c) 2011 Scott Truger
 *  See the file COPYING.txt for copying permissions
 **/

package com.trugertech.quickbart;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.format.Time;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

	//TODO: use style for layout file
	//TODO: use inflator for layout???
	//TODO: hook up events
	//TODO: make new API wrapper

public class ActivityPlanner extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.planner);
		
		fillData();
	}
	
	/**
	 * Fills in the spinners with the stations from Bart API and sets
	 * the date and time for the buttons.
	 */
	private void fillData(){
		String[][] stations = null;
		String[] stationNames;
		String dateTime;
		Time timeNow;
		TextView tv;
		Button btn;
		RadioButton rbtn;
		Spinner sp;
		ArrayAdapter<String> adapterStationNames;
		
		//get the station listing and set the spinners
		try{
			//get station listings from bart api
			stations = BartAPI_cmd_stn.getStations();
			//use the full names
			stationNames = stations[0];
			
			
			//set the string adapter for the station names
			adapterStationNames = new ArrayAdapter<String>
				(this, android.R.layout.simple_spinner_dropdown_item, stationNames);
			//set the adapter item
			adapterStationNames.setDropDownViewResource
				(android.R.layout.simple_spinner_dropdown_item);
			
			//get the spinner from the view
			sp = (Spinner) findViewById(R.id.planner_spinnerDeparture);
			//set the spinner to the station names for display
			sp.setAdapter(adapterStationNames);
			
			//get the spinner from the view
			sp = (Spinner) findViewById(R.id.planner_spinnerDestination);
			//set the spinner to the station names for display
			sp.setAdapter(adapterStationNames);
			
		}
    	catch(Exception e){
    		Context context = getApplicationContext();
    		CharSequence text = "Sorry there was an error accessing BART information. Please try again.";
    		int duration = Toast.LENGTH_LONG;

    		Toast toast = Toast.makeText(context, text, duration);
    		toast.show();
    		finish();
    	}
		
		//set text for departure station
		tv = (TextView) findViewById(R.id.planner_txtDeparture);
		tv.setText("Select departure station:");
		
		//set text for destination station
		tv = (TextView) findViewById(R.id.planner_txtDestination);
		tv.setText("Select destination station:");
		
		//set the time widgets
		rbtn = (RadioButton) findViewById(R.id.planner_rdTimeArrive);
		rbtn.setText("Arrive");
		
		rbtn = (RadioButton) findViewById(R.id.planner_rdTimeDepart);
		rbtn.setText("Depart");
		rbtn.setChecked(true);
		
		// get the current time date
		timeNow = new Time();
		timeNow.setToNow();

		//set the time format to 10:12 am
		dateTime = timeNow.format("%l:%M %p").toString();
		btn = (Button) findViewById(R.id.planner_btnTimeChange);
		btn.setText(dateTime);
		
		//set the date widgets to Mon, Feb 3
		dateTime = timeNow.format("%a, %b %e").toString();
		btn = (Button) findViewById(R.id.planner_btnDateChange);
		btn.setText(dateTime);
		
		//set the go button
		btn = (Button) findViewById(R.id.planner_btnSubmit);
		btn.setText("Go");
		
	}
	

	
	

}
