package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import entity.Table;

public class TableModel {
	
	private Connection conn;
	
	public TableModel(Connect connect) {
		this.conn = connect.conn;
	}
	
	public Table getIdTable(int tableId) {
		Table table = new Table();
		
		try {
			String queryTable = "SELECT * FROM tables WHERE id_table = ?";
			PreparedStatement pstTable = this.conn.prepareStatement(queryTable);
			pstTable.setInt(1, tableId);
			
			ResultSet rs = pstTable.executeQuery();
			if(rs.next()) {
				table.setIdTable(rs.getInt("id_table"));
			}
			rs.close();
			pstTable.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return table;
	}
	
	public int countTable() {
		int nbTable = 0;
		
		try {
			String queryTable = "SELECT count(*) FROM tables";
			PreparedStatement pstTable = this.conn.prepareStatement(queryTable);		
			ResultSet rs = pstTable.executeQuery();
			if(rs.next()) {
				nbTable = rs.getInt(1);
			}
			rs.close();
			pstTable.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return nbTable;
	}
	
	public int getNbrPlaceTotalRoom() throws SQLException {
		int nbrPlaceTotal = 0;
		String queryTable = "SELECT SUM(nbr_chaise) AS somme_nbr_chaise FROM tables;";
		PreparedStatement pstTable = this.conn.prepareStatement(queryTable);
		ResultSet rs = pstTable.executeQuery();
		if(rs.next()) {
			nbrPlaceTotal = rs.getInt(1);
		}
		return nbrPlaceTotal;
	}	
	
	public int getNbrPlaceDispoRoom(LocalDate dateSelected) throws SQLException {
		int nbrPlaceDispo = 0;
		String queryTable = "SELECT SUM(t.nbr_chaise) AS somme_nbr_chaise "
				+ " FROM tables t"
				+ " LEFT JOIN reservation r ON t.id_table = r.id_table AND r.date = '" + dateSelected + "'" 
				+ " WHERE r.id_reservation IS NULL;";
		PreparedStatement pstTable = this.conn.prepareStatement(queryTable);
		ResultSet rs = pstTable.executeQuery();
		if(rs.next()) {
			nbrPlaceDispo = rs.getInt(1);
		}
		return nbrPlaceDispo;
	}	


	public int getNbrPlaceChoosed(ArrayList<Integer> choosedTable, LocalDate dateSelected) throws SQLException {
	    int nbrPlaceChoosed = 0;
	    StringBuilder params = new StringBuilder("(");
	    for (int i = 0; i < choosedTable.size(); i++) {
	        params.append("?");
	        if (i < choosedTable.size() - 1) {
	            params.append(",");
	        }
	    }
	    params.append(")");
	    String queryTable = "SELECT SUM(t.nbr_chaise) AS total_chaises" +
	                        " FROM tables t" +
	                        " WHERE t.id_table NOT IN (SELECT id_table FROM reservation WHERE date = '"+ dateSelected +" ')" +
	                        " AND t.id_table IN " + params;

	    try (PreparedStatement pstTable = this.conn.prepareStatement(queryTable)) {
	        for (int i = 0; i < choosedTable.size(); i++) {
	            pstTable.setInt(i + 1, choosedTable.get(i));
	        }
	        ResultSet rs = pstTable.executeQuery();

	        if (rs.next()) {
	            nbrPlaceChoosed = rs.getInt(1);
	        }

	    }

	    return nbrPlaceChoosed;
	}

}
