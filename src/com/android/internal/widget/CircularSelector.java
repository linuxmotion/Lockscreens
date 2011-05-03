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
	private static final boolean TDBG = false;
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
	   
	   
	   // *** Circular areas **
	   Bitmap mOuterCircul;
	   Bitmap mInnerCircul;
	   Bitmap mLockIcon;
	   private int mLockX, mLockY;
	   private boolean mIsTouchInCircle = false;
	   
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
		
		final int height = getHeight();
		final int width  = getWidth();
		
		final int action = event.getAction();
		
    	final int eventX = isHoriz() ?
                (int) event.getX():
                height - ((int) event.getY());
                
        final int eventY = isHoriz() ?
    	                (int) event.getY():
    	                height - ((int) event.getX());
		

        if (DBG) log("x -" + eventX + " y -" + eventY);
    	                
		switch (action) {
        case MotionEvent.ACTION_DOWN:
            if (DBG) log("touch-down");
            /* If the event is lower thatn the inner radius than cause the lock icon to move the 
             * position
             */
            
            if(isYUnderArc(width/2, eventY, eventX, height, width) || TDBG){
            	if (DBG) log("touch-down within arc");
            	mIsTouchInCircle = true;
            }/*
            */
            if(TDBG){
            	
            	mIsTouchInCircle = true;
            }
            
            
            break;

        case MotionEvent.ACTION_MOVE:
            if (DBG) log("touch-move");
            
            if(isYUnderArc(width/2, eventY, eventX, height, width) || TDBG){
            	if (DBG) log("touch-move within arc");
            	setLockXY(eventX, eventY);
            	invalidate();
            	mIsTouchInCircle = true;
            }
            else{
            	
            	reset();
            	invalidate();
            	
            }
            if(TDBG){
            	setLockXY(eventX, eventY);
            	invalidate();
            	mIsTouchInCircle = true;
            }
            break;
        case MotionEvent.ACTION_UP:
            if (DBG) log("touch-up");
            reset();
            invalidate();
            break;
        case MotionEvent.ACTION_CANCEL:
            if (DBG) log("touch-cancel");
            reset();
            invalidate();
        
    }
		
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
              mPaint.setStyle(Paint.Style.STROKE);
              canvas.drawRect(0, 0, width, height , mPaint);
          }
          
          
          canvas.drawBitmap(mOuterCircul,  0, 0, mPaint);
          
          if(mIsTouchInCircle)	
        	  
        	  canvas.drawBitmap(mLockIcon,  mLockX-(mLockIcon.getWidth()/2), mLockY - mLockIcon.getHeight(), mPaint);
          
          else{
        	  // Fallback case where the lock is always drawn in the center on the bottom of the view
        	   canvas.drawBitmap(mLockIcon,  (width/2)-(mLockIcon.getWidth()/2), height-mLockIcon.getHeight(), mPaint);
        	  
          }
		
		
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
    
    
    
    // ***********
    /**
     * Assuming bitmap is a bounding box around a piece of an arc drawn by two concentric circles
     * (as the background drawable for the portrait circular widget is), and given an x and y coordinate along the
     * drawable, return false if the the radius for the touch points is greater than the touch circle.
     * This is accomplished by shifting the touch onto a cartesian plane with the bootom of the
     * view defined as Y = 0 and the middle of the view defined as X = 0. With the knowledge of
     * the radius of the circle and the touch points a radius can be  
     * 
     * y coordinate of a point on the arc that is between the two concentric
     * circles.  The resulting y combined with the incoming x is a point along the circle in
     * between the two concentric circles.
     * 
     * 
     * 
     *
     * 
     * @param innerRadius The radius of the circle that intersects the drawable at the bottom two
     *        corders of the drawable (top two corners in terms of drawing coordinates).
     * @param y The distance along the y axis of the touch point. 
     * @param x The distance along the x axis of the touchpoint.   
     * @param height The height of the view
     * @param width The width of the view
     * @return False if the radius of the touch point is greater than the radius of the touch circle 
     */
    private boolean isYUnderArc(int innerRadius, int y, int x, int height, int width) {

    	int CartesianX = width/2; // The x point directly in the middle of the view
    	int CartesianY = height;  // The Y point, at the bottom of the view
    	
    	
    	
    	int YRadiusUnderArc = innerRadius;
    	
    	int CartesianShiftTouchX;
    	int CartesianShiftTouchY; 
    	
    	if(x > CartesianX)
    		CartesianShiftTouchX = CartesianX - x;
    	else
    		 CartesianShiftTouchX = x - CartesianX;
    	
    	if(y > CartesianY)
    		CartesianShiftTouchY = CartesianX - y;
    	else
    		 CartesianShiftTouchY = y - CartesianY;
    	
    	
    	
    	int YTouchRadius = (int) Math.sqrt((CartesianShiftTouchX*CartesianShiftTouchX) + (CartesianShiftTouchY*CartesianShiftTouchY));
    	
    	if(YTouchRadius > YRadiusUnderArc)
    		return false;
    	else 
    		return true;
    	
    	
    	
  
 
    }
    private int getYWithinCircle(int backgroundWidth, int innerRadius, int outerRadius, int x) {

        // the hypotenuse
        final int halfWidth = (outerRadius - innerRadius) / 2;
        final int middleRadius = innerRadius + halfWidth;

        // the bottom leg of the triangle
        final int triangleBottom = (backgroundWidth / 2) - x;

        // "Our offense is like the pythagorean theorem: There is no answer!" - Shaquille O'Neal
        final int triangleY =
                (int) Math.sqrt(middleRadius * middleRadius - triangleBottom * triangleBottom);

        // convert to drawing coordinates:
        // middleRadius - triangleY =
        //   the vertical distance from the outer edge of the circle to the desired point
        // from there we add the distance from the top of the drawable to the middle circle
        return middleRadius - triangleY + halfWidth;
    }
    
    
    
    // Lock positiojn function
    
    private void setLockXY(int eventX, int eventY){
    	mLockX = eventX;
    	mLockY = eventY;
    	
    	
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
    private void reset(){
    	
    	mIsTouchInCircle = false;
    	
    }
    
    
    
}
