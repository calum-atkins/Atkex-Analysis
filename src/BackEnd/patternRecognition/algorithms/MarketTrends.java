package BackEnd.patternRecognition.algorithms;

import BackEnd.markets.Market;
import BackEnd.markets.MarketTrend;


public abstract class MarketTrends {

    /**
     * Calculate the current trend of the market form the day data
     * @param timeframesDataStore day timeframe data
     * @return computed market trend
     */
    public static MarketTrend dayTrend(Market.StoreData timeframesDataStore, double percentageChange) {
        //Repeat for each value, from start to current date
//        System.out.println(percentageChange);
        for (Market.MarketValues values : timeframesDataStore.getMarketValues()) {
//            System.out.println(values.getClose());
            /**
             * Chose start point - Swing Low[0]
             * Use percentage movement - percentageChange
             * identify next swing high[0] that has >% difference from swing Low[0]
             * Identify next swing low[1] that has >% difference from swing high[0]
             * Repeat until at most recent swing low/high
             */


        }
        return MarketTrend.UP;
    }
}
