package BackEnd.patternRecognition.availablePatterns;

import BackEnd.markets.Market;
import BackEnd.markets.Status;
import BackEnd.patternRecognition.Patterns;
import FrontEnd.Main;

import java.util.ArrayList;

public class DoubleTop {
    public static ArrayList<Patterns> findDoubleTops(Market market) {
        ArrayList<Patterns> doubleTops = new ArrayList<>();
        for (int tf = 0; tf < Main.getNumberOfTimeframes(); tf++) {
            int counter = 0;
            /* Run check for each candle. */

            for (Market.MarketValues values : market.getTimeframesDataStore(tf).getMarketValues()) {
                counter++;

                /* If resistance or support line is in between the open and close of the candle */
                for (Double d : market.getTimeframesDataStore(tf).getCriticalLevels()) {
                    if (d < values.getOpen() && d > values.getClose()) {

                        /* Go to method start price, candle number, market values and timeframe. */
                        if (checkCandleForPattern(d, counter, market.getTimeframesDataStore(tf).getMarketValues(), tf, market.getPipMultiply(), market.getTrendIndicatorPercentage()) != null) {
                            Patterns newDoubleTop = checkCandleForPattern(d, counter, market.getTimeframesDataStore(tf).getMarketValues(), tf, market.getPipMultiply(), market.getTrendIndicatorPercentage());
                            boolean duplicatePattern = false;
                            for (Patterns p : doubleTops) {
                                if (newDoubleTop.getStartCandle() - 10 < p.getStartCandle() &&
                                        newDoubleTop.getEntryCandle() == p.getEntryCandle() ||
                                        newDoubleTop.getEntryCandle() == p.getEntryCandle()) {
                                    duplicatePattern = true;
                                }
                            }
                            if (!duplicatePattern) {
                                doubleTops.add(checkCandleForPattern(d, counter, market.getTimeframesDataStore(tf).getMarketValues(), tf, market.getPipMultiply(), market.getTrendIndicatorPercentage()));
                            }
                        }
                    }
                }
            }
        }
        return doubleTops;
    }

    private static Patterns checkCandleForPattern(Double d, int startCounter, ArrayList<Market.MarketValues> marketValues, int tf, int pipMultiply, double percentageChange) {
        /**
         * Checks occur: Find the swings
         *  - if swing[4] > swing[3] && swing[4] < swing[1] Then move onto next
         *      - If [3] > [5] - 1% && [3] < +1% then next
         *          - If [2][4][6] are within 1% of each other then entry is candle of [6]
         */
        //Get an array list of all swing's
        ArrayList<Double> swingsList = new ArrayList<>();
        ArrayList<Integer> swingsListCandles = new ArrayList<>();
        double lastSwing = marketValues.get(0).getClose();
        double swingLow = 0;
        double potentialSwingLow = 100;
        double swingHigh = 0;
        double potentialSwingHigh = 0;
        boolean addLow = false;
        boolean addHigh = false;

        int invalidCounter = startCounter;
        int candleCounter = startCounter;
        /* Repeat for each value (candle) to find where the turning (swing) points are */
        for (Market.MarketValues values : marketValues) {
            /* Checking for next swing low */
            if (values.getLow() < lastSwing) {
                if (values.getLow() > potentialSwingLow) {
                    invalidCounter++;
                    if (invalidCounter == 5) {
                        swingLow = potentialSwingLow;
                        lastSwing = swingLow;
                        potentialSwingHigh = 0;
                        invalidCounter = 0;
                        if (addHigh) {
                            swingsList.add(swingHigh);
                            swingsListCandles.add(candleCounter);
                            addHigh = true;
                        }
                    }
                } else if ((((lastSwing / values.getLow()) - 1) * 100) > percentageChange) {
                    potentialSwingLow = values.getLow();
                    invalidCounter = 0;
                    addLow = true;
                }
            }
            /* Checking for next swing high */
            if (values.getHigh() > lastSwing) {
                if (values.getHigh() < potentialSwingHigh) {
                    invalidCounter++;
                    if (invalidCounter == 5) {
                        swingHigh = potentialSwingHigh;
                        lastSwing = swingHigh;
                        potentialSwingLow = 100;
                        invalidCounter = 0;
                        if (addLow) {
                            swingsList.add(swingLow);
                            swingsListCandles.add(candleCounter);
                            addLow = false;
                        }
                    }
                } else if (100 - ((lastSwing / values.getHigh()) * 100) > percentageChange) {
                    potentialSwingHigh = values.getHigh();
                    invalidCounter = 0;
                    addHigh = true;
                }
            }
            candleCounter++;
        }

        int swingListSize = swingsList.size();

        double startPrice = marketValues.get(startCounter-1).getClose();
        double lowerBoundNeckline = startPrice
                - startPrice * 1.02;
        double upperBoundNeckline = startPrice
                + startPrice * 1.02;

        boolean firstTop = false;
        boolean secondTop = false;
        boolean middle = false;

        double topPrice = 0;
        int latestSwingCandlePosition = 0;
        double topPriceLower = 0;
        double topPriceUpper = 0;
        double stopLossSwingPrice = 0;
        boolean first = true;
        int candle = 0;
        boolean bottomBool = true;
        boolean failedPattern = false;
        boolean rise = false;
        //Loop through each candle from "neckline hit"
        for (int i = startCounter; i < marketValues.size() - startCounter; i++) {
            if (!failedPattern) {
                if (first) { // Check if the candle is swing
                    for (int j = 0; j < swingListSize; j++) {
                        // If the current candle is at a swing
                        if (first) {
                            if (swingsListCandles.get(j) == i) {
                                // Start pattern search
                                firstTop = true;
                                latestSwingCandlePosition = i;
                                topPrice = marketValues.get(i).getClose();
                                topPriceLower = marketValues.get(i).getLow();
                                topPriceUpper = marketValues.get(i).getHigh();
                                first = false;
                                i = i + 5;
                            }
                        }
                    }
                }
                if (firstTop) { // Search for the first top
                    if (marketValues.get(i).getLow() < startPrice &&
                            marketValues.get(i).getHigh() > startPrice) {
                        firstTop = false;
                        middle = true;
                    }else if (marketValues.get(i).getClose() < startPrice) {
                        return null;
                    }
                }
                if (middle) { // Search for the middle (neckline) hit
                    if (marketValues.get(i).getClose() > lowerBoundNeckline &&
                            marketValues.get(i).getClose() < upperBoundNeckline) {
                        middle = false;
                        secondTop = true;
                        stopLossSwingPrice = topPrice;
                    } else if (marketValues.get(i).getClose() > topPriceUpper) {
                        return null;
                    }
                }
                if (secondTop) { // Search for entry position
                    if (marketValues.get(i).getClose() < startPrice) {
                        Patterns newDoubleBottom = searchStopLossTakeProfit(i, marketValues, stopLossSwingPrice, startPrice, pipMultiply, tf, latestSwingCandlePosition);
                        return newDoubleBottom;
                    } else if (marketValues.get(i).getClose() > topPrice) {
                        return null;
                    }
                    return null;
                }
            }
            /**
             * Find the start swing [1]
             * Find the middle/neckline touch
             * Find the neckline touch in the dip
             * for loop
             *  - Find if low -> high before neckline also occurs after head line -> if so then two tops have been formed
             *  - Find if the price falls back to the neckline (start price)
             *  - Calculate the TP and SL and compute if either of teh values have been hit.
             *
             */
        }
        return null;
    }

    public static Patterns searchStopLossTakeProfit(int entryCandle, ArrayList<Market.MarketValues> marketValues, double stopLossSwingPrice, double startPrice, int pipMultiply, int tf, int startCandle) {
        double stopLossTarget = stopLossSwingPrice;
        double stopLossPips = (stopLossTarget - startPrice) * pipMultiply;
        double takeProfitDifference = (stopLossTarget - startPrice) * 1.2;
        double takeProfitTarget = startPrice - takeProfitDifference;
        double takeProfitPips = (startPrice - takeProfitTarget) * pipMultiply;
        boolean pendingPattern = false;

        for (int i = entryCandle; i < marketValues.size() - entryCandle; i++) {
            if (marketValues.get(i).getLow() < takeProfitTarget) { // TP Hit
                Patterns toAdd = new Patterns(tf, Status.DOUBLE_TOP, (int) takeProfitPips, startCandle, entryCandle, String.valueOf(i - startCandle));
                toAdd.setExitCandlePrice((float) takeProfitTarget);
                toAdd.setExitCandle(i);
                return toAdd;
            } else if (marketValues.get(i).getHigh() > stopLossTarget) { // SL Hit
                Patterns toAdd = new Patterns(tf, Status.DOUBLE_TOP, -(int) stopLossPips, startCandle, entryCandle, String.valueOf(i - startCandle));
                toAdd.setExitCandlePrice((float) stopLossTarget);
                toAdd.setExitCandle(i);
                return toAdd;
            } else {
                pendingPattern = true;
            }
        }
        if (pendingPattern) {
            Patterns toAdd = new Patterns(tf, Status.DOUBLE_TOP, 0, startCandle, entryCandle, "Pending");
            return toAdd;
        }
        return null;
    }
}
