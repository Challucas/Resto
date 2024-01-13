package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import service.LstReservModele;

public class LstReservController implements Initializable{

	@FXML
    private AnchorPane anchorMenu;

    @FXML
    private ListView listViewReservWeb;

    @FXML
    private ImageView wallpaper;
    
    LstReservModele modele;
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			modele = new LstReservModele();
			initListPost();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    public void initListPost() throws SQLException {
    	ArrayList<String> lstClient;
		try {
			lstClient = this.modele.getListClient();
			this.listViewReservWeb.getItems().clear();
			for (int i = 0; i < lstClient.size(); i++) {
				String post = lstClient.get(i);
				if (post != null) {
					this.listViewReservWeb.getItems().add(post);				}
			}
			
			this.listViewReservWeb.setOnMouseClicked(new EventHandler<MouseEvent>() {
	            @Override
	            public void handle(MouseEvent event) {
	                String selectedItem = (String) listViewReservWeb.getSelectionModel().getSelectedItem();
	                if (selectedItem != null) {
	                	displayPopup(selectedItem);
	                }
	            }
	        });
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void displayPopup(String selectedItem) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Détails de la réservation");
        alert.setHeaderText(null);
        alert.setContentText(selectedItem);

        final ButtonType validerButton = new ButtonType("Valider");
        final ButtonType refuserButton = new ButtonType("Refuser");

        alert.getButtonTypes().setAll(validerButton, refuserButton);

        alert.showAndWait().ifPresent(new Consumer<ButtonType>() {
            @Override
            public void accept(ButtonType buttonType) {
                if (buttonType == validerButton) {
                    
                } else if (buttonType == refuserButton) {
                    
                }
            }
        });
    }

}
