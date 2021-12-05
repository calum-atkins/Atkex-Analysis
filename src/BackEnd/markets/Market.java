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
    private double lowerRange, upperRange;


    /**
     * Three candle stick charts for each time frame.
     */
    private ArrayList<StoreData> timeframesDataStore = new ArrayList<StoreData>();


    public StoreData getTimeframesDataStore(int i) {
        return timeframesDataStore.get(i);
    }
    public void addTimeframesDataStore(StoreData dataStore) { timeframesDataStore.add(dataStore); }

    public void setTimeframesStoreSize(int i) {
        for (int j = 0; j < i; j++) {
            timeframesDataStore.add(new StoreData());
        }
    }

    public int getSegment() { return segment; }
    public void setSegment(int segment) { this.segment = segment; }


    /**
     * Array list to store a list of market values.
     */
    private Patterns patternsList;//do same as store data

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

    public void setCandleStickChart(CandleStickChart candleStickChart, int i) {
        timeframesDataStore.get(i).setCandleStickChart(candleStickChart);
    }
    public CandleStickChart getCandleStickChart(int i) {
        return timeframesDataStore.get(i).getCandleStickChart();
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
     * Adders used to add each value(candle) to the specified array list.
     * Open, close, high and low values added to each new object of market values.
     *
     * @param open  The price the candle opens at.
     * @param close The price the candle closes at.
     * @param high  The highest the price reaches in a single candle.
     * @param low   The lowest the price reaches in a single candle.
     */

    public void addPattern(Pattern p) {
    }

    public double getLowerRange() { return lowerRange; }
    public void setLowerRange(double range) {
        range = 1 - (range / 100);
        this.lowerRange = range;
    }
    public double getUpperRange() { return upperRange; }
    public void setUpperRange(double range) {
        range = (range / 100) + 1;
        this.upperRange = range;
    }

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

    public static class StoreData  {
        private CandleStickChart candleStickChart;
        private ArrayList<MarketValues> marketValues = new ArrayList<>();
        private ArrayList<Double> criticalLevels = new ArrayList<>();
        private ArrayList<Float> minSegments = new ArrayList<>();
        private String filePath;
        private MarketTimeframe timeframe;
        private double chartYAxisTickValue;

        public double getChartYAxisTickValue() { return chartYAxisTickValue; }
        public void setChartYAxisTickValue(double chartYAxisTickValue) { this.chartYAxisTickValue = chartYAxisTickValue; }

        public MarketTimeframe getTimeframe() { return timeframe; }
        public void setTimeframe(MarketTimeframe timeframe) { this.timeframe = timeframe; }

        public String getFilePath() { return filePath; }
        public void setFilePath(String filePath) { this.filePath = filePath; }

        public CandleStickChart getCandleStickChart() { return candleStickChart; }
        public void setCandleStickChart(CandleStickChart candleStickChart) { this.candleStickChart = candleStickChart; }
        public ArrayList<MarketValues> getMarketValues() { return marketValues; }
        public void addMarketValue(MarketValues m) {
            marketValues.add(m);
        }
        public void setMarketValues(ArrayList<MarketValues> marketValues) { this.marketValues = marketValues; }
        public ArrayList<Double> getCriticalLevels() { return criticalLevels; }
        public void addCriticalLevel(double c) {
            criticalLevels.add(c);
        }
        public void setCriticalLevels(ArrayList<Double> criticalLevels) { this.criticalLevels = criticalLevels; }
        public ArrayList<Float> getMinSegments() { return minSegments; }
        public void addMinSegment(float s) {
            minSegments.add(s);
        }
        public void setMinSegments(ArrayList<Float> minSegments) { this.minSegments = minSegments;
        }
    }
}
