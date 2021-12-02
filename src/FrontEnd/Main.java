package FrontEnd;

import BackEnd.markets.Market;
import BackEnd.markets.MarketTrend;
import BackEnd.markets.Status;
import BackEnd.chart.CandleStickChart;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;


import java.io.*;

//
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The main application class
 */
public class Main extends Application {

    /** Array list to store data on all markets. */
    private ArrayList<Market> markets = new ArrayList<>();

    /** Constant variables for convenient use */
    private final String rawDataLocation = "resources/marketsRawData";
    private final String rawDataFileType = ".csv";

    /** start method to initialise application */
    @Override
    public void start(Stage stage) throws Exception{
//        stage.setTitle("Atkex");
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

    /**
     * Loads the main pane for the UI.
     * Sets up chart selector switching the markets chart.
     * Creates all the charts that can be displayed.
     *
     * @return the loaded pane.
     * @throws IOException if the pane could not be loaded.
     */
    private Pane loadMainPane() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane mainPane = (Pane) loader.load(
                getClass().getResourceAsStream(
                        "homeScreen.fxml"
                )
        );

        mainPane.setPrefHeight(600);
        mainPane.setPrefWidth(900);
        MainController mainController = loader.getController();

        ChartSelector.setMainController(mainController);

        createCharts();
        mainController.setMarkets(markets);

        return mainPane;
    }

    /**
     * Creates the main application screen.
     *
     * @param mainPane the main application layout.
     * @return the created scene.
     */
    private Scene createScene(Pane mainPane) {
        Scene scene = new Scene(
                mainPane
        );

        scene.getStylesheets().setAll(
                getClass().getResource("styles/MainApplication.css").toExternalForm()
        );

        return scene;
    }

    /**
     * Loads raw market data into an array list of Market objects.
     *
     * Assigns open, close, high and low prices to the array.
     */
    public void loadSaveData() {
        String oneHourSuffix = ", 60" + rawDataFileType;
        String fourHourSuffix = ", 240" + rawDataFileType;
        String oneDaySuffix = ", 1D" + rawDataFileType;

        //Find number of markets
        File directoryP = new File(rawDataLocation);
        String contents[] = directoryP.list();

        //Loop repeats for number of markets in folder
        for (int i = 0; i < contents.length; i++) {
            String index = contents[i];
            File directoryPath = new File(rawDataLocation + "/" + index);
            String marketsList[] = directoryPath.list();

            String line = "";
            //Loop to repeat for three timeframes
            for (int j = 0; j < 3; j++) {
                String path = null;
                if (marketsList[j].equals(index + oneHourSuffix)) {
                    path = rawDataLocation + "/" + index + "/" + index + oneHourSuffix;
                    try {
                        BufferedReader br = new BufferedReader(
                                new FileReader(path));
                        while ((line = br.readLine()) != null) {
                            if (!line.equals("time,open,high,low,close")) {
                                String[] values = line.split(",");
                                markets.get(i).addOneHourValues(
                                        Float.parseFloat(values[1]),
                                        Float.parseFloat(values[4]),
                                        Float.parseFloat(values[2]),
                                        Float.parseFloat(values[3]));
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
                        BufferedReader br = new BufferedReader(
                                new FileReader(path));
                        while ((line = br.readLine()) != null) {
                            if (!line.equals("time,open,high,low,close")) {
                                String[] values = line.split(",");
                                markets.get(i).addFourHourValues(
                                        Float.parseFloat(values[1]),
                                        Float.parseFloat(values[4]),
                                        Float.parseFloat(values[2]),
                                        Float.parseFloat(values[3]));
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
                        BufferedReader br = new BufferedReader(
                                new FileReader(path));
                        markets.add(new Market(index, Status.PENDING, MarketTrend.NO_TREND));
                        while ((line = br.readLine()) != null) {
                            if (!line.equals("time,open,high,low,close")) {
                                String[] values = line.split(",");
                                markets.get(i).addOneDayValues(
                                        Float.parseFloat(values[1]),
                                        Float.parseFloat(values[4]),
                                        Float.parseFloat(values[2]),
                                        Float.parseFloat(values[3]));
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
    }

    /**
     * Creates all charts necessary to be displayed.
     * Adds all charts the the array list.
     *
     */
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
                        new XYChart.Data<Number, Number>(i,
                                markets.get(m).getOneDayValues().get(i).getOpen(),
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

    /**
     *
     * @return markets array list.
     */
    public ArrayList<Market> getMarkets() { return markets; }

    /**
     * @param markets array list.
     */
    public void setMarkets(ArrayList<Market> markets) { this.markets = markets; }

    public static void main(String[] args) { launch(args); }
}
