package com.test.selenium.visualizer;

import com.test.selenium.visualizer.chart.bar.BarData;
import com.test.selenium.visualizer.chart.bar.Data;
import com.test.selenium.visualizer.chart.bar.Series;
import com.test.selenium.visualizer.chart.line.LineData;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VisualizerService {

    private final int width = 1600;
    private final int height = 600;
    private static final char[] CHARS = {'a', 'b', 'c', 'd', 'e', 'f', 'g'};

    @PostConstruct
    protected void init() throws Throwable {
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        Thread t = new Thread("JavaFX Init Thread") {
            public void run() {
                Application.launch(InitJavaFx.class, new String[0]);
            }
        };
        t.setDaemon(false);
        t.start();
        Thread.sleep(1000);
    }

    public void showAll(Node... nodes) {
        FxUtil.runAndWait(() -> {
            Stage stage = new Stage();
            SplitPane splitPane = new SplitPane();
            splitPane.setOrientation(Orientation.VERTICAL);
            for (int i = 0; i < nodes.length; i++) {
                Node node = nodes[i];
                BorderPane borderPane = new BorderPane();
                if (nodes.length > 1) {
                    Label label = new Label(CHARS[i] + ")");
                    label.setFont(Font.font(Font.getDefault().getName(), FontWeight.BOLD, 20));
                    label.setAlignment(Pos.CENTER_LEFT);
                    borderPane.setLeft(new VBox(label) {{
                        this.setAlignment(Pos.CENTER_LEFT);
                    }});
                }
                borderPane.setCenter(node);
                splitPane.getItems().add(borderPane);
                //HBox hBox = new HBox();
                //hBox.getChildren().add();
                //hBox.getChildren().add(node);
                //splitPane.getItems().add(hBox);
            }
            stage.setScene(new Scene(splitPane, width, height));
            stage.showAndWait();
        });
    }

    public StackedBarChart<Number, String> buildStackedBarChart(BarData barData) {
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
        //stackedBarChart.setTitle(barData.getTitle());


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
        return stackedBarChart;
    }


    public void showStackedBarChar(BarData barData) {
        FxUtil.runAndWait(() -> {
            Stage stage = new Stage();
            stage.setScene(new Scene(buildStackedBarChart(barData), width, height));
            stage.showAndWait();
        });
    }

    public LineChart buildLineChart(LineData lineData) {
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
        //chart.setTitle(lineData.getTitle());
        return chart;
    }

    public void showLineChart(LineData lineData) {
        FxUtil.runAndWait(() -> {
            Stage stage = new Stage();
            stage.setScene(new Scene(buildLineChart(lineData), width, height));
            stage.showAndWait();
        });
    }
}
