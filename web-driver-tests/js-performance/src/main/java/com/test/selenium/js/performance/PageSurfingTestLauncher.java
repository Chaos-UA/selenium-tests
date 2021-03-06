package com.test.selenium.js.performance;


import com.test.selenium.common.ApplicationConfiguration;
import com.test.selenium.webdriver.common.LogUtil;

public class PageSurfingTestLauncher {
    public static void main(String[] args) {
        try {
            ApplicationConfiguration.getApplicationContext().getBean(PageSurfingTestService.class).test();
            System.exit(0);
        }
        catch (Throwable t) {
            LogUtil.getLogger().error("Exception", t);
            System.exit(1);
        }
    }
}
