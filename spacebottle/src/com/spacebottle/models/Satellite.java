package com.spacebottle.models;

public class Satellite {
	@com.google.gson.annotations.SerializedName("id")
	private String mId;
	
	@com.google.gson.annotations.SerializedName("name")
	private String mName;
	
	@com.google.gson.annotations.SerializedName("satellite_number")
	private String mSatelliteNumber;
	
	@com.google.gson.annotations.SerializedName("international_designator")
	private String mInternationalDesignator;
	
	@com.google.gson.annotations.SerializedName("epoch_date")
	private String mEpochDate;
	
	@com.google.gson.annotations.SerializedName("d_mean_motion")
	private String mDMeanMotion;
	
	@com.google.gson.annotations.SerializedName("dd_mean_motion")
	private String mDDMeanMotion;
	
	@com.google.gson.annotations.SerializedName("element_number")
	private String mElementNumber;
	
	@com.google.gson.annotations.SerializedName("inclination")
	private String mInclination;
	
	@com.google.gson.annotations.SerializedName("ascension")
	private String mAscension;
	
	@com.google.gson.annotations.SerializedName("eccentricity")
	private String mEccentricity;
	
	@com.google.gson.annotations.SerializedName("arg_of__perigee")
	private String mArgOfPerigee;
	
	@com.google.gson.annotations.SerializedName("mean_anomaly")
	private String mMeanAnomaly;
	
	@com.google.gson.annotations.SerializedName("mean_motion")
	private String mMeanMotion;
	
	@com.google.gson.annotations.SerializedName("revolution_number")
	private String mRevolutionNumber;
	
	public String getId(){
		return mId;
	}
	public void setId(String id){
		mId = id;
	}
	public String getName(){
		return mName;
	}
	public void setName(String name){
		mName = name;
	}
	public String getSatelliteNumber(){
		return mSatelliteNumber;
	}
	public void setSatelliteNumber(String satelliteNumber){
		mSatelliteNumber = satelliteNumber;
	}
	public String getInternationalDesignator(){
		return mInternationalDesignator;
	}
	public void setInternationalDesignator(String internationalDesignator){
		mInternationalDesignator = internationalDesignator;
	}
	public String getEpochDate(){
		return mEpochDate;
	}
	public void setEpochDate(String epochDate){
		mEpochDate = epochDate;
	}
	public String getDMeanMotion(){
		return mDMeanMotion;
	}
	public void setDMeanMotion(String dMeanMotion){
		mDMeanMotion = dMeanMotion;
	}
	public String getDDMeanMotion(){
		return mDDMeanMotion;
	}
	public void setDDMeanMotion(String ddMeanMotion){
		mDDMeanMotion = ddMeanMotion;
	}
	public String getElementNumber(){
		return mElementNumber;
	}
	public void setElementNumber(String elementNumber){
		mElementNumber = elementNumber;
	}
	public String getInclination(){
		return mInclination;
	}
	public void setInclination(String inclination){
		mInclination = inclination;
	}
	public String getAscension(){
		return mAscension;
	}
	public void setAscension(String ascension){
		mAscension = ascension;
	}
	public String getEccentricity(){
		return mEccentricity;
	}
	public void setEccentricity(String eccentricity){
		mEccentricity = eccentricity;
	}
	public String getArgOfPerigee(){
		return mArgOfPerigee;
	}
	public void setArgOfPerigee(String argOfPerigee){
		mArgOfPerigee = argOfPerigee;
	}
	public String getMeanAnomaly(){
		return mMeanAnomaly;
	}
	public void setMeanAnomaly(String meanAnomaly){
		mMeanAnomaly = meanAnomaly;
	}
	public String getMeanMotion(){
		return mMeanMotion;
	}
	public void setMeanMotion(String meanMotion){
		mMeanMotion = meanMotion;
	}
	public String getRevolutionNumber(){
		return mRevolutionNumber;
	}
	public void setRevolutionNumber(String revolutionNumber){
		mRevolutionNumber = revolutionNumber;
	}
}
