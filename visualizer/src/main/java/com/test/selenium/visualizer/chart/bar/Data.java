package com.test.selenium.visualizer.chart.bar;

import java.util.ArrayList;
import java.util.List;

public class Data {
    private String name;
    private List<Series> seriesData = new ArrayList<>();

    public Data(String name, List<Series> seriesData) {
        this.name = name;
        this.seriesData = seriesData;
    }

    public Data() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Series> getSeriesData() {
        return seriesData;
    }

    public void setSeriesData(List<Series> seriesData) {
        this.seriesData = seriesData;
    }
}
