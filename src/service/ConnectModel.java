package service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ConnectModel {
	
	Connect conn;
	
	public ConnectModel() {
		try {
			conn = new Connect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Connect getConnect() {
        return conn;
    }

}
