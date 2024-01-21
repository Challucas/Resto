package service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import entity.Reservation;
import entity.Table;

public class ReservationModel {
	
	private Connection conn;
    ClientModel clientModel;
    ConnectModel model;

	
	public ReservationModel(Connect connect) {
		this.conn = connect.conn;
		initializeModels();
	}
	
	private void initializeModels() {
		try {			
			model = new ConnectModel();	
			clientModel = new ClientModel(model.getConnect());			

		} catch (Exception e) {
			e.printStackTrace();
		}
    }
	
	public void insertReservation(Reservation reservation) {
		if(!isTableReservedOnDate(reservation.getIdTable().getIdTable(), reservation.getDate())) {			
			try {
				String queryReservation = "INSERT INTO reservation (nbr_personne, date, id_client, id_table) "
						+ " VALUES (?, ?, ?, ?)";
				PreparedStatement pstReservation = this.conn.prepareStatement(queryReservation);
				int paramIndex = 0;
				pstReservation.setInt(++paramIndex, reservation.getNbrPersonne());
				pstReservation.setDate(++paramIndex, reservation.getDate());
				pstReservation.setInt(++paramIndex, reservation.getIdClient().getIdClient());
				pstReservation.setInt(++paramIndex, reservation.getIdTable().getIdTable());
				pstReservation.executeUpdate();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean isTableReservedOnDate(int tableId, Date date){
		try {
			String queryReservation = "SELECT count(*) FROM reservation"
					+ " WHERE id_table = ?"
					+ " AND date = ?";
			PreparedStatement pstReservation = this.conn.prepareStatement(queryReservation);
			int paramIndex = 0;
			pstReservation.setInt(++paramIndex, tableId);
			pstReservation.setDate(++paramIndex, date);
			ResultSet rs = pstReservation.executeQuery();
			if(rs.next()) {
				return rs.getInt(1) > 0;
			}
			rs.close();
			pstReservation.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	
	public ArrayList<LocalDate> existingReservationDate() {
		ArrayList<LocalDate> existingDates = new ArrayList<>();
		try {
			String queryReservation = "SELECT date FROM reservation";
			PreparedStatement pstReservation = this.conn.prepareStatement(queryReservation);
			ResultSet rs = pstReservation.executeQuery();
			while(rs.next()) {
				existingDates.add(rs.getDate("date").toLocalDate());
			}
			rs.close();
			pstReservation.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return existingDates;
	}
	
	public ArrayList<Reservation> checkingNoCompletedDate(ArrayList<LocalDate> dates){
		ArrayList<Reservation> datesTable = new ArrayList<>();
		for(LocalDate date : dates) {				
			try {
				String queryReservation = "SELECT date, r.id_table FROM reservation r"
						+ " JOIN tables t"
						+ " ON r.id_table = t.id_table"
						+ " WHERE date = ? ";
				PreparedStatement pstReservation = this.conn.prepareStatement(queryReservation);
				int paramIndex = 0;
				pstReservation.setDate(++paramIndex, java.sql.Date.valueOf(date));
				ResultSet rs = pstReservation.executeQuery();
				while(rs.next()) {
					Reservation reservation = new Reservation();
					Table table = new Table();
					table.setIdTable(rs.getInt("id_table"));
					
					reservation.setDate(rs.getDate("date"));
					reservation.setIdTable(table);
					datesTable.add(reservation);
				}
				rs.close();
				pstReservation.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return datesTable;
	}
	
	
	public void deleteReservationBDD(String selectedItem) {
		String[] champs = selectedItem.split("\\s*\\|\\s*");	
		
		String type = "";
		int idToDelete = Integer.parseInt(champs[champs.length - 1].trim());
		if(champs.length == 6) {
			type = "particulier";
		}
		else if (champs.length == 5 ){
			type = "professionnel";
		}
		this.clientModel.deleteDataClient(idToDelete, type);
		deleteReservation(idToDelete);
		
		
	}
	
	private void deleteReservation(int idToDelete) {
		try {
			String queryReservation = "DELETE FROM reservation"
					+ " WHERE id_client = "
					+ 		"(SELECT id_client"
					+ 		" FROM reservation"
					+ 		" WHERE id_reservation = ?);";
			PreparedStatement delPart = this.conn.prepareStatement(queryReservation);
			delPart.setInt(1, idToDelete);
			delPart.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	
	

	public void deleteReservationWeb(String selectedItem) {
	    String[] champs = selectedItem.split("\\s*\\|\\s*");

	    if (champs.length == 6 || champs.length == 5) {
	        int idToDelete = Integer.parseInt(champs[champs.length - 1].trim());

	        File file = new File("src/assets/reservWeb/reserv.txt");
	        File tempFile = new File("src/assets/reservWeb/reserv_temp.txt");

	        try (BufferedReader reader = new BufferedReader(new FileReader(file));
	             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

	            String lineRead;

	            while ((lineRead = reader.readLine()) != null) {
	                String[] parts = lineRead.split(" \\; ");

	                if (parts.length >= 1) {
	                    int id = Integer.parseInt(parts[0].trim());
	                    if (id != idToDelete) {
	                        writer.write(lineRead);
	                        writer.newLine();
	                    }
	                }
	            }

	        } catch (IOException e) {
	            e.printStackTrace();
	        }

	        if (file.delete()) {
	            if (tempFile.renameTo(file)) {
	            } 
	        }

	    }	
	}
}
