package com.test.selenium.visualizer;


import com.test.selenium.common.ApplicationConfiguration;
import com.test.selenium.visualizer.chart.bar.BarData;
import com.test.selenium.visualizer.chart.bar.Data;
import com.test.selenium.visualizer.chart.bar.Series;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.HashMap;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationConfiguration.class)
public class TestVisualizations {

    @Autowired
    private VisualizerService visualizerService;

    @Test
    public void test() throws Throwable {
        BarData barData = new BarData();
        barData.setTitle("Title");
        barData.setyTitle("Y title");

        Data data = new Data();
        data.setName("XYChart.Series 1");
        data.getSeriesData().add(new Series("January", 100));
        data.getSeriesData().add(new Series("February", 200));
        data.getSeriesData().add(new Series("March", 50));
        barData.getDatas().add(data);

        data = new Data();
        data.setName("XYChart.Series 2");
        data.getSeriesData().add(new Series("January", 150));
        data.getSeriesData().add(new Series("February", 100));
        data.getSeriesData().add(new Series("March", 60));
        barData.getDatas().add(data);
        visualizerService.showStackedBarChar(barData);




        Thread.sleep(1000000L);
    }

    @Test
    public void testLineChart() throws InterruptedException {
        //visualizerService.showBar(null);
        Thread.sleep(1000000L);
    }


}
