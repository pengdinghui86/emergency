package com.dssm.esc.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dssm.esc.R;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.event.PlanStarListEntity;
import com.dssm.esc.view.widget.LeftSlideView;

import java.util.List;

public class LeftSlideAdapter extends RecyclerView.Adapter<LeftSlideAdapter.MyViewHolder> implements LeftSlideView.IonSlidingButtonListener {

    private Context mContext;
    private List<PlanStarListEntity> arraylist;
    /** 0，已授权1,授权决策；2,人员签到 ；3,人员指派;4,协同通告; 5,指挥与展示,6,事件流程列表7,驳回事件列表 */
    private String tags;

    private IonSlidingViewClickListener mIDeleteBtnClickListener;

    private IonSlidingViewClickListener mISetBtnClickListener;

    private LeftSlideView mMenu = null;


    public LeftSlideAdapter(Context context,
                            List<PlanStarListEntity> list, String tags) {

        mContext = context;
        mIDeleteBtnClickListener = (IonSlidingViewClickListener) context;
        mISetBtnClickListener = (IonSlidingViewClickListener) context;
        this.arraylist = list;
        this.tags = tags;
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final PlanStarListEntity entity = arraylist.get(position);
        if (tags.equals("1")) {// 待启动事件列表
            holder.tvEventName.setText(entity.getEveName());
            holder.tvState.setTextColor(Color.RED);
            holder.tvTradeType.setText(entity.getTradeType());
            String planResType = entity.getEveType();
            if (planResType.equals("1")) {
                holder.ivEventType.setImageResource(R.drawable.emergency_type);
            } else if (planResType.equals("2")) {
                holder.ivEventType.setImageResource(R.drawable.drill_type);
            }
            holder.tvEventLevel.setText(entity.getEveLevel());
            // 0:，初始状态；1，待预案评估；2，执行中；3，结束；5，启动中；-1，驳回评估
            if (!entity.getState().equals("null")
                    && !entity.getState().equals("")) {
                String status = "";
                switch (Integer.parseInt(entity.getState())) {
                    case 0:
                        status = "初始状态";
                        holder.tvState.setTextColor(Color.BLUE);
                        break;
                    case 1:
                        status = "待预案评估";
                        break;
                    case 2:
                        status = "执行中";
                        break;
                    case 3:
                        status = "结束";
                        break;
                    case 5:
                        status = "启动中";
                        break;
                    case -1:
                        status = "驳回评估";
                        break;

                }
                holder.tvState.setTextColor(Color.RED);
                holder.tvState.setText(status);
            }
        } else if (tags.equals("2")) {// 已启动事件列表
            holder.tvEventName.setText(entity.getPlanName());
            holder.tvState.setTextColor(Color.RED);
            String planResType = entity.getPlanResType();
            if (planResType.equals("1")) {
                holder.ivEventType.setImageResource(R.drawable.emergency_type);
            } else if (planResType.equals("2")) {
                holder.ivEventType.setImageResource(R.drawable.drill_type);
            }
            holder.tvEventLevel.setVisibility(View.GONE);
            holder.tvTradeType.setText(entity.getPlanResName());
            // （0.待启动 1.已启动 2.已授权 3.流程启动 4.完成 5.强行中止）
            if (!entity.getState().equals("null")
                    && !entity.getState().equals("")) {
                String status = "";
                switch (Integer.parseInt(entity.getState())) {
                    case 0:
                        status = "待启动";
                        break;
                    case 1:
                        status = "已启动";
                        break;
                    case 2:
                        status = "已授权";
                        break;
                    case 3:
                        status = "执行中";

                        break;
                    case 4:
                        status = "完成";
                        break;
                    case 5:
                        status = "强行中止";
                        break;
                    /**
                     * 添加暂停状态
                     * 2017.10.16
                     */
                    case 6:
                        status = "暂停";
                        break;
                }
                holder.tvState.setText(status);
            }
        }
        //设置内容布局的宽为屏幕宽度
        holder.layout_content.getLayoutParams().width = Utils.getScreenWidth(mContext);

        //item正文点击事件
        holder.layout_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //判断是否有删除菜单打开
                if (menuIsOpen()) {
                    closeMenu();//关闭菜单
                } else {
                    int n = holder.getLayoutPosition();
                    mIDeleteBtnClickListener.onItemClick(v, n);
                }

            }
        });


        //左滑设置点击事件
        holder.btn_Set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = holder.getLayoutPosition();
                mISetBtnClickListener.onSetBtnCilck(view, n);
            }
        });


        //左滑删除点击事件
        holder.btn_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = holder.getLayoutPosition();
                mIDeleteBtnClickListener.onDeleteBtnCilck(view, n);
            }
        });

    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {

        //获取自定义View的布局（加载item布局）
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_event_hide_drag, arg0, false);
        MyViewHolder holder = new MyViewHolder(view);


        return holder;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView btn_Set;
        public TextView btn_Delete;
        public ViewGroup layout_content;
        private TextView tvEventName;
        private TextView tvEventLevel;
        private TextView tvTradeType;
        private TextView tvState;
        private ImageView ivEventType;

        public MyViewHolder(View itemView) {
            super(itemView);

            btn_Set = (TextView) itemView.findViewById(R.id.tv_set);
            btn_Delete = (TextView) itemView.findViewById(R.id.tv_delete);
            layout_content = (ViewGroup) itemView.findViewById(R.id.layout_content);
            tvEventName = (TextView) itemView.findViewById(R.id.event_listview_tv_name);
            tvState = (TextView) itemView
                    .findViewById(R.id.event_listview_tv_event_status);
            tvTradeType = (TextView) itemView
                    .findViewById(R.id.event_listview_tv_sub_name);
            tvEventLevel = (TextView) itemView
                    .findViewById(R.id.event_listview_tv_event_level);
            ivEventType = (ImageView) itemView
                    .findViewById(R.id.event_listview_iv_type);
            ((LeftSlideView) itemView).setSlidingButtonListener(LeftSlideAdapter.this);
        }
    }


    /**
     * 删除item
     * @param position
     */
    public void removeData(int position) {
        arraylist.remove(position);
        notifyItemRemoved(position);
    }


    /**
     * 删除菜单打开信息接收
     */
    @Override
    public void onMenuIsOpen(View view) {
        mMenu = (LeftSlideView) view;
    }


    /**
     * 滑动或者点击了Item监听
     *
     * @param leftSlideView
     */
    @Override
    public void onDownOrMove(LeftSlideView leftSlideView) {
        if (menuIsOpen()) {
            if (mMenu != leftSlideView) {
                closeMenu();
            }
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        mMenu.closeMenu();
        mMenu = null;

    }

    /**
     * 判断菜单是否打开
     *
     * @return
     */
    public Boolean menuIsOpen() {
        if (mMenu != null) {
            return true;
        }
        return false;
    }


    /**
     * 注册接口的方法：点击事件。在Mactivity.java实现这些方法。
     */
    public interface IonSlidingViewClickListener {
        void onItemClick(View view, int position);//点击item正文

        void onDeleteBtnCilck(View view, int position);//点击“删除”

        void onSetBtnCilck(View view, int position);//点击“设置”
    }

}
