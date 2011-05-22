package com.trugertech.quickbart;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * The main screen for QuickBART
 * Displays the user's Favorite Routes
 * Each route is selectable which will then display the future schedule times
 * for the selected route.
 * If no routes are saved a message is displayed that there are no routes and they
 * can be added by pressing the menu and selecting Add Favorite.
 * 
 * @author scott
 *
 */
public class ActivityQuickBart extends ListActivity  {
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;
    private static final int ACTIVITY_DISPLAY=2;

    private static final int INSERT_ID = Menu.FIRST;
    private static final int EDIT_ID = Menu.FIRST + 1;
    private static final int DELETE_ID = Menu.FIRST + 2;
    
    private QuickBartDbAdapter mDbHelper;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_list);
        try{
	        mDbHelper = new QuickBartDbAdapter(this);
	        mDbHelper.open();
        }
        catch(Exception e){
        	Log.d("error in onCreate", e.getStackTrace().toString());
        }
        fillData();
        registerForContextMenu(getListView());
        
        //set touch listener for swipe
        getListView().setOnTouchListener(null);
        
    }
    
    /**
     * Populates the list with favorites from the SQLite DB
     */
    private void fillData(){
    	try{
	    	// get all the rows of data from the database and create favorite list
	    	Cursor favoritesCursor = mDbHelper.fetchAllFavorites();
	    	startManagingCursor(favoritesCursor);
	    	
	    	//create an array to specify the fields to display in the list
	    	String[] from = new String[]{QuickBartDbAdapter.KEY_FAVORITE_NAME};
	    	
	    	//create an array of the fields to bind those fields to
	    	int[] to = new int[]{R.id.text1};
	    	
	    	//create cursor adapter and set it to display
	    	SimpleCursorAdapter favorites = 
	    		new SimpleCursorAdapter(this, R.layout.favorite_row, favoritesCursor, from, to);
	    	
	    	setListAdapter(favorites);	
    	}
    	catch(Exception e){
    		Log.d("error in filldata", e.getStackTrace().toString());
    	}
    	
    }

	/**
     * Creates the menu dialog to create a favorite
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE, INSERT_ID, Menu.NONE, R.string.menu_favorites_insert);
        return true;
    }

    /**
     * Sets menu action to create a favorite when selected
     */
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch(item.getItemId()) {
            case INSERT_ID:
                createFavorite();
                return true;
        }

        return super.onMenuItemSelected(featureId, item);
    }
    
    /**
     * Creates a list context menu for long pressing on an item.
     * When long pressing on a favorite the Delete Favorite option
     * is presented
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, EDIT_ID, Menu.FIRST, R.string.menu_favorites_edit);
        menu.add(Menu.NONE, DELETE_ID, Menu.FIRST+1, R.string.menu_favorites_delete);
    }

    /**
     * Check what option was selected in the long press context menu
     * and takes the correct action.
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	
    	AdapterContextMenuInfo info;
    	
    	switch(item.getItemId()) {
            case DELETE_ID:
                info = (AdapterContextMenuInfo) item.getMenuInfo();
                mDbHelper.deleteFavorite(info.id);
                fillData();
                return true;
            
            case EDIT_ID:
            	info = (AdapterContextMenuInfo) item.getMenuInfo();
            	Intent i = new Intent(this, ActivityFavoriteEdit.class);
            	i.putExtra(QuickBartDbAdapter.KEY_ROWID, info.id);
               	startActivityForResult(i, ACTIVITY_EDIT);
               	return true;
        }
        
    	return super.onContextItemSelected(item);
    }

    /**
     * Creates a new favorite
     */
    private void createFavorite() {
        Intent i = new Intent(this, ActivityFavoriteEdit.class);
       	startActivityForResult(i, ACTIVITY_CREATE);
    }

    /**
     * When a favorite is clicked the Favorite Schedule activity is called to
     * display the details of the favorite
     */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent i = new Intent(this, ActivityFavoriteSchedule.class);
        i.putExtra(QuickBartDbAdapter.KEY_ROWID, id);
        startActivityForResult(i, ACTIVITY_DISPLAY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        fillData();
    }
}