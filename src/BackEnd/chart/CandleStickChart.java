package BackEnd.chart;

import BackEnd.chart.node.Candle;
import javafx.animation.FadeTransition;
import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.XYChart;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import javafx.util.Duration;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * These classes store the instances created of a candlestick chart.
 */

public class CandleStickChart<X, Y> extends XYChart<X, Y> {
    private static final double DEFAULT_CANDLE_WIDTH = 5d;
    private final double candleWidth;

    /** Lists to hold the horizontal and vertical markers. */
    private final ObservableList<Data<X, Y>> horizontalMarkers;
    private final ObservableList<Data<X, Y>> verticalMarkers;

    /**
     * Constructor to initialise a new chart with the given values for x and y axis.
     * @param xAxis Maximum x axis value.
     * @param yAxis Maximum y axis value.
     */
    public CandleStickChart(Axis<X> xAxis, Axis<Y> yAxis) {
        super(xAxis, yAxis);
        getStylesheets().add(getClass().getResource("/FrontEnd/styles/CandleStickChartStyles.css").toExternalForm());
        candleWidth = DEFAULT_CANDLE_WIDTH;
        horizontalMarkers = FXCollections.observableArrayList();
        horizontalMarkers.addListener((InvalidationListener) observable -> layoutPlotChildren());
        verticalMarkers = FXCollections.observableArrayList();
        verticalMarkers.addListener((InvalidationListener) observable -> layoutPlotChildren());
    }

    /**
     * Create a horizontal line to be added to the chart.
     * Used to show critical price levels.
     * @param marker        Marker to be added to the chart.
     * @param currentPrice  Current price (y value) to add the line to.
     */
    public void addHorizontalValueMarker(Data<X, Y> marker, float currentPrice) {
        Objects.requireNonNull(marker, "the marker must not be null");
        if (horizontalMarkers.contains(marker)) return;
        Line line = new Line();
        if (Float.parseFloat(marker.getYValue().toString()) < currentPrice) {
            line.setStroke(Color.BLUE);
        } else {
            line.setStroke(Color.RED);
        }
        marker.setNode(line );
        getPlotChildren().add(line);
        horizontalMarkers.add(marker);
    }

    /**
     * Vertical line to show the start candle of a pattern.
     * @param marker        Marker to be added to the chart.
     */
    public void addStartCandleMarker(Data<X, Y> marker) {
        Objects.requireNonNull(marker, "the marker must not be null");
        if (verticalMarkers.contains(marker)) return;
        Line line = new Line();
        line.setStroke(Color.BLACK);
        marker.setNode(line );

        getPlotChildren().add(line);
        verticalMarkers.add(marker);
    }

    /**
     * Vertical line to show the entry candle of a pattern
     * @param marker        Marker to be added to the chart.
     */
    public void addEntryCandleMarker(Data<X, Y> marker) {
        Objects.requireNonNull(marker, "the marker must not be null");
        if (verticalMarkers.contains(marker)) return;
        Line line = new Line();
        line.setStroke(Color.BLUE);
        marker.setNode(line );

        getPlotChildren().add(line);
        verticalMarkers.add(marker);
    }

    /**
     * Method to create a vertical line of the exit candle of a pattern
     * @param marker        Marker to be added to the chart.
     * @param profitLoss    Profit loss to determine the line colour.
     */
    public void addExitCandleMarker(Data<X, Y> marker, float profitLoss) {
        Objects.requireNonNull(marker, "the marker must not be null");
        if (verticalMarkers.contains(marker)) return;
        Line line = new Line();
        if (profitLoss >= 0) {
            line.setStroke(Color.GREEN);
        } else {
            line.setStroke(Color.RED);
        }
        marker.setNode(line );

        getPlotChildren().add(line);
        verticalMarkers.add(marker);
    }

//    public void removeHorizontalValueMarker(Data<X, Y> marker) {
//        Objects.requireNonNull(marker, "the marker must not be null");
//        if (marker.getNode() != null) {
//            getPlotChildren().remove(marker.getNode());
//            marker.setNode(null);
//        }
//        horizontalMarkers.remove(marker);
//    }

    @Override
    protected void dataItemAdded(Series<X, Y> series, int itemIndex, Data<X, Y> item) {
        Node candle = createCandle(getData().indexOf(series), item, itemIndex);
        if (shouldAnimate()) {
            candle.setOpacity(0);
            getPlotChildren().add(candle);
            /* Fade in a new candle. */
            FadeTransition ft = new FadeTransition(Duration.millis(500), candle);
            ft.setToValue(1);
            ft.play();
        } else {
            getPlotChildren().add(candle);
        }
        /* Draw the average line on top. */
        if (series.getNode() != null) {
            series.getNode().toFront();
        }
    }

    /**
     * Create a new Candle node to represent a single data item.
     *
     * @param seriesIndex The index of the series the data item is in.
     * @param item        The data item to create node for.
     * @param itemIndex   The index of the data item in the series.
     * @return New candle node to represent the give data item.
     */
    private Node createCandle(int seriesIndex, final Data item, int itemIndex) {
        Node candle = item.getNode();
        /* Check if candle has already been created. */
        if (candle instanceof Candle) {
            ((Candle) candle).setSeriesAndDataStyleClasses("series" + seriesIndex,
                    "data" + itemIndex);
        } else {
            candle = new Candle("series" + seriesIndex, "data" + itemIndex);
            item.setNode(candle);
        }
        return candle;
    }

    @Override
    protected void dataItemRemoved(Data<X, Y> item, Series<X, Y> series) {
        final Node candle = item.getNode();
        if (shouldAnimate()) {
            /* Fade out old candle */
            FadeTransition ft = new FadeTransition(Duration.millis(500), candle);
            ft.setToValue(0);
            ft.setOnFinished(actionEvent -> getPlotChildren().remove(candle));
            ft.play();
        } else {
            getPlotChildren().remove(candle);
        }
    }

    @Override
    protected void dataItemChanged(Data<X, Y> item) {
    }

    @Override
    protected void seriesAdded(Series<X, Y> series, int seriesIndex) {
        /* Handle any data already in series. */
        for (int j = 0; j < series.getData().size(); j++) {
            Data item = series.getData().get(j);
            Node candle = createCandle(seriesIndex, item, j);
            if (shouldAnimate()) {
                candle.setOpacity(0);
                getPlotChildren().add(candle);
                /* Fade in new candle. */
                FadeTransition ft = new FadeTransition(Duration.millis(500), candle);
                ft.setToValue(1);
                ft.play();
            } else {
                getPlotChildren().add(candle);
            }
        }
        /* Create series path. */
        Path seriesPath = new Path();
        seriesPath.getStyleClass().setAll("candlestick-average-line", "series" + seriesIndex);
        series.setNode(seriesPath);
        getPlotChildren().add(seriesPath);
    }

    @Override
    protected void seriesRemoved(Series<X, Y> series) {
        /* Remove all candle nodes. */
        for (XYChart.Data<X, Y> d : series.getData()) {
            final Node candle = d.getNode();
            if (shouldAnimate()) {
                /* Fade out old candle. */
                FadeTransition ft = new FadeTransition(Duration.millis(500), candle);
                ft.setToValue(0);
                ft.setOnFinished(actionEvent -> getPlotChildren().remove(candle));
                ft.play();
            } else {
                getPlotChildren().remove(candle);
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void layoutPlotChildren() {
        if (getData() == null) return;
        for (int seriesIndex = 0; seriesIndex < getData().size(); seriesIndex++) {
            Series<X, Y> series = getData().get(seriesIndex);

            Iterator<Data<X, Y>> iterator = getDisplayedDataIterator(series);
            while (iterator.hasNext()) {
                Data<X, Y> item = iterator.next();
                double x = getXAxis().getDisplayPosition(getCurrentDisplayedXValue(item));
                double y = getYAxis().getDisplayPosition(getCurrentDisplayedYValue(item));
                Node itemNode = item.getNode();
                CandleStickExtraValues<Y> extra = (CandleStickExtraValues<Y>) item.getExtraValue();
                if (itemNode instanceof Candle) {
                    Candle candle = (Candle) itemNode;

                    double close = getYAxis().getDisplayPosition(extra.getClose());
                    double high = getYAxis().getDisplayPosition(extra.getHigh());
                    double low = getYAxis().getDisplayPosition(extra.getLow());

                    candle.update(close - y, high - y, low - y, candleWidth);
                    candle.setLayoutX(x);
                    candle.setLayoutY(y);
                }
            }
        }
        for (Data<X, Y> horizontalMarker : horizontalMarkers) {
            Line line = (Line) horizontalMarker.getNode();
            line.setStartX(0);
            line.setEndX(getBoundsInLocal().getWidth());
            line.setStartY(getYAxis().getDisplayPosition(horizontalMarker.getYValue()) + 0.5);
            line.setEndY(line.getStartY());
            line.toFront();
        }

        for (Data<X, Y> verticalMarker : verticalMarkers) {
            Line line = (Line) verticalMarker.getNode();
            line.setStartX(getXAxis().getDisplayPosition(verticalMarker.getXValue()) + 0.5);
            line.setEndX(line.getStartX());
            line.setStartY(0);
            line.setEndY(getBoundsInLocal().getWidth());
            line.toFront();
        }
/*            Path path = new Path();
            path.getElements().addAll(new MoveTo(getBoundsInLocal().getWidth() / 2, line.getLayoutY()), new VLineTo(line.getStartY() + 25));
            PathTransition pathTransition = new PathTransition(Duration.millis(1000), path, line);
            pathTransition.play();*/

    }

    /**
     * This is called when the range has been invalidated and we need to update it. If the axis are auto
     * ranging then we compile a list of all data that the given axis has to plot and call invalidateRange() on the
     * axis passing it that data.
     */
    @Override
    @SuppressWarnings("unchecked")
    protected void updateAxisRange() {
        final Axis<X> xa = getXAxis();
        final Axis<Y> ya = getYAxis();
        List<X> xData = null;
        List<Y> yData = null;
        if (xa.isAutoRanging()) {
            xData = new ArrayList<>();
        }
        if (ya.isAutoRanging()) {
            yData = new ArrayList<>();
        }
        if (xData != null || yData != null) {
            for (Series<X, Y> series : getData()) {
                for (Data<X, Y> data : series.getData()) {
                    if (xData != null) {
                        xData.add(data.getXValue());
                    }
                    if (yData != null) {
                        CandleStickExtraValues<Y> extras = (CandleStickExtraValues<Y>) data.getExtraValue();
                        if (extras != null) {
                            yData.add(extras.getHigh());
                            yData.add(extras.getLow());
                        } else {
                            yData.add(data.getYValue());
                        }
                    }
                }
            }
            if (xData != null) {
                xa.invalidateRange(xData);
            }
            if (yData != null) {
                ya.invalidateRange(yData);
            }
        }
    }

    /**
     * Class to store the values of each candle.
     */
    public static class CandleStickExtraValues<Y> {
        private final Y close;
        private final Y high;
        private final Y low;

        public CandleStickExtraValues(Y close, Y high, Y low) {
            this.close = close;
            this.high = high;
            this.low = low;
        }

        public Y getClose() {
            return close;
        }
        public Y getHigh() {
            return high;
        }
        public Y getLow() {
            return low;
        }
    }
}
