/**
 *  Copyright (c) 2011 Scott Truger
 *  See the file COPYING.txt for copying permissions
 **/

package com.trugertech.quickbart;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Provides a view of the BART map in a web view
 * @author scott
 *
 */

public class ActivityMap extends Activity {
	WebView mWebView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.map_web);

	    mWebView = (WebView) findViewById(R.id.webview);
	    mWebView.loadUrl("http://www.bart.gov/images/global/system-map29.gif");
	}
	
}
