package FrontEnd;

import BackEnd.Markets.CandleStickChart;
import BackEnd.Markets.Market;
import BackEnd.Markets.MarketTrend;
import BackEnd.Markets.Status;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.image.Image;
import javafx.stage.Stage;


import java.io.*;
import java.util.Iterator;

//
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main extends Application {

    private ArrayList<Market> markets = new ArrayList<>();
    private String rawDataLocation = "Resources/MarketsRawData";
    private final String rawDataFileType = ".csv";


    public void start2() {

        System.out.println(markets.get(5).getOneHourValues().get(0).getOpen());
        System.out.println(markets.get(5).getOneDayValues().get(0).getOpen());

        //loader here
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("homeScreen.fxml"));

    }


    public void saveMarkets(String location) {

    }

    public void loadSaveData() {
        String oneHourSuffix = ", 60" + rawDataFileType;
        String fourHourSuffix = ", 240" + rawDataFileType;
        String oneDaySuffix = ", 1D" + rawDataFileType;

        //Loop repeats for number of markets in folder
        for (int i = 0; i < markets.size(); i++) {
            //Get index and make array of directory for markets
            String index = markets.get(i).getIndex();
            File directoryPath = new File("Resources/MarketsRawData/" + index);
            String marketsList[] = directoryPath.list();

            String line = "";
            //Loop to repeat for three timeframes
            for (int j = 0; j < 3; j++) {
                String path = null;
                if (marketsList[j].equals(index + oneHourSuffix)) {
                    path = rawDataLocation + "/" + index + "/" + index + oneHourSuffix;
                    try {

                        BufferedReader br = new BufferedReader(new FileReader(path));
                        while ((line = br.readLine()) != null) {
                            if (!line.equals("time,open,high,low,close")) {
                                String[] values = line.split(",");
                                markets.get(i).addOneHourValues(Float.parseFloat(values[1]),
                                        Float.parseFloat(values[2]), Float.parseFloat(values[3]),
                                        Float.parseFloat(values[4]));

                                //create candlestick charts here
                                //CandleStickChart bc = new CandleStickChart()
                            }
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (marketsList[j].equals(index + fourHourSuffix)) {
                    path = rawDataLocation + "/" + index + "/" + index + fourHourSuffix;
                    try {

                        BufferedReader br = new BufferedReader(new FileReader(path));
                        while ((line = br.readLine()) != null) {
                            if (!line.equals("time,open,high,low,close")) {
                                String[] values = line.split(",");
                                markets.get(i).addFourHourValues(Float.parseFloat(values[1]),
                                        Float.parseFloat(values[2]), Float.parseFloat(values[3]),
                                        Float.parseFloat(values[4]));

                                //create candlestick charts here
                                //CandleStickChart bc = new CandleStickChart()
                            }
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (marketsList[j].equals(index + oneDaySuffix)) {
                    path = rawDataLocation + "/" + index + "/" + index + oneDaySuffix;
                    try {

                        BufferedReader br = new BufferedReader(new FileReader(path));
                        while ((line = br.readLine()) != null) {
                            if (!line.equals("time,open,high,low,close")) {
                                String[] values = line.split(",");
                                markets.get(i).addOneDayValues(Float.parseFloat(values[1]),
                                        Float.parseFloat(values[2]), Float.parseFloat(values[3]),
                                        Float.parseFloat(values[4]));

                                //create candlestick charts here
                                //CandleStickChart bc = new CandleStickChart()
                            }
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        System.out.println("Data successfully loaded");
    }

    //
    @Override
    public void start(Stage primaryStage) throws Exception {

        String[] contents = getAllIndex();
        for (int i = 0; i < contents.length; i++) {
            markets.add(new Market(contents[i], Status.PENDING, MarketTrend.NO_TREND));
        }

        Controller controller = new Controller(markets);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("homeScreen.fxml"));
        loader.setController(controller);

        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        //primaryStage.setMaximized(true);
        primaryStage.setTitle("Atkex");
        primaryStage.getIcons().add(new Image("img/AtkexLogo.png"));
        primaryStage.setResizable(true);
        primaryStage.show();

        loadSaveData();
        start2();


    }

    public static String[] getAllIndex() {
        File directoryPath = new File("Resources/MarketsRawData");
        String contents[] = directoryPath.list();

        return contents;
    }

    public ArrayList<Market> getMarkets() {
        return markets;
    }

    public void setMarkets(ArrayList<Market> markets) {
        this.markets = markets;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
