package com.android.internal.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class MusicTester extends Activity implements CircularSelector.OnCircularSelectorTriggerListener{
	
	
	CircularSelector mCircularSelector;
	private String TAG = "LockMusicControlsTester";
	private static final boolean DBG = false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        
        int orientation = display.getRotation();
        
        
      
        
        if( orientation == Surface.ROTATION_0 || orientation == Surface.ROTATION_180){
        setContentView(R.layout.main);
        }
        else{

            setContentView(R.layout.landscape);
        }
          
        mCircularSelector = (CircularSelector) findViewById(R.id.circular_selector);
        mCircularSelector.setOnCircularSelectorTriggerListener(this);
        
        
        
    }
	private void log(String msg) {
	    Log.d(TAG, msg);
	}
	public void OnCircularSelectorGrabbedStateChanged(View v, int GrabState) {
		// TODO Auto-generated method stub
		
		
	}
	public void onCircularSelectorTrigger(View v, int Trigger) {
		Toast();
		// TODO Auto-generated method stub
		
		
		
	}
	
	public void Toast(){
		
		Context context = getApplicationContext();
		CharSequence text = "Lock icon triggered toast!";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		
	}
	
}