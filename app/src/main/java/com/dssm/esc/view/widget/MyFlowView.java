package com.dssm.esc.view.widget;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.dssm.esc.view.activity.ProcessMonitoringSubMissionActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * 流程监控-流程图绘制
 */

public class MyFlowView extends View {

    private NSSetPointValueToSteps nsSetPointValueToSteps = new NSSetPointValueToSteps();
    private NSstep currentStep = new NSstep();
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
    private Paint plusPaint = new Paint();
    private Paint textFlowPaint = new Paint();
    private Paint circlePaint = new Paint();
    private int radius = 16;
    private int buttonRadius = 16;
    //节点圆心之间的最小距离
    private int minDistance = 64;
    private int maxButtonRadius = 16;
    private int minButtonRadius = 16;
    private int textSize = 8;

    private float minZoom = 0.1f;
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
        if(nsSetPointValueToSteps.steplist != null) {
            if (nsSetPointValueToSteps.steplist.size() > 50)
                addArrowLine2();
            else
                addArrowLine();
            addButtonAndText();
            setZoomAndMove(canvas);
            clearDrawLineFlag();
        }
    }

    private void setZoomAndMove(Canvas canvas) {
        // prepare matrix
        m.preScale(smoothZoom, smoothZoom);
        canvas.save();
        canvas.concat(m);
        canvas.restore();
        if(smoothMoveX > getWidth() - defaultWidth * 4 / 5)
            smoothMoveX = getWidth() - defaultWidth * 4 / 5;
        else if(smoothMoveX < - defaultWidth / 5)
            smoothMoveX = - defaultWidth / 5;
        if(smoothMoveY > getHeight() - defaultHeight * 4 / 5)
            smoothMoveY = getHeight() - defaultHeight * 4 / 5;
        else if(smoothMoveY < - defaultHeight / 5)
            smoothMoveY = - defaultHeight / 5;
        scrollTo((int) smoothMoveX, (int) smoothMoveY);
    }

    public void setData(NSSetPointValueToSteps nsSetPointValueToSteps) {
        this.nsSetPointValueToSteps = nsSetPointValueToSteps;
        maxRow = nsSetPointValueToSteps.rowNum;
        maxColumn = nsSetPointValueToSteps.maxLineNum;
        if(defaultWidth == 0)
            defaultWidth = getWidth();
        if(defaultHeight == 0)
            defaultHeight = getHeight();
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.height = Math.max((maxRow + 1) * 4 * buttonRadius, defaultHeight);
        lp.width = Math.max((maxColumn + 1) * 4 * buttonRadius, defaultWidth);
        if(maxColumn > 100)
            lp.height = Math.max(lp.width, lp.height);
        if(maxColumn > 30 && maxColumn > maxRow)
            setMinZoom(Math.max(minZoom - 0.2f * minZoom * (maxColumn / 30), 0.1f));
        else if(maxRow > 30)
            setMinZoom(Math.max(minZoom - 0.2f * minZoom * (maxRow / 30), 0.1f));
        setLayoutParams(lp);
        currentStep = new NSstep();
        setPoisition2ExcuteNode(lp.width, lp.height);
        invalidate();
    }

    /**
     * 定位到正在执行的节点
     */
    private void setPoisition2ExcuteNode(int width, int height) {
        int flag = 0;
        for (NSstep step : nsSetPointValueToSteps.steplist) {
            if(null != step.statusId) {
                //正在执行
                if (step.statusId.equals("4")) {
                    flag = 1;
                    currentStep = step;
                    if(nsSetPointValueToSteps.steplist.size() > 6) {
                        smoothMoveX = step.y * width - defaultWidth / 2f;
                        smoothMoveY = step.x * height - defaultHeight / 2f;
                    }
                    break;
                }
            }
        }
        if(flag == 0) {
            for (NSstep step : nsSetPointValueToSteps.steplist) {
                if (null != step.statusId) {
                    //准备执行
                    if (step.statusId.equals("6")) {
                        flag = 1;
                        if(nsSetPointValueToSteps.steplist.size() > 6) {
                            smoothMoveX = step.y * width - defaultWidth / 2f;
                            smoothMoveY = step.x * height - defaultHeight / 2f;
                        }
                        break;
                    }
                }
            }
        }
        if(flag == 0) {
            for (NSstep step : nsSetPointValueToSteps.steplist) {
                if (null != step.statusId) {
                    //可执行
                    if (step.statusId.equals("5")) {
                        flag = 1;
                        if(nsSetPointValueToSteps.steplist.size() > 6) {
                            smoothMoveX = step.y * width - defaultWidth / 2f;
                            smoothMoveY = step.x * height - defaultHeight / 2f;
                        }
                        break;
                    }
                }
            }
        }
        if(flag == 0) {
            if (nsSetPointValueToSteps.steplist.size() > 6) {
                smoothMoveX = nsSetPointValueToSteps.steplist.get(0).y * width - defaultWidth / 2f;
                smoothMoveY = nsSetPointValueToSteps.steplist.get(2).x * height - defaultHeight / 2f;
            }
        }
        if(nsSetPointValueToSteps.steplist.size() > 6) {
            smoothMoveX = (smoothMoveX > 0 ? smoothMoveX : 0);
            smoothMoveY = (smoothMoveY > 0 ? smoothMoveY : 0);
        }
    }

    public void setMinZoom(float minZoom) {
        this.minZoom = minZoom;
        minButtonRadius = (int) (radius * minZoom);
    }

    public void setMaxZoom(float maxZoom) {
        this.maxZoom = maxZoom;
    }

    private List<NSstep> findAllParentNodes(NSstep step) {
        List<NSstep> parentList = new ArrayList<>();
        for (NSstep oneStep : nsSetPointValueToSteps.steplist) {
            if (oneStep.isParentStep(step)) {
                parentList.add(oneStep);
            }
        }
        return parentList;
    }

    private List<NSstep> findAllNextNodes(NSstep step) {
        List<NSstep> list = new ArrayList<>();
        for (NSstep oneStep : nsSetPointValueToSteps.steplist) {
            for(String id : step.nextStepIds) {
                if (oneStep.stepId.equals(id)) {
                    list.add(oneStep);
                    break;
                }
            }
        }
        return list;
    }

    private void addArrowLine() {
        for (NSstep onesstep : nsSetPointValueToSteps.steplist) {
            if(currentStep.stepId != null && currentStep.stepId.equals(onesstep.stepId))
                linePaint.setColor(getResources().getColor(R.color.color_flow_line_green));
            else
                linePaint.setColor(getResources().getColor(R.color.color_flow_line_red));
            for (NSstep sstep : nsSetPointValueToSteps.steplist) {
                if (onesstep.isParentStep(sstep)) {
                    float by = (int) (onesstep.x * this.getHeight());
                    float bx = (int) (onesstep.y * this.getWidth());
                    float ex = (int) (sstep.y * this.getWidth());
                    float ey = (int) (sstep.x * this.getHeight());
//                    if(!invisible2Draw(bx, by + buttonRadius, ex, ey - buttonRadius))
//                    continue;
                    drawAL(bx, by + buttonRadius, ex, ey - buttonRadius);
                }
            }
        }
    }

    private void clearDrawLineFlag() {
        for (NSstep step : nsSetPointValueToSteps.steplist) {
            if(step.parentDrawLine != null)
                step.parentDrawLine.clear();
            if(step.nextDrawLine != null)
                step.nextDrawLine.clear();
        }
    }

    private boolean containsStepId(List<String> list, String id) {
        boolean result = false;
        for(String str : list) {
            if(str.equals(id)) {
                result = true;
                break;
            }
        }
        return result;
    }

    private void addArrowLine2() {
        for (NSstep sstep : nsSetPointValueToSteps.steplist) {
            List<NSstep> parentSteps = findAllParentNodes(sstep);
            List<NSstep> nextSteps = findAllNextNodes(sstep);
            if (parentSteps.size() == 1) {
                if(currentStep.stepId != null && currentStep.stepId.equals(parentSteps.get(0).stepId))
                    linePaint.setColor(getResources().getColor(R.color.color_flow_line_green));
                else
                    linePaint.setColor(getResources().getColor(R.color.color_flow_line_red));
                if(!containsStepId(parentSteps.get(0).nextDrawLine, sstep.stepId)) {
                    float by = parentSteps.get(0).x * this.getHeight();
                    float bx = parentSteps.get(0).y * this.getWidth();
                    float ex = sstep.y * this.getWidth();
                    float ey = sstep.x * this.getHeight();
                    //跨层级
                    if(Math.abs(parentSteps.get(0).lineId - sstep.lineId) > 1) {
                        if(bx > ex) {
                            drawLeftBrokenAL(bx, by, ex, ey);
                        }
                        else if(bx < ex){
                            drawRightBrokenAL(bx, by, ex, ey);
                        }
                        else {
                            myCanvas.drawLine(bx, by, bx + 2 * buttonRadius - 2, by, linePaint);
                            drawAL(ex - 2 * buttonRadius - 2, ey, ex - buttonRadius, ey);
                            myCanvas.drawLine(bx + 2 * buttonRadius - 2, by + 2 * buttonRadius, ex - 2 * buttonRadius - 2, ey, linePaint);
                        }
                    }
                    else {
                        drawAL(bx, by + buttonRadius, ex, ey - buttonRadius);
                    }
                    parentSteps.get(0).nextDrawLine.add(sstep.stepId);
                    sstep.parentDrawLine.add(parentSteps.get(0).stepId);
                }
            }
            else if(parentSteps.size() > 1){
                if(currentStep.stepId != null && currentStep.stepId.equals(parentSteps.get(0).stepId))
                    linePaint.setColor(getResources().getColor(R.color.color_flow_line_green));
                else
                    linePaint.setColor(getResources().getColor(R.color.color_flow_line_red));
                List<NSstep> tmpParentSteps = new ArrayList<>();
                List<Integer> lineIds = findAllLineId(parentSteps);
                for(Integer i : lineIds) {
                    tmpParentSteps.clear();
                    for(NSstep sstep1 : parentSteps) {
                        if(sstep1.lineId == i && !containsStepId(sstep.parentDrawLine, sstep1.stepId))
                            tmpParentSteps.add(sstep1);
                    }
                    if(tmpParentSteps.size() > 3) {
                        drawParentMutiSteps(tmpParentSteps, sstep);
                    }
                    else if(tmpParentSteps.size() > 0) {
                        drawParentSteps(tmpParentSteps, sstep);
                    }
                }
            }

            if(nextSteps.size() > 1) {
                if(currentStep.stepId != null && currentStep.stepId.equals(sstep.stepId))
                    linePaint.setColor(getResources().getColor(R.color.color_flow_line_green));
                else
                    linePaint.setColor(getResources().getColor(R.color.color_flow_line_red));
                List<NSstep> tmpNextSteps = new ArrayList<>();
                List<Integer> lineIds = findAllLineId(nextSteps);
                for(Integer i : lineIds) {
                    tmpNextSteps.clear();
                    for(NSstep sstep1 : nextSteps) {
                        if(sstep1.lineId == i)
                            tmpNextSteps.add(sstep1);
                    }
                    if(tmpNextSteps.size() > 3) {
                        drawNextMutiSteps(tmpNextSteps, sstep);
                    }
                    else if(tmpNextSteps.size() > 0) {
                        drawNextSteps(tmpNextSteps, sstep);
                    }
                }
            }
        }
    }

    /*
    **在跨层级情况下避免连接线穿过节点因此绘制折线
    **/
    private void drawLeftBrokenAL(float bx, float by, float ex, float ey) {
        myCanvas.drawLine(bx - buttonRadius, by, bx - 2 * buttonRadius + 2, by, linePaint);
        myCanvas.drawLine(bx - 2 * buttonRadius + 2, by, bx - 2 * buttonRadius + 2, by + 2 * buttonRadius, linePaint);
        drawAL(ex + 2 * buttonRadius + 2, ey, ex + buttonRadius, ey);
        myCanvas.drawLine(bx - 2 * buttonRadius + 2, by + 2 * buttonRadius, ex + 2 * buttonRadius + 2, by + 2 * buttonRadius, linePaint);
        myCanvas.drawLine(ex + 2 * buttonRadius + 2, by + 2 * buttonRadius, ex + 2 * buttonRadius + 2, ey, linePaint);
    }

    /*
     **在跨层级情况下避免连接线穿过节点因此绘制折线
     **/
    private void drawRightBrokenAL(float bx, float by, float ex, float ey) {
        myCanvas.drawLine(bx + buttonRadius, by, bx + 2 * buttonRadius - 2, by, linePaint);
        myCanvas.drawLine(bx + 2 * buttonRadius - 2, by, bx + 2 * buttonRadius - 2, by + 2 * buttonRadius, linePaint);
        drawAL(ex - 2 * buttonRadius - 2, ey, ex - buttonRadius, ey);
        myCanvas.drawLine(bx + 2 * buttonRadius - 2, by + 2 * buttonRadius, ex - 2 * buttonRadius - 2, by + 2 * buttonRadius, linePaint);
        myCanvas.drawLine(ex - 2 * buttonRadius - 2, by + 2 * buttonRadius, ex - 2 * buttonRadius - 2, ey, linePaint);
    }

    private List<Integer> findAllLineId(List<NSstep> steps) {
        List<Integer> list = new ArrayList<>();
        for(NSstep step : steps) {
            if(!containsLineId(list, step.lineId))
                list.add(step.lineId);
        }
        return list;
    }

    private boolean containsLineId(List<Integer> list, int id) {
        boolean result = false;
        for(Integer lineId : list) {
            if(lineId == id) {
                result = true;
                break;
            }
        }
        return result;
    }

    private void drawParentMutiSteps(List<NSstep> parentSteps, NSstep sstep) {
        float maxY = parentSteps.get(0).y, minY = parentSteps.get(0).y;
        for(NSstep step : parentSteps) {
            if(maxY < step.y)
                maxY = step.y;
            if(minY > step.y)
                minY = step.y;
        }
        float by = (parentSteps.get(0).x + (sstep.x - parentSteps.get(0).x) /
                2 / (Math.abs(parentSteps.get(0).lineId - sstep.lineId))) * this.getHeight();
        float bx = minY * this.getWidth();
        float ex = maxY * this.getWidth();
        float ey = by;
        float middleX = sstep.y * this.getWidth();
        float endX = sstep.y * this.getWidth();
        float endY = sstep.x * this.getHeight();
        if(endX < bx) {
            middleX = bx + (ex - bx) / 2 / (Math.abs(parentSteps.get(0).lineId - sstep.lineId));
        }
        else if(endX > ex) {
            middleX = bx + (ex - bx) * (1f - 1f / 2 / (Math.abs(parentSteps.get(0).lineId - sstep.lineId)));;
        }
        myCanvas.drawLine(bx, by, ex, ey, linePaint);

        //跨层级
        if(Math.abs(parentSteps.get(0).lineId - sstep.lineId) > 1) {
            if(middleX > endX) {
                myCanvas.drawLine(middleX, by, endX + 2 * buttonRadius - 2, by, linePaint);
                myCanvas.drawLine(endX + 2 * buttonRadius - 2, by, endX + 2 * buttonRadius - 2, endY, linePaint);
                drawAL(endX + 2 * buttonRadius - 2, endY, endX + buttonRadius, endY);

            }
            else if(middleX < endX){
                myCanvas.drawLine(middleX, by, endX - 2 * buttonRadius + 2, by, linePaint);
                myCanvas.drawLine(endX - 2 * buttonRadius + 2, by, endX - 2 * buttonRadius + 2, endY, linePaint);
                drawAL(endX - 2 * buttonRadius + 2, endY, endX - buttonRadius, endY);

            }
            else {
                myCanvas.drawLine(middleX + 2 * buttonRadius - 2, by, endX + 2 * buttonRadius - 2, endY, linePaint);
                drawAL(endX + 2 * buttonRadius - 2, endY, endX + buttonRadius, endY);

            }
        }
        else {
            drawAL(middleX, by, endX, endY - buttonRadius);

        }
        for(NSstep step : parentSteps) {
            float y1 = step.x * this.getHeight();
            float x1 = step.y * this.getWidth();
            float x2 = x1;
            float y2 = by;
            myCanvas.drawLine(x1, y1 + buttonRadius, x2, y2, linePaint);

            step.nextDrawLine.add(sstep.stepId);
            sstep.parentDrawLine.add(step.stepId);
        }
    }

    private void drawParentSteps(List<NSstep> parentSteps, NSstep sstep) {
        for(NSstep step : parentSteps) {
            float y1 = step.x * this.getHeight();
            float x1 = step.y * this.getWidth();
            float x2 = sstep.y * this.getWidth();
            float y2 = sstep.x * this.getHeight();
            //跨层级
            if(Math.abs(step.lineId - sstep.lineId) > 1) {
                if(x1 > x2) {
                    drawLeftBrokenAL(x1, y1, x2, y2);
                }
                else if(x1 < x2){
                    drawRightBrokenAL(x1, y1, x2, y2);
                }
                else {
                    myCanvas.drawLine(x1 + buttonRadius, y1, x1 + 2 * buttonRadius - 2, y1, linePaint);
                    drawAL(x2 + 2 * buttonRadius - 2, y2, x2 + buttonRadius, y2);
                    myCanvas.drawLine(x1 + 2 * buttonRadius - 2, y1, x2 + 2 * buttonRadius - 2, y2, linePaint);
                }
            }
            else {
                drawAL(x1, y1 + buttonRadius, x2, y2 - buttonRadius);
            }
            step.nextDrawLine.add(sstep.stepId);
            sstep.parentDrawLine.add(step.stepId);
        }
    }

    private void drawNextMutiSteps(List<NSstep> nextSteps, NSstep sstep) {
        float maxY = nextSteps.get(0).y,minY = nextSteps.get(0).y;
        for(NSstep step : nextSteps) {
            if(maxY < step.y)
                maxY = step.y;
            if(minY > step.y)
                minY = step.y;
        }
        float by = (sstep.x + (nextSteps.get(0).x - sstep.x) / 2 /
                (Math.abs(sstep.lineId - nextSteps.get(0).lineId))) * this.getHeight();
        float bx = minY * this.getWidth();
        float ex = maxY * this.getWidth();
        float ey = by;
        float middleX = sstep.y * this.getWidth();
        float startX = sstep.y * this.getWidth();
        float startY = sstep.x * this.getHeight();
        if(startX < bx) {
            middleX = bx + (ex - bx) / 2f / (Math.abs(sstep.lineId - nextSteps.get(0).lineId));
        }
        else if(startX > ex) {
            middleX = bx + (ex - bx) * (1f - 1f / 2f / (Math.abs(sstep.lineId - nextSteps.get(0).lineId)));
        }
        myCanvas.drawLine(bx, by, ex, ey, linePaint);

        //跨层级
        if(Math.abs(nextSteps.get(0).lineId - sstep.lineId) > 1) {
            if(startX > middleX) {
                myCanvas.drawLine(startX - 2 * buttonRadius + 2, startY,  startX - buttonRadius, startY, linePaint);
                myCanvas.drawLine(startX - 2 * buttonRadius + 2, startY,  startX - 2 * buttonRadius + 2, by, linePaint);
                myCanvas.drawLine(startX - 2 * buttonRadius + 2, by,  middleX, by, linePaint);
            }
            else if(startX < middleX){
                myCanvas.drawLine(startX + 2 * buttonRadius - 2, startY,  startX + buttonRadius, startY, linePaint);
                myCanvas.drawLine(startX + 2 * buttonRadius - 2, startY,  startX + 2 * buttonRadius - 2, by, linePaint);
                myCanvas.drawLine(startX + 2 * buttonRadius - 2, by,  middleX, by, linePaint);
            }
            else {
                myCanvas.drawLine(startX - 2 * buttonRadius + 2, startY,  startX - buttonRadius, startY, linePaint);
                myCanvas.drawLine(startX - 2 * buttonRadius + 2, startY,  middleX - 2 * buttonRadius + 2, by, linePaint);

            }
        }
        else {
            myCanvas.drawLine(startX, startY + buttonRadius, middleX, by, linePaint);

        }
        for(NSstep step : nextSteps) {
            float y1 = by;
            float x1 = step.y * this.getWidth();
            float x2 = x1;
            float y2 = step.x * this.getHeight();
            drawAL(x1,y1, x2,y2 - buttonRadius);

            sstep.nextDrawLine.add(step.stepId);
            step.parentDrawLine.add(sstep.stepId);
        }
    }

    private void drawNextSteps(List<NSstep> nextSteps, NSstep sstep) {
        for(NSstep step : nextSteps) {
            float y1 = sstep.x * this.getHeight();
            float x1 = sstep.y * this.getWidth();
            float x2 = step.y * this.getWidth();
            float y2 = step.x * this.getHeight();
            //跨层级
            if(Math.abs(step.lineId - sstep.lineId) > 1) {
                if(x1 > x2) {
                    drawLeftBrokenAL(x1, y1, x2, y2);
                }
                else if(x1 < x2){
                    drawRightBrokenAL(x1, y1, x2, y2);
                }
                else {
                    myCanvas.drawLine(x1 - buttonRadius, y1, x1 - 2 * buttonRadius + 2, y1, linePaint);
                    drawAL(x2 - 2 * buttonRadius + 2, y2, x2 - buttonRadius, y2);
                    myCanvas.drawLine(x1 - 2 * buttonRadius + 2, y1, x2 - 2 * buttonRadius + 2, y2, linePaint);

                }
            }
            else {
                drawAL(x1, y1 + buttonRadius, x2, y2 - buttonRadius);

            }
            sstep.nextDrawLine.add(step.stepId);
            step.parentDrawLine.add(sstep.stepId);
        }
    }

    private boolean invisible2Draw(float x1, float y1, float x2, float y2) {
        if (((x2 < smoothMoveX && x1 < smoothMoveX)
                || (x2 > smoothMoveX + defaultWidth && x1 > smoothMoveX + defaultWidth))
                || ((y2 < smoothMoveY && y1 < smoothMoveY)
                || (y2 > smoothMoveY + defaultHeight
                && y1 > smoothMoveY + defaultHeight)))
            return false;
        else
            return true;
    }

    private void addButtonAndText() {
        Paint paint;
        String str;
        for (final NSstep step : nsSetPointValueToSteps.steplist) {
            float w = step.x * this.getHeight();
            float h = step.y * this.getWidth();
            if((h + buttonRadius < smoothMoveX || h - buttonRadius > smoothMoveX + defaultWidth)
                    && (w + buttonRadius < smoothMoveY || w - buttonRadius > smoothMoveY + defaultHeight))
                continue;
            if (step.type.equals("begin")) {
                paint = startFlowPaint;
                str = "开始";
            } else if (step.type.equals("end")) {
                paint = endFlowPaint;
                str = "结束";
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
                circlePaint.setColor(0xFF000000); // 边框颜色
            } else {
                circlePaint.setColor(0xFFf00); // 边框颜色
            }
            if(step.nodeStepType.equals("CallActivity"))
                drawCircleAndPlus(h, w, paint, circlePaint);
            else if(step.type.equals("merge"))
                drawRectangleAndWord(h, w, paint, textFlowPaint, circlePaint, str);
            else
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
                        if(currentZoom > 1)
                            zoom = smoothZoom * 1.1f;
                        else if(currentZoom > 0.6)
                            zoom = smoothZoom * 1.06f;
                        else if(currentZoom > 0.2)
                            zoom = smoothZoom * 1.04f;
                        else
                            zoom = smoothZoom * 1.03f;
                    }
                    else {
                        if(currentZoom > 1)
                            zoom = smoothZoom / 1.1f;
                        else if(currentZoom > 0.6)
                            zoom = smoothZoom / 1.06f;
                        else if(currentZoom > 0.2)
                            zoom = smoothZoom / 1.04f;
                        else
                            zoom = smoothZoom / 1.03f;
                    }
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
            if(currentZoom > 1) {
                minDistance = buttonRadius * 5;
            }
            else if(currentZoom > 0.6) {
                minDistance = buttonRadius * 4;
            }
            else if(currentZoom > 0.4) {
                minDistance = buttonRadius * 3;
            }
            else if(currentZoom > 0.2) {
                minDistance = buttonRadius * 2;
            }
            else if(currentZoom > 0.1) {
                minDistance = buttonRadius;
            }
            else {
                minDistance = Math.min(buttonRadius, defaultWidth / ((maxColumn + 1)));
            }
            ViewGroup.LayoutParams lp = getLayoutParams();
            float tempX = x / lp.width;
            float tempY = y / lp.height;
            float dx = smoothMoveX / lp.width;
            float dy = smoothMoveY / lp.height;
            lp.height = Math.max((maxRow + 1) * minDistance, defaultHeight);
            lp.width = Math.max((maxColumn + 1) * minDistance, defaultWidth);
            if (maxColumn > 100)
                lp.height = Math.max(lp.width, lp.height);
            if (flag == 1) {
                smoothMoveX = (tempX + dx) * lp.width - x;
                smoothMoveY = (tempY + dy) * lp.height - y;
            } else {
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
        for(NSstep step : nsSetPointValueToSteps.steplist) {
            float w = step.x * this.getHeight();
            float h = step.y * this.getWidth();
            if(w - buttonRadius <= y && w + buttonRadius >= y && h - buttonRadius <= x && h + buttonRadius >= x) {
                //子预案
                if(step.nodeStepType.equals("CallActivity")) {
                    Intent intent = new Intent();
                    intent.setClass(getContext(), ProcessMonitoringSubMissionActivity.class);
                    intent.putExtra("name", step.name);
                    intent.putExtra("stepId", step.stepId);
                    Gson gson = new Gson();
                    String jsonStr = gson.toJson(this.nsSetPointValueToSteps.subFlowChart); //将List转换成Json
                    SharedPreferences sp = getContext().getSharedPreferences("SP_MISSION_LIST", Activity.MODE_PRIVATE);//创建sp对象
                    SharedPreferences.Editor editor = sp.edit() ;
                    editor.putString("KEY_MISSION_LIST_DATA", jsonStr) ; //存入json串
                    editor.commit() ;  //提交
                    getContext().startActivity(intent);
                }
                else
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
            else if(step.statusId.equals(RealTimeTrackingStatus.NO_CHOICE_TO_EXECUTE))
                message += "执行状态：未选择执行";
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
            else if(step.statusId.equals(RealTimeTrackingStatus.NO_CHOICE_TO_EXECUTE)) {
                if(!message.equals(""))
                    message += "\n" + "执行状态：未选择执行";
                else
                    message += "执行状态：未选择执行";
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
            else if(step.statusId.equals(RealTimeTrackingStatus.NO_CHOICE_TO_EXECUTE)) {
                if(!message.equals(""))
                    message += "\n" + "执行状态：未选择执行";
                else
                    message += "执行状态：未选择执行";
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
            else if(step.statusId.equals(RealTimeTrackingStatus.NO_CHOICE_TO_EXECUTE)) {
                if(!message.equals(""))
                    message += "\n" + "执行状态：未选择执行";
                else
                    message += "执行状态：未选择执行";
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
    }

    /**
     * 设置画笔默认样式
     */
    public void setPaintDefaultStyle() {
        linePaint.setAntiAlias(true);
        linePaint.setColor(getResources().getColor(R.color.color_flow_line_red));
        linePaint.setStyle(Paint.Style.STROKE);
        if(smoothZoom < 0.5f)
            linePaint.setStrokeWidth(DisplayUtils.dp2px(0.8f));
        else
            linePaint.setStrokeWidth(DisplayUtils.dp2px(1.6f));

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
        circlePaint.setStrokeWidth(1);

        plusPaint.setAntiAlias(true);
        plusPaint.setColor(getResources().getColor(R.color.white));
        plusPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        plusPaint.setStrokeWidth(3f);
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

    /**
     * 画正方形和文字
     *
     * @param x      x坐标
     * @param y      y坐标
     */
    public void drawRectangleAndWord(float x, float y, Paint buttonPaint, Paint textPaint, Paint circlePaint, String str) {
        myCanvas.drawRect(x - buttonRadius, y - buttonRadius, x + buttonRadius, y + buttonRadius, buttonPaint);
        myCanvas.drawRect(x - buttonRadius, y - buttonRadius, x + buttonRadius, y + buttonRadius, circlePaint);
        Rect rect = new Rect();
        //返回包围整个字符串的最小的一个Rect区域
        textPaint.getTextBounds(str, 0, str.length(), rect);
        int strWidth = rect.width();
        int strHeight = rect.height();
        myCanvas.drawText(str, x - strWidth / 2f, y + strHeight / 2f, textPaint);
    }

    public void drawCircleAndPlus(float x, float y, Paint buttonPaint, Paint circlePaint) {
        myCanvas.drawCircle(x, y, buttonRadius, buttonPaint);
        myCanvas.drawCircle(x, y, buttonRadius, circlePaint);
        myCanvas.drawLine(x - buttonRadius / 3, y, x + buttonRadius / 3, y, plusPaint);
        myCanvas.drawLine(x, y - buttonRadius / 3, x, y + buttonRadius / 3, plusPaint);
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
        double H = buttonRadius / 3f; // 箭头高度
        double L = buttonRadius / 8f; // 底边的一半
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