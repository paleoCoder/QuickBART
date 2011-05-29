/**
 *  Copyright (c) 2011 Scott Truger
 *  See the file COPYING.txt for copying permissions
 **/

package com.trugertech.quickbart;

import android.os.Parcel;
import android.os.Parcelable;


public class FavoriteTripLeg implements Parcelable {
	
	private int order;
	private String transferCode;
	private String origin;
	private String destination;
	private String originTime;
	private String destinationTime;
	private String trainHeadStation;
	
	public FavoriteTripLeg() {
		this.order = 0;
		this.transferCode = "";
		this.origin = "";
		this.destination = "";
		this.originTime = "";
		this.destinationTime = "";
		this.trainHeadStation = "";
	}
	
	public FavoriteTripLeg(Parcel in){
		String[] data = new String[7];
		
		in.readStringArray(data);
		
		this.order = Integer.parseInt(data[0]);
		this.transferCode = data[1];
		this.origin = data[2];
		this.destination = data[3];
		this.originTime = data[4];
		this.destinationTime = data[5];
		this.trainHeadStation = data[6];
	}
			
	public int getOrder() {
		return order;
	}


	public void setOrder(int order) {
		this.order = order;
	}


	public String getTransferCode() {
		return transferCode;
	}


	public void setTransferCode(String transferCode) {
		this.transferCode = transferCode;
	}


	public String getOrigin() {
		return origin;
	}


	public void setOrigin(String origin) {
		this.origin = origin;
	}


	public String getDestination() {
		return destination;
	}


	public void setDestination(String destination) {
		this.destination = destination;
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


	public String getTrainHeadStation() {
		return trainHeadStation;
	}


	public void setTrainHeadStation(String trainHeadStation) {
		this.trainHeadStation = trainHeadStation;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		String[] data = {
				Integer.toString(this.order),
				this.transferCode,
				this.origin,
				this.destination,
				this.originTime,
				this.destinationTime,
				this.trainHeadStation
		};
		
		dest.writeStringArray(data);
	}
	
	public static final Parcelable.Creator<FavoriteTripLeg> CREATOR
		= new Parcelable.Creator<FavoriteTripLeg>() {
		public FavoriteTripLeg createFromParcel(Parcel In){
			return new FavoriteTripLeg(In);
		}
		public FavoriteTripLeg[] newArray(int size){
			return new FavoriteTripLeg[size];
		}
	};

}
