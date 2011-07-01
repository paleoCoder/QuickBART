/**
 *  Copyright (c) 2011 Scott Truger
 *  See the file COPYING.txt for copying permissions
 **/

package com.trugertech.quickbart;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

	//TODO: use style for layout file
	//TODO: use inflater for layout???

public class ActivityPlanner extends Activity {

	// *** Class members ***
	private int mHour;
	private int mMin;
	private int mMonth;
	private int mDay;
	private int mYear;
	static final int DIALOG_TIME_ID = 0;
	static final int DIALOG_DATE_ID = 1;
	
	//Activity Request Codes
	static final int ACTIVITY_PLANNER_SCHEDULE = 100;
	
	//holders for the station information
	//used for the spinners and to pass info to the BART API adapter
	String[][] mStations = null;
	String[] mStationNames;

	/////////////////////////////////////////////////////////////////
	// ***** Activity Listeners ******
	
	private View.OnClickListener mGoBtnListener = 
		new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// get locations
				Spinner depart = (Spinner) findViewById(R.id.planner_spinnerDeparture);
				Spinner dest = (Spinner) findViewById(R.id.planner_spinnerDestination);
				RadioButton btnArrive = (RadioButton) findViewById(R.id.planner_rdTimeArrive);
				Button btnDate = (Button) findViewById(R.id.planner_btnDateChange);
				Button btnTime = (Button) findViewById(R.id.planner_btnTimeChange);
				
				//strings to pass to the intent
				String destNameFull;
				String destNameShort;
				String departNameFull;
				String departNameShort;
				String departOrArrive;
				String planTime;
				String planDate;
				
				//Validate Spinners
				// check to insure two different stations are selected
            	if(depart.getSelectedItemPosition() != dest.getSelectedItemPosition()){
            		
            		//create intent for to display the selected planner route
            		Intent i = new Intent(ActivityPlanner.this , ActivityPlannerSchedule.class);
            		
            		//save the selected values from the planner to pass to the new activity
            		destNameFull = mStations[0][dest.getSelectedItemPosition()];
            		destNameShort = mStations[1][dest.getSelectedItemPosition()];
            		departNameFull = mStations[0][depart.getSelectedItemPosition()];
            		departNameShort = mStations[1][depart.getSelectedItemPosition()];
            		planTime = btnTime.getText().toString();
            		planTime = planTime.replace(" ", "+");
            		planDate = btnDate.getText().toString();
            		if(btnArrive.isChecked()){
            			departOrArrive = "Arrive";
            		}
            		else{
            			departOrArrive = "Depart";
            		}
            		
            		String[] plannerInfo = new String[] {destNameFull
            											, destNameShort
            											, departNameFull
            											, departNameShort
            											, departOrArrive
            											, planTime
            											, planDate};
            		
            		//put the values into the intent
            		i.putExtra(getPackageName().concat("plannerInfo"), plannerInfo);
            		
            		try{
            			//launch the activity to display the planned route
            		startActivityForResult(i, ACTIVITY_PLANNER_SCHEDULE);
            		}
            		catch(Exception e){
            			System.out.print(e);
            		}
            		
            	}
            	else{
                	//display a message to select different stations
                	CharSequence text = "Both selections cannot be the same station. Please select at least one different station.";
                	ActivityHelper.showToastMessage(text, true, getApplicationContext());
            	}
				
			}
		};
		
	//Date button listener
    private DatePickerDialog.OnDateSetListener mDateSetListener = 
    	new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				mYear = year;
				mMonth = monthOfYear;
				mDay = dayOfMonth;
				updateDateTime();
			}
		};	
		
	//Time dialog call back/listener
	
	// Time button listener
	private TimePickerDialog.OnTimeSetListener mTimeSetListener =
	    new TimePickerDialog.OnTimeSetListener() {
	        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
	            mHour = hourOfDay;
	            mMin = minute;
	            updateDateTime();
	        }
	    };
	
	// **** End Listeners ****    
	
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		Button btnGo;
		Button btnTime;
		Button btnDate;
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.planner);
		
		//fill the data elements
		setCurrentDateTime();
		fillData();
		
		//set actions for Go/submit button
		btnGo = (Button) findViewById(R.id.planner_btnSubmit);
		btnGo.setOnClickListener(mGoBtnListener);
		
		//set action for changing the time
		btnTime = (Button) findViewById(R.id.planner_btnTimeChange);
		btnTime.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DIALOG_TIME_ID);
			}
		});
		
		//set action for changing the date
		btnDate = (Button) findViewById(R.id.planner_btnDateChange);
		btnDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showDialog(DIALOG_DATE_ID);
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//update the fields on screen for date and time
		setCurrentDateTime();
		updateDateTime();
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		//Check which activity returns
		switch (requestCode) {
			case ACTIVITY_PLANNER_SCHEDULE:
				if(resultCode != RESULT_OK){
		    		CharSequence text = 
		    			"Sorry there was an error accessing BART information. Please try again.";
		    		ActivityHelper.showToastMessage(text, true, getApplicationContext());
				}
				break;
	
			default:
				break;
		}
	}
	

	/**
	 * Fills in the spinners with the stations from Bart API and sets
	 * the date and time for the buttons.
	 */
	private void fillData(){
		TextView tv;
		RadioButton rbtn;
		Button btn;
		Spinner sp;
		ArrayAdapter<String> adapterStationNames;
		
		//get the station listing and set the spinners
		try{
			//get station listings from bart API
			mStations = BartAPI_cmd_stn.getStations();
			//use the full names
			mStationNames = mStations[0];
			
			
			//set the string adapter for the station names
			adapterStationNames = new ArrayAdapter<String>
				(this, android.R.layout.simple_spinner_dropdown_item, mStationNames);
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
    		CharSequence text = "Sorry there was an error accessing BART information. Please try again.";
    		ActivityHelper.showToastMessage(text, true, getApplicationContext());
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
		
		//set the go button
		btn = (Button) findViewById(R.id.planner_btnSubmit);
		btn.setText("Go");
		
	}
	
	/***
	 * Updates the Date and Time buttons with the class members
	 */
	private void updateDateTime(){
		Button btn;
		String ampm;
		int hour;
		 
		//determine to use AM or PM for time display
		if(mHour > 12)
		{
			ampm = "pm";
			hour = mHour - 12;
		}
		else
		{
			ampm = "am";
			hour = mHour;
		}
		
		//set time button text
		btn = (Button) findViewById(R.id.planner_btnTimeChange);
		btn.setText(new StringBuilder()
							.append(hour)
							.append(":")
							.append(pad(mMin))
							.append(" ")
							.append(ampm));
		
		//set the date button text
		btn = (Button) findViewById(R.id.planner_btnDateChange);
		btn.setText(new StringBuilder()
							.append(mMonth+1) // zero based month
							.append("/")
							.append(mDay)
							.append("/")
							.append(mYear));		
	}
	
	/***
	 * Pads a zero to the returned string if the integer is less than ten.
	 * @param c
	 * @return
	 */
	private static String pad(int c) {
	    if (c >= 10)
	        return String.valueOf(c);
	    else
	        return "0" + String.valueOf(c);
	}

	/***
	 * Creates the dialogs for the time and date pickers
	 */
    @Override
    
    /***
     * 
     */
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DIALOG_TIME_ID:
            return new TimePickerDialog(this,
                    mTimeSetListener, mHour, mMin, false);
        case DIALOG_DATE_ID:
        	return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
        }
        	
        return null;
    }
    
    /***
     * Sets the class members for the current date and time.
     */
    private void setCurrentDateTime(){		
		//get the current date and time
		Calendar c = Calendar.getInstance();
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMin = c.get(Calendar.MINUTE);
		mMonth = c.get(Calendar.MONTH); //zero based calendar
		mDay = c.get(Calendar.DATE);
		mYear = c.get(Calendar.YEAR);
    }
	

}
