package entity;

public class Pro {

	private String nomSociete;
	private Client idClient;
	
	public Pro(String nomSociete, Client idClient) {
		this.nomSociete = nomSociete;
		this.idClient = idClient;
	}

	public String getNomSociete() {
		return nomSociete;
	}

	public void setNomSociete(String nomSociete) {
		this.nomSociete = nomSociete;
	}

	public Client getIdClient() {
		return idClient;
	}

	public void setIdClient(Client idClient) {
		this.idClient = idClient;
	}
	
	
	
}
