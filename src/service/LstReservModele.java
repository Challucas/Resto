package service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LstReservModele {

	public String loadNoteForImage() throws IOException {
        File file = new File("src/historyNote.txt");

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String lineRead;
                while ((lineRead = reader.readLine()) != null) {
                    String[] parts = lineRead.split(";");
                    if (parts.length == 3 && parts[0].trim().equals("particulier")) {
                        return parts[2].trim();
                    }
                }
            }
        }
		return null;
    }
	
}
