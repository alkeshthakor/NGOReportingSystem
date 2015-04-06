package com.ngo.rs.util;

import java.io.File;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHandler extends SQLiteOpenHelper {
	
	// Application Database Version Number
	private static final int DATABASE_VERSION=1;
	// Application Database name
	private static final String DATABASE_NAME="rotaryclubapp";
	
	//Instance of this class
	private static DatabaseHandler sInstance;
	
	// Report master table name
	private static final String TABLE_REPORT_MASTER="tbl_report_master";		
	
	
    // Report master table column name
	public static final String KEY_REPORT_ID="pk_report_id";
	public static final String KEY_TITLE="title";
	public static final String KEY_LOCATION="location";
	public static final String KEY_CHAIRMAN="chairman";
	public static final String KEY_DATE="date";
	public static final String KEY_DETAIL_DESC="detail_desc";
	public static final String KEY_IMAGE_PATH="image_path";
	public static final String KEY_CLUB_NAME="club_name";
	public static final String KEY_STATUS="status";
	public static final String KEY_MODIFY="modify";
	public static final String KEY_REPORT_NAME="report_name";
	public static final String KEY_REPORT_CREATED="report_created";
	
		 
	// String statement for create TABLE_REPORT_MASTER table structure
	private static final String CREATE_REPORT_MASTER_TABLE="CREATE TABLE "+TABLE_REPORT_MASTER+" ( "
			+KEY_REPORT_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
			+KEY_TITLE+" TEXT,"
			+KEY_LOCATION+" TEXT,"
			+KEY_CHAIRMAN+" TEXT,"
			+KEY_DATE+" DATE,"
			+KEY_DETAIL_DESC+" TEXT,"
			+KEY_IMAGE_PATH+" TEXT,"
			+KEY_CLUB_NAME+" TEXT,"
			+KEY_STATUS+" TEXT Default 1,"
			+KEY_MODIFY+" DATE , "
			+KEY_REPORT_NAME+" TEXT ,"
			+KEY_REPORT_CREATED+" TEXT Default false "+" ) ";
	
			
	/**
	 *  Returns an Instance of DatabaseHandler. Singleton Pattern.
	 *  
	 * @param context an Object of Application Context
	 * 
	 * @return an Object of DatabaseHandler
	 */
	public static DatabaseHandler getInstance(Context context) {
	    // Use the application context, which will ensure that you 
	    // don't accidentally leak an Activity's context.
	    // See this article for more information: http://bit.ly/6LRzfx
	    if (sInstance == null) {
	      sInstance = new DatabaseHandler(context.getApplicationContext());
	    }
	    return sInstance;
	  }
	
	public DatabaseHandler(Context context) {
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
	}
	
	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
	    db.execSQL(CREATE_REPORT_MASTER_TABLE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORT_MASTER);
       /// db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEOPLE);
        // Create tables again
        onCreate(db);
	}
	
	public double addNewProject(ContentValues values){
		double row = 0;
		try {
			SQLiteDatabase db=this.getWritableDatabase();
			row = (int) db.insert(TABLE_REPORT_MASTER,null,values);
			db.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return row;
	}
	
	
	public double updateProject(ContentValues values,String reportId){
		double row = 0;
		try {
			SQLiteDatabase db=this.getWritableDatabase();
			row = (int) db.update(TABLE_REPORT_MASTER,values,KEY_REPORT_ID+"="+reportId,null);
			db.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return row;		
	}
	
	
	public ArrayList<DataItem> getReportList(){
		ArrayList<DataItem> reportList = new ArrayList<DataItem>();;
		DataItem reportItem;
		SQLiteDatabase db=this.getReadableDatabase();
		String selectQuery="Select * from "+TABLE_REPORT_MASTER;
		try {
			Cursor mCursor=db.rawQuery(selectQuery,null);
			if(mCursor!=null&&mCursor.getCount()>0){
				mCursor.moveToFirst();
				for(int i=0;i<mCursor.getCount();i++){
					reportItem=new DataItem();
					reportItem.setmReportId(mCursor.getString(0));
					reportItem.setmTitle(mCursor.getString(1));
					reportItem.setmLocation(mCursor.getString(2));
					reportItem.setmChairman(mCursor.getString(3));
					reportItem.setmDate(mCursor.getString(4));
					reportItem.setmDescription(mCursor.getString(5));
					reportItem.setmImagePath(mCursor.getString(6));
					reportItem.setmClubName(mCursor.getString(7));
					reportItem.setmStatus(mCursor.getString(8));
					reportItem.setmModifyDate(mCursor.getString(9));
					
					reportList.add(reportItem);
					mCursor.moveToNext();
					
				}
				Log.d("No of Record: ",""+reportList.size());
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reportList;
	}
	
	public ArrayList<DataItem> getLocalReportList(){
		ArrayList<DataItem> reportList = new ArrayList<DataItem>();;
		DataItem reportItem;
		SQLiteDatabase db=this.getReadableDatabase();
		String selectQuery="Select * from "+TABLE_REPORT_MASTER+" where "+KEY_REPORT_CREATED+"='true'";
		try {
			Cursor mCursor=db.rawQuery(selectQuery,null);
			if(mCursor!=null&&mCursor.getCount()>0){
				mCursor.moveToFirst();
				for(int i=0;i<mCursor.getCount();i++){
					
					String path=ImageUtil.createMyImagesDir().getAbsolutePath()+"/"+mCursor.getString(10)+".pdf";

					if(new File(path).exists())
					{
						reportItem=new DataItem();
						reportItem.setmReportId(mCursor.getString(0));
						reportItem.setmTitle(mCursor.getString(1));
						reportItem.setmLocation(mCursor.getString(2));
						reportItem.setmChairman(mCursor.getString(3));
						reportItem.setmDate(mCursor.getString(4));
						reportItem.setmDescription(mCursor.getString(5));
						reportItem.setmImagePath(mCursor.getString(6));
						reportItem.setmClubName(mCursor.getString(7));
						reportItem.setmStatus(mCursor.getString(8));
						reportItem.setmModifyDate(mCursor.getString(9));
						reportItem.setmLocalReportName(mCursor.getString(10));
						reportItem.setmIsLocalReportCreated(mCursor.getString(11));
						reportList.add(reportItem);	
					}
					
					mCursor.moveToNext();
					
				}
				Log.d("No of Record: ",""+reportList.size());
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reportList;
	}

	public ArrayList<DataItem> getServerReportList(){
		ArrayList<DataItem> reportList = new ArrayList<DataItem>();;
		DataItem reportItem;
		SQLiteDatabase db=this.getReadableDatabase();
		String selectQuery="Select * from "+TABLE_REPORT_MASTER+" where "+KEY_REPORT_CREATED+"='true'";
		try {
			Cursor mCursor=db.rawQuery(selectQuery,null);
			if(mCursor!=null&&mCursor.getCount()>0){
				mCursor.moveToFirst();
				for(int i=0;i<mCursor.getCount();i++){
					
					if(mCursor.getString(8).equalsIgnoreCase("0")){
					String path=ImageUtil.createMyImagesDir().getAbsolutePath()+"/"+mCursor.getString(10)+".pdf";
					if(new File(path).exists())
					{
						reportItem=new DataItem();
						reportItem.setmReportId(mCursor.getString(0));
						reportItem.setmTitle(mCursor.getString(1));
						reportItem.setmLocation(mCursor.getString(2));
						reportItem.setmChairman(mCursor.getString(3));
						reportItem.setmDate(mCursor.getString(4));
						reportItem.setmDescription(mCursor.getString(5));
						reportItem.setmImagePath(mCursor.getString(6));
						reportItem.setmClubName(mCursor.getString(7));
						reportItem.setmStatus(mCursor.getString(8));
						reportItem.setmModifyDate(mCursor.getString(9));
						reportItem.setmLocalReportName(mCursor.getString(10));
						reportItem.setmIsLocalReportCreated(mCursor.getString(11));
						reportList.add(reportItem);	
					}
					
					}
					
					mCursor.moveToNext();
					
				}
				Log.d("No of Record: ",""+reportList.size());
			}			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reportList;
	}

	
	public DataItem getDetailReport(String reportId){
		
		DataItem reportItem;
		
		SQLiteDatabase db=this.getReadableDatabase();
		String selectQuery="Select * from "+TABLE_REPORT_MASTER+" where "+KEY_REPORT_ID+"='"+reportId+"'";
		try {
			Cursor mCursor=db.rawQuery(selectQuery,null);
			if(mCursor!=null&&mCursor.getCount()>0){
				mCursor.moveToFirst();
					reportItem=new DataItem();
					reportItem.setmReportId(mCursor.getString(0));
					reportItem.setmTitle(mCursor.getString(1));
					reportItem.setmLocation(mCursor.getString(2));
					reportItem.setmChairman(mCursor.getString(3));
					reportItem.setmDate(mCursor.getString(4));
					reportItem.setmDescription(mCursor.getString(5));
					reportItem.setmImagePath(mCursor.getString(6));
					reportItem.setmClubName(mCursor.getString(7));
					reportItem.setmStatus(mCursor.getString(8));
					reportItem.setmModifyDate(mCursor.getString(9));
					
					return reportItem;
										
			}			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
		
//	/**This method used to add new Survey Record in table tbl_survey_info
//	 * @param values is an Object of ContentValues which contains new survey data 
//	 * @return  the newly inserted row id , or -1 if an error occurred default is return is 0
//	 */
//	public double addNewSurvey(ContentValues values){
//		double row = 0;
//		try {
//			SQLiteDatabase db=this.getWritableDatabase();
//			row = (int) db.insert(TABLE_SURVEY_INFO,null,values);
//			db.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return row;
//	}
//	
//	/**This method used to add new Fixtures Record in table tbl_fixtures_info
//	 * 
//	 * @param values is an Object of ContentValues which contains new fixtures data 
//	 * @return  the newly inserted row id , or -1 if an error occurred default is return is 0
//	 */
//	public double addNewFixtures(ContentValues values){
//		double row = 0;
//		try {
//			SQLiteDatabase db=this.getWritableDatabase();
//			row = (int) db.insert(TABLE_FIXTURES_INFO,null,values);
//			db.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return row;
//	}
//	
//	/**
//	 * This method used to update Fixtures Record in table tbl_fixtures_info 
//	 * 
//	 * @param values is an Object of ContentValues which contains new fixtures data
//	 * @param fid is Fixtures Id
//	 * @return the number of rows affected 
//	 */
//	public int updateFixtures(ContentValues values,String fid){
//		int row = 0;
//		try {
//			SQLiteDatabase db=this.getWritableDatabase();
//			row=db.update(TABLE_FIXTURES_INFO, values,KEY_FIXTURE_ID+"="+fid,null);
//			db.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return row;
//	}
//	
//	
//	/**
//	 *  This method used to delete all Survey Record from table tbl_survey_info which has sid 
//	 *  
//	 * @param sid SurveyId
//	 * 
//	 * @return number of deleted rows, 0 otherwise. 
//	 */
//	public int deleteSurvey(String sid){
//		int row = 0;
//		try {
//			SQLiteDatabase db=this.getWritableDatabase();
//			row = (int) db.delete(TABLE_SURVEY_INFO,KEY_SID+"="+sid,null);
//			db.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return row;
//	}
//		
//	/**
//	 *  This method used to delete Floor Record from tbl_floor_info which has fid and it also delete all Area record 
//	 *  from tbl_area_info which has fid reference.
//	 *  
//	 * @param fid Floor Id
//	 * 
//	 * @return number of deleted row 
//	 */
//	public int deleteFloorWithChiledRecord(String fid){
//		int rowss=0;
//		SQLiteDatabase db=this.getReadableDatabase();
//		String selectAreaQuery="Select a_id from "+TABLE_AREA_INFO+" where "+KEY_FID+"="+fid;
//		try {
//					Cursor aCursor=db.rawQuery(selectAreaQuery,null);
//					if(aCursor!=null&&aCursor.getCount()>0){
//						aCursor.moveToFirst();
//						for(int j=0;j<aCursor.getCount();j++){					
//							int row=db.delete(TABLE_FIXTURES_INFO,KEY_AID+"="+aCursor.getString(0),null);
//							Log.d("AID "+aCursor.getString(0)+" No of Fix Deleted: ", row+"");
//							aCursor.moveToNext();			
//						}	
//					}
//					db.delete(TABLE_AREA_INFO,KEY_FID+"="+fid,null);
//					db.delete(TABLE_FLOOR_INFO,KEY_FID+"="+fid,null);			
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return rowss;
//	}
//
//	/**
//	 *  This method used to deleted Area record from tbl_area_info which has aid and also delete all fixture record from tbl_fixtures_info which has aid reference
//	 *   
//	 * @param aid Area Id 
//	 * 
//	 * @return number of delete area record 
//	 */
//	public int deleteAreaWithChiledRecord(String aid){
//		int rowss=0;
//		SQLiteDatabase db=this.getReadableDatabase();
//		try {					
//				db.delete(TABLE_FIXTURES_INFO,KEY_AID+"="+aid,null);
//				rowss=db.delete(TABLE_AREA_INFO,KEY_AID+"="+aid,null);			
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return rowss;
//	}
//
//  /**
//   *  This method used to delete fixture record from tbl_fixtures_info which has fixId
//   *  
// * @param fixId Fixture Id
// * 
// * @return number of deleted row
// * 
// */
//public int deleteFixture(String fixId){
//	int rowss=0;
//	SQLiteDatabase db=this.getReadableDatabase();
//	try {					
//			db.delete(TABLE_FIXTURES_INFO,KEY_FIXTURE_ID+"="+fixId,null);			
//	} catch (SQLException e) {
//		e.printStackTrace();
//	}
//	return rowss;
//  }
//		
//	/**
//	 *  This method used to delete all Survey Record from table tbl_survey_info which has sid and
//	 *  delete record from tbl_flor_info, tbl_area_info and tbl_fixtures_info which contains its reference
//	 *  
//	 * @param sid SurveyId
//	 * 
//	 * @return the number of rows affected, 0 otherwise. 
//	 */
//	public int deleteSurveyWithChiledRecord(String sid){
//		int rowss=0;
//		
//		SQLiteDatabase db=this.getReadableDatabase();
//		String selectQuery="Select f_id from "+TABLE_FLOOR_INFO+" where "+KEY_SID+"="+sid;
//		String selectAreaQuery="Select a_id from "+TABLE_AREA_INFO+" where "+KEY_FID+"=";
//		try {
//			Cursor fCursor=db.rawQuery(selectQuery,null);
//			if(fCursor!=null&&fCursor.getCount()>0){
//				fCursor.moveToFirst();			
//				for(int i=0;i<fCursor.getCount();i++){
//					String areaQuery=selectAreaQuery+fCursor.getString(0);
//					Cursor aCursor=db.rawQuery(areaQuery,null);
//					
//					if(aCursor!=null&&aCursor.getCount()>0){
//						aCursor.moveToFirst();
//						for(int j=0;j<aCursor.getCount();j++){
//							int row=db.delete(TABLE_FIXTURES_INFO,KEY_AID+"="+aCursor.getString(0),null);
//							Log.d("AID "+aCursor.getString(0)+" No of Fix Deleted: ", row+"");
//							aCursor.moveToNext();
//						}
//					}
//					int rows=db.delete(TABLE_AREA_INFO,KEY_FID+"="+fCursor.getString(0),null);
//					Log.d("FID "+fCursor.getString(0)+" No of Area Deleted: ", rows+"");
//					fCursor.moveToNext();
//				}
//			}
//			int rows=db.delete(TABLE_FLOOR_INFO,KEY_SID+"="+sid,null);
//			Log.d("SID "+sid+" No of Floor Deleted: ", rows+"");
//			rowss=db.delete(TABLE_SURVEY_INFO,KEY_SID+"="+sid,null);
//			Log.d("SID "+sid+" No of Survey Deleted ", rowss+"");
//			
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return rowss;
//	}
//
//	/**
//	 * This method used to update Survey Record in table tbl_survey_info 
//	 * 
//	 * @param values is an Object of ContentValues which contains new Survey data
//	 * @param sid is Survey Id
//	 * @return the number of rows affected 
//	 */
//  public int updateSurvey(ContentValues values,String sid){
//		int row = 0;
//		try {
//			SQLiteDatabase db=this.getWritableDatabase();
//			row=db.update(TABLE_SURVEY_INFO, values,KEY_SID+"="+sid,null);
//			db.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return row;
//	}
//  
//   /**This method used to add new Floor Record in table tbl_floor_info
//	 * 
//	 * @param values is an Object of ContentValues which contains new Floor data 
//	 * 
//	 * @return  the newly inserted row id , or -1 if an error occurred default is return is 0
//	 */
//	public double addNewFloor(ContentValues values){
//		double row = 0;
//		try {
//			SQLiteDatabase db=this.getWritableDatabase();
//			row = (int) db.insert(TABLE_FLOOR_INFO,null,values);
//			db.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return row;
//	}
//	
//	/** This method used to update Floor table record which has fid.
//	 * 
//	 * @param values contains data which you want to update
//	 * 
//	 * @param fid Floor Id
//	 * 
//	 * @return row number 
//	 */
//	public int updateFloor(ContentValues values,String fid){
//		int row = 0;
//		try {
//			SQLiteDatabase db=this.getWritableDatabase();
//			row=db.update(TABLE_FLOOR_INFO, values,KEY_FID+"="+fid,null);
//			db.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return row;
//	}
//	
//	/**  This method used to update Area record which has aid.
//	 * 
//	 * @param values contains data which you want to update
//	 * 
//	 * @param aid Area Id
//	 * 
//	 * @return row number
//	 */
//	public int updateArea(ContentValues values,String aid){
//		int row = 0;
//		try {
//			SQLiteDatabase db=this.getWritableDatabase();
//			row=db.update(TABLE_AREA_INFO, values,KEY_AID+"="+aid,null);
//			db.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return row;
//	}
//	
//	
//	 /**This method used to add new Area Record in table tbl_area_info
//		 * 
//		 * @param values is an Object of ContentValues which contains new Area data 
//		 * 
//		 * @return  the newly inserted row id , or -1 if an error occurred default is return is 0
//		 */
//	public double addArea(ContentValues values){
//		double row = 0;
//		try {
//			SQLiteDatabase db=this.getWritableDatabase();
//			row = (int) db.insert(TABLE_AREA_INFO,null,values);
//			db.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return row;
//	}
//	
//	/** This method used to get Survey list data based on status parameter
//	 * 
//	 * @param status should be 1 or 0 means open and closed respectivly 
//	 * 
//	 * @return an object of list of Survey type
//	 */
	
//	
//	/**
//	 * This method used to get Fixtures List data based on areaid parameter
//	 * 
//	 * @param areaId AreaId 
//	 * 
//	 * @return an object of list of Fixtures type
//	 */
//	public ArrayList<Fixtures> getFixturesList(String areaId){
//		ArrayList<Fixtures> fixturesList = new ArrayList<Fixtures>();;
//		Fixtures fixItem;
//		SQLiteDatabase db=this.getReadableDatabase();
//		String selectQuery="Select * from "+TABLE_FIXTURES_INFO+" where "+KEY_AID+"="+areaId;
//		try {
//			Cursor sCursor=db.rawQuery(selectQuery,null);
//			if(sCursor!=null&&sCursor.getCount()>0){
//				sCursor.moveToFirst();
//				for(int i=0;i<sCursor.getCount();i++){
//					fixItem=new Fixtures(sCursor.getString(0),sCursor.getString(1), sCursor.getString(2),sCursor.getString(3),
//							sCursor.getString(4),sCursor.getString(5),sCursor.getString(6),sCursor.getString(7),
//							sCursor.getString(8),sCursor.getString(9),sCursor.getString(10),sCursor.getString(11),sCursor.getString(12),sCursor.getString(13),sCursor.getString(14));
//					fixturesList.add(fixItem);
//					sCursor.moveToNext();
//				}
//				Log.d("No of Record in Surver: ",""+fixturesList.size());
//			}			
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return fixturesList;
//	}
//	
//	/** This method used to get Survey Detail of specific survey from tbl_survey_info which has sid
//	 * 
//	 * @param sid Survey Id
//	 * 
//	 * @return Survey object which contain survey data.
//	 * 
//	 */
//	public Survey getSurveyInfo(String sid){
//		Survey surveyItem = null;
//		SQLiteDatabase db=this.getReadableDatabase();
//		String selectQuery="Select * from "+TABLE_SURVEY_INFO+" where "+KEY_SID+"="+sid;
//		try {
//			Cursor sCursor=db.rawQuery(selectQuery,null);
//			if(sCursor!=null&&sCursor.getCount()>0){
//				sCursor.moveToFirst();
//					surveyItem=new Survey(sCursor.getString(0),sCursor.getString(1), sCursor.getString(2),sCursor.getString(3),
//							sCursor.getString(4),sCursor.getString(5),sCursor.getString(6),sCursor.getString(7),
//							sCursor.getString(8),sCursor.getString(9),sCursor.getString(10),sCursor.getString(11),sCursor.getString(12),sCursor.getString(13));
//				}		
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return surveyItem;
//	}
//	
//	/** This method used to get Floor List data based on sid parameter
//	 * 
//	 * @param sid Survey Id
//	 * 
//	 * @return an object of list of Floor type
//	 */
//	public ArrayList<Floor> getFloorList(String sid){
//		ArrayList<Floor> floorList = null;
//		int areaCount=0;
//		int fixCount=0;
//		
//		Floor floorItem;
//		SQLiteDatabase db=this.getReadableDatabase();
//		String selectQuery="Select * from "+TABLE_FLOOR_INFO+" where "+KEY_SID+"="+sid;
//		String selectArea="Select a_id from "+TABLE_AREA_INFO+" where "+KEY_FID+"=";
//		
//		try {
//			
//			Cursor sCursor=db.rawQuery(selectQuery,null);
//		
//			if(sCursor!=null&&sCursor.getCount()>0){	
//				
//				floorList=new ArrayList<Floor>();
//			
//				sCursor.moveToFirst();
//				
//				for(int i=0;i<sCursor.getCount();i++){
//					
//				   areaCount=0;
//				   
//				   fixCount=0;
//				   
//				   Cursor aCursor=db.rawQuery(selectArea+sCursor.getString(0),null);
//				   
//				   if(aCursor!=null&&aCursor.getCount()>0){
//					   
//					   areaCount=aCursor.getCount();
//					   
//					   aCursor.moveToFirst();
//					   
//					   fixCount=0;
//					   
//					   for(int j=0;j<aCursor.getCount();j++){
//						   
//						   int count=getNumberOfFixtures(aCursor.getString(0));
//						   
//						   fixCount=fixCount+count;
//						   
//						   aCursor.moveToNext();
//					   
//					   }
//					   
//				   }
//					floorItem=new Floor(sCursor.getString(0),sCursor.getString(2),areaCount+"",fixCount+"");
//					floorList.add(floorItem);
//					
//					sCursor.moveToNext();
//				}
//				Log.d("No of Record in Floor Table: ",""+floorList.size());
//			}			
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return floorList;
//	}
//	
//	/** This method used to get List of Area from tble_area_info table which has fid.
//	 * 
//	 * @param fid Fixture Id
//	 * 
//	 * @return list of Area.
//	 * 
//	 */
//	public ArrayList<Area> getAreaList(String fid){
//		ArrayList<Area> areaList = null;
//		Area areaItem;
//		SQLiteDatabase db=this.getReadableDatabase();
//		String selectQuery="Select * from "+TABLE_AREA_INFO+" where "+KEY_FID+"="+fid;
//		try {
//			
//			Cursor sCursor=db.rawQuery(selectQuery,null);
//			
//			if(sCursor!=null&&sCursor.getCount()>0){
//				
//				areaList=new ArrayList<Area>();
//				
//				sCursor.moveToFirst();
//				
//				for(int i=0;i<sCursor.getCount();i++){
//					
//					int count=getNumberOfFixtures(sCursor.getString(0));
//					
//					areaItem=new Area(sCursor.getString(0),sCursor.getString(2),count+"");
//					
//					areaList.add(areaItem);
//					
//					sCursor.moveToNext();
//				}
//				Log.d("No of Record in Floor Table: ",""+areaList.size());
//			}			
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return areaList;
//	}
//
//	/** This method used to calculate number of floor,number of area and number of fixture count in perticular survey. 
//	 * 
//	 * @param sid SurveyID
//	 * 
//	 * @return return an object of HasMap
//	 */
//	public HashMap<String, String> GetCount(String sid){
//		HashMap<String,String> hasMap=new HashMap<String, String>();
//		SQLiteDatabase db=this.getReadableDatabase();
//		String selectQuery="Select f_id from "+TABLE_FLOOR_INFO+" where "+KEY_SID+"="+sid;
//		String selectAreaQuery="Select a_id from "+TABLE_AREA_INFO+" where "+KEY_FID+"=";
//		String selectFixtureQuery="Select fix_id,fix_count from "+TABLE_FIXTURES_INFO+" where "+KEY_AID+"=";
//		int floorCount=0;
//		int areaCount=0;
//		int fixCount=0;
//		int totalfixCount=0;
//		try {
//			Cursor fCursor=db.rawQuery(selectQuery,null);
//			
//			if(fCursor!=null&&fCursor.getCount()>0){
//				floorCount=fCursor.getCount();
//				fCursor.moveToFirst();
//			
//				for(int i=0;i<fCursor.getCount();i++){
//				
//					String areaQuery=selectAreaQuery+fCursor.getString(0);
//					Cursor aCursor=db.rawQuery(areaQuery,null);
//					
//					if(aCursor!=null&&aCursor.getCount()>0){
//					
//						areaCount=areaCount+aCursor.getCount();
//						aCursor.moveToFirst();
//						
//						for(int j=0;j<aCursor.getCount();j++){
//							
//							String fixQuery=selectFixtureQuery+aCursor.getString(0);
//							Log.d("Fix Query:",fixQuery);
//							Cursor fixCursor=db.rawQuery(fixQuery,null);
//							if(fixCursor!=null&&fixCursor.getCount()>0){
//								Log.d("Fix Cursor Count:",fixCursor.getCount()+"");
//								fixCount=fixCount+fixCursor.getCount();
//								fixCursor.moveToFirst();
//								for(int k=0;k<fixCursor.getCount();k++){
//									int value=Integer.parseInt(fixCursor.getString(1));
//									Log.d("Count Value",""+value);
//									totalfixCount=totalfixCount+value;
//									fixCursor.moveToNext();
//								}
//							}
//							aCursor.moveToNext();
//						}
//					}
//					fCursor.moveToNext();
//				}
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		hasMap.put("floor",""+floorCount);
//		hasMap.put("area",""+areaCount);
//		hasMap.put("fixture",""+fixCount);
//		hasMap.put("totalfixcount",""+totalfixCount);
//		
//		return hasMap;
//	}
//	
////	public String getImagePath(String sid){
////		SQLiteDatabase db=this.getReadableDatabase();
////		Cursor imageCursor=db.query(TABLE_IMAGES,new String[]{KEY_PATH},KEY_SID+"="+sid,null,null,null,null);
////		if(imageCursor!=null&&imageCursor.getCount()>0){
////			imageCursor.moveToFirst();
////			Log.d("Image Path",imageCursor.getString(0));
////			return imageCursor.getString(0);
////		}
////		return null;
////	}
//	
//	/**
//	 * This method used to get Number of Survey which Scheduled date is Today date 
//	 * 
//	 * @return count number 
//	 */
//	@SuppressLint("SimpleDateFormat")
//	public int getTodayScheduled(){
//		SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
//		int count=0;
//		try {
//			SQLiteDatabase db=this.getReadableDatabase();
//			Cursor cursor=db.query(TABLE_SURVEY_INFO,new String[]{KEY_SCHEDULED},KEY_STATUS+"=1",null,null,null,null,null);
//			if(cursor!=null&&cursor.getCount()>0){
//			   cursor.moveToFirst();
//			   Date scheduledDate;
//			try {
//				  for(int i=0;i<cursor.getCount();i++){
//		   				scheduledDate = dateFormat.parse(cursor.getString(0));
//		   				String currentdate=dateFormat.format(new Date());
//		   				Date  todayDate=dateFormat.parse(currentdate);
//		   				if(scheduledDate.compareTo(todayDate)<0){
//		   				   }else if(scheduledDate.compareTo(todayDate)>0){
//		   				   }else{
//		   					  Log.d("DatabaseHandle","Scheduled Date is equal");
//		   					  count++; 
//		   				   }
//		               }
//			} catch (ParseException e) {
//				Log.e("DatabaseHandler",e.getMessage());
//				e.printStackTrace();
//			}
//			}
//		} catch (SQLException e) {
//			Log.e("DatabaseHandler",e.getMessage());
//			e.printStackTrace();
//			
//		}
//		return count;
//	}
//	
//	/** This method used to get HTML format String of all information of Survey. This method also 
//	 * responsible for creating HTML file and CSV file of Survey detail.
//	 * 
//	 * @param sid Survey Id
//	 * 
//	 * @return StringBuilder 
//	 * 
//	 */
//	@SuppressLint("DefaultLocale")
//	public StringBuilder getSurveyStringTemplet(String sid){
//		
//		StringBuilder htmlFormate=new StringBuilder();
//		StringBuilder csvFormate = new StringBuilder();
//        String buildingName = null;
//        String floorName;
//        Constant.CSV_URI=null;
//        Constant.HTML_URI=null;
//    	Constant.FIXTURE_IMAGE_LIST=new ArrayList<String>();
//    	
//		String csvColumn= "\"Building Name\",\"Address\",\"Floor\",\"Location\",\"Type\",\"Code\",\"Style\",\"Mount\",\"Control\",\"Height\",\"Total Hours\",\"Option\",\"Description\",\"Count\",\"Hours per Day\",\"Days per Week\"\n";
//		csvFormate.append(csvColumn);
//		
//		htmlFormate.append("<!DOCTYPE html><html><body><table align=\"left\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"font-family: Helvetica,Arial,sans-serif; width: 100%; height: 100%; background: #e6e6e6; color: #5e5e5e; border-collapse: separate; border: 0; margin: 0px; padding: 20px 0px 20px 0px;\"><tbody style=\"vertical-align: top;\">");
//		
//		SQLiteDatabase db=this.getReadableDatabase();
//		
//		String selectSurveyQuery="Select * from "+TABLE_SURVEY_INFO+" where "+KEY_SID+"="+sid;	
//		String selectQuery="Select f_id,floor_description from "+TABLE_FLOOR_INFO+" where "+KEY_SID+"="+sid;
//		String selectAreaQuery="Select a_id,location_name from "+TABLE_AREA_INFO+" where "+KEY_FID+"=";
//		String selectFixtureQuery="Select * from "+TABLE_FIXTURES_INFO+" where "+KEY_AID+"=";
//		
//		try {
//			Cursor sCursor=db.rawQuery(selectSurveyQuery,null);
//		
//			if(sCursor!=null&&sCursor.getCount()>0){
//				sCursor.moveToFirst();
//				buildingName=sCursor.getString(1);
//				String description=sCursor.getString(2)+", "+sCursor.getString(3)+", "+sCursor.getString(4)+", "+sCursor.getString(5)+", "+sCursor.getString(6);
//				
//		    htmlFormate.append("<tr height=\"200\" style=\"height: 200px;\"><td colspan=\"1\" border=\"0\">&nbsp;</td><td colspan=\"1\" border=\"0\" width=\"20\" style=\"width: 20px\">&nbsp;</td><td colspan=\"1\" border=\"0\" width=\"700\" style=\"width: 700px;\">"+
//					 "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width: 100%; border-collapse: separate; border: 0; margin: 0px; padding: 0px 0px 30px 0px;\"><tbody><tr height=\"180\" style=\"height: 180px; vertical-align: top;\">"+
//					  "<td colspan=\"1\" border=\"0\" width=\"550\" style=\"width: 550px; padding: 35px 0px 0px 10px;\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width: 100%; border-collapse: separate; border: 0; margin: 0px; padding: 0px 0px 0px 0px;\"><tbody><tr><td> <span style=\"text-decoration: none; color: #2f2f36; font-weight: bold; font-size: 38px; line-height: 44px;\">");
//			htmlFormate.append(buildingName);
//			htmlFormate.append("</span></td></tr><tr><br><td style=\"padding: 0px 0px 0px 3px;\"><span style=\"text-decoration: none; color: #a0a0a5; font-weight: normal; font-size: 16px; line-height: 24px;\">");
//		    htmlFormate.append(description);
//			htmlFormate.append("</td></tr></tbody></table></td></tr>");
//			
//		
//
//			 Cursor fCursor=db.rawQuery(selectQuery,null);
//			 if(fCursor!=null&&fCursor.getCount()>0){
//				fCursor.moveToFirst();
//				for(int i=0;i<fCursor.getCount();i++){
//					htmlFormate.append("<tr style=\"vertical-align: top;\"><td colspan=\"2\" border=\"0\" style=\"padding: 5px 0px 0px 0px;\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width: 100%; height: 100%; border-collapse: separate; border: 0; margin: 0px; padding: 5px; background-color: #fafafa;\"><tbody><tr height=\"32\" style=\"height: 42px; color: #fff; background-color: #00bfff;\"><br><td colspan=\"1\" width=\"80\" style=\"font-size: 18px; padding: 0px 0px 0px 10px;\">FLOOR:</td><td colspan=\"1\" style=\"font-size: 18px; padding: 0px 0px 0px 10px;\"><span>&nbsp;&nbsp;");
//					floorName=fCursor.getString(1);
//					htmlFormate.append(floorName);
//					htmlFormate.append("</td></tr>");
//					Log.d("Floor  Name",fCursor.getString(1));
//					String areaQuery=selectAreaQuery+fCursor.getString(0);
//					Cursor aCursor=db.rawQuery(areaQuery,null);
//					if(aCursor!=null&&aCursor.getCount()>0){
//						aCursor.moveToFirst();	
//						for(int j=0;j<aCursor.getCount();j++){		
//					      htmlFormate.append(" <tr><td colspan=\"2\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"color: #aaadb4; width: 100%; height: 100%; border-collapse: separate; border: 0; margin: 0px; padding: 0px; background-color: #fafafa; padding: 0px 0px 10px 0px;\"><tbody><tr height=\"32\" style=\"height: 32px;\"><br><td colspan=\"1\" width=\"80\" style=\"padding: 0px 0px 0px 10px;\">AREA:</td><td colspan=\"1\" style=\"color: deepskyblue; padding: 0px 0px 0px 10px;\"><span>&nbsp;&nbsp;");
//						  htmlFormate.append(aCursor.getString(1));
//						  htmlFormate.append("</span></td></tr>");
//							Log.d("@@@@@@@@@","******Area Record***********");
//							Log.d("Area  Name",aCursor.getString(1));
//							
//							String fixQuery=selectFixtureQuery+aCursor.getString(0);
//							Log.d("Fix Query:",fixQuery);
//							Cursor fixCursor=db.rawQuery(fixQuery,null);
//							
//							if(fixCursor!=null&&fixCursor.getCount()>0){
//															
//								fixCursor.moveToFirst();
//								Log.d("@@@@@@@@@","******Fixtures Record***********");
//							    htmlFormate.append("<br><tr><td colspan=\"2\" style=\"border-top: 1px solid #aaadb4;\"><table align=\"left\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"font-weight: normal; font-size: 10px; line-height: 16px; width: 100%; height: 100%; border-collapse: separate; border: 0; margin: 0px; padding: 0px; background-color: #f1f1f1;\"><thead align=\"left\" style=\"background-color: #e5e5e5; color: #5e5e5e;\"><tr><th style=\"padding: 0px 0px 0px 5px;\">&nbsp;COUNT</th><th>&nbsp;&nbsp;&nbsp;type</th><th>&nbsp;&nbsp;&nbsp;&nbsp;CODE</th><th>&nbsp;&nbsp;&nbsp;style</th><th>&nbsp;&nbsp;&nbsp;&nbsp;MOUNT</th><th>&nbsp;&nbsp;&nbsp;&nbsp;CONTROL</th><th>&nbsp;&nbsp;&nbsp;&nbsp;HEIGHT</th><th>"+
//                                                    "&nbsp;&nbsp;&nbsp;&nbsp;HOURS</th><th>&nbsp;&nbsp;&nbsp;&nbsp;OPTION</th><th>&nbsp;&nbsp;&nbsp;&nbsp;NOTE</th><th>&nbsp;</th></tr></thead> <tbody style=\"color: #5e5e5e;\">");
//								for(int k=0;k<fixCursor.getCount();k++){
//									
//									String hours=fixCursor.getString(12);//+" ("+fixCursor.getString(10)+"-"+fixCursor.getString(11)+")";
//									
//									String row="<br><tr><td style=\"padding: 0px 0px 0px 5px;\">&nbsp;"+fixCursor.getString(3)+"</td><td>&nbsp;&nbsp;"+fixCursor.getString(2)+"</td><td>&nbsp;&nbsp;&nbsp;"+fixCursor.getString(4)+"</td><td>&nbsp;&nbsp;&nbsp;"+fixCursor.getString(5)
//											+"</td><td>&nbsp;&nbsp;&nbsp;"+fixCursor.getString(6)+"</td><td>&nbsp;&nbsp;&nbsp;"+fixCursor.getString(7)+"</td><td>&nbsp;&nbsp;&nbsp;"+fixCursor.getString(9)+"</td><td>&nbsp;&nbsp;&nbsp;"+fixCursor.getString(12)+" ("+fixCursor.getString(10)+"-"+fixCursor.getString(11)+")"
//											+"</td><td>&nbsp;&nbsp;&nbsp;"+fixCursor.getString(8)+"</td><td>&nbsp;&nbsp;&nbsp;"+fixCursor.getString(14)+"</td><td>&nbsp;</td></tr>";
//									htmlFormate.append(row);	
//									
//								  //  csvFormate.append("\"" +buildingName.toUpperCase()+"\",\"" +description.toUpperCase() + "\",\"" +floorName + "\",\"" + aCursor.getString(1).toUpperCase() + "\",\"" + fixCursor.getString(2).toUpperCase() + "\",\"" + fixCursor.getString(4).toUpperCase() + "\",\"" + fixCursor.getString(5).toUpperCase()  + "\",\"" + fixCursor.getString(6).toUpperCase() + "\",\"" + fixCursor.getString(7).toUpperCase() + "\",\"" + fixCursor.getString(9)+ "\",\"" +hours+ "\",\"" + fixCursor.getString(8).toUpperCase() + "\",\"" + fixCursor.getString(14).toUpperCase() + "\",\"" + fixCursor.getString(3).toUpperCase() + "\",\"" + fixCursor.getString(10).toUpperCase() + "\",\"" + fixCursor.getString(11).toUpperCase() + "\",\"" + fixCursor.getString(12).toUpperCase() + "\" \n");
//									  csvFormate.append("\"" +buildingName.toUpperCase()+"\",\"" +description.toUpperCase() + "\",\"" +floorName + "\",\"" + aCursor.getString(1).toUpperCase() + "\",\"" + fixCursor.getString(2).toUpperCase() + "\",\"" + fixCursor.getString(4).toUpperCase() + "\",\"" + fixCursor.getString(5).toUpperCase()  + "\",\"" + fixCursor.getString(6).toUpperCase() + "\",\"" + fixCursor.getString(7).toUpperCase() + "\",\"" + fixCursor.getString(9)+ "\",\"" +hours+ "\",\"" + fixCursor.getString(8).toUpperCase() + "\",\"" + fixCursor.getString(14).toUpperCase() + "\",\"" + fixCursor.getString(3).toUpperCase() + "\",\"" + fixCursor.getString(10).toUpperCase() + "\",\"" + fixCursor.getString(11).toUpperCase() + "\" \n");
//									
//									String imagePath=fixCursor.getString(13);
//									
//									if(imagePath!=null&&!imagePath.equals("")){
//								      Constant.FIXTURE_IMAGE_LIST.add(imagePath);		
//									}
//									
//									fixCursor.moveToNext();
//								}
//								htmlFormate.append("</tbody></table></td></tr>");
//							}else{
//							  csvFormate.append("\"" +buildingName.toUpperCase() +"\",\"" +description.toUpperCase() + "\",\"" +floorName + "\",\"" + aCursor.getString(1).toUpperCase()+"\"\n");
//							}
//							htmlFormate.append("</tbody></table></td></tr>");
//							aCursor.moveToNext();
//						}
//					}else{
//						 csvFormate.append("\"" +buildingName.toUpperCase() +"\",\"" +description.toUpperCase() + "\",\"" +floorName+"\"\n");
//					}
//					htmlFormate.append("</tbody></table></td></tr>");
//					fCursor.moveToNext();
//				}
//			}else{
//				 csvFormate.append("\"" +buildingName.toUpperCase() +"\",\"" +description.toUpperCase()+ "\"\n");
//			}
//			 htmlFormate.append("</tbody></table></td><td colspan=\"1\" border=\"0\" width=\"20\" style=\"width: 20px\">&nbsp;</td><td colspan=\"1\" border=\"0\">&nbsp;</td></tr>");
//		  }
//			
//		   String dirNamePath=Constant.SD_CARD_PATH+"/"+Constant.SURVEY_NAME_PATH+"_"+Constant.SURVEY_ID;
//			File dirFile = new File(dirNamePath);
//			if (!dirFile.exists()) {
//				dirFile.mkdir();
//			}
//			
//			String path=dirFile.getPath()+"/"+Constant.SURVEY_NAME_PATH+"_"+Constant.SURVEY_ID+".csv";
//				
//			Log.d("CSV Path:::::::::::",path);
//			
//			File file = new File(path);
//			try {
//				FileOutputStream out = new FileOutputStream(file);
//				out.write(csvFormate.toString().getBytes());
//				out.close();
//				
//			} catch (IOException e) {
//				Log.e("BROKEN", "Could not write file " + e.getMessage());
//			}
//		  Constant.CSV_URI=Uri.fromFile(file);
//		 
//		  
//		  htmlFormate.append("</tbody></table></body></html>");
//		  
//		  String pathHTML=dirFile.getPath()+"/"+Constant.SURVEY_NAME_PATH+"_"+Constant.SURVEY_ID+ ".html";
//			
//		  File htmlFile = new File(pathHTML);
//			try {
//				FileOutputStream out = new FileOutputStream(htmlFile);
//				out.write(htmlFormate.toString().getBytes());
//				out.close();
//			} catch (IOException e) {
//				Log.e("BROKEN", "Could not write file " + e.getMessage());
//			}
//		   Constant.HTML_URI=Uri.fromFile(htmlFile);
//		   
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		Log.d("HTML FORMATE",htmlFormate.toString());
//		return htmlFormate;
//	}
//	
//	public int getNumberOfFixtures(String areaId){
//		SQLiteDatabase db=this.getReadableDatabase();
//		String selectQuery="Select fix_id from "+TABLE_FIXTURES_INFO+" where "+KEY_AID+"="+areaId;
//		Cursor fcursor=db.rawQuery(selectQuery,null);
//		if(fcursor!=null&&fcursor.getCount()>0)
//			  return fcursor.getCount();
//		else
//			  return 0;	
//	}
}
