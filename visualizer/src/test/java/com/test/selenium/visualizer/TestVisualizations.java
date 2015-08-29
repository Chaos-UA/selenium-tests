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



        FxUtil.runAndWait(() -> {
            Stage stage = new Stage();
            Group root = new Group();

            final CategoryAxis xAxis = new CategoryAxis();
            final NumberAxis yAxis = new NumberAxis();


            xAxis.setCategories(FXCollections.<String>observableArrayList(Arrays.asList(
                    "January",
                    "February",
                    "January2",
                    "March")));
            yAxis.setLabel("Value");

            final StackedBarChart<Number, String> stackedBarChart = new StackedBarChart<>(yAxis, xAxis);
            stackedBarChart.setTitle("StackedBarChart");

            XYChart.Series<Number, String> series1 = new XYChart.Series();
            series1.setName("XYChart.Series 1");

            series1.getData().add(new XYChart.Data(100, "January"));
            series1.getData().add(new XYChart.Data(200, "February"));
            series1.getData().add(new XYChart.Data(50, "March"));

            XYChart.Series<Number, String> series2 = new XYChart.Series();
            series2.setName("XYChart.Series 2");

            series2.getData().add(new XYChart.Data(150, "January2"));
            series2.getData().add(new XYChart.Data(100, "February"));
            series2.getData().add(new XYChart.Data(60, "March"));

            stackedBarChart.getData().addAll(series1, series2);

            stackedBarChart.prefWidthProperty().bind(stage.widthProperty());
            stage.heightProperty().addListener(v -> stackedBarChart.setPrefHeight(stage.getHeight() - 50));
            root.getChildren().addAll(stackedBarChart);

            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        });
        Thread.sleep(1000000L);
    }


}
