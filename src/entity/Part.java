package entity;

public class Part {
	
	private String nom;
	private String prenom;
	private Client telephone;
	
	
	

	public Part(String nom, String prenom, Client telephone) {
		this.nom = nom;
		this.prenom = prenom;
		this.telephone = telephone;
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


	public Client getTelephone() {
		return telephone;
	}


	public void setTelephone(Client telephone) {
		this.telephone = telephone;
	}
	
	
	
}
