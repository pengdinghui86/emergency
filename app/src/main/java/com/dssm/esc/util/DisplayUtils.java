package com.dssm.esc.util;

import android.util.TypedValue;

import com.easemob.chatuidemo.DemoApplication;

public class DisplayUtils {
    /**
     * convert px to its equivalent dp
     *
     * 将px转换为与之相等的dp
     */
    public static int px2dp(float pxValue) {
        final float scale = DemoApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * convert dp to its equivalent px
     *
     * 将dp转换为与之相等的px
     */
    public static int dp2px(float dipValue) {
        final float scale = DemoApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    /**
     * convert px to its equivalent sp
     *
     * 将px转换为sp
     */
    public static int px2sp(float pxValue) {
        final float fontScale = DemoApplication.getInstance().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    /**
     * convert sp to its equivalent px
     *
     * 将sp转换为px
     */
    public static int sp2px(float spValue) {
        final float fontScale = DemoApplication.getInstance().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * convert dp to its equivalent px
     */
    protected int dp2px(int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, DemoApplication.getInstance().getResources().getDisplayMetrics());
    }

    /**
     * convert sp to its equivalent px
     */
    protected int sp2px(int sp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,DemoApplication.getInstance().getResources().getDisplayMetrics());
    }
}
