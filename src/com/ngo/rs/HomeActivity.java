package com.ngo.rs;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.ngo.rs.util.ViewPagerAdapter;

public class HomeActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Get the view from activity_main.xml
		setContentView(R.layout.activity_home);
			 
		ActionBar actionbar=getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);

		// Locate the viewpager in activity_main.xml
		ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
		 
		// Set the ViewPagerAdapter into ViewPager
		viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_add_projetc:
	        	Intent newProjectIntent=new Intent(getApplicationContext(),NewProjectActivity.class);
	        	newProjectIntent.putExtra("mode",false);
	        	startActivity(newProjectIntent);
	            return true;
	        case android.R.id.home:
	        	  finish();
	            return true;

	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
