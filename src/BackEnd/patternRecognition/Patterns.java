package BackEnd.patternRecognition;

import BackEnd.patternRecognition.availablePatterns.*;

import java.util.ArrayList;

public abstract class Patterns {
    private ArrayList<AscendingTriangle> ascendingTriangleList;
    private ArrayList<DescendingTriangle> descendingTriangleList;
    private ArrayList<DoubleBottom> doubleBottomList;
    private ArrayList<DoubleTop> doubleTopList;
    private ArrayList<Pennant> pennantList;
    private ArrayList<Wedge> wedgeList;


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
