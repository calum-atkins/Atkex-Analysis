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
        //if (market.getIndex().equals("EURGBP")) {

            for (int i = 0; i < Main.getNumberOfTimeframes(); i++) {
                //if (i == 2) {
                double startPrice = 0;
                double startPriceCandle = 0;
                double possibleSwingLow = 0;
                double percentageChange = 0;
                ArrayList<Double> swingLowList = new ArrayList<>();
//                    for (int a = 0; a < market.getTimeframesDataStore(i).getMarketValues().size(); a++) {
                int counter = 0;
                boolean formingTriangle = false;
                boolean first = false;
                boolean rise = false;
                for (Market.MarketValues values : market.getTimeframesDataStore(i).getMarketValues()) {
                    counter++;
                    /**
                     * If  resistance/support line is in between the open and the close then start price = close
                     */
                    //if (!formingTriangle) {
                    for (Double d : market.getTimeframesDataStore(i).getResistanceLevels()) {
                        if (d < values.getOpen() && d > values.getClose()) {
                            //Go to method send start price, candle number and markets.
                            if (checkCandleForPattern(d, counter, market.getTimeframesDataStore(i).getMarketValues(), i) != null) {

                                ascendingTriangles.add(checkCandleForPattern(d, counter, market.getTimeframesDataStore(i).getMarketValues(), i));
                            }
                            startPrice = d;
                            possibleSwingLow = startPrice;
                            startPriceCandle = counter;
                            formingTriangle = true;
                            first = true;
                        }
                    }

                }
            //}
        //}

                        //}
                        //If the triangle has closed out with 2 or more sinwg lows
//                        if (values.getClose() > startPrice && swingLowList.size() >= 2 && formingTriangle == true) {
//                            swingLowList.clear();
//                            formingTriangle = false;
//                            ascendingTriangles.add(new Patterns(Status.ASCENDING_TRIANGLE, 100, values.getDate(), startPriceCandle));
//                        } else if (values.getClose() > startPrice && swingLowList.size() <= 1 && formingTriangle == true) { //if it has closed out with 1 or less wing lows
//                            swingLowList.clear();
//                            formingTriangle = false;
//                        }
//
//                        //If another low is formed
//                        if (values.getClose() <= startPrice && values.getClose() <= possibleSwingLow && rise == false) {
//                            possibleSwingLow = values.getClose();
//                            rise = false;
//                        } else if (values.getClose() <= startPrice && values.getClose() <= possibleSwingLow && rise == true) {
//                            formingTriangle = false;
//                            swingLowList.clear();
//                            rise = true;
//                        }
//
//                        //If values reach horizontal line
//                        if (values.getHigh() > startPrice && values.getClose() < startPrice) {
//                            if (first) {
//                                possibleSwingLow = startPrice;
//                                formingTriangle = true;
//                            } else {
//                                swingLowList.add(possibleSwingLow);
//                                possibleSwingLow = startPrice;
//                            }
//                        }
////                        if (values.getClose() < market.getTimeframesDataStore(i).getMarketValues().get(counter-1).getClose()) {
////                            formingTriangle = false;
////                            swingLowList.clear();
////                        }
//
////                        if (formingTriangle == true && values.getClose() <= startPrice && values.getClose() >= possibleSwingLow) {
////
////                        } else {
////                            formingTriangle = false;
////                            swingLowList.clear();
////                        }
//                        if (!swingLowList.isEmpty()) {
//                        if (formingTriangle == true && values.getClose() < swingLowList.get(0)) {
//                            formingTriangle = false;
//                            swingLowList.clear();
//                        }}


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
//                    if (market.getIndex().equals("EURGBP")) { //1 Hour
//                        System.out.println(market.getIndex());
//                        ascendingTriangles.add(new Patterns(Status.ASCENDING_TRIANGLE, 100, 1, 1));
//                    }
//                }
//            }

            //return ascendingTriangles;
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
            System.out.println(candleNumber);
        } else {
        //System.out.println(tf + "   " + candleNumber);
//        System.out.println(valuesList.size());
//        System.out.println(valuesList.get(524).getDate());

            Date startDate = valuesList.get(candleNumber).getDate();

            //double percentageChange = 0;
            ArrayList<Double> swingLowList = new ArrayList<>();
//                    for (int a = 0; a < market.getTimeframesDataStore(i).getMarketValues().size(); a++) {
            boolean first = false;
            boolean rise = false;
            //System.out.println(candleNumber +  " " + startPrice);
            //If the triangle has closed out with 2 or more sinwg lows
            int length = valuesList.size() - candleNumber;
            boolean validTriangle = false;
            for (int i = candleNumber; i < length; i++) {
                if (valuesList.get(i).getClose() > startPrice && swingLowList.size() >= 2) {
                    //System.out.println(swingLowList.get(0) + " Crossing " + startPrice + "found " + swingLowList.size());

                    double percentageHighest = startPrice - swingLowList.get(0);
                    double comparisonValue = 0;
                    double lowerBound = 0, upperBound = 0;
                    for (int a = 0; a < swingLowList.size(); a++) {
                        //System.out.println(swingLowList.get(0));
                        if (a != 0) {

                            double compareOne = startPrice / swingLowList.get(a);
                            double compareTwo = startPrice / swingLowList.get(a - 1);
                            //System.out.println(swingLowList.get(a) + "   " + swingLowList.get(a-1));
                            //System.out.println(compareOne + "   " + compareTwo);
                            double percentageToCompute = startPrice - swingLowList.get(a);
                            //System.out.println((percentageToCompute / percentageHighest) * 100);

                            double value = ((percentageToCompute / percentageHighest) * 100);
                            if (a == 1) {
                                comparisonValue = 1 - ((percentageToCompute / percentageHighest) * 100);
                                lowerBound = value - comparisonValue - 0.05;
                                upperBound = value - comparisonValue + 0.05;
                            }
                            //System.out.println(lowerBound + "  lu  " + upperBound);

                            if (value > lowerBound && value < upperBound) {
                                //System.out.println("Triangle found");
                                /**
                                 * HERE
                                 */
                                return new Patterns(tf, Status.ASCENDING_TRIANGLE, 100, startDate, (double) candleNumber);
                            }


                            //System.out.println(comparisonValue);
                            /**
                             * startPrice - lowest = max
                             * (startPrice - sL) / max * 100 = Percetage
                             */
                        }
                    }
                    swingLowList.clear();
                    return null;
//                return new Patterns(Status.ASCENDING_TRIANGLE, 100, startDate, (double) candleNumber);

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

//        if (!swingLowList.isEmpty()) {
//            if (formingTriangle == true && values.getClose() < swingLowList.get(0)) {
//                formingTriangle = false;
//                swingLowList.clear();
//            }}
            }
        }
        return null;

    }
}
