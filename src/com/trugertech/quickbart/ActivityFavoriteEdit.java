package com.trugertech.quickbart;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

public class ActivityFavoriteEdit extends Activity{
	
	private QuickBartDbAdapter mDbHelper;
	private Long mRowId;
	String[][] stations = null;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try{
        	mDbHelper = new QuickBartDbAdapter(this);
        	mDbHelper.open();
        }
        catch(Exception e){
        	System.out.print(e.getMessage().toString());
        }
        

        //set the display
        setContentView(R.layout.favorite_edit);
        setTitle(R.string.edit_name);
        
        //get the favorite from the DB from either being resumed or passed from an activity
        mRowId = (savedInstanceState == null) ? null :
            (Long) savedInstanceState.getSerializable(QuickBartDbAdapter.KEY_ROWID);
		if (mRowId == null) {
			Bundle extras = getIntent().getExtras();
			mRowId = extras != null ? extras.getLong(QuickBartDbAdapter.KEY_ROWID)
									: null;
		}
        
		// gets the station listings
        populateFields();

        
        //sets the button save actions
        Button btnSave = (Button) findViewById(R.id.btnFavoriteSave);
        btnSave.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
            	Spinner spinDepart = (Spinner) findViewById(R.id.spinDepart);
                Spinner spinDest = (Spinner) findViewById(R.id.spinDestination);
                CheckBox chkReturnTrip = (CheckBox) findViewById(R.id.chkSaveReturnTrip);
                
                // check to insure two different stations are selected
            	if(spinDepart.getSelectedItemPosition() != spinDest.getSelectedItemPosition()){
            		setResult(RESULT_OK);
            		saveFavorite(spinDepart.getSelectedItemPosition(), spinDest.getSelectedItemPosition(), chkReturnTrip.isChecked());
            		finish();
            	}
            	else{
                	//display a message to select different stations
                	Context context = getApplicationContext();
                	CharSequence text = "Both selections cannot be the same station. Please select at least one different station.";
                	int duration = Toast.LENGTH_LONG;
                	
                	Toast.makeText(context, text, duration).show();
            	}
                
            }

        });
    }

	/**
	 * Fills in the spinners with all available BART stations
	 */
    private void populateFields() {
        String favoriteDest = "";
        String favoriteDepart = "";
        int[] favIndex;
    	
    	if (stations == null){
    		try{
    			stations = BartAPI_cmd_stn.getStations();
    		}
        	catch(Exception e){
        		Context context = getApplicationContext();
        		CharSequence text = "Sorry there was an error accessing BART information. Please try again.";
        		// TODO: for debugging, remove next line
        		text = e.getMessage();
        		int duration = Toast.LENGTH_LONG;

        		Toast toast = Toast.makeText(context, text, duration);
        		toast.show();
        	}
        }
    	
    	String[] stationNames = stations[0];
        
        Spinner spinDepart = (Spinner) findViewById(R.id.spinDepart);
        Spinner spinDest = (Spinner) findViewById(R.id.spinDestination);
        
        ArrayAdapter<String> adapterDepart = 
        	new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, stationNames);
        ArrayAdapter<String> adapterDest = 
        	new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, stationNames);
        
        adapterDepart.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterDest.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        spinDepart.setAdapter(adapterDepart);
        spinDest.setAdapter(adapterDest);
        
        if (mRowId != null) {
            Cursor favorite = mDbHelper.fetchFavorite(mRowId);
            startManagingCursor(favorite);
            
            favoriteDest = favorite.getString(
            		favorite.getColumnIndexOrThrow(QuickBartDbAdapter.KEY_DESTINATION_LONG));
            favoriteDepart = favorite.getString(
            		favorite.getColumnIndexOrThrow(QuickBartDbAdapter.KEY_DEPARTURE_LONG));
            
            favIndex = getFavoriteStationsIndex(favoriteDest, favoriteDepart);
            
            spinDest.setSelection(favIndex[0]);
            spinDepart.setSelection(favIndex[1]);

        }
    }

    /***
     * Finds the index of the destination and departure station
     * in the station array
     * @param String destination station long name
     * @param String departure station long name
     * @return integer array {destination index, departure index}
     */
    private int[] getFavoriteStationsIndex(String dest, String depart){
        int favIndex = 0;
        int retIndex[] = {-1, -1};
        
        String[] stationNames = stations[0]; 
        for(String s : stationNames){
        	if(s.equals(dest)){
        		retIndex[0] = favIndex;
        	}else if(s.equals(depart)){
        		retIndex[1] = favIndex;
        	}
        	if(retIndex[0] != -1 && retIndex[1] != -1) {break;}
        	favIndex++;
        }
        return retIndex;
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        saveState();
        outState.putSerializable(QuickBartDbAdapter.KEY_ROWID, mRowId);
    }

//    Removed since to resolve bug - item saved with out save button being pressed
//    @Override
//    protected void onPause() {
//        super.onPause();
//        saveState();
//    }

    @Override
    protected void onResume() {
        super.onResume();
        populateFields();
    }

    /***
     * Saves the current state of the edit view. If the currently displayed
     * favorite is new then save, else it already exists and update. If true for
     * saveReturnTrip the opposite trip is saved too
     */
    private void saveFavorite(int stationDepartID, int stationDestinationID, boolean saveReturnTrip) {
//    	Spinner spinDepart = (Spinner) findViewById(R.id.spinDepart);
//        Spinner spinDest = (Spinner) findViewById(R.id.spinDestination);
        
        //if there isn't an existing favorite that is being displayed create it
        if (mRowId == null) {
            long id = mDbHelper.createFavorite(
            		stations[0][stationDepartID] +
            			" to " + 
            			stations[0][stationDestinationID],
            		stations[1][stationDepartID], 
            		stations[0][stationDepartID], 
            		stations[1][stationDestinationID], 
            		stations[0][stationDestinationID]);
            if (id > 0) {
                mRowId = id;
            }
        } else { // update the existing favorite that is being displayed
            mDbHelper.updateFavorite(mRowId,
            		stations[0][stationDepartID] +
        			" to " + 
        			stations[0][stationDestinationID],
            		stations[1][stationDepartID], 
            		stations[0][stationDepartID], 
            		stations[1][stationDestinationID], 
            		stations[0][stationDestinationID]);
        }
        
        //save return trip
        if (saveReturnTrip){
        	mDbHelper.createFavorite(
	    		stations[0][stationDestinationID] +
	    			" to " + 
	    			stations[0][stationDepartID],
	    		stations[1][stationDestinationID], 
	    		stations[0][stationDestinationID], 
	    		stations[1][stationDepartID], 
	    		stations[0][stationDepartID]);
        }
    }

}


















