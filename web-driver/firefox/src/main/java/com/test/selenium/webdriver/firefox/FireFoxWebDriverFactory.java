package com.test.selenium.webdriver.firefox;

import com.test.selenium.webdriver.common.Settings;
import com.test.selenium.webdriver.common.WebDriver;
import com.test.selenium.webdriver.common.WebDriverFactory;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.io.File;


public class FireFoxWebDriverFactory extends WebDriverFactory {

    @Override
    public String getName() {
        return "firefox-driver";
    }

    @Override
    public WebDriver createWebDriver() {
        FirefoxProfile firefoxProfile = new FirefoxProfile();
        FirefoxBinary firefoxBinary = new FirefoxBinary(new File("/usr/bin/firefox"));
        FirefoxDriver firefoxDriver = new FirefoxDriver(firefoxBinary, firefoxProfile);
        return new RemoteDriverWrapper(firefoxDriver);
    }
}
