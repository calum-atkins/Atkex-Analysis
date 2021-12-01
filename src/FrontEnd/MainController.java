package FrontEnd;

import BackEnd.markets.Market;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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

    /** Holder for chart views to switch in. */
    @FXML
    private StackPane chartView;

    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private Button loadDataButton;

    /** Array list to store market data. */
    private static ArrayList<Market> marketsData = new ArrayList<>();

    /** List to display market data to markets table. */
    ObservableList<Market> marketsList = FXCollections.observableArrayList();

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
        System.out.println(selectedMarket.getIndex() + " Selected");
        for (int i = 0; i < marketsData.size();i++) {
            if (marketsData.get(i).getIndex().equals(selectedMarket.getIndex())) {
                ChartSelector.loadChart(
                        ChartSelector.getMarkets().get(i).getDayCandleStickChart());
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
