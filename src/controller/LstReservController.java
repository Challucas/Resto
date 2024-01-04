package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import service.listReservModele;

public class LstReservController implements Initializable{

	@FXML
    private AnchorPane anchorMenu;

    @FXML
    private ListView listViewReservWeb;

    @FXML
    private ImageView wallpaper;
    
    listReservModele modele;
    
    @Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			modele = new listReservModele();
			initListPost();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
    
    public void initListPost() throws SQLException {
    	ArrayList<String> lstClient;
		try {
			lstClient = this.modele.getListClient();
			this.listViewReservWeb.getItems().clear();
			for (int i = 0; i < lstClient.size(); i++) {
				String post = lstClient.get(i);
				if (post != null) {
					this.listViewReservWeb.getItems().add(post);				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

}
