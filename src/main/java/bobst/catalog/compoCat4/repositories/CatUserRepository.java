package bobst.catalog.compoCat4.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import bobst.catalog.compoCat4.models.CatUser;

public interface CatUserRepository extends CrudRepository<CatUser, String> {

    // @Query ("select iduser, password, firstname, lastname, role  from CatUser where iduser = ?1 and password = ?2")
    @Query ("select new bobst.catalog.compoCat4.models.CatUser(iduser, password, firstname, lastname, role)  from CatUser where iduser = ?1 and password = ?2")
    public CatUser getCatUser(String name, String password);

    
}
