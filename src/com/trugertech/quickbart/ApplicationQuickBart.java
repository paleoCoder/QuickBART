/**
 *  Copyright (c) 2011 Scott Truger
 *  See the file COPYING.txt for copying permissions
 **/

package com.trugertech.quickbart;

import android.app.Application;

public class ApplicationQuickBart extends Application {
	
	private BartApi_Stations mBartStations;
	
	@Override
	public void onCreate(){

	}
	
	public BartApi_Stations getStations() throws BartApiException{
		if(mBartStations == null){
			mBartStations = new BartApi_Stations();
		}
		return mBartStations;
	}

}
