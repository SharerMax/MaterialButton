package us.saeratom.materialbuttonlibrary;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
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
    private static final float DEFAULT_RIPPLE_SPEED = 3;
    private boolean mAnimation;
    private float mStartX;
    private float mStartY;
    private float mStartR;
    private double mMaxRadius;
    private Paint mAnimationPaint;
    private float mAnimationSpeed = 3;
    private OnClickListener mOnClickListener;
    private boolean mAfterRippleClicked;
    private int mRippleColor;
    public FlatButton(Context context) {
        super(context);
    }

    public FlatButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FlatButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FlatButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FlatButton);
        mAnimationSpeed = typedArray.getFloat(R.styleable.FlatButton_rippleSpeed, DEFAULT_RIPPLE_SPEED);
        mAfterRippleClicked = typedArray.getBoolean(R.styleable.FlatButton_afterRippleClicked, true);
        mRippleColor = typedArray.getColor(R.styleable.FlatButton_rippleColor, getDefaultRippleColor(context));
        setBackground(new ColorDrawable(Color.TRANSPARENT));
        typedArray.recycle();
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
                setPressed(false);
                if (null != mOnClickListener && mAfterRippleClicked) {
                    mOnClickListener.onClick(this);
                }
            }
            invalidate();
        }
    }

    private Paint initAnimationPaint() {
        if (null == mAnimationPaint) {
            mAnimationPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mAnimationPaint.setColor(mRippleColor);
        }
        return mAnimationPaint;
    }
    private int getDefaultRippleColor(Context context) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                new int[] {android.R.attr.colorBackground});
        int color = typedArray.getColor(0, Color.parseColor("#88DDDDDD"));
        if ((color & 0xFF000000) == 0xFF000000) {
            color &= 0x00FFFFFF;
            color |= 0x88000000;
        }
        return color;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        mAnimation = true;
        if (MotionEvent.ACTION_DOWN == event.getAction()) {

                setPressed(true);
                mStartX = event.getX();
                mStartY = event.getY();
                mStartR = 0;
                computeMaxRadius(mStartX, mStartY);
                invalidate();
        }

        return super.onTouchEvent(event);
    }

    private void computeMaxRadius(float x, float y) {
        double x1 = x > (getWidth() / 2) ? x : getWidth() - x;
        double y1 = y > (getWidth() / 2) ? y : getHeight() - y;
        mMaxRadius = Math.sqrt(x1 * x1 + y1 * y1);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        mOnClickListener = l;
    }

    public void setAfterRippleClicked(boolean enable) {
        mAfterRippleClicked = enable;
    }
}
