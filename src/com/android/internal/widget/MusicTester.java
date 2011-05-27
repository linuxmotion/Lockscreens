package com.android.internal.widget;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MusicTester extends Activity implements CircularSelector.OnCircularSelectorTriggerListener{


	    private String mDateFormatString;
	    private TextView mDate;
	    private TextView mTime;
	    private AmPm mAmPm;
	    private TextView mTimeDisplay;


	    private final static String M12 = "h:mm";
	    private final static String M24 = "kk:mm";
	    private Calendar mCalendar;
	    
	    private String mFormat;
	
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
        setContentView(R.layout.portrait);
        }
        else{

            setContentView(R.layout.landscape);
           
        }
          
        //setContentView(R.layout.music_controls);
        
        mDate = (TextView) findViewById(R.id.date);
        
        
        // Time testing stuff
        mTimeDisplay = (TextView) this.findViewById(R.id.timeDisplay);
        mTimeDisplay.setTypeface(Typeface.createFromFile("/system/fonts/Clockopia.ttf"));
        mTimeDisplay.setText("11:34");        
        

       // mAmPm = (TextView) this.findViewById(R.id.am_pm);
        
        mAmPm = new AmPm(((LinearLayout) this.findViewById(R.id.time)), Typeface.createFromFile("/system/fonts/DroidSans-Bold.ttf"));
        
        ////
        
        
        mCircularSelector = (CircularSelector) findViewById(R.id.circular_selector);
        mCircularSelector.setOnCircularSelectorTriggerListener(this);
        
       
        
        
        refreshTimeAndDateDisplay();
        setDateFormat();
        mCalendar = Calendar.getInstance();
        updateTime(mCalendar);
        
        
        
    }
    private void refreshTimeAndDateDisplay() {
        mDate.setText(DateFormat.format("MMMM dd, EEEE, yyyy", new Date()));
    }
	private void log(String msg) {
	    Log.d(TAG, msg);
	}
	public void OnCircularSelectorGrabbedStateChanged(View v, int GrabState) {
		// TODO Auto-generated method stub
		
		
	}
	public void onCircularSelectorTrigger(View v, int Trigger) {
		
		Toast();
		
		
	}
	
	public void Toast(){
		
		Context context = getApplicationContext();
		CharSequence text = "Lock icon triggered toast!";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
		
	}
	
	static class AmPm {
        private TextView mAmPm;
        private String mAmString, mPmString;

        AmPm(View parent, Typeface tf) {
            mAmPm = (TextView) parent.findViewById(R.id.am_pm);
        	if (tf != null) {
                mAmPm.setTypeface(tf);
            }

            String[] ampm = new DateFormatSymbols().getAmPmStrings();
            mAmString = ampm[0];
            mPmString = ampm[1];
        }

        void setShowAmPm(boolean show) {
            mAmPm.setVisibility(show ? View.VISIBLE : View.GONE);
        }

        void setIsMorning(boolean isMorning) {
            mAmPm.setText(isMorning ? mAmString : mPmString);
        }
    }
	
	
	private void setDateFormat() {
        mFormat = android.text.format.DateFormat.is24HourFormat(this.getApplicationContext())
            ? M24 : M12;
        mAmPm.setShowAmPm(mFormat.equals(M12));
    }
	  void updateTime(Calendar c) {
	        mCalendar = c;
	        updateTime();
	    }

	    private void updateTime() {
	        mCalendar.setTimeInMillis(System.currentTimeMillis());

	        CharSequence newTime = DateFormat.format(mFormat, mCalendar);
	        mTimeDisplay.setText(newTime);
	        mAmPm.setIsMorning(mCalendar.get(Calendar.AM_PM) == 0);
	    }
	    
	    
	    
	    
	    
	    
	
}