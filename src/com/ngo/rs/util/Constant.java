package com.ngo.rs.util;

import android.content.Context;
import android.os.Environment;

public class Constant {
	
	//APPLICATION PREFERENCE FILE NAME
    public static String PREF_FILE = "PREF_ROTARYCLUB_APP";
	
   //APPLICATION CONTEXT OBJECT
    public static Context CONTEXT;
    
   //SD card directory name or path
 	public static String SD_CARD_PATH=Environment.getExternalStorageDirectory().getPath()+"/RotaryClub";
 	
   //Preference name to store last project id
   public static String PREF_LAST_PROJECT_ID="PREF_LAST_PROJECT_ID";
   		
 //=================== APPLICATION CONSTANT ================================
  	
	public static boolean ISUPDATEMODE=false;
	
	public static boolean ISFINISH=false;
	
	public static String PROJECT_NAME="";
	
	public static String PREF_PRESIDENT="pref_president";
	public static String PREF_RCC="pref_rcc";
	public static String PREF_SECRETORY="pref_secretory";

	public static String PREF_IS_AUTHENTICATION_CREATED="pref_authentication";
	public static String PREF_USER_ID="pref_userid";
	public static String PREF_PASSWORD="pref_password";

}