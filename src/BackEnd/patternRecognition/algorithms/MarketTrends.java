package BackEnd.patternRecognition.algorithms;

import BackEnd.markets.Market;
import BackEnd.markets.MarketTrend;

import java.util.ArrayList;

public abstract class MarketTrends {

    /**
     * Calculate the current trend of the market form the day data
     * @param timeframesDataStore day timeframe data
     * @return computed market trend
     */
    public static MarketTrend dayTrend(Market.StoreData timeframesDataStore, double percentageChange) {
        int counter = 0;
        double lastSwing = timeframesDataStore.getMarketValues().get(0).getClose();
        double swingLow = 0;
        double potentialSwingLow = 100;
        double swingHigh = 0;
        double potentialSwingHigh = 0;
        boolean addLow = false;
        boolean addHigh = false;
        ArrayList<Double> swingsList = new ArrayList<Double>();
        MarketTrend currentTrend = MarketTrend.NO_TREND;

        /* Repeat for each value (candle) to find where the turning (swing) points are */
        for (Market.MarketValues values : timeframesDataStore.getMarketValues()) {
            /* Checking for next swing low */
            if (values.getLow() < lastSwing) {
                if (values.getLow() > potentialSwingLow) {
                    counter++;
                    if (counter == 5) {
                        swingLow = potentialSwingLow;
                        lastSwing = swingLow;
                        potentialSwingHigh = 0;
                        counter = 0;
                        if (addHigh) {
                            swingsList.add(swingHigh);
                            addHigh = true;
                        }
                    }
                } else if ((((lastSwing / values.getLow()) - 1) * 100) > percentageChange) {
                    potentialSwingLow = values.getLow();
                    counter = 0;
                    addLow = true;
                }
            }
            /* Checking for next swing high */
            if (values.getHigh() > lastSwing) {
                if (values.getHigh() < potentialSwingHigh) {
                    counter++;
                    if (counter == 5) {
                        swingHigh = potentialSwingHigh;
                        lastSwing = swingHigh;
                        potentialSwingLow = 100;
                        counter = 0;
                        if (addLow) {
                            swingsList.add(swingLow);
                            addLow = false;
                        }
                    }
                } else if (100 - ((lastSwing / values.getHigh()) * 100) > percentageChange) {
                    potentialSwingHigh = values.getHigh();
                    counter = 0;
                    addHigh = true;
                }
            }
        }

        int size = swingsList.size() - 1;
        if (size > 3) {
            /* If the past 3 swings cause a down trend */
            if (swingsList.get(size) < swingsList.get(size - 1)) {
                if (swingsList.get(size - 2) < swingsList.get(size - 3)
                        && swingsList.get(size - 1) > swingsList.get(size - 2)) {
                    return MarketTrend.DOWN;
                }
            }
            /* If the past 3 swings cause an up trend */
            if (swingsList.get(size) > swingsList.get(size - 1)) {
                if (swingsList.get(size - 2) > swingsList.get(size - 3)
                        && swingsList.get(size - 1) < swingsList.get(size - 2)) {
                    return MarketTrend.UP;
                }
            }
        }
        /* If there is no current trend */
        return MarketTrend.NO_TREND;
    }
}
