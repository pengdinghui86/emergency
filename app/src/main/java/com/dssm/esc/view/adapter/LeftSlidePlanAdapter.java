package com.dssm.esc.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dssm.esc.R;
import com.dssm.esc.util.Utils;
import com.dssm.esc.util.event.PlanStarListEntity;
import com.dssm.esc.view.widget.LeftSlideView;

import java.util.List;

public class LeftSlidePlanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements LeftSlideView.IonSlidingButtonListener {

    private Context mContext;
    private List<PlanStarListEntity> arraylist;
    /** 0,已授权；1,待授权；2,已启动预案；3,人员指派；4,协同通告；5,指挥与展示；6,预案执行；7，人员签到*/
    private String tags;

    private IonSlidingViewClickListener mIDeleteBtnClickListener;

    private IonSlidingViewClickListener mISetBtnClickListener;

    private LeftSlideView mMenu = null;


    public LeftSlidePlanAdapter(Context context,
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
    public int getItemViewType(int position) {
        return arraylist.get(position).getDataType();
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                EventViewHolder eventViewHolder = (EventViewHolder) holder;
                final PlanStarListEntity entity = arraylist.get(position);
                eventViewHolder.tvEventName.setText(entity.getEveName());
                eventViewHolder.tvState.setTextColor(mContext.getResources().getColor(R.color.color_state_red));
                eventViewHolder.tvTradeType.setText(entity.getTradeType());
                String eveType = entity.getEveType();
                if ("1".equals(eveType)) {
                    eventViewHolder.ivEventType.setImageResource(R.drawable.emergency_type);
                } else if ("2".equals(eveType)) {
                    eventViewHolder.ivEventType.setImageResource(R.drawable.drill_type);
                }
                eventViewHolder.tvEventLevel.setText(entity.getEveLevel());
                eventViewHolder.tvState.setVisibility(View.GONE);
                //设置内容布局的宽为屏幕宽度
                eventViewHolder.layout_content.getLayoutParams().width = Utils.getScreenWidth(mContext);
                int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
                int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
                eventViewHolder.layout_content.measure(w, h);
                eventViewHolder.btn_Function1.setVisibility(View.GONE);
                eventViewHolder.btn_Function2.setVisibility(View.GONE);
                if(position > 0)
                    eventViewHolder.item_event_hide_drag_v_split.setVisibility(View.VISIBLE);
                else
                    eventViewHolder.item_event_hide_drag_v_split.setVisibility(View.GONE);
                break;
            case 1:
                PlanViewHolder planViewHolder = (PlanViewHolder) holder;
                final PlanStarListEntity entity1 = arraylist.get(position);
                switch (tags) {
                    case "0":
                        //已授权
                        planViewHolder.btn_Function1.setVisibility(View.GONE);
                        planViewHolder.btn_Function2.setVisibility(View.GONE);
                        break;
                    case "1":
                        //待授权
                        planViewHolder.btn_Function1.setText("授权");
                        planViewHolder.btn_Function2.setText("中止");
                        planViewHolder.btn_Function1.setVisibility(View.VISIBLE);
                        planViewHolder.btn_Function2.setVisibility(View.VISIBLE);
                        break;
                    case "2":
                        //已启动预案
                        planViewHolder.btn_Function2.setText("中止");
                        planViewHolder.btn_Function1.setVisibility(View.GONE);
                        planViewHolder.btn_Function2.setVisibility(View.VISIBLE);
                        break;
                    case "3":
                        //人员指派
                        planViewHolder.btn_Function1.setVisibility(View.GONE);
                        planViewHolder.btn_Function2.setVisibility(View.GONE);
                        break;
                    case "4":
                        //协同通告
                        planViewHolder.btn_Function1.setVisibility(View.GONE);
                        planViewHolder.btn_Function2.setVisibility(View.GONE);
                        break;
                    case "5":
                        //指挥与展示
                        if (arraylist.get(position).isBtnPlanStart()) {
                            if (!arraylist.get(position).getState().equals("null")
                                    && !arraylist.get(position).getState().equals("")) {
                                switch (Integer.parseInt(arraylist.get(position).getState())) {
                                    case 0:
                                        planViewHolder.btn_Function1.setVisibility(View.GONE);
                                        planViewHolder.btn_Function2.setVisibility(View.GONE);
                                        break;
                                    case 1:
                                        planViewHolder.btn_Function1.setVisibility(View.GONE);
                                        planViewHolder.btn_Function2.setVisibility(View.GONE);
                                        break;
                                    case 2:
                                        planViewHolder.btn_Function1.setVisibility(View.VISIBLE);
                                        planViewHolder.btn_Function1.setText("启动");
                                        planViewHolder.btn_Function2.setVisibility(View.GONE);
                                        break;
                                    case 3:
                                        planViewHolder.btn_Function1.setVisibility(View.GONE);
                                        planViewHolder.btn_Function2.setVisibility(View.VISIBLE);
                                        planViewHolder.btn_Function2.setText("中止");
                                        break;
                                    case 4:
                                        planViewHolder.btn_Function1.setVisibility(View.GONE);
                                        planViewHolder.btn_Function2.setVisibility(View.GONE);
                                        break;
                                    case 5:
                                        planViewHolder.btn_Function1.setVisibility(View.GONE);
                                        planViewHolder.btn_Function2.setVisibility(View.GONE);
                                        break;
                                    default:
                                        planViewHolder.btn_Function1.setVisibility(View.GONE);
                                        planViewHolder.btn_Function2.setVisibility(View.GONE);
                                        break;
                                }
                            } else {
                                planViewHolder.btn_Function1.setVisibility(View.GONE);
                                planViewHolder.btn_Function2.setVisibility(View.GONE);
                            }
                        } else {
                            planViewHolder.btn_Function1.setVisibility(View.GONE);
                            planViewHolder.btn_Function2.setVisibility(View.GONE);
                        }
                        break;
                    case "6":
                        //预案执行
                        planViewHolder.btn_Function1.setVisibility(View.GONE);
                        planViewHolder.btn_Function2.setVisibility(View.GONE);
                        break;
                    case "7":
                        //人员签到
                        planViewHolder.btn_Function1.setVisibility(View.GONE);
                        planViewHolder.btn_Function2.setVisibility(View.GONE);
                        break;
                    default:
                        planViewHolder.btn_Function1.setVisibility(View.GONE);
                        planViewHolder.btn_Function2.setVisibility(View.GONE);
                        break;
                }
                planViewHolder.tvPlanName.setText(entity1.getPlanName() + "-" + entity1.getSceneName());
                planViewHolder.tvState.setVisibility(View.VISIBLE);
                planViewHolder.tvState.setTextColor(mContext.getResources().getColor(R.color.color_state_red));
                // （0.待启动 1.已启动 2.已授权 3.流程启动 4.完成 5.强行中止）
                if (!"null".equals(entity1.getState())
                        && !"".equals(entity1.getState())) {
                    String status = "";
                    switch (Integer.parseInt(entity1.getState())) {
                        case 0:
                            status = "待启动";
                            planViewHolder.tvState.setTextColor(mContext.getResources().getColor(R.color.color_state_gray));
                            break;
                        case 1:
                            status = "已启动";
                            planViewHolder.tvState.setTextColor(mContext.getResources().getColor(R.color.color_state_green));
                            break;
                        case 2:
                            status = "已授权";
                            planViewHolder.tvState.setTextColor(mContext.getResources().getColor(R.color.color_state_blue));
                            break;
                        case 3:
                            status = "执行中";
                            planViewHolder.tvState.setTextColor(mContext.getResources().getColor(R.color.color_state_yellow));
                            break;
                        case 4:
                            status = "完成";
                            planViewHolder.tvState.setTextColor(mContext.getResources().getColor(R.color.color_state_blue));
                            break;
                        case 5:
                            status = "强行中止";
                            planViewHolder.tvState.setTextColor(mContext.getResources().getColor(R.color.color_state_red));
                            break;

                        /**
                         * 添加暂停状态
                         * 2017.10.12
                         */
                        case 6:
                            status = "暂停";
                            planViewHolder.tvState.setTextColor(mContext.getResources().getColor(R.color.color_state_red));
                            break;
                    }
                    planViewHolder.tvState.setText(status);
                }

                //设置内容布局的宽为屏幕宽度
                planViewHolder.layout_content.getLayoutParams().width = Utils.getScreenWidth(mContext);
                //使左滑出现的菜单与内容一样高
                int w1 = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
                int h1 = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
                planViewHolder.layout_content.measure(w1, h1);
                int height1 = planViewHolder.layout_content.getMeasuredHeight();
                planViewHolder.btn_Function1.getLayoutParams().height = height1;
                planViewHolder.btn_Function2.getLayoutParams().height = height1;
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
                planViewHolder.btn_Function1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int n = holder.getLayoutPosition();
//                        if ("5".equals(tags)) {
//                            if (arraylist.get(n).isCheckExecutePeople() || "0".equals(arraylist.get(n).getIsSign())) {
//                                mISetBtnClickListener.onFunction1BtnClick(view, n);
//                            } else
//                                Toast.makeText(mContext, "没有执行人", Toast.LENGTH_SHORT).show();
//                        }
//                        else
                            mISetBtnClickListener.onFunction1BtnClick(view, n);
                    }
                });

                //左滑菜单点击事件
                planViewHolder.btn_Function2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int n = holder.getLayoutPosition();
                        mISetBtnClickListener.onFunction2BtnClick(view, n);
                    }
                });

                break;

        }
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

        public TextView btn_Function1;
        public TextView btn_Function2;
        public ViewGroup layout_content;
        private TextView tvEventName;
        private TextView tvEventLevel;
        private TextView tvTradeType;
        private TextView tvState;
        private ImageView ivEventType;
        private View item_event_hide_drag_v_split;

        public EventViewHolder(View itemView) {
            super(itemView);

            btn_Function1 = (TextView) itemView.findViewById(R.id.tv_function1);
            btn_Function2 = (TextView) itemView.findViewById(R.id.tv_function2);
            layout_content = (ViewGroup) itemView.findViewById(R.id.layout_content);
            tvEventName = (TextView) itemView.findViewById(R.id.event_listview_tv_name);
            tvState = (TextView) itemView
                    .findViewById(R.id.event_listview_tv_event_status);
            tvTradeType = (TextView) itemView
                    .findViewById(R.id.event_listview_tv_sub_name);
            tvEventLevel = (TextView) itemView
                    .findViewById(R.id.event_listview_tv_event_level);
            item_event_hide_drag_v_split = (View) itemView
                    .findViewById(R.id.item_event_hide_drag_v_split);
            ivEventType = (ImageView) itemView
                    .findViewById(R.id.event_listview_iv_type);
            ((LeftSlideView) itemView).setSlidingButtonListener(LeftSlidePlanAdapter.this);
        }
    }

    class PlanViewHolder extends RecyclerView.ViewHolder {

        public TextView btn_Function1;
        public TextView btn_Function2;
        public ViewGroup layout_content;
        private TextView tvPlanName;
        private TextView tvState;

        public PlanViewHolder(View itemView) {
            super(itemView);

            btn_Function1 = (TextView) itemView.findViewById(R.id.tv_function1);
            btn_Function2 = (TextView) itemView.findViewById(R.id.tv_function2);
            layout_content = (ViewGroup) itemView.findViewById(R.id.layout_content);
            tvPlanName = (TextView) itemView.findViewById(R.id.plan_listview_tv_plan_name);
            tvState = (TextView) itemView
                    .findViewById(R.id.plan_listview_tv_plan_status);

            ((LeftSlideView) itemView).setSlidingButtonListener(LeftSlidePlanAdapter.this);
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

        void onFunction1BtnClick(View view, int position);//点击左滑出来的菜单按钮

        void onFunction2BtnClick(View view, int position);//点击左滑出来的菜单按钮
    }

}
