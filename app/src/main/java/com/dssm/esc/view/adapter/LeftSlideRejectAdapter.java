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
import com.dssm.esc.model.entity.emergency.BoHuiListEntity;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.event.PlanStarListEntity;
import com.dssm.esc.view.widget.LeftSlideView;

import java.util.List;

public class LeftSlideRejectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements LeftSlideView.IonSlidingButtonListener {

    private Context mContext;
    private List<BoHuiListEntity> arraylist;
    /** 0，已授权1,授权决策；2,人员签到 ；3,人员指派;4,协同通告; 5,指挥与展示,6,事件流程列表7,驳回事件列表 */
    private String tags;

    private IonSlidingViewClickListener mIDeleteBtnClickListener;

    private IonSlidingViewClickListener mISetBtnClickListener;

    private LeftSlideView mMenu = null;


    public LeftSlideRejectAdapter(Context context,
                                  List<BoHuiListEntity> list, String tags) {

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
    public int getItemViewType(int position) {
        return arraylist.get(position).getDataType();
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                EventViewHolder eventViewHolder = (EventViewHolder) holder;
                final BoHuiListEntity entity = arraylist.get(position);
                if (tags.equals("1")) {// 待启动事件列表
                    eventViewHolder.tvEventName.setText(entity.getEveName());
                    eventViewHolder.tvState.setTextColor(Color.RED);
                    eventViewHolder.tvTradeType.setText(entity.getTradeType());
                    String planResType = entity.getEveType();
                    if (planResType.equals("1")) {
                        eventViewHolder.ivEventType.setImageResource(R.drawable.emergency_type);
                    } else if (planResType.equals("2")) {
                        eventViewHolder.ivEventType.setImageResource(R.drawable.drill_type);
                    }
                    eventViewHolder.tvEventLevel.setText(entity.getEveLevel());
                    // 0:，初始状态；1，待预案评估；2，执行中；3，结束；5，启动中；-1，驳回评估
                    if (!entity.getState().equals("null")
                            && !entity.getState().equals("")) {
                        String status = "";
                        switch (Integer.parseInt(entity.getState())) {
                            case 0:
                                status = "初始状态";
                                eventViewHolder.tvState.setTextColor(Color.BLUE);
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
                        eventViewHolder.tvState.setTextColor(Color.RED);
                        eventViewHolder.tvState.setText(status);
                    }
                } else if (tags.equals("2")) {// 已启动事件列表
                    eventViewHolder.tvEventName.setText(entity.getEveName());
                    eventViewHolder.tvState.setTextColor(Color.RED);
                    String planResType = entity.getPlanResType();
                    if (planResType.equals("1")) {
                        eventViewHolder.ivEventType.setImageResource(R.drawable.emergency_type);
                    } else if (planResType.equals("2")) {
                        eventViewHolder.ivEventType.setImageResource(R.drawable.drill_type);
                    }
                    eventViewHolder.tvEventLevel.setVisibility(View.GONE);
                    eventViewHolder.tvTradeType.setText(entity.getPlanResName());
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
                        eventViewHolder.tvState.setText(status);
                    }
                }
                //设置内容布局的宽为屏幕宽度
                eventViewHolder.layout_content.getLayoutParams().width = Utils.getScreenWidth(mContext);
                //使左滑出现的菜单与内容一样高
                int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
                int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
                eventViewHolder.layout_content.measure(w, h);
                int height = eventViewHolder.layout_content.getMeasuredHeight();
                eventViewHolder.btn_Function.getLayoutParams().height = height;
                //item正文点击事件
                eventViewHolder.layout_content.setOnClickListener(new View.OnClickListener() {
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


                //左滑菜单点击事件
                eventViewHolder.btn_Function.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int n = holder.getLayoutPosition();
                        mISetBtnClickListener.onFunctionBtnClick(view, n);
                    }
                });

                break;
            case 1:
                PlanViewHolder planViewHolder = (PlanViewHolder) holder;
                final BoHuiListEntity entity1 = arraylist.get(position);
                planViewHolder.tvPlanName.setText(entity1.getEveName());
                planViewHolder.tvState.setTextColor(Color.RED);
                // （0.待启动 1.已启动 2.已授权 3.流程启动 4.完成 5.强行中止）
                if (!entity1.getState().equals("null")
                        && !entity1.getState().equals("")) {
                    String status = "";
                    switch (Integer.parseInt(entity1.getState())) {
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
                         * 2017.10.12
                         */
                        case 6:
                            status = "暂停";
                            break;
                    }
                    planViewHolder.tvState.setTextColor(Color.RED);
                    planViewHolder.tvState.setText(status);
                }

                //设置内容布局的宽为屏幕宽度
                planViewHolder.layout_content.getLayoutParams().width = Utils.getScreenWidth(mContext);
                //使左滑出现的菜单与内容一样高
                int w1 = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
                int h1 = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
                planViewHolder.layout_content.measure(w1, h1);
                int height1 = planViewHolder.layout_content.getMeasuredHeight();
                planViewHolder.btn_Function.getLayoutParams().height = height1;
                //item正文点击事件
                planViewHolder.layout_content.setOnClickListener(new View.OnClickListener() {
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


                //左滑菜单点击事件
                planViewHolder.btn_Function.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int n = holder.getLayoutPosition();
                        mISetBtnClickListener.onFunctionBtnClick(view, n);
                    }
                });

                break;

        }
    }

    public void refreshData(List<BoHuiListEntity> list)
    {
        this.arraylist = list;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {
        if (arg1 == 0) {
            return new EventViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_event_hide_drag, null, false));
        } else {
            return new PlanViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_plan_hide_drag, null, false));
        }
    }


    class EventViewHolder extends RecyclerView.ViewHolder {

        public TextView btn_Function;
        public ViewGroup layout_content;
        private TextView tvEventName;
        private TextView tvEventLevel;
        private TextView tvTradeType;
        private TextView tvState;
        private ImageView ivEventType;

        public EventViewHolder(View itemView) {
            super(itemView);

            btn_Function = (TextView) itemView.findViewById(R.id.tv_function);
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
            ((LeftSlideView) itemView).setSlidingButtonListener(LeftSlideRejectAdapter.this);
        }
    }

    class PlanViewHolder extends RecyclerView.ViewHolder {

        public TextView btn_Function;
        public ViewGroup layout_content;
        private TextView tvPlanName;
        private TextView tvState;

        public PlanViewHolder(View itemView) {
            super(itemView);

            btn_Function = (TextView) itemView.findViewById(R.id.tv_function);
            layout_content = (ViewGroup) itemView.findViewById(R.id.layout_content);
            tvPlanName = (TextView) itemView.findViewById(R.id.plan_listview_tv_plan_name);
            tvState = (TextView) itemView
                    .findViewById(R.id.plan_listview_tv_plan_status);

            ((LeftSlideView) itemView).setSlidingButtonListener(LeftSlideRejectAdapter.this);
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

        void onFunctionBtnClick(View view, int position);//点击左滑出来的菜单按钮
    }

}
