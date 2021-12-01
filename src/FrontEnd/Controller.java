package FrontEnd;

import BackEnd.Markets.Market;
import BackEnd.Markets.MarketTrend;
import BackEnd.chart.CandleStickChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private TableView<Market> marketsTableView;
    @FXML
    private TableColumn<Market, String> indexColumn;
    @FXML
    private TableColumn<Market, String> statusColumn;
    @FXML
    private TableColumn<Market, String> trendColumn;
    @FXML
    private StackPane chartView;
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private Button loadDataButton;

    private static ArrayList<Market> marketsData = new ArrayList<>();
    ObservableList<Market> markets = FXCollections.observableArrayList();

    @FXML void loadData() {
        System.out.println("Loading data...");
        marketsTableView.setItems(getMarkets());
    }

    //Method to show the chart that gets clicked on the table
    @FXML void showChart() {
        Market selectedMarket = marketsTableView.getSelectionModel().getSelectedItem();
        System.out.println(selectedMarket.getIndex() + " Selected");
        for (int i = 0; i < marketsData.size();i++) {
            if (marketsData.get(i).getIndex().equals(selectedMarket.getIndex())) {
                ChartSelector.loadChart(ChartSelector.getMarkets().get(i).getDayCandleStickChart());
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Setup columns in table
        indexColumn.setCellValueFactory(new PropertyValueFactory<Market, String>("tableIndexColumn"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<Market, String>("tableStatusColumn"));
        trendColumn.setCellValueFactory(new PropertyValueFactory<Market, String>("tableTrendColumn"));
    }

    public Controller() {
    }

    public void setMarkets(ArrayList<Market> markets) {
        this.marketsData = markets;
    }

    public ArrayList<Market> getMarketsData() {
        return marketsData;
    }

    public void setChart(Node node) {
        chartView.getChildren().setAll(node);
    }

    //Method to get markets
    public ObservableList<Market> getMarkets() {
        for (int i = 0; i < marketsData.size(); i++) {
            markets.add(new Market(marketsData.get(i).getIndex(),
                    marketsData.get(i).getStatus(), marketsData.get(i).getTrend()));
        }
        return markets;
    }
}
