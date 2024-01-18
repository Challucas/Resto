package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class NavMenuController {

    @FXML
    private AnchorPane anchorMenu;
    
    @FXML
    private ImageView buttonHome;

    @FXML
    private ImageView buttonLstReserv;

    @FXML
    private ImageView buttonCreateReserv;

    @FXML
    private ImageView buttonRoom;

    @FXML
    void goToCreateReserv(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/createReserv.fxml"));
        Parent createReserv = loader.load();
        Scene sceneCreateReserv = new Scene(createReserv);
        CreateReservController createReservController = loader.getController();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(sceneCreateReserv);
        stage.show();
    }

    @FXML
    void goToHome(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/home.fxml"));
        Parent home = loader.load();
        Scene sceneHome = new Scene(home);
        HomeController homeController = loader.getController();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(sceneHome);
        stage.show();
    }

    @FXML
    void goToRoom(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/roomForView.fxml"));
        Parent room = loader.load();
        Scene sceneRoom = new Scene(room);
        RoomController roomController = loader.getController();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(sceneRoom);
        stage.show();
    }

    @FXML
    void goTolstReserv(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/lstReserv.fxml"));
        Parent lstReserv = loader.load();
        Scene sceneLstReserv = new Scene(lstReserv);
        LstReservController lstReservController = loader.getController();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(sceneLstReserv);
        stage.show();
    }
}
