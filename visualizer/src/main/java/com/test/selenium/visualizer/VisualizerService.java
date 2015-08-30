package com.test.selenium.visualizer;

import com.test.selenium.visualizer.chart.bar.BarData;
import com.test.selenium.visualizer.chart.bar.Data;
import com.test.selenium.visualizer.chart.bar.Series;
import com.test.selenium.visualizer.chart.line.LineData;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.stage.Stage;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
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
                categories.add(data.getName());
            }
            xAxis.setCategories(FXCollections.observableArrayList(categories));
            yAxis.setLabel(barData.getyTitle());
            final StackedBarChart<Number, String> stackedBarChart = new StackedBarChart<>(yAxis, xAxis);
            stackedBarChart.setTitle(barData.getTitle());


            Data d = barData.getDatas().get(0);
            for (Series s : d.getSeriesData()) {
                XYChart.Series<Number, String> series = new XYChart.Series<>();
                series.setName(s.getName());
                for (Data data : barData.getDatas()) {
                    for (Series seriesData : data.getSeriesData()) {
                        if (seriesData.getName().equals(series.getName())) {
                            series.getData().add(new XYChart.Data<>(seriesData.getValue(), data.getName()));
                        }
                    }
                }
                stackedBarChart.getData().add(series);
            }
            stage.setScene(new Scene(stackedBarChart, 800, 600));
            stage.show();
        });
    }

    public void showLineChart(LineData lineData) {
        FxUtil.runAndWait(() -> {
            Stage stage = new Stage();
            NumberAxis xAxis = new NumberAxis();
            xAxis.setLabel(lineData.getxLabel());
            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel(lineData.getyLabel());

            List<XYChart.Series<Number, Number>> serieses = new ArrayList<XYChart.Series<Number, Number>>();
            for (LineData.Data data : lineData.getDatas()) {
                LineChart.Series<Number, Number> series = new LineChart.Series<>(data.getName(), FXCollections.observableArrayList(
                        data.getSeries().stream().map(v-> new XYChart.Data<Number, Number>(v.getX(), v.getY())).collect(Collectors.toList())
                ));
                serieses.add(series);
            }
            LineChart chart = new LineChart(xAxis, yAxis, FXCollections.observableArrayList(serieses));
            stage.setScene(new Scene(chart));
            stage.show();
        });
    }
}
