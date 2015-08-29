package com.test.selenium.js.performance;

import com.test.selenium.visualizer.VisualizerService;
import com.test.selenium.webdriver.manager.WebDriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InheritanceTestService {

    @Autowired
    private VisualizerService visualizerService;

    @Autowired
    private WebDriverService webDriverService;

    public void test() {

    }
}
