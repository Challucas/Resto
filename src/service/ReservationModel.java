package service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.StringTokenizer;

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
	
	
	public void deleteReservationBDD() {
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

	                    // Écrivez la ligne dans le fichier temporaire uniquement si l'ID ne correspond pas à celui à supprimer
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
