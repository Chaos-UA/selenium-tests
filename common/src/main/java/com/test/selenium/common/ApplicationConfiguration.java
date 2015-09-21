package com.test.selenium.common;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.test.selenium")
public class ApplicationConfiguration {
    private static final ApplicationContext APPLICATION_CONTEXT = new AnnotationConfigApplicationContext(ApplicationConfiguration.class);

    public static void initApp() {

    }



    public static ApplicationContext getApplicationContext() {
        return APPLICATION_CONTEXT;
    }
}
