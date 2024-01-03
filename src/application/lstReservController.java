package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class lstReservController {

    private IndexController indexController;
    
    @FXML
    private AnchorPane anchorMenu;

    @FXML
    private Button buttonHome;

    @FXML
    private Button buttonLstReserv;

    @FXML
    private Button buttonCreateReserv;

    @FXML
    private Button buttonRoom;

    @FXML
    private Button buttonHome1;


    public void goToHome(ActionEvent event) {
    	indexController = new IndexController();
    	System.out.println("ttttttttttttttttttttttt");
        try {
			indexController.goToHome(event);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
}