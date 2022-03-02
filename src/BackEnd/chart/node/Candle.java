package BackEnd.chart.node;

import javafx.scene.Group;
import javafx.scene.layout.Region;
import javafx.scene.shape.Line;

/**
 * This class is used to create each candle within
 * a chart to be displayed to the UI.
 */
public class Candle extends Group {
    private Line highLowLine = new Line();
    private Region bar = new Region();
    private String seriesStyleClass;
    private String dataStyleClass;
    private boolean openAboveClose = true;

    /**
     * Constructor for the candle.
     * @param seriesStyleClass style of the candle to add.
     * @param dataStyleClass data of the candle to add.
     */
    public Candle(String seriesStyleClass, String dataStyleClass) {
        setAutoSizeChildren(false);
        getChildren().addAll(highLowLine, bar);
        this.seriesStyleClass = seriesStyleClass;
        this.dataStyleClass = dataStyleClass;
        updateStyleClasses();
    }

    /**
     * Method to set the styles and update the colour of the candle.
     * @param seriesStyleClass
     * @param dataStyleClass
     */
    public void setSeriesAndDataStyleClasses(String seriesStyleClass, String dataStyleClass) {
        this.seriesStyleClass = seriesStyleClass;
        this.dataStyleClass = dataStyleClass;
        updateStyleClasses();
    }

    /**
     * Method to update the candle data.
     * @param closeOffset
     * @param highOffset
     * @param lowOffset
     * @param candleWidth
     */
    public void update(double closeOffset, double highOffset, double lowOffset, double candleWidth) {
        openAboveClose = closeOffset > 0;
        updateStyleClasses();
        highLowLine.setStartY(highOffset);
        highLowLine.setEndY(lowOffset);
        if (candleWidth == -1) {
            candleWidth = bar.prefWidth(-1);
        }
        if (openAboveClose) {
            bar.resizeRelocate(-candleWidth / 2, 0, candleWidth, closeOffset);
        } else {
            bar.resizeRelocate(-candleWidth / 2, closeOffset, candleWidth, closeOffset * -1);
        }
    }

    /**
     * Method to update the style of the candle depending on if it is a bullish or bearish candle
     */
    private void updateStyleClasses() {
        getStyleClass().setAll("candlestick-candle", seriesStyleClass, dataStyleClass);
        highLowLine.getStyleClass().setAll("candlestick-line", seriesStyleClass, dataStyleClass,
                openAboveClose ? "open-above-close" : "close-above-open");
        bar.getStyleClass().setAll("candlestick-bar", seriesStyleClass, dataStyleClass,
                openAboveClose ? "open-above-close" : "close-above-open");
    }
}
