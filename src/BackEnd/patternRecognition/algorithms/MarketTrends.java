package BackEnd.patternRecognition.algorithms;

import BackEnd.markets.Market;
import BackEnd.markets.MarketTrend;

import java.util.ArrayList;


public abstract class MarketTrends {

    private static boolean first = true;
    /**
     * Calculate the current trend of the market form the day data
     * @param timeframesDataStore day timeframe data
     * @return computed market trend
     */
    public static MarketTrend dayTrend(Market.StoreData timeframesDataStore, double percentageChange) {
        //Repeat for each value, from start to current date
//        System.out.println(percentageChange);
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

        for (Market.MarketValues values : timeframesDataStore.getMarketValues()) {
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
                            System.out.println("Here " + swingHigh);
                        }
                    }
                } else if ((((lastSwing / values.getLow()) - 1) * 100) > percentageChange) {
                    //System.out.println("here" + values.getLow() + " " + counter);
                    potentialSwingLow = values.getLow();
                    counter = 0;
                    addLow = true;
                }
            }
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
                            System.out.println("Here 2 " + swingLow);
                        }
                    }
                } else if (100 - ((lastSwing / values.getHigh()) * 100) > percentageChange) {
                    //System.out.println("here 2 " + values.getHigh() + " "  + counter);
                    potentialSwingHigh = values.getHigh();
                    counter = 0;
                    addHigh = true;
                }
            }
        }

        int size = swingsList.size() - 1;
        System.out.println(size);
        if (size > 3) {
            if (swingsList.get(size) < swingsList.get(size - 1)) {
                if (swingsList.get(size - 2) < swingsList.get(size - 3)
                        && swingsList.get(size - 1) > swingsList.get(size - 2)) {
                    return MarketTrend.DOWN;
                }
            }
            if (swingsList.get(size) > swingsList.get(size - 1)) {
                if (swingsList.get(size - 2) > swingsList.get(size - 3)
                        && swingsList.get(size - 1) < swingsList.get(size - 2)) {
                    return MarketTrend.UP;
                }
            }
        }
        return MarketTrend.NO_TREND;

        /**
         * Get first close price = lastSwing
         *
         * lastSwing = close
         *
         * for i
         * { if (low < lastSwing)
         *      if (low >  swingLow)
         *          swingLow = potentialSwingLow
         *          lastSwing = swingLow
         *      if (lastSwing/low)-1)*100) > percentageChange }
         *          potentialSwingLow = low
         *
         * { if (high > lastSwing)
         *      if (high < swingHigh)
         *          swingHigh = potentialSwingHigh
         *          lastSwing = swingHigh
         *      if (100-((swingLow/high)*100) > percentageChange }
         *          potentialSwingHigh = high
         *
         */


        /**
         * Chose start point - Swing Low[0]
         * Use percentage movement - percentageChange
         * identify next swing high[0] that has >% difference from swing Low[0]
         * Identify next swing low[1] that has >% difference from swing high[0]
         * Repeat until at most recent swing low/high
         */


    }
}
