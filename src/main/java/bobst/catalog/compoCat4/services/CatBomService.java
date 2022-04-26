package bobst.catalog.compoCat4.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import static java.nio.file.StandardCopyOption.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import bobst.catalog.compoCat4.models.CatBom;
import bobst.catalog.compoCat4.models.CatDoc;
import bobst.catalog.compoCat4.models.CatItem;
import bobst.catalog.compoCat4.models.NodeBom;
import bobst.catalog.compoCat4.repositories.CatBomRepository;
import bobst.catalog.compoCat4.repositories.CatDocRepository;
import bobst.catalog.compoCat4.repositories.CatItemRepository;

@Service
public class CatBomService {

    @Autowired
    private CatBomRepository catBomRepository;

    @Autowired
    private CatItemRepository catItemRepository;

    @Autowired
    private CatDocRepository catDocRepository;
    
    private String[] parents = new String[20];
    private String docName;
    private String rootBom;
    private Date dateEclatement;

    public boolean saveCatBom(CatBom bom) {
        catBomRepository.save(bom);
        return true;
    }

    public boolean saveCatItem(CatItem catItem) {
        catItemRepository.save(catItem);
        return true;
    }

    public List<String> readFile(File file) {
        BufferedReader br = null;
        String line;
        List<String> lines = new ArrayList<>();
        String otp = "";

        //ZNC_BSA03802000066_D87_017_-.txt
        this.docName = file.getName().substring(0, file.getName().indexOf('.'));

        //"UTF8"
        try {
            br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8)); 
            line = br.readLine();
            Integer cptLine = 1;
            while(line!=null){
                lines.add(line);
                
                if (cptLine == 3) { 
                    String[] infoLine = line.split("/");    
                    otp = infoLine[1].substring(4,infoLine[1].length()).trim();
                    this.rootBom = infoLine[2].trim();
                    this.dateEclatement = Date.valueOf(infoLine[3].trim());
                }

                line = br.readLine();
                cptLine++;
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        try{
            br.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }

        //update catDoc table with the new document
        CatDoc catDoc = new CatDoc(this.docName, this.dateEclatement, otp);
        catDocRepository.save(catDoc);

        //update catBom table with the root of the document
        CatBom catBom = new CatBom();
        catBom.setIdDoc(this.docName);
        catBom.setItemParent("M");
        catBom.setItemToc(this.rootBom);
        catBom.setToc(true);
        catBomRepository.save(catBom);

        return lines;

    }

    public File convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        System.out.println(file.getOriginalFilename());
        convFile.createNewFile();
          try(InputStream is = file.getInputStream()) {
            Files.copy(is, convFile.toPath(), REPLACE_EXISTING); 
          }
        return convFile;
      }

    public void uploadBom(File file)   throws IOException {
        List<String> lines = readFile(file);
        for (String line : lines) {
            if (line.contains("|")){
                convertLine(line);
            }                               
        }
        this.synchronizeBom(this.docName, this.dateEclatement);

        //delete file on server 
        if ( file.delete()) {
            System.out.println("File delete successfully");
        } else {
            System.out.println("Failed to delete");
        }
    }

    public void uploadBom(MultipartFile mfile) {
        try {
            File file = convert(mfile);
            uploadBom(file);
        }
        catch(IOException e) {
            
        }
    }

    public void convertLine(String line) {
        CatBom catBom = new CatBom();
        CatItem catItem = new CatItem();
        
        Integer level = 0;
        this.parents[0] = this.rootBom;

        String[] lineSplit = line.split("\\s*\\|\\s*"); // use a regular expression to split and trim result
        try {
            
                level = Integer.parseInt(lineSplit[1]); //exception if not an integer
                this.parents[level] = lineSplit[3];
                
                catBom.setIdDoc(this.docName);
                catBom.setItemParent(this.parents[level-1]);
                catBom.setItemToc(lineSplit[3]);

                String path = "";
                Boolean f = false;
                for (String child : this.parents) {
                    if (!f) {
                        if (child.equals(lineSplit[3])) f=true;
                        if (child != null) path = (path.equals("")) ? child : path + "|" + child ;
                    }
                }
                catBom.setPath(path);
                catBom.setToc(false);

                // don't expose material with RMA/SCD/SCC => Matière / Parts 
                if (!(lineSplit[7].equals("RMA"))) 
                 this.saveCatBom(catBom);

                catItem.setIdItem(catBom.getItemToc());
                catItem.setDescriptionFr(lineSplit[6]);
                this.saveCatItem(catItem);
            
        }
        catch (NumberFormatException e) {
            catBom = null;
        }

    
    }


    public void synchronizeBom(String idDoc, Date dateEclatement) {
        // search all item for an idDoc in catBom
        ArrayList<NodeBom> nodes = catBomRepository.findBomByIdDoc(idDoc, dateEclatement); 
        String path = "";
        for (NodeBom nodeBom : nodes) {
            if (!(nodeBom.getDrawing().isEmpty())) {
                // declare parent in the toc (toc=true)
                path = nodeBom.getPath();
                if (path != null) {
                    while (path.contains("|")) { 
                        CatBom nBom = catBomRepository.findNodeByPath(idDoc,path);
                        if (nBom.getToc()) {
                            path = "";
                        } else {
                            nBom.setToc(true);
                            catBomRepository.save(nBom);
                            path = path.substring(0,path.lastIndexOf("|"));
                        }
                        
                    }

                }
            }
            
        }
        
    }
    
}
