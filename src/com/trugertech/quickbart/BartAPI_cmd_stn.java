/**
 *  Copyright (c) 2011 Scott Truger
 *  See the file COPYING.txt for copying permissions
 **/

package com.trugertech.quickbart;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class BartApi_cmd_stn extends DefaultHandler {
	
    private String mCurrentNode;

    private ArrayList<String> nameList;
    private ArrayList<String> abbrList;
    

    public BartApi_cmd_stn() {
    }

    /**
     * Gets the BART station name listing as a 2 dimensional string array.
     * The first array is the long names of the stations.
     * The second array is the abbreviated names of the stations.
     * @return
     */
    public String[][] getResults()
    {
    	
    	String[][] ret = { 	nameList.toArray(new String[nameList.size()]), 
    						abbrList.toArray(new String[abbrList.size()]) };
    	return ret;
    }
    
    @Override
    public void startDocument() throws SAXException {
    	// initialize "list"
    	nameList = new ArrayList<String>();
    	abbrList = new ArrayList<String>();
    }

    @Override
    public void endDocument() throws SAXException {

    }

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        try {

        	// Check and save state for parent nodes
        	if (localName.equals("station")) {
	        	this.mCurrentNode = localName;
	        }
        	else if (localName.equals("name")) {
	        	this.mCurrentNode = localName;
	        }
        	else if (localName.equals("abbr")) {
	        	this.mCurrentNode = localName;
	        }
        	else {
        		this.mCurrentNode = null;
        	}
        	
        } catch (Exception ee) {
        	//TODO: clean up exception
            Log.d("error in startElement", ee.getStackTrace().toString());
        }
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

    }


    @Override
    public void characters(char ch[], int start, int length) {

    	String info = new String(ch, start, length);
   
    	if (this.mCurrentNode != null){
    		if (this.mCurrentNode.equals("name")) {
    			this.nameList.add(info);
            }
    		else if (this.mCurrentNode.equals("abbr")) {
        		this.abbrList.add(info);
            }
    		this.mCurrentNode = null;// reset since value saved
    	}
    	
    }
    
}
