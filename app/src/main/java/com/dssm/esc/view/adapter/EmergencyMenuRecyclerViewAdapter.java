package com.dssm.esc.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.model.entity.emergency.EmergencyMenuEntity;

import java.util.List;

public class EmergencyMenuRecyclerViewAdapter extends RecyclerView.Adapter<EmergencyMenuRecyclerViewAdapter.ViewHolder>
{
    private Context mContext;
    private List<EmergencyMenuEntity> mDatas;
    private LayoutInflater mInflater;
    private  OnItemClickListener mOnItemClickListener;
    //定义点击事件的接口
    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
    }

    public EmergencyMenuRecyclerViewAdapter( Context mContext,List<EmergencyMenuEntity> datats)
    {
        this.mContext = mContext;
        this.mDatas = datats;
        mInflater = LayoutInflater.from(this.mContext);
    }

    public void setmOnItemClickListener(OnItemClickListener mOnItemClickListener)
    {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i)
    {
        View view = mInflater.inflate(R.layout.item_listview_emergency_menu,null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position)
    {
        if(mDatas.get(position).getIcon() > 0)
            viewHolder.imageView.setImageResource(mDatas.get(position).getIcon());
        else
            viewHolder.imageView.setImageResource(0);
        viewHolder.name.setText(mDatas.get(position).getName());
        //设置回调
        if (mOnItemClickListener != null){
            viewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(viewHolder.relativeLayout, pos);
                }
            });
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder  //我们需要inflate的item布局需要传入
    {
        private RelativeLayout relativeLayout;
        private ImageView imageView;
        private TextView name;
        public ViewHolder(View arg0)
        {
            super(arg0);
            relativeLayout = (RelativeLayout) arg0.findViewById(R.id.item_list_view_emergency_menu_rl);
            name = (TextView) arg0.findViewById(R.id.item_list_view_emergency_menu_tv);
            imageView = (ImageView) arg0.findViewById(R.id.item_list_view_emergency_menu_iv);

        }

    }
}
