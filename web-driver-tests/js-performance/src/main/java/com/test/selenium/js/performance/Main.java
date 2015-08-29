package com.test.selenium.js.performance;


import com.test.selenium.common.ApplicationConfiguration;

public class Main {

    public static void main(String[] args) {
        ApplicationConfiguration.getApplicationContext().getBean(InheritanceTestService.class).test();
    }
}
