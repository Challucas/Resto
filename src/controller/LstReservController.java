package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import entity.Part;
import entity.Pro;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import service.ClientModel;
import service.ConnectModel;
import service.LstReservModele;
import service.ReservationModel;
import service.TableModel;

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
    ConnectModel model;
    ClientModel clientModel;
    ReservationModel reservationModel;
    TableModel tableModel;
    
    private Integer nbrPeople;
    private LocalDate dateSelected;
    private int idTypeClient;
    
    //TODO Initialize models and lists
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			initializeModels();
			initListPost();
			initListReservValidate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    private void initializeModels() {
		try {
			model = new ConnectModel();			
			clientModel = new ClientModel(model.getConnect());			
			reservationModel = new ReservationModel(model.getConnect());
			tableModel = new TableModel(model.getConnect());
			this.listModel = new LstReservModele(model.getConnect());

		} catch (Exception e) {
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
    
    //TODO action popup
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
                	LstReservController.this.validateReservWeb(selectedItem);
                } else if (buttonType == delateButton) {
                	if( typeReserv == "ReservBDD") {
                		LstReservController.this.reservationModel.deleteReservationBDD(selectedItem);                		
                	}
                	else {
                		LstReservController.this.reservationModel.deleteReservationWeb(selectedItem);                		                		
                	}
                	
                	
                	
                }
            }
        });
    }
    
    //TODO ADD Reservation in BDD
    private void validateReservWeb(String selectedItem) {
	    String[] champs = selectedItem.split("\\s*\\|\\s*");
	    if(champs.length == 5 ) {
	    	insertForProfessionnel(champs[0], champs[1]);
	    	
	    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	        LocalDate dateReserv = LocalDate.parse(champs[3], formatter);
	    	this.dateSelected = dateReserv;
	    	
	        int nbrPer = Integer.parseInt(champs[4].trim());
	    	this.nbrPeople = nbrPer;
	    }
	    else if(champs.length == 6) {
	    	insertForParticulier(champs[0], champs[1], champs[2]);
	    	
	    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	        LocalDate dateReserv = LocalDate.parse(champs[4], formatter);
	    	this.dateSelected = dateReserv;
	    	
	    	int nbrPer = Integer.parseInt(champs[5].trim());
	    	this.nbrPeople = nbrPer;
	    }
	    
	    try {
			goToRoom();
    		this.reservationModel.deleteReservationWeb(selectedItem);                		                		
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    
    private void insertForProfessionnel(String nomSocieteWeb, String telephoneWeb) {	
		String nomSociete = nomSocieteWeb;
		String telephone = telephoneWeb;
		
		Pro pro = new Pro();
		pro.setNomSociete(nomSociete);
		
		int idPro = this.clientModel.insertProfessionnel(pro);
		
		this.idTypeClient = this.clientModel.insertTypeClient(idPro, false);
		
		this.clientModel.insertClient(telephone, idTypeClient);
    }

	private void insertForParticulier(String nom, String prenom, String telephonePart) {		
		String nomParticulier = nom;
		String prenomParticulier = prenom;
		String telephone = telephonePart;
		
		Part part = new Part();
		part.setNom(nomParticulier);
		part.setPrenom(prenomParticulier);
		
		int idPart = this.clientModel.insertParticulier(part);
		
		this.idTypeClient = this.clientModel.insertTypeClient(idPart, true);
		
		this.clientModel.insertClient(telephone, idTypeClient);
	}
	
	@FXML
	void goToRoom() throws IOException {
	    try {
	        FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/room.fxml"));
	        Parent room = loader.load();
	        Scene sceneRoom = new Scene(room);
	        RoomController roomController = loader.getController();
	        roomController.setSelectedDate(this.dateSelected);
	        roomController.currentClient(this.idTypeClient);
	        roomController.setNbrPeople(this.nbrPeople);

	        Stage stage = (Stage) listViewReservWeb.getScene().getWindow();
	        stage.setScene(sceneRoom);
	        stage.show();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	

}