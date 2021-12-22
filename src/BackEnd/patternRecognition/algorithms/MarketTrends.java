package BackEnd.patternRecognition.algorithms;

import BackEnd.markets.Market;
import BackEnd.markets.MarketTrend;


public abstract class MarketTrends {

    /**
     * Calculate the current trend of the market form the day data
     * @param timeframesDataStore day timeframe data
     * @return computed market trend
     */
    public static MarketTrend dayTrend(Market.StoreData timeframesDataStore) {
        for (Market.MarketValues values : timeframesDataStore.getMarketValues()) {
//            System.out.println(values.getClose());
        }
        return MarketTrend.UP;
    }
}
