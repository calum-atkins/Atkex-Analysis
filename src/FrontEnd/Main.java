package FrontEnd;

import BackEnd.markets.Market;
import BackEnd.markets.MarketTimeframe;
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


import java.awt.*;
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

    /** Variable declaration to hold the number of markets and number of timeframes used */
    private int marketsSize;
    private final int NUMBER_OF_TIMEFRAMES = 3;

    /** Constant variables for convenient use */
    private final String rawDataLocation = "resources/marketsRawData";
    private final String rawDataFileType = ".csv";
    private final String marketPreferences = "resources/marketPreferences/preferences.csv";

    /** start method to initialise application */
    @Override
    public void start(Stage stage) throws Exception{
        /** Loading screen here */


        /** Settings  */
        stage.setTitle("Atkex");
        stage.getIcons().add(new Image("img/AtkexLogo.png"));
        stage.setResizable(true);
        //stage.setMaximized(true);

        /** Load number of markets and initialise array list */
        File directoryP = new File(rawDataLocation);
        String contents[] = directoryP.list();
        for (int b = 0; b < contents.length; b++) {
            markets.add(new Market(contents[b], Status.PENDING, MarketTrend.NO_TREND));
            markets.get(b).setTimeframesStoreSize(NUMBER_OF_TIMEFRAMES);
        }
        marketsSize = contents.length;

        /** Load segment and range */
        loadSegmentRange();

        /** Load market data */
        loadSaveData();

        /** Generate Resistance/support */
        generateCriticalLevels();

        /** Generate Trends */


        /** Generate Patterns */


        /** Create charts */
        createCharts();

        /** Load stage */
        stage.setScene(
                createScene(
                        loadMainPane()
                )
        );
        stage.show();
    }

    /**
     * Method to load segment and range preferences for support and resistance.
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
                    for (int i = 0; i < marketsSize;i++) {
                        if (values[0].equals(markets.get(i).getIndex())) {
                            j++;
                            markets.get(i).setSegment(Integer.parseInt(values[1]));

                            markets.get(i).setLowerRange(Double.parseDouble(values[2]));
                            markets.get(i).setUpperRange(Double.parseDouble(values[2]));
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
     * Loads raw market data into an array list of Market objects.
     *
     * Assigns open, close, high and low prices an array.
     */
    public void loadSaveData() {
        String oneHourSuffix = ", 60" + rawDataFileType;
        String fourHourSuffix = ", 240" + rawDataFileType;
        String oneDaySuffix = ", 1D" + rawDataFileType;

        //Loop repeats for number of markets in folder
        for (int marketNum = 0; marketNum < marketsSize; marketNum++) {
            String index = markets.get(marketNum).getIndex();
            File directoryPath = new File(rawDataLocation + "/" + index);
            String marketsList[] = directoryPath.list();

            String line = "";
            float minOverall;
            float maxOverall;
            for (int j = 0; j <  NUMBER_OF_TIMEFRAMES; j++) {
                String path = null;
                //Set the path
                if (marketsList[j].equals(index + oneHourSuffix)) {
                    path = rawDataLocation + "/" + index + "/" + index + oneHourSuffix;
                    markets.get(marketNum).getTimeframesDataStore(j).setTimeframe(MarketTimeframe.ONE_HOUR);
                }
                else if (marketsList[j].equals(index + fourHourSuffix)) {
                    path = rawDataLocation + "/" + index + "/" + index + fourHourSuffix;
                    markets.get(marketNum).getTimeframesDataStore(j).setTimeframe(MarketTimeframe.FOUR_HOUR);
                }
                else if (marketsList[j].equals(index + oneDaySuffix)) {
                    path = rawDataLocation + "/" + index + "/" + index + oneDaySuffix;
                    markets.get(marketNum).getTimeframesDataStore(j).setTimeframe(MarketTimeframe.DAY);
                }
                markets.get(marketNum).getTimeframesDataStore(j).setFilePath(path);

                try {
                    BufferedReader br = new BufferedReader(
                            new FileReader(markets.get(marketNum).getTimeframesDataStore(j).getFilePath()));
                    int a = 0;
                    float min = 0;
                    float[] valueArray = new float[markets.get(marketNum).getSegment()];
                    minOverall = 0;
                    maxOverall = 0;
                    while ((line = br.readLine()) != null) {
                        if (!line.equals("time,open,high,low,close")) {
                            String[] values = line.split(",");
                            markets.get(marketNum).getTimeframesDataStore(j).addMarketValue(
                                    new Market.MarketValues(
                                            Float.parseFloat(values[1]),
                                            Float.parseFloat(values[4]),
                                            Float.parseFloat(values[2]),
                                            Float.parseFloat(values[3]))
                            );

                            if (minOverall == 0) {
                                minOverall = Float.parseFloat(values[3]);
                            } else if (Float.parseFloat(values[3]) < minOverall) {
                                minOverall = Float.parseFloat(values[3]);
                            }
                            if (maxOverall == 0) {
                                maxOverall = Float.parseFloat(values[2]);
                            } else if (Float.parseFloat(values[2]) > maxOverall) {
                                maxOverall = Float.parseFloat(values[2]);
                            }

                            if (a == 0) {
                                min = Float.parseFloat(values[3]);
                            } else if (Float.parseFloat(values[3]) < min) {
                                min = Float.parseFloat(values[3]);
                            }
                            a++;
                            if (a == markets.get(marketNum).getSegment()) {
                                a = 0;
                                markets.get(marketNum).getTimeframesDataStore(j).addMinSegment(min);
                            }
                        }
                    }
                    markets.get(marketNum).getTimeframesDataStore(j).setChartYAxisTickValue((maxOverall - minOverall) /10);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Method to generate critical levels fo support/resistance.
     */
    private void generateCriticalLevels() {
        for (Market m : markets) {
            for (int marketNum = 0; marketNum < NUMBER_OF_TIMEFRAMES; marketNum++) {

                ArrayList<Float> arrayOfMin = m.getTimeframesDataStore(marketNum).getMinSegments();
                //Repeat until array is empty
                while (!arrayOfMin.isEmpty()) {
                    ArrayList<Float> arrayToSet = new ArrayList<>();
                    //Find the minimum value
                    float minOfArray = 0;
                    ArrayList<Float> supportArray = new ArrayList<>();
                    for (int timeframeNum = 0; timeframeNum < arrayOfMin.size(); timeframeNum++) {
                        if (timeframeNum == 0) {
                            minOfArray = arrayOfMin.get(timeframeNum);
                        } else if (arrayOfMin.get(timeframeNum) < minOfArray) {
                            minOfArray = arrayOfMin.get(timeframeNum);
                        }
                    }
                    //Find any in range
                    int size = arrayOfMin.size();
                    for (int i = 0; i < size; i++) {
                        //Remove min value
                        if (arrayOfMin.get(i) == minOfArray) {
                            supportArray.add(minOfArray);
                        } else if ((arrayOfMin.get(i) / minOfArray) < m.getUpperRange()
                                && (arrayOfMin.get(i) / minOfArray) > m.getLowerRange()) {
                            supportArray.add(arrayOfMin.get(i));
                        } else {
                            arrayToSet.add(arrayOfMin.get(i));
                        }
                    }
                    float supportLevel = 0;
                    for (int i = 0; i < supportArray.size(); i++) {
                        supportLevel += supportArray.get(i);
                    }
                    supportLevel /= supportArray.size();
                    if (supportArray.size() > 4) {
                        m.getTimeframesDataStore(marketNum).addCriticalLevel(supportLevel);
                    }
                    arrayOfMin = arrayToSet;
                }
            }
        }
        for (Market m : markets) {
            for (int i = 0; i < NUMBER_OF_TIMEFRAMES; i++) {
                ArrayList<Float> arrayOfMax = m.getTimeframesDataStore(i).getMinSegments();
                //Repeat until array is empty
                while (!arrayOfMax.isEmpty()) {
                    ArrayList<Float> arrayToSet = new ArrayList<>();

                    //Find the minimum value
                    float maxOfArray = 0;
                    ArrayList<Float> resistanceArray = new ArrayList<>();
                    for (int j = 0; j < arrayOfMax.size(); j++) {
                        if (j == 0) {
                            maxOfArray = arrayOfMax.get(j);
                        } else if (arrayOfMax.get(j) > maxOfArray) {
                            maxOfArray = arrayOfMax.get(j);
                        }
                    }
                    //Find any in range
                    int size = arrayOfMax.size();
                    for (int j = 0; j < size; j++) {
                        //Remove min value
                        if (arrayOfMax.get(j) == maxOfArray) {
                            resistanceArray.add(maxOfArray);
                        } else if ((arrayOfMax.get(j) / maxOfArray) < m.getUpperRange()
                                && (arrayOfMax.get(j) / maxOfArray) > m.getLowerRange()) {
                            resistanceArray.add(arrayOfMax.get(j));
                        } else {
                            arrayToSet.add(arrayOfMax.get(j));
                        }
                    }

                    float resistanceLevel = 0;
                    for (int j = 0; j < resistanceArray.size(); j++) {
                        resistanceLevel += resistanceArray.get(j);
                    }
                    resistanceLevel /= resistanceArray.size();
                    if (resistanceArray.size() > 4) {
                        m.getTimeframesDataStore(i).addCriticalLevel(resistanceLevel);
                    }
                    arrayOfMax = arrayToSet;
                }
            }
        }
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

        mainController.setMarkets(markets);

        return mainPane;
    }

    /**
     * Creates all charts necessary to be displayed.
     * Adds all charts the the array list.
     *
     */
    private void createCharts() {
        for (int m = 0; m < markets.size(); m++) {
            for  (int j = 0; j < NUMBER_OF_TIMEFRAMES; j++) {
                markets.get(m).setCandleStickChart(createChart(markets.get(m), j), j);
            }
        }
    }

    /**
     * Creates a chart to be displayed to the UI.
     * @param market data containing values to plot
     * @param j market timeframe to set chart to
     * @return Viewable candle stick chart
     */
    public CandleStickChart createChart(Market market, int j) {
        double xAxisLowerBound = getXAxisLowerBound(market.getTimeframesDataStore(j).getMarketValues());
        double xAxisUpperBound = getXAxisUpperBound(market.getTimeframesDataStore(j).getMarketValues());

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis(xAxisLowerBound, xAxisUpperBound, market.getTimeframesDataStore(j).getChartYAxisTickValue());

        final CandleStickChart bc = new CandleStickChart(xAxis, yAxis);
        // setup chart
        bc.setTitle(market.getIndex() + " Chart");
        xAxis.setLabel(market.getTimeframesDataStore(j).getTimeframe().toString());
        yAxis.setLabel("Price");
        // add starting data
        XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();

        for (int i = 0; i < market.getTimeframesDataStore(j).getMarketValues().size(); i++) {
            series.getData().add(
                    new XYChart.Data<Number, Number>(i,
                            market.getTimeframesDataStore(j).getMarketValues().get(i).getOpen(),
                            new CandleStickChart.CandleStickExtraValues(
                                    market.getTimeframesDataStore(j).getMarketValues().get(i).getClose(),
                                    market.getTimeframesDataStore(j).getMarketValues().get(i).getHigh(),
                                    market.getTimeframesDataStore(j).getMarketValues().get(i).getLow()
                            )
                    )
            );
        }
        ObservableList<XYChart.Series<Number, Number>> chartData = bc.getData();
        if (chartData == null) {
            chartData = FXCollections.observableArrayList(series);
            bc.setData(chartData);
        } else {
            bc.getData().add(series);
        }

        // add horizontal markers
        for (int s = 0; s < market.getTimeframesDataStore(j).getCriticalLevels().size(); s++) {
            XYChart.Data<Number, Number> horizontalMarker = new XYChart.Data<>
                    (0, market.getTimeframesDataStore(j).getCriticalLevels().get(s));
            bc.addHorizontalValueMarker(horizontalMarker); //This can be used to resistance and support levels
        }
        return bc;
    }

    /**
     * Method to return the lower bound.
     * @param values Market values from the raw data.
     * @return lowest value (swing low).
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
     * Method to return the upper bound.
     * @param values Market values from the raw data.
     * @return highest value (swing high).
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


    /**
     * @return markets array list.
     */
    public ArrayList<Market> getMarkets() { return markets; }

    /**
     * @param markets array list.
     */
    public void setMarkets(ArrayList<Market> markets) { this.markets = markets; }

    public static void main(String[] args) { launch(args); }
}
