package com.ngo.rs;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import com.ngo.rs.util.Constant;

public class AppSettingActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		  // Display the fragment as the main content.
	        getFragmentManager().beginTransaction()
	                .replace(android.R.id.content, new SettingsFragment())
	                .commit();
	        
			getActionBar().setDisplayHomeAsUpEnabled(true);
			getActionBar().setHomeButtonEnabled(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case android.R.id.home:
	        	  finish();
	            return true;
	    }
		return false;
	}

	
	public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    // Load the preferences from an XML resource
                    addPreferencesFromResource(R.xml.settings);
                    Context hostActivity = getActivity();
                    
                    final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(hostActivity);
            		
            		final EditTextPreference pref_president = (EditTextPreference) findPreference("pref_president");
            		final EditTextPreference pref_rcc = (EditTextPreference) findPreference("pref_rcc");
            		final EditTextPreference pref_secretory = (EditTextPreference) findPreference("pref_secretory");
                    
            		
            		pref_president.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            			public boolean onPreferenceChange(Preference preference, Object newValue) {
            				Editor editor = settings.edit();        
            				editor.putString(Constant.PREF_PRESIDENT,(String)newValue);
            				editor.commit();
            				return true;
            			}
            		});

            		pref_rcc.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            			public boolean onPreferenceChange(Preference preference, Object newValue) {
            				Editor editor = settings.edit();        
            				editor.putString(Constant.PREF_RCC,(String)newValue);
            				editor.commit();
            				return true;
            			}
            		});

            		
            		pref_secretory.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            			public boolean onPreferenceChange(Preference preference, Object newValue) {
            				Editor editor = settings.edit();        
            				editor.putString(Constant.PREF_SECRETORY,(String)newValue);
            				editor.commit();
            				return true;
            			}
            		});

            	            		
        }
}
}
