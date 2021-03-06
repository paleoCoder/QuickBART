/**
 *  Copyright (c) 2011 Scott Truger
 *  See the file COPYING.txt for copying permissions
 **/

/**
 * 
 */

package com.trugertech.quickbart;

import java.util.ArrayList;

/**
 * @author Scott
 *
 */
public class FavoriteRoute {
	
	// class variables
	protected String fare;
	protected String destination;
	protected String origin;
	protected String originTime;
	protected String destinationTime;
	protected ArrayList<FavoriteTripLeg> tripLegs;
		
	
	/**
	 * Class to provide data container for Favorite Routes for BART
	 */
	protected FavoriteRoute() {
		this.fare = "";
		this.destination = "";
		this.origin = "";
		this.originTime = null;
		this.destinationTime = null;
		this.tripLegs = new ArrayList<FavoriteTripLeg>();
	}

	public String getFare() {
		return fare;
	}

	public void setFare(String fare) {
		this.fare = fare;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getOriginTime() {
		return originTime;
	}

	public void setOriginTime(String originTime) {
		this.originTime = originTime;
	}

	public String getDestinationTime() {
		return destinationTime;
	}

	public void setDestinationTime(String destinationTime) {
		this.destinationTime = destinationTime;
	}

	public ArrayList<FavoriteTripLeg> getTripLegs() {
		return tripLegs;
	}

	public void setTripLegs(ArrayList<FavoriteTripLeg> tripLegs) {
		this.tripLegs = tripLegs;
	}

}
