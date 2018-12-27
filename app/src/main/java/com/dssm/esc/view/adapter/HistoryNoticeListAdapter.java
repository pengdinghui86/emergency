package com.dssm.esc.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.dssm.esc.R;
import com.dssm.esc.model.entity.message.HistoryNoticeEntity;
import com.dssm.esc.util.Utils;
import com.dssm.esc.view.widget.MergeHeadViewGroup;

import java.util.ArrayList;
import java.util.List;

public class HistoryNoticeListAdapter extends BaseAdapter {
	private List<HistoryNoticeEntity> arraylist;
	private Context context;
	public HistoryNoticeListAdapter(Context context, List<HistoryNoticeEntity> list) {
		this.context = context;
		this.arraylist = list;
	}

	@Override
	public int getCount() {
		return arraylist.size();
	}

	@Override
	public HistoryNoticeEntity getItem(int position) {
		return arraylist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HistoryNoticeEntity entity = getItem(position);
		ViewHolder mhHolder = null;
        MergeHeadViewGroup.NineGridImageViewAdapter mAdapter = new MergeHeadViewGroup.NineGridImageViewAdapter() {
            @Override
            protected void onDisplayImage(Context context, ImageView imageView, Integer s) {
                if (s <= 0) {
                    Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.system_notice);
                    imageView.setImageBitmap(bmp);
                    return;
                }
                Glide.with(context).load(s).into(imageView);
            }

            @Override
            protected ImageView generateImageView(Context context) {
                return super.generateImageView(context);
            }
        };

        if (convertView == null) {
			mhHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_listview_notice, null);

			mhHolder.message_listview_iv_type = convertView.findViewById(R.id.message_listview_iv_type);
			mhHolder.message_listview_tv_name = convertView.findViewById(R.id.message_listview_tv_name);
			mhHolder.message_listview_tv_time = convertView
					.findViewById(R.id.message_listview_tv_time);
			mhHolder.message_listview_tv_content = convertView
					.findViewById(R.id.message_listview_tv_content);
			convertView.setTag(mhHolder);
		} else {
			mhHolder = (ViewHolder) convertView.getTag();
		}
        mhHolder.message_listview_iv_type.setAdapter(mAdapter);
        List<Integer> mPostList = new ArrayList<>();
        String[] typeList = entity.getSendType().split(",");
        String title = "";
        for(String type : typeList)
		{
			if("0".equals(type))
			{
				mPostList.add(R.drawable.system_notice);
				title += ",系统";
			}
			else if("1".equals(type))
			{
				mPostList.add(R.drawable.email_notice);
				title += ",邮件";
			}
			else if("2".equals(type))
			{
				mPostList.add(R.drawable.message_notice);
				title += ",短信";
			}
			else if("3".equals(type))
			{
				mPostList.add(R.drawable.app_notice);
				title += ",APP";
			}
		}
		if(title.length() > 0)
			title = title.substring(1, title.length());
		mhHolder.message_listview_tv_name.setText(title);
        mhHolder.message_listview_iv_type.setImagesData(mPostList);
		String creatTime = entity.getCreateTime();
		long time = Utils.getStringToDate(creatTime, "yyyy-MM-dd HH:mm:ss");
		mhHolder.message_listview_tv_time.setText(Utils.getNewChatTime(time));
		mhHolder.message_listview_tv_content.setText(entity.getMessage());
		return convertView;
	}

	class ViewHolder {

		private MergeHeadViewGroup message_listview_iv_type;
		private TextView message_listview_tv_name;
		private TextView message_listview_tv_time;
		private TextView message_listview_tv_content;

	}

}
