package BackEnd.markets;

import BackEnd.chart.CandleStickChart;
import BackEnd.patternRecognition.Patterns;
import BackEnd.patternRecognition.availablePatterns.AscendingTriangle;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * This class is used to hold data on each of the markets
 * on each time frame entered.
 */
public class Market {

    /** Variables to declare data on the market */
    private String index;
    private MarketTrend trend;
    private Status status;
    private MarketTimeframe currentTimeFrame = MarketTimeframe.DAY;
    private int segment;
    private double lowerRange, upperRange;
    private double trendIndicatorPercentage;
    private ArrayList<Double> supportResistanceLevels = new ArrayList<>();

    public ArrayList<Double> getSupportResistanceLevels() {
        return supportResistanceLevels;
    }

    public void setSupportResistanceLevels(ArrayList<Double> supportResistanceLevels) {
        this.supportResistanceLevels = supportResistanceLevels;
    }

    public void addSupportResistanceLevels(Double supportResistanceLevel) {
        supportResistanceLevels.add(supportResistanceLevel);
    }

    /**
     * Array list of StoreData holding information on (x) amount
     * of timeframes the user has used.
     * 0 - ONE DAY
     * 1 - FOUR HOUR
     * 2 - ONE HOUR
     * */
    private ArrayList<StoreData> timeframesDataStore = new ArrayList<StoreData>();

    /** Array list to store a list of market values. */
    private ArrayList<Patterns> patternsList = new ArrayList<Patterns>();//do same as store data

    /** Data columns for table. */
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
     * Setter, Getter and Adder for the data stored on each timeframe.
     * @param i timeframe index to retrieve.
     * @return StoreData of specified time frame.
     */
    public StoreData getTimeframesDataStore(int i) {
        return timeframesDataStore.get(i);
    }

    /**
     * @param dataStore timeframe of data to add to the array list.
     */
    public void addTimeframesDataStore(StoreData dataStore) { timeframesDataStore.add(dataStore); }
    /**
     * Setter used to initialise array for number of timeframes
     */
    public void setTimeframesStoreSize(int i) {
        for (int j = 0; j < i; j++) {
            timeframesDataStore.add(new StoreData());
        }
    }

    /** Mehtod to ass a new pattern to the list */
    public void addPattern(Pattern p) {
    }

    /**
     * Setters and getters for the charts created from the market data.
     * @param i Timeframe index to set chart to
     */
    public void setCandleStickChart(CandleStickChart candleStickChart, int i) {
        timeframesDataStore.get(i).setCandleStickChart(candleStickChart);
    }
    public CandleStickChart getCandleStickChart(int i) {
        return timeframesDataStore.get(i).getCandleStickChart();
    }

    public void setCandleStickChartEmpty(CandleStickChart candleStickChart, int i) {
        timeframesDataStore.get(i).setCandleStickChartEmpty(candleStickChart);
    }
    public CandleStickChart getCandleStickChartEmpty(int i) {
        return timeframesDataStore.get(i).getCandleStickChartEmpty();
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
     * Setters and getters for market index, status, timeframe, segment and trend percentage.
     */
    public String getIndex() { return index; }
    public void setIndex(String index) { this.index = index; }

    public MarketTrend getTrend() {return trend; }
    public void setTrend(MarketTrend trend) { this.trend = trend; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public MarketTimeframe getCurrentTimeFrame() { return currentTimeFrame; }
    public void setCurrentTimeFrame(MarketTimeframe currentTimeFrame) { this.currentTimeFrame = currentTimeFrame; }

    public int getSegment() { return segment; }
    public void setSegment(int segment) { this.segment = segment; }

    public double getTrendIndicatorPercentage() { return trendIndicatorPercentage; }
    public void setTrendIndicatorPercentage(double trendIndicatorPercentage) { this.trendIndicatorPercentage = trendIndicatorPercentage; }

    /**
     * Setter and getters for lower range and upper range value.
     * Values must be calculated from the range value in preferences.csv
     */
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
     * Added and getter for patterns identified
     */
    public ArrayList<Patterns> getPatternsList() { return patternsList; }
    public void setPatternsList(ArrayList<Patterns> patternsList) { this.patternsList = patternsList; }

    //public void addPatternAT(AscendingTriangle pattern) { patternsList.add(pattern); }


    /**
     * This class is used to create a single candle with each of its four necessary prices.
     */
    public static class MarketValues {
        private Date date;
        private Time time;
        private double open;
        private double close;
        private double high;
        private double low;

        public MarketValues(String dateT, double open, double close, double high, double low) {
            String[] dateTime = dateT.split("T");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            try {
                this.date = sdf.parse(dateT);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            this.open = open;
            this.close = close;
            this.high = high;
            this.low = low;
        }

        public Date getDate() { return date; }
        public double getOpen() { return open; }
        public double getClose() { return close; }
        public double getHigh() { return high; }
        public double getLow() { return low; }
    }

    /**
     * This  class is used to hold data for each individual timeframe.
     */
    public static class StoreData  {
        private CandleStickChart candleStickChart;
        private CandleStickChart candleStickChartEmpty;
        private ArrayList<MarketValues> marketValues = new ArrayList<>();
        private ArrayList<Double> criticalLevels = new ArrayList<>();
        private ArrayList<Double> resistanceLevels = new ArrayList<>();
        private ArrayList<Double> supportLevels = new ArrayList<>();
        private ArrayList<Double> tempSupportLevels = new ArrayList<>();
        private ArrayList<Double> tempResistanceLevels = new ArrayList<>();
        private ArrayList<Float> minSegments = new ArrayList<>();
        private ArrayList<Float> maxSegments = new ArrayList<>();
        private float minimumPrice;
        private float maximumPrice;
        private String filePath;
        private MarketTimeframe timeframe;
        private double chartYAxisTickValue;
        private float currentPrice;

        private ArrayList<AscendingTriangle> atList = new ArrayList<AscendingTriangle>();

        public void addAT(AscendingTriangle at) { atList.add(at); }
        public ArrayList<AscendingTriangle> getAtList() {
            return atList;
        }

        public float getCurrentPrice() { return currentPrice; }
        public void setCurrentPrice(float currentPrice) { this.currentPrice = currentPrice; }

        public double getChartYAxisTickValue() { return chartYAxisTickValue; }
        public void setChartYAxisTickValue(double chartYAxisTickValue) { this.chartYAxisTickValue = chartYAxisTickValue; }

        public MarketTimeframe getTimeframe() { return timeframe; }
        public void setTimeframe(MarketTimeframe timeframe) { this.timeframe = timeframe; }

        public String getFilePath() { return filePath; }
        public void setFilePath(String filePath) { this.filePath = filePath; }

        public CandleStickChart getCandleStickChart() { return candleStickChart; }
        public void setCandleStickChart(CandleStickChart candleStickChart) { this.candleStickChart = candleStickChart; }

        public CandleStickChart getCandleStickChartEmpty() { return candleStickChartEmpty; }
        public void setCandleStickChartEmpty(CandleStickChart candleStickChart) { this.candleStickChartEmpty = candleStickChart; }

        public ArrayList<MarketValues> getMarketValues() { return marketValues; }
        public void addMarketValue(MarketValues m) {
            marketValues.add(m);
        }

        public void setMarketValues(ArrayList<MarketValues> marketValues) { this.marketValues = marketValues; }

        public ArrayList<Double> getResistanceLevels() { return resistanceLevels; }
        public void addResistanceLevel(double r) {
            resistanceLevels.add(r);
        }
        public void setResistanceLevels(ArrayList<Double> resistanceLevels) { this.resistanceLevels = resistanceLevels; }

        public ArrayList<Double> getCriticalLevels() { return criticalLevels; }
        public void addCriticalLevel(double c) {
            criticalLevels.add(c);
        }
        public void setCriticalLevels(ArrayList<Double> criticalLevels) { this.criticalLevels = criticalLevels; }

        public ArrayList<Float> getMinSegments() { return minSegments; }
        public void addMinSegment(float s) {
            minSegments.add(s);
        }
        public void setMinSegments(ArrayList<Float> minSegments) { this.minSegments = minSegments; }

        public ArrayList<Float> getMaxSegments() { return maxSegments; }
        public void addMaxSegment(float s) {
            maxSegments.add(s);
        }
        public void setMaxSegments(ArrayList<Float> maxSegments) { this.minSegments = maxSegments; }

        public ArrayList<Double> getSupport() { return tempSupportLevels; }
        public void addSupport(double s) { tempSupportLevels.add(s); }
        public ArrayList<Double> getResistance() { return tempResistanceLevels; }
        public void addResistance(double s) { tempResistanceLevels.add(s); }

        public float getMinimumPrice() { return minimumPrice; }
        public void setMinimumPrice(float minimumPrice) { this.minimumPrice = minimumPrice; }
        public float getMaximumPrice() { return maximumPrice; }
        public void setMaximumPrice(float maximumPrice) { this.maximumPrice = maximumPrice; }
    }
}
