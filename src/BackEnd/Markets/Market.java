package BackEnd.Markets;

import javafx.beans.property.SimpleStringProperty;

public class Market {
    private String index;
    private MarketTrend trend;
    private Status status;
    private MarketTimeframe currentTimeFrame = MarketTimeframe.DAY;
    private static CandleStickChart oneHourCandleStickChart;
    private static CandleStickChart fourHourCandleStickChart;
    private static CandleStickChart dayCandleStickChart;

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
        oneHourCandleStickChart = new CandleStickChart();
        fourHourCandleStickChart = new CandleStickChart();
        dayCandleStickChart = new CandleStickChart();
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
}
