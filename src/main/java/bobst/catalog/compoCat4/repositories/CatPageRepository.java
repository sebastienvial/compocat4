package bobst.catalog.compoCat4.repositories;

import java.sql.Date;
import java.util.ArrayList;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import bobst.catalog.compoCat4.models.CatPage;

public interface CatPageRepository extends CrudRepository<CatPage, String> {

    @Query ("select idPage from CatPage where idE43 = ?1")
    public ArrayList<String> findAllDrawingsBrother(String idE43);

    @Query ("select p.idPage from CatPage p, CatE43 e where p.idE43 = e.idE43 and e.idItem = ?1 and p.idPage like '%P01' and e.dateF >= ?2 and e.dateD <= ?2")
    public ArrayList<String> findDrawingFromItem(String idItem, Date date);
    
}
