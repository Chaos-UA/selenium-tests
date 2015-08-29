package com.test.selenium.visualizer.chart.bar;

import java.io.Serializable;

public class Series implements Serializable {
    private String name;
    private Number value;

    public Series(String name, Number value) {
        this.name = name;
        this.value = value;
    }

    public Series() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Number getValue() {
        return value;
    }

    public void setValue(Number value) {
        this.value = value;
    }
}
