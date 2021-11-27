package BackEnd.Markets;

import javafx.scene.chart.Axis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;


//These classes store the data to be displayed to the UI and the values to be used within the algorithms for pattern recognition
public class CandleStickChart<X, Y> extends XYChart<X, Y> {
    private NumberAxis xAxis;
    private NumberAxis yAxis;
    private String label;

    public CandleStickChart(Axis<X> xAxis, Axis<Y> yAxis) {
        super(xAxis, yAxis);

    }

    @Override
    protected void dataItemAdded(Series<X, Y> series, int itemIndex, Data<X, Y> item) {

    }

    @Override
    protected void dataItemRemoved(Data<X, Y> item, Series<X, Y> series) {

    }

    @Override
    protected void dataItemChanged(Data<X, Y> item) {

    }

    @Override
    protected void seriesAdded(Series<X, Y> series, int seriesIndex) {

    }

    @Override
    protected void seriesRemoved(Series<X, Y> series) {

    }

    @Override
    protected void layoutPlotChildren() {

    }


    public static class CandleStickExtraValues<Y> {
        private Y open;
        private Y close;
        private Y high;
        private Y low;

        public CandleStickExtraValues(Y open, Y close, Y high, Y low) {
            this.open = open;
            this.close = close;
            this.high = high;
            this.low = low;
        }

        public Y getOpen() { return open; }

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
