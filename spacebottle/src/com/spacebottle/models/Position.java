package com.spacebottle.models;

import java.io.Serializable;

public class Position implements Serializable {
	@com.google.gson.annotations.SerializedName("id")
	private String mId;
	
	@com.google.gson.annotations.SerializedName("user_id")
	private String mUserId;
	
	@com.google.gson.annotations.SerializedName("latitude")
	private double mLatitude;
	
	@com.google.gson.annotations.SerializedName("longitude")
	private double mLongitude;
	
	
	public String getId(){
		return mId;
	}
	
	public void setId(String id){
		mId = id;
	}
	
	public String getUserId(){
		return mId;
	}
	
	public void setUserId(String userId){
		mUserId = userId;
	}
	
	public double getLatitude(){
		return mLatitude;
	}
	
	public void setLatitude(double latitude){
		mLatitude = latitude;
	}
	
	public double getLogitude(){
		return mLongitude;
	}
	public void setLogitude(double longitude){
		mLongitude = longitude;
	}
}
