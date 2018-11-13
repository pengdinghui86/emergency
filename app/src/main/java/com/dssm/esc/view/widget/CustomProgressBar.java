package com.dssm.esc.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.dssm.esc.R;

public class CustomProgressBar extends View {
	//描述文本
	private String description;
	//进度条高度
	private float progressBarHeight;
	//进度条默认背景色
	private int defaultBarColor;
	//进度条填充色
	private int progressBarColor;
	//描述文本字体大小
	private float progressBarTextSize;
	//描述文本字体颜色
	private int progressBarTextColor;
	private Paint mPaint;
	//控件宽度百分比
	private double widthPercent = 1;
	private float progress = 100f;

	public CustomProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initAttrs(context, attrs);
		initPaint();
	}

	private void initAttrs(Context context, AttributeSet attrs) {
		TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.CustomProgressBarStyle, 0, 0);
		progressBarHeight = typeArray.getDimension(R.styleable.CustomProgressBarStyle_progressBarHeight, 10);
		description = typeArray.getString(R.styleable.CustomProgressBarStyle_progressText);
		progressBarTextSize = typeArray.getDimension(R.styleable.CustomProgressBarStyle_progressBarTextSize, 20);
		defaultBarColor = typeArray.getColor(R.styleable.CustomProgressBarStyle_defaultBarColor, 0xFFF8F8F8);
		progressBarColor = typeArray.getColor(R.styleable.CustomProgressBarStyle_progressBarColor, 0xFF000000);
		progressBarTextColor = typeArray.getColor(R.styleable.CustomProgressBarStyle_progressBarTextColor, 0xFF000000);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		float mTxtWidth = mPaint.measureText(description, 0, description.length());
		float width = (float) (getWidth() * widthPercent);
		float x = (width - mTxtWidth) / 2 - 3;
		float y = getHeight() / 2;
		mPaint.setColor(progressBarTextColor);
		mPaint.setTextSize(progressBarTextSize);
		canvas.drawText(description, x * 2 + 3, y + progressBarHeight/4, mPaint);

		mPaint.setColor(defaultBarColor);
		canvas.drawRect(0, y - progressBarHeight/2, x * 2, y + progressBarHeight/2, mPaint);

		mPaint.setColor(progressBarColor);
		canvas.drawRect(0, y - progressBarHeight/2, x * 2 * (progress / 100), y + progressBarHeight/2, mPaint);

	}

	private void initPaint() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(20);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(Color.WHITE);
	}

	public void setData(int progress, double widthPercent, String description) {
		this.progress = progress;
		this.widthPercent = widthPercent;
		this.description = description;
		postInvalidate();
	}
}