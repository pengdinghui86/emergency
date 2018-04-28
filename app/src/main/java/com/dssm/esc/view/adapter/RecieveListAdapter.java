package com.dssm.esc.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.model.entity.emergency.RecieveListEntity;

import java.util.ArrayList;


public class RecieveListAdapter extends BaseAdapter {

	private ArrayList<RecieveListEntity> arraylist;
	private Context context;


	public RecieveListAdapter(Context context, ArrayList<RecieveListEntity> list) {
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		 final RecieveListEntity entity =arraylist.get(position);
		ViewHolderRecieve mhHolder = null;
		if (convertView == null) {
			mhHolder = new ViewHolderRecieve();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_listview_contact, null);

			mhHolder.name = (TextView) convertView.findViewById(R.id.name1);
			mhHolder.phonenumber = (TextView) convertView
					.findViewById(R.id.phonenumber1);
			mhHolder.email = (TextView) convertView.findViewById(R.id.email1);
			mhHolder.checkBox = (CheckBox) convertView
					.findViewById(R.id.reciev_item_checkbox1);

			convertView.setTag(mhHolder);
		} else {
			mhHolder = (ViewHolderRecieve) convertView.getTag();
		}
		mhHolder.name.setText(entity.getReceiver());
		mhHolder.phonenumber.setText(entity.getReceiverPhone());
		mhHolder.email.setText(entity.getReceiverEmail());
		mhHolder.checkBox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						// TODO Auto-generated method stub
						if (arg1) {
							entity.setIschecked(true);
						} else {
							entity.setIschecked(false);
						}
					}
				});
		mhHolder.checkBox.setChecked(entity.getIschecked());
		return convertView;
	}

	  class ViewHolderRecieve {
	private	TextView name;
	private	TextView phonenumber;
	private	TextView email;
	private CheckBox checkBox;

	}

	// public static HashMap<Integer, Boolean> getIsSelected() {
	// return isSelected;
	// }
}
