package us.saeratom.materialbuttonlibrary;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Author: SharerMax
 * Time  : 2015/6/10
 * E-Mail: mdcw1103@gmail.com
 */
public class FlatButton extends TextView {
    private Paint mPaint;
    private boolean mAnimation;
    private float mStartX;
    private float mStartY;
    private float mStartR;
    private double mMaxRadius;
    private Paint mAnimationPaint;
    private float mAnimationSpeed = 3;
    public FlatButton(Context context) {
        super(context);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public FlatButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        init(context, attrs);
    }

    public FlatButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FlatButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlatButton);
        mAnimationSpeed = typedArray.getFloat(R.styleable.FlatButton_animationSpeed, 3);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (mAnimation) {
            Paint paint = initAnimationPaint();
            canvas.drawCircle(mStartX, mStartY, mStartR, paint);
            mStartR += mAnimationSpeed;
            if (mStartR > mMaxRadius) {
                mAnimation = false;
            }
            invalidate();
        }
    }

    private Paint initAnimationPaint() {
        if (null == mAnimationPaint) {
            mAnimationPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mAnimationPaint.setColor(makeRippleColor());
        }
        return mAnimationPaint;
    }
    private int makeRippleColor() {
        return Color.parseColor("#88DDDDDD");
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        mAnimation = true;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartX = event.getX();
                mStartY = event.getY();
                mStartR = 0;
                computeMaxRadius(mStartX, mStartY);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }

        return super.onTouchEvent(event);
    }

    private void computeMaxRadius(float x, float y) {
        double x1 = x > (getWidth() / 2) ? x : getWidth() - x;
        double y1 = y > (getWidth() / 2) ? y : getHeight() - y;
        mMaxRadius = Math.sqrt(x1 * x1 + y1 * y1);
    }
}
