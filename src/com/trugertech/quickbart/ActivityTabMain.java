/**
 *  Copyright (c) 2011 Scott Truger
 *  See the file COPYING.txt for copying permissions
 **/

package com.trugertech.quickbart;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class ActivityTabMain extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_main);
		
		Resources res = getResources(); // resource object to get 
		TabHost tabHost = getTabHost(); //the activity TabHost
		TabHost.TabSpec spec; //reusable TabSpec for each tab
		Intent intent; //reusable intent for each tab
		
		//initialize a TabsSpec for each tab and add it to the TabHost
		
		//favorites tab
		intent = new Intent().setClass(this, ActivityQuickBart.class);
		spec = tabHost.newTabSpec("favorites")
				.setIndicator("Favorites", res.getDrawable(R.drawable.ic_tab_favorite))
				.setContent(intent);
		tabHost.addTab(spec);
		
		//planner tab
		intent = new Intent().setClass(this, ActivityPlanner.class);
		spec = tabHost.newTabSpec("panner")
				.setIndicator("Planner", res.getDrawable(R.drawable.ic_tab_planner))
				.setContent(intent);
		tabHost.addTab(spec);
		
		//map tab
		intent = new Intent().setClass(this, ActivityMap.class);
		spec = tabHost.newTabSpec("map")
				.setIndicator("Map", res.getDrawable(R.drawable.ic_tab_map))
				.setContent(intent);
		tabHost.addTab(spec);
		
		//Set the selected tab
		tabHost.setCurrentTabByTag("favorites");
		
	}

}
