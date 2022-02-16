package BackEnd.patternRecognition;

import BackEnd.markets.Market;
import BackEnd.markets.MarketTimeframe;
import BackEnd.markets.Status;
import BackEnd.patternRecognition.availablePatterns.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class is used to store all the patterns detected on each market.
 */
public class Patterns {
    private String timeframe;
    private Status type;
    private int profitLoss;
    private int startCandle;
    private int entryCandle;
    private String duration;
    private Date start;
    private Double end;

    private static ArrayList<AscendingTriangle> ascendingTriangleList;
    private ArrayList<DescendingTriangle> descendingTriangleList;
    private ArrayList<DoubleBottom> doubleBottomList;
    private ArrayList<DoubleTop> doubleTopList;
    private ArrayList<Pennant> pennantList;
    private ArrayList<Wedge> wedgeList;

    private ArrayList<Patterns> patternsList;

    public Patterns(int tf, Status status, Integer profit, Integer startCandle, Integer entryCandle, String duration) {
        switch (tf) {
            case 2: this.timeframe = "1h"; break;
            case 1: this.timeframe = "4h"; break;
            case 0: this.timeframe = "1d"; break;
        }
        this.type = status;
        this.profitLoss = profit;
        this.startCandle = startCandle;
        this.entryCandle = entryCandle;
        this.duration = duration;

        this.tableTimeframeColumn = new SimpleStringProperty(timeframe);
        this.tablePatternNumberColumn = new SimpleStringProperty(type.toString());
        this.tableProfitLossColumn = new SimpleIntegerProperty(profit);
        this.tableStartCandleColumn = new SimpleIntegerProperty(startCandle);
        this.tableEntryCandleColumn = new SimpleIntegerProperty(entryCandle);
        this.tableDurationColumn = new SimpleStringProperty(String.valueOf(duration));
    }

    public Patterns(String tf, Status status, Integer profit, Integer startC, Integer entryC, String duration) {
        this.timeframe = tf;
        this.type = status;
        this.profitLoss = profit;
        this.startCandle = startCandle;
        this.entryCandle = entryCandle;
        this.duration = duration;

        this.tableTimeframeColumn = new SimpleStringProperty(tf.toString());
        this.tablePatternNumberColumn = new SimpleStringProperty(type.toString());
        this.tableProfitLossColumn = new SimpleIntegerProperty(profit);
        this.tableStartCandleColumn = new SimpleIntegerProperty(startC);
        this.tableEntryCandleColumn = new SimpleIntegerProperty(entryC);
        this.tableDurationColumn = new SimpleStringProperty(String.valueOf(duration));
    }

    /** Data columns for table. */
    private SimpleStringProperty tableTimeframeColumn, tablePatternNumberColumn,  tableDurationColumn;
    private SimpleIntegerProperty tableProfitLossColumn, tableStartCandleColumn, tableEntryCandleColumn;

    public String getTableTimeframeColumn() { return tableTimeframeColumn.get(); }
    public SimpleStringProperty tableTimeframeColumnProperty() { return tableTimeframeColumn; }
    public void setTableTimeframeColumn(String tableTimeframeColumn) { this.tableTimeframeColumn.set(tableTimeframeColumn); }

    public String getTablePatternNumberColumn() { return tablePatternNumberColumn.get(); }
    public SimpleStringProperty tablePatternNumberColumnProperty() { return tablePatternNumberColumn; }
    public void setTablePatternNumberColumn(String tablePatternNumberColumn) { this.tablePatternNumberColumn.set(tablePatternNumberColumn); }

    public Integer getTableProfitLossColumn() { return tableProfitLossColumn.get(); }
    public SimpleIntegerProperty tableProfitLossColumnProperty() { return tableProfitLossColumn; }
    public void setTableProfitLossColumn(Integer tableProfitLossColumn) { this.tableProfitLossColumn.set(tableProfitLossColumn); }

    public Integer getTableStartCandleColumn() { return tableStartCandleColumn.get(); }
    public SimpleIntegerProperty tableStartCandleColumnProperty() { return tableStartCandleColumn; }
    public void setTableStartColumn(Integer tableStartCandleColumn) { this.tableStartCandleColumn.set(tableStartCandleColumn); }

    public Integer getTableEntryCandleColumn() { return tableEntryCandleColumn.get(); }
    public SimpleIntegerProperty tableEntryCandleColumnProperty() { return tableEntryCandleColumn; }
    public void setTableEntryCandleColumn(Integer tableEntryCandleColumn) { this.tableEntryCandleColumn.set(tableEntryCandleColumn); }

    public String getTableDurationColumn() { return tableDurationColumn.get(); }
    public SimpleStringProperty tableDurationColumnProperty() { return tableDurationColumn; }
    public void setTableDurationColumn(String tableDurationColumn) { this.tableDurationColumn.set(tableDurationColumn); }

    public String getTimeframe() {return timeframe; }
    public Status getType() {
        return type;
    }
    public int getProfitLoss() {
        return profitLoss;
    }
    public int getStartCandle() { return startCandle; }
    public int getEntryCandle() { return entryCandle; }
    public String getDuration() { return duration; }

    public Date getStart() {
        return start;
    }
    public Double getEnd() {
        return end;
    }

}
