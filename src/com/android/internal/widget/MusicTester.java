package com.android.internal.widget;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MusicTester extends Activity implements CircularSelector.OnCircularSelectorTriggerListener, SenseLikeLock.OnSenseLikeSelectorTriggerListener, 
RotarySelector.OnDialTriggerListener,OnClickListener{


	    private String mDateFormatString;
	    private TextView mDate;
	    private TextView mTime;
	    private AmPm mAmPm;
	    private TextView mTimeDisplay;
	    private Display mDisplay;
	    private int mOrientation;
	    private ListView mList;
	    
	    private ImageNotificationView mNotif1;
	    private ImageNotificationView mNotif2;
	    private ImageNotificationView mNotif3;
	    private ImageNotificationView mNotif4;

	    private static boolean mUseRotary  = false;
	    private static boolean mCirc       = true;
	    private static boolean mUseTest    = false;

	    private final static String M12 = "h:mm";
	    private final static String M24 = "kk:mm";
	    private Calendar mCalendar;
	    
	    private String mFormat;
	
	private CircularSelector mCircularSelector;
	private RotarySelector mRotarySelector;
	private SenseLikeLock mSenseLock;
	
	
	private String TAG = "LockMusicControlsTester";
	private Context mContext;
	private static boolean DBG = true;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        
        
        /*
         * 
         */
        
        
        /* 
         * 
         * 
         */
        
        

        
        /**/
        mDisplay = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        
        mOrientation = mDisplay.getRotation();

        /*
        if(this.mCirc ==  true)
        	useHCConcept();
        else if(this.mUseRotary == true )
        	useRotary();
        else if(this.mUseTest == true)
        	useTest();
        	*/
        useSense();
        
        //setContentView(R.layout.main);
        
    }
    private void useTest() {
		// TODO Auto-generated method stub
		setContentView(R.layout.keyguard_screen_information_container);
      
	}
private void useSense(){
    	
		if (DBG) log("Setting the sense style");
    	
		 if( mOrientation == Surface.ROTATION_0 || mOrientation == Surface.ROTATION_180){
			 
		      	this.setContentView(R.layout.keyguard_screen_sense_unlock_port);
		      }
		      else{

		          setContentView(R.layout.keyguard_screen_sense_unlock_land);
		         
		      }
		 
		 
		 mSenseLock = (SenseLikeLock) findViewById(R.id.sense_selector);
		 
		 mSenseLock.setOnSenseLikeSelectorTriggerListener(this);
		 mSenseLock.setVisibility(View.VISIBLE);


    	
    	
    	
    }
	private void useRotary(){
    	
		if (DBG) log("Setting the rotary style");
    	
		 if( mOrientation == Surface.ROTATION_0 || mOrientation == Surface.ROTATION_180){
			 
		      	this.setContentView(R.layout.keyguard_screen_rotary_unlock_port);
		      }
		      else{

		          setContentView(R.layout.keyguard_screen_rotary_unlock_land);
		         
		      }
		 
		 
		 mRotarySelector = (RotarySelector) findViewById(R.id.rotary_selector);
		 
		 mRotarySelector.setLeftHandleResource(R.drawable.ic_jog_dial_unlock);
		 mRotarySelector.setRightHandleResource(R.drawable.ic_jog_dial_sound_off);
         mRotarySelector.setOnDialTriggerListener(this);
         mRotarySelector.setVisibility(View.VISIBLE);


    	
    	
    	
    }
    
    private void useHCConcept(){
    	
    	
    	
        
   	 if( mOrientation == Surface.ROTATION_0 || mOrientation == Surface.ROTATION_180){
      	
      	this.setContentView(R.layout.keyguard_screen_circular_unlock_hc_port);
      }
      else{

          setContentView(R.layout.keyguard_screen_circular_unlock_hc_land);
         
      }
         
        
        mNotif1 = (ImageNotificationView) this.findViewById(R.id.notification_image_view1);
        mNotif1.setOnClickListener(this);
        
        mNotif2 = (ImageNotificationView) this.findViewById(R.id.notification_image_view2);
        mNotif2.setOnClickListener(this);
        
        mNotif3 = (ImageNotificationView) this.findViewById(R.id.notification_image_view3);
        mNotif3.setOnClickListener(this);
        
        mNotif4 = (ImageNotificationView) this.findViewById(R.id.notification_image_view4);
        mNotif4.setOnClickListener(this);
        
        
        
        
        mDate = (TextView) findViewById(R.id.date);
        
        
        //  mTime testing stuff
        mTimeDisplay = (TextView) this.findViewById(R.id.timeDisplay);
        mTimeDisplay.setTypeface(Typeface.createFromFile("/system/fonts/Clockopia.ttf"));
        mTimeDisplay.setText("11:34");        
        

        
        mAmPm = new AmPm(((LinearLayout) this.findViewById(R.id.time)), Typeface.createFromFile("/system/fonts/DroidSans-Bold.ttf"));
        
        ////
        
        
        mCircularSelector = (CircularSelector) findViewById(R.id.circular_selector);
        mCircularSelector.setOnCircularSelectorTriggerListener(this);
        
       
        
        
        refreshTimeAndDateDisplay();
        setDateFormat();
        mCalendar = Calendar.getInstance();
        updateTime(mCalendar);
        
     
    	
    }
    
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.options, menu);


    	return true;
    }
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent i = new Intent();
    	i.setClassName("com.android.internal.widget", "com.android.internal.widget.MusicTester");
    	
        switch (item.getItemId()) {
            case R.id.rotary:     
            	Toast.makeText(this, "You pressed the rotary!", Toast.LENGTH_LONG).show();
            	this.mCirc = false;
            	this.mUseRotary = true;
            	this.mUseTest = false;
                startActivity(i);

                                break;
            case R.id.circular:     
            	Toast.makeText(this, "You pressed the circile!", Toast.LENGTH_LONG).show();
            	this.mCirc = true;
            	this.mUseRotary = false;
            	this.mUseTest = false;
                startActivity(i);

            
                                break;
            case R.id.test:         
            	Toast.makeText(this, "You pressed the notifications layout", Toast.LENGTH_LONG).show();
            	this.mCirc = false;
            	this.mUseRotary = false;
            	this.mUseTest = true;
            	
                startActivity(i);

            					break;
        }
        
        return true;
    }
    
    
    
    
    
    
    
    /******************************************/
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
public void Toast(String string){
		
		Context context = getApplicationContext();
		
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, string, duration);
		toast.show();
		
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
		public void onClick(View v) {
			Intent i = new Intent();
			if(v == mNotif1){
				
				log("Notification pressed");
				
				Intent intent = new Intent();
				intent.setClassName("com.android.settings", "com.android.settings.fuelgauge.PowerUsageSummary");
				
				startActivity(intent);
				
				
				
			}
			if(v == mNotif2){
				
				log("Notification pressed");
				startActivity(new Intent(Intent.ACTION_DIAL));
				
			}
			if(v == mNotif3){
	
				log("Notification pressed");
				Uri uri = Uri.parse("smsto:xxxxxxxx"); 
				Intent intent = new Intent(Intent.ACTION_SENDTO, uri); 
				intent.putExtra("sms_body", ""); 
				startActivity(new Intent(intent));
	
			}
			if(v == mNotif4){
				
				log("Notification pressed");
	

				i.setClassName("com.google.android.gm", "com.google.android.gm.ConversationListActivityGmail");
				startActivity(new Intent(i));
				
			}
		
			i = null;
			
			// TODO Auto-generated method stub
			
		}
		public void onDialTrigger(View v, int whichHandle) {
			// TODO Auto-generated method stub
			if(whichHandle == RotarySelector.OnDialTriggerListener.LEFT_HANDLE)Toast();
			if(whichHandle == RotarySelector.OnDialTriggerListener.RIGHT_HANDLE)Toast();
		}
		public void onGrabbedStateChange(View v, int grabbedState) {
			// this would poke the wake lock
			// TODO Auto-generated method stub
			
		}
		@Override
		public void OnSenseLikeSelectorGrabbedStateChanged(View v, int GrabState) {
			// TODO Auto-generated method stub
			
			
		}
		@Override
		public void onSenseLikeSelectorTrigger(View v, int Trigger) {
			// TODO Auto-generated method stub
			Toast();
		}
		
		
		
	    
	    
	    
	    
	    
	    
	
}
