package com.spacebottle.models;

public class Destination {
	@com.google.gson.annotations.SerializedName("id")
	private String mId;
	
	@com.google.gson.annotations.SerializedName("message_id")
	private String mMessageId;
	
	@com.google.gson.annotations.SerializedName("user_id")
	private String mUserId;
	
	public String getId(){
		return mId;
	}
	
	public void setId(String id){
		mId = id;
	}
	
	public String getMessageId(){
		return mMessageId;
	}
	
	public void setMessageId(String messageId){
		mMessageId = messageId;
	}
	
	public String getUserId(){
		return mUserId;
	}
	
	public void setUserId(String userId){
		mUserId = userId;
	}
}
