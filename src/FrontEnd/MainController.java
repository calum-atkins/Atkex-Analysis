package FrontEnd;


import BackEnd.markets.Market;
import BackEnd.markets.MarketTimeframe;
import BackEnd.patternRecognition.Patterns;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;


import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Main controller class for entire application.
 */
public class MainController implements Initializable {
    /**
     * Table and column definitions to display market information.
     */
    @FXML
    private TableView<Market> marketsTableView;
    @FXML
    private TableColumn<Market, String> indexColumn;
    @FXML
    private TableColumn<Market, String> statusColumn;
    @FXML
    private TableColumn<Market, String> trendColumn;

    /**
     * Table and column definitions to display pattern recognition.
     */
    @FXML
    private TableView<Patterns> patternsTableView;

    @FXML
    private ImageView imageViewLogo;

    /** Holder for chart views to switch in. */
    @FXML
    private StackPane chartView;

    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private Button loadDataButton;

    @FXML
    private ComboBox comboBox;

    /** Array list to store market data. */
    private static ArrayList<Market> marketsData = new ArrayList<>();

    /** List to display market data to markets table. */
    ObservableList<Market> marketsList = FXCollections.observableArrayList();

    private MarketTimeframe currentTimeframe;



    public MainController() { }

    @FXML void loadData() {
        marketsTableView.setItems(getMarkets());
    }

    /**
     * Method to find the selected market value within the table and
     * display the corresponding candle stick chart.
     */
    @FXML void showChart() {
        Market selectedMarket = marketsTableView.getSelectionModel().getSelectedItem();
        if (comboBox.getSelectionModel().getSelectedItem() == null) {
            currentTimeframe = MarketTimeframe.ONE_HOUR;
        } else {
            switch (comboBox.getSelectionModel().getSelectedItem().toString()) {
                case "4h":
                    currentTimeframe = MarketTimeframe.FOUR_HOUR;
                    break;
                case "D":
                    currentTimeframe = MarketTimeframe.DAY;
                    break;
                default:
                    currentTimeframe = MarketTimeframe.ONE_HOUR;
                    break;
            }
        }
        if (marketsTableView.getSelectionModel().getSelectedItem() == null) {
            System.out.println("test");
            selectedMarket = marketsData.get(0);
        }
        System.out.println(selectedMarket.getIndex() + " Selected || " + currentTimeframe.toString() + " Timeframe");
        for (int i = 0; i < marketsData.size();i++) {
            if (marketsData.get(i).getIndex().equals(selectedMarket.getIndex())) {
                switch (currentTimeframe) {
                    case FOUR_HOUR:
                        ChartSelector.loadChart(
                                ChartSelector.getMarkets().get(i).getTimeframesDataStore(1).getCandleStickChart());
                        break;
                    case DAY:
                        ChartSelector.loadChart(
                                ChartSelector.getMarkets().get(i).getTimeframesDataStore(0).getCandleStickChart());
                        break;
                    default:
                        ChartSelector.loadChart(
                                ChartSelector.getMarkets().get(i).getTimeframesDataStore(2).getCandleStickChart());
                        break;
                }
            }
        }
    }

    /**
     * Initialise the columns within table.
     * @param location
     * @param resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        indexColumn.setCellValueFactory(new PropertyValueFactory<Market, String>("tableIndexColumn"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<Market, String>("tableStatusColumn"));
        trendColumn.setCellValueFactory(new PropertyValueFactory<Market, String>("tableTrendColumn"));

        marketsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        patternsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        comboBox.getItems().add("1h");
        comboBox.getItems().add("4h");
        comboBox.getItems().add("D");

        getMarkets();
    }

    public void setMarkets(ArrayList<Market> markets) { this.marketsData = markets; }

    public ArrayList<Market> getMarketsData() { return marketsData; }

    public void setChart(Node node) { chartView.getChildren().setAll(node); }

    /**
     * Method to create observable list to display to UI.
     *
     * @return the observable list.
     */
    public ObservableList<Market> getMarkets() {
        for (int i = 0; i < marketsData.size(); i++) {
            marketsList.add(new Market(
                    marketsData.get(i).getIndex(),
                    marketsData.get(i).getStatus(),
                    marketsData.get(i).getTrend()));
        }
        return marketsList;
    }
}
