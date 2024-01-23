package entity;

public class Pro extends Client{

	private Integer idProPart;
    private String nomSociete;
    
    public Integer getIdProPart() {
        return idProPart;
    }
    public void setIdProPart(Integer idProPart) {
        this.idProPart = idProPart;
    }

    public String getNomSociete() {
        return nomSociete;
    }
    public void setNomSociete(String nomSociete) {
        this.nomSociete = nomSociete;
    }
}
