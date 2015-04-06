package com.ngo.rs.util;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ngo.rs.R;

public class LocalReportsAdapter extends BaseAdapter {

	 private Context mContext;
	 private List<DataItem> reportList;
	 LayoutInflater mLayoutInflater;
	 
	 public LocalReportsAdapter(Context mContext,List<DataItem> productList){
		 this.mContext=mContext;
		 this.reportList=productList;
		 mLayoutInflater=LayoutInflater.from(mContext);
	 }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return reportList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub		return reportList.get(position);
		return reportList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder mViewHolder;
		
		if(convertView == null){
			convertView=(View)mLayoutInflater.inflate(com.ngo.rs.R.layout.layout_local_report_list,parent,false);     	
			mViewHolder=new ViewHolder();
			mViewHolder.reportTitleTV=(TextView)convertView.findViewById(R.id.reportTitleTextView);		
			
			convertView.setTag(mViewHolder);
			convertView.setId(position);
			
			
		}else{
			mViewHolder=(ViewHolder)convertView.getTag();
		}
		
		DataItem mDataItem=(DataItem)getItem(position);
		
		
		mViewHolder.reportTitleTV.setText(mDataItem.getmLocalReportName()+"");
		
		return convertView;
	}

	public class ViewHolder{
		TextView reportTitleTV;
	}
}
