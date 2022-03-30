package BackEnd.patternRecognition.availablePatterns;

import BackEnd.markets.Market;
import BackEnd.patternRecognition.Patterns;
import FrontEnd.Main;

import java.util.ArrayList;

public class Pennant {
    public static ArrayList<Patterns> findPennants(Market market) {
        ArrayList<Patterns> pennants = new ArrayList<>();

        /** Run check for each timeframe */
        for (int tf = 0; tf < Main.getNumberOfTimeframes(); tf++) {
            if (tf == 0 && market.getIndex().equals("EURGBP")) { //DELETE AFTER

            int counter = 0;

            //Get an array list of all swing's
            ArrayList<Double> swingsList = new ArrayList<>();
            ArrayList<Integer> swingsListCandles = new ArrayList<>();
            double lastSwing = market.getTimeframesDataStore(tf).getMarketValues().get(0).getClose();
            double swingLow = 0;
            double potentialSwingLow = 100;
            double swingHigh = 0;
            double potentialSwingHigh = 0;
            boolean addLow = false;
            boolean addHigh = false;

            int invalidCounter = counter;
            int candleCounter = counter;
            /* Repeat for each value (candle) to find where the turning (swing) points are */
            for (Market.MarketValues values : market.getTimeframesDataStore(tf).getMarketValues()) {
                /* Checking for next swing low */
                if (values.getLow() < lastSwing) {
                    if (values.getLow() > potentialSwingLow) {
                        invalidCounter++;
                        if (invalidCounter == 6) {
                            swingLow = potentialSwingLow;
                            lastSwing = swingLow;
                            potentialSwingHigh = 0;
                            invalidCounter = 0;
                            if (addHigh) {
                                swingsList.add(swingHigh);
                                swingsListCandles.add(candleCounter  - 6);
                                addHigh = true;
                            }
                        }
                    } else if ((((lastSwing / values.getLow()) - 1) * 100) > market.getTrendIndicatorPercentage()) {
                        potentialSwingLow = values.getLow();
                        invalidCounter = 0;
                        addLow = true;
                    }
                }
                /* Checking for next swing high */
                if (values.getHigh() > lastSwing) {
                    if (values.getHigh() < potentialSwingHigh) {
                        invalidCounter++;
                        if (invalidCounter == 6) {
                            swingHigh = potentialSwingHigh;
                            lastSwing = swingHigh;
                            potentialSwingLow = 100;
                            invalidCounter = 0;
                            if (addLow) {
                                swingsList.add(swingLow);
                                swingsListCandles.add(candleCounter  - 6);
                                addLow = false;
                            }
                        }
                    } else if (100 - ((lastSwing / values.getHigh()) * 100) > market.getTrendIndicatorPercentage()) {
                        potentialSwingHigh = values.getHigh();
                        invalidCounter = 0;
                        addHigh = true;
                    }
                }
                candleCounter++;
            }

            //Check if the candle is at a swing ->run checks if it is
                System.out.println("Here " + market.getIndex());
//            for (int i = 0; i < swingsList.size(); i++) {
//                System.out.println(swingsListCandles.get(i));
//            }
            for (int i = 0; i < market.getTimeframesDataStore(tf).getMarketValues().size(); i++) {
                for (int swingCounter = 0; swingCounter <  swingsListCandles.size();  swingCounter++) {
                    if (i == swingsListCandles.get(swingCounter)) {
                        if (checkCandleForPattern(i, market.getTimeframesDataStore(tf).getMarketValues(), tf, market.getPipMultiply(), swingsList, swingsListCandles, swingCounter) != null) {
                            Patterns newDoubleBottom = checkCandleForPattern(i, market.getTimeframesDataStore(tf).getMarketValues(), tf, market.getPipMultiply(), swingsList, swingsListCandles, swingCounter);
                        }

                    }

                }
            }

            }//EURGBP
        }

        return pennants;
    }

    private static Patterns checkCandleForPattern(int startCounter, ArrayList<Market.MarketValues> marketValues, int tf, int pipMultiply, ArrayList<Double> swingsList, ArrayList<Integer> swingListCandles,int swingCounter) {
        //System.out.println(startCounter);
        if (swingListCandles.size() == swingCounter + 1) {
            swingCounter = swingCounter - 1;
        }

        double firstTopSwing = 0;
        double firstBottomSwing = 0;
        int latestSwingCounter = startCounter;
        if (marketValues.get(startCounter).getClose()  > marketValues.get(swingListCandles.get(swingCounter + 1)).getClose()) {
            firstTopSwing = marketValues.get(startCounter).getClose();
            firstBottomSwing = marketValues.get(swingListCandles.get(swingCounter + 1)).getClose();

            // First top to second top percentage change,  needs to be a swing in top 40% of  the pennant
            System.out.println("START HERE " + swingListCandles.get(swingCounter));
            latestSwingCounter = swingListCandles.get(swingCounter);
            double lastSwingPrice = 0;
            int entryCandle = 0;
            for (int i = swingListCandles.get(swingCounter + 1); i < marketValues.size(); i++) {
                /**
                 * IF (FT - New) / (FT - FB) > 40%
                 */
                //System.out.println("Yes : " + (firstTopSwing - marketValues.get(i).getHigh()) / (firstTopSwing - firstBottomSwing));
                /** Add the swings that are in the top 40% */
                ArrayList<Double> newSwings = new ArrayList<>();
                ArrayList<Integer> topSwingsCandles = new ArrayList<>();
                if (newSwings.size() != 0) {
                    //Do a loop to see if the first swing, and the current top swing creates a value within range of one of the next swings
                    
                if (swingListCandles.contains(i) &&
                        ((firstTopSwing - marketValues.get(i).getHigh()) / (firstTopSwing - firstBottomSwing)) < 0.4 &&
                        i > startCounter) {
                    //System.out.println("Swing at " + marketValues.get(i).getHigh());
                    newSwings.add(marketValues.get(i).getHigh());
                    topSwingsCandles.add(i);
                } }
                latestSwingCounter = i;

                /** For each new top 40% swing, check if the value is within range of past swings */
//                for (Double swing : newSwings) { //For each new swing
//                    double percentageChange = (firstTopSwing - swing) / firstTopSwing;
////                    System.out.println("Percentage :" + percentageChange);
//                    double price = swing - (swing * (percentageChange / 2));
//
//                    System.out.println(swing);
//                }


                for (Integer a : topSwingsCandles) {
                    System.out.println("Starting breakout search at: " + a);
                    for (int j = a; j < marketValues.size(); j++) {

                    }
                }

                    //For each candle if price is in target range for last top
                    //for (int j = latestSwingCounter; j < marketValues.size(); j++) {
//                    for (int a = 0; a < newSwings.size(); a++) {
//                        if (price < marketValues.get(i).getHigh() &&
//                                price > marketValues.get(i).getLow()) {
//                            entryCandle = i;
//                            System.out.println("Price in range: " + i);
//                        }
//
//                    }
                    //}


//                    System.out.println("price: " + price + " " + i);
//                    System.out.println(lastSwingPrice);

//                    for (int j = entryCandle; j < marketValues.size(); j++) {
//                        if (marketValues.get(j).getClose() > price) {
//                            return null;
//                        }
//                        if (marketValues.get(j).getClose() < lastSwingPrice) {
//                            System.out.println("Search SP TP");
//                            //return searchStopLossTakeProfit(swingListCandles.get(swingCounter), marketValues, price, lastSwingPrice, pipMultiply, tf, startCounter);
//                        }
//                    }

//                    boolean patternForming = false;
//                    double lastTopSwingPrice = 0;
//                    System.out.println("New s: " + swing);
//                    lastSwingPrice = price;
//                }
//                double pChange = ((firstTopSwing - marketValues.get(i).getHigh()) / firstTopSwing);
//                double pPrice = (marketValues.get(i).getHigh() - (marketValues.get(i).getHigh() * (pChange / 2)));
                //System.out.println("Search for: " + pPrice);

//                    System.out.println(i);
//                    for (int j = latestSwingCounter; j < marketValues.size(); j++) {
//                        if (price < marketValues.get(j).getHigh() &&
//                            price > marketValues.get(j).getLow()) {
//                            System.out.println(price + "Check for breakout at ");
//                            patternForming = true;
//                            return null;
//
//                            //checkForBreakout(marketValues, j, price);
//
//                        }
//                        if (patternForming) {
//                            System.out.println(j + "Pattern forming at: " + lastTopSwingPrice + " : " + price);
//                            for (int a = j; a < marketValues.size(); a++) {
//                                if (marketValues.get(a).getClose() > lastTopSwingPrice) {
//                                    System.out.println("Search for breakout after " + a);
//                                    //System.out.println(a + " : " + marketValues.get(a).getClose());
//                                    //System.out.print("here " + lastSwingPrice);
//                                }
//                            }
//                            patternForming = false;
//                        }
                        //lastTopSwingPrice = price;

//                    }
                    //System.out.println(x);

                    /**
                     * find % change
                     *     (firsttop - swing) / first top = 0.025 change
                     * x = ((change / 2) * swing) - swing
                     *     for each swing (again)
                     *         if x < upper && x > lower
                     */
                    //lastSwingPrice = price;

                //if (firstTopSwing - )

            }

            /**
             * Repeat for number of candles remaining
             * If the second top price is within the top 33% with the first top price - get %
             *     if any of the swings after that land in bewteen lower and upper bound
             *         find the swings in between, if get the % change on bottom
             *             search for breakout
             *                  searchforsltop
             */


//            System.out.println(marketValues.get(swingListCandles.get(swingCounter + 1)).getClose() + "First T and B" + startCounter + ": " + marketValues.get(startCounter).getHigh());

        }
        return null;
    }

    public static Patterns searchStopLossTakeProfit(int entryCandle, ArrayList<Market.MarketValues> marketValues, double stopLossSwingPrice, double startPrice, int pipMultiply, int tf, int startCandle) {
        System.out.println("Entry candle " + entryCandle);
        return null;
    }
}
