package FrontEnd;

import BackEnd.Markets.Market;
import BackEnd.Markets.MarketTrend;
import BackEnd.Markets.Status;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

//
import java.io.File;
import java.util.ArrayList;

public class Main extends Application {

    private ArrayList<Market> markets = new ArrayList<>();


    public void start2() {

        //loader here
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("homeScreen.fxml"));

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
