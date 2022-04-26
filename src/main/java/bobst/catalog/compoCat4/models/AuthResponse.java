package bobst.catalog.compoCat4.models;

public class AuthResponse {

    private String token;
    private CatUser user;
    
    public AuthResponse(String token, CatUser user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public CatUser getUser() {
        return user;
    }

    public void setUser(CatUser user) {
        this.user = user;
    }
   
    
}
