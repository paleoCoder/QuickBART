/**
 *  Copyright (c) 2011 Scott Truger
 *  See the file COPYING.txt for copying permissions
 **/

package com.trugertech.quickbart;

/***
 * Provides the URI for calling the BART.gov API. The URI with the command,
 * any required parameters and the Developer API Key is returned for use.
 * 
 * @author scott
 *
 */
public class BartApi_UriGenerator {
	protected static final String API_KEY = "YRSI-ZII8-QHUQ-JXYX";
	protected static final String URI_ROOT = "http://api.bart.gov/api/";
	
	/**
	 * Provides the URI for calling the Schedule command for the origin
	 * and destination stations. The current time is used. One previous train
	 * and the next 4 train times are returned.
	 * @param orig
	 * @param dest
	 * @return
	 */
	public static final String getCmd_Sched(String orig, String dest){
		return URI_ROOT 
				+ "sched.aspx?" 		//Schedule command
				+ "cmd=depart"			//calculate departure time
				+ "&orig=" + orig 		//origin station
				+ "&dest=" + dest 		//destination station
				+ "&a=4&b=1"			//next 4 and 1 previous train
				+ "&key=" + API_KEY;	//developer API key
	}
	
	/**
	 * Provides the URI for calling the Station command.
	 * @return
	 */
	public static final String getCmd_Stn(){
		return URI_ROOT + "stn.aspx?cmd=stns&key=" + API_KEY;
	}
	
	/**
	 * Provides the URI for calling the Schedule command for a trip at
	 * the specified data and time. If depart is true the time is used for
	 * departure time, if false then the time is used for the arrival time.
	 * @param orig
	 * @param dest
	 * @param date mm/dd/yyyy
	 * @param time h:mm+am/pm
	 * @param depart
	 * @return
	 */
	public static final String getCmd_Schedule_Planned(String orig
													, String dest
													, String date
													, String time
													, boolean depart){
		String uri = URI_ROOT;
		
		uri = uri.concat("sched.aspx?");  		//Schedule command
		if(depart){
			uri = uri.concat("cmd=depart");		//calculate departure time	
		}
		else {
			uri = uri.concat("cmd=arrive");		//calculate arrival time
		}
		uri = uri.concat("&orig=" + orig 			//origin station
					+ "&dest=" + dest 		//destination station
					+ "&time=" + time		//time for arrive/depart
					+ "&a=4&b=1"			//next 4 and 1 previous train
					+ "&key=" + API_KEY);	//developer API key	}
		
		return uri;
	}
}
