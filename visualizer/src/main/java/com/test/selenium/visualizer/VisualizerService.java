package com.test.selenium.visualizer;

import com.test.selenium.visualizer.chart.bar.BarData;
import com.test.selenium.visualizer.chart.bar.Data;
import com.test.selenium.visualizer.chart.bar.Series;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VisualizerService {

    @PostConstruct
    protected void init() throws Throwable {
        Thread t = new Thread("JavaFX Init Thread") {
            public void run() {
                Application.launch(InitJavaFx.class, new String[0]);
            }
        };
        t.setDaemon(false);
        t.start();
        Thread.sleep(1000);
    }

    public void showStackedBarChar(BarData barData) {
        FxUtil.runAndWait(() -> {
            Stage stage = new Stage();
            Group root = new Group();
            final CategoryAxis xAxis = new CategoryAxis();
            final NumberAxis yAxis = new NumberAxis();
            //barData.get
            Set<String> categories = new HashSet<String>();
            for (Data data : barData.getDatas()) {
                for (Series series : data.getSeriesData()) {
                    categories.add(series.getName());
                }
            }
            xAxis.setCategories(FXCollections.observableArrayList(categories));
            yAxis.setLabel(barData.getyTitle());
            final StackedBarChart<Number, String> stackedBarChart = new StackedBarChart<>(yAxis, xAxis);
            stackedBarChart.setTitle(barData.getTitle());

            for (Data data: barData.getDatas()) {
                List<Series> seriesDataList = data.getSeriesData();
                //for (seriesData)
                XYChart.Series<Number, String> series = new XYChart.Series<>();
                series.setName(data.getName());
                for (Series seriesData : seriesDataList) {
                    series.getData().add(new XYChart.Data<>(seriesData.getValue(), seriesData.getName()));
                }
                stackedBarChart.getData().add(series);
            }
            stackedBarChart.prefWidthProperty().bind(stage.widthProperty());
            stage.heightProperty().addListener(v -> stackedBarChart.setPrefHeight(stage.getHeight() - 50));
            root.getChildren().addAll(stackedBarChart);

            stage.setScene(new Scene(root, 800, 600));
            stage.show();
        });
    }
}
