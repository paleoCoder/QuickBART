/**
 *  Copyright (c) 2011 Scott Truger
 *  See the file COPYING.txt for copying permissions
 **/

package com.trugertech.quickbart;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;



public class ActivityMap extends Activity {
	WebView mWebView;
	
	//TODO: to remove the title bar from the web view add this to the manifest:
	//	<activity android:name=".HelloGoogleMaps" android:label="@string/app_name"
	//	     android:theme="@android:style/Theme.NoTitleBar">
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.map_web);

	    mWebView = (WebView) findViewById(R.id.webview);
	    mWebView.loadUrl("http://www.bart.gov/images/global/system-map29.gif");
	}
	
}
