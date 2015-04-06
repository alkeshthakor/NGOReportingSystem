package com.ngo.rs;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.ngo.rs.util.DataItem;
import com.ngo.rs.util.DatabaseHandler;
import com.ngo.rs.util.ImageUtil;
import com.ngo.rs.util.LocalReportsAdapter;

public class LocalReportFragment extends Fragment {
	DatabaseHandler dbHandler;
	Context mContext;
	List<DataItem> reportList;
	ListView reportListView;
	LocalReportsAdapter mAdapter;
    Activity mParentActivity;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext=activity.getApplicationContext();
		mParentActivity=activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.layout_fragment_local_report, container, false);
		reportListView=(ListView)view.findViewById(R.id.reportListView);
		dbHandler=new DatabaseHandler(mContext);
		
		reportListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				DataItem mDataItem=reportList.get(position);
				
				viewOrSendEmail(mDataItem);
				
				//openPdfIntent(path);
			    
			}
		});
		return view;
	}
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		reportList=dbHandler.getLocalReportList();
		if(reportList.size()>0){
			mAdapter=new LocalReportsAdapter(getActivity(), reportList);
			reportListView.setAdapter(mAdapter);
		}
	}
	
	
	private void viewOrSendEmail(final DataItem mDataItem) {

		   String path=ImageUtil.createMyImagesDir().getAbsolutePath()+"/"+mDataItem.getmLocalReportName()+".pdf";

		  final File file = new File(path); 

	     
		final CharSequence[] options = { "View Pdf", "Send Email",
				"Cancel" };

		AlertDialog.Builder builder = new AlertDialog.Builder(mParentActivity);
		builder.setTitle("Select Action");
		builder.setItems(options, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (options[item].equals("View Pdf")) {
					 if (file.exists()) {
						 Uri path1 = Uri.fromFile(file); 
			              Intent intent = new Intent(Intent.ACTION_VIEW); 
			              intent.setDataAndType(path1, "application/pdf"); 
			              intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); 

			              try { 
			                  startActivity(intent); 
			              }  
			              catch (ActivityNotFoundException e) { 
			                  Toast.makeText(getActivity(),  
			                      "No Application Available to View PDF",  
			                      Toast.LENGTH_SHORT).show(); 
			                  downloadReaderApp();
			              } 				    	  
				      }					
				} else if (options[item].equals("Send Email")) {
					
					 if (file.exists()) {
						 Uri path1 = Uri.fromFile(file); 

							final Intent emailIntent = new Intent(
									android.content.Intent.ACTION_SEND);
							emailIntent.setType("application/pdf");
							emailIntent.putExtra(Intent.EXTRA_EMAIL,
									new String[] { "abc@gmail.com" });
							emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,mDataItem.getmTitle());
							emailIntent.putExtra(android.content.Intent.EXTRA_TEXT,
									mDataItem.getmDescription());
							
							emailIntent.putExtra(Intent.EXTRA_STREAM, path1);
													    
						    startActivity(Intent.createChooser(emailIntent, "Send email using:"));
						    
				      }
					
				} else if (options[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}

	
	
	public void downloadReaderApp(){
		 AlertDialog.Builder builder1 = new AlertDialog.Builder(mParentActivity);
        builder1.setTitle("No Application Found");
        builder1.setMessage("Download from Android Market?");
        builder1.setPositiveButton("Yes, Please", new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
        // TODO Auto-generated method stub

        Intent marketIntent = new Intent(Intent.ACTION_VIEW);
        marketIntent.setData(Uri.parse("market://details?id=com.adobe.reader"));
        startActivity(marketIntent);

        }
        });
        builder1.setNegativeButton("No, Thanks", null);
        builder1.create().show();
	}
}

