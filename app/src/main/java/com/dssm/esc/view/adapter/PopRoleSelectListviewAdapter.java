package com.dssm.esc.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.model.entity.user.UserLoginObjEntity;

import java.util.List;

public class PopRoleSelectListviewAdapter extends BaseAdapter {
    private Context context;
    private List<UserLoginObjEntity> list;

    public PopRoleSelectListviewAdapter(Context context, List<UserLoginObjEntity> list){
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_role_name, null);
            vh.textview = (TextView) convertView.findViewById(R.id.item_role_name_tv);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.textview.setText(list.get(position).getRoleName() + "");
        return convertView;
    }

    static class ViewHolder {
        TextView textview;
    }
}
