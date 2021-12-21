package FrontEnd;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class LoadingController implements Initializable {

    @FXML
    private Label labelCurrentProcess;
    @FXML
    private AnchorPane mainAnchorPane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labelCurrentProcess.setText("Worknig");
    }

    public void test(String str) {
        System.out.println(str);
        labelCurrentProcess.setText("str");
    }
}
