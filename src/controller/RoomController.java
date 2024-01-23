package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import entity.Client;
import entity.Part;
import entity.Pro;
import entity.Reservation;
import entity.Table;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import service.ClientModel;
import service.ConnectModel;
import service.ReservationModel;
import service.TableModel;
import service.util.DatePickerUtil;

public class RoomController implements Initializable{

    @FXML
    private DatePicker selectedDate;

    @FXML
    private CheckBox tableFourPlace1;

    @FXML
    private CheckBox tableFourPlace2;

    @FXML
    private CheckBox tableEightPlace;

    @FXML
    private CheckBox tableSixPlace;

    @FXML
    private ImageView selectedTable1;

    @FXML
    private ImageView selectedTable2;

    @FXML
    private ImageView selectedTable3;

    @FXML
    private ImageView selectedTable4;

    @FXML
    private ImageView btnValidation;
    
    @FXML
    private Label labelError;
    
    
    private LocalDate dateSelected;
    private int nbrPeople;
    private int idTypeClient;
    
    ConnectModel connectModel;
    TableModel tableModel;
    ClientModel clientModel;
    ReservationModel reservationModel;
    
    Client currentClient = new Client();
    Part currentPart = new Part();
    Pro currentPro = new Pro();
    Reservation reservation = new Reservation();
    
    private static final int idTableFourPlace1 = 1;
    private static final int idTableFourPlace2 = 2;
    private static final int idTableSixPlace = 3;
    private static final int idTableEightPlace = 4;

    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
    	initializeModels();
    	setupTableSelectionListeners();
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
    
    public void setClient(Pro pro, Part part) {
    	if(pro != null) {
    		this.currentPro = pro;
    	}
    	
    	if(part != null) {
    		this.currentPart = part;
    	}
    }
    
    private void setupTableSelectionListeners() {
    	setupTableSelectionListener(tableFourPlace1, selectedTable1);
    	setupTableSelectionListener(tableFourPlace2, selectedTable2);
    	setupTableSelectionListener(tableSixPlace, selectedTable3);
    	setupTableSelectionListener(tableEightPlace, selectedTable4);
    }
    
    private void setupTableSelectionListener(CheckBox checkbox, ImageView imageView) {
    	checkbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
    		if(newValue) {
    			imageView.setVisible(true); 			
    		} else {
    			imageView.setVisible(false);
    		}
    	});
    }
    
    public void setSelectedDate(LocalDate date) {
    	this.dateSelected = date;
    	selectedDate.setValue(this.dateSelected);
    	configureDatePicker();
    }
    
    private void configureDatePicker() {
    	ArrayList<LocalDate> disabledDates = reservationModel.existingReservationDate();
    	ArrayList<Reservation> reservations = reservationModel.checkingNoCompletedDate(disabledDates);
    	int totalTable = tableModel.countTable();
    	
    	DatePickerUtil.disableDatesAndPrevious(selectedDate, disabledDates, reservations, totalTable, true);
    	updateTablesState(reservations);
    }
    
    public void setNbrPeople(int nbr) {
    	this.nbrPeople = nbr;
    	reservation.setNbrPersonne(this.nbrPeople);
    }
    
    @FXML
    void validReservation(MouseEvent event) throws IOException {
    	createClient(this.currentPro, this.currentPart);
    	
    	ArrayList<Integer> choosedTable = tableSelected();  
    	try {
    		int nbrPlaceChoosed = this.tableModel.getNbrPlaceChoosed(choosedTable, this.dateSelected);
    		if(this.reservation.getNbrPersonne() > nbrPlaceChoosed) {
    			this.labelError.setText("Merci de choisir plus de table pour avoir le \n nombre de place suffisant");
    		}
    		else {
    			int idClient = 0;
    	    	if(this.currentPart.getNom() != null) {
    	    		this.clientModel.insertClient(this.currentPart.getTelephone(), this.currentClient.getIdProPart(), this.currentClient.getIsParticulier());
    	    		idClient = this.clientModel.getClientByIdProPart(this.idTypeClient, true);
    	    	}
    	    	
    	    	if(this.currentPro.getNomSociete() != null) {
    	    		this.clientModel.insertClient(this.currentPro.getTelephone(), this.currentClient.getIdProPart(), this.currentClient.getIsParticulier());
    	    		idClient = this.clientModel.getClientByIdProPart(this.idTypeClient, false);
    	    	}
    	    	this.currentClient.setIdClient(idClient);
    	    	
    	    	for(Integer idTable : choosedTable) {		
    	    		Table table = this.tableModel.getIdTable(idTable);
    	    		this.reservation = createReservation(table);
    	    		this.reservationModel.insertReservation(this.reservation);
    	    	}
    	    	
    	    	goToHome(event);
    		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    private Client createClient(Pro pro, Part part) {
    	
    	if(pro.getNomSociete() != null) {
    		this.idTypeClient = this.clientModel.insertProfessionnel(pro);
    		this.currentClient.setIdProPart(this.idTypeClient);
    		this.currentClient.setIsParticulier(false);
    	}
    	
    	if(part.getNom() != null) {
    		this.idTypeClient = this.clientModel.insertParticulier(part);
    		this.currentClient.setIdProPart(this.idTypeClient);
    		this.currentClient.setIsParticulier(true);
    	}    	
    	return this.currentClient;
    }
    
    private Reservation createReservation(Table table) {
    	Reservation reservation = new Reservation();
    	reservation.setIdTable(table);
    	reservation.setIdClient(this.currentClient);
    	reservation.setDate(Date.valueOf(this.dateSelected));
    	reservation.setNbrPersonne(this.nbrPeople);
    	
    	return reservation;
    }
    
    public ArrayList<Integer> tableSelected() {
    	ArrayList<Integer> selectedTables = new ArrayList<>();
    	if(tableFourPlace1.isSelected()) {
    		selectedTables.add(idTableFourPlace1);
    	} 
    	if (tableFourPlace2.isSelected()) {
    		selectedTables.add(idTableFourPlace2);
    	} 
    	if (tableSixPlace.isSelected()) {
    		selectedTables.add(idTableSixPlace);
    	} 
    	if (tableEightPlace.isSelected()) {
    		selectedTables.add(idTableEightPlace);
    	}
    	return selectedTables;
    }
    
    private void updateTablesState(ArrayList<Reservation> reservations) {
    	resetAllCheckBoxes();
    	disableReservedTables(reservations);
    }
    
    private void resetAllCheckBoxes() {
    	tableFourPlace1.setSelected(false);
    	tableFourPlace2.setSelected(false);
    	tableSixPlace.setSelected(false);
    	tableEightPlace.setSelected(false);
    	
    	selectedTable1.setVisible(false);
    	selectedTable2.setVisible(false);
    	selectedTable3.setVisible(false);
    	selectedTable4.setVisible(false);
    }
    
    private void disableReservedTables(ArrayList<Reservation> reservations) {
    	for(Reservation reservation : reservations) {
    		if(reservation.getDate().toLocalDate().equals(dateSelected)) {
    			disableTable(reservation.getIdTable().getIdTable());
    		}
    	}
    }
    
    private void disableTable(int idTable) {
    	switch(idTable) {
    	case idTableFourPlace1:
    		disableCheckBox(tableFourPlace1, selectedTable1);
    		break;
    	case idTableFourPlace2:
    		disableCheckBox(tableFourPlace2, selectedTable2);
    		break;
    	case idTableSixPlace:
    		disableCheckBox(tableSixPlace, selectedTable3);
    		break;
    	case idTableEightPlace:
    		disableCheckBox(tableEightPlace, selectedTable4);
    		break;
    	}
    }
    
    private void disableCheckBox(CheckBox checkBox, ImageView imageView) {
    	checkBox.setSelected(true);
    	checkBox.setDisable(true);
    	imageView.setVisible(true);
    }
    
    @FXML
    void goToHome(MouseEvent event) throws IOException {
        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/home.fxml"));
	        Parent room = loader.load();
	        Scene sceneRoom = new Scene(room);
	        HomeController homeController = loader.getController();

	        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	        stage.setScene(sceneRoom);
	        stage.show();
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }


}
