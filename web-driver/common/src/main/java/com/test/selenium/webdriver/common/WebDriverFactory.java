package com.test.selenium.webdriver.common;


public abstract class WebDriverFactory {
    public WebDriverFactory() {
        try (WebDriver webDriver = createWebDriver(null)) {
            // test by creating and destroying instance
        }
    }

    public abstract String getName();

    /**
     * @param settings null to use default
     */
    public abstract WebDriver createWebDriver(Settings settings);
}
