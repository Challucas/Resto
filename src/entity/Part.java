package entity;

public class Part {
	
	private String nom;
	private String prenom;
	private Client idClient;
	
	
	

	public Part(String nom, String prenom, Client idClient) {
		this.nom = nom;
		this.prenom = prenom;
		this.idClient = idClient;
	}


	public String getNom() {
		return nom;
	}


	public void setNom(String nom) {
		this.nom = nom;
	}


	public String getPrenom() {
		return prenom;
	}


	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}


	public Client getIdClient() {
		return idClient;
	}


	public void setIdClient(Client idClient) {
		this.idClient = idClient;
	}
	
	
	
}
