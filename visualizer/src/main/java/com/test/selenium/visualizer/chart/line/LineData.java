package com.test.selenium.visualizer.chart.line;


import java.util.ArrayList;
import java.util.List;

public class LineData {
    private final String xLabel;
    private final String yLabel;
    private final String title;
    private final List<Data> datas = new ArrayList<>();

    public LineData(String xLabel, String yLabel, String title) {
        this.xLabel = xLabel;
        this.yLabel = yLabel;
        this.title = title;
    }

    public String getxLabel() {
        return xLabel;
    }

    public String getyLabel() {
        return yLabel;
    }


    public List<Data> getDatas() {
        return datas;
    }

    public String getTitle() {
        return title;
    }

    public static class Data {
        private String name;
        private final List<Series> series = new ArrayList<>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Series> getSeries() {
            return series;
        }
    }

    public static class Series {
        private final Number x;
        private final Number y;

        public Series(Number x, Number y) {
            this.x = x;
            this.y = y;
        }

        public Number getX() {
            return x;
        }

        public Number getY() {
            return y;
        }
    }

}
