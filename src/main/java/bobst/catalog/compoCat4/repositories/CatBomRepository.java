package bobst.catalog.compoCat4.repositories;


import java.sql.Date;
import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import bobst.catalog.compoCat4.models.CatBom;
import bobst.catalog.compoCat4.models.NodeBom;
import bobst.catalog.compoCat4.models.NodeBomDynamic;

public interface CatBomRepository extends CrudRepository<CatBom, Long>{
        
    @Query("select new bobst.catalog.compoCat4.models.NodeBom(i.descriptionFr, b.itemToc, b.itemParent, e.idE43, e.dateD, e.dateF, b.path) " + 
           "  from CatBom b JOIN CatItem i ON b.itemToc = i.idItem LEFT JOIN CatE43 e ON b.itemToc = e.idItem where b.idDoc = ?1")
	public ArrayList<NodeBom> findBomByIdDoc(String idDoc, Date dateEclatement);

    @Query("select itemToc from CatBom where idDoc = ?1 and itemParent = ?2")
    public String[] findBomDynamic(String idDoc, String idParent);

    @Query("select new bobst.catalog.compoCat4.models.NodeBomDynamic(b.itemToc, i.descriptionFr, e.idE43, e.dateD, e.dateF) " + 
    "  from CatBom b JOIN CatItem i ON b.itemToc = i.idItem LEFT JOIN CatE43 e ON b.itemToc = e.idItem where b.idDoc = ?1 and b.itemParent = ?2 and b.toc=true")
    public ArrayList<NodeBomDynamic> findBomChildDynamic(String idDoc, String idParent);

    @Query("select new bobst.catalog.compoCat4.models.NodeBom(i.descriptionFr, b.itemToc, b.itemParent, e.idE43, e.dateD, e.dateF, b.path) " + 
           "  from CatBom b JOIN CatItem i ON b.itemToc = i.idItem LEFT JOIN CatE43 e ON b.itemToc = e.idItem where b.idDoc = ?1 and (b.itemToc like %?2% or i.descriptionFr like %?2%)")
    public ArrayList<NodeBom> findNodes(String idDoc, String searchTerm);

    @Query("select new bobst.catalog.compoCat4.models.CatBom(idDoc, itemToc, itemParent, toc, path) from CatBom where idDoc = ?1 and path = ?2")
    public CatBom findNodeByPath(String idDoc, String path);

    

    // @Query("update CatBom")
    // public void updateToc(String item);


    @Query("select path from CatBom where idDoc = ?1 and itemParent = ?2 and itemToc = ?3")
    public ArrayList<String> findPath(String idDoc, String idParent, String idToc);	
    
}
