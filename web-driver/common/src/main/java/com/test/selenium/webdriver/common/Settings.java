package com.test.selenium.webdriver.common;


import java.awt.*;
import java.util.Map;

public class Settings {
    private Dimension windowSize;
    private Map<String, Object> parameters;

    public Dimension getWindowSize() {
        return windowSize;
    }

    public void setWindowSize(Dimension windowSize) {
        this.windowSize = windowSize;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
}
