/**
 *  Copyright (c) 2011 Scott Truger
 *  See the file COPYING.txt for copying permissions
 **/

package com.trugertech.quickbart;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

/**
 * Displays a general info page for the app.
 * @author scott
 *
 */
public class ActivityDisplayInfo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
			
		PackageInfo manager;
		TextView tvTitle;
		TextView tvInfo;
		
		String info = 
			"<p>Quick Bay Area Rapid Transit Routing Tool</p>" +
			"<p>QuickBART was created to be a simple app to provide quick access to " +
			"your favorite routes.</p>" +
			"<p>QuickBART is in constant development. " +
			"If you have any issues or would like to requset a feature please " +
			"<a href=\"mailto:android@truger.net?subject=Re:QuickBART\">email me.</a>" +
			"</p>" +
			"<p>Updates are posted on the <a href=\"http://blog.trugertech.com\">TrugerTech.com Blog</a></p>";
		
		try{
			manager = getPackageManager().getPackageInfo(getPackageName(), 0);
			info += "<p>App Version: " +
					manager.versionName.toString() +
					"</p>";	
		}
		catch (Exception e){
			//just won't have the version
		}
			
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.info);
		
		tvTitle = (TextView)findViewById(R.id.textTitle);
		tvTitle.setText("QuickBART");
		
		tvInfo = (TextView)findViewById(R.id.textInfo);
		tvInfo.setText(Html.fromHtml(info));
		
		tvInfo.setMovementMethod(LinkMovementMethod.getInstance());
		
		//set return code for calling activity
		setResult(RESULT_OK);

	}

}
