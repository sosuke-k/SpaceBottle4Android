package com.spacebottle.models;

public class Device {
	@com.google.gson.annotations.SerializedName("id")
	private String mId;

	@com.google.gson.annotations.SerializedName("os")
	private String mOs = "android";

	@com.google.gson.annotations.SerializedName("user_id")
	private String mUserId;

	@com.google.gson.annotations.SerializedName("registration_id")
	private String mRegistrationId;

	public Device(){}

	public Device(String registration_id){
		mRegistrationId = registration_id;
	}

	public Device(String registration_id, String userId, String id){
		mRegistrationId = registration_id;
		mUserId = userId;
		mId = id;
	}

	public String toString(){
		return getRegistrationId();
	}

	public String getId(){
		return mId;
	}

	public void serId(String id){
		mId = id;
	}

	public String getUserId(){
		return mUserId;
	}

	public void setUserId(String userId){
		mUserId = userId;
	}

	public String getRegistrationId(){
		return mRegistrationId;
	}

	public void setRegistrationId(String registrationId)
	{
		mRegistrationId = registrationId;
	}

	@Override
	public boolean equals(Object o){
		return mRegistrationId.equals( ((Device)o).getRegistrationId() );
	}

}
