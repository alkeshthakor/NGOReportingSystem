package com.ngo.rs;

import com.ngo.rs.util.Constant;
import com.ngo.rs.util.Pref;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class SplashActivity extends Activity {

	/**
     * The thread to process splash screen events
     */
    private Thread mSplashThread;  
    Context context;

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
		setContentView(R.layout.activity_splash);
		Constant.CONTEXT=this;
		Pref.getValue(Constant.PREF_IS_AUTHENTICATION_CREATED,"false");
		if(Pref.getValue(Constant.PREF_IS_AUTHENTICATION_CREATED,"false").equalsIgnoreCase("false")){
			Pref.setValue(Constant.PREF_USER_ID,"admin");
			Pref.setValue(Constant.PREF_PASSWORD,"admin");
			Pref.setValue(Constant.PREF_IS_AUTHENTICATION_CREATED,"true");
		}
		context=this;  
        // The thread to wait for splash screen events
        mSplashThread =  new Thread(){
            @Override
            public void run(){
                try {
                    synchronized(this){
                        // Wait given period of time or exit on touch
                        wait(2000);
                    }
                }
                catch(InterruptedException ex){ }
                finish();
                // Run next activity
                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
              //  stop();                    
            }
        };
        mSplashThread.start();        
    }
	
}
