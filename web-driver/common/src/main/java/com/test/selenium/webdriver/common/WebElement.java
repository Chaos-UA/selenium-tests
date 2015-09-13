package com.test.selenium.webdriver.common;

import java.awt.*;
import java.util.List;

public interface WebElement {
    void click();

    void submit();

    void sendKeys(CharSequence... var1);

    void clear();

    String getTagName();

    String getAttribute(String var1);

    boolean isSelected();

    boolean isEnabled();

    String getText();

    List<WebElement> findElementsByXpath(String xpath);

    default WebElement findElementByXpath(String xpath) {
        List<WebElement> elements = findElementsByXpath(xpath);
        if (elements.size() != 1) {
            throw new RuntimeException("No element by xpath");
        }
        return elements.get(0);
    }

    boolean isDisplayed();

    Point getLocation();

    Dimension getSize();

    String getCssValue(String var1);
}
