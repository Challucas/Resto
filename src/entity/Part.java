package entity;

public class Part extends Client{

	private Integer idProPart;
    private String nom;
    private String prenom;
	
	public Integer getIdProPart() {
		return idProPart;
	}
	public void setIdProPart(Integer idProPart) {
		this.idProPart = idProPart;
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
}
