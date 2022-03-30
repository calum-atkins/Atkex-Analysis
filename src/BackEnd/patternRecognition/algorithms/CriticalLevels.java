package BackEnd.patternRecognition.algorithms;

import BackEnd.markets.Market;
import FrontEnd.Main;

import java.util.ArrayList;

/**
 * Class is used to declare and define functions that are used to find critical price levels
 * Two methods are used to find all levels of Support and one for Resistance
 *
 * To find the levels, The model separates the candles into 'segments' that can be used to find
 * the price action that is passed through the most, and repeat until there is 'strong' prices (a lot of price action)
 */
public abstract class CriticalLevels {

    /**
     * @param markets array list of all the market data
     * @return the market array list is edited to add support levels and returned
     */
    public static ArrayList<Market> generateSupportLevels(ArrayList<Market> markets) {
        for (Market m : markets) {
            for (int i = 0; i < Main.getNumberOfTimeframes(); i++) {
                /* Define an array to store the segments */
                ArrayList<Float> arrayOfMin = m.getTimeframesDataStore(i).getMinSegments();
                /* Repeat until array is empty */
                while (!arrayOfMin.isEmpty()) {
                    ArrayList<Float> arrayToSetMin = new ArrayList<>();

                    /* Find the minimum value in array */
                    float minOfArray = 0;
                    ArrayList<Float> supportArray = new ArrayList<>();

                    for (int j = 0; j < arrayOfMin.size(); j++) {
                        if (j == 0) {
                            minOfArray = arrayOfMin.get(j);
                        } else if (arrayOfMin.get(j) > minOfArray) {
                            minOfArray = arrayOfMin.get(j);
                        }
                    }

                    /* Find any within range */
                    int size = arrayOfMin.size();
                    for (int j = 0; j < size; j++) {
                        /* Remove minimum value */
                        if (arrayOfMin.get(j) == minOfArray) {
                            supportArray.add(minOfArray);
                        } else if ((arrayOfMin.get(j) / minOfArray) < m.getUpperRange()
                                && (arrayOfMin.get(j) / minOfArray) > m.getLowerRange()) {
                            supportArray.add(arrayOfMin.get(j));
                        } else {
                            arrayToSetMin.add(arrayOfMin.get(j));
                        }

                    }

                    /* Find level average and strength */
                    float supportLevel = 0;
                    for (int j = 0; j < supportArray.size(); j++) {
                        supportLevel += supportArray.get(j);
                    }
                    supportLevel /= supportArray.size();

                    /* Check strength, anything over 2 is valid */
                    if (supportArray.size() > 2) {
                        m.getTimeframesDataStore(i).addSupport(supportLevel);
                    }

                    arrayOfMin = arrayToSetMin;
                }
            }
        }
        return markets;
    }

    /**
     * @param markets array list of all the market data
     * @return the market array list is edited to add resistance levels and returned
     */
    public static ArrayList<Market> generateResistanceLevels(ArrayList<Market> markets) {
        for (Market m : markets) {
            for (int i = 0; i < Main.getNumberOfTimeframes(); i++) {
                /* Define an array to store the segments */
                ArrayList<Float> arrayOfMax = m.getTimeframesDataStore(i).getMaxSegments();
                /* Repeat until array is empty */
                while (!arrayOfMax.isEmpty()) {
                    ArrayList<Float> arrayToSetMax = new ArrayList<>();

                    /* Find max value */
                    float maxOfArray = 0;
                    ArrayList<Float> resistanceArray = new ArrayList<>();
                    for (int j = 0; j < arrayOfMax.size(); j++) {
                        if (j == 0) {
                            maxOfArray = arrayOfMax.get(j);
                        } else if (arrayOfMax.get(j) < maxOfArray) {
                            maxOfArray = arrayOfMax.get(j);
                        }
                    }

                    int size = arrayOfMax.size();
                    for (int j = 0; j < size; j++) {
                        if (arrayOfMax.get(j) == maxOfArray) {
                            resistanceArray.add(maxOfArray);
                        } else if ((arrayOfMax.get(j) / maxOfArray) < m.getUpperRange()
                                && (arrayOfMax.get(j) / maxOfArray) > m.getLowerRange()) {
                            resistanceArray.add(arrayOfMax.get(j));
                        } else {
                            arrayToSetMax.add(arrayOfMax.get(j));
                        }
                    }

                    /* Find level average and strength */
                    float resistanceLevel = 0;
                    for (int j = 0; j < resistanceArray.size(); j++) {
                        resistanceLevel += resistanceArray.get(j);
                    }
                    resistanceLevel /= resistanceArray.size();


                    /* Check strength, anything over 2 is valid */
                    if (resistanceArray.size() > 2) {
                        m.getTimeframesDataStore(i).addResistance(resistanceLevel);
                    }
                    arrayOfMax = arrayToSetMax;
                }
            }
        }
        return markets;
    }
}
