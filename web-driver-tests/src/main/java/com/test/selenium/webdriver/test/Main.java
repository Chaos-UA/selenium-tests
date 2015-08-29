package com.test.selenium.webdriver.test;


import com.test.selenium.webdriver.manager.WebDriverManager;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        System.out.println(Arrays.asList(WebDriverManager.getWebDriverFactories()));
    }
}
