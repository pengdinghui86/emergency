package com.dssm.esc.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.dssm.esc.R;

public class RingChartView extends View {

    // 圆画笔
    private Paint mCirclePaint;
    // 圆环画笔
    private Paint mRingPaint;
    // 文本画笔
    private Paint mTextPaint;
    // 填充颜色
    private int mFillColor;
    // 文本颜色
    private int mTextColor;
    // 默认弧颜色
    private int mDefaultRingColor;
    // 里面弧颜色
    private int mInnerRingColor;
    // 外面弧颜色
    private int mOutRingColor;
    // 里面的弧半径
    private float mInnerRingRadius;
    // 最外弧半径
    private float mOutRingRadius;
    // 里面的弧的宽度
    private float mInnerRingWidth;
    // 外面的弧的宽度
    private float mOutRingWidth;
    // 文本的中心x轴位置
    private int mXCenter;
    // 文本的中心y轴位置
    private int mYCenter;
    // 描述文本的宽度
    private float mTxtWidth;
    // 描述文本
    private String mTxt = "";
    // 文本的高度
    private float mTxtHeight;
    // 文本的大小
    private float mTxtSize;
    // 总百分比
    private int mTotalProgress = 100;
    // 内环百分比
    private double mInnerProgress;
    // 外环百分比
    private double mOutProgress;
    public RingChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initVariable();
    }


    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.RingChartViewStyle, 0, 0);
        mInnerRingRadius = typeArray.getDimension(R.styleable.RingChartViewStyle_innerRingRadius, 0);
        mTxt = typeArray.getString(R.styleable.RingChartViewStyle_description);
        mTxtSize=typeArray.getDimension(R.styleable.RingChartViewStyle_textSize, 20);
        mInnerRingWidth = typeArray.getDimension(R.styleable.RingChartViewStyle_innerRingStrokeLen, 0);
        mFillColor = typeArray.getColor(R.styleable.RingChartViewStyle_centerColor, 0xFFFFFFFF);
        mTextColor = typeArray.getColor(R.styleable.RingChartViewStyle_textColor, 0xFF000000);
        mDefaultRingColor = typeArray.getColor(R.styleable.RingChartViewStyle_defaultRingColor, 0xFFF8F8F8);
        mOutRingColor = typeArray.getColor(R.styleable.RingChartViewStyle_outRingColor, 0xFFFFFFFF);
        mInnerRingColor = typeArray.getColor(R.styleable.RingChartViewStyle_innerRingColor, 0xFFFFFFFF);
        mOutRingRadius = typeArray.getDimension(R.styleable.RingChartViewStyle_outRingRadius, 0);
        mOutRingWidth = typeArray.getDimension(R.styleable.RingChartViewStyle_outRingStrokeLen, 0);
    }


    private void initVariable() {
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(mFillColor);

        mRingPaint = new Paint();
        mRingPaint.setAntiAlias(true);
        mRingPaint.setColor(mInnerRingColor);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setStrokeWidth(mInnerRingWidth);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTxtSize);
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        mTxtHeight = (int) Math.ceil(fm.descent - fm.ascent);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mXCenter = getWidth() / 2;
        mYCenter = getHeight() / 2;
        if(mOutRingRadius - mOutRingWidth > 0 && mOutRingWidth > 0) {
            RectF oval = new RectF();
            oval.left = mXCenter - mOutRingRadius;
            oval.top = mYCenter - mOutRingRadius;
            oval.right = mXCenter + mOutRingRadius;
            oval.bottom = mYCenter + mOutRingRadius;
            mRingPaint.setColor(mDefaultRingColor);
            mRingPaint.setStrokeWidth(mOutRingWidth);
            canvas.drawArc(oval, -90, 360, false, mRingPaint);
            if (mOutProgress > 0) {
                mRingPaint.setColor(mOutRingColor);
                mRingPaint.setStrokeWidth(mOutRingWidth);
                canvas.drawArc(oval, -90, ((float) mOutProgress / mTotalProgress) * 360, false, mRingPaint);
            }
            canvas.drawCircle(mXCenter, mYCenter, mOutRingRadius - mOutRingWidth, mCirclePaint);
        }
        if(mInnerRingRadius - mInnerRingWidth > 0 && mInnerRingWidth > 0) {
            RectF oval = new RectF();
            oval.left = mXCenter - mInnerRingRadius;
            oval.top = mYCenter - mInnerRingRadius;
            oval.right = mXCenter + mInnerRingRadius;
            oval.bottom = mYCenter + mInnerRingRadius;
            mRingPaint.setColor(mDefaultRingColor);
            mRingPaint.setStrokeWidth(mInnerRingWidth);
            canvas.drawArc(oval, -90, 360, false, mRingPaint);
            if (mInnerProgress > 0) {
                mRingPaint.setColor(mInnerRingColor);
                mRingPaint.setStrokeWidth(mInnerRingWidth);
                canvas.drawArc(oval, -90, ((float) mInnerProgress / mTotalProgress) * 360, false, mRingPaint);
            }
            canvas.drawCircle(mXCenter, mYCenter, mInnerRingRadius - mInnerRingWidth, mCirclePaint);
        }
        String txt = "";
        if (mOutProgress > 0)
            txt = ((int) mOutProgress) + "%";
        else if(mInnerProgress > 0)
            txt = ((int) mInnerProgress) + "%";
        if(!mTxt.equals(""))
            txt = mTxt;
        mTxtWidth = mTextPaint.measureText(txt, 0, txt.length());
        canvas.drawText(txt, mXCenter - mTxtWidth / 2, mYCenter + mTxtHeight / 4, mTextPaint);
    }

    public void setOutProgress(double progress){
        mOutProgress = progress;
        postInvalidate();
    }

    public void setInnerProgress(double progress) {
        mInnerProgress = progress;
        postInvalidate();
    }

    public void setDescription(String description) {
        mTxt = description;
        postInvalidate();
    }
}
