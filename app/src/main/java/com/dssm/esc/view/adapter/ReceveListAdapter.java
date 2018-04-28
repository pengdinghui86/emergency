package com.dssm.esc.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.model.entity.emergency.RecieveListEntity;

import java.util.ArrayList;


/**
 * 新增联系人的适配器
 * 
 * @author zsj
 * 
 */
public class ReceveListAdapter extends BaseAdapter {

	public ArrayList<RecieveListEntity> arraylist;
	private Context context;

	public ReceveListAdapter(Context context, ArrayList<RecieveListEntity> list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.arraylist = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arraylist.size();
	}

	@Override
	public RecieveListEntity getItem(int position) {
		// TODO Auto-generated method stub
		return arraylist.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		RecieveListEntity entity = getItem(position);
		HolderRecieve mhHolder = null;
		if (convertView == null) {
			mhHolder = new HolderRecieve();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_listview_recievelist, null);

			mhHolder.name = (TextView) convertView.findViewById(R.id.name);
			mhHolder.phonenumber = (TextView) convertView
					.findViewById(R.id.phonenumber);
			mhHolder.email = (TextView) convertView.findViewById(R.id.email);
			mhHolder.delete = (ImageView) convertView.findViewById(R.id.delete);

			convertView.setTag(mhHolder);
		} else {
			mhHolder = (HolderRecieve) convertView.getTag();
		}
		mhHolder.name.setText(entity.getReceiver());
		mhHolder.phonenumber.setText(entity.getReceiverPhone());
		mhHolder.email.setText(entity.getReceiverEmail());
		mhHolder.delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				arraylist.remove(position);
				ReceveListAdapter.this.notifyDataSetChanged();
			}
		});
		return convertView;
	}

	  class HolderRecieve {
		private	TextView name;
		private	TextView phonenumber;
		private	TextView email;
		private  ImageView delete;

	}

}
