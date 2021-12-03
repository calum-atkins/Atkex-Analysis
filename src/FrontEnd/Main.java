package FrontEnd;

import BackEnd.markets.Market;
import BackEnd.markets.MarketTrend;
import BackEnd.markets.Status;
import BackEnd.chart.CandleStickChart;
import BackEnd.patternRecognition.algorithms.criticalLevels;

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
import java.lang.reflect.Array;
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
    private final String marketPreferences = "resources/marketPreferences/preferences.csv";

    /** start method to initialise application */
    @Override
    public void start(Stage stage) throws Exception{
        stage.setTitle("Atkex");
        stage.getIcons().add(new Image("img/AtkexLogo.png"));
        stage.setResizable(true);

        //stage.setMaximized(true);

        /** Load market data */
        loadSaveData();

        /** Load segment and range */
        loadSegmentRange();

        /** Generate Resistance/support */


        /** Generate Trend */


        /** Generate Patterns */


        /** Load stage */
        stage.setScene(
                createScene(
                        loadMainPane()
                )
        );
        stage.show();
    }

    /**
     * Method to laod segment and range preferences for support and resistance.
     */
    private void loadSegmentRange() {
        String line = "";
        try {
            int j = 1;
            BufferedReader br = new BufferedReader(
                    new FileReader(marketPreferences));
            while ((line = br.readLine()) != null) {
                if (!line.equals("index,segments,range")) {

                    String[] values = line.split(",");
                    for (int i = 0; i < markets.size();i++) {
                        if (values[0].equals(markets.get(i).getIndex())) {
                            j++;
                            markets.get(i).setSegment(Integer.parseInt(values[1]));

                            markets.get(i).setRange(Double.parseDouble(values[2]));
                        }
                    }
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            /** Loop to repeat for three time frames and add values and store them */
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

                    //System.out.println(markets.get(i).getOneHourValues().size());
                    for (int k = 0; k < markets.size(); k++) {
                        //System.out.println(markets.get(i).getOneHourValues().get(k).getOpen());
                    }
                    //Generate Resistance/Support here, use a method

                    if (markets.get(i).getIndex().equals("CADJPY")) {
                        System.out.println("Working");
                        markets.get(i).addOneHourSR(87);
                        markets.get(i).addOneHourSR(88.5);
                    }
                    ArrayList<Double> oneHourSRList = criticalLevels.start(markets.get(i));
                    markets.get(i).setOneHourSR(new ArrayList<Double>());


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
                findSegmentRange(markets.get(i).getIndex(), i);
            }
        }
//        System.out.println(markets.get(0).getSegment());
    }

    /**
     * Creates all charts necessary to be displayed.
     * Adds all charts the the array list.
     *
     */
    private void createCharts() {
        for (int m = 0; m < markets.size(); m++) {
            markets.get(m).setDayCandleStickChart(
                    createOneDayChart(
                            markets.get(m)
                    )
            );
            markets.get(m).setOneHourCandleStickChart(
                    createOneHourChart(
                            markets.get(m)
                    )
            );
            markets.get(m).setFourHourCandleStickChart(
                    createFourHourChart(
                            markets.get(m)
                    )
            );
        }
    }

    /**
     * Method to create a viewable chart for the one day timeframe
     * @param market The market to base the chart on
     * @return The created CandleStickChart
     */
    public CandleStickChart createOneDayChart(Market market) {

        double xAxisLowerBound = getXAxisLowerBound(market.getOneDayValues());
        double xAxisUpperBound = getXAxisUpperBound(market.getOneDayValues());

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis(xAxisLowerBound, xAxisUpperBound, 10000);

        final CandleStickChart bc = new CandleStickChart(xAxis, yAxis);
        // setup chart
        bc.setTitle(market.getIndex() + " Chart");
        xAxis.setLabel("Day");
        yAxis.setLabel("Price");
        // add starting data
        XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();

        for (int i = 0; i < market.getOneDayValues().size(); i++) {
            series.getData().add(
                    new XYChart.Data<Number, Number>(i,
                            market.getOneDayValues().get(i).getOpen(),
                            new CandleStickChart.CandleStickExtraValues(
                                    market.getOneDayValues().get(i).getClose(),
                                    market.getOneDayValues().get(i).getHigh(),
                                    market.getOneDayValues().get(i).getLow()))
            );
        }
        ObservableList<XYChart.Series<Number, Number>> chartData = bc.getData();
        if (chartData == null) {
            chartData = FXCollections.observableArrayList(series);
            bc.setData(chartData);
        } else {
            bc.getData().add(series);
        }

        //Add resistance/support here
        XYChart.Data<Number, Number> horizontalMarker = new XYChart.Data<>(0, 25);
        bc.addHorizontalValueMarker(horizontalMarker); //This can be used to resistance and support levels



        return bc;
    }

    /**
     * Method to create a viewable chart for the one hour timeframe
     * @param market The market to base the chart on
     * @return The created CandleStickChart
     */
    public CandleStickChart createOneHourChart(Market market) {

        double xAxisLowerBound = getXAxisLowerBound(market.getOneHourValues());
        double xAxisUpperBound = getXAxisUpperBound(market.getOneHourValues());

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis(xAxisLowerBound, xAxisUpperBound, 10000);

        final CandleStickChart bc = new CandleStickChart(xAxis, yAxis);
        // setup chart
        bc.setTitle(market.getIndex() + " Chart");
        xAxis.setLabel("Hourly");
        yAxis.setLabel("Price");
        // add starting data
        XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();

        for (int i = 0; i < market.getOneHourValues().size(); i++) {
            series.getData().add(
                    new XYChart.Data<Number, Number>(i,
                            market.getOneHourValues().get(i).getOpen(),
                            new CandleStickChart.CandleStickExtraValues(
                                    market.getOneHourValues().get(i).getClose(),
                                    market.getOneHourValues().get(i).getHigh(),
                                    market.getOneHourValues().get(i).getLow()))
            );
        }
        ObservableList<XYChart.Series<Number, Number>> chartData = bc.getData();
        if (chartData == null) {
            chartData = FXCollections.observableArrayList(series);
            bc.setData(chartData);
        } else {
            bc.getData().add(series);
        }

        /** Add resistance/support here */
        if (market.getIndex().equals("CADJPY")) {
            System.out.println("Working");
            for (int s = 0; s < market.getOneHourSR().size(); s++) {
                XYChart.Data<Number, Number> horizontalMarker = new XYChart.Data<>(0, market.getOneHourSR().get(s));
                bc.addHorizontalValueMarker(horizontalMarker); //This can be used to resistance and support levels

            }
        }

        return bc;
    }

    /**
     * Method to create a viewable chart for the four hour timeframe
     * @param market The market to base the chart on
     * @return The created CandleStickChart
     */
    public CandleStickChart createFourHourChart(Market market) {

        double xAxisLowerBound = getXAxisLowerBound(market.getFourHourValues());
        double xAxisUpperBound = getXAxisUpperBound(market.getFourHourValues());

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis(xAxisLowerBound, xAxisUpperBound, 10000);

        final CandleStickChart bc = new CandleStickChart(xAxis, yAxis);
        // setup chart
        bc.setTitle(market.getIndex() + " Chart");
        xAxis.setLabel("Four Hourly");
        yAxis.setLabel("Price");
        // add starting data
        XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();

        for (int i = 0; i < market.getFourHourValues().size(); i++) {
            series.getData().add(
                    new XYChart.Data<Number, Number>(i,
                            market.getFourHourValues().get(i).getOpen(),
                            new CandleStickChart.CandleStickExtraValues(
                                    market.getFourHourValues().get(i).getClose(),
                                    market.getFourHourValues().get(i).getHigh(),
                                    market.getFourHourValues().get(i).getLow()))
            );
        }
        ObservableList<XYChart.Series<Number, Number>> chartData = bc.getData();
        if (chartData == null) {
            chartData = FXCollections.observableArrayList(series);
            bc.setData(chartData);
        } else {
            bc.getData().add(series);
        }

        return bc;
    }

    /**
     * Method to return the lower bound
     * @param values Market values from the raw data
     * @return lowest value (swing low)
     */
    public double getXAxisLowerBound(ArrayList<Market.MarketValues> values) {
        double lowerBound = 0;
        for (int a = 0; a < values.size(); a++) {
            if (lowerBound == 0) {
                lowerBound = values.get(a).getLow();
            } else {
                if (values.get(a).getLow() < lowerBound) {
                    lowerBound = values.get(a).getLow();
                }
            }
        }
        return lowerBound;
    }

    /**
     * Method to return the upper bound
     * @param values Market values from the raw data
     * @return highest value (swing high)
     */
    public double getXAxisUpperBound(ArrayList<Market.MarketValues> values) {
        double xAxisUpperBound = 0;
        for (int a = 0; a < values.size(); a++) {

            if (xAxisUpperBound == 0) {
                xAxisUpperBound = values.get(a).getHigh();
            } else {
                if (values.get(a).getHigh() > xAxisUpperBound) {
                    xAxisUpperBound = values.get(a).getHigh();
                }
            }
        }
        return xAxisUpperBound;
    }

    public void findSegmentRange(String index, int i) {
//        File file = new File(marketPreferences);
//        String line = "";
//        try {
//            BufferedReader br = new BufferedReader(
//                    new FileReader(marketPreferences));
//            while ((line = br.readLine()) != null) {
//                if (!line.equals("index,segments,range")) {
//                    String[] values = line.split(",");
//                    System.out.println(values[0]);
//                    if (values[0].equals(index)) {
//                        markets.get(i).setSegment(Integer.parseInt(values[1]));
//                        markets.get(i).setRange(Double.parseDouble(values[2]));
//                    }
//                }
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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
