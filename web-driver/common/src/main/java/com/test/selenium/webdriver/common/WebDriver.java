package com.test.selenium.webdriver.common;

import java.io.Closeable;
import java.util.List;

public interface WebDriver extends Closeable {
    void close();
    boolean canTakeScreenshot();
    byte[] getScreenshot();

    Object executeScript(String s, Object... objects);

    Object executeAsyncScript(String s, Object... objects);

    void get(String s);

    String getCurrentUrl();

    String getTitle();

    String getPageSource();

    int getProcessId();

    String getName();

    List<WebElement> findElementsByXpath(String xpath);

    default WebElement findElementByXpath(String xpath) {
        List<WebElement> elements = findElementsByXpath(xpath);
        if (elements.size() != 1) {
            throw new RuntimeException("No element by xpath");
        }
        return elements.get(0);
    }
}
