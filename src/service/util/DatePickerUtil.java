package service.util;

import java.time.LocalDate;
import java.util.ArrayList;

import entity.Reservation;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.util.Callback;

public class DatePickerUtil {

    public static void disableDatesAndPrevious(DatePicker datePicker, final ArrayList<LocalDate> disabledDates, 
    		final ArrayList<Reservation> reservations, int nbTables, boolean enabledDates) {
    	datePicker.setDayCellFactory(new Callback<DatePicker, DateCell>(){
			@Override
			public DateCell call(final DatePicker selectDate) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);
						
						if(item.isBefore(LocalDate.now())) {
							if(enabledDates) {
								setDisable(true);				
							}
						}
						
						long count = reservations.stream()
	                            .filter(reservation -> reservation.getDate().toLocalDate().equals(item))
	                            .map(reservation -> reservation.getIdTable().getIdTable())
	                            .distinct()
	                            .count();
						
						if(count >= nbTables) {
							if(enabledDates) {
								setDisable(true);				
							}
							setStyle("-fx-background-color: #F08080;");
						} else if(count > 0) {
							setStyle("-fx-background-color: #FFA500;");
						}
					}
				};
			}
		});  		
    }  
}
