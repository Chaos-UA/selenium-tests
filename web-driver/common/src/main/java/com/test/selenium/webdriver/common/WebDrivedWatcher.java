package com.test.selenium.webdriver.common;


import java.util.LinkedList;
import java.util.List;

public class WebDrivedWatcher implements WebDriver {
    private static final long STATUS_CATCH_INTERVAL = 500;

    private final WebDriver webDriver;
    private final long createdAt;
    private final Thread watcher;
    private final List<ProcessStatus> processStatuses = new LinkedList<>();
    private transient boolean isClosed;

    public WebDrivedWatcher(WebDriver webDriver) {
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
                        break;
                    }
                }
            }
        };
    }

    private void catchProcessStatus() {
        // ps -p 6305 -o pid,%cpu,rss
        double cpu = 5;
        int rss = 5;

        processStatuses.add(new ProcessStatus(rss, cpu, (int) (System.currentTimeMillis() - createdAt)));
    }

    @Override
    public void close() {
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
        webDriver.get(s);
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

    public static class ProcessStatus {

        /**
         * Sum of rss memory usage by process and all its child processes
         */
        private final int rss;

        /**
         * CPU usage in percent by process and all its child processes
         */
        private final double cpu;

        /**
         * Time since driver created
         */
        private final int time;

        public ProcessStatus(int rss, double cpu, int time) {
            this.rss = rss;
            this.cpu = cpu;
            this.time = time;
        }

        public int getRss() {
            return rss;
        }

        public double getCpu() {
            return cpu;
        }

        public int getTime() {
            return time;
        }
    }
}
