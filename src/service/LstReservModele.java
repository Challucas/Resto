package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import entity.*;

public class LstReservModele {
	
    private ArrayList<String> listClient = new ArrayList<>();
	
	public ArrayList<String> getListClient() throws IOException {
		File file = new File("src/assets/reservWeb/reserv.txt");
		if (file.exists()) {
			try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
				String lineRead;
				while ((lineRead = reader.readLine()) != null) {
					String[] parts = lineRead.split(";");
					if (parts.length == 7 && parts[0].trim().equals("particulier")) {
						String reservClientPart = parts[1] + parts[2]  + parts[3] + parts[4] + "per" + " service" + parts[6] + " le" + parts[5];
						this.listClient.add(reservClientPart);
					}
					else if (parts.length == 6 && parts[0].trim().equals("Professionnel")) {
						String reservClientPro = parts[1] + parts[2] + parts[3] + "per" + " service" + parts[5] + " le" + parts[4];
						this.listClient.add(reservClientPro);
					}
				}
				return this.listClient;
			}
		}
		return null;
	}
}