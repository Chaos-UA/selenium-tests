package com.test.selenium.js.performance;

import com.test.selenium.common.GcUtil;
import com.test.selenium.visualizer.VisualizerService;
import com.test.selenium.visualizer.chart.bar.BarData;
import com.test.selenium.visualizer.chart.bar.Data;
import com.test.selenium.visualizer.chart.bar.Series;
import com.test.selenium.visualizer.chart.line.LineData;
import com.test.selenium.webdriver.common.LogUtil;
import com.test.selenium.webdriver.common.ProcessStatus;
import com.test.selenium.webdriver.common.WebDriverFactory;
import com.test.selenium.webdriver.common.WebDriverWatcher;
import com.test.selenium.webdriver.manager.WebDriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PageSurfingTestService {

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
        List<BrowserResult> results = new ArrayList<>();
        for (WebDriverFactory webDriverFactory : webDriverService.getWebDriverFactories()) {
            GcUtil.gc();
            try (WebDriverWatcher webDriverWatcher = new WebDriverWatcher(webDriverFactory.createWebDriver(), 100)) {
                LogUtil.getLogger().info("Testing webdriver: " + webDriverWatcher.getName());
                List<Series> serieses = new ArrayList<>(LinksCounterTestService.PAGES.size());
                /*
                BrowserResult browserResult = new BrowserResult(webDriverWatcher.getName());
                results.add(browserResult);
                */
                int links = 0;
                for (String pageUrl : LinksCounterTestService.PAGES) {
                    //LogUtil.getLogger().info("Visiting page: " + pageUrl);
                    long beforePageLoadTime = System.currentTimeMillis();
                    webDriverWatcher.get(pageUrl);

                    /*
                    long urls = webDriverWatcher.findElementsByXpath("//a[@href]")
                            .stream()
                            .map(v -> v.getAttribute("href").trim())
                                    //.filter(v -> v.startsWith("http"))
                            .count();
                    links += urls;
                    LogUtil.getLogger().info(String.format("Found %s links on site[%s]", urls, pageUrl));
                    */

                     /*
                    Page page = new Page(pageUrl);
                    browserResult.getPages().add(page);
                    // visit all urls on web site


                    while (urls.size() > VISIT_FIRST_URLS) {
                        urls.remove(urls.size()-1);
                    }
                    for (int i = 0; i < urls.size(); i++) {
                        String url = urls.get(i);
                        LogUtil.getLogger().info(
                                String.format("[%s] Visiting found url: %s [%s of %s]",
                                webDriverWatcher.getName(),
                                url,
                                i + 1,
                                urls.size())
                        );
                        page.getPages().add(url);
                        webDriverWatcher.get(url);
                    }
                    */
                    serieses.add(new Series(
                                    pageUrl.replaceFirst("/home/volodymyr/projects/unkur-research/selenium/web-driver-tests/js-performance/src/main/resources/web-surfing-test-local-pages", "..."),
                                    System.currentTimeMillis() - beforePageLoadTime)
                    );
                }
                results.add(new BrowserResult(webDriverWatcher.getName(), links));
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


        BrowserResult result = results.get(0);

        boolean allEquals = true;
        for (BrowserResult browserResult : results) {
            if (result.foundLinks != browserResult.foundLinks) {
                allEquals = false;
            }
        }
        System.out.println(results);
        if (allEquals) {
            LogUtil.getLogger().info("All results equals");
        }
        else {
            LogUtil.getLogger().error("Not all results equals");
        }


        visualizerService.showAll(
                visualizerService.buildStackedBarChart(surfingData),
                visualizerService.buildLineChart(memoryChart),
                visualizerService.buildLineChart(cpuChart),
                visualizerService.buildLineChart(processesChart)
        );
        //visualizerService.showStackedBarChar(surfingData);
        //visualizerService.showLineChart(memoryChart);
        //visualizerService.showLineChart(cpuChart);
        //visualizerService.showLineChart(processesChart);
        // check if all results equals



    }

    private static class BrowserResult {
        private final String browser;
        private final int foundLinks;

        public BrowserResult(String browser, int foundLinks) {
            this.browser = browser;
            this.foundLinks = foundLinks;
        }

        @Override
        public String toString() {
            return "BrowserResult{" +
                    "browser='" + browser + '\'' +
                    ", foundLinks=" + foundLinks +
                    '}';
        }
    }
}
