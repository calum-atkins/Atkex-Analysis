package FrontEnd;

import BackEnd.markets.Market;
import BackEnd.chart.CandleStickChart;

import java.util.ArrayList;

/**
 * Utility class for controlling the view of charts between markets.
 *
 * All methods on the selector are static to facilitate simple access
 * from anywhere in the application.
 */
public class ChartSelector {

    /** Constant for the main fxml file. */
    public static final String MAIN = "homeScreen.fxml";

    /** The main application controller. */
    private static MainController mainController;

    /** Array list to store markets data. */
    private static ArrayList<Market> markets;

    /**
     * Stores the main controller.
     *
     * @param mainController the main application layout controller.
     */
    public static void setMainController(MainController mainController) {
        ChartSelector.mainController = mainController;
    }

    /**
     * Sets the market array list to the controller class.
     *
     * @param m the markets array list.
     */
    public static void setMarkets(ArrayList<Market> m) { mainController.setMarkets(m); }

    /**
     * Retrieves the array list from the main controller class.
     *
     * @return markets array list.
     */
    public static ArrayList<Market> getMarkets() { return mainController.getMarketsData(); }

    /**
     * Loads the specified candle stick chart into the main controller to be
     * displayed to the UI.
     *
     * @param chart the chart under a specified index and timeframe
     */
    public static void loadChart(CandleStickChart chart) { mainController.setChart(chart); }
}
