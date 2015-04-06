package com.ngo.rs;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ngo.rs.util.DataItem;
import com.ngo.rs.util.DatabaseHandler;
import com.ngo.rs.util.DatePickerFragment;
import com.ngo.rs.util.ImageUtil;

public class NewProjectActivity extends Activity implements OnClickListener {

	private EditText mTitleEditText;
	private EditText mLocationEditText;
	private EditText mChiarmanEditText;
	private EditText mClubNameEditText;
	private EditText mDateEditText;
	private EditText mDiscriptionEditText;
	
	private Button saveButton;
	private Button attachPhotosButton;
	private LinearLayout photolistLinearLayout;
	private Uri mPhotoUri;
	
	private int year;
	private int month;
	private int day;
	
	private int hours;
	private int minute;
	private int second;
	
	static final int DATE_PICKER_ID = 1001;
	private static final int PICK_FROM_CAMERA = 1001;
	private static final int PICK_FROM_FILE = 1002;
	
	Calendar calender;

	FragmentManager fragmentManger;
	
	DatabaseHandler dbHandler;
	
	String finaleImagePath = null;
	Bitmap thumbnail;
	
	List<String> attachmentList;
	List<String> pathList;
	
	Context mContext;
	private boolean isEditMode;
	
	private String reportId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_new_project);
		
		ActionBar actionbar=getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
	
		isEditMode=getIntent().getBooleanExtra("mode",false);
		if(isEditMode)
		 actionbar.setTitle(getResources().getString(R.string.title_activity_project_edit));
		
		
		attachmentList=new ArrayList<String>();
		fragmentManger=getFragmentManager();
		dbHandler=new DatabaseHandler(getApplicationContext());
		calender = Calendar.getInstance();
		pathList=new ArrayList<String>();
		mContext=this;
		
		mTitleEditText=(EditText)findViewById(R.id.titleEditText);
		mLocationEditText=(EditText)findViewById(R.id.locationEditText);
		mChiarmanEditText=(EditText)findViewById(R.id.chiarmanEditText);
		mClubNameEditText=(EditText)findViewById(R.id.clubnameEditText);
		mDateEditText=(EditText)findViewById(R.id.dateEditText);
		mDiscriptionEditText=(EditText)findViewById(R.id.discriptionEditText);
		photolistLinearLayout=(LinearLayout)findViewById(R.id.photolistLinearLayout);
		
		
		saveButton=(Button)findViewById(R.id.saveProjectBT);
		attachPhotosButton=(Button)findViewById(R.id.attacPhotosBT);
		
		saveButton.setOnClickListener(this);
		attachPhotosButton.setOnClickListener(this);
		
		ImageUtil.createMyImagesDir();
		
		
		mDateEditText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				 DatePickerFragment date = new DatePickerFragment();
				  /**
				   * Set Up Current Date Into dialog
				   */
				 //Calendar calender = Calendar.getInstance();
				  Bundle args = new Bundle();
				  args.putInt("year", calender.get(Calendar.YEAR));
				  args.putInt("month", calender.get(Calendar.MONTH));
				  args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
				  date.setArguments(args);
				  /**
				   * Set Call back to capture selected date
				   */
				  date.setCallBack(ondate);
				  date.show(getFragmentManager(), "Date Picker");
			}
		});		
		
		
		if(isEditMode){
			initializeGUI();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case android.R.id.home:
	        	  finish();
	            return true;
	        case R.id.action_settings_admin:
	        	Intent settingIntent=new Intent(getApplicationContext(),AppSettingActivity.class);
	        	startActivity(settingIntent);
	        	return true;
	    }
		return false;
	}
	
	OnDateSetListener ondate = new OnDateSetListener() {
		  @Override
		  public void onDateSet(DatePicker view, int year, int monthOfYear,
		    int dayOfMonth) {
		 
			    monthOfYear = monthOfYear + 1;
				String dateValue = "";
				if (dayOfMonth < 10) {
					dateValue ="0" + dayOfMonth;
				} else {
					dateValue =""+dayOfMonth;
				}
				if (monthOfYear < 10) {
					dateValue = dateValue+"-0" + monthOfYear;
				} else {
					dateValue = dateValue+"-"+ monthOfYear;
				}
				dateValue = dateValue + "-" + year;
				mDateEditText.setText(dateValue);	
		  }
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_project, menu);
		return true;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		//super.onBackPressed();
		finish();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
		case R.id.saveProjectBT:
			saveProject();
			break;
		case R.id.attacPhotosBT:
			selectImage();
			break;
			
	}
  }
	
@Override
protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
}
	
public boolean isValidInput(){
		  if(mTitleEditText.getText().toString().length()>0&&mLocationEditText.getText().toString().length()>0&&mChiarmanEditText.getText().toString().length()>0
			&&mDateEditText.getText().toString().length()>0&&mDiscriptionEditText.getText().toString().length()>0&&mClubNameEditText.getText().toString().length()>0)
			  return true;
		  else
			  return false;
}
	 
  private void saveProject(){
	  if(isValidInput()){
	  ContentValues projectValue=new ContentValues();
	  projectValue.put(DatabaseHandler.KEY_TITLE,mTitleEditText.getText().toString());
	  projectValue.put(DatabaseHandler.KEY_LOCATION,mLocationEditText.getText().toString());
	  projectValue.put(DatabaseHandler.KEY_CHAIRMAN,mChiarmanEditText.getText().toString());
	  projectValue.put(DatabaseHandler.KEY_DATE,mDateEditText.getText().toString());
	  projectValue.put(DatabaseHandler.KEY_DETAIL_DESC,mDiscriptionEditText.getText().toString());
	  projectValue.put(DatabaseHandler.KEY_CLUB_NAME,mClubNameEditText.getText().toString());
	 // projectValue.put(DatabaseHandler.KEY_STATUS,"");
	  projectValue.put(DatabaseHandler.KEY_MODIFY,"");
	  projectValue.put(DatabaseHandler.KEY_MODIFY,getCurrentDateTime());
	  
	  String pathlistString="";
	  if(pathList.size()>0){
		  for(int i=0;i<pathList.size();i++){
			  pathlistString=pathlistString+pathList.get(i)+",";
		  }
	  }
	  
	 projectValue.put(DatabaseHandler.KEY_IMAGE_PATH,pathlistString);
	  
	  double rowId;
	  if(isEditMode){
		  
		  rowId=dbHandler.updateProject(projectValue,reportId);
		  
		  if(rowId!=-1){
			  Toast.makeText(getApplicationContext(),"Record successfully updated", Toast.LENGTH_SHORT).show();
			  finish();
		  }else{
			  Toast.makeText(getApplicationContext(),"Record successfully not updated", Toast.LENGTH_SHORT).show();
		  }
		  
	  }else{
		  rowId=dbHandler.addNewProject(projectValue);
		  if(rowId!=-1){
			  Toast.makeText(getApplicationContext(),"Record successfully saved", Toast.LENGTH_SHORT).show();
			  finish();
		  }else{
			  Toast.makeText(getApplicationContext(),"Record successfully not saved", Toast.LENGTH_SHORT).show();
		  }  
	  }
	  Log.d("Row number","Row "+rowId);
	  }else{
		  Toast.makeText(getApplicationContext(),"Please fill up all details!", Toast.LENGTH_SHORT).show();
	  }
  }
  
  private String getCurrentDateTime(){
	  year=calender.get(Calendar.YEAR);
	  month=calender.get(Calendar.MONTH);
	  day=calender.get(Calendar.DAY_OF_MONTH);
	  
	  hours=calender.get(Calendar.HOUR_OF_DAY);
	  minute=calender.get(Calendar.MINUTE);
	  second=calender.get(Calendar.SECOND);
	  
	    month = month + 1;
		String dateValue = "";
		String timeValue = "";
		
		if (day < 10) {
			dateValue ="0" + day;
		} else {
			dateValue =""+day;
		}
		if (month < 10) {
			dateValue = dateValue+"-0" + month;
		} else {
			dateValue = dateValue+"-"+ month;
		}
		if (hours < 10) {
			timeValue ="0" + hours;
		} else {
			timeValue =""+hours;
		}
		if (minute < 10) {
			timeValue = timeValue+":0" + minute;
		} else {
			timeValue = timeValue+":"+ minute;
		}	
		if (second < 10) {
			timeValue = timeValue+":0" + second;
		} else {
			timeValue = timeValue+":"+ second;
		}
		
		dateValue = dateValue + "-" + year+" "+timeValue;
	    Log.d("Date formate",""+dateValue);

		return dateValue;
  }
  
private void selectImage() {

		final CharSequence[] options = { "Take Photo", "Choose from Gallery",
				"Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(NewProjectActivity.this);
		builder.setTitle("Select Image!");
		builder.setItems(options, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (options[item].equals("Take Photo")) {

					mPhotoUri=Uri.fromFile(ImageUtil.createImageFile());
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					intent.putExtra(MediaStore.EXTRA_OUTPUT,mPhotoUri);
					intent.putExtra("return-data", true);
					startActivityForResult(intent,PICK_FROM_CAMERA);
					
				} else if (options[item].equals("Choose from Gallery")) {
					
//					Intent intent = new Intent();
//					intent.setType("image/*");
//					intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//					intent.setAction(Intent.ACTION_GET_CONTENT);
//					startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_FROM_FILE);
					
					Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(i, PICK_FROM_FILE);
					
				} else if (options[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	@SuppressWarnings("resource")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		
		case PICK_FROM_CAMERA:
			if (resultCode != RESULT_OK)
				return;
			try {
				
				Log.d("Return Image path", mPhotoUri.getPath());
				finaleImagePath = mPhotoUri.getPath();
				pathList.add(finaleImagePath);
				
				createAttachmentList();

				
			} catch (Exception e) {
				Log.e("Error: ", e.getMessage());
				e.printStackTrace();
			}
			break;
			
			
		case PICK_FROM_FILE:
			if (resultCode != RESULT_OK)
				return;
			
			mPhotoUri = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			
			Cursor cursor = getContentResolver().query(mPhotoUri,
					filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			finaleImagePath = cursor.getString(columnIndex);
			cursor.close();
			thumbnail = ImageUtil.decodeFile(new File(finaleImagePath), 60, 80);
			
			File source = new File(finaleImagePath);
			File destination =ImageUtil.createImageFile();
			pathList.add(destination.getAbsolutePath());
			
			try {
				if (source.exists()) {
					FileChannel src = new FileInputStream(source).getChannel();
					FileChannel dst = new FileOutputStream(destination)
							.getChannel();
					dst.transferFrom(src, 0, src.size());
					src.close();
					dst.close();
					Log.d("@@@@@@@@",
							"********** File Successfully copyed ***********");
				}
			} catch (Exception e) {
				Log.e("Error: ", e.getMessage());
			}
			
			createAttachmentList();
			
		    break;
		}	
}
	
	
	public void createAttachmentList(){
		photolistLinearLayout.removeAllViews();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
		for(int i=0;i<pathList.size();i++){
			if(new File(pathList.get(i)).exists()){
			View view = inflater.inflate( R.layout.layout_attachment,null);
			final ImageView attachmentDeleteIV = (ImageView) view.findViewById(R.id.attachmentDeleteIV);
			attachmentDeleteIV.setTag(i);
			
			final ImageView attachmentIV = (ImageView) view.findViewById(R.id.attachmentImage);
			
			thumbnail = ImageUtil.decodeFile(new File(pathList.get(i)), 60,60);
			attachmentIV.setImageBitmap(thumbnail);
			
		    attachmentDeleteIV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View viewArg) {
				// TODO Auto-generated method stub
				int id=(Integer) viewArg.getTag();
				Toast.makeText(mContext,"Item no "+id+" removed",Toast.LENGTH_SHORT).show();
				pathList.remove(id);
				createAttachmentList();
				photolistLinearLayout.invalidate();
			}
		});
		   
			photolistLinearLayout.addView(view);
		}
		}
	}
	
	public void initializeGUI(){
		
		reportId=getIntent().getStringExtra("reportid");

		//String projectId=getIntent().getStringExtra("reportid");
		if(reportId!=null){
			DataItem mDataItem=dbHandler.getDetailReport(reportId);
			if(mDataItem!=null){
				mTitleEditText.setText(mDataItem.getmTitle());
				mLocationEditText.setText(mDataItem.getmLocation());
				mChiarmanEditText.setText(mDataItem.getmChairman());
				mDateEditText.setText(mDataItem.getmDate());
				mClubNameEditText.setText(mDataItem.getmClubName());
				mDiscriptionEditText.setText(mDataItem.getmDescription());
				pathList=new ArrayList<String>();
				if(!mDataItem.getmImagePath().equalsIgnoreCase("")&&mDataItem.getmImagePath().length()>0){
					String []paths=mDataItem.getmImagePath().split(",");
	                for(String path:paths)
	                	pathList.add(path);
	                
	                createAttachmentList();	
				}
			}
		}
	}
}