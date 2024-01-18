package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import entity.Reservation;

public class ReservationModel {
	
	private Connection conn;
	
	public ReservationModel(Connect connect) {
		this.conn = connect.conn;
	}
	
	public void insertReservation(Reservation reservation) {
		try {
			String queryReservation = "INSERT INTO reservation (nbr_personne, date, id_client, id_table) VALUES (?, ?, ?, ?)";
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
