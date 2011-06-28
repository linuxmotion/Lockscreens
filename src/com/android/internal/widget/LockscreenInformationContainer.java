package com.android.internal.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

public class LockscreenInformationContainer extends LinearLayout {

	
	
	public LockscreenInformationContainer(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}	
	
	public LockscreenInformationContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	
	@Override 
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
		
		Log.d("LockcsreenInformationContatiner", "On Draw");
		
		
		
		
		
	}

	   
    @Override
    public void onFinishInflate(){
    	super.onFinishInflate();

		Log.d("LockcsreenInformationContatiner", "OnFinishInflate");
    
    }
	
	@Override
	public void onAttachedToWindow(){
		super.onAttachedToWindow();
		
		Log.d("LockcsreenInformationContatiner", "On attached");
		
		
		
	}
	

}
