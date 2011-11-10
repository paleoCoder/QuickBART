/**
 *  Copyright (c) 2011 Scott Truger
 *  See the file COPYING.txt for copying permissions
 **/

package com.trugertech.quickbart;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class BartApi_Stations {
	
	String[][] mStations;
	
	public BartApi_Stations() throws BartApiException{
		setStations();
	}
	
    /**
     * Takes in a station listing array, finds the passed short name
     * and returns the full name of the station.
     * If the matching long name is not found the short name is returned.
     * @throws BartApiException 
     */
    public String getStationLongName(String shortName) throws BartApiException{

    	int index = 0;
    	
    	if(mStations == null){
    		setStations();
    	}
    	
    	for(String tmpName : mStations[1]){
    		if(tmpName.equals(shortName)){
    			return mStations[0][index]; 
    		}
    		index++;
    	}
    	return shortName;
    }
    
    /**
     * Returns the list of BART Stations
     * @return Two dimensional string array wit the long an short
     * station names
     * @throws BartApiException 
     *  
     */
    public String[][] getStations() throws BartApiException {
    	 	
    	if(this.mStations == null){
    		setStations();
    	}      	
			
    	return mStations;
    }
    
    /**
     * Grabs all the available BART stations from the BART API.
     * Stores both the Long and Short station names
     * @throws BartApiException
     */
    private void setStations() throws BartApiException{
    
    	// set up XML source
		URL bartURL;
		try {
			
			bartURL = new URL(BartApi_UriGenerator.getCmd_Stn());
			InputSource is = new InputSource(bartURL.openStream());
			
			//create XML factory
			SAXParserFactory factory = SAXParserFactory.newInstance();
			
			//crate XML parser
			SAXParser parser = factory.newSAXParser();
			
			//create XML reader
			XMLReader xmlReader = parser.getXMLReader();
			
			//instantiate handler	        	
			BartApi_cmd_stn bfh = new BartApi_cmd_stn();
			
			//assign handler
			xmlReader.setContentHandler(bfh);
			
			//parse
			xmlReader.parse(is);
			
			//get some data!
			mStations = bfh.getResults();
		} catch (MalformedURLException e) {	
			throw new BartApiException(e.getLocalizedMessage());
		} catch (IOException e) {
			throw new BartApiException(e.getLocalizedMessage());
		} catch (ParserConfigurationException e) {
			throw new BartApiException(e.getLocalizedMessage());
		} catch (SAXException e) {
			throw new BartApiException(e.getLocalizedMessage());
		}	
    }

}
