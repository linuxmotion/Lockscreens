package com.android.internal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class CircularSelector extends View{
	
	
	// ********************* Debug Variables
	
	
	private String TAG = "CircularSelector";
	private static final boolean DBG = true;
	private static final boolean IDBG = true;
    private static final boolean VISUAL_DEBUG = true;
	
    
    // ***********Rotation constants and variables
    /**
     * Either {@link #HORIZONTAL} or {@link #VERTICAL}.
     */	
	 private int mOrientation;

	 public static final int HORIZONTAL = 0;
	 public static final int VERTICAL = 1;
    
	 
	 // ********************* UI Elements
	 
	   final Matrix mBgMatrix = new Matrix();
	   private Paint mPaint = new Paint();
	   
	   
	   // *** Circular areas *&*
	   Bitmap mOuterCircul;
	   Bitmap mInnerCircul;
	   Bitmap mLockIcon;
	   
	   private float mDensity;
	 
	 
    //
    //********************** Constructors**********
	//
	public CircularSelector(Context context) {
		this(context,null);
		// TODO Auto-generated constructor stub
	}
	public CircularSelector(Context context, AttributeSet attrs) {
		super(context,attrs);
		
		   TypedArray a =
	            context.obtainStyledAttributes(attrs, R.styleable.CircularSelector);
	        mOrientation = a.getInt(R.styleable.CircularSelector_orientation, HORIZONTAL);
	        a.recycle();
	        
	        initializeUI();
		// TODO Auto-generated constructor stub
	}
	
	//**************** Overridden super methods
	
	@Override 
	public boolean onTouchEvent(MotionEvent event){
		super.onTouchEvent(event);
		
		return true;
		
	}
	
	
	@Override 
	public void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
		  if (IDBG) log("Redrawing the view");

          final int width = getWidth();
          final int height = getHeight();

 	    
          if (DBG) log("The width of the view is " + width + " and the hieght of the veiw is " + height );

          if (VISUAL_DEBUG) {
              // draw bounding box around widget

			  if (IDBG) log("Debugging the widget visibly");
              mPaint.setColor(0xffff0000);
              mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
              canvas.drawRect(0, 0, width, height , mPaint);
          }
          
          
          canvas.drawBitmap(mOuterCircul,  0, 0, mPaint);
          canvas.drawBitmap(mLockIcon,  (width/2)-(mLockIcon.getWidth()/2), height-mLockIcon.getHeight(), mPaint);
		
		
		return;
	}
	

    @Override 
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	   
		  if (IDBG) log("Measuring the demensions of the view");
    	   
		  
		  final int length = isHoriz() ?
                  MeasureSpec.getSize(widthMeasureSpec) :
                  MeasureSpec.getSize(heightMeasureSpec);
                  
      	final int height = (isHoriz() ?
                      (MeasureSpec.getSize(heightMeasureSpec)/5)*2 :
                      MeasureSpec.getSize(widthMeasureSpec)/2);
		  
		 
                


		  if (DBG) log("The demensions of the view is length:" + length + " and height: " + height );
           if (isHoriz()) {
               setMeasuredDimension(length, height);
           } else {
               setMeasuredDimension(height, length);
           }
       }

    // ************** Interfacees
    
  
    
    // ************* Initilization function
    
    private void initializeUI(){
    	mOuterCircul = getBitmapFor(R.drawable.honey_circul_portrait);
    	mLockIcon = getBitmapFor(R.drawable.lock_ic_lock);
    }
    
    
    
    
    
    //************************** Misc Function***********************
    private boolean isHoriz() {
        return (mOrientation == HORIZONTAL);
    }
    
    
    private void log(String msg) {
	    Log.d(TAG, msg);
	}
	    
    private Bitmap getBitmapFor(int resId) {
        return BitmapFactory.decodeResource(getContext().getResources(), resId);
    }
    
    
    
}
