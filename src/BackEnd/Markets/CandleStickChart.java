package BackEnd.Markets;

import javafx.scene.chart.NumberAxis;


//These classes store the data to be displayed to the UI and the values to be used within the algorithms for pattern recognition
public class CandleStickChart {
    private NumberAxis xAxis;
    private NumberAxis yAxis;
    private String label;








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
