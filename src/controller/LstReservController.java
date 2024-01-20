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
import service.ConnectModel;
import service.LstReservModele;
import service.ReservationModel;

public class LstReservController implements Initializable{

	@FXML
    private AnchorPane anchorMenu;

    @FXML
    private ListView listViewReservWeb;
    
    @FXML
    private ListView listViewReservBDD;

    @FXML
    private ImageView wallpaper;
    
    LstReservModele listModel;
    ReservationModel reservationModel;
    ConnectModel modele;

    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			modele = new ConnectModel();
			this.listModel = new LstReservModele(modele.getConnect());
			this.reservationModel = new ReservationModel(modele.getConnect());
			initListPost();
			initListReservValidate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    public void initListPost() throws SQLException {
    	ArrayList<String> lstClient;
		try {
			lstClient = this.listModel.getListClient();
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
	                	displayPopup(selectedItem, "ReservWeb");
	                }
	            }
	        });
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void initListReservValidate() throws SQLException, IOException {
    	ArrayList<String> lstReserv;
		lstReserv = this.listModel.getListReservBDD();
		this.listViewReservBDD.getItems().clear();
		for (int i = 0; i < lstReserv.size(); i++) {
			String post = lstReserv.get(i);
			if (post != null) {
				this.listViewReservBDD.getItems().add(post);				}
		}
		
		this.listViewReservBDD.setOnMouseClicked(new EventHandler<MouseEvent>() {
		    @Override
		    public void handle(MouseEvent event) {
		        String selectedItem = (String) listViewReservBDD.getSelectionModel().getSelectedItem();
		        if (selectedItem != null) {
		        	displayPopup(selectedItem, "ReservBDD");
		        }
		    }
		});
    }
    
    private void displayPopup(final String selectedItem, final String typeReserv) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Détails de la réservation");
        alert.setHeaderText(null);
        alert.setContentText(selectedItem);
        
        final ButtonType validateButton = new ButtonType("Valider");
        final ButtonType delateButton = new ButtonType("Supprimer");
        final ButtonType cancelButton = new ButtonType("Annuler");
        
        if(typeReserv == "ReservWeb") {
        	alert.getButtonTypes().setAll(validateButton, delateButton, cancelButton);        	
        }
        else {
        	alert.getButtonTypes().setAll(delateButton, cancelButton);        	        	
        }

        alert.showAndWait().ifPresent(new Consumer<ButtonType>() {

			@Override
            public void accept(ButtonType buttonType) {
                if (buttonType == validateButton) {
                } else if (buttonType == delateButton) {
                	if( typeReserv == "ReservBDD") {
                		LstReservController.this.reservationModel.deleteReservationBDD();                		
                	}
                	else {
                		LstReservController.this.reservationModel.deleteReservationWeb(selectedItem);                		                		
                	}
                	
                	
                	
                }
            }
        });
    }

}