package com.dssm.esc.view.widget;


import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dssm.esc.R;

/**
 * title
 * @Description TODO
 * @author Zsj
 * @date 2015-9-7
 * @Copyright: Copyright: Copyright (c) 2015 Shenzhen DENGINE Technology Co., Ltd. Inc. All rights reserved.
 */
public class Title_Layout extends LinearLayout
{
	public Title_Layout(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		LayoutInflater.from(context).inflate(R.layout.actionbar_title, this);
		ImageView titleback = (ImageView) findViewById(R.id.iv_actionbar_back);
		titleback.setImageResource(R.drawable.back);
		titleback.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				((Activity) getContext()).finish();
			}
		});
	}

}
