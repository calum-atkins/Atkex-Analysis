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

public class AscendingTriangle {

    public static ArrayList<Patterns> findAscendingTriangles(Market market) {
        ArrayList<Patterns> ascendingTriangles = new ArrayList<Patterns>();
            for (int i = 0; i < Main.getNumberOfTimeframes(); i++) {
                int counter = 0;
                for (Market.MarketValues values : market.getTimeframesDataStore(i).getMarketValues()) {
                    counter++;
                    /**
                     * If  resistance/support line is in between the open and the close then start price = close
                     */
                    for (Double d : market.getTimeframesDataStore(i).getResistanceLevels()) {
                        if (d < values.getOpen() && d > values.getClose()) {
                            //Go to method send start price, candle number and markets.
                            if (checkCandleForPattern(d, counter, market.getTimeframesDataStore(i).getMarketValues(), i) != null) {
                                ascendingTriangles.add(checkCandleForPattern(d, counter, market.getTimeframesDataStore(i).getMarketValues(), i));
                            }
                        }
                    }

                }

                    /**
                     * for (i to timesStore.length)
                     *  start point = close[i]
                     *  arrayList SWL
                     *  arraylist SWH
                     *  onRise = false
                     *  If close[i] <= start price && close[i] <= possibleSwingLow then
                     *   possibleSwingLow = close[i]
                     *
                     *
                     *  if high[i] > startPrice && close[i] < startPrice then
                     *   SWL.add(possilbeSwingLow)
                     *   possibleSwingLow = startPrice
                     *
                     *  if close[i] > startPrice && SWL.size >= 3
                     *      Output (price)
                     *
                     *  for i to number swings
                     *    if s[i] != first
                     *      slP[i] = (sP / s[i]) * 100
                     *      slP[i-1] = (sP / s[i]) * 100
                     *
                     */
        }
        return ascendingTriangles;
    }

    public static Patterns checkCandleForPattern(double startPrice, int candleNumber, ArrayList<Market.MarketValues> valuesList, int tf) {
        /**
         * Do check here
         * If the candle has closed above price
         * If another low is formed
         * If high above line
         */
        double possibleSwingLow = 0;

        if (valuesList.size() == candleNumber) {

        } else {
            Date startDate = valuesList.get(candleNumber).getDate();

            ArrayList<Double> swingLowList = new ArrayList<>();
            boolean first = false;
            boolean rise = false;
            //If the triangle has closed out with 2 or more sinwg lows
            int length = valuesList.size() - candleNumber;
            boolean validTriangle = false;
            for (int i = candleNumber; i < length; i++) {
                if (valuesList.get(i).getClose() > startPrice && swingLowList.size() >= 2) {

                    double percentageHighest = startPrice - swingLowList.get(0);
                    double comparisonValue = 0;
                    double lowerBound = 0, upperBound = 0;
                    for (int a = 0; a < swingLowList.size(); a++) {
                        if (a != 0) {

                            double percentageToCompute = startPrice - swingLowList.get(a);
                            double value = ((percentageToCompute / percentageHighest) * 100);
                            if (a == 1) {
                                comparisonValue = 1 - ((percentageToCompute / percentageHighest) * 100);
                                lowerBound = value - comparisonValue - 0.05;
                                upperBound = value - comparisonValue + 0.05;
                            }

                            if (value > lowerBound && value < upperBound) {
                                return new Patterns(tf, Status.ASCENDING_TRIANGLE, 10, candleNumber, 1, "Duration");
                            }
                            /**
                             * startPrice - lowest = max
                             * (startPrice - sL) / max * 100 = Percetage
                             */
                        }
                    }
                    swingLowList.clear();
                    return null;
                } else if (valuesList.get(i).getClose() > startPrice && swingLowList.size() <= 1) { //if it has closed out with 1 or less wing lows
                    swingLowList.clear();
                    return null;
                }

                //If another low is formed
                if (valuesList.get(i).getClose() <= startPrice && valuesList.get(i).getClose() <= possibleSwingLow && rise == false) {
                    possibleSwingLow = valuesList.get(i).getClose();
                    rise = false;
                } else if (valuesList.get(i).getClose() <= startPrice && valuesList.get(i).getClose() <= possibleSwingLow && rise == true) {
                    swingLowList.clear();
                    rise = true;
                }

                //If values reach horizontal line
                if (valuesList.get(i).getHigh() > startPrice && valuesList.get(i).getClose() < startPrice) {
                    if (first) {
                        possibleSwingLow = startPrice;
                    } else {
                        swingLowList.add(possibleSwingLow);
                        possibleSwingLow = startPrice;
                    }
                }
            }
        }
        return null;
    }
}
