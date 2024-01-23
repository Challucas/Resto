package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import entity.*;

public class LstReservModele {
	
    private ArrayList<String> listClient = new ArrayList<>();
    private ArrayList<String> lstReserv = new ArrayList<>();
    private Reservation lstReservClient = new Reservation();
	
    private Connection conn;
    
	public LstReservModele(Connect connect) {
		this.conn = connect.conn;
	}
    
	public ArrayList<String> getListClient() throws IOException {
		File file = new File("src/assets/reservWeb/reserv.txt");
		if (file.exists()) {
			try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
				String lineRead;
				while ((lineRead = reader.readLine()) != null) {
					String[] parts = lineRead.split(";");
					if (parts.length == 7 && parts[1].trim().equals("particulier")) {
						String reservClientPart = parts[2] + " | " + parts[3]  + " | " + parts[4] + " | " + parts[5] + " |" + parts[6] + " | " + parts[0];
						this.listClient.add(reservClientPart);
					}
					else if (parts.length == 6 && parts[1].trim().equals("Professionnel")) {
						String reservClientPro = parts[2] + " | " + parts[3] + " | " + parts[4] +" | " + parts[5] + " | " + parts[0];
						this.listClient.add(reservClientPro);
					}
				}
				return this.listClient;
			}
		}
		return null;
	}
	
	public ArrayList<String> getListReservBDD() throws SQLException {
        ArrayList<String> resultList = new ArrayList<>();
        if (this.conn == null) {
            return resultList;
        }
        
        resultList.addAll(getPartBDD());
        resultList.addAll(getProBDD());
       
        return resultList;
    }
	
	
	
	public ArrayList<String> getPartBDD() throws SQLException {
		ArrayList<String> resultListPart = new ArrayList<>();
		if (this.conn == null) {
			return resultListPart;
		}
		
		String queryGetReserv = "SELECT res.id_reservation idReservation, par.prenom, par.nom, cli.telephone, res.nbr_personne, res.date " +
				"FROM reservation res " +
				"JOIN client cli ON res.id_client = cli.id_client " +
				"JOIN particulier par ON cli.id_pro_part = par.id_pro_part ";
		
		try (PreparedStatement pstReservation = this.conn.prepareStatement(queryGetReserv);
				ResultSet resultSet = pstReservation.executeQuery()) {
			
			while (resultSet.next()) {
				String resultRow = resultSet.getString("prenom") + " | " +
						resultSet.getString("nom") + " | " +
						resultSet.getString("telephone") + " | " +
						resultSet.getInt("nbr_personne") + " | " +
						resultSet.getDate("date") + " | " +
						resultSet.getInt("idReservation");
				
				resultListPart.add(resultRow);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return resultListPart;
	}
	
	public ArrayList<String> getProBDD() throws SQLException {
		ArrayList<String> resultListPro = new ArrayList<>();
		if (this.conn == null) {
			return resultListPro;
		}
		
		String queryGetReserv = "SELECT res.id_reservation idReservation, pro.nom_societe, cli.telephone, res.nbr_personne, res.date " +
								"FROM reservation res " +
								"JOIN client cli ON res.id_client = cli.id_client " +
								"JOIN professionel pro ON cli.id_pro_part = pro.id_pro_part";
				
		
		try (PreparedStatement pstReservation = this.conn.prepareStatement(queryGetReserv);
				ResultSet resultSet = pstReservation.executeQuery()) {
			
			while (resultSet.next()) {
				String resultRow = resultSet.getString("nom_societe") + " | " +
						resultSet.getString("telephone") + " | " +
						resultSet.getInt("nbr_personne") + " | " +
						resultSet.getDate("date") + " | " +
						resultSet.getInt("idReservation");
				
				resultListPro.add(resultRow);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
		return resultListPro;
	}
}