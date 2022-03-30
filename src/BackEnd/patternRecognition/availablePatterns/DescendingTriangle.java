package BackEnd.patternRecognition.availablePatterns;

import BackEnd.markets.Market;
import BackEnd.markets.Status;
import BackEnd.patternRecognition.Patterns;
import FrontEnd.Main;

import java.util.ArrayList;
import java.util.Date;

public class DescendingTriangle {
    public static ArrayList<Patterns> findDescendingTriangles(Market market) {
        ArrayList<Patterns> descendingTriangles = new ArrayList<Patterns>();
        for (int tf = 0; tf < Main.getNumberOfTimeframes(); tf++) {
            int counter = 0;
            /* Run a check for each candle. */
            for (Market.MarketValues values : market.getTimeframesDataStore(tf).getMarketValues()) {
                counter++;
                /* If support line is in between the open and the close then start price = close. */
                for (Double d : market.getTimeframesDataStore(tf).getCriticalLevels()) {
                    if (d > values.getOpen() && d < values.getClose()) {
                        /* Go to method send start price, candle number, market values and timeframe. */
                        if (checkCandleForPattern(d, counter, market.getTimeframesDataStore(tf).getMarketValues(), tf, market.getPipMultiply()) != null) {
                            descendingTriangles.add(checkCandleForPattern(d, counter, market.getTimeframesDataStore(tf).getMarketValues(), tf, market.getPipMultiply()));
                        }
                    }
                }
            }
        }
        return descendingTriangles;
    }

    public static Patterns checkCandleForPattern(double startPrice, int candleNumber, ArrayList<Market.MarketValues> valuesList, int tf, int pipMultiply) {
        /**
         * Checks that occur:
         *  - If the candle has closed below price (check for failed or successful pattern).
         *  - If another high is formed (Next value to check in range of last high).
         *  - If low below line (Next high could be forming, in target for triangle).
         */
        double possibleSwingHigh = 0;

        if (valuesList.size() != candleNumber) {
            Date startDate = valuesList.get(candleNumber).getDate();

            ArrayList<Double> swingHighList = new ArrayList<>();
            boolean first = false;
            boolean rise = true;
            //If the triangle has closed out with 2 or more swing lows
            int remainingCandles = valuesList.size() - candleNumber;
            int entryCandle = candleNumber;

            /* Repeat for number of candles left to current date/time. */
            for (int i = candleNumber; i < remainingCandles; i++) {

                /* If the close is below the start price, check for possible candle */
                if (valuesList.get(i).getClose() < startPrice && swingHighList.size() >= 3) {

                    double percentageHighest = startPrice + swingHighList.get(0);
                    double comparisonValue = 0;
                    double lowerBound = 0, upperBound = 0;

                    for (int a = 0; a < swingHighList.size(); a++) {
                        if (a != 0) {

                            /* Calculate the upper and lower bound the next swing high has to be for possible triangle. */
                            double percentageToCompute = swingHighList.get(a) - startPrice;
                            double value = ((percentageToCompute / percentageHighest) * 100);
                            if (a == 1) {
                                comparisonValue = 1 - ((percentageToCompute / percentageHighest) * 100);
                                lowerBound = value - comparisonValue - 0.05;
                                upperBound = value - comparisonValue + 0.05;
                            }

                            if (value > lowerBound && value < upperBound) {
                                double stopLossTarget = swingHighList.get(2);
                                double stopLossPips = (stopLossTarget - startPrice) * pipMultiply;
                                double takeProfitDifference = (stopLossTarget - startPrice) * 2;
                                double takeProfitTarget = startPrice - takeProfitDifference;
                                double takeProfitPips = (startPrice - takeProfitTarget) * pipMultiply;

                                /* Check for if the stop loss or take profit has been reached. */
                                int candleCounter = entryCandle;
                                while (true) {
                                    /**
                                     * Calculate if the stop loss, take profit has been reached.
                                     * If not then the candle is returned as pending (still being formed).
                                     */

                                    int duration = candleCounter - candleNumber;
                                    if (valuesList.get(candleCounter) == null) {
                                        Patterns toAdd = new Patterns(tf, Status.DESCENDING_TRIANGLE, 0, candleNumber, entryCandle, "Pending");
                                        return toAdd;
                                    } else if ((takeProfitTarget - valuesList.get(candleCounter).getHigh()) < 0) {
                                        Patterns toAdd = new Patterns(tf, Status.DESCENDING_TRIANGLE, (int) takeProfitPips, candleNumber, entryCandle, String.valueOf(duration));
                                        toAdd.setExitCandlePrice((float) takeProfitTarget);
                                        toAdd.setExitCandle(candleCounter);
                                        return toAdd;
                                    } else if ((valuesList.get(candleCounter).getLow() - stopLossTarget) < 0) {
                                        Patterns toAdd = new Patterns(tf, Status.DESCENDING_TRIANGLE, -(int) stopLossPips, candleNumber, entryCandle, String.valueOf(duration));
                                        toAdd.setExitCandlePrice((float) stopLossTarget);
                                        toAdd.setExitCandle(candleCounter);
                                        return toAdd;
                                    }
                                    candleCounter++;
                                }
                            }
                        }
                    }
                    swingHighList.clear();
                    return null;
                } /* If the pattern has closed out with 1 or less swing highs */
                else if (valuesList.get(i).getClose() < startPrice && swingHighList.size() <= 1) {
                    swingHighList.clear();
                    return null;
                }

                /* If another high is formed, replace temp swing high with new high. */
                if (valuesList.get(i).getClose() >= startPrice && valuesList.get(i).getClose() >= possibleSwingHigh && rise == true) {
                    possibleSwingHigh = valuesList.get(i).getClose();
                    rise = true;
                } else if (valuesList.get(i).getClose() >= startPrice && valuesList.get(i).getClose() >= possibleSwingHigh && rise == false) {
                    swingHighList.clear();
                    rise = false;
                }

                /* If values reach horizontal line, add recent swing high to array and search for next swing high. */
                if (valuesList.get(i).getLow() < startPrice && valuesList.get(i).getClose() > startPrice) {
                    if (first) {
                        possibleSwingHigh = startPrice;
                    } else {
                        swingHighList.add(possibleSwingHigh);
                        possibleSwingHigh = startPrice;
                    }
                }
                entryCandle++;
            }
        }
        return null;
    }
}
