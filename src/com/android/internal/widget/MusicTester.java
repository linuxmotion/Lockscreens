package com.android.internal.widget;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

public class MusicTester extends Activity {
	
	MusicControls mMusicControls;
	CircularSelector mCircularSelector;
	
	private String TAG = "LockMusicControlsTester";
	private static final boolean DBG = false;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        
        int orientation = display.getRotation();
        
        
        mCircularSelector = (CircularSelector) findViewById(R.id.circular_selector);
        
        if( orientation == Surface.ROTATION_0 || orientation == Surface.ROTATION_180){
        setContentView(R.layout.main);
        }
        else{

            setContentView(R.layout.landscape);
        }
        
        
        
    }
	private void log(String msg) {
	    Log.d(TAG, msg);
	}
	
}