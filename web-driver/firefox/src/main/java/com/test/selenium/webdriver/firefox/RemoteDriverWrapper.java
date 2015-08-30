package com.test.selenium.webdriver.firefox;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.os.CommandLine;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.service.DriverService;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class RemoteDriverWrapper implements com.test.selenium.webdriver.common.WebDriver {

    private final RemoteWebDriver webDriver;
    private final boolean canTakeScreenshot;

    private final int pid;

    public RemoteDriverWrapper(RemoteWebDriver remoteWebDriver, DriverService service) {
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

    @Override
    public int getProcessId() {
        return this.pid;
    }

    @Override
    public String getName() {
        return "Firefox";
    }
}
