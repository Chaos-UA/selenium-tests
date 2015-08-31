package com.test.selenium.js.performance;


import com.test.selenium.common.ApplicationConfiguration;

public class InheritanceTestLauncher {

    public static void main(String[] args) throws Throwable {
        ApplicationConfiguration.getApplicationContext().getBean(InheritanceTestService.class).test();
    }
}
