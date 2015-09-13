package com.test.selenium.webdriver.chrome;

import com.test.selenium.webdriver.common.*;
import org.openqa.selenium.*;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.os.CommandLine;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverService;

import java.awt.*;
import java.awt.Dimension;
import java.awt.Point;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class RemoteDriverWrapper implements com.test.selenium.webdriver.common.WebDriver {

    private final RemoteWebDriver webDriver;
    private final boolean canTakeScreenshot;

    private final int pid;
    private final String name;

    public RemoteDriverWrapper(RemoteWebDriver remoteWebDriver, DriverService service, String name) {
        this.name = name;
        this.webDriver = remoteWebDriver;
        this.canTakeScreenshot = remoteWebDriver instanceof TakesScreenshot;
        try {
            Field field = DriverService.class.getDeclaredField("process");
            field.setAccessible(true);
            CommandLine cmd = (CommandLine) field.get(service);
            field = CommandLine.class.getDeclaredField("process");
            field.setAccessible(true);
            Object unixProcess = field.get(cmd);
            field = unixProcess.getClass().getDeclaredField("executeWatchdog");
            field.setAccessible(true);
            Object executeWatchdog = field.get(unixProcess);
            Method method = executeWatchdog.getClass().getDeclaredMethod("getPID");
            method.setAccessible(true);
            String pid = (String) method.invoke(executeWatchdog);
            this.pid = Integer.parseInt(pid);
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
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
        return webDriver.getScreenshotAs(OutputType.BYTES);
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

    @Override
    public int getProcessId() {
        return this.pid;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<com.test.selenium.webdriver.common.WebElement> findElementsByXpath(String xpath) {
        return toElements(webDriver.findElementsByXPath(xpath));
    }

    @Override
    public com.test.selenium.webdriver.common.WebElement findElementByXpath(String xpath) {
        return toElement(webDriver.findElementByXPath(xpath));
    }


    public static com.test.selenium.webdriver.common.WebElement toElement(org.openqa.selenium.WebElement webElement) {
        return new WebElementImpl(webElement);
    }

    public static List<com.test.selenium.webdriver.common.WebElement> toElements(List<org.openqa.selenium.WebElement> webElements) {
        return webElements.stream().map(v -> new WebElementImpl(v)).collect(Collectors.toList());
    }

    public static class WebElementImpl implements com.test.selenium.webdriver.common.WebElement {

        private final org.openqa.selenium.WebElement webElement;

        public WebElementImpl(org.openqa.selenium.WebElement webElement) {
            this.webElement = webElement;
        }

        @Override
        public void click() {
            webElement.click();
        }

        @Override
        public void submit() {
            webElement.submit();
        }

        @Override
        public void sendKeys(CharSequence... charSequences) {
            webElement.sendKeys(charSequences);
        }

        @Override
        public void clear() {
            webElement.clear();
        }

        @Override
        public String getTagName() {
            return webElement.getTagName();
        }

        @Override
        public String getAttribute(String s) {
            return webElement.getAttribute(s);
        }

        @Override
        public boolean isSelected() {
            return webElement.isSelected();
        }

        @Override
        public boolean isEnabled() {
            return webElement.isEnabled();
        }

        @Override
        public String getText() {
            return webElement.getText();
        }

        @Override
        public List<com.test.selenium.webdriver.common.WebElement> findElementsByXpath(String xpath) {
            return toElements(webElement.findElements(By.xpath(xpath)));
        }

        @Override
        public com.test.selenium.webdriver.common.WebElement findElementByXpath(String xpath) {
            return toElement(webElement.findElement(By.xpath(xpath)));
        }

        public List<org.openqa.selenium.WebElement> findElements(By by) {
            return webElement.findElements(by);
        }

        public org.openqa.selenium.WebElement findElement(By by) {
            return webElement.findElement(by);
        }

        @Override
        public boolean isDisplayed() {
            return webElement.isDisplayed();
        }

        @Override
        public Point getLocation() {
            org.openqa.selenium.Point point = webElement.getLocation();
            return new Point(point.x, point.y);
        }

        @Override
        public java.awt.Dimension getSize() {
            org.openqa.selenium.Dimension d = webElement.getSize();
            return new java.awt.Dimension(d.width, d.height);
        }

        @Override
        public String getCssValue(String s) {
            return webElement.getCssValue(s);
        }

        @Override
        public String toString() {
            return webElement.toString();
        }
    }
}
