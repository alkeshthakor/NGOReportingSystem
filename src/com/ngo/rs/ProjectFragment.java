package com.ngo.rs;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ngo.rs.util.DataItem;
import com.ngo.rs.util.DatabaseHandler;
import com.ngo.rs.util.ReportsAdapter;

public class ProjectFragment extends Fragment {
	DatabaseHandler dbHandler;
	Context mContext;
	List<DataItem> reportList;
	ListView reportListView;
	ReportsAdapter mAdapter;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mContext=activity.getApplicationContext();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	Bundle savedInstanceState) {
		// Get the view from fragmenttab1.xml
		View view = inflater.inflate(R.layout.layout_report_fragment, container, false);
		reportListView=(ListView)view.findViewById(R.id.reportListView);
		
		reportListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				Intent detailIntent=new Intent(mContext,ProjectDetailActivity.class);
				detailIntent.putExtra("reportid",reportList.get(position).getmReportId());
				startActivity(detailIntent);
			}
		});
		dbHandler=new DatabaseHandler(mContext);
		
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		reportList=dbHandler.getReportList();
		if(reportList.size()>0){
			mAdapter=new ReportsAdapter(getActivity(), reportList);
			reportListView.setAdapter(mAdapter);
		}
	}
}
