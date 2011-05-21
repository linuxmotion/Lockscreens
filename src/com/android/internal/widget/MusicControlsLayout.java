package com.android.internal.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class MusicControlsLayout extends RelativeLayout{
	
	
	ImageView  mPlayPauseButton;
	ImageView  mFastFowardButton;
	ImageView  mRewindButton;
	RelativeLayout mLayout;
	

    public MusicControlsLayout(Context context) {
        this(context, null);
        
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.musiccontrols, this);

    }

    public MusicControlsLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        

    	
    	  LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
          View view = layoutInflater.inflate(R.layout.musiccontrols, this);
      
      	 
        
    }
    
   
    @Override
    public void onFinishInflate(){
    	super.onFinishInflate();
    	
    	 mPlayPauseButton = (ImageView) findViewById(R.id.play_pause_button);
    	 mFastFowardButton = (ImageView) findViewById(R.id.fast_forward_button);
    	 mRewindButton = (ImageView) findViewById(R.id.rewind_button);
    	 mLayout = (RelativeLayout) findViewById(R.id.music_controls_layout);
    	 
   
         NinePatchDrawable myNinePatchDrawable = (NinePatchDrawable) getResources().getDrawable(R.drawable.lock_ic_background);
    	 mLayout.setBackgroundDrawable(myNinePatchDrawable);
    	 
    	 
     mPlayPauseButton.setImageBitmap(getBitmapFor(R.drawable.lock_ic_media_play));
     
   	 //mPlayPauseButton.setImageResource(R.drawable.lock_ic_media_play);
   	 //mFastFowardButton.setImageResource(R.drawable.lock_ic_media_next);
   	 //mRewindButton.setImageResource(R.drawable.lock_ic_media_previous);
    	 
    }
    
    @Override
    protected void onAttachedToWindow() {
    	super.onAttachedToWindow();
    	

    }
    
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
    private Bitmap getBitmapFor(int resId) {
        return BitmapFactory.decodeResource(getContext().getResources(), resId);
    }
    

}
