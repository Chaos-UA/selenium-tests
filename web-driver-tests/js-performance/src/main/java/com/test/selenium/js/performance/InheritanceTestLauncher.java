package com.test.selenium.js.performance;


import com.test.selenium.common.ApplicationConfiguration;
import com.test.selenium.webdriver.common.LogUtil;

public class InheritanceTestLauncher {

    public static void main(String[] args) throws Throwable {
        try {
            ApplicationConfiguration.getApplicationContext().getBean(InheritanceTestService.class).test();
            System.exit(0);
        }
        catch (Throwable t) {
            LogUtil.getLogger().error("Exception", t);
            System.exit(1);
        }
    }
}
