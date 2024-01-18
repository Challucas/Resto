package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
}
