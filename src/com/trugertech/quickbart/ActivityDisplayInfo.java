package com.trugertech.quickbart;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class ActivityDisplayInfo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
			
		PackageInfo manager;
		
		String info = 
			"<h1>QuickBART</h1>" +
			"<h2>Quick Bay Area Rapid Transit Routing Tool</h2>" +
			"<p>QuickBART was created to be a simple app to provide quick access to " +
			"your favorite routes.</p>" +
			"<p>QuickBART is in constant development." +
			"If you have any issues or would like to requset a feature please " +
			"<a href=\"mailto:quickbart@truger.net?Subject=Re:QuickBART\">email me.</a>" +
			"</p>";
		
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
		
		TextView vw = (TextView)findViewById(R.id.textInfo);
		vw.setText(Html.fromHtml(info));
		
		vw.setMovementMethod(LinkMovementMethod.getInstance());

	}

}
