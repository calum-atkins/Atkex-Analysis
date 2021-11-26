package FrontEnd;

import BackEnd.Markets.Market;
import BackEnd.Markets.MarketTrend;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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

    private ArrayList<Market> marketsData = new ArrayList<>();
    ObservableList<Market> markets = FXCollections.observableArrayList();

    public Controller(ArrayList<Market> marketsData) {
        this.marketsData = marketsData;
    }

    //Method to show the chart that gets clicked on the table
    @FXML void showChart() {
        Market selectedMarket = marketsTableView.getSelectionModel().getSelectedItem();
        System.out.println(selectedMarket.getIndex());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Setup columns in table
        indexColumn.setCellValueFactory(new PropertyValueFactory<Market, String>("tableIndexColumn"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<Market, String>("tableStatusColumn"));
        trendColumn.setCellValueFactory(new PropertyValueFactory<Market, String>("tableTrendColumn"));

        //Load data
        marketsTableView.setItems(getMarkets());
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
