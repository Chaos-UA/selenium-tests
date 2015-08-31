package com.test.selenium.webdriver.manager;

import com.test.selenium.common.GcUtil;
import com.test.selenium.webdriver.common.WebDriverFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class WebDriverService {

    private List<WebDriverFactory> webDriverFactories = Arrays.asList(WebDriverManager.getWebDriverFactories());

    @PostConstruct
    protected void init() {
        // allocate max memory immediately to not allocate it during tests
        int mbytes = 2000;
        byte[] bytes = new byte[1024 * 1024 * mbytes];
        bytes = null;
        GcUtil.gc();
    }

    public List<WebDriverFactory> getWebDriverFactories() {
        return webDriverFactories;
    }
}
