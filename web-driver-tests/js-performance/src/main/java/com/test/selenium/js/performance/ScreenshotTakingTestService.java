package com.test.selenium.js.performance;

import com.test.selenium.common.GcUtil;
import com.test.selenium.visualizer.VisualizerService;
import com.test.selenium.visualizer.chart.bar.BarData;
import com.test.selenium.visualizer.chart.bar.Data;
import com.test.selenium.visualizer.chart.bar.Series;
import com.test.selenium.webdriver.common.WebDriverFactory;
import com.test.selenium.webdriver.common.WebDriverWatcher;
import com.test.selenium.webdriver.manager.WebDriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ScreenshotTakingTestService {
    public static final List<String> PAGES = Arrays.asList(
            "http://unkur.com/",
            "https://github.com/showcases/software-development-tools/",
            "https://en.wikipedia.org/wiki/List_of_countries_and_dependencies_and_their_capitals_in_native_languages/",
            "http://habrahabr.ru/",
            "https://www.youtube.com/",
            "http://www.ebay.com/",
            "https://www.google.com",
            "http://www.yandex.ua/"
    );

    @Autowired
    private VisualizerService visualizerService;

    @Autowired
    private WebDriverService webDriverService;


    public void test() throws Throwable {

        BarData surfingData = new BarData();
        surfingData.setTitle("Screenshot capturing time");
        surfingData.setyTitle("Time ms");
        for (WebDriverFactory webDriverFactory : webDriverService.getWebDriverFactories()) {
            GcUtil.gc();
            try (WebDriverWatcher webDriverWatcher = new WebDriverWatcher(webDriverFactory.createWebDriver())) {
                if (true) {
                    webDriverWatcher.get("http://unkur.com");
                    String dir = System.getProperty("user.home") + "/Downloads/selenium-test/screenshots";// check screenshots manually
                    new File(dir).mkdirs();
                    try (FileOutputStream out = new FileOutputStream(dir + "/" + webDriverWatcher.getName() + ".png")) {
                        out.write(webDriverWatcher.getScreenshot());
                    }
                    //continue;
                }
                List<Series> serieses = new ArrayList<>(PAGES.size());
                for (String pageUrl : PAGES) {
                    GcUtil.gc();
                    webDriverWatcher.get(pageUrl);
                    long beforeCaptureScreenshotTime = System.currentTimeMillis();
                    byte[] screenshot = webDriverWatcher.getScreenshot();
                    serieses.add(new Series(pageUrl, System.currentTimeMillis() - beforeCaptureScreenshotTime));
                    /*
                    try (FileOutputStream out = new FileOutputStream("/home/volodymyr/Downloads/screenshot-"+webDriverWatcher.getName()+".png")) {
                        out.write(screenshot);
                    }
                    */
                }
                surfingData.getDatas().add(new Data(webDriverWatcher.getName(), serieses));
            }
        }
        visualizerService.showStackedBarChar(surfingData);
    }
}
