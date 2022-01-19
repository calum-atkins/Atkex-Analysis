package BackEnd.patternRecognition;

import BackEnd.markets.Market;
import BackEnd.markets.MarketTimeframe;
import BackEnd.markets.Status;
import BackEnd.patternRecognition.availablePatterns.*;
import javafx.beans.property.SimpleStringProperty;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class Patterns {
    private Status type;
    private int profit;
    private Date start;
    private Double end;
    private String timeframe;

    private static ArrayList<AscendingTriangle> ascendingTriangleList;
    private ArrayList<DescendingTriangle> descendingTriangleList;
    private ArrayList<DoubleBottom> doubleBottomList;
    private ArrayList<DoubleTop> doubleTopList;
    private ArrayList<Pennant> pennantList;
    private ArrayList<Wedge> wedgeList;

    private ArrayList<Patterns> patternsList;

    public Patterns(int tf, Status status, int profit, Date start, Double end) {
        switch (tf) {
            case 2: this.timeframe = "1h"; break;
            case 1: this.timeframe = "4h"; break;
            case 0: this.timeframe = "1d"; break;
        }
        this.type = status;
        this.profit = profit;
        this.start = start;
        this.end = end;

        this.timeframeColumn = new SimpleStringProperty(getTimeframe());
        this.patternNumberColumn = new SimpleStringProperty(type.toString());
        this.profitColumn = new SimpleStringProperty(String.valueOf(profit));
        this.startColumn = new SimpleStringProperty(String.valueOf(start));
        this.endColumn = new SimpleStringProperty(String.valueOf(end));
    }
    public Patterns(String tf, Status status, int profit, Date start, Double end) {
        this.timeframe = tf;
        this.type = status;
        this.profit = profit;
        this.start = start;
        this.end = end;

        this.timeframeColumn = new SimpleStringProperty(tf.toString());
        this.patternNumberColumn = new SimpleStringProperty(type.toString());
        this.profitColumn = new SimpleStringProperty(String.valueOf(profit));
        this.startColumn = new SimpleStringProperty(String.valueOf(start));
        this.endColumn = new SimpleStringProperty(String.valueOf(end));
    }

    /** Data columns for table. */
    private SimpleStringProperty timeframeColumn, patternNumberColumn, profitColumn, startColumn, endColumn;

    public String getTimeframeColumn() { return timeframeColumn.get(); }
    public SimpleStringProperty timeframeColumnProperty() { return timeframeColumn; }
    public void setTimeframeColumn(String timeframeColumn) { this.timeframeColumn.set(timeframeColumn); }

    public String getPatternNumberColumn() { return patternNumberColumn.get(); }
    public SimpleStringProperty patternNumberColumnProperty() { return patternNumberColumn; }
    public void setPatternNumberColumn(String patternNumberColumn) { this.patternNumberColumn.set(patternNumberColumn); }

    public String getProfitColumn() { return profitColumn.get(); }
    public SimpleStringProperty profitColumnProperty() { return profitColumn; }
    public void setProfitColumn(String profitColumn) { this.profitColumn.set(profitColumn); }

    public String getStartColumn() { return startColumn.get(); }
    public SimpleStringProperty startColumnProperty() { return startColumn; }
    public void setStartColumn(String startColumn) { this.startColumn.set(startColumn); }

    public String getEndColumn() { return endColumn.get(); }
    public SimpleStringProperty endColumnProperty() { return endColumn; }
    public void setEndColumn(String endColumn) { this.endColumn.set(endColumn); }

//    public ArrayList<Patterns> findAllPatterns(ArrayList<Market> markets) {
//        /**
//         * Algorithm Here
//         * Do something to find triangle if found then
//         */
//        patternsList.add(new Patterns(Status.ASCENDING_TRIANGLE, 100, 1, 1));
//
//        return null;
//    }


    public String getTimeframe() {return timeframe; }
    public Status getType() {
        return type;
    }
    public int getProfit() {
        return profit;
    }
    public Date getStart() {
        return start;
    }
    public Double getEnd() {
        return end;
    }



    public void addAscendingTriangle(AscendingTriangle ascendingTriangle) { ascendingTriangleList.add(ascendingTriangle); }
    public ArrayList<AscendingTriangle> getAscendingTriangleList() {
        return ascendingTriangleList;
    }
    public void setAscendingTriangleList(ArrayList<AscendingTriangle> ascendingTriangleList) {
        this.ascendingTriangleList = ascendingTriangleList;
    }

    public ArrayList<DescendingTriangle> getDescendingTriangleList() {
        return descendingTriangleList;
    }
    public void setDescendingTriangleList(ArrayList<DescendingTriangle> descendingTriangleList) {
        this.descendingTriangleList = descendingTriangleList;
    }

    public ArrayList<DoubleBottom> getDoubleBottomList() {
        return doubleBottomList;
    }
    public void setDoubleBottomList(ArrayList<DoubleBottom> doubleBottomList) { this.doubleBottomList = doubleBottomList; }

    public ArrayList<DoubleTop> getDoubleTopList() {
        return doubleTopList;
    }
    public void setDoubleTopList(ArrayList<DoubleTop> doubleTopList) {
        this.doubleTopList = doubleTopList;
    }

    public ArrayList<Pennant> getPennantList() {
        return pennantList;
    }
    public void setPennantList(ArrayList<Pennant> pennantList) {
        this.pennantList = pennantList;
    }

    public ArrayList<Wedge> getWedgeList() {
        return wedgeList;
    }
    public void setWedgeList(ArrayList<Wedge> wedgeList) {
        this.wedgeList = wedgeList;
    }
}
