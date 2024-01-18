package entity;

import java.sql.Date;
import java.time.LocalDate;

public class Reservation {

	private Integer idReservation;
	private Integer nbrPersonne;
	private Date date;
	private Client idClient;
	private Table idTable;
	
	public Integer getIdReservation() {
		return idReservation;
	}
	public void setIdReservation(Integer idReservation) {
		this.idReservation = idReservation;
	}
	
	public Integer getNbrPersonne() {
		return nbrPersonne;
	}
	public void setNbrPersonne(Integer nbrPersonne) {
		this.nbrPersonne = nbrPersonne;
	}
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date sqlDate) {
		this.date = sqlDate;
	}
	
	public Client getIdClient() {
		return idClient;
	}
	public void setIdClient(Client idClient) {
		this.idClient = idClient;
	}
	
	public Table getIdTable() {
		return idTable;
	}
	public void setIdTable(Table idTable) {
		this.idTable = idTable;
	}
	
}
