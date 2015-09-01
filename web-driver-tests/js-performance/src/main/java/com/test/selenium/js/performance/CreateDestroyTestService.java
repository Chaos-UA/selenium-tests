package com.test.selenium.js.performance;


import com.test.selenium.common.GcUtil;
import com.test.selenium.visualizer.VisualizerService;
import com.test.selenium.visualizer.chart.bar.BarData;
import com.test.selenium.visualizer.chart.bar.Data;
import com.test.selenium.visualizer.chart.bar.Series;
import com.test.selenium.visualizer.chart.line.LineData;
import com.test.selenium.webdriver.common.ProcessStatus;
import com.test.selenium.webdriver.common.WebDriver;
import com.test.selenium.webdriver.common.WebDriverFactory;
import com.test.selenium.webdriver.common.WebDriverWatcher;
import com.test.selenium.webdriver.manager.WebDriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class CreateDestroyTestService {
    private static final List<String> PAGES = Arrays.asList(
            "http://unkur.com/",
            "http://i-butler.eu/en/",
            "http://habrahabr.ru/",
            "https://www.youtube.com/",
            "http://www.ebay.com/",
            "https://www.google.com",
            "http://www.yandex.ua/"
    );

    @Autowired
    private WebDriverService webDriverService;

    @Autowired
    private VisualizerService visualizerService;


    public void test() throws Throwable {
        LineData creationLine = new LineData("Attempt №", "Creation time", "WebDriver creation test");
        LineData destroyingLine = new LineData("Attempt №", "Destroying time", "WebDriver destroying test");
        for (WebDriverFactory webDriverFactory : webDriverService.getWebDriverFactories()) {
            GcUtil.gc();
            LineData.Data dataCreation = new LineData.Data();
            creationLine.getDatas().add(dataCreation);
            WebDriver webDriver = webDriverFactory.createWebDriver();
            dataCreation.setName(webDriver.getName());
            LineData.Data dataDestroying = new LineData.Data();
            destroyingLine.getDatas().add(dataDestroying);
            dataDestroying.setName(webDriver.getName());
            webDriver.close();
            for (int i = 0; i < 100; i++) {
                GcUtil.gc();
                try {
                    long startTime = System.currentTimeMillis();
                    webDriver = webDriverFactory.createWebDriver();
                    dataCreation.getSeries().add(new LineData.Series(i, System.currentTimeMillis() - startTime));
                    webDriver.get(PAGES.get(i % PAGES.size()));
                } finally {
                    long startTime = System.currentTimeMillis();
                    webDriver.close();
                    dataDestroying.getSeries().add(new LineData.Series(i, System.currentTimeMillis() - startTime));
                }
            }
        }
        visualizerService.showLineChart(creationLine);
        visualizerService.showLineChart(destroyingLine);
    }
}
