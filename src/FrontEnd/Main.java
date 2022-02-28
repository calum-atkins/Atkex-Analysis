package FrontEnd;

import BackEnd.markets.Market;
import BackEnd.markets.MarketTimeframe;
import BackEnd.markets.MarketTrend;
import BackEnd.markets.Status;
import BackEnd.chart.CandleStickChart;

import BackEnd.patternRecognition.Patterns;
import BackEnd.patternRecognition.algorithms.CriticalLevels;
import BackEnd.patternRecognition.algorithms.MarketTrends;
import BackEnd.patternRecognition.availablePatterns.AscendingTriangle;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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

    /** Variable declaration to hold the number of markets and number of timeframes used */
    private int marketsSize;
    private static final int NUMBER_OF_TIMEFRAMES = 3;

    /** Constant variables for convenient use */
    private final String rawDataLocation = "resources/marketsRawData";
    private final String rawDataFileType = ".csv";
    private final String marketPreferences = "resources/marketPreferences/preferences.csv";

    private boolean criticalLevelsToggle = false;

    /** start method to initialise application */
    @Override
    public void start(Stage stage) throws Exception{
        System.out.println("Initialising..");
        stage.setTitle("Atkex");
        stage.getIcons().add(new Image("/img/atkex_logo_dark.png"));
        stage.setResizable(true);
        //stage.setMaximized(true);

        /** Load loading screen */
//        stage.setScene(
//                createScene(
//                        loadLoadingPane()
//                )
//        );
//        stage.show();


        /** Load number of markets and initialise array list */
        File directoryP = new File(rawDataLocation);
        String contents[] = directoryP.list();
        for (int b = 0; b < contents.length; b++) {
            markets.add(new Market(contents[b], Status.PENDING, MarketTrend.NO_TREND));
            markets.get(b).setTimeframesStoreSize(NUMBER_OF_TIMEFRAMES);
        }
        marketsSize = contents.length;
        System.out.println("Array list initialised.");

        /** Load preferences */
        loadPreferences();
        System.out.println("Preferences loaded successfully.");

        /** Load market data */
        loadSaveData();
        System.out.println("Market data loaded successfully.");

        /** Generate Resistance/support */
        generateCriticalLevels();
        System.out.println("Critical levels generated.");

        /** Generate Trends */
        generateTrends();
        System.out.println("Trend lines generated.");

        /** Generate Patterns */
        generatePatterns();
        System.out.println("Patterns Generated.");

        /** Create charts */
        createCharts();
        System.out.println("Charts created.");

        /** Load stage */
        System.out.println("Displaying UI.");
        stage.setScene(
                createScene(
                        loadMainPane()
                )
        );
        stage.show();
    }

    /**
     * Method to load segment, range and zig zag percentage preferences.
     */
    private void loadPreferences() {
        String line = "";
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader(marketPreferences));
            while ((line = br.readLine()) != null) {
                if (!line.equals("index,segments,range,zigzag,pip_multiply")) {
                    String[] values = line.split(",");
                    for (int i = 0; i < marketsSize;i++) {
                        if (values[0].equals(markets.get(i).getIndex())) {
                            markets.get(i).setSegment(Integer.parseInt(values[1]));

                            markets.get(i).setLowerRange(Double.parseDouble(values[2]));
                            markets.get(i).setUpperRange(Double.parseDouble(values[2]));

                            markets.get(i).setTrendIndicatorPercentage(Double.parseDouble(values[3]));

                            markets.get(i).setPipMultiply(Integer.parseInt(values[4]));
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
                /** Set the path depending on the file name (determines the timeframe) */
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

                /** This block is used to store all the time and market prices to individual time frames as market values */
                try {
                    BufferedReader br = new BufferedReader(
                            new FileReader(markets.get(marketNum).getTimeframesDataStore(j).getFilePath()));
                    int segmentIncrement = 0;
                    float min = 0;
                    float max = 0;
                    float[] valueArray = new float[markets.get(marketNum).getSegment()];
                    minOverall = 0;
                    maxOverall = 0;
                    while ((line = br.readLine()) != null) {
                        if (!line.equals("time,open,high,low,close")) {
                            String[] values = line.split(",");

                            markets.get(marketNum).getTimeframesDataStore(j).setCurrentPrice(Float.parseFloat(values[4]));
                            markets.get(marketNum).getTimeframesDataStore(j).addMarketValue(
                                    new Market.MarketValues(
                                            values[0],
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

                            /** This block finds the min and max price for every 'x' amount of bars
                             * x = number of candles per segment, defined in preferences
                             */
                            if (segmentIncrement == 0) {
                                min = Float.parseFloat(values[3]);
                            } else if (Float.parseFloat(values[3]) < min) {
                                min = Float.parseFloat(values[3]);
                            }
                            if (segmentIncrement == 0) {
                                max = Float.parseFloat(values[2]);
                            } else if (Float.parseFloat(values[2]) > max) {
                                max = Float.parseFloat(values[2]);
                            }
                            segmentIncrement++;
                            if (segmentIncrement == markets.get(marketNum).getSegment()) {
                                segmentIncrement = 0;
                                markets.get(marketNum).getTimeframesDataStore(j).addMinSegment(min);
                                markets.get(marketNum).getTimeframesDataStore(j).addMaxSegment(max);
                            }
                        }
                    }
                    markets.get(marketNum).getTimeframesDataStore(j).setChartYAxisTickValue((maxOverall - minOverall) /10);
                    markets.get(marketNum).getTimeframesDataStore(j).setMaximumPrice(maxOverall);
                    markets.get(marketNum).getTimeframesDataStore(j).setMinimumPrice(minOverall);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Method to generate critical levels for support and resistance.
     */
    private void generateCriticalLevels() {
        markets = CriticalLevels.generateSupportLevels(markets);
        markets = CriticalLevels.generateResistanceLevels(markets);

        for (Market m : markets) {
            for (int i = 0; i < NUMBER_OF_TIMEFRAMES; i++) {
                for (int j = 0; j < m.getTimeframesDataStore(i).getSupport().size(); j++) {
                    for (int k = 0; k < m.getTimeframesDataStore(i).getResistance().size(); k++) {
                        float range = (m.getTimeframesDataStore(i).getMaximumPrice()
                                - m.getTimeframesDataStore(i).getMinimumPrice()) / 100;
                        if (((m.getTimeframesDataStore(i).getSupport().get(j) /
                                m.getTimeframesDataStore(i).getResistance().get(k)) < m.getUpperRange())
                                && (m.getTimeframesDataStore(i).getSupport().get(j) /
                                m.getTimeframesDataStore(i).getResistance().get(k)) > m.getLowerRange()) {
                            m.getTimeframesDataStore(i).addCriticalLevel(
                                    (m.getTimeframesDataStore(i).getSupport().get(j) +
                                            m.getTimeframesDataStore(i).getResistance().get(k)) / 2);
                            m.getTimeframesDataStore(i).addResistanceLevel(
                                    (m.getTimeframesDataStore(i).getSupport().get(j) +
                                            m.getTimeframesDataStore(i).getResistance().get(k)) / 2);
                        }
                        //Adding level to array
                        //System.out.println(m.getTimeframesDataStore(i).getResistance().get(k));
                    }
                    //Adding level to array
                    //m.addSupportResistanceLevels(m.getTimeframesDataStore(i).getSupport().get(j));
                }

            }
        }



    }

    /**
     * Method to generate trend for each market.
     */
    private void generateTrends() {
        for (Market m : markets) {
            m.setTrend(MarketTrends.dayTrend(m.getTimeframesDataStore(0),
                    m.getTrendIndicatorPercentage()));
        }
    }

    /**
     * Method to generate patterns for each chart.
     */
    private void generatePatterns() {
        for (Market m : markets) {
            m.setPatternsList(AscendingTriangle.findAscendingTriangles(m));
            for (Patterns p : m.getPatternsList()) {
                m.modifyPips(p.getProfitLoss());
            }

        }
        /** Compute final pip returned value */

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

        mainPane.setPrefHeight(800);
        mainPane.setPrefWidth(1200);
        MainController mainController = loader.getController();

        ChartSelector.setMainController(mainController);

        mainController.setMarkets(markets);

        return mainPane;
    }

    /**
     * Loader to create a pane for loading screen
     * @return the loading screen pane
     * @throws IOException if the pane cannot be loaded
     */
    private Pane loadLoadingPane() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        Pane mainPane = (Pane) loader.load(
                getClass().getResourceAsStream(
                        "loadingScreen.fxml"
                )
        );

        mainPane.setPrefHeight(300);
        mainPane.setPrefWidth(450);
        LoadingController loadingController = loader.getController();

        loadingController.test("test");

        return mainPane;
    }

    /**
     * Creates all charts necessary to be displayed.
     * Adds all charts the the array list.
     *
     */
    private void createCharts() {
        for (Market m : markets) {
            System.out.println("Creating " + m.getIndex() + " chart.");
            for  (int j = 0; j < NUMBER_OF_TIMEFRAMES; j++) {
                m.setCandleStickChartCriticalLevels(createChart(m, j, 1), j);
                m.setCandleStickChartPatternIdentifiers(createChart(m, j, 2), j);
                m.setCandleStickChartEmpty(createChart(m, j, 0), j);
            }
        }
    }

    /**
     * Creates a chart to be displayed to the UI.
     * @param market data containing values to plot
     * @param j market timeframe to set chart to
     * @param {0, 1, 2} Chart view to create
     *     0 = Empty chart
     *     1 = Critical Levels
     *     2 = Pattern identifiers
     * @return Viewable candle stick chart
     */
    public CandleStickChart createChart(Market market, int j, int chartIndicator) {
        double xAxisLowerBound = getXAxisLowerBound(market.getTimeframesDataStore(j).getMarketValues());
        double xAxisUpperBound = getXAxisUpperBound(market.getTimeframesDataStore(j).getMarketValues());

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis(xAxisLowerBound, xAxisUpperBound, market.getTimeframesDataStore(j).getChartYAxisTickValue());

        final CandleStickChart bc = new CandleStickChart(xAxis, yAxis);
        /** Setup chart. */
        bc.setTitle(market.getIndex());
        String tf = "";
        if (market.getTimeframesDataStore(j).getTimeframe() == MarketTimeframe.DAY) {
            tf = "(One day)";
        } else if (market.getTimeframesDataStore(j).getTimeframe() == MarketTimeframe.FOUR_HOUR) {
            tf = "(Four hours)";
        } else if (market.getTimeframesDataStore(j).getTimeframe() == MarketTimeframe.ONE_HOUR) {
            tf = "(One hour)";
        }
        xAxis.setLabel("Number of candles " + tf);
        yAxis.setLabel("Price ratio");
        /** Add starting data. */
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

        /** Add critical levels to the chart. */
        if (chartIndicator == 0) {
            return bc;
        } else if (chartIndicator == 1) {
            for (int s = 0; s < market.getTimeframesDataStore(j).getCriticalLevels().size(); s++) {
                XYChart.Data<Number, Number> horizontalMarker = new XYChart.Data<>
                        (0, market.getTimeframesDataStore(j).getCriticalLevels().get(s));

                bc.addHorizontalValueMarker(horizontalMarker,
                        market.getTimeframesDataStore(j).getCurrentPrice()); //This can be used to resistance and support levels
            }
            return bc;
        } else if (chartIndicator == 2) {

            /** Add vertical lines */
            for (Patterns pattern : market.getPatternsList()) {
                XYChart.Data<Number, Number> verticalMarker = new XYChart.Data<>
                        (pattern.getEntryCandle(), 0);
                bc.addVerticalValueMarker(verticalMarker,
                        pattern.getEntryCandle());
            }

//            for (int i = 0; i < 10; i++) {
//                XYChart.Data<Number, Number> verticalMarker = new XYChart.Data<>
//                        (, 0);
//                bc.addVerticalValueMarker(verticalMarker,
//                        100);
//            }
            return bc;
        }
        return null;
    }

    private double getYAxisUpperBound(ArrayList<Market.MarketValues> values) {
        double yAxisUpperBound = 0;
        for (int a = 0; a < values.size(); a++) {

            if (yAxisUpperBound == 0) {
                yAxisUpperBound = values.get(a).getHigh();
            } else {
                if (values.get(a).getHigh() > yAxisUpperBound) {
                    yAxisUpperBound = values.get(a).getHigh();
                }
            }
        }
        return yAxisUpperBound + 50;
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

    /**
     * @return number of timeframes available within the application
     */
    public static int getNumberOfTimeframes() {
        return NUMBER_OF_TIMEFRAMES;
    }

    /** Main method */
    public static void main(String[] args) { launch(args); }
}
