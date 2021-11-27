package BackEnd.Markets;

import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;

public class Market {
    private String index;
    private MarketTrend trend;
    private Status status;
    private MarketTimeframe currentTimeFrame = MarketTimeframe.DAY;
    private static CandleStickChart oneHourCandleStickChart;
    private static CandleStickChart fourHourCandleStickChart;
    private static CandleStickChart dayCandleStickChart;

    private ArrayList<MarketValues> oneHourValues = new ArrayList<>();
    private ArrayList<MarketValues> fourHourValues = new ArrayList<>();
    private ArrayList<MarketValues> oneDayValues = new ArrayList<>();




    //Data columns for tables
    private SimpleStringProperty tableIndexColumn, tableStatusColumn, tableTrendColumn;

    public Market(String index, Status status, MarketTrend trend) {
        this.index = index;
        this.status = status;
        this.trend = trend;

        //Table View
        this.tableIndexColumn = new SimpleStringProperty(index);
        this.tableStatusColumn = new SimpleStringProperty(status.toString());
        this.tableTrendColumn = new SimpleStringProperty(trend.toString());

    }


    //Method to determine what chart to create under what time frame
    public static void createCandleStickChart() {
        //Find excel file, pass through here to
//        oneHourCandleStickChart = new CandleStickChart();
//        fourHourCandleStickChart = new CandleStickChart();
//        dayCandleStickChart = new CandleStickChart();
    }

    public static CandleStickChart getOneHourCandleStickChart() {
        return oneHourCandleStickChart;
    }

    public static void setOneHourCandleStickChart(CandleStickChart oneHourCandleStickChart) {
        Market.oneHourCandleStickChart = oneHourCandleStickChart;
    }

    public static CandleStickChart getFourHourCandleStickChart() {
        return fourHourCandleStickChart;
    }

    public static void setFourHourCandleStickChart(CandleStickChart fourHourCandleStickChart) {
        Market.fourHourCandleStickChart = fourHourCandleStickChart;
    }

    public static CandleStickChart getDayCandleStickChart() {
        return dayCandleStickChart;
    }

    public static void setDayCandleStickChart(CandleStickChart dayCandleStickChart) {
        Market.dayCandleStickChart = dayCandleStickChart;
    }

    //Table columns
    public String getTableIndexColumn() {
        return tableIndexColumn.get();
    }

    public SimpleStringProperty tableIndexColumnProperty() {
        return tableIndexColumn;
    }

    public void setTableIndexColumn(String tableIndexColumn) {
        this.tableIndexColumn.set(tableIndexColumn);
    }

    public String getTableStatusColumn() {
        return tableStatusColumn.get();
    }

    public SimpleStringProperty tableStatusColumnProperty() {
        return tableStatusColumn;
    }

    public void setTableStatusColumn(String tableStatusColumn) {
        this.tableStatusColumn.set(tableStatusColumn);
    }

    public String getTableTrendColumn() {
        return tableTrendColumn.get();
    }

    public SimpleStringProperty tableTrendColumnProperty() {
        return tableTrendColumn;
    }

    public void setTableTrendColumn(String tableTrendColumn) {
        this.tableTrendColumn.set(tableTrendColumn);
    }

    //here

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public MarketTrend getTrend() {
        return trend;
    }

    public void setTrend(MarketTrend trend) {
        this.trend = trend;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public MarketTimeframe getCurrentTimeFrame() {
        return currentTimeFrame;
    }

    public void setCurrentTimeFrame(MarketTimeframe currentTimeFrame) {
        this.currentTimeFrame = currentTimeFrame;
    }

    //setters and getters for market values
    public ArrayList<MarketValues> getOneHourValues() {
        return oneHourValues;
    }

    public void addOneHourValues(float open, float close, float high, float low) {
        oneHourValues.add(new MarketValues(open, close, high, low));
    }

    public ArrayList<MarketValues> getFourHourValues() {
        return fourHourValues;
    }

    public void addFourHourValues(float open, float close, float high, float low) {
        fourHourValues.add(new MarketValues(open, close, high, low));
    }

    public ArrayList<MarketValues> getOneDayValues() {
        return oneDayValues;
    }

    public void addOneDayValues(float open, float close, float high, float low) {
        oneDayValues.add(new MarketValues(open, close, high, low));
    }

    //Class to store the values
    public static class MarketValues {
        private float open;
        private float close;
        private float high;
        private float low;

        public MarketValues(float open, float close, float high, float low) {
            this.open = open;
            this.close = close;
            this.high = high;
            this.low = low;
        }

        public float getOpen() { return open; }

        public float getClose() {
            return close;
        }

        public float getHigh() {
            return high;
        }

        public float getLow() {
            return low;
        }
    }
}
