package bobst.catalog.compoCat4.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import bobst.catalog.compoCat4.models.CatE43;
import bobst.catalog.compoCat4.repositories.CatE43Repository;

@Service
public class CatE43Service {

    @Autowired
    CatBomService catBomService;

    @Autowired
    CatE43Repository catE43Repository;

    public List<String> readFile(File file) {
        BufferedReader br = null;
        String line;
        List<String> lines = new ArrayList<>();

        //"UTF8"
        try {
            br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8)); 
            line = br.readLine();
            while(line!=null){
                lines.add(line);
                line = br.readLine();
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

        return lines;

    }

    public void uploadE43(MultipartFile file) throws IOException, ParseException {
        //traitement du fichier liste des E43 exitants
        //Lire le fichier et renseigner la table cat_E43 
        List<String> lines = readFile(catBomService.convert(file));
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        
        
        String idE43 = "";
        String idItem = "";
        Date dateD = null;
        Date dateF = null;
        String otp = "";

        for (String line : lines) {
            if (line.contains("|")) {
                String[] lineSplit = line.split("\\s*\\|\\s*"); // use a regular expression to split and trim result

                if (lineSplit[1].equals("") && (lineSplit.length>11) ) {
            
                    idE43 = lineSplit[2] + "_" + lineSplit[3] + "_" + lineSplit[4] + "_" + lineSplit[5];
                    dateD = formatter.parse(lineSplit[10]);
                    dateF = formatter.parse(lineSplit[11]);
                    idItem = lineSplit[3].substring(0, lineSplit[3].indexOf("_"));
                    
                    if (lineSplit.length == 13) otp = lineSplit[lineSplit.length-1];

                    CatE43 catE43 = new CatE43(idE43, idItem, dateD, dateF, otp);
                    catE43Repository.save(catE43);
                }

            }
            

        }


    }
    
}
