package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import entity.Client;
import entity.Part;
import entity.Pro;
import entity.Table;


public class ClientModel {
	
	private Connection conn;
	
	public ClientModel(Connect connect) {
		this.conn = connect.conn;
	}
	
	public int insertParticulier(Part part) {
		int idPart = 0;
		try {
			String queryParticulier = "INSERT INTO particulier (nom, prenom) VALUES (?, ?)";
			PreparedStatement pstParticulier = this.conn.prepareStatement(queryParticulier, Statement.RETURN_GENERATED_KEYS);
			int paramIndex = 0;
			pstParticulier.setString(++paramIndex, part.getNom());
			pstParticulier.setString(++paramIndex, part.getPrenom());
			pstParticulier.executeUpdate();
			ResultSet rs = pstParticulier.getGeneratedKeys();
			if(rs.next()) {
				idPart = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return idPart;
	}
	
	public int insertProfessionnel(Pro pro) {
		int idPro = 0;
		try {
			String queryProfessionel = "INSERT INTO professionel (nom_societe) VALUES (?)";
			PreparedStatement pstProfessionel = this.conn.prepareStatement(queryProfessionel, Statement.RETURN_GENERATED_KEYS);
			int paramIndex = 0;
			pstProfessionel.setString(++paramIndex, pro.getNomSociete());
			pstProfessionel.executeUpdate();
			ResultSet rs = pstProfessionel.getGeneratedKeys();
			if(rs.next()) {
				idPro = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return idPro;
	}
	
	public int insertTypeClient(int idProPart, boolean isParticulier) {
		int idTypeClient = 0;
		try {
			String queryTypeClient = "INSERT INTO type_client (is_particulier, id_pro_part) VALUES (?, ?)";
			PreparedStatement pstTypeClient = this.conn.prepareStatement(queryTypeClient, Statement.RETURN_GENERATED_KEYS);
			int paramIndex = 0;
			pstTypeClient.setBoolean(++paramIndex, isParticulier);
			pstTypeClient.setInt(++paramIndex, idProPart);
			pstTypeClient.executeUpdate();
			ResultSet rs = pstTypeClient.getGeneratedKeys();
			if(rs.next()) {
				idTypeClient = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return idTypeClient;
	}
	
	public void insertClient(String telephone, int idTypeClient) {
		try {
			String queryClient = "INSERT INTO client (telephone, id_type_client) VALUES (?, ?)";
			PreparedStatement pstClient = this.conn.prepareStatement(queryClient);
			int paramIndex = 0;
			pstClient.setString(++paramIndex, telephone);
			pstClient.setInt(++paramIndex, idTypeClient);
			pstClient.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Client getClientByIdTypeClient(int idTypeClient) {
		Client client = new Client();
		
		try {
			String queryClient = "SELECT id_client FROM client WHERE id_type_client = ?";
			PreparedStatement pstClient = this.conn.prepareStatement(queryClient);
			pstClient.setInt(1, idTypeClient);
			ResultSet rs = pstClient.executeQuery();
			if(rs.next()) {
				client.setIdClient(rs.getInt("id_client"));
			}
			rs.close();
			pstClient.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return client;
	}
	
	
	public void deleteDataClient(int idToDelete, String type) {
		deletePartPro(idToDelete, type);
		deleteTypeClient(idToDelete);
		deleteClient(idToDelete);
	}
	
	private void deletePartPro(int idToDelete, String type) {
		try {
	        String tableName = (type.equals("particulier")) ? "particulier" : "professionel";
			String queryReservation = "DELETE FROM " + tableName
									+ " WHERE id_pro_part = "
									+ "(SELECT id_pro_part "
									+ 		"FROM type_client "
									+ 		"WHERE id_type_client = "
									+ 			"(SELECT id_type_client "
									+ 			" FROM client WHERE id_client = "
									+ 				"(SELECT id_client "
									+ 				"FROM reservation "
									+ 				" WHERE id_reservation = ?)));";
				PreparedStatement delPart = this.conn.prepareStatement(queryReservation);
				delPart.setInt(1, idToDelete);
				delPart.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	private void deleteTypeClient(int idToDelete) {
		try {
			String queryReservation = "DELETE FROM type_client"
									+ " WHERE id_type_client = "
									+ 		"(SELECT id_type_client"
									+ 		" FROM client WHERE id_client ="
									+ 			" (SELECT id_client"
									+ 			" FROM reservation"
									+ 			" WHERE id_reservation = ?));";
			PreparedStatement delPart = this.conn.prepareStatement(queryReservation);
			delPart.setInt(1, idToDelete);
			delPart.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	private void deleteClient(int idToDelete) {
		try {
			String queryReservation = "DELETE FROM client"
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

}
