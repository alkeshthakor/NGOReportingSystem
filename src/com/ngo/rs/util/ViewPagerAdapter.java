package com.ngo.rs.util;

import com.ngo.rs.ReportsFragment;
import com.ngo.rs.ProjectFragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

	final int PAGE_COUNT = 2;
	// Tab Titles
	private String tabtitles[] = new String[] { "Projects", "Reports"};
	Context context;
	
	public ViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}
	
	@Override
	public Fragment getItem(int position) {
		switch (position) {
		 
		// Open FragmentTab1.java
		case 0:
		ProjectFragment reportFragment = new ProjectFragment();
		return reportFragment;

		// Open FragmentTab2.java
		case 1:
		ReportsFragment generatedReportFragment = new ReportsFragment();
		return generatedReportFragment;

		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return PAGE_COUNT;	
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return tabtitles[position];
	}
	
}
