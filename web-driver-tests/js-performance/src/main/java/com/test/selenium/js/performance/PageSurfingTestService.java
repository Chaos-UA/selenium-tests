package com.test.selenium.js.performance;


import com.test.selenium.common.GcUtil;
import com.test.selenium.visualizer.VisualizerService;
import com.test.selenium.visualizer.chart.bar.BarData;
import com.test.selenium.visualizer.chart.bar.Data;
import com.test.selenium.visualizer.chart.bar.Series;
import com.test.selenium.visualizer.chart.line.LineData;
import com.test.selenium.webdriver.common.*;
import com.test.selenium.webdriver.manager.WebDriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PageSurfingTestService {
    private static final int VISIT_FIRST_URLS = 5;
    public static final List<String> PAGES = Arrays.asList(
            "http://unkur.com/",
            "https://github.com/showcases/software-development-tools/",
            "https://en.wikipedia.org/wiki/List_of_countries_and_dependencies_and_their_capitals_in_native_languages/",
            "http://habrahabr.ru/",
            "http://www.ebay.com/",
            "https://www.yandex.com/",
            "https://www.google.com",
            "https://www.youtube.com/",
            "https://www.google.com.ua/search?q=images&es_sm=93&biw=1855&bih=995&tbm=isch&source=lnms&sa=X&ved=0CAYQ_AUoAWoVChMIhueMpPr0xwIVg_1yCh3_TAx5&dpr=1"
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
        //List<BrowserResult> results = new ArrayList<>();
        for (WebDriverFactory webDriverFactory : webDriverService.getWebDriverFactories()) {
            GcUtil.gc();
            try (WebDriverWatcher webDriverWatcher = new WebDriverWatcher(webDriverFactory.createWebDriver())) {
                LogUtil.getLogger().info("Testing webdriver: " + webDriverWatcher.getName());
                List<Series> serieses = new ArrayList<>(PAGES.size());
                /*
                BrowserResult browserResult = new BrowserResult(webDriverWatcher.getName());
                results.add(browserResult);
                */
                for (String pageUrl : PAGES) {
                    //LogUtil.getLogger().info("Visiting page: " + pageUrl);
                    long beforePageLoadTime = System.currentTimeMillis();
                    webDriverWatcher.get(pageUrl);

                    long urls = webDriverWatcher.findElementsByXpath("//a[@href]")
                            .stream()
                            .map(v -> v.getAttribute("href").trim())
                            .filter(v -> v.startsWith("http"))
                            .count();
                    LogUtil.getLogger().info(String.format("Found %s urls on site[%s]", urls, pageUrl));

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
        // check if all results equals
          /*
        BrowserResult result = results.get(0);

        boolean allEquals = true;
        for (BrowserResult browserResult : results) {
            if (!result.equals(browserResult)) {
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
        */
    }

    private static class BrowserResult {
        private final String browser;
        private final List<Page> pages = new ArrayList<>();

        public BrowserResult(String browser) {
            this.browser = browser;
        }

        public String getBrowser() {
            return browser;
        }

        public List<Page> getPages() {
            return pages;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            BrowserResult that = (BrowserResult) o;

            if (browser != null ? !browser.equals(that.browser) : that.browser != null) return false;
            return !(pages != null ? !pages.equals(that.pages) : that.pages != null);

        }

        @Override
        public int hashCode() {
            int result = browser != null ? browser.hashCode() : 0;
            result = 31 * result + (pages != null ? pages.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "BrowserResult{" +
                    "browser='" + browser + '\'' +
                    ", pages=" + pages +
                    '}';
        }
    }

    private static class Page {
        private final String page;
        private final List<String> pages = new ArrayList<>();

        public Page(String page) {
            this.page = page;
        }

        public String getPage() {
            return page;
        }

        public List<String> getPages() {
            return pages;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Page page1 = (Page) o;

            if (getPage() != null ? !getPage().equals(page1.getPage()) : page1.getPage() != null) return false;
            return !(getPages() != null ? !getPages().equals(page1.getPages()) : page1.getPages() != null);

        }

        @Override
        public int hashCode() {
            int result = getPage() != null ? getPage().hashCode() : 0;
            result = 31 * result + (getPages() != null ? getPages().hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Page{" +
                    "page='" + page + '\'' +
                    ", pages=" + pages +
                    '}';
        }
    }
}
