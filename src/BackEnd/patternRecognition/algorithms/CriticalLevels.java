package BackEnd.patternRecognition.algorithms;

import BackEnd.markets.Market;
import FrontEnd.Main;

import java.util.ArrayList;

public abstract class CriticalLevels {


    public static ArrayList<Double> start(Market market) {
        return null;
    }

    public ArrayList<Market> getHorizontalSupportResistance() {
        return null;
    }

    public static ArrayList<Market> generateSupportLevels(ArrayList<Market> markets) {
        for (Market m : markets) {
            for (int i = 0; i < Main.getNumberOfTimeframes(); i++) {
                //Segments
                ArrayList<Float> arrayOfMin = m.getTimeframesDataStore(i).getMinSegments();
                //Repeat until array is empty
                while (!arrayOfMin.isEmpty()) {
                    ArrayList<Float> arrayToSetMin = new ArrayList<>();

                    //Find the minimum value in array
                    float minOfArray = 0;
                    ArrayList<Float> supportArray = new ArrayList<>();

                    for (int j = 0; j < arrayOfMin.size(); j++) {
                        if (j == 0) {
                            minOfArray = arrayOfMin.get(j);
                        } else if (arrayOfMin.get(j) > minOfArray) {
                            minOfArray = arrayOfMin.get(j);
                        }
                    }

                    //Find any within range
                    int size = arrayOfMin.size();
                    for (int j = 0; j < size; j++) {
                        //Remove minimum value
                        if (arrayOfMin.get(j) == minOfArray) {
                            supportArray.add(minOfArray);
                        } else if ((arrayOfMin.get(j) / minOfArray) < m.getUpperRange()
                                && (arrayOfMin.get(j) / minOfArray) > m.getLowerRange()) {
                            supportArray.add(arrayOfMin.get(j));
                        } else {
                            arrayToSetMin.add(arrayOfMin.get(j));
                        }

                    }

                    //Find level avg and strength
                    float supportLevel = 0;
                    for (int j = 0; j < supportArray.size(); j++) {
                        supportLevel += supportArray.get(j);
                    }
                    supportLevel /= supportArray.size();

                    //Check strength, anything over 2 is acceptable
                    if (supportArray.size() > 2) {
                        m.getTimeframesDataStore(i).addSupport(supportLevel);
                    }

                    arrayOfMin = arrayToSetMin;
                }
            }
        }
        return markets;
    }

    public static ArrayList<Market> generateResistanceLevels(ArrayList<Market> markets) {
        for (Market m : markets) {
            for (int i = 0; i < Main.getNumberOfTimeframes(); i++) {
                //Segments
                ArrayList<Float> arrayOfMax = m.getTimeframesDataStore(i).getMaxSegments();
                //Repeat until array is empty
                while (!arrayOfMax.isEmpty()) {
                    ArrayList<Float> arrayToSetMax = new ArrayList<>();

                    //Find max value
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

                    //Find level avg and strength

                    float resistanceLevel = 0;
                    for (int j = 0; j < resistanceArray.size(); j++) {
                        resistanceLevel += resistanceArray.get(j);
                    }
                    resistanceLevel /= resistanceArray.size();


                    //Check strength, anything over 2 is acceptable
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
