package bobst.catalog.compoCat4.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CatUser {

    @Id
    private String iduser;
    private String password;
    private String firstname;
    private String lastname;
    private String role;
    
    public CatUser(String iduser, String password, String firstname, String lastname, String role) {
        this.iduser = iduser;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
    }

    public String getIduser() {
        return iduser;
    }

    public void setIduser(String iduser) {
        this.iduser = iduser;
    }

    

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    
}
