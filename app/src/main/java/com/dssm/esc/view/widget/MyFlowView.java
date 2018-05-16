package com.dssm.esc.view.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.dssm.esc.R;
import com.dssm.esc.status.RealTimeTrackingStatus;
import com.dssm.esc.util.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

public class MyFlowView extends View {

    private List<NSstep> steplist = new ArrayList<>();
    private int maxRow, maxColumn;
    private Canvas myCanvas;
    private Paint linePaint = new Paint();
    //开始节点
    private Paint startFlowPaint = new Paint();
    //结束节点
    private Paint endFlowPaint = new Paint();
    private Paint tempFlowPaint = new Paint();
    private Paint yellowFlowPaint = new Paint();
    private Paint greenFlowPaint = new Paint();
    private Paint redFlowPaint = new Paint();
    private Paint otherFlowPaint = new Paint();
    private Paint textPaint = new Paint();
    private Paint textFlowPaint = new Paint();
    private Paint circlePaint = new Paint();
    private int radius = 16;
    private int buttonRadius = 16;
    private int maxButtonRadius = 16;
    private int minButtonRadius = 16;
    private int textSize = 8;
    private int descriptionTextSize = 12;

    private float minZoom = 0.5f;
    private float maxZoom = 1.5f;
    private float currentZoom = 1.0f;
    private float smoothZoom = 1.0f;
    private float smoothMoveX, smoothMoveY;
    // touching variables
    private long lastTapTime;
    private float touchStartX, touchStartY;
    private float touchLastX, touchLastY;
    private float startd;
    private boolean doubleFinger = false;
    private float lastd;
    private final Matrix m = new Matrix();
    //控件初始化时的宽和高
    private int defaultWidth = 0, defaultHeight = 0;

    public MyFlowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init();
    }

    public MyFlowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }

    private void init() {
        radius = DisplayUtils.dp2px(radius);
        textSize = (int) (radius / 2f);
        buttonRadius = radius;
        maxButtonRadius = (int) (radius * maxZoom);
        minButtonRadius = (int) (radius * minZoom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        this.myCanvas = canvas;
        setPaintDefaultStyle();
        addArrowLine();
        addButtonAndText();
        setZoomAndMove(canvas);
    }

    private void setZoomAndMove(Canvas canvas) {
        // prepare matrix
        m.preScale(smoothZoom, smoothZoom);
        canvas.save();
        canvas.concat(m);
        canvas.restore();
        if(smoothMoveX > getWidth() - defaultWidth / 2f)
            smoothMoveX = getWidth() - defaultWidth / 2f;
        else if(smoothMoveX < - defaultWidth / 2f)
            smoothMoveX = - defaultWidth / 2f;
        if(smoothMoveY > getHeight() - defaultHeight / 2f)
            smoothMoveY = getHeight() - defaultHeight / 2f;
        else if(smoothMoveY < - defaultHeight / 2f)
            smoothMoveY = - defaultHeight / 2f;
        scrollTo((int) smoothMoveX, (int) smoothMoveY);
    }

    public void setData(NSSetPointValueToSteps nsSetPointValueToSteps) {
        int flag = 0;
        if(this.steplist.size() == 0)
            flag = 1;
        this.steplist = nsSetPointValueToSteps.steplist;
        maxRow = nsSetPointValueToSteps.rowNum;
        maxColumn = nsSetPointValueToSteps.maxLineNum;
        if(defaultWidth == 0)
            defaultWidth = getWidth();
        if(defaultHeight == 0)
            defaultHeight = getHeight();
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.height = Math.max((maxRow + 1) * 4 * buttonRadius, defaultHeight);
        lp.width = Math.max((maxColumn + 1) * 4 * buttonRadius, defaultWidth);
        if(maxColumn > maxRow)
            lp.height = Math.max(lp.width * 4 / 3, lp.height);
        setLayoutParams(lp);
        if(flag == 1)
            setPoisition2ExcuteNode(lp.width, lp.height);
        invalidate();
    }

    /**
     * 定位到正在执行的节点
     */
    private void setPoisition2ExcuteNode(int width, int height) {
        if(steplist.size() < 6)
            return;
        int flag = 0;
        for (NSstep step : steplist) {
            if(null != step.statusId) {
                //正在执行
                if (step.statusId.equals("4")) {
                    flag = 1;
                    smoothMoveX = step.y * width - defaultWidth / 2f;
                    smoothMoveY = step.x * height - defaultHeight / 2f;
                    break;
                }
            }
        }
        if(flag == 0) {
            for (NSstep step : steplist) {
                if (null != step.statusId) {
                    //准备执行
                    if (step.statusId.equals("6")) {
                        flag = 1;
                        smoothMoveX = step.y * width - defaultWidth / 2f;
                        smoothMoveY = step.x * height - defaultHeight / 2f;
                        break;
                    }
                }
            }
        }
        if(flag == 0) {
            smoothMoveX = steplist.get(2).y * width - defaultWidth / 2f;
            smoothMoveY = steplist.get(2).x * height - defaultHeight / 2f;
        }
    }

    private void addArrowLine() {
        for (NSstep onesstep : steplist) {
            for (NSstep sstep : steplist) {
                if (onesstep.isParentStep(sstep)) {
                    float by = (int) (onesstep.x * this.getHeight());
                    float bx = (int) (onesstep.y * this.getWidth());
                    float ex = (int) (sstep.y * this.getWidth());
                    float ey = (int) (sstep.x * this.getHeight());
                    if(((ex < smoothMoveX && bx < smoothMoveX)
                            || (ex > smoothMoveX + defaultWidth && bx > smoothMoveX + defaultWidth))
                            ||((ey - buttonRadius < smoothMoveY && by + buttonRadius < smoothMoveY)
                            || (ey - buttonRadius > smoothMoveY + defaultHeight
                            && by + buttonRadius > smoothMoveY + defaultHeight)))
                        continue;
                    drawAL(bx, by + buttonRadius, ex, ey - buttonRadius);
                }
            }
        }
    }

    private void addButtonAndText() {
        Paint paint;
        String str;
        for (final NSstep step : steplist) {
            float w = step.x * this.getHeight();
            float h = step.y * this.getWidth();
            if((h + buttonRadius < smoothMoveX || h - buttonRadius > smoothMoveX + defaultWidth)
                    && (w + buttonRadius < smoothMoveY || w - buttonRadius > smoothMoveY + defaultHeight))
                continue;
            if (step.type.equals("begin")) {
                paint = startFlowPaint;
                str = "开始";
                //drawText(h + buttonRadius, w, textPaint, "开始");
            } else if (step.type.equals("end")) {
                paint = endFlowPaint;
                str = "结束";
                //drawText(h + buttonRadius, w, textPaint, "结束");
            } else if (step.type.equals("merge")) {
                if (!step.statusId.equals("") && step.statusId != null
                        && step.statusId.length() > 0
                        && !step.statusId.equals("null")) {// 初始合并status为null

                    if (step.statusId.equals("1") || step.statusId.equals("2")
                            || step.statusId.equals("3")) {// (1，全部完成；2，部分完成；3，跳过)
                        // 绿色
                        paint = greenFlowPaint;
                        str = step.editOrderNum;
                    } else if (step.statusId.equals("4")) {// （4，正在执行）黄色
                        paint = yellowFlowPaint;
                        str = step.editOrderNum;
                    } else if (step.statusId.equals("5")
                            || step.statusId.equals("6")
                            || step.statusId.equals("7")) {// （5，可执行；6,准备执行；7，还未执行）
                        // 红色
                        paint = redFlowPaint;
                        str = step.editOrderNum;
                    } else {
                        paint = redFlowPaint;
                        str = step.editOrderNum;
                    }
                } else {
                    // 红色
                    paint = redFlowPaint;
                    str = step.editOrderNum;
                }
            } else if (step.type.equals("") && !step.stepId.startsWith("sid")) {
                // 新建节点
                paint = otherFlowPaint;
                str = step.editOrderNum;
            } else {
                if (step.statusId.equals("1") || step.statusId.equals("2")
                        || step.statusId.equals("3")) {// (1，全部完成；2，部分完成；3，跳过)
                    // 绿色
                    paint = greenFlowPaint;
                    str = step.editOrderNum;
                } else if (step.statusId.equals("4")) {// （4，正在执行）黄色
                    paint = yellowFlowPaint;
                    str = step.editOrderNum;
                } else if (step.statusId.equals("5")
                        || step.statusId.equals("6")
                        || step.statusId.equals("7")) {// （5，可执行；6,准备执行；7，还未执行）
                    paint = redFlowPaint;
                    str = step.editOrderNum;
                } else {
                    paint = redFlowPaint;
                    str = step.editOrderNum;
                }
            }
            if (!TextUtils.isEmpty(step.color)) {
                tempFlowPaint.setColor(Color.parseColor(step.color));
                paint = tempFlowPaint;
                circlePaint.setColor(0xFF000000); // 边框内部颜色
            } else {
                circlePaint.setColor(0xFFf00); // 边框内部颜色
            }
            drawCircleAndWord(h, w, paint, textFlowPaint, circlePaint, str);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // single touch
        if (event.getPointerCount() == 1) {
            processSingleTouchEvent(event);
        }
        // double touch
        if (event.getPointerCount() == 2) {
            processDoubleTouchEvent(event);
        }
        getRootView().invalidate();
        invalidate();
        return true;
    }

    private void processDoubleTouchEvent(final MotionEvent ev) {
        final float x1 = ev.getX(0);
        final float y1 = ev.getY(0);
        final float x2 = ev.getX(1);
        final float y2 = ev.getY(1);
        // pointers distance
        final float d = (float) Math.hypot(x2 - x1, y2 - y1);
        final float dd = d - lastd;
        lastd = d;
        final float ld = Math.abs(d - startd);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startd = d;
                doubleFinger = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (ld > 30.0f) {
                    final float x = 0.5f * (x1 + x2);
                    final float y = 0.5f * (y1 + y2);
                    float zoom;
                    if(dd > 0f) {
                        zoom = smoothZoom * 1.02f;
                    }
                    else
                        zoom = smoothZoom / 1.02f;
                    smoothZoomTo(zoom, x, y, 1);
                }
                break;
            case MotionEvent.ACTION_UP:
                doubleFinger = false;
                break;
            default:
                doubleFinger = false;
                break;
        }
        ev.setAction(MotionEvent.ACTION_CANCEL);
        super.onTouchEvent(ev);
    }

    private void processSingleTouchEvent(final MotionEvent ev) {
        final float x = ev.getX();
        final float y = ev.getY();
        float lx = x - touchStartX;
        float ly = y - touchStartY;
        final float l = (float) Math.hypot(lx, ly);
        float dx = x - touchLastX;
        float dy = y - touchLastY;
        touchLastX = x;
        touchLastY = y;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStartX = x;
                touchStartY = y;
                touchLastX = x;
                touchLastY = y;
                detectClicked(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                if (!doubleFinger && l > 10.0f) {
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    super.onTouchEvent(ev);
                    smoothMoveX -= dx;
                    smoothMoveY -= dy;
                    return;
                }
                break;
            case MotionEvent.ACTION_OUTSIDE:
            case MotionEvent.ACTION_UP:
                // tap
                if (l < 20.0f) {
                    // check double tap
                    if (System.currentTimeMillis() - lastTapTime < 500) {
                        if(smoothZoom == maxZoom) {
                            smoothZoomTo(1.0f, 0, 0, 0);
                        }
                        else
                            smoothZoomTo(smoothZoom * 1.2f, x, y, 0);
                        lastTapTime = 0;
                        ev.setAction(MotionEvent.ACTION_CANCEL);
                        super.onTouchEvent(ev);
                        return;
                    }
                    lastTapTime = System.currentTimeMillis();
                    performClick();
                }
                break;
            default:
                break;
        }
        super.onTouchEvent(ev);
    }

    /**
     * param zoom 缩放参数
     * param x 屏幕x坐标值
     * param y 屏幕y坐标值
     * param flag 是否保持位置不变 0=否，1=是
    **/
    public synchronized void smoothZoomTo(float zoom, float x, float y, int flag) {
        if(zoom > maxZoom)
            smoothZoom = maxZoom;
        else if(zoom < minZoom)
            smoothZoom = minZoom;
        else
            smoothZoom = zoom;
        if(currentZoom != smoothZoom) {
            buttonRadius = radius + (int) (radius * (smoothZoom - 1f));
            if(buttonRadius > maxButtonRadius)
                buttonRadius = maxButtonRadius;
            else if(buttonRadius < minButtonRadius)
                buttonRadius = minButtonRadius;
            currentZoom = smoothZoom;
            textSize = (int) (buttonRadius / 2f);
            ViewGroup.LayoutParams lp = getLayoutParams();
            float tempX = x / lp.width;
            float tempY = y / lp.height;
            float dx = smoothMoveX / lp.width;
            float dy = smoothMoveY / lp.height;
            lp.height = Math.max((maxRow + 1) * 4 * buttonRadius, defaultHeight);
            lp.width = Math.max((maxColumn + 1) * 4 * buttonRadius, defaultWidth);
            if(maxColumn > maxRow)
                lp.height = Math.max(lp.width * 4 / 3, lp.height);
            if(flag == 1) {
                smoothMoveX = (tempX + dx) * lp.width - x;
                smoothMoveY = (tempY + dy) * lp.height - y;
            }
            else {
                smoothMoveX = (tempX + dx) * lp.width - defaultWidth / 2f;
                smoothMoveY = (tempY + dy) * lp.height - defaultHeight / 2f;
            }
            setLayoutParams(lp);
            if(currentZoom == 1.0f)
                setPoisition2ExcuteNode(lp.width, lp.height);
        }
    }

    private void detectClicked(float x, float y) {
        x = x + smoothMoveX;
        y = y + smoothMoveY;
        for(NSstep step : steplist) {
            float w = step.x * this.getHeight();
            float h = step.y * this.getWidth();
            if(w - buttonRadius <= y && w + buttonRadius >= y && h - buttonRadius <= x && h + buttonRadius >= x) {
                showDialog(step);
                break;
            }
        }
    }

    private void showDialog(NSstep step) {
        String beginTime = step.beginTime;
        String endTime = step.endTime;
        String executePeople = step.executePeople;
        String message = "";
        CustomDialog.Builder builder = new CustomDialog.Builder(
                getContext());
        if (step.type.equals("") && !step.stepId.startsWith("sid")) {// 新建节点
            builder.setTitle("步骤：" + step.name);
            if (step.statusId.equals(RealTimeTrackingStatus.IGNORE) ||
                    step.statusId.equals(RealTimeTrackingStatus.NO_OPTION_CAN_START) ||
                    step.statusId.equals(RealTimeTrackingStatus.NO_OPTION_NO_CAN_START)) {
                message += "执行状态：跳过";
            }
            if(!"".equals(message))
                builder.setMessage(message);
            builder.setPositiveButton("ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(
                                DialogInterface dialog,
                                int which) {
                            dialog.dismiss();

                        }
                    });

            builder.create().show();
        } else if (step.type.equals("merge")) {// 合并节点
            if (!TextUtils.isEmpty(beginTime)) {
                message += "开始时间：" + beginTime;
            }
            if (!TextUtils.isEmpty(endTime)) {
                if(!message.equals(""))
                    message += "\n" + "完成时间：" + endTime;
                else
                    message += "\n" + "完成时间：" + endTime;
            }
            if (step.statusId.equals(RealTimeTrackingStatus.IGNORE) ||
                    step.statusId.equals(RealTimeTrackingStatus.NO_OPTION_CAN_START) ||
                    step.statusId.equals(RealTimeTrackingStatus.NO_OPTION_NO_CAN_START)) {
                if(!message.equals(""))
                    message += "\n" + "执行状态：跳过";
                else
                    message += "执行状态：跳过";
            }
            if(!"".equals(message))
                builder.setMessage(message);
            builder.setTitle("步骤：" + step.name);
            builder.setPositiveButton("ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(
                                DialogInterface dialog,
                                int which) {
                            dialog.dismiss();

                        }
                    });

            builder.create().show();
        }
        else if (step.type.equals("drillNew")) {// 新增节点
            builder.setTitle("步骤：" + step.name);
            if (step.statusId.equals(RealTimeTrackingStatus.IGNORE) ||
                    step.statusId.equals(RealTimeTrackingStatus.NO_OPTION_CAN_START) ||
                    step.statusId.equals(RealTimeTrackingStatus.NO_OPTION_NO_CAN_START)) {
                message += "执行状态：跳过";
            }
            if(!"".equals(message))
                builder.setMessage(message);
            builder.setPositiveButton("ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(
                                DialogInterface dialog,
                                int which) {
                            dialog.dismiss();

                        }
                    });

            builder.create().show();
        }
        else {
            if (!TextUtils.isEmpty(executePeople)) {
                message += "执行人：" + executePeople;
            }
            if (!TextUtils.isEmpty(beginTime)) {
                if(!message.equals(""))
                    message += "\n" + "开始时间：" + beginTime;
                else
                    message += "开始时间：" + beginTime;
            }
            if (!TextUtils.isEmpty(endTime)) {
                if(!message.equals(""))
                    message += "\n" + "完成时间：" + endTime;
                else
                    message += "完成时间：" + endTime;
            }
            if (step.statusId.equals(RealTimeTrackingStatus.IGNORE) ||
                    step.statusId.equals(RealTimeTrackingStatus.NO_OPTION_CAN_START) ||
                    step.statusId.equals(RealTimeTrackingStatus.NO_OPTION_NO_CAN_START)) {
                if(!message.equals(""))
                    message += "\n" + "执行状态：跳过";
                else
                    message += "执行状态：跳过";
            }
//                            message = "执行人：" + executePeople + "\n" + "开始时间："
//                                    + beginTime + "\n" + "完成时间：" + endTime;
            if(!"".equals(message))
                builder.setMessage(message);
            builder.setTitle("步骤：" + step.name);
            builder.setPositiveButton("ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(
                                DialogInterface dialog,
                                int which) {
                            dialog.dismiss();

                        }
                    });

            builder.create().show();
        }
    }

    /**
     * 设置画笔默认样式
     */
    public void setPaintDefaultStyle() {
        linePaint.setAntiAlias(true);
        linePaint.setColor(Color.RED);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(2);

        startFlowPaint.setAntiAlias(true);
        startFlowPaint.setColor(getResources().getColor(R.color.color_flow_start));
        startFlowPaint.setStyle(Paint.Style.FILL);

        endFlowPaint.setAntiAlias(true);
        endFlowPaint.setColor(getResources().getColor(R.color.color_flow_end));
        endFlowPaint.setStyle(Paint.Style.FILL);

        tempFlowPaint.setAntiAlias(true);
        tempFlowPaint.setColor(getResources().getColor(R.color.white));
        tempFlowPaint.setStyle(Paint.Style.FILL);

        yellowFlowPaint.setAntiAlias(true);
        yellowFlowPaint.setColor(getResources().getColor(R.color.color_flow_yellow));
        yellowFlowPaint.setStyle(Paint.Style.FILL);

        greenFlowPaint.setAntiAlias(true);
        greenFlowPaint.setColor(getResources().getColor(R.color.color_flow_green));
        greenFlowPaint.setStyle(Paint.Style.FILL);

        redFlowPaint.setAntiAlias(true);
        redFlowPaint.setColor(getResources().getColor(R.color.color_flow_red));
        redFlowPaint.setStyle(Paint.Style.FILL);

        otherFlowPaint.setAntiAlias(true);
        otherFlowPaint.setColor(getResources().getColor(R.color.color_flow_other));
        otherFlowPaint.setStyle(Paint.Style.FILL);

        textFlowPaint.setAntiAlias(true);
        textFlowPaint.setColor(getResources().getColor(R.color.white));
        textFlowPaint.setStyle(Paint.Style.STROKE);
        textFlowPaint.setTextSize(textSize);

        circlePaint.setAntiAlias(true);
        circlePaint.setColor(getResources().getColor(R.color.white));
        circlePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(1);

        textPaint.setAntiAlias(true);
        textPaint.setColor(getResources().getColor(R.color.black));
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setTextSize(DisplayUtils.dp2px(descriptionTextSize));
    }


    /**
     * 画圆和文字
     *
     * @param x      x坐标
     * @param y      y坐标
     */
    public void drawCircleAndWord(float x, float y, Paint buttonPaint, Paint textPaint, Paint circlePaint, String str) {
        myCanvas.drawCircle(x, y, buttonRadius, buttonPaint);
        myCanvas.drawCircle(x, y, buttonRadius, circlePaint);
        Rect rect = new Rect();
        //返回包围整个字符串的最小的一个Rect区域
        textPaint.getTextBounds(str, 0, str.length(), rect);
        int strWidth = rect.width();
        int strHeight = rect.height();
        myCanvas.drawText(str, x - strWidth / 2f, y + strHeight / 2f, textPaint);
    }

    private void drawText(float x, float y, Paint textPaint, String str) {
        Rect rect = new Rect();
        //返回包围整个字符串的最小的一个Rect区域
        textPaint.getTextBounds(str, 0, str.length(), rect);
        int strHeight = rect.height();
        myCanvas.drawText(str, x, y + strHeight / 2f, textPaint);
    }

    /**
     * 画箭头
     *
     * @param sx 起点x坐标
     * @param sy 起点Y坐标
     * @param ex   终点X坐标
     * @param ey   终点Y坐标
     */
    public void drawAL(float sx, float sy, float ex, float ey) {
        double H = buttonRadius / 4f; // 箭头高度
        double L = buttonRadius / 12f; // 底边的一半
        int x3,y3,x4,y4;
        double awrad = Math.atan(L / H); // 箭头角度
        double arraow_len = Math.sqrt(L * L + H * H); // 箭头的长度
        double[] arrXY_1 = rotateVec(ex - sx, ey - sy, awrad, true, arraow_len);
        double[] arrXY_2 = rotateVec(ex - sx, ey - sy, -awrad, true, arraow_len);
        double x_3 = ex - arrXY_1[0]; // (x3,y3)是第一端点
        double y_3 = ey - arrXY_1[1];
        double x_4 = ex - arrXY_2[0]; // (x4,y4)是第二端点
        double y_4 = ey - arrXY_2[1];
        Double X3 = new Double(x_3);
        x3 = X3.intValue();
        Double Y3 = new Double(y_3);
        y3 = Y3.intValue();
        Double X4 = new Double(x_4);
        x4 = X4.intValue();
        Double Y4 = new Double(y_4);
        y4 = Y4.intValue();
        // 画线
        myCanvas.drawLine(sx, sy, ex, ey, linePaint);
        if(x3 >0 || y3 > 0)
            myCanvas.drawLine(ex, ey, x3, y3, linePaint);
        if(x4 >0 || y4 > 0)
            myCanvas.drawLine(ex, ey, x4, y4, linePaint);
    }

    // 计算
    public double[] rotateVec(float px, float py, double ang, boolean isChLen, double newLen) {
        double mathstr[] = new double[2];
        // 矢量旋转函数，参数含义分别是x分量、y分量、旋转角、是否改变长度、新长度
        double vx = px * Math.cos(ang) - py * Math.sin(ang);
        double vy = px * Math.sin(ang) + py * Math.cos(ang);
        if (isChLen) {
            double d = Math.sqrt(vx * vx + vy * vy);
            vx = vx / d * newLen;
            vy = vy / d * newLen;
            mathstr[0] = vx;
            mathstr[1] = vy;
        }
        return mathstr;
    }
}