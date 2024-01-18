package controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import service.ClientModel;
import service.ConnectModel;
import entity.Part;
import entity.Pro;

public class CreateReservFormController implements Initializable{

    @FXML
    private ImageView wallpaper;

    @FXML
    private DatePicker selectDate;

    @FXML
    private TextField inputNom;

    @FXML
    private TextField inputPrenom;

    @FXML
    private TextField inputTel;

    @FXML
    private TextField inputNbr;
    
    @FXML
    private TextField inputSociete;
    
    @FXML
    private ImageView btnValidate;
    
    private String selectedClient;
    private Integer nbrPeople;
    private LocalDate dateSelected;
    private int idTypeClient;
    
    ConnectModel modele;
    ClientModel clientModel;
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			modele = new ConnectModel();
			this.clientModel = new ClientModel(modele.getConnect());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}   
    
    public void setSelectedClient(String clientType) {
    	selectedClient = clientType;
    }

    @FXML
    void validateForm(MouseEvent event) throws IOException {
        if ("particulier".equals(selectedClient)) {
        	forParticulier();
        } else if ("professionnel".equals(selectedClient)) {
        	forProfessionnel();    
        }
        this.dateSelected = selectDate.getValue();
        this.nbrPeople = Integer.parseInt(inputNbr.getText());
        goToRoom(event);
    }

	private void forProfessionnel() {	
		String nomSociete = inputSociete.getText();
		String telephone = inputTel.getText();
		
		Pro pro = new Pro();
		pro.setNomSociete(nomSociete);
		
		int idPro = this.clientModel.insertProfessionnel(pro);
		
		this.idTypeClient = this.clientModel.insertTypeClient(idPro, false);
		
		this.clientModel.insertClient(telephone, idTypeClient);
    }

	private void forParticulier() {		
		String nomParticulier = inputNom.getText();
		String prenomParticulier = inputPrenom.getText();
		String telephone = inputTel.getText();
		
		Part part = new Part();
		part.setNom(nomParticulier);
		part.setPrenom(prenomParticulier);
		
		int idPart = this.clientModel.insertParticulier(part);
		
		this.idTypeClient = this.clientModel.insertTypeClient(idPart, true);
		
		this.clientModel.insertClient(telephone, idTypeClient);
	}
	
    @FXML
    void goToRoom(MouseEvent event) throws IOException {
        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/room.fxml"));
	        Parent room = loader.load();
	        Scene sceneRoom = new Scene(room);
	        RoomController roomController = loader.getController();
	        roomController.setSelectedDate(this.dateSelected);
	        roomController.currentClient(this.idTypeClient);
	        roomController.setNbrPeople(this.nbrPeople);

	        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	        stage.setScene(sceneRoom);
	        stage.show();
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }
}
