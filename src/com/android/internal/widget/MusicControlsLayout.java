package com.android.internal.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.media.AudioManager;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MusicControlsLayout extends RelativeLayout implements OnClickListener{
	
	
	private String TAG = "MusicControlsLayout";
	private static final boolean DBG = true;
	private static final boolean IDBG = true;
    private static final boolean VISUAL_DEBUG = true;
	
	ImageView  mPlayPauseButton;
	ImageView  mFastFowardButton;
	ImageView  mRewindButton;
	RelativeLayout mLayout;
	
	boolean mIsMusicPlaying;
	boolean mControlsAlwaysDisplayed;
	
	
	// Albums stats
	private static String mArtist = "";
	private static String mTrack = "";
	private static long mSongId = 0;
	private static long mAlbumId = 0;
	
	
	private AudioManager am = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);
	
	private static Context mContext;

    public MusicControlsLayout(Context context) {
        this(context, null);
        
   
    }

    public MusicControlsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        

        
    }
    
   
    @Override
    public void onFinishInflate(){
    	super.onFinishInflate();
    	
    	
    	 
    	 mLayout = (RelativeLayout) this.findViewById(R.id.music_controls_layout);
    	 /*
    	 mPlayPauseButton = (ImageView) mLayout.findViewById(R.id.play_pause_button);
    	 mFastFowardButton = (ImageView) mLayout.findViewById(R.id.fast_forward_button);
    	 mRewindButton = (ImageView) mLayout.findViewById(R.id.rewind_button);
    	 
    	 
    	
        this.mPlayPauseButton.setOnClickListener(this);
        this.mFastFowardButton.setOnClickListener(this);
        this.mRewindButton.setOnClickListener(this);
    	 
   
    	   
    	 mRewindButton.setImageBitmap(getBitmapFor(R.drawable.lock_ic_media_previous));
    	 mPlayPauseButton.setImageBitmap(getBitmapFor(R.drawable.lock_ic_media_play));
    	 mFastFowardButton.setImageBitmap(getBitmapFor(R.drawable.lock_ic_media_next));
    	 */
    }
    
    @Override
    protected void onAttachedToWindow() {
    	super.onAttachedToWindow();
    	
    	  
   	 
    	// Register call backs
    	  if (IDBG) log("Attching " + TAG + " to the window");
		  
		  	 mContext = this.getContext();
			
			 IntentFilter iF = new IntentFilter();
			 iF.addAction("com.android.music.metachanged");
			 iF.addAction("com.android.music.playstatechanged");
			 
			 // Register if the music play state has change
			 mContext.registerReceiver(mMusicReceiver, iF);
    	

    }
    
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        
        // unregister call backs

		  mContext = this.getContext();
		  mContext.unregisterReceiver(mMusicReceiver);
		  
		  mContext = null;
        
    }
    
    public void onClick(View v) {
		// TODO Auto-generated method stub
		log("A view has been clicked");
		
		
		if(v.equals(mRewindButton)){
			
			
		}

		if(v.equals(this.mPlayPauseButton)){
			
			
		}

		if(v.equals(this.mFastFowardButton)){
			
			
		}

		
		
	}
    
    

	private BroadcastReceiver mMusicReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			         String action = intent.getAction();
	            mArtist = intent.getStringExtra("artist");
	            mTrack = intent.getStringExtra("track");
	            mIsMusicPlaying = intent.getBooleanExtra("playing", false);
	            mSongId = intent.getLongExtra("songid", 0);
	            mAlbumId = intent.getLongExtra("albumid", 0);
	            
	            // Update the lock screen music controls here
	            intent = new Intent("internal.policy.impl.updateSongStatus");
	            
	            // Send the broadcast signaling that the lockscreen should update the controls
	            context.sendBroadcast(intent);
		}

    };
    
    
    
	   public void sendMediaButtonEvent(int code) {
		   

			  if (IDBG) log("sending media button event");
	        long eventtime = SystemClock.uptimeMillis();

	        Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
	        KeyEvent downEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_DOWN, code, 0);
	        downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent);
	        getContext().sendOrderedBroadcast(downIntent, null);

	        Intent upIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
	        KeyEvent upEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_UP, code, 0);
	        upIntent.putExtra(Intent.EXTRA_KEY_EVENT, upEvent);
	        getContext().sendOrderedBroadcast(upIntent, null);
	    }
    
    private Bitmap getBitmapFor(int resId) {
        return BitmapFactory.decodeResource(getContext().getResources(), resId);
    }
    
    
    
 // Debugging / testing code

	private void log(String msg) {
	    Log.d(TAG, msg);
	}
	    

}
