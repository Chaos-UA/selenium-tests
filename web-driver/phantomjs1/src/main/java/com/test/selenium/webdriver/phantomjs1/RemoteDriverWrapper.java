package com.test.selenium.webdriver.phantomjs1;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;


public class RemoteDriverWrapper implements com.test.selenium.webdriver.common.WebDriver {

    private final RemoteWebDriver webDriver;
    private final boolean canTakeScreenshot;

    public RemoteDriverWrapper(RemoteWebDriver remoteWebDriver) {
        this.webDriver = remoteWebDriver;
        this.canTakeScreenshot = remoteWebDriver instanceof TakesScreenshot;
    }

    @Override
    public void close() {
        this.webDriver.quit();
    }

    @Override
    public boolean canTakeScreenshot() {
        return canTakeScreenshot;
    }

    @Override
    public byte[] getScreenshot() {
        return ((TakesScreenshot) this).getScreenshotAs(OutputType.BYTES);
    }

    @Override
    public Object executeScript(String s, Object... objects) {
        return this.webDriver.executeScript(s, objects);
    }

    @Override
    public Object executeAsyncScript(String s, Object... objects) {
        return this.webDriver.executeAsyncScript(s, objects);
    }

    @Override
    public void get(String s) {
        this.webDriver.get(s);
    }

    @Override
    public String getCurrentUrl() {
        return this.webDriver.getCurrentUrl();
    }

    @Override
    public String getTitle() {
        return this.webDriver.getTitle();
    }


    @Override
    public String getPageSource() {
        return this.webDriver.getPageSource();
    }
}
