package com.test.selenium.webdriver.common;

import java.io.Closeable;

public interface WebDriver extends Closeable {
    void close();
    boolean canTakeScreenshot();
    byte[] getScreenshot();

    Object executeScript(String s, Object... objects);

    Object executeAsyncScript(String s, Object... objects);

    void get(String s);

    String getCurrentUrl();

    String getTitle();

    String getPageSource() ;
}
