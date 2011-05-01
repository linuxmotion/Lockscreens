package com.android.internal.widget;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.AudioManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

//import com.android.internal.R;

public class MusicControls extends View {
	
	
    private final int OFFSETT = 60;
    private final int PADDING = 2;
    private final int SPACING = 20;

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

	private String TAG = "LockMusicControls";
	private static final boolean DBG = false;
	private static final boolean IDBG = false;
    private static final boolean VISUAL_DEBUG = false;
	
	
	// Listener for onMusic*Listeners() callbacks.
    private OnMusicTriggerListener mOnMusicTriggerListener; 
    
    final Matrix mBgMatrix = new Matrix();
    private float mDensity;
    
    // UI elements

    private Bitmap mLeftHandle;
    private Bitmap mRightHandle;
    
    private Bitmap mUnscalledAlbumArt;
    
    private Bitmap mBackground;
    private Bitmap mAlbumArt;
    private Bitmap mPlayButton;
    private Bitmap mPauseButton;
    private Bitmap mSkipButton;
    private Bitmap mSeekButton;
    private Bitmap mArtistName;
    private Bitmap mAlbumName;
    

    private int mBackgroundWidth;
    private int mBackgroundHeight;
    
	// Albums stats
	private static String mArtist = "";
	private static String mTrack = "";
	private static Boolean mPlaying = false;
	private static long mSongId = 0;
	private static long mAlbumId = 0;
	
	private static Context mContext;
	/**
     * Either {@link #HORIZONTAL} or {@link #VERTICAL}.
     */	
	 private int mOrientation;
	 
	// true if the music controls are hidden
	private boolean mHidden = false;
	
	 private Paint mPaint = new Paint();
	
	 /**
     * If the user is currently dragging something.
     */
    private int mGrabbedState = NOTHING_GRABBED;
    public static final int NOTHING_GRABBED = 0;
    public static final int LEFT_HANDLE_GRABBED = 1;
    public static final int RIGHT_HANDLE_GRABBED = 2;
    
  	/**
	 * If the user selected a music control
	 */
	public static final int PLAY_PRESSED = 10;
	public static final int PAUSE_PRESSED = 11;
	public static final int SKIP_PRESSED = 12;
	public static final int SEEK_PRESSED = 13;
	public static final int ALBUM_ART_PRESSED = 14;

    
	 // positions of the left and right handle
    private int mLeftHandleX;
    private int mRightHandleX;

    
    
    private AudioManager am = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);
    private boolean mIsMusicActive = am.isMusicActive();
    private boolean mWasMusicActive = false;
    private boolean mAreMusicControlsVisible = true;
    //private boolean m
    
	
    private Vibrator mVibrator;
    
    /**
     * Whether the user has triggered something (e.g dragging the left handle all the way over to
     * the right).
     */
    private boolean mTriggered = false;


    /**
     * How far from the edge of the screen the user must drag to trigger the event.
     */
    private static final int EDGE_TRIGGER_DIP = 100;
    
    
	
	public MusicControls(Context context){
	     this(context, null);
	}
	
	
	/**
     * Constructor used when this widget is created from a layout file.
     */
	public MusicControls(Context context, AttributeSet attrs)  {
		 super(context,attrs);
		 
		 // Set the widget layout structure
		 
		 TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MusicControls);
	     mOrientation = a.getInt(R.styleable.MusicControls_orientation, HORIZONTAL);
	     a.recycle();
	     
	     Resources r = getResources();
	        mDensity = r.getDisplayMetrics().density;
	        if (DBG) log("- Density: " + mDensity);
	     
	     // Set the background of the music widget
	     // This should not be completely transparent
	     // And should be a .9 to stretch
	        
	    
	       
	     //mBackground =  Bitmap.createScaledBitmap(mUnscalledAlbumArt,480,347,false);
	     
	     mBackground =  Bitmap.createBitmap(480, 347, Bitmap.Config.RGB_565);
	     
	     mLeftHandle = this.getBitmapFor(R.drawable.lock_ic_media_handle);
	     mRightHandle = this.getBitmapFor(R.drawable.lock_ic_media_handle);
	     
	     

	     mUnscalledAlbumArt = getBitmapFor(R.drawable.lock_ic_default_artwork);
	     
	     
	     
	     mPlayButton = this.getBitmapFor(R.drawable.lock_ic_media_play);
	     mPauseButton = this.getBitmapFor(R.drawable.lock_ic_media_pause);
	     mSkipButton = this.getBitmapFor(R.drawable.lock_ic_media_next);
	     mSeekButton = this.getBitmapFor(R.drawable.lock_ic_media_previous);
		
	     
	     mBackgroundWidth = mBackground.getWidth();
	        mBackgroundHeight = mBackground.getHeight();
		 
	}
	
    private Bitmap getBitmapFor(int resId) {
        return BitmapFactory.decodeResource(getContext().getResources(), resId);
    }
    
    
	
    private boolean isHoriz() {
        return mOrientation == HORIZONTAL;
    }
	  @Override
	    protected void onFinishInflate() {
	        super.onFinishInflate();
	        // Set the default inflation space
	   
	    }

	  
	  @Override
	    protected void onAttachedToWindow() {     
		  if (IDBG) log("Attching " + TAG + " to the window");
		  
		  	 mContext = this.getContext();
			
			 IntentFilter iF = new IntentFilter();
			 iF.addAction("com.android.music.metachanged");
			 iF.addAction("com.android.music.playstatechanged");
			 
			 // Register if the music play state has change
			 mContext.registerReceiver(mMusicReceiver, iF);
	  }
	  
	  @Override
	  protected void onDetachedFromWindow(){
		  
		  mContext = this.getContext();
		  mContext.unregisterReceiver(mMusicReceiver);
		  
	  }
	
	 // Broadcast receiver to determine if the music state has changed
	 // 
	 private BroadcastReceiver mMusicReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				         String action = intent.getAction();
		            mArtist = intent.getStringExtra("artist");
		            mTrack = intent.getStringExtra("track");
		            mPlaying = intent.getBooleanExtra("playing", false);
		            mSongId = intent.getLongExtra("songid", 0);
		            mAlbumId = intent.getLongExtra("albumid", 0);
		            
		            // Update the lock screen music controls here
		            intent = new Intent("internal.policy.impl.updateSongStatus");
		            
		            // Send the broadcast signaling that the lockscreen should update the controls
		            context.sendBroadcast(intent);
			}

	    };
	

	    private static String NowPlayingArtist() {
	        if (mArtist != null && mPlaying) {
	            return (mArtist);
	        } else {
	            return "";
	        }
	    }

	    private static String NowPlayingAlbum() {
	        if (mArtist != null && mPlaying) {
	            return (mTrack);
	        } else {
	            return "";
	        }
	    }

	    private static long SongId() {
	        return mSongId;
	    }

	    private static long AlbumId() {
	        return mAlbumId;
	    }
	    
	    
	    public interface OnMusicTriggerListener{

	    	
	 	   /**
	         * The music widget was triggered because the user grabbed the left handle,
	         * and moved the handle to the right.
	         */
	        public static final int LEFT_HANDLE = 1;

	        /**
	         * The music widget was triggered because the user grabbed the right handle,
	         * and moved the handle to the left.
	         */
	        public static final int RIGHT_HANDLE = 2;
	    
	    	
	    	/**
	    	 * The music controls play button was pressed 
	    	 */
	    	public static final int PLAY = 10;
	    	/**
	    	 * The music controls pause button was pressed 
	    	 */
	    	public static final int PAUSE = 11;
	    	/**
	    	 * The music controls play button was pressed 
	    	 */
	    	public static final int SKIP = 11;
	    	/**
	    	 * The music controls play button was pressed 
	    	 */
	    	public static final int SEEK = 12;
	    	
	    	
	    	
	        /**
	         * Called when one of the music button changes (i.e. when
	         * the user either plays, pauses, seeks or skips a track.)
	         *
	         * @param v the view that was triggered
	         * @param grabbedState the new state: either {@link #PLAY_PRESSED},
	         * {@link #PUASE_PRESSED}, {@link #SKIP_PRESSED}, or {@link #SEEK_PRESSED}.
	         */
	    	public void onMusicButtonStateChange(View v, int musicstate);
	    	
	    	  /**
		         * Called when the "grabbed state" changes (i.e. when
		         * the user either grabs or releases one of the handles.)
		         *
		         * @param v the view that was triggered
		         * @param grabbedState the new state: either {@link #NOTHING_GRABBED},
		         * {@link #LEFT_HANDLE_GRABBED}, or {@link #RIGHT_HANDLE_GRABBED}.
		         */
	    	  public void onMusicGrabbedStateChange(View v, int grabbedState);
		        
	    	  /**
		         * Called when the Music Handle is triggered.
		         *
		         * @param v The view that was triggered
		         * @param whichHandle  Which "dial handle" the user grabbed,
		         *        either {@link #LEFT_HANDLE}, {@link #RIGHT_HANDLE}.
		         */
		        public void onMusicHandleTrigger(View v, int whichHandle);
		    
		    
		        /**
		         * Called when the music control is triggered.
		         *
		         * @param v The view that was triggered
		         * @param whichControl  Which "music control" the user pressed,
		         * either {@link #PLAY}, {@link #PUASE}, 
		         * {@link #SKIP}, or {@link #SEEK}.
		         */
			   public void onMusicControlTrigger(View v, int whichControl);
		}
	    	
	    
	    
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
	
	    
	 /**/
		
        @Override 
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        	   
			  if (IDBG) log("Measuring the demensions of the view");
        	   
        	final int length = isHoriz() ?
                    MeasureSpec.getSize(widthMeasureSpec) :
                    MeasureSpec.getSize(heightMeasureSpec);
                    
        	final int height = (isHoriz() ?
                        MeasureSpec.getSize(heightMeasureSpec) :
                        MeasureSpec.getSize(widthMeasureSpec))/4;
                    


			  if (DBG) log("The demensions of the view is length:" + length + " and height: " + height );
               if (isHoriz()) {
                   setMeasuredDimension(length, height);
               } else {
                   setMeasuredDimension(height, length);
               }
           }
    	/**/
	 
	    @Override
	    protected void onDraw(Canvas canvas){
	    	  super.onDraw(canvas);
	    	  

			  if (IDBG) log("beginning to draw the view");

	          final int width = getWidth();
	          final int height = getHeight();

	 	     mAlbumArt = Bitmap.createScaledBitmap(mUnscalledAlbumArt, (height-4), (height-4), false);
	 	     
	          if (DBG) log("The width of the view is " + width + " and the hieght of the veiw is " + height );

	          if (VISUAL_DEBUG) {
	              // draw bounding box around widget

				  if (IDBG) log("Debugging the widget visibly");
	              mPaint.setColor(0xffff0000);
	              mPaint.setStyle(Paint.Style.STROKE);
	              canvas.drawRect(0, 0, width, height , mPaint);
	          }
	          
	          // Background: 

			  if (IDBG) log("Drawing the background");
	         // canvas.drawBitmap(mBackground, mBgMatrix, mPaint);
	          
			  canvas.drawBitmap(mLeftHandle,  0, 0, mPaint); 
			  canvas.drawBitmap(mRightHandle,  width-mRightHandle.getWidth(), 0, mPaint);
			  
			  
	          // Draw music album
	          if (IDBG) log("Drawing the music album");
	          canvas.drawBitmap(mAlbumArt,  mLeftHandle.getWidth()+PADDING, (height-mAlbumArt.getHeight())+PADDING, mPaint);
	          
	          

	         if(am.isMusicActive() && mWasMusicActive && mAreMusicControlsVisible) {
	        	
	        	 canvas.drawBitmap(mPauseButton,  OFFSETT+(width/2)+SPACING , (height-mPauseButton.getHeight()), mPaint);
	        	 mWasMusicActive = true;
	         }
	         else if(mAreMusicControlsVisible){
	        	 
	        	 canvas.drawBitmap(mPlayButton, OFFSETT+(width/2)+SPACING , (height-mPlayButton.getHeight()), mPaint); 
	        	 mWasMusicActive = false;
	         }
	          
	          // Draw music buttons
	          if (IDBG) log("Drawing the buttons");
	          
	         
	          
	          canvas.drawBitmap(mSkipButton, OFFSETT+(width/2)+mPauseButton.getWidth()+3*SPACING   , (height-mSkipButton.getHeight()), mPaint);
	          
	          canvas.drawBitmap(mSeekButton, OFFSETT+(width/2)-mPauseButton.getWidth()-SPACING , (height-mSeekButton.getHeight()), mPaint);
	          
	          
	          
	    	
	    	
	    }
	    /**
	     * Handle touch screen events.
	     *
	     * @param event The motion event.
	     * @return True if the event was handled, false otherwise.
	    */
	    
	    @Override
	    public boolean onTouchEvent(MotionEvent event) {
	    	

	        final int height = getHeight();

	    	
	    	final int eventX = isHoriz() ?
	                (int) event.getX():
	                height - ((int) event.getY());
	                
	                final int eventY = isHoriz() ?
	    	                (int) event.getY():
	    	                height - ((int) event.getX());
	    	
	    	final int action = event.getAction();
	    	 
	    	 if (DBG) log("Width = " + getWidth() + " Height = " + getHeight());
	    	 
	         switch (action) {
	         	case MotionEvent.ACTION_DOWN:
	         		if (DBG) log("touch-down");
                    if (DBG) log("x -" + eventX + " y -" + eventY);
	         		  mTriggered = false;
	                  if (mGrabbedState != NOTHING_GRABBED) {

	  	         		if (DBG) log("grabState -" + mGrabbedState);
	                      
	  	         		  reset();
	                      invalidate();
	                      
	                  }

		         	  // Determine if pause/play pressed	                  
	                  if(((eventX > (OFFSETT+(getWidth()/2)+SPACING-(3*PADDING) ))
	                		  && (eventX < (getWidth() + (3*PADDING) - 3*SPACING-(2*mSkipButton.getWidth())))) && (eventY > (getHeight()-mPlayButton.getHeight()))){
	                	  
	                	  
	                	  
	                	  
	                	  if(!mWasMusicActive){
	                		if (DBG) log("Play button pressed");
	                		dispatchTriggerEvent(MusicControls.PLAY_PRESSED); 
	                		
	                		
	                		mWasMusicActive = !am.isMusicActive() ? true : false;
	                	
	                		
	                		
	                	  }
	                	  
	                	  else {
	                	  if (DBG) log("Pause button pressed");
	                	  dispatchTriggerEvent(MusicControls.PAUSE_PRESSED);
	                	  

	                		mWasMusicActive = am.isMusicActive() ? true : false;
	                	  
	                	  }
	                	  invalidate();
	                	  
	                  }
	                 
	                  // Determine Seek pressed
	                  if((eventX > ( mAlbumArt.getWidth() + 2*SPACING )) 
	                		  && (eventX < ( getWidth() - (3*PADDING) - mSkipButton.getWidth()- 3 *SPACING  - mPlayButton.getWidth() - OFFSETT )) && 
	                		  (eventY > (getHeight()-mPlayButton.getHeight()))){
	                	  

                		  if (DBG) log("Seek button pressed");
	                	  dispatchTriggerEvent(MusicControls.SEEK_PRESSED);
	                	  
	                	  
	                  } 
	                  
	                  // Determine Skip pressed
	                  if((eventX > (getWidth()  - mSkipButton.getWidth()- 3*SPACING   )) 
	                		  && (eventX < (getWidth() - PADDING - SPACING)) && 
	                		  (eventY > (getHeight()-mPlayButton.getHeight() - (2*SPACING)))){

                		  if (DBG) log("Skip button pressed");
	                	  dispatchTriggerEvent(MusicControls.SKIP_PRESSED);
	                	  
	                	  
	                  }
	                  // Determine album art pressed
	                  if(eventX < mAlbumArt.getWidth() && eventX > mLeftHandle.getWidth()){

                		  if (DBG) log("Album art pressed");
	                	  dispatchTriggerEvent(MusicControls.ALBUM_ART_PRESSED);
	                	  
	                  }
	                  break;
	            case MotionEvent.ACTION_MOVE:
	                if (IDBG) log("touch-move");
                    if (DBG) log("x = " + eventX + " y = " + eventY);
	                // This is where the slide animation to the left or right would occur
	            	invalidate();
	                break;
	            case MotionEvent.ACTION_UP:
	                if (IDBG) log("touch-up");
                    if (DBG) log("x = " + eventX + " y = " + eventY);
	                // This is where the animation to "snap back" the left or right would occur
	            	invalidate();
	                break;
	            case MotionEvent.ACTION_CANCEL:
	            	 if (DBG) log("touch-cancel");
	                 if (DBG) log("x = " + eventX + " y = " + eventY);
	                 reset();
	                 invalidate();
	            	break;
	         }
	         return true;
	    
	    
	    }
	     /**/
	    		
	    /**
	     * Registers a callback to be invoked when the music controls
	     * are "triggered" by sliding the view one way or the other
	     * or pressing the music control buttons.
	     *
	     * @param l the OnMusicTriggerListener to attach to this view
	     */
	    public void setOnMusicTriggerListener(OnMusicTriggerListener l) {
	    	 if (DBG) log("Setting the listners");
	    	mOnMusicTriggerListener = l;
	    }
	    
	    /**
	     * Dispatches a trigger event to our listener.
	     */
	    private void dispatchTriggerEvent(int whichHandle) {
	    	
	    	 if (IDBG) log("Dispatching a trigered event");
	        //vibrate(VIBRATE_LONG);
	        if (mOnMusicTriggerListener != null) {
	            
	        	if(whichHandle >= OnMusicTriggerListener.PLAY)
	        		mOnMusicTriggerListener.onMusicControlTrigger(this, whichHandle);
	        	else	            
	        		mOnMusicTriggerListener.onMusicHandleTrigger(this, whichHandle);
	            
	        }
	    }
	    /**
	     * Sets the current button pressed state, and dispatches a media state change
	     * event to our listener.
	     */
	    private void setMusicControlTrigger(int musiccontrol){
	    	
	    	 if (IDBG) log("Music control triggered");
	    	
	    	if (mOnMusicTriggerListener != null) {
	    		mOnMusicTriggerListener.onMusicControlTrigger(this, musiccontrol);
	    	}
	    	
	    }
	    
	    /**
	     * Sets the current handle pressed state, and dispatches a grabbed handle state change
	     * event to our listener.
	     */
	    private void setMusicHandleTrigger(int whichHandle){
	    	 if (IDBG) log("Setting the music control handle triggered");
	    	if (mOnMusicTriggerListener != null) {
	    		mOnMusicTriggerListener.onMusicHandleTrigger(this, whichHandle);
	    	}
	    	
	    }
	
	    /**
	     * Sets the current button pressed state, and dispatches a pressed button state change
	     * event to our listener.
	     */
	    private void setMusicButtonStateChanged(int musicstate){
	    	 if (IDBG) log("The music button state has changed");
	    	
	    	if (mOnMusicTriggerListener != null) {
	    		mOnMusicTriggerListener.onMusicButtonStateChange(this, musicstate);
	    	}
	    	
	    }
	    
	    /**
	     * Sets the current grabbed state, and dispatches a grabbed state change
	     * event to our listener.
	     */
	    private void setGrabbedState(int newState) {

	    	 if (IDBG) log("Determining the grab state change");
	        if (newState != mGrabbedState) {
	            mGrabbedState = newState;
	            if (mOnMusicTriggerListener != null) {
	            	mOnMusicTriggerListener.onMusicGrabbedStateChange(this, mGrabbedState);
	            }
	        }
	    }
	    
	    /**
	     * Triggers haptic feedback.
	     */
	    private synchronized void vibrate(long duration) {
	        if (mVibrator == null) {
	            mVibrator = (android.os.Vibrator)
	                    getContext().getSystemService(Context.VIBRATOR_SERVICE);
	        }
	        mVibrator.vibrate(duration);
	    }
	    	
	    private void reset() {
	    	
	        //mAnimating = false;
	        setGrabbedState(NOTHING_GRABBED);
	        mTriggered = false;
	    }
	    
	    public void changeVisiblity(){
	    	
	    	if(!mHidden){
	    		setVisibility(VISIBLE);
	    		mHidden = false;
	    	}
	    	else {
	    		setVisibility(GONE);
	    		mHidden = true;
	    	}
	    	
	    }


// Debugging / testing code

	private void log(String msg) {
	    Log.d(TAG, msg);
	}
	    
	
}
