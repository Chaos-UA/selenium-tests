package com.test.selenium.webdriver.common;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtil {
    private static final Logger LOGGER = LogManager.getLogger("Application-Logger");

    public static Logger getLogger() {
        return LOGGER;
    }
}
