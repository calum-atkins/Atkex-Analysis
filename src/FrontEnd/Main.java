package FrontEnd;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

//
import java.awt.*;

public class Main extends Application {

    //
    Button button;

    //
    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("homeScreen.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
//        primaryStage.setMaximized(true);
        primaryStage.setTitle("Atkex");
        primaryStage.getIcons().add(new Image("AtkexLogo.png"));
        primaryStage.setResizable(true);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
