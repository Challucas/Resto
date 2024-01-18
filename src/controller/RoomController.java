package controller;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;

import entity.Client;
import entity.Reservation;
import entity.Table;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import service.ClientModel;
import service.ConnectModel;
import service.ReservationModel;
import service.TableModel;

public class RoomController implements Initializable{

    @FXML
    private RadioButton tableFourPlace1;

    @FXML
    private RadioButton tableFourPlace2;

    @FXML
    private RadioButton tableSixPlace1;

    @FXML
    private RadioButton tableEightPlace1;
    
    @FXML
    private ToggleGroup radioGroup;
    
    @FXML
    private DatePicker selectedDate;
    
    @FXML
    private ImageView btnValidation;
    
    
    private LocalDate dateSelected;
    private int currentClientId;
    private int nbrPeople;
    
    ConnectModel modele;
    TableModel tableModel;
    ClientModel clientModel;
    ReservationModel reservationModel;
    
    Client currentClient = new Client();
    
    
    public void setSelectedDate(LocalDate date) {
    	this.dateSelected = date;
    	selectedDate.setValue(this.dateSelected);
    }
    
    public void currentClient(int client) {  	
    	this.currentClient =  this.clientModel.getClientByIdTypeClient(client);
    }
    
    public void setNbrPeople(int nbr) {
    	this.nbrPeople = nbr;
    }
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			modele = new ConnectModel();
			this.tableModel = new TableModel(modele.getConnect());
			this.clientModel = new ClientModel(modele.getConnect());
			this.reservationModel = new ReservationModel(modele.getConnect());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}   
    
    @FXML
    void validReservation(MouseEvent event) {
    	int choosedTable = tableSelected();
    	
    	Table table = this.tableModel.getIdTable(choosedTable);
    	
    	Reservation reservation = new Reservation();
    	reservation.setIdTable(table);
    	reservation.setIdClient(currentClient);
    	Date sqlDate = Date.valueOf(this.dateSelected);
    	reservation.setDate(sqlDate);
    	reservation.setNbrPersonne(this.nbrPeople);
    	this.reservationModel.insertReservation(reservation);
    	
    }
    
    public int tableSelected() {
    	int selectedTable = 0;
    	if(tableFourPlace1.isSelected()) {
    		selectedTable = 1;
    	} else if (tableFourPlace2.isSelected()) {
    		selectedTable = 2;
    	} else if (tableSixPlace1.isSelected()) {
    		selectedTable = 3;
    	} else if (tableEightPlace1.isSelected()) {
    		selectedTable = 4;
    	}
    	return selectedTable;
    }


}
