package com.test.selenium.webdriver.common;


public abstract class WebDriverFactory {
    public WebDriverFactory() {
        try (WebDriver webDriver = createWebDriver()) {
            // test by creating and destroying instance
        }
    }

    public abstract String getName();

    public abstract WebDriver createWebDriver();
}
