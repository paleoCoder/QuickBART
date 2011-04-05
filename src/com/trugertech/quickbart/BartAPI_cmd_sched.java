package com.trugertech.quickbart;

import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

public class BartAPI_cmd_sched extends DefaultHandler {
	
    private ArrayList<FavoriteRoute> favoriteRoutes;
    private FavoriteRoute route;
    private FavoriteTripLeg routeLeg;
    
    private String[][] stations;

//    private String parentNode;
//    private String currentNode;

    
    BartAPI_cmd_sched() {
    }

    /**
     * Gets the BART route listing as a favorite route object.
     * The first array is the long names of the stations.
     * The second array is the abbreviated names of the stations.
     * @return
     */
    ArrayList<FavoriteRoute> getResults()
    {
    	return favoriteRoutes;
    }
    
    @Override
    public void startDocument() throws SAXException {
    	// initialize favorite route container
    	route = null;
    	favoriteRoutes = new ArrayList<FavoriteRoute>();
    	
    	try{
    		this.stations = BartAPI_cmd_stn.getStations();
    	}
    	catch(Exception e){
    		//TODO: log error to system log
    		this.stations = new String[][] {{""},{""}};
    	}
    	
    }

    @Override
    public void endDocument() throws SAXException {
    	//Save last found route
    	this.favoriteRoutes.add(route);
    }

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {

        try {
        	// Check and save state for parent nodes
        	if (localName.equals("trip")) {
        		
        		// check for previous route
        		if(this.route != null){
        			//if previous route save to favorite routes
        			this.favoriteRoutes.add(route);
        		}
        		
        		//create new route
        		this.route = new FavoriteRoute();

        		//Save off attributes of route
        		this.route.setOrigin(atts.getValue("origin"));
        		this.route.setDestination(atts.getValue("destination"));
        		this.route.setFare(atts.getValue("fare"));
        		this.route.setOriginTime(atts.getValue("origTimeMin"));
        		this.route.setDestinationTime(atts.getValue("destTimeMin"));
        		
	        }
        	else if (localName.equals("leg")) {
        		
				//create new leg to save attributes
				this.routeLeg = new FavoriteTripLeg();
        		
				//save attributes of trip
	    		this.routeLeg.setOrigin(BartAPI_cmd_stn.getStationLongName(stations, atts.getValue("origin")));
	        	this.routeLeg.setDestination(BartAPI_cmd_stn.getStationLongName(stations, atts.getValue("destination")));
	        	this.routeLeg.setOriginTime(atts.getValue("origTimeMin"));
	        	this.routeLeg.setDestinationTime(atts.getValue("destTimeMin"));
	        	this.routeLeg.setOrder(Integer.parseInt(atts.getValue("order")));
	        	this.routeLeg.setTransferCode(atts.getValue("transfercode"));
	        	this.routeLeg.setTrainHeadStation(BartAPI_cmd_stn.getStationLongName(stations, atts.getValue("trainHeadStation")));
	        					
				//add trip leg to current route
				this.route.tripLegs.add(this.routeLeg);
	        }

        } catch (Exception ee) {
            Log.d("error in startElement", ee.getStackTrace().toString());
        }
    }

    @Override
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {

    }


    @Override
    public void characters(char ch[], int start, int length) {

//    	String info = new String(ch, start, length);   	
    	
    	
    }

}
