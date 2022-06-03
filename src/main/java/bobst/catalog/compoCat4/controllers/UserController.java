package bobst.catalog.compoCat4.controllers;

import java.lang.reflect.Array;
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

    private static final String ORIGINS = "http://localhost:4200" ;
    
    private final CatUserRepository catUserRepository;

    public UserController(CatUserRepository catUserRepository) {
        this.catUserRepository=catUserRepository;
    }

   // @CrossOrigin(origins = {"http://localhost:4200","http://192.168.99.100:4200/"})
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/auth")
    public AuthResponse getUser(@RequestBody AuthRequest authrequest) {
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
