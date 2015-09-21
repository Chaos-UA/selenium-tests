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
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class InheritanceTestService {

    @Autowired
    private VisualizerService visualizerService;

    @Autowired
    private WebDriverService webDriverService;

    public void test() throws Throwable {
        BarData barData = new BarData();
        barData.setTitle("Javascript performance (processing time)");
        barData.setyTitle("Time ms");
        LineData memoryChart = new LineData("Time ms", "RSS kb", "Memory usage");
        LineData cpuChart = new LineData("Time ms", "CPU%", "CPU% usage");
        LineData processesChart = new LineData("Time ms", "Processes", "Processes");
        String js = IOUtils.toString(InheritanceTestService.class.getResource("/test-inheritance-performance.js")) + "\nreturn testPerformance();";
        for (WebDriverFactory webDriverFactory : webDriverService.getWebDriverFactories()) {
            GcUtil.gc();
            try (WebDriverWatcher webDriverWatcher = new WebDriverWatcher(webDriverFactory.createWebDriver())) {
                Map<String, Integer> result = (Map<String, Integer>) webDriverWatcher.executeScript(js);
                List<Series> serieses = new ArrayList<>(result.size());
                for (String testName : result.keySet()) {
                    serieses.add(new Series(testName, result.get(testName)));
                }
                barData.getDatas().add(new Data(webDriverWatcher.getName(), serieses));
                webDriverWatcher.close();
                // memory
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
        //visualizerService.showStackedBarChar(barData);
        //visualizerService.showLineChart(memoryChart);
        //visualizerService.showLineChart(cpuChart);
        //visualizerService.showLineChart(processesChart);
        visualizerService.showAll(
                visualizerService.buildStackedBarChart(barData),
                visualizerService.buildLineChart(memoryChart),
                visualizerService.buildLineChart(cpuChart),
                visualizerService.buildLineChart(processesChart)
        );
    }
}
