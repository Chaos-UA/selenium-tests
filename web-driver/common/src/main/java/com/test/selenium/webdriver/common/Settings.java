package com.test.selenium.webdriver.common;


import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Settings {
    private Dimension windowSize;
    private Map<String, Object> parameters;
    private Map<String, String> environment = new HashMap<>();

    {
        environment.put("DISPLAY", ":1.0");
    }

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

    public Map<String, String> getEnvironment() {
        return environment;
    }

    public void setEnvironment(Map<String, String> environment) {
        this.environment = environment;
    }
}
