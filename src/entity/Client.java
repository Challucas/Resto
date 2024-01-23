package entity;

public class Client {

	private Integer idClient;
    private String telephone;
    private Boolean isParticulier;
    private Integer idProPart;

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Integer getIdClient() {
        return idClient;
    }

    public void setIdClient(Integer idClient) {
        this.idClient = idClient;
    }
    
    public boolean getIsParticulier() {
        return isParticulier;
    }

    public void setIsParticulier(Boolean isParticulier) {
        this.isParticulier = isParticulier;
    }
    
    public Integer getIdProPart() {
    	return idProPart;
    }
    
    public void setIdProPart(Integer idProPart) {
        this.idProPart = idProPart;
    }
}