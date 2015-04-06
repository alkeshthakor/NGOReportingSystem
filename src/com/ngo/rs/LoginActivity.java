package com.ngo.rs;

import com.ngo.rs.util.Constant;
import com.ngo.rs.util.Pref;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	EditText useridEditText;
	EditText passwordEditText;
	
	Button loginButton;
	
	TextView changePassword;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		Constant.CONTEXT=this;

		useridEditText=(EditText)findViewById(R.id.editText_userid);
		passwordEditText=(EditText)findViewById(R.id.editText_password);
		changePassword=(TextView)findViewById(R.id.change_passowrdTV);
		
		loginButton=(Button)findViewById(R.id.loginbt);
		
		
		loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if(useridEditText.getText().toString().length()>0&&passwordEditText.getText().toString().length()>0){
					if(useridEditText.getText().toString().equalsIgnoreCase(Pref.getValue(Constant.PREF_USER_ID,""))&&passwordEditText.getText().toString().equalsIgnoreCase(Pref.getValue(Constant.PREF_PASSWORD,""))){
						Intent homeIntent=new Intent(getApplicationContext(),HomeActivity.class);
						startActivity(homeIntent);
					}else{
						Toast.makeText(getApplicationContext(),"Invalid userid or password !",Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(getApplicationContext(),"Please enter userid and password !",Toast.LENGTH_SHORT).show();
				}
				
			}
		});
		
		
		changePassword.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			 Intent changeIntent=new Intent(getApplicationContext(),ChangesPasswordActivity.class);
			 startActivity(changeIntent);
			}
		});
	}

	

}
