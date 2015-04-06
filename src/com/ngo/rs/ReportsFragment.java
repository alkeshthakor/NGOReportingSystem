package com.ngo.rs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ReportsFragment extends Fragment {
	private FragmentTabHost mTabHost;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	Bundle savedInstanceState) {
		// Get the view from fragmenttab2.xml
		View view = inflater.inflate(R.layout.layout_generated_report_fragment, container, false);
		
		mTabHost = (FragmentTabHost)view.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("fragment_local_report").setIndicator("Local"),
                LocalReportFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("fragment_server_report").setIndicator("Server"),
                ServerReportFragment.class, null);
                
		return view;
	}

}
