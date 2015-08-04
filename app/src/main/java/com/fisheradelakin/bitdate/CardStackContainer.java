package com.fisheradelakin.bitdate;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

/**
 * Created by Fisher on 8/3/15.
 */
public class CardStackContainer extends RelativeLayout implements View.OnTouchListener {

    private static final String TAG = "CardStackContainer";

    private float mLastTouchX;
    private float mLastTouchY;
    private float mPositionX;
    private float mPositionY;
    private float mOriginX;
    private float mOriginY;

    private CardAdapter mCardAdapter;
    private GestureDetector mGestureDetector;

    public CardStackContainer(Context context) {
        this(context, null, 0);
    }

    public CardStackContainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardStackContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mGestureDetector = new GestureDetector(context, new FlingListener());
    }

    public void setCardAdapter(CardAdapter cardAdapter) {
        mCardAdapter = cardAdapter;

        if(mCardAdapter.getCount() > 0) {
            CardView cardView = mCardAdapter.getView(0, null, this);
            cardView.setOnTouchListener(this);
            addView(cardView);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (mGestureDetector.onTouchEvent(event)) {
            if(mPositionX < mOriginX) { // check current position vs original position
                v.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_left));
            } else {
                v.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_right));
            }
            removeView(v);
            return true;
        }

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mLastTouchX = event.getX();
                mLastTouchY = event.getY();

                mPositionX = v.getX();
                mPositionY = v.getY();

                mOriginX = v.getX();
                mOriginY = v.getY();

                break;
            case MotionEvent.ACTION_UP:
                reset(v);
                break;
            case MotionEvent.ACTION_MOVE:
                float xMove = event.getX();
                float yMove = event.getY();

                float changeX = xMove - mLastTouchX;
                float changeY = yMove - mLastTouchY;

                mPositionX += changeX;
                mPositionY += changeY;

                v.setX(mPositionX);
                v.setY(mPositionY);

                break;
        }
        return true;
    }

    private void reset(View v) {
        mPositionX = mOriginX;
        mPositionY = mOriginY;

        v.animate()
                .setDuration(200)
                .setInterpolator(new AccelerateInterpolator())
                .x(mOriginX)
                .y(mOriginY);
    }

    private class FlingListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d(TAG, "FLING!!");
            return true;
        }
    }
}
