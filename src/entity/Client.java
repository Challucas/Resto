package entity;

public class Client {

    private String telephone;
    private Integer idClient;
    
    public Client(String telephone, Integer idClient) {
        this.telephone = telephone;
        this.idClient = idClient;
    }

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
}