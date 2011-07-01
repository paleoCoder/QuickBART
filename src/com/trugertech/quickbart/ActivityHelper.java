package com.trugertech.quickbart;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * Provides common helper functions for any activity
 * @author scott
 *
 */
public final class ActivityHelper extends Activity {
	
    /**
     * Shows long toast message on screen
     */
    public final static void showToastMessage(CharSequence message, Boolean durationLong, Context activityContext){
        Toast toast;
        int duration;
        
        if(durationLong){
        	duration = Toast.LENGTH_LONG;
        }
        else{
        	duration = Toast.LENGTH_SHORT;
        }
        	
        toast = Toast.makeText(activityContext, message, duration);
		toast.show();
    }

}
