package controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import entity.Client;
import entity.Reservation;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.image.ImageView;
import service.ClientModel;
import service.ConnectModel;
import service.ReservationModel;
import service.TableModel;
import service.util.DatePickerUtil;

public class RoomControllerView implements Initializable{

    @FXML
    private CheckBox tableFourPlace1;

    @FXML
    private CheckBox tableFourPlace2;

    @FXML
    private CheckBox tableEightPlace;

    @FXML
    private CheckBox tableSixPlace;
    
    @FXML
    private DatePicker selectedDate;
    
    @FXML
    private ImageView selectedTable1;

    @FXML
    private ImageView selectedTable2;

    @FXML
    private ImageView selectedTable3;
    
    @FXML
    private ImageView selectedTable4;
    
    
    private LocalDate dateSelected;
    
    ConnectModel connectModel;
    TableModel tableModel;
    ClientModel clientModel;
    ReservationModel reservationModel;
    
    Client currentClient = new Client();
    
    private static final int idTableFourPlace1 = 1;
    private static final int idTableFourPlace2 = 2;
    private static final int idTableSixPlace = 3;
    private static final int idTableEightPlace = 4;

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
    	
    	selectedDate.valueProperty().addListener((observable, oldValue, newValue) ->{
    		dateSelected = newValue;
    		updateTablesState(reservations);
    	});
    	DatePickerUtil.disableDatesAndPrevious(selectedDate, disabledDates, reservations, totalTable, false);
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
    
    public void currentClient(int client) {  	
    	this.currentClient =  this.clientModel.getClientByIdTypeClient(client);
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
    
    private void disableCheckBox(CheckBox checkBox, ImageView imageView) {
    	checkBox.setSelected(true);
    	checkBox.setDisable(true);
    	imageView.setVisible(true);
    }
}
