package bobst.catalog.compoCat4.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import bobst.catalog.compoCat4.models.AuthRequest;
import bobst.catalog.compoCat4.models.AuthResponse;
import bobst.catalog.compoCat4.models.CatUser;
import bobst.catalog.compoCat4.repositories.CatUserRepository;

@RestController
public class UserController {
    @Value ("${origins.path}")  //origins.path=http://localhost:4200
    private static final String ORIGINS = "http://localhost:4200" ;

    private final CatUserRepository catUserRepository;

    public UserController(CatUserRepository catUserRepository) {
        this.catUserRepository=catUserRepository;
    }

    @CrossOrigin(origins = ORIGINS)
    @PostMapping("/auth")
    public AuthResponse getUser(@RequestBody AuthRequest authrequest) {
        //System.out.println("Debut authentification" + authrequest.getName());
        AuthResponse res = new AuthResponse("", null);
        //search user in DB
        CatUser user = catUserRepository.getCatUser(authrequest.getName(), authrequest.getPassword()); 
        if ( user != null)  {
            //user authenticated
            res = new AuthResponse("1234", new CatUser(user.getIduser(), "", user.getFirstname(), user.getLastname(), user.getRole()));                
        }

        return res;
    }
    
}
