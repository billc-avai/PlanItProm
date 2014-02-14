package com.sevendesign.planitprom.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mib on 20.08.13.
 */
public class TimelineCategory extends TimelineItem {

    private String name;
    private List<TimelinePoint> pointsList;

    public TimelineCategory() {
        name = "";
        pointsList = new ArrayList<TimelinePoint>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public List<TimelinePoint> getPointsList() {
        return pointsList;
    }

    public void setPointsList(List<TimelinePoint> points) {
        this.pointsList = points;
    }

}
