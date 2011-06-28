package com.android.internal.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class DividerView extends View{

	private Bitmap mDivider;
	private final String TAG = "DiverView";
	
	

	   final Matrix mBgMatrix = new Matrix();
	   private Paint mPaint = new Paint();
	
	public DividerView(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}
	
	public DividerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		
		
		
		mDivider = getBitmapFor(R.drawable.lock_ic_media_handle_land);
	}
	
	
	
	@Override 
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		

        mPaint.setColor(0xffff0000);
        mPaint.setStyle(Paint.Style.STROKE);
        
        canvas.drawBitmap(mDivider, (getWidth() - mDivider.getWidth() )/2,0, mPaint);
		
		
		
		
	}
	
	
	
	
	  private Bitmap getBitmapFor(int resId) {
	        return BitmapFactory.decodeResource(getContext().getResources(), resId);
	    }
	    
	    
	    
	 // Debugging / testing code

		private void log(String msg) {
		    Log.d(TAG, msg);
		}
	
	

}
