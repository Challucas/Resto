package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
    
    ConnectModel modele;
    ClientModel clientModel;
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			modele = new ConnectModel();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}   
    
    public void setSelectedClient(String clientType) {
    	selectedClient = clientType;
    }

    @FXML
    void validateForm(MouseEvent event) {
        if ("particulier".equals(selectedClient)) {
        	forParticulier();
        } else if ("professionnel".equals(selectedClient)) {
        	forProfessionnel();       
        }
    }

	private void forProfessionnel() {
		this.clientModel = new ClientModel(modele.getConnect());
		String nomSociete = inputSociete.getText();
		Pro pro = new Pro();
		pro.setNomSociete(nomSociete);
		int idPro = this.clientModel.insertProfessionnel(pro);
		int idTypeClient = this.clientModel.insertTypeClient(idPro, false);
		String telephone = inputTel.getText();
		this.clientModel.insertClient(telephone, idTypeClient);
    }

	private void forParticulier() {
		this.clientModel = new ClientModel(modele.getConnect());
		String nomParticulier = inputNom.getText();
		String prenomParticulier = inputPrenom.getText();
		Part part = new Part();
		part.setNom(nomParticulier);
		part.setPrenom(prenomParticulier);
		int idPart = this.clientModel.insertParticulier(part);
		int idTypeClient = this.clientModel.insertTypeClient(idPart, true);
		String telephone = inputTel.getText();
		this.clientModel.insertClient(telephone, idTypeClient);
	}
}
