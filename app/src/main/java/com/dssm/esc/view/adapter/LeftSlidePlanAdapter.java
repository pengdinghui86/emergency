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

public class LeftSlidePlanAdapter extends RecyclerView.Adapter<LeftSlidePlanAdapter.MyViewHolder> implements LeftSlideView.IonSlidingButtonListener {

    private Context mContext;
    private List<PlanStarListEntity> arraylist;
    /** 0，已授权1,授权决策；2,人员签到 ；3,人员指派;4,协同通告; 5,指挥与展示,6,事件流程列表7,驳回事件列表 */
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
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final PlanStarListEntity entity = arraylist.get(position);
        holder.tvPlanName.setText(entity.getEveName());
        holder.tvState.setTextColor(Color.RED);
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
                 * 2017.10.12
                 */
                case 6:
                    status = "暂停";
                    break;
            }
            holder.tvState.setTextColor(Color.RED);
            holder.tvState.setText(status);
        }

        //设置内容布局的宽为屏幕宽度
        holder.layout_content.getLayoutParams().width = Utils.getScreenWidth(mContext);
        //使左滑出现的菜单与内容一样高
        int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        holder.layout_content.measure(w, h);
        int height = holder.layout_content.getMeasuredHeight();
        holder.btn_Function.getLayoutParams().height = height;
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


        //左滑菜单点击事件
        holder.btn_Function.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n = holder.getLayoutPosition();
                mISetBtnClickListener.onFunctionBtnClick(view, n);
            }
        });

    }



    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup arg0, int arg1) {

        //获取自定义View的布局（加载item布局）
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_plan_hide_drag, arg0, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView btn_Function;
        public ViewGroup layout_content;
        private TextView tvPlanName;
        private TextView tvState;

        public MyViewHolder(View itemView) {
            super(itemView);

            btn_Function = (TextView) itemView.findViewById(R.id.tv_function);
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

        void onFunctionBtnClick(View view, int position);//点击左滑出来的菜单按钮
    }

}
