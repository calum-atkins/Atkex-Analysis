package FrontEnd;

import BackEnd.Markets.Market;
import BackEnd.chart.CandleStickChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.io.IOException;
import java.util.ArrayList;

public class ChartSelector {

    public static final String MAIN = "homeScreen.fxml";
    private static Controller mainController;
    private static ArrayList<Market> markets;

    public static void setMarkets(ArrayList<Market> m) { mainController.setMarkets(m); }

    public static ArrayList<Market> getMarkets() { return mainController.getMarketsData(); }

    public static void setMainController(Controller mainController) {
        ChartSelector.mainController = mainController;
    }

    public static void loadChart(CandleStickChart chart) { mainController.setChart(chart); }
}
