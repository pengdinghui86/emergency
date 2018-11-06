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
    // 百分数画笔
    private Paint mTextPaint;
    // 文本画笔
    private Paint mTextPaint2;
    // 里面圆颜色
    private int mCircleColor;
    // 里面弧颜色
    private int mInnerRingColor;
    // 外面弧颜色
    private int mOutRingColor;
    // 空白的圆半径
    private float mRadius;
    // 里面的弧半径
    private float mRingRadius;
    // 最外弧半径
    private float mRingRadius2;
    // 圆环的宽度
    private float mStrokeWidth;
    // 文本的中心x轴位置
    private int mXCenter;
    // 文本的中心y轴位置
    private int mYCenter;
    // 百分比文本的宽度
    private float mTxtWidth;
    // 描述文本的宽度
    private float mTxtWidth2;
    // 文本的高度
    private float mTxtHeight;
    // 百分数文本的大小
    private float mTxtSize;
    // 总成绩
    private int mTotalProgress = 100;
    // 个人的正确率
    private double mInnerProgress;
    // 班级的正确率
    private double mOutProgress;
    public RingChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initVariable();
    }


    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.RingChartViewStyle, 0, 0);
        mRadius = typeArray.getDimension(R.styleable.RingChartViewStyle_radiusLen, 80);
        mTxtSize=typeArray.getDimension(R.styleable.RingChartViewStyle_textSize, 20);
        mStrokeWidth = typeArray.getDimension(R.styleable.RingChartViewStyle_strokeLen, 10);
        mCircleColor = typeArray.getColor(R.styleable.RingChartViewStyle_circleColor, 0xFFFFFFFF);
        mOutRingColor = typeArray.getColor(R.styleable.RingChartViewStyle_innerRingColor, 0xFFFFFFFF);
        mInnerRingColor = typeArray.getColor(R.styleable.RingChartViewStyle_outRingColor, 0xFFFFFFFF);
        mRingRadius = mRadius + mStrokeWidth / 2;
        mRingRadius2 = mRadius + mStrokeWidth/2*3;
    }


    private void initVariable() {
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL);


        mRingPaint = new Paint();
        mRingPaint.setAntiAlias(true);
        mRingPaint.setColor(mInnerRingColor);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setStrokeWidth(mStrokeWidth);


        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setARGB(255, 32, 207, 152);
        mTextPaint.setTextSize(mTxtSize);

        mTextPaint2 = new Paint();
        mTextPaint2.setAntiAlias(true);
        mTextPaint2.setStyle(Paint.Style.FILL);
        mTextPaint2.setARGB(255, 0, 0, 0);
        mTextPaint2.setTextSize(20);
        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        mTxtHeight = (int) Math.ceil(fm.descent - fm.ascent);


    }


    @Override
    protected void onDraw(Canvas canvas) {
        mXCenter = getWidth() / 2;
        mYCenter = getHeight() / 2;
        mCirclePaint.setColor(getResources().getColor(R.color.gray));
        canvas.drawCircle(mXCenter,mYCenter, mRadius + mStrokeWidth*2, mCirclePaint);
        RectF oval1 = new RectF();
        oval1.left = (mXCenter - mRingRadius);
        oval1.top = (mYCenter - mRingRadius);
        oval1.right = mRingRadius * 2 + (mXCenter - mRingRadius);
        oval1.bottom = mRingRadius * 2 + (mYCenter - mRingRadius);
        mRingPaint.setColor(mOutRingColor);
        canvas.drawArc(oval1, -90, ((float)mOutProgress / mTotalProgress) * 360, false, mRingPaint);

        mCirclePaint.setColor(mCircleColor);
        canvas.drawCircle(mXCenter, mYCenter, mRadius, mCirclePaint);
        if (mInnerProgress > 0 ) {
            RectF oval = new RectF();
            oval.left = (mXCenter - mRingRadius2);
            oval.top = (mYCenter - mRingRadius2);
            oval.right = mRingRadius2 * 2 + (mXCenter - mRingRadius2);
            oval.bottom = mRingRadius2 * 2 + (mYCenter - mRingRadius2);
            mRingPaint.setColor(mInnerRingColor);
            canvas.drawArc(oval, -90, ((float)mInnerProgress / mTotalProgress) * 360, false, mRingPaint); //
            // canvas.drawCircle(mXCenter, mYCenter, mRadius + mStrokeWidth / 2, mRingPaint);
            String txt = mInnerProgress + "%";
            String txt2 = "正确率";
            mTxtWidth = mTextPaint.measureText(txt, 0, txt.length());
            mTxtWidth2 = mTextPaint2.measureText(txt2, 0, txt2.length());
            canvas.drawText(txt, mXCenter - mTxtWidth / 2, mYCenter+mTxtWidth / 8, mTextPaint);
            canvas.drawText(txt2 ,mXCenter - mTxtWidth2 / 2, mYCenter + mTxtWidth / 2, mTextPaint2);
        }else if(mInnerProgress==0){
            String txt = mInnerProgress + "%";
            String txt2 = "正确率";
            mTxtWidth = mTextPaint.measureText(txt, 0, txt.length());
            mTxtWidth2 = mTextPaint2.measureText(txt2, 0, txt2.length());
            canvas.drawText(txt, mXCenter - mTxtWidth / 2, mYCenter+mTxtWidth / 8, mTextPaint);
            canvas.drawText(txt2 ,mXCenter - mTxtWidth2 / 2, mYCenter + mTxtWidth / 2, mTextPaint2);
        }
    }
    public void setOutProgress(double progress){
        mOutProgress = progress;
    }
    public void setInnerProgress(double progress) {
        mInnerProgress = progress;
        postInvalidate();
    }
}
