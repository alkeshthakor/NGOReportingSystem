package com.ngo.rs;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ngo.rs.util.Constant;
import com.ngo.rs.util.Pref;

public class ChangesPasswordActivity extends Activity {

	EditText oldPassEditText;
	EditText newPassEditText;
	EditText confirmPassEditText;

	Button saveButton;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changes_password);
		
		ActionBar actionbar=getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);

		Constant.CONTEXT=this;

		oldPassEditText=(EditText)findViewById(R.id.editText_oldpass);
		newPassEditText=(EditText)findViewById(R.id.editText_newpass);
		confirmPassEditText=(EditText)findViewById(R.id.editText_confirm_pass);

		
		saveButton=(Button)findViewById(R.id.savebt);
		
		saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if(Pref.getValue(Constant.PREF_PASSWORD,"").equalsIgnoreCase(oldPassEditText.getText().toString())){
					
					if(newPassEditText.getText().toString().length()>0){
					if(newPassEditText.getText().toString().equals(confirmPassEditText.getText().toString())){
						Pref.setValue(Constant.PREF_PASSWORD,newPassEditText.getText().toString());
						Pref.setValue(Constant.PREF_IS_AUTHENTICATION_CREATED,"true");
						Toast.makeText(getApplicationContext(),"Password successfully changed !!",Toast.LENGTH_SHORT).show();
						finish();
					}else{
						Toast.makeText(getApplicationContext(),"New password and confirm password does not matched !!",Toast.LENGTH_SHORT).show();
					}
					}else{
						Toast.makeText(getApplicationContext(),"Please enter new password !!",Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(getApplicationContext(),"Old password does not matched !!",Toast.LENGTH_SHORT).show();
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.changes_password, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case android.R.id.home:
	        	  finish();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
