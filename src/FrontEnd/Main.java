package FrontEnd;

import BackEnd.Markets.Market;
import BackEnd.Markets.MarketTrend;
import BackEnd.Markets.Status;
import BackEnd.chart.CandleStickChart;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.StringConverter;


import java.io.*;

//
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {

    private ArrayList<Market> markets = new ArrayList<>();
    private String rawDataLocation = "Resources/MarketsRawData";
    private final String rawDataFileType = ".csv";

    @Override
    public void start(Stage stage) throws Exception{
        stage.setTitle("Atkex");
        stage.getIcons().add(new Image("img/AtkexLogo.png"));
        stage.setResizable(true);
        //stage.setMaximized(true);

        loadSaveData();
        stage.setScene(
                createScene(
                        loadMainPane()
                )
        );
        stage.show();
    }

    private Pane loadMainPane() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane mainPane = (Pane) loader.load(
                getClass().getResourceAsStream(
                        "homeScreen.fxml"
                )
        );

        Controller controller = loader.getController();

        ChartSelector.setMainController(controller);

        createCharts();
        controller.setMarkets(markets);

        return mainPane;
    }

    private Scene createScene(Pane mainPane) {
        Scene scene = new Scene(
                mainPane
        );

        scene.getStylesheets().setAll(
                getClass().getResource("CandleStickChartStyles.css").toExternalForm()
        );

        return scene;
    }

    public void loadSaveData() {
        String oneHourSuffix = ", 60" + rawDataFileType;
        String fourHourSuffix = ", 240" + rawDataFileType;
        String oneDaySuffix = ", 1D" + rawDataFileType;

        //Find number of markets
        File directoryP = new File("Resources/MarketsRawData");
        String contents[] = directoryP.list();

        //Loop repeats for number of markets in folder
        for (int i = 0; i < contents.length; i++) {
            //Get index and make array of directory for markets
            String index = contents[i];
            File directoryPath = new File("Resources/MarketsRawData/" + index);
            String marketsList[] = directoryPath.list();

            String line = "";
            //Loop to repeat for three timeframes
            for (int j = 0; j < 3; j++) {
                String path = null;
                if (marketsList[j].equals(index + oneHourSuffix)) {
                    path = rawDataLocation + "/" + index + "/" + index + oneHourSuffix;
                    try {

                        BufferedReader br = new BufferedReader(new FileReader(path));
                        while ((line = br.readLine()) != null) {
                            if (!line.equals("time,open,high,low,close")) {
                                String[] values = line.split(",");
                                markets.get(i).addOneHourValues(Float.parseFloat(values[1]),
                                        Float.parseFloat(values[4]), Float.parseFloat(values[2]),
                                        Float.parseFloat(values[3]));

                                //create candlestick charts here
                                //CandleStickChart bc = new CandleStickChart()
                            }
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (marketsList[j].equals(index + fourHourSuffix)) {
                    path = rawDataLocation + "/" + index + "/" + index + fourHourSuffix;
                    try {

                        BufferedReader br = new BufferedReader(new FileReader(path));
                        while ((line = br.readLine()) != null) {
                            if (!line.equals("time,open,high,low,close")) {
                                String[] values = line.split(",");
                                markets.get(i).addFourHourValues(Float.parseFloat(values[1]),
                                        Float.parseFloat(values[4]), Float.parseFloat(values[2]),
                                        Float.parseFloat(values[3]));

                                //create candlestick charts here
                                //CandleStickChart bc = new CandleStickChart()
                            }
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (marketsList[j].equals(index + oneDaySuffix)) {
                    path = rawDataLocation + "/" + index + "/" + index + oneDaySuffix;
                    try {

                        BufferedReader br = new BufferedReader(new FileReader(path));
                        markets.add(new Market(index, Status.PENDING, MarketTrend.NO_TREND));
                        while ((line = br.readLine()) != null) {
                            if (!line.equals("time,open,high,low,close")) {
                                String[] values = line.split(",");
                                markets.get(i).addOneDayValues(Float.parseFloat(values[1]),
                                        Float.parseFloat(values[4]), Float.parseFloat(values[2]),
                                        Float.parseFloat(values[3]));

                                //create candlestick charts here
                                //CandleStickChart bc = new CandleStickChart()
                            }
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        System.out.println("Data successfully loaded");

    }

    private void createCharts() {
        for (int m = 0; m < markets.size(); m++) {

            double xAxisLowerBound = 0;
            for (int a = 0; a < markets.get(m).getOneDayValues().size(); a++) {
                if (xAxisLowerBound == 0) {
                    xAxisLowerBound = markets.get(m).getOneDayValues().get(a).getLow();
                } else {
                    if (markets.get(m).getOneDayValues().get(a).getLow() < xAxisLowerBound) {
                        xAxisLowerBound = markets.get(m).getOneDayValues().get(a).getLow();
                    }
                }
            }

            double xAxisUpperBound = 0;
            for (int a = 0; a < markets.get(m).getOneDayValues().size(); a++) {

                if (xAxisUpperBound == 0) {
                    xAxisUpperBound = markets.get(m).getOneDayValues().get(a).getHigh();
                } else {
                    if (markets.get(m).getOneDayValues().get(a).getHigh() > xAxisUpperBound) {
                        xAxisUpperBound = markets.get(m).getOneDayValues().get(a).getHigh();
                    }
                }
            }

            final NumberAxis xAxis = new NumberAxis();
            final NumberAxis yAxis = new NumberAxis(xAxisLowerBound, xAxisUpperBound, 10000);

            final CandleStickChart bc = new CandleStickChart(xAxis, yAxis);
            // setup chart
            bc.setTitle(markets.get(m).getIndex() + " Chart");
            xAxis.setLabel("Day");
            yAxis.setLabel("Price");
            // add starting data
            XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();

            for (int i = 0; i < markets.get(m).getOneDayValues().size(); i++) {
                series.getData().add(
                        new XYChart.Data<Number, Number>(i, markets.get(m).getOneDayValues().get(i).getOpen(),
                                new CandleStickChart.CandleStickExtraValues(
                                        markets.get(m).getOneDayValues().get(i).getClose(),
                                        markets.get(m).getOneDayValues().get(i).getHigh(),
                                        markets.get(m).getOneDayValues().get(i).getLow()))
                );
            }
            ObservableList<XYChart.Series<Number, Number>> chartData = bc.getData();
            if (chartData == null) {
                chartData = FXCollections.observableArrayList(series);
                bc.setData(chartData);
            } else {
                bc.getData().add(series);
            }
            markets.get(m).setDayCandleStickChart(bc);
        }
    }

    public static String[] getAllIndex() {
        File directoryPath = new File("Resources/MarketsRawData");
        String contents[] = directoryPath.list();

        return contents;
    }

    public ArrayList<Market> getMarkets() {
        return markets;
    }

    public void setMarkets(ArrayList<Market> markets) {
        this.markets = markets;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
