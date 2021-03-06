/**
 *  Copyright (c) 2011 Scott Truger
 *  See the file COPYING.txt for copying permissions
 **/

package com.trugertech.quickbart;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;

/**
 * Displays the route details for the selected schedule. The name of the favorite 
 * is displayed on top followed by a list of the legs of the route.
 * 
 * @author scott
 *
 */
public class ActivityFavoriteDetails extends ListActivity{
	
	private ArrayList<FavoriteTripLeg> mTripLegs;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);      

        setContentView(R.layout.favorite_details);
        setTitle(R.string.schedule_name);
        
      //retrieve stored reference to trip legs to display
        if(savedInstanceState == null){
        	this.mTripLegs = null;
        }
        else{
        	Bundle stuff = savedInstanceState.getBundle("TripLegs");
        	this.mTripLegs = stuff.getParcelableArrayList("TripLegs");
        }
        
		if (this.mTripLegs == null) {
			Bundle extras = getIntent().getExtras();
			if(extras != null){
				this.mTripLegs = extras.getParcelableArrayList("TripLegs");
			}
			else{
				this.mTripLegs = null;
			}	
		}
		
		//set result for parent calling activity
		setResult(RESULT_OK);
    }

	/**
	 * Populates the trip legs display for the selected favorite route
	 */
    private void populateTripLegs() {
            	
        if (mTripLegs != null) {
            
            // set layout attributes with Schedule Item values
            FavoriteTripLegAdapter legAdapter = new FavoriteTripLegAdapter(this, R.layout.favorite_details_item, mTripLegs);
            setListAdapter(legAdapter);
            
        }
        else{
        	//this will close the activity and return to the parent with a canceled status
        	setResult(RESULT_CANCELED);
        	finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        
        Bundle stuff = new Bundle();
        stuff.putParcelableArrayList("TripLegs", this.mTripLegs);
        outState.putBundle("TripLegs", stuff);
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateTripLegs();
    }

}
