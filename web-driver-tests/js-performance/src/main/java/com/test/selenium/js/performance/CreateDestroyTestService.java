package com.test.selenium.js.performance;


import com.test.selenium.common.GcUtil;
import com.test.selenium.visualizer.FxUtil;
import com.test.selenium.visualizer.VisualizerService;
import com.test.selenium.visualizer.chart.line.LineData;
import com.test.selenium.webdriver.common.WebDriver;
import com.test.selenium.webdriver.common.WebDriverFactory;
import com.test.selenium.webdriver.manager.WebDriverService;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;


@Service
public class CreateDestroyTestService {
    private static final List<String> PAGES = Arrays.asList(
            "http://unkur.com",
            "http://habrahabr.ru",
            "https://www.youtube.com",
            "http://www.ebay.com",
            "https://www.google.com",
            "http://www.yandex.ua"
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
            int iterations = 100;
            for (int i = 0; i < iterations; i++) {
                GcUtil.gc();
                try {
                    long startTime = System.currentTimeMillis();
                    webDriver = webDriverFactory.createWebDriver();
                    dataCreation.getSeries().add(new LineData.Series(i, System.currentTimeMillis() - startTime));
                    String url = PAGES.get(i % PAGES.size());
                    System.out.println(String.format("[%s] page: %s (%s/%s)", webDriver.getName(), url, i, iterations));
                    //webDriver.get(url);
                } finally {
                    long startTime = System.currentTimeMillis();
                    webDriver.close();
                    dataDestroying.getSeries().add(new LineData.Series(i, System.currentTimeMillis() - startTime));
                }
            }
        }
        FxUtil.runAndWait(() -> {
            Vector<String> columns = new Vector<>(Arrays.asList("Browser", "Create instance ms (avr)", "Destroy instance ms (avr)", "Destroy+Create instance ms (avr)"));
            Vector<Vector> rows = new Vector<>();
            for (LineData.Data data : creationLine.getDatas()) {
                String browser = data.getName();
                double averageCreationTime = data.getSeries().stream().mapToDouble(v->v.getY().doubleValue()).average().getAsDouble();
                double averageDestroyingTime = destroyingLine.getDatas().stream()
                        .filter(v->v.getName().equals(browser))
                        .findFirst().get()
                        .getSeries().stream().mapToDouble(v->v.getY().doubleValue())
                        .average()
                        .getAsDouble();
                Vector row = new Vector(Arrays.asList(browser, averageCreationTime, averageDestroyingTime, averageCreationTime + averageDestroyingTime));
                rows.add(row);
            }
            JTable table = new JTable(rows, columns);
            SwingNode node = new SwingNode(){{this.setContent(new JScrollPane(table));}};
            Stage stage = new Stage();
            stage.setScene(new Scene(new BorderPane(node), 1366, 768));
            stage.show();
            visualizerService.showAll(
                    visualizerService.buildLineChart(creationLine),
                    visualizerService.buildLineChart(destroyingLine)
            );
        });
    }

}
