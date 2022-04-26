package bobst.catalog.compoCat4.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(CatBomCompositeKey.class)
public class CatBom {

      
    @Id 
    private String idDoc;
    @Id 
    private String itemToc;
    @Id 
    private String itemParent;
    private Boolean toc;
    private String path;

    public CatBom() {}    

    public CatBom( String idDoc, String itemToc, String itemParent) {
        this.idDoc = idDoc;
        this.itemToc = itemToc;
        this.itemParent = itemParent;
    }

    

    public CatBom(String idDoc, String itemToc, String itemParent, boolean toc, String path) {
        this.idDoc = idDoc;
        this.itemToc = itemToc;
        this.itemParent = itemParent;
        this.toc = toc;
        this.path = path;
    }

    

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getToc() {
        return toc;
    }

    public void setToc(Boolean toc) {
        this.toc = toc;
    }

    public String getIdDoc() {
        return idDoc;
    }

    public void setIdDoc(String idDoc) {
        this.idDoc = idDoc;
    }

    public String getItemToc() {
        return itemToc;
    }

    public void setItemToc(String itemToc) {
        this.itemToc = itemToc;
    }

    public String getItemParent() {
        return itemParent;
    }

    public void setItemParent(String itemParent) {
        this.itemParent = itemParent;
    }

}
