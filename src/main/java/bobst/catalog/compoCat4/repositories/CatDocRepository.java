package bobst.catalog.compoCat4.repositories;

import java.sql.Date;
import java.sql.Timestamp;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import bobst.catalog.compoCat4.models.CatDoc;

public interface CatDocRepository extends CrudRepository<CatDoc, String> {

    @Query ("select dateEclatement from CatDoc where idDoc= ?1")
    public Timestamp findDateByIdoc(String idDoc);
    
}
