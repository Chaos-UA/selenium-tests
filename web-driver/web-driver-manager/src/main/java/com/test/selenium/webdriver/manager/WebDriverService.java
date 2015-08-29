package com.test.selenium.webdriver.manager;

import com.test.selenium.webdriver.common.WebDriverFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class WebDriverService {

    private List<WebDriverFactory> webDriverFactories = Arrays.asList(WebDriverManager.getWebDriverFactories());

    public List<WebDriverFactory> getWebDriverFactories() {
        return webDriverFactories;
    }
}
