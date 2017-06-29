package com.ljstudio.android.loveday.views.fallingview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.ljstudio.android.loveday.R;


public class FallingView extends RelativeLayout {
    private static final String TAG = FallingView.class.getName();
    private static final int DEFAULT_FLAKES_DENSITY = 80;
    private static final int DEFAULT_DELAY = 10;
    private int mFlakesDensity = DEFAULT_FLAKES_DENSITY;
    private int mDelay = DEFAULT_DELAY;
    private static final int DEFAULT_SCALE = 3;
    private Flake[] mFlakes;
    private Bitmap mFlakeBitmap;
    private Paint mPaint;
    private int mImgId;
    private int mScale;
    private int mWidth;
    private int mHeight;
    private int mRawWidth;

//    mFallingView.setImageResource(R.drawable.img1);//设置碎片的图片,默认的图片是雪花
//    mFallingView.setDensity(progress);//设置密度，数值越大，碎片越密集,默认值是80
//    mFallingView.setScale(progress);//设置碎片的大小，数值越大，碎片越小，默认值是3
//    mFallingView.setDelay(progress);//设置碎片飘落的速度，数值越大，飘落的越慢，默认值是10

    public FallingView(Context context) {
        this(context, null);
    }

    public FallingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FallingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }



    private void init(Context context, AttributeSet attrs) {
        setBackgroundColor(Color.TRANSPARENT);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FallingView);
            mImgId = a.getResourceId(R.styleable.FallingView_flakeSrc, R.mipmap.ic_winter_snow);
            mScale = a.getInt(R.styleable.FallingView_flakeScale, DEFAULT_SCALE);
            mFlakesDensity = a.getInt(R.styleable.FallingView_flakeDensity, DEFAULT_FLAKES_DENSITY);
            mDelay = a.getInt(R.styleable.FallingView_fallingDelay, DEFAULT_DELAY);

        }
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw || h != oldh) {
            mWidth = w;
            mHeight = h;
            mRawWidth = initScale(mScale);
            initDenstity(w, h, mRawWidth);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        for (Flake flake : mFlakes) {
            flake.draw(canvas, mFlakeBitmap);
        }
        getHandler().postDelayed(mRunnable, mDelay);
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    private void initDenstity(int w, int h, int rawWidth) {
        mFlakes = new Flake[mFlakesDensity];
        for (int i = 0; i < mFlakesDensity; i++) {
            mFlakes[i] = Flake.create(w, h, mPaint, rawWidth / mScale);
        }
    }

    private int initScale(int scale) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), mImgId, options);
        int rawWidth = options.outWidth;
        mRawWidth = rawWidth;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        mFlakeBitmap = BitmapFactory.decodeResource(getResources(), mImgId, options);
        return rawWidth;
    }

    public void setImageResource(int imgId) {
        this.mImgId = imgId;
        initScale(mScale);
    }

    public void setScale(int scale) {
        initScale(scale);
    }

    public void setDensity(int density) {
        this.mFlakesDensity = density;
        initDenstity(mWidth, mHeight, mRawWidth);
    }

    public void setDelay(int delay) {
        this.mDelay = delay;
    }
}
