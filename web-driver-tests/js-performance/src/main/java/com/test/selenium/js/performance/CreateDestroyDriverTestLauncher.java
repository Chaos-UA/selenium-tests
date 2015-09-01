package com.test.selenium.js.performance;


import com.test.selenium.common.ApplicationConfiguration;

public class CreateDestroyDriverTestLauncher {
    public static void main(String[] args) throws Throwable {
        ApplicationConfiguration.getApplicationContext().getBean(CreateDestroyTestService.class).test();
    }
}
