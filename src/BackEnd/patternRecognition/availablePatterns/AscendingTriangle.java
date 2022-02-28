package BackEnd.patternRecognition.availablePatterns;

import BackEnd.markets.Market;
import BackEnd.markets.MarketTimeframe;
import BackEnd.markets.Status;
import BackEnd.patternRecognition.Patterns;
import BackEnd.patternRecognition.algorithms.CriticalLevels;
import FrontEnd.Main;

import java.lang.reflect.AccessibleObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Class is used to find any ascending triangles on the specified market.
 *
 * The unsupervised model is used to cluster data apart to identify if there is a possible pattern starting from each candle.
 *
 * If a possible pattern is available (cross under a critical level) then checks will occur to identify if the
 * pattern is forming, failed or successful.
 */
public class AscendingTriangle {

    public static ArrayList<Patterns> findAscendingTriangles(Market market) {
        ArrayList<Patterns> ascendingTriangles = new ArrayList<Patterns>();
        for (int tf = 0; tf < Main.getNumberOfTimeframes(); tf++) {
            int counter = 0;
            /** Run check for each candle.  */
            for (Market.MarketValues values : market.getTimeframesDataStore(tf).getMarketValues()) {
                counter++;
                /** If resistance/support line is in between the open and the close then start price = close. */
                for (Double d : market.getTimeframesDataStore(tf).getResistanceLevels()) {
                    if (d < values.getOpen() && d > values.getClose()) {
                        /** Go to method send start price, candle number, market values and timeframe. */
                        if (checkCandleForPattern(d, counter, market.getTimeframesDataStore(tf).getMarketValues(), tf, market.getPipMultiply()) != null) {
                            ascendingTriangles.add(checkCandleForPattern(d, counter, market.getTimeframesDataStore(tf).getMarketValues(), tf, market.getPipMultiply()));
                        }
                    }
                }
            }
        }
        return ascendingTriangles;
    }

    public static Patterns checkCandleForPattern(double startPrice, int candleNumber, ArrayList<Market.MarketValues> valuesList, int tf, int pipMultiply) {
        /**
         * Checks that occur:
         *  - If the candle has closed above price (check for failed or successful pattern).
         *  - If another low is formed (Next value to check in range of last low).
         *  - If high above line (Next low could be forming, in target for triangle).
         */
        double possibleSwingLow = 0;

        if (valuesList.size() != candleNumber) {
            Date startDate = valuesList.get(candleNumber).getDate();

            ArrayList<Double> swingLowList = new ArrayList<>();
            boolean first = false;
            boolean rise = false;
            //If the triangle has closed out with 2 or more swing lows
            int remainingCandles = valuesList.size() - candleNumber;
            int entryCandle = candleNumber;

            /** Repeat for number of candles left to current date/time. */
            for (int i = candleNumber; i < remainingCandles; i++) {

                /** If the close is above the start price, check for possible candle */
                if (valuesList.get(i).getClose() > startPrice && swingLowList.size() >= 3) {

                    double percentageHighest = startPrice - swingLowList.get(0);
                    double comparisonValue = 0;
                    double lowerBound = 0, upperBound = 0;
                    for (int a = 0; a < swingLowList.size(); a++) {
                        if (a != 0) {

                            /** Calculate the upper and lower bound the next swing low has to be for possible triangle. */
                            double percentageToCompute = startPrice - swingLowList.get(a);
                            double value = ((percentageToCompute / percentageHighest) * 100);
                            if (a == 1) {
                                comparisonValue = 1 - ((percentageToCompute / percentageHighest) * 100);
                                lowerBound = value - comparisonValue - 0.05;
                                upperBound = value - comparisonValue + 0.05;
                            }

                            if (value > lowerBound && value < upperBound) {
                                double stopLossTarget = swingLowList.get(2);
                                double stopLossPips = (startPrice - stopLossTarget) * pipMultiply;
                                double takeProfitDifference = (startPrice - stopLossTarget) * 2;
                                double takeProfitTarget = takeProfitDifference + startPrice;
                                double takeProfitPips = (takeProfitTarget - startPrice) * pipMultiply;

                                /** Check for if the stop loss or take profit has been reached. */
                                int candleCounter = entryCandle;
                                while (true) {
                                    /**
                                     * Calculate if the stop loss, take profit has been reached.
                                     * If not then the candle is returned as pending (still being formed).
                                     */
                                    int duration = candleCounter - candleNumber;
                                    if (valuesList.get(candleCounter) == null) {
                                        return new Patterns(tf, Status.ASCENDING_TRIANGLE, 0, candleNumber, entryCandle, "Pending");
                                    } else if ((takeProfitTarget - valuesList.get(candleCounter).getHigh()) < 0) {
                                        return new Patterns(tf, Status.ASCENDING_TRIANGLE, (int) takeProfitPips, candleNumber, entryCandle, String.valueOf(duration));
                                    } else if ((valuesList.get(candleCounter).getLow() - stopLossTarget) < 0) {
                                        return new Patterns(tf, Status.ASCENDING_TRIANGLE, -(int) stopLossPips, candleNumber, entryCandle, String.valueOf(duration));
                                    }
                                    candleCounter++;
                                }
                            }
                        }
                    }
                    swingLowList.clear();
                    return null;
                } else if (valuesList.get(i).getClose() > startPrice && swingLowList.size() <= 1) { /** If the pattern has closed out with 1 or less swing lows */
                    swingLowList.clear();
                    return null;
                }

                /** If another low is formed, replace temp swing low with new low. */
                if (valuesList.get(i).getClose() <= startPrice && valuesList.get(i).getClose() <= possibleSwingLow && rise == false) {
                    possibleSwingLow = valuesList.get(i).getClose();
                    rise = false;
                } else if (valuesList.get(i).getClose() <= startPrice && valuesList.get(i).getClose() <= possibleSwingLow && rise == true) {
                    swingLowList.clear();
                    rise = true;
                }

                /** If values reach horizontal line, add recent swing low to array and search for next swing low. */
                if (valuesList.get(i).getHigh() > startPrice && valuesList.get(i).getClose() < startPrice) {
                    if (first) {
                        possibleSwingLow = startPrice;
                    } else {
                        swingLowList.add(possibleSwingLow);
                        possibleSwingLow = startPrice;
                    }
                }
                entryCandle++;
            }
        }
        return null;
    }
}
