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
	    

}
