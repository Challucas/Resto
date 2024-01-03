package application;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class IndexController {

    @FXML
    private AnchorPane anchorMenu;

    @FXML
    private Button buttonCreateReserv;

    @FXML
    private Button buttonHome;

    @FXML
    private Button buttonLstReserv;

    @FXML
    private Button buttonRoom;

    public void goToHome(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home.fxml"));
        Parent home = loader.load();
        Scene sceneHome = new Scene(home);
        HomeController homeController = loader.getController();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(sceneHome);
        stage.show();
    }
}
