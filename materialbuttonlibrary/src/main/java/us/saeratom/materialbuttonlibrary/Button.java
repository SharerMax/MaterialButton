package us.saeratom.materialbuttonlibrary;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Author: SharerMax
 * Time  : 2015/6/10
 * E-Mail: mdcw1103@gmail.com
 */
public class Button extends View {
    private Paint mPaint;
    private boolean mAnimation;
    private float mStartX;
    private float mStartY;
    private float mStartR;
    private double mMaxRadius;
    private Paint mAnimationPaint;
    public Button(Context context) {
        super(context);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public Button(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public Button(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Button(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.BLUE);
        RectF rect = new RectF(0, 0, getWidth(), getHeight());
        canvas.drawRoundRect(rect, 8, 8, mPaint);
        String text = "Test";
        mPaint.setTextSize(200);
        mPaint.setColor(Color.YELLOW);
        canvas.drawText(text, 0, getHeight(), mPaint);
        if (mAnimation) {
//            canvas.save();
            Paint paint = initAnimationPaint();
            canvas.drawCircle(mStartX, mStartY, mStartR, paint);
            mStartR += 16;
            if (mStartR > mMaxRadius) {
                mAnimation = false;
            }
//            canvas.restore();
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
