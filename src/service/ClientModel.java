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
	
	public void insertClient(String telephone, int idProPart, boolean isParticulier) {
		try {
			String queryClient = "INSERT INTO client (telephone, id_pro_part, is_particulier) VALUES (?, ?, ?)";
			PreparedStatement pstClient = this.conn.prepareStatement(queryClient);
			int paramIndex = 0;
			pstClient.setString(++paramIndex, telephone);
			pstClient.setInt(++paramIndex, idProPart);
			pstClient.setBoolean(++paramIndex, isParticulier);
			pstClient.executeUpdate();
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getClientByIdProPart(int idProPart) {
		int idClient = 0;
		try {
			String queryClient = "SELECT id_client FROM client WHERE id_pro_part = ?";
			PreparedStatement pstClient = this.conn.prepareStatement(queryClient);
			pstClient.setInt(1, idProPart);
			ResultSet rs = pstClient.executeQuery();
			if(rs.next()) {
				idClient = rs.getInt("id_client");
			}
			rs.close();
			pstClient.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return idClient;
	}
	
	
	public void deleteDataClient(int idToDelete, String type) {
		deletePartPro(idToDelete, type);
//		deleteTypeClient(idToDelete);
		deleteClient(idToDelete);
	}
	
	private void deletePartPro(int idToDelete, String type) {
		try {
	        String tableName = (type.equals("particulier")) ? "particulier" : "professionel";
			String queryReservation = "DELETE FROM " + tableName
									+ " WHERE id_pro_part = "
									+	"(SELECT id_pro_part "
						            + 		" FROM client WHERE id_client = "
							 		+		 "(SELECT id_client "
									+ 		  "FROM reservation "
									+ 		   " WHERE id_reservation = ?));";
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
