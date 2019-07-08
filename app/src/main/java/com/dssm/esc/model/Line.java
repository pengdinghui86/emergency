package com.dssm.esc.model;

import java.util.ArrayList;
import java.util.List;

public class Line {
    private Point point;
    private Point startPoint;
    private List<Point> endPoints = new ArrayList<>();

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point startPoint) {
        this.startPoint = startPoint;
    }

    public List<Point> getEndPoints() {
        return endPoints;
    }

    public void setEndPoints(List<Point> endPoints) {
        this.endPoints = endPoints;
    }
}
