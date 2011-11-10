/**
 *  Copyright (c) 2011 Scott Truger
 *  See the file COPYING.txt for copying permissions
 **/

package com.trugertech.quickbart;

import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.TextView;

/***
 * Displays the BART route for the planner activity. 
 * 
 * @author scott
 *
 */

public class ActivityPlannerSchedule extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		String[] extras;
		try {
				super.onCreate(savedInstanceState);
			
			//reuse the favorite layout for displaying the BART route
			setContentView(R.layout.favorite_schedule);
			setTitle(R.string.schedule_name); 
			
			//get info from planner activity
			extras = getIntent().getStringArrayExtra(getPackageName().concat("plannerInfo"));
			
			//get the route in from the BART API
			populateSchedule(extras);
			
			//Every thing went well and is displayed
			setResult(RESULT_OK);
		}
		catch (Exception e){
			setResult(RESULT_CANCELED);
    		finish();
		}
		
	}
	
	/***
	 * Gets the BART route info for the selection from the Planner.
	 */
	private void populateSchedule(String[] extras){
		String destNameFull = extras[0];
		String destNameShort = extras[1];
		String departNameFull = extras[2];
		String departNameShort = extras[3];
		String departOrArrive = extras[4];
		String planTime = extras[5];
		String planDate = extras[6];
		
		String plannedRouteName = departNameFull + " to " + destNameFull;
		
		TextView tv; //text view for setting display items
		
		ArrayList<FavoriteRoute> favRoute = new ArrayList<FavoriteRoute>(); //adapter for list
		
		favRoute = getPlannedSchedule(departNameShort
								, destNameShort
								, departOrArrive
								, planTime
								, planDate);
		
		//set list adapter
		FavoriteRouteAdapter favAdapter = new FavoriteRouteAdapter(
				this, R.layout.favorite_schedule_item, favRoute, (long) 0);
		setListAdapter(favAdapter);
		
		//set the planned route names
		tv = (TextView)findViewById(R.id.favName);
		tv.setText(plannedRouteName);
		
		//set the fare for the trip
		tv = (TextView)findViewById(R.id.routeFare);
		tv.setText("Fare: " + favRoute.get(0).getFare());
		
	}
	
	private ArrayList<FavoriteRoute> getPlannedSchedule(String departure, String destination, String departOrArrive, String time, String date){
		ArrayList<FavoriteRoute> favoriteRoutes = new ArrayList<FavoriteRoute>();
		try{
			//boolean for calculating if the planner is for departure or arrival time
			boolean depart = true;
			if(departOrArrive.compareToIgnoreCase("arrive") == 0){
				depart = false;
			}

    		// set up XML source
        	URL bartURL = new URL(BartApi_UriGenerator.getCmd_Schedule_Planned(departure, destination, date, time, depart));
        	InputSource is = new InputSource(bartURL.openStream());
        	
        	//create XML factory
        	SAXParserFactory factory = SAXParserFactory.newInstance();
        	
        	//crate XML parser
        	SAXParser parser = factory.newSAXParser();
        	
        	//create XML reader
        	XMLReader xmlReader = parser.getXMLReader();
        	
        	//instantiate handler	        	
        	BartAPI_cmd_sched bfh = new BartAPI_cmd_sched((ApplicationQuickBart)this.getApplicationContext());
        	
        	//assign handler
        	xmlReader.setContentHandler(bfh);
        	
        	//parse
        	xmlReader.parse(is);
        	
        	//get some data!
        	favoriteRoutes = bfh.getResults();	
    	}
    	catch(Exception e){
    		setResult(RESULT_CANCELED);
    		finish();
    	}	
    	
    	return favoriteRoutes;
	}

}
