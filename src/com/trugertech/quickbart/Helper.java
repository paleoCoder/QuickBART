/**
 *  Copyright (c) 2011 Scott Truger
 *  See the file COPYING.txt for copying permissions
 **/

package com.trugertech.quickbart;

import android.content.Context;
import android.widget.Toast;

/**
 * Provides common helper functions
 * @author scott
 *
 */
public final class Helper {
	
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
