package service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class Connect {
	
	Connection conn;
	
	public Connect() throws Exception {
		this.conn = getConnection();
	}
	
	
	public static Connection getConnection() throws Exception {
		try {
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://127.0.0.1:3306/exam_11_m9d5y";
			String username = "root";
			String password = "";
			Class.forName(driver);
			Connection conn = DriverManager.getConnection(url, username, password);
			return conn;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
}
