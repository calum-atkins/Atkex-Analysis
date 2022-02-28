package BackEnd.patternRecognition.availablePatterns;

import BackEnd.markets.Market;
import BackEnd.patternRecognition.Patterns;
import FrontEnd.Main;

import java.util.ArrayList;

public class DescendingTriangle {
//    public static ArrayList<Patterns> findDescendingTriangles(Market market) {
//        ArrayList<Patterns> descendingTriangles = new ArrayList<Patterns>();
//        for (int tf = 0; tf < Main.getNumberOfTimeframes(); tf++) {
//            int counter = 0;
//            for (Market.MarketValues values : market.getTimeframesDataStore(tf).getMarketValues()) {
//                counter++;
//                /** If resistance/support line is in between the open and the close then start price = close. */
//                for (Double d : market.getTimeframesDataStore(tf).getSupportLevels()) {
//                    if (d < values.getOpen() && d > values.getClose()) {
//                        /** Go to method send start price, candle number, market values and timeframe. */
//                        if (checkCandleForPattern(d, counter, market.getTimeframesDataStore(tf).getMarketValues(), tf, market.getPipMultiply()) != null) {
//                            ascendingTriangles.add(checkCandleForPattern(d, counter, market.getTimeframesDataStore(tf).getMarketValues(), tf, market.getPipMultiply()));
//                        }
//                    }
//                }
//            }
//        }
//        return ascendingTriangles;
//    }

}
