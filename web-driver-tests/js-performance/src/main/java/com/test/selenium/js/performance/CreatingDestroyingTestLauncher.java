package com.test.selenium.js.performance;


import com.test.selenium.common.ApplicationConfiguration;

public class CreatingDestroyingTestLauncher {
    public static void main(String[] args) throws Throwable {
        ApplicationConfiguration.getApplicationContext().getBean(CreatingDestroyingTestService.class).test();
    }
}
