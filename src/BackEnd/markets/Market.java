package BackEnd.markets;

import BackEnd.chart.CandleStickChart;
import BackEnd.patternRecognition.Patterns;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Market {
    private String index;
    private MarketTrend trend;
    private Status status;
    private MarketTimeframe currentTimeFrame = MarketTimeframe.DAY;
    private int segment;
    private double range;

    /**
     * Three candle stick charts for each time frame.
     */
    private CandleStickChart oneHourCandleStickChart;
    private CandleStickChart fourHourCandleStickChart;
    private CandleStickChart dayCandleStickChart;

    /**
     * Array list to store a list of market values.
     */
    private ArrayList<MarketValues> oneHourValues = new ArrayList<>();
    private ArrayList<MarketValues> fourHourValues = new ArrayList<>();
    private ArrayList<MarketValues> oneDayValues = new ArrayList<>();

    private ArrayList<Double> oneHourSR = new ArrayList<>();
    private ArrayList<Double> fourHourSR;
    private ArrayList<Double> oneDaySR;

    private Patterns patternsList;

    /**
     * Data columns for table.
     */
    private SimpleStringProperty tableIndexColumn, tableStatusColumn, tableTrendColumn;

    public Market(String index, Status status, MarketTrend trend) {
        this.index = index;
        this.status = status;
        this.trend = trend;

        this.tableIndexColumn = new SimpleStringProperty(index);
        this.tableStatusColumn = new SimpleStringProperty(status.toString());
        this.tableTrendColumn = new SimpleStringProperty(trend.toString());

    }

    /**
     * Setters and getters for the chart created from the market data.
     */
    public CandleStickChart getOneHourCandleStickChart() {
        return oneHourCandleStickChart;
    }
    public void setOneHourCandleStickChart(CandleStickChart oneHourCandleStickChart) {
        this.oneHourCandleStickChart = oneHourCandleStickChart;
    }

    public CandleStickChart getFourHourCandleStickChart() {
        return fourHourCandleStickChart;
    }
    public void setFourHourCandleStickChart(CandleStickChart fourHourCandleStickChart) {
        this.fourHourCandleStickChart = fourHourCandleStickChart;
    }

    public CandleStickChart getDayCandleStickChart() {
        return dayCandleStickChart;
    }
    public void setDayCandleStickChart(CandleStickChart dayCandleStickChart) {
        this.dayCandleStickChart = dayCandleStickChart;
    }

    /**
     * Setters and getters for table columns.
     */
    public String getTableIndexColumn() { return tableIndexColumn.get(); }
    public SimpleStringProperty tableIndexColumnProperty() { return tableIndexColumn; }
    public void setTableIndexColumn(String tableIndexColumn) { this.tableIndexColumn.set(tableIndexColumn); }

    public String getTableStatusColumn() { return tableStatusColumn.get(); }
    public SimpleStringProperty tableStatusColumnProperty() { return tableStatusColumn; }
    public void setTableStatusColumn(String tableStatusColumn) { this.tableStatusColumn.set(tableStatusColumn); }

    public String getTableTrendColumn() { return tableTrendColumn.get(); }
    public SimpleStringProperty tableTrendColumnProperty() { return tableTrendColumn; }
    public void setTableTrendColumn(String tableTrendColumn) { this.tableTrendColumn.set(tableTrendColumn); }


    /**
     * Setters and getters for market index, status and timeframe.
     */
    public String getIndex() { return index; }
    public void setIndex(String index) { this.index = index; }

    public MarketTrend getTrend() {return trend; }
    public void setTrend(MarketTrend trend) { this.trend = trend; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public MarketTimeframe getCurrentTimeFrame() { return currentTimeFrame; }
    public void setCurrentTimeFrame(MarketTimeframe currentTimeFrame) { this.currentTimeFrame = currentTimeFrame; }

    /**
     * Getters to retrieve markets values for each timeframe.
     */
    public ArrayList<MarketValues> getOneHourValues() { return oneHourValues; }
    public ArrayList<MarketValues> getFourHourValues() { return fourHourValues; }
    public ArrayList<MarketValues> getOneDayValues() { return oneDayValues; }

    /**
     * Adders used to add each value(candle) to the specified array list.
     * Open, close, high and low values added to each new object of market values.
     *
     * @param open  The price the candle opens at.
     * @param close The price the candle closes at.
     * @param high  The highest the price reaches in a single candle.
     * @param low   The lowest the price reaches in a single candle.
     */
    public void addOneHourValues(float open, float close, float high, float low) {
        oneHourValues.add(new MarketValues(open, close, high, low));
    }

    public void addFourHourValues(float open, float close, float high, float low) {
        fourHourValues.add(new MarketValues(open, close, high, low));
    }

    public void addOneDayValues(float open, float close, float high, float low) {
        oneDayValues.add(new MarketValues(open, close, high, low));
    }

    public void addOneHourSR(double value) {
        oneHourSR.add(value);
    }

    public ArrayList<Double> getOneHourSR() {
        return oneHourSR;
    }
    public void setOneHourSR(ArrayList<Double> oneHSR) {
        this.oneHourSR = oneHSR;
    }

    public void addPattern(Pattern p) {
    }

    public int getSegment() { return segment; }
    public void setSegment(int segment) { this.segment = segment; }

    public double getRange() { return range; }
    public void setRange(double range) { this.range = range; }

    /**
     * This class is used to create a single candle with each of its four necessary prices.
     */
    public static class MarketValues {
        private double open;
        private double close;
        private double high;
        private double low;


        public MarketValues(double open, double close, double high, double low) {
            this.open = open;
            this.close = close;
            this.high = high;
            this.low = low;
        }

        public double getOpen() { return open; }
        public double getClose() { return close; }
        public double getHigh() { return high; }
        public double getLow() { return low; }

    }

    /**
     * Class to store the support/resistance levels
     */
//    public static class SupportResistance {
//        private double firstResistance;
//        private double secondResistance;
//        private double firstSupport;
//        private double secondSupport;
//
//        public SupportResistance(double firstR, double secondR, double firstS, double secondS) {
//            this.firstResistance = firstR;
//            this.secondResistance = secondR;
//            this.firstSupport = firstS;
//            this.secondSupport = secondS;
//        }
//
//        public double getFirstResistance() { return firstResistance; }
//        public void setFirstResistance(double firstResistance) { this.firstResistance = firstResistance; }
//        public double getSecondResistance() { return secondResistance; }
//        public void setSecondResistance(double secondResistance) { this.secondResistance = secondResistance; }
//        public double getFirstSupport() { return firstSupport; }
//        public void setFirstSupport(double firstSupport) { this.firstSupport = firstSupport; }
//        public double getSecondSupport() { return secondSupport; }
//        public void setSecondSupport(double secondSupport) { this.secondSupport = secondSupport; }
//
//
//    }

}
