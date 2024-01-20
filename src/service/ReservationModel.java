package service;

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
	
	public ReservationModel(Connect connect) {
		this.conn = connect.conn;
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
}
