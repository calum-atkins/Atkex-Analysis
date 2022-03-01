package FrontEnd;


import BackEnd.markets.Market;
import BackEnd.markets.MarketTimeframe;


import BackEnd.patternRecognition.Patterns;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
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

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TextArea textArea;

    /**
     * Table and column definitions to display pattern recognition.
     */
    @FXML
    private TableView<Patterns> patternsTableView;
    @FXML
    private TableColumn<Patterns, String> timeframeColumn;
    @FXML
    private TableColumn<Patterns, String> patternNumberColumn;
    @FXML
    private TableColumn<Patterns, String> profitLossColumn;
    @FXML
    private TableColumn<Patterns, String> startCandleColumn;
    @FXML
    private TableColumn<Patterns, String> entryCandleColumn;
    @FXML
    private TableColumn<Patterns, String> durationColumn;

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
    @FXML
    private CheckBox checkBoxCriticalLevels;

    private boolean criticalLevelsCheck = false;
    private boolean patternIdentifierCheck = false;

    /** Array list to store market data. */
    private static ArrayList<Market> markets = new ArrayList<>();

    /** List to display market data to markets table. */
    ObservableList<Market> marketsList = FXCollections.observableArrayList();
    ObservableList<Patterns> patternsList = FXCollections.observableArrayList();

    private MarketTimeframe currentTimeframe;
    private boolean marketsLoaded = false;

    public MainController() {
    }

    /**
     * Method called on load button press on the UI to display
     * market list in table.
     */
    @FXML void loadData() {
        if (!marketsLoaded) {
            marketsTableView.setItems(getMarkets());
            marketsLoaded = true;
        }
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
            selectedMarket = markets.get(0);
        }
        reloadScrollPaneMarket(selectedMarket, currentTimeframe);
        reloadPatternTable(selectedMarket);
        System.out.println(selectedMarket.getIndex() + " Selected || " + currentTimeframe.toString() + " Timeframe");
        for (int i = 0; i < markets.size();i++) {
            if (markets.get(i).getIndex().equals(selectedMarket.getIndex())) {
                switch (currentTimeframe) {
                    case FOUR_HOUR:
                        if (criticalLevelsCheck) {
                            ChartSelector.loadChart(
                                    ChartSelector.getMarkets().get(i).getTimeframesDataStore(1).getCandleStickChartCriticalLevels());
                        } else if (patternIdentifierCheck) {
                            ChartSelector.loadChart(
                                    ChartSelector.getMarkets().get(i).getTimeframesDataStore(1).getCandleStickChartPatternIdentifiers());
                        } else {
                            ChartSelector.loadChart(
                                    ChartSelector.getMarkets().get(i).getTimeframesDataStore(1).getCandleStickChartEmpty());
                        }
                        break;
                    case DAY:
                        if (criticalLevelsCheck) {
                            ChartSelector.loadChart(
                                    ChartSelector.getMarkets().get(i).getTimeframesDataStore(0).getCandleStickChartCriticalLevels());
                        } else if (patternIdentifierCheck) {
                            ChartSelector.loadChart(
                                    ChartSelector.getMarkets().get(i).getTimeframesDataStore(0).getCandleStickChartPatternIdentifiers());
                        } else {
                            ChartSelector.loadChart(
                                    ChartSelector.getMarkets().get(i).getTimeframesDataStore(0).getCandleStickChartEmpty());
                        }
                        break;
                    default:
                        if (criticalLevelsCheck) {
                            ChartSelector.loadChart(
                                    ChartSelector.getMarkets().get(i).getTimeframesDataStore(2).getCandleStickChartCriticalLevels());
                        } else if (patternIdentifierCheck) {
                            ChartSelector.loadChart(
                                    ChartSelector.getMarkets().get(i).getTimeframesDataStore(2).getCandleStickChartPatternIdentifiers());
                        } else {
                            ChartSelector.loadChart(
                                    ChartSelector.getMarkets().get(i).getTimeframesDataStore(2).getCandleStickChartEmpty());
                        }
                        break;
                }
            }
        }
    }

    /**
     * Method used to identify when a pattern in the table is selected to display relevant information to the user.
     */
    @FXML void onPatternClick() {
        Patterns patternSelected = patternsTableView.getSelectionModel().getSelectedItem();
        reloadScrollPanePattern(patternSelected);

        /**
         *  get bc chart to show
         *  add lines for the pattern selected
         *  display chart to the UI
         */
    }

    @FXML void toggleCriticalLevels() {
        if (patternIdentifierCheck) {
            System.out.println("Please deselect the patterns identifier first");
        } else {
            if (criticalLevelsCheck) {
                criticalLevelsCheck = false;
                showChart();
            } else {
                criticalLevelsCheck = true;
                showChart();
            }
        }
    }

    @FXML void togglePatternIdentifiers() {
        if (criticalLevelsCheck) {
            System.out.println("Please deselect the critical levels first");
        } else {
            if (patternIdentifierCheck) {
                patternIdentifierCheck = false;
                showChart();
            } else {
                patternIdentifierCheck = true;
                showChart();
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

        timeframeColumn.setCellValueFactory(new PropertyValueFactory<Patterns, String>("tableTimeframeColumn"));
        patternNumberColumn.setCellValueFactory(new PropertyValueFactory<Patterns, String>("tablePatternNumberColumn"));
        profitLossColumn.setCellValueFactory(new PropertyValueFactory<Patterns, String>("tableProfitLossColumn"));
        startCandleColumn.setCellValueFactory(new PropertyValueFactory<Patterns, String>("tableStartCandleColumn"));
        entryCandleColumn.setCellValueFactory(new PropertyValueFactory<Patterns, String>("tableEntryCandleColumn"));
        durationColumn.setCellValueFactory(new PropertyValueFactory<Patterns, String>("tableDurationColumn"));

        imageViewLogo.setImage(new Image("/img/atkex_logo_dark.png"));

        textArea.setEditable(false);
        textArea.appendText("Welcome to Atkex Analysis!");

        marketsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        patternsTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        comboBox.getItems().add("1h");
        comboBox.getItems().add("4h");
        comboBox.getItems().add("D");
    }

    /**
     * Setter and getter for markets data.
     */
    public void setMarkets(ArrayList<Market> markets) { this.markets = markets; }
    public ArrayList<Market> getMarketsData() { return markets; }

    /**
     * Method to set chart to the UI
     * @param node chart to display
     */
    public void setChart(Node node) { chartView.getChildren().setAll(node); }

    /**
     * Method to create observable list to display to UI.
     *
     * @return the observable list.
     */
    public ObservableList<Market> getMarkets() {
        for (int i = 0; i < markets.size(); i++) {
            marketsList.add(new Market(
                    markets.get(i).getIndex(),
                    markets.get(i).getStatus(),
                    markets.get(i).getTrend()));
        }
        return marketsList;
    }

    public ObservableList<Patterns> getPatterns(Market selected) {
        for (Market m : markets) {
            if (selected.getIndex().equals(m.getIndex())) {
                for (int i = 0; i < m.getPatternsList().size(); i++) {
                    patternsList.add(new Patterns(m.getPatternsList().get(i).getTimeframe(),
                            m.getPatternsList().get(i).getType(),
                            m.getPatternsList().get(i).getProfitLoss(),
                            m.getPatternsList().get(i).getStartCandle(),
                            m.getPatternsList().get(i).getEntryCandle(),
                            m.getPatternsList().get(i).getDuration()));
                }
            }

        }
        return patternsList;
    }

    /**
     * Method to refresh the text in the scroll pane.
     * @param selectedMarket current selected market.
     * @param currentTimeframe current selected timeframe.
     */
    public void reloadScrollPaneMarket(Market selectedMarket, MarketTimeframe currentTimeframe) {
        int timeframeInteger;
        switch (currentTimeframe) {
            case DAY: timeframeInteger = 0; break;
            case FOUR_HOUR: timeframeInteger = 1; break;
            default: timeframeInteger = 2; break;
        }

        int marketNumber = 0;
        for (int i = 0; i < markets.size(); i++) {
            if (markets.get(i).getIndex().equals(selectedMarket.getIndex())) {
                marketNumber = i;
            }
        }

        int numberOfCandles = markets.get(marketNumber).getTimeframesDataStore(timeframeInteger).getMarketValues().size();
        textArea.clear();
        textArea.appendText(selectedMarket.getIndex().toString() + "\n");
        textArea.appendText("Start Date: " + markets.get(marketNumber).getTimeframesDataStore(timeframeInteger).getMarketValues().get(0).getDate() + "\n");
        textArea.appendText("End Date: " + markets.get(marketNumber).getTimeframesDataStore(timeframeInteger).getMarketValues().get(numberOfCandles - 1).getDate() + "\n");
        textArea.appendText("Number of Candles: " + numberOfCandles + "\n");
        textArea.appendText("Profit Loss (Pips): " + markets.get(marketNumber).getReturnsPips() + "\n");
        textArea.appendText(currentTimeframe.toString());

    }

    /**
     * Method used to refresh the scroll pane to display information to the user.
     * @param pattern the pattern selected in the patterns table.
     */
    public void reloadScrollPanePattern(Patterns pattern) {
        textArea.clear();
        textArea.appendText(pattern.getProfitLoss() + "\n");
//        textArea.appendText("Start Date: " + markets.get(marketNumber).getTimeframesDataStore(timeframeInteger).getMarketValues().get(0).getDate() + "\n");
//        textArea.appendText("End Date: " + markets.get(marketNumber).getTimeframesDataStore(timeframeInteger).getMarketValues().get(numberOfCandles - 1).getDate() + "\n");
//        textArea.appendText("Number of Candles: " + numberOfCandles + "\n");
//        textArea.appendText(currentTimeframe.toString());

    }

    public void reloadPatternTable(Market selectedMarket) {
        for (Market m : markets) {
            if (m.getIndex().equals(selectedMarket.getIndex())) {
                patternsTableView.getItems().clear();
                patternsTableView.setItems(getPatterns(selectedMarket));
            }
        }
    }


}
