package com.test.selenium.webdriver.common;


import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class WebDriverWatcher implements WebDriver {
    private static final long STATUS_CATCH_INTERVAL = 200;

    private final WebDriver webDriver;
    private final long createdAt;
    private final Thread watcher;
    private final List<ProcessStatus> processStatuses = new LinkedList<>();
    private transient boolean isClosed;

    public WebDriverWatcher(WebDriver webDriver) {
        this.webDriver = webDriver;
        this.createdAt = System.currentTimeMillis();
        watcher = new Thread() {

            @Override
            public void run() {
                while (!isClosed) {
                    try {
                        catchProcessStatus();
                        Thread.sleep(STATUS_CATCH_INTERVAL);
                    } catch (Throwable e) {
                        LogUtil.getLogger().error("Exception during getting process status", e);
                    }
                }
            }
        };
        watcher.start();
        catchProcessStatus();
    }

    public List<ProcessStatus> getProcessStatuses() {
        return processStatuses;
    }

    private synchronized void catchProcessStatus() {
        if (isClosed) {
            return;
        }
        processStatuses.add(ProcessUtil.getProcessStatusWithSubprocesses(
                getProcessId(),
                (int) (System.currentTimeMillis() - createdAt))
        );
    }

    @Override
    public synchronized void close() {
        isClosed = true;
        webDriver.close();
    }

    @Override
    public boolean canTakeScreenshot() {
        return webDriver.canTakeScreenshot();
    }

    @Override
    public byte[] getScreenshot() {
        return webDriver.getScreenshot();
    }

    @Override
    public Object executeScript(String s, Object... objects) {
        return webDriver.executeScript(s, objects);
    }

    @Override
    public Object executeAsyncScript(String s, Object... objects) {
        return webDriver.executeAsyncScript(s, objects);
    }

    @Override
    public void get(String s) {
        webDriver.get("about:blank");
        webDriver.get(s);
        if (webDriver.getCurrentUrl().equals("about:blank")) {
            throw new RuntimeException("about:blank");
        }
    }

    @Override
    public String getCurrentUrl() {
        return webDriver.getCurrentUrl();
    }

    @Override
    public String getTitle() {
        return webDriver.getTitle();
    }

    @Override
    public String getPageSource() {
        return webDriver.getPageSource();
    }

    @Override
    public int getProcessId() {
        return webDriver.getProcessId();
    }

    @Override
    public String getName() {
        return webDriver.getName();
    }
}
