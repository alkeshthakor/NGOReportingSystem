package com.ngo.rs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ngo.rs.pdfwriter.PDFWriter;
import com.ngo.rs.pdfwriter.StandardFonts;
import com.ngo.rs.util.ConnectionDetector;
import com.ngo.rs.util.Constant;
import com.ngo.rs.util.DataItem;
import com.ngo.rs.util.DatabaseHandler;
import com.ngo.rs.util.ImageUtil;
import com.ngo.rs.util.ServerConnector;

public class ProjectDetailActivity extends Activity implements OnClickListener{

	private TextView mTitleTextView;
	private TextView mLocationTextView;
	private TextView mChairmanTextView;
	private TextView mClubNameTextView;
	private TextView mDateTextView;
	private TextView mDetaiTextView;
	private TextView mStatusTextView;
	private TextView mLastModifyTextView;
	
	private LinearLayout mPhotoLinearLayout;
	
	private Button generateReportButton;
	private Button sendToServerButton;
	
	
    DatabaseHandler dbHandler;
	DataItem mDataItem;
	
	private ProgressDialog mProgressDialog;
    ConnectionDetector cd;
    ServerConnector connector;
    Context mContext;
    
	private String reportId;
	private String pdfFileName;
	
	private SharedPreferences prefSettings;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_project_detail);
		
		ActionBar actionbar=getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		
		mContext=this;
		dbHandler=new DatabaseHandler(mContext);
		
		prefSettings= PreferenceManager.getDefaultSharedPreferences(mContext);

		
		cd=new ConnectionDetector(mContext);
		connector=new ServerConnector();
		mProgressDialog=new ProgressDialog(ProjectDetailActivity.this);
		mProgressDialog.setMessage("Please wait...");
		mProgressDialog.setIndeterminate(false);
		
		
		mTitleTextView=(TextView)findViewById(R.id.titleTextViewDetail);
		mLocationTextView=(TextView)findViewById(R.id.locationTextViewDetail);
		mChairmanTextView=(TextView)findViewById(R.id.chairmanTextViewDetail);
		mClubNameTextView=(TextView)findViewById(R.id.clubnameTextViewDetail);
		mDateTextView=(TextView)findViewById(R.id.dateTextViewDetail);
		mDetaiTextView=(TextView)findViewById(R.id.descriptionTextViewDetail);
		mStatusTextView=(TextView)findViewById(R.id.statusTextViewDetail);
		mLastModifyTextView=(TextView)findViewById(R.id.lastmodifyTextViewDetail);
		mPhotoLinearLayout=(LinearLayout)findViewById(R.id.photolistLLDetail);
		
		generateReportButton=(Button)findViewById(R.id.generateReportButton);
		sendToServerButton=(Button)findViewById(R.id.sendToServerButton);
		
		generateReportButton.setOnClickListener(this);
		sendToServerButton.setOnClickListener(this);
		
		
		if(getIntent()!=null){
			reportId=getIntent().getStringExtra("reportid");
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(reportId!=null){
			mDataItem=dbHandler.getDetailReport(reportId);
			initializeGUI(mDataItem);
		}
	}
	
	
	public void initializeGUI(DataItem mDataItem){
		mTitleTextView.setText(mDataItem.getmTitle());
		mLocationTextView.setText(mDataItem.getmLocation());
		mChairmanTextView.setText(mDataItem.getmChairman());
		mClubNameTextView.setText(mDataItem.getmClubName());
		mDateTextView.setText(mDataItem.getmDate());
		mDetaiTextView.setText(mDataItem.getmDescription());
		
		if(mDataItem.getmStatus().equalsIgnoreCase("1")){
			mStatusTextView.setText("Active");
		}else{
			mStatusTextView.setText("Closed");
		}
		
		//mStatusTextView.setText(mDataItem.getmStatus());
		
		mLastModifyTextView.setText(mDataItem.getmModifyDate());
		
		if(!mDataItem.getmImagePath().equalsIgnoreCase("")&&mDataItem.getmImagePath().length()>0){
			String []paths=mDataItem.getmImagePath().split(",");
			
			
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
			mPhotoLinearLayout.removeAllViews();
			
			for(int i=0;i<paths.length;i++){
			
				File imageFile=new File(paths[i]);
				if(imageFile.exists()){
					View view = inflater.inflate( R.layout.layout_attachment,null);
					final ImageView attachmentDeleteIV = (ImageView) view.findViewById(R.id.attachmentDeleteIV);
					attachmentDeleteIV.setVisibility(View.GONE);
					//attachmentDeleteIV.setTag(i);
					
					final ImageView attachmentIV = (ImageView) view.findViewById(R.id.attachmentImage);
					
					Bitmap thumbnail = ImageUtil.decodeFile(new File(paths[i]), 60,60);
					attachmentIV.setImageBitmap(thumbnail); 
				    mPhotoLinearLayout.addView(view);	
				}
			}	
		}
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_edit_projetc:
	        	Intent editProjectIntent=new Intent(getApplicationContext(),NewProjectActivity.class);
	        	editProjectIntent.putExtra("reportid",reportId);
	        	editProjectIntent.putExtra("mode",true);
	        	startActivity(editProjectIntent);
	            return true;
	        case android.R.id.home:
	        	  finish();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.project_detail, menu);
		return true;
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()){
		case R.id.generateReportButton:
			 DataItem mDataItem=dbHandler.getDetailReport(reportId);
			 if(mDataItem!=null){
				new PdfGeneratedTask().execute(mDataItem);
			 }
			break;
		case R.id.sendToServerButton:
			new SendToServerTask().execute();
			break;
			
		}
		
	}

 
	private class PdfGeneratedTask extends AsyncTask<DataItem,Void,Boolean>{

    	@Override
    	protected void onPreExecute() {
    		// TODO Auto-generated method stub
    		super.onPreExecute();
    		if(!mProgressDialog.isShowing())
    		    mProgressDialog.show();
    	}
		@Override
		protected Boolean doInBackground(DataItem... mDataItem) {
			// TODO Auto-generated method stub
			 String pdfcontent = generateHelloWorldPDF(mDataItem[0]);
			 pdfFileName="Project_"+mDataItem[0].getmTitle().replace(" ","_")+"_"+System.currentTimeMillis();
			 String pdfFilePath=ImageUtil.createMyImagesDir().getAbsolutePath()+"/"+pdfFileName+".pdf";
			 return outputToFile(pdfFilePath,pdfcontent,"ISO-8859-1");	 
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(mProgressDialog.isShowing())
			   mProgressDialog.dismiss();
			
			if(result){
			  Toast.makeText(mContext,getResources().getString(R.string.lbl_pdf_success),Toast.LENGTH_SHORT).show();
			  ContentValues values=new ContentValues();
			  values.put(DatabaseHandler.KEY_REPORT_NAME, pdfFileName);
			  values.put(DatabaseHandler.KEY_REPORT_CREATED,"true");
			  dbHandler.updateProject(values,mDataItem.getmReportId());
			  
			}else{
			Toast.makeText(mContext,getResources().getString(R.string.lbl_pdf_fail),Toast.LENGTH_SHORT).show();
			}
		}		
    }
	
	 private String generateHelloWorldPDF(DataItem mDataItem) {
	 		PDFWriter mPDFWriter = new PDFWriter();
	
	 		Bitmap logoImage = ImageUtil.decodeSampledBitmapFromResource(getResources(), R.drawable.ic_launcher,60,60);
		   
	 		mPDFWriter.addImage(10,752, logoImage);

				
	         mPDFWriter.setFont(StandardFonts.SUBTYPE, StandardFonts.TIMES_BOLD);
	          mPDFWriter.addText(120,770,30,"NGO Report System");
	          
	          mPDFWriter.addLine(0,748,595,748);
	          
	          mPDFWriter.setFont(StandardFonts.SUBTYPE, StandardFonts.TIMES_ROMAN);

	          mPDFWriter.addText(500,720,12, "Date: "+mDataItem.getmDate());
	          
	          mPDFWriter.addText(20,680,12, "Peoject Title: "+mDataItem.getmTitle());
	          
	          mPDFWriter.addText(20,640,12, "Peoject Chair man: "+mDataItem.getmChairman());
	          
	          mPDFWriter.addText(20,620,12, "Location: "+mDataItem.getmLocation());
	          
	          mPDFWriter.addText(20,590,12, "Detail: ");
	          
	          String detailString=mDataItem.getmDescription();
	          	          
	          List<String> detailContentLine=splitEqually(detailString,110);
	          int position=590;
	          
	          for(int i=0;i<detailContentLine.size();i++){
	        	  position=position-20;
	        	  mPDFWriter.addText(20,position,12,detailContentLine.get(i).toString());  
	          }
	          
	        
	          String presidentName=prefSettings.getString(Constant.PREF_PRESIDENT,"");
	          String rccName=prefSettings.getString(Constant.PREF_RCC,"");
	          String secretoryName=prefSettings.getString(Constant.PREF_SECRETORY,"");
	          
	          mPDFWriter.addText(20,70,12, "President:");
	          mPDFWriter.addText(80,70,12,presidentName);
	          
	          mPDFWriter.addText(20,40,12, "Signature:_______________");
	         
	          
	          mPDFWriter.addText(200,70,12, "RCC:");
	          mPDFWriter.addText(240,70,12,rccName);
	          
	          mPDFWriter.addText(200,40,12, "Signature:_______________");

	          mPDFWriter.addText(400,70,12, "Secretory:");
	          mPDFWriter.addText(460,70,12,secretoryName);
	          
	          mPDFWriter.addText(400,40,12, "Signature:______________");
	          
	          
	          
	          if(!mDataItem.getmImagePath().equals("")&&mDataItem.getmImagePath().length()>0){
	        	
	        	  
	           String []attachmentList=mDataItem.getmImagePath().split(",");  
	          
	          if(attachmentList.length>0){
	        	  
	          
		          List<String[]> pageImageSet=new ArrayList<String[]>();
		          String[] tempPathList=new String[6];
		          int count=0;
		          if(attachmentList.length>6){
		          for(int i=0;i<attachmentList.length;i++){
		        	  if(count==6){
		        		  pageImageSet.add(tempPathList);
		        		  tempPathList=new String[6];
		        		  count=0;
		        	  }
		        	  tempPathList[count]=attachmentList[i];
		        	  count++;
		        	  if(i==(attachmentList.length-1)){
		        		  pageImageSet.add(tempPathList);
		        	  }
		          }
		          }else{
		        	  pageImageSet.add(attachmentList);
		          }
		          if(attachmentList.length>0){
			          float numberOfPages=attachmentList.length/6;
			          if(attachmentList.length%6!=0){
			        	  numberOfPages++;
			          }
			          
			          int numberOfRow=attachmentList.length/2;
			          if(numberOfRow%2!=0){
			        	  numberOfRow++;
			          }
			          
			          for(int j=0;j<numberOfPages;j++){
			        	  mPDFWriter.newPage();
			        	  String []pageImages=pageImageSet.get(j);
			        	   if(pageImages.length>0){   
			        		  if(new File(pageImages[0]).exists()){
				        		  Bitmap photoBitmap=ImageUtil.decodeFile(new File(pageImages[0]),100,100);
				                  mPDFWriter.addImage(100,572, photoBitmap);
			        		  }
			        	   }
			        	   if(pageImages.length>1){
				        		  if(new File(pageImages[0]).exists()){
				        			  Bitmap photoBitmap=ImageUtil.decodeFile(new File(pageImages[1]),100,100);
				        			  mPDFWriter.addImage(330,572, photoBitmap);
				        		  }
			        	   }
			        	   if(pageImages.length>2){
				        		  if(new File(pageImages[0]).exists()){
			        		   Bitmap photoBitmap=ImageUtil.decodeFile(new File(pageImages[2]),100,100);
				               mPDFWriter.addImage(100,302, photoBitmap);
				        		  }
			        	   }
			        	   if(pageImages.length>3){
				        		  if(new File(pageImages[0]).exists()){
			        		   Bitmap photoBitmap=ImageUtil.decodeFile(new File(pageImages[3]),100,100);
				               mPDFWriter.addImage(330,302, photoBitmap);
				        		  }
			        	   }
			        	   
			        	   if(pageImages.length>4){
				        		  if(new File(pageImages[0]).exists()){
			        		   Bitmap photoBitmap=ImageUtil.decodeFile(new File(pageImages[4]),100,100);
				               mPDFWriter.addImage(100,32, photoBitmap);
				        		  }
			        	   }
			        	   if(pageImages.length>5){
				        		  if(new File(pageImages[0]).exists()){

			        		   Bitmap photoBitmap=ImageUtil.decodeFile(new File(pageImages[5]),100,100);
				               mPDFWriter.addImage(330,32, photoBitmap);
				        		  }
			        	   }
			          } 
		           }
	            }
	          }
	          
	         int pageCount = mPDFWriter.getPageCount();
	         for (int i = 0; i < pageCount; i++) {
	         	mPDFWriter.setCurrentPage(i);
	         	mPDFWriter.addText(10, 10, 8, Integer.toString(i + 1) + " / " + Integer.toString(pageCount));
	         }
	         
	         String s = mPDFWriter.asString();
	         
	         return s;
	 	}
	 	  
	  private boolean outputToFile(String fileName, String pdfContent, String encoding) {
	         File newFile = new File(fileName);
	         try {
	             newFile.createNewFile();
	             try {
	             	 FileOutputStream pdfFile = new FileOutputStream(newFile);
	             	 pdfFile.write(pdfContent.getBytes(encoding));
	                 pdfFile.close();
	                 return true;
	             } catch(FileNotFoundException e) {
	             	e.printStackTrace();
	             	return false;
	             }
	         } catch(IOException e) {
	        	 e.printStackTrace();
	        	 return false;
	         }
	 	}
	 	
	 	public static List<String> splitEqually(String text, int size) {
		    // Give the list the right capacity to start with. You could use an array
		    // instead if you wanted.
		    List<String> ret = new ArrayList<String>((text.length() + size - 1) / size);

		    for (int start = 0; start < text.length(); start += size) {
		        ret.add(text.substring(start, Math.min(text.length(), start + size)));
		    }
		    return ret;
		}
	 	
	 	
	 	class SendToServerTask extends AsyncTask<Void,Void,Double>{
	 		
	 		@Override
	 		protected void onPreExecute() {
	 			// TODO Auto-generated method stub
	 			super.onPreExecute();
	 			 if(!mProgressDialog.isShowing())
	 	 		    mProgressDialog.show();
	 		}
			@Override
			protected Double doInBackground(Void... params) {
				// TODO Auto-generated method stub
				ContentValues projectValue=new ContentValues();
		 		  projectValue.put(DatabaseHandler.KEY_STATUS,"0");
		 		  double rowId;	  
		 		  rowId=dbHandler.updateProject(projectValue,reportId);
		 		  try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		 		  
		 		  return rowId;
			}
	 		@Override
	 		protected void onPostExecute(Double result) {
	 			// TODO Auto-generated method stub
	 			super.onPostExecute(result);
	 			 if(result!=-1){
		 			  Toast.makeText(getApplicationContext(),"Report successfully send to server", Toast.LENGTH_SHORT).show();
		 			  mProgressDialog.dismiss();
		 			  finish();
		 		  }else{
		 			  mProgressDialog.dismiss();
		 			  Toast.makeText(getApplicationContext(),"Send to server fail !", Toast.LENGTH_SHORT).show();
		 	       }  
	 			 
	 		}
	 	}
	 	
}
