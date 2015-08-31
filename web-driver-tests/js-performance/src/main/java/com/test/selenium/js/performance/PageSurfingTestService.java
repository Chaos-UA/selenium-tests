package com.test.selenium.js.performance;


import com.test.selenium.common.GcUtil;
import com.test.selenium.visualizer.VisualizerService;
import com.test.selenium.visualizer.chart.bar.BarData;
import com.test.selenium.visualizer.chart.bar.Data;
import com.test.selenium.visualizer.chart.bar.Series;
import com.test.selenium.visualizer.chart.line.LineData;
import com.test.selenium.webdriver.common.ProcessStatus;
import com.test.selenium.webdriver.common.WebDriverFactory;
import com.test.selenium.webdriver.common.WebDriverWatcher;
import com.test.selenium.webdriver.manager.WebDriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class PageSurfingTestService {
    private static final List<String> PAGES = Arrays.asList(
            "http://unkur.com/",
            "http://i-butler.eu/en/",
            "http://habrahabr.ru/",
            "https://www.youtube.com/",
            "http://www.ebay.com/",
            "https://www.google.com",
            "http://www.yandex.ua/",
            "https://maps.yandex.ua/144/lvov/",
            "https://www.google.com.ua/maps/place/Lviv,+Lviv+Oblast/@49.832689,24.0122355,12z/data=!3m1!4b1!4m2!3m1!1s0x473add7c09109a57:0x4223c517012378e2",
            "https://www.google.com.ua/search?q=internet&biw=1855&bih=949&source=lnms&tbm=isch&sa=X&ved=0CAYQ_AUoAWoVChMI_PPc-O7SxwIVhtssCh1M5gy2"
    );

    @Autowired
    private VisualizerService visualizerService;

    @Autowired
    private WebDriverService webDriverService;

    public void test() throws Throwable {
        BarData surfingData = new BarData();
        surfingData.setTitle("Web surfing (processing time)");
        surfingData.setyTitle("Time ms");
        LineData memoryChart = new LineData("Time ms", "RSS kb", "Memory usage");
        LineData cpuChart = new LineData("Time ms", "CPU%", "CPU% usage");
        LineData processesChart = new LineData("Time ms", "Processes", "Processes");
        for (WebDriverFactory webDriverFactory : webDriverService.getWebDriverFactories()) {
            GcUtil.gc();
            try (WebDriverWatcher webDriverWatcher = new WebDriverWatcher(webDriverFactory.createWebDriver())) {
                List<Series> serieses = new ArrayList<>(PAGES.size());
                for (String pageUrl : PAGES) {
                    long beforePageLoadTime = System.currentTimeMillis();
                    webDriverWatcher.get(pageUrl);
                    serieses.add(new Series(pageUrl, System.currentTimeMillis() - beforePageLoadTime));
                }
                surfingData.getDatas().add(new Data(webDriverWatcher.getName(), serieses));
                webDriverWatcher.close();
                // memory
                webDriverWatcher.getProcessStatuses();
                LineData.Data data = new LineData.Data();
                memoryChart.getDatas().add(data);
                data.setName(webDriverWatcher.getName());
                for (ProcessStatus processStatus : webDriverWatcher.getProcessStatuses()) {
                    data.getSeries().add(new LineData.Series(processStatus.getTime(), processStatus.getUss()));
                }
                // cpu
                data = new LineData.Data();
                cpuChart.getDatas().add(data);
                data.setName(webDriverWatcher.getName());
                for (ProcessStatus processStatus : webDriverWatcher.getProcessStatuses()) {
                    data.getSeries().add(new LineData.Series(processStatus.getTime(), processStatus.getCpu()));
                }
                // processes
                data = new LineData.Data();
                processesChart.getDatas().add(data);
                data.setName(webDriverWatcher.getName());
                for (ProcessStatus processStatus : webDriverWatcher.getProcessStatuses()) {
                    data.getSeries().add(new LineData.Series(processStatus.getTime(), processStatus.getProcesses()));
                }
            }
        }
        visualizerService.showStackedBarChar(surfingData);
        visualizerService.showLineChart(memoryChart);
        visualizerService.showLineChart(cpuChart);
        visualizerService.showLineChart(processesChart);
    }
}
