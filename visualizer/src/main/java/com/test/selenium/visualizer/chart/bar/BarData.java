package com.test.selenium.visualizer.chart.bar;

import java.util.ArrayList;
import java.util.List;

public class BarData {
    private String title;
    private String yTitle;
    private List<Data> datas = new ArrayList<>();

    public BarData(String title, String yTitle, List<Data> datas) {
        this.title = title;
        this.yTitle = yTitle;
        this.datas = datas;
    }

    public BarData() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Data> getDatas() {
        return datas;
    }

    public void setDatas(List<Data> datas) {
        this.datas = datas;
    }

    public String getyTitle() {
        return yTitle;
    }

    public void setyTitle(String yTitle) {
        this.yTitle = yTitle;
    }
}
