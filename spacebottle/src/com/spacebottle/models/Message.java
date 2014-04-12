package com.spacebottle.models;

public class Message {
	@com.google.gson.annotations.SerializedName("id")
	private String mId;
	
	@com.google.gson.annotations.SerializedName("text")
	private String mText;
	
	@com.google.gson.annotations.SerializedName("latitude")
	private double mLatitude;
	
	@com.google.gson.annotations.SerializedName("longitude")
	private double mLongitude;
	
	@com.google.gson.annotations.SerializedName("user_id")
	private String mUserId;
	
	@com.google.gson.annotations.SerializedName("satellite_id")
	private String mSatelliteId;
	
	@com.google.gson.annotations.SerializedName("ticket_id")
	private String mTicketId;
	
	@com.google.gson.annotations.SerializedName("is_anonymous")
	private boolean mAnonymous;
	
	public String getId(){
		return mId;
	}
	public void setId(String id){
		mId = id;
	}
	public String getText(){
		return mText;
	}

	public void setText(String text){
		mText = text;
	}
	public double getLatitude(){
		return mLatitude;
	}
	public void setLatitude(double latitude){
		mLatitude = latitude;
	}
	public double getLongitude(){
		return mLongitude;
	}
	public void setLongitude(double longitude){
		mLongitude = longitude;
	}
	public String getUserId(){
		return mUserId;
	}
	public void setUserId(String userId){
		mUserId = userId;
	}
	public String getSatelliteId(){
		return mSatelliteId;
	}
	public void setSatelliteId(String satelliteId){
		mSatelliteId = satelliteId;
	}
	public String getTicketId(){
		return mTicketId;
	}
	public void setTicketId(String ticketId){
		mTicketId = ticketId;
	}
	public boolean isAnonymous(){
		return mAnonymous;
	}
	public void setAnonymous(boolean isAnonymous){
		mAnonymous = isAnonymous;
	}
}
