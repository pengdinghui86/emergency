package com.dssm.esc.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.model.entity.emergency.EmergencyMenuEntity;
import com.dssm.esc.view.widget.RedPointView;

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
        if(mDatas.get(position).getIcon() > 0) {
            viewHolder.imageView.setImageResource(mDatas.get(position).getIcon());
            //数量提醒
            RedPointView redPointView = remind(viewHolder.redPoint, mDatas.get(position).getCount() + "");
            if(mDatas.get(position).getCount() > 99) {
                redPointView.setText("99+");
                redPointView.show();
            }
            else if (mDatas.get(position).getCount() > 0) {
                redPointView.setText(mDatas.get(position).getCount() + "");
                redPointView.show();
            }
            else {
                redPointView.hide();
            }
        } else
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
        private TextView redPoint;
        public ViewHolder(View arg0)
        {
            super(arg0);
            relativeLayout = (RelativeLayout) arg0.findViewById(R.id.item_list_view_emergency_menu_rl);
            name = (TextView) arg0.findViewById(R.id.item_list_view_emergency_menu_tv);
            redPoint = (TextView) arg0.findViewById(R.id.item_list_view_emergency_menu_tv_red_point);
            imageView = (ImageView) arg0.findViewById(R.id.item_list_view_emergency_menu_iv);

        }

    }

    public void updateRemindCount(String id, int count) {
        for(EmergencyMenuEntity entity : mDatas)
        {
            if(entity.getId().equals(id))
            {
                entity.setCount(count);
                break;
            }
        }
        notifyDataSetChanged();
    }

    public RedPointView remind(View view, String count) {
        RedPointView redPointView = new RedPointView(mContext, view);
        FrameLayout.LayoutParams lP = (FrameLayout.LayoutParams) new FrameLayout.LayoutParams(
                20, 20);
        redPointView.setLayoutParams(lP);
        redPointView.setText(count);// 需要显示的提醒类容
        redPointView.setPosition(0, Gravity.RIGHT);// 显示的位置.右上角,BadgeView.POSITION_BOTTOM_LEFT,下左，还有其他几个属性
        redPointView.setTextSize(12); // 文本大小
        // redPointView.show();// 只有显示
        redPointView.hide();// 先默认隐藏
        return redPointView;
    }
}
