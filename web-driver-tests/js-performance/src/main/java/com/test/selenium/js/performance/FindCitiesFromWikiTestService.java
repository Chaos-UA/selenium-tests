package com.test.selenium.js.performance;

import com.test.selenium.common.GcUtil;
import com.test.selenium.visualizer.VisualizerService;
import com.test.selenium.visualizer.chart.bar.BarData;
import com.test.selenium.visualizer.chart.bar.Data;
import com.test.selenium.visualizer.chart.bar.Series;
import com.test.selenium.visualizer.chart.line.LineData;
import com.test.selenium.webdriver.common.*;
import com.test.selenium.webdriver.manager.WebDriverService;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class FindCitiesFromWikiTestService {
    private static final URL MAIN_PAGE_URL = toURL("https://en.wikipedia.org/wiki/List_of_towns_and_cities_with_100,000_or_more_inhabitants/country:_A-B");

    @Autowired
    private WebDriverService webDriverService;

    @Autowired
    private VisualizerService visualizerService;

    private Logger logger = LogUtil.getLogger();

    public void test() throws Throwable {
        BarData surfingData = new BarData();
        surfingData.setTitle("Group city names by countries (processing time)");
        surfingData.setyTitle("Time ms");
        LineData memoryChart = new LineData("Time ms", "RSS kb", "Memory usage");
        LineData cpuChart = new LineData("Time ms", "CPU%", "CPU% usage");
        LineData processesChart = new LineData("Time ms", "Processes", "Processes");
        Map<String, List<Country>> browserFoundCities = new HashMap<>(webDriverService.getWebDriverFactories().size());
        for (WebDriverFactory webDriverFactory : webDriverService.getWebDriverFactories()) {
            GcUtil.gc();
            try (WebDriverWatcher webDriverWatcher = new WebDriverWatcher(webDriverFactory.createWebDriver(), 1000)) {
                long startProcessingTime = System.currentTimeMillis();
                //
                List<Country> countries = new ArrayList<Country>(250);
                    // hardcode some countries
                countries.add(new Country("French Guiana", "FR"));
                countries.add(new Country("England", "UK"));
                countries.add(new Country("Saudi Arabia", "SA"));
                countries.add(new Country("Western Sahara", "EH"));
                countries.add(new Country("Réunion", "RE"));
                countries.add(new Country("Martinique", "FR"));
                //
                List<URL> citiesByNameLinks = getCitiesByNameLinks(webDriverWatcher);
                if (citiesByNameLinks.isEmpty()) {
                    throw new RuntimeException();
                }
                Set<URL> countryUrls = new HashSet<>(250);
                for (URL citiesByNameLink : citiesByNameLinks) {// add
                    try {
                        logger.info("Processing url: " + citiesByNameLink);
                        webDriverWatcher.get(citiesByNameLink.toString());
                        List<WebElement> rows = webDriverWatcher.findElementsByXpath("//*[@id=\"mw-content-text\"]/table[@class=\"wikitable sortable jquery-tablesorter\"]/tbody/tr");
                        if (rows.isEmpty()) {
                            throw new RuntimeException();
                        }
                        class RowData {
                            private final String cityName;
                            private final String countryName;
                            private final URL urlToCountry;

                            public RowData(String cityName, String countryName, URL urlToCountry) {
                                this.cityName = cityName;
                                this.countryName = countryName;
                                this.urlToCountry = urlToCountry;
                            }

                            public String getCityName() {
                                return cityName;
                            }

                            public String getCountryName() {
                                return countryName;
                            }

                            public URL getUrlToCountry() {
                                return urlToCountry;
                            }
                        }
                        List<RowData> rowDatas = new ArrayList<>(rows.size());
                        for (WebElement row : rows) {
                            try {
                                WebElement cityElem = row.findElementByXpath(".//td[1]/a");
                                WebElement countryElem = row.findElementByXpath(".//td[2]/a");
                                String cityName = cityElem.getText();
                                String countryName = countryElem.getText();
                                String urlToCountry = countryElem.getAttribute("href");
                                rowDatas.add(new RowData(cityName, countryName, new URL(urlToCountry)));
                            }
                            catch (Throwable t) {
                                System.out.println(String.format("Error occurred during processing URL[%s], row[%s] ", citiesByNameLink, row.getText()));
                            }
                        }
                        // resolve countries and add city
                        for (RowData rowData : rowDatas) {
                            countryUrls.add(rowData.getUrlToCountry());
                            getCountry(webDriverWatcher, rowData.getCountryName(), rowData.getUrlToCountry(), countries).getCities().add(rowData.getCityName());
                        }
                    }
                    catch (Throwable t) {
                        System.out.println("Error occurred during processing url: " + citiesByNameLink);
                        throw t;
                    }
                }
                /*
                if (true) {
                    for (URL url : countryUrls) {
                        System.out.println(url);
                    }
                    System.exit(1);
                }
                */
                webDriverWatcher.close();
                //
                List<Series> seriesData = new ArrayList<>();
                seriesData.add(new Series("Processing time", System.currentTimeMillis() - startProcessingTime));
                surfingData.getDatas().add(new Data(webDriverWatcher.getName(), seriesData));
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
                //
                browserFoundCities.put(webDriverWatcher.getName(), countries);
            }
        }
        //visualizerService.showStackedBarChar(surfingData);
        //visualizerService.showLineChart(memoryChart);
        //visualizerService.showLineChart(cpuChart);
        //visualizerService.showLineChart(processesChart);
        visualizerService.showAll(
                visualizerService.buildStackedBarChart(surfingData),
                visualizerService.buildLineChart(memoryChart),
                visualizerService.buildLineChart(cpuChart),
                visualizerService.buildLineChart(processesChart)
        );
        //
        List<Country> countries = browserFoundCities.get(browserFoundCities.keySet().stream().findFirst().get());
        boolean allResultsEquals = true;
        for (String browser : browserFoundCities.keySet()) {
            if (!browserFoundCities.get(browser).equals(countries)) {
                allResultsEquals = false;
            }
        }
        logger.debug(browserFoundCities);
        if (allResultsEquals) {
            logger.info("All browsers have same results");
        }
        else {
            logger.warn("Not all browsers have same results");
        }
    }

    private static URL toURL(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<URL> getCitiesByNameLinks(WebDriver driver) {
        driver.get(MAIN_PAGE_URL.toString());
        List<WebElement> relativeUrls = driver.findElementsByXpath("//*[@id=\"mw-content-text\"]/ul[2]/li/center/a");
        return relativeUrls.stream().map(v -> toURL(v.getAttribute("href"))).collect(Collectors.toList());
    }

    private static Country getCountry(WebDriver driver, String name, URL urlToCountry, List<Country> countries) throws Throwable {
        Country country = countries.stream().filter(c -> c.getName().equals(name)).findFirst().orElse(null);
        if (country == null) {
            LogUtil.getLogger().info("Resolving country`s ISO_3166-2 by processing url: " + urlToCountry);
            driver.get(urlToCountry.toString());
            String pageSource = driver.getPageSource();
            Matcher matcher = Pattern.compile("\\/wiki\\/ISO_3166-2:(\\w+)\"").matcher(pageSource);
            if (!matcher.find()) {
                throw new RuntimeException("Country ISO_3166-2 not found by url: " + urlToCountry);
            }
            country = new Country(name, matcher.group(1));
            countries.add(country);
        }
        return country;
    }

    private static class Country {
        private final String name;
        private final String iso_3166_2;
        private final Set<String> cities = new HashSet<>();

        public Country(String name, String iso_3166_2) {
            this.name = name;
            this.iso_3166_2 = iso_3166_2;
        }

        public String getName() {
            return name;
        }

        public String getIso_3166_2() {
            return iso_3166_2;
        }

        public Set<String> getCities() {
            return cities;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Country country = (Country) o;

            if (!getName().equals(country.getName())) return false;
            if (!getIso_3166_2().equals(country.getIso_3166_2())) return false;
            return getCities().equals(country.getCities());

        }

        @Override
        public int hashCode() {
            int result = getName().hashCode();
            result = 31 * result + getIso_3166_2().hashCode();
            result = 31 * result + getCities().hashCode();
            return result;
        }

        @Override
        public String toString() {
            return "Country{" +
                    "name='" + name + '\'' +
                    ", iso_3166_2='" + iso_3166_2 + '\'' +
                    ", cities=" + cities +
                    '}';
        }
    }
}
