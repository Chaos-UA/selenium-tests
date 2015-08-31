package com.test.selenium.webdriver.common;


import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public abstract class WebDriverFactory {

    protected Settings settings = new Settings();


    {
        settings.setWindowSize(new Dimension(1336, 768));
    }

    public WebDriverFactory() {
        try (WebDriver webDriver = createWebDriver()) {
            // test by creating and destroying instance
        }
    }

    public abstract String getName();

    public abstract WebDriver createWebDriver();
}
