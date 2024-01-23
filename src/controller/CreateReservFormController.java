package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import service.ClientModel;
import service.ConnectModel;
import service.ReservationModel;
import service.TableModel;
import service.util.DatePickerUtil;
import entity.Part;
import entity.Pro;
import entity.Reservation;

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
    
    @FXML
    private Label labelError;
    
    private String selectedClient;
    private Integer nbrPeople;
    private LocalDate dateSelected;
    
    private static final String clientTypeProfessional = "professionnel";
    private static final String clientTypeParticular = "particulier";
    
    Part part = new Part();
    Pro pro = new Pro();
    
    ConnectModel connectModel;
    ClientModel clientModel;
    ReservationModel reservationModel;
    TableModel tableModel;
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	initializeModels();
    	configureDatePicker();
	}  
    
    private void initializeModels() {
		try {
			connectModel = new ConnectModel();			
			clientModel = new ClientModel(connectModel.getConnect());			
			reservationModel = new ReservationModel(connectModel.getConnect());
			tableModel = new TableModel(connectModel.getConnect());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private void configureDatePicker() {
    	ArrayList<LocalDate> disabledDates = reservationModel.existingReservationDate();
		ArrayList<Reservation> reservations = reservationModel.checkingNoCompletedDate(disabledDates);
		int totalTable = tableModel.countTable();
		DatePickerUtil.disableDatesAndPrevious(selectDate, disabledDates, reservations, totalTable, true);
    }
    
    public void setSelectedClient(String clientType) {
    	this.selectedClient = clientType;
    }

    @FXML
    void validateForm(MouseEvent event) throws IOException {      
    	String inputNbrValue = inputNbr.getText();
        try {
            this.nbrPeople = Integer.parseInt(inputNbrValue);
            this.dateSelected = selectDate.getValue();

            if (!validateReservationCapacity()) {
                return;
            }
            
            if(!validateCalendar()) {
            	return;
            }
            
            if (clientTypeParticular.equals(selectedClient)) {
                if(!insertForParticulier()) {
                	return;
                }
            } else if (clientTypeProfessional.equals(selectedClient)) {
                if(!insertForProfessionnel()) {
                	return;
                }
            }
            
            goToRoom(event);
        } catch (NumberFormatException e) {
            this.labelError.setText("Veuillez entrer un nombre valide");
        }
    }
    
    private boolean validateReservationCapacity() {
        String inputNbrValue = inputNbr.getText();
        if (inputNbrValue == null || inputNbrValue.trim().isEmpty()) {
            this.labelError.setText("Merci de renseigner le nombre de personnes");
            return false;
        }
        int nbrPlaceDispo = getNbrPlaceDispoRoom(this.dateSelected);
        if (this.nbrPeople > getNbrPlaceTotalRoom()) {
            this.labelError.setText("Le nombre maximum de places dans le restaurant est de 22");
            return false;
        } else if (this.nbrPeople > nbrPlaceDispo) {
            this.labelError.setText("Pour cette date, le nombre de places disponibles dans le \nrestaurant est de " + nbrPlaceDispo);
            return false;
        } else if (this.nbrPeople <= 0) {
            this.labelError.setText("Merci de mettre au moins une personne");
            return false;
        }
        return true;
    }
    
    private boolean validateCalendar() {
    	LocalDate date = selectDate.getValue();
        if (date == null) {
            this.labelError.setText("Merci de renseigner une date");
            return false;
        }
        return true;
    }

    
    public int getNbrPlaceTotalRoom() {
    	try {
			return tableModel.getNbrPlaceTotalRoom();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return 0;
    }
    
    public int getNbrPlaceDispoRoom(LocalDate dateSelected) {
    	try {
    		return this.tableModel.getNbrPlaceDispoRoom(dateSelected);
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    	return 0;
    }

	private boolean insertForProfessionnel() {	
		String nomSociete = inputSociete.getText();
		String telephone = inputTel.getText();
		
		if (nomSociete == null || nomSociete.trim().isEmpty()) {
	        this.labelError.setText("Merci de renseigner un nom de société");
	        return false;
	    }
		
		if (telephone == null || telephone.trim().isEmpty()) {
			this.labelError.setText("Merci de renseigner un numéro de téléphone");
			return false;
		}
		
		this.pro.setNomSociete(nomSociete);
		this.pro.setTelephone(telephone);
		this.pro.setIsParticulier(false);
		
		return true;
    }

	private boolean insertForParticulier() {		
		String nomParticulier = inputNom.getText();
		String prenomParticulier = inputPrenom.getText();
		String telephone = inputTel.getText();
			
		if (nomParticulier == null || nomParticulier.trim().isEmpty()) {
	        this.labelError.setText("Merci de renseigner un nom");
	        return false;
	    }
		
		if (prenomParticulier == null || prenomParticulier.trim().isEmpty()) {
			this.labelError.setText("Merci de renseigner un prénom");
			return false;
		}
		
		if (telephone == null || telephone.trim().isEmpty()) {
			this.labelError.setText("Merci de renseigner un numéro de téléphone");
			return false;
		}
		
		this.part.setNom(nomParticulier);
		this.part.setPrenom(prenomParticulier);
		this.part.setTelephone(telephone);
		this.part.setIsParticulier(true);
		
		return true;
	}
	
    @FXML
    void goToRoom(MouseEvent event) throws IOException {
        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/room.fxml"));
	        Parent room = loader.load();
	        Scene sceneRoom = new Scene(room);
	        RoomController roomController = loader.getController();
	        roomController.setSelectedDate(this.dateSelected);
	        roomController.setNbrPeople(this.nbrPeople);
	        roomController.setClient(this.pro, this.part);

	        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	        stage.setScene(sceneRoom);
	        stage.show();
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }
}
