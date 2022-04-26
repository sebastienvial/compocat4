package bobst.catalog.compoCat4.services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import bobst.catalog.compoCat4.models.CatPage;
import bobst.catalog.compoCat4.repositories.CatBomRepository;
import bobst.catalog.compoCat4.repositories.CatPageRepository;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;

@Service
public class CatPageService {

    @Autowired
    private CatPageRepository catPageRepository;

    @Autowired
    private CatBomRepository catBomRepository;

    private void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) >= 0) {
          outputStream.write(buffer, 0, length);
        }
      }

    

    private boolean uploadInStorage(Path source, String typeStorage) throws IOException {
      boolean res = false;

      if (typeStorage.equals("DEV")) {

        Path target = Paths.get("svg");
        //Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(source, target.resolve(source.getFileName()), StandardCopyOption.REPLACE_EXISTING);
      } else {
        uploadInStorage(source);
      }

      String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                      .path("/files/download/")
                      .path(source.toString())
                      .toUriString();
      
      //charger en BD l'URI                
      System.out.println(fileDownloadUri);
      return true;
    }

    private boolean uploadInStorage(Path source) throws IOException {

        boolean res = false;
        String connectionString = "DefaultEndpointsProtocol=https;AccountName=svgview;AccountKey=I++B93xrQ5mpYyLtTahG08/wM20grjTqRQhFrYDi8w8HvS7mMBt3y09aOLswkmqgS5DNQo8LxXzOAt9MXEvicw==;EndpointSuffix=core.windows.net";
        
        // Create a BlobServiceClient object which will be used to return the container client
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
        
        // Return the container client object
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient("svg");

        String localPath = source.toAbsolutePath().toString();
        String fileName = source.toString();

        // Get a reference to a blob
        BlobClient blobClient = containerClient.getBlobClient(fileName);

        System.out.println("\nUploading to Blob storage as blob:\n\t" + blobClient.getBlobUrl());

        // Upload or update the blob (overwrite= true)
        try {
          blobClient.uploadFromFile(localPath, true);          
          res = true;

        } catch (UncheckedIOException ex) {
          System.err.printf("Failed to upload from file %s%n", ex.getMessage());
        } 
        
        return res;
    }
      
    public boolean insertBDcatPage(Path source) {
      boolean res = false;
      String idPage = source.toString().substring(0,source.toString().lastIndexOf("."));
      String idE43 = idPage.substring(0,idPage.lastIndexOf("_"));

      CatPage catPage = new CatPage();
      catPage.setIdPage(idPage);
      catPage.setIdE43(idE43);

      if (catPageRepository.save(catPage) != null) {
        res = true;
      };
      return res;
    }

    public void unzip(MultipartFile file) throws IOException {    
      ZipInputStream zis = new ZipInputStream(file.getInputStream()); 
      ZipEntry entry = zis.getNextEntry();
      while (entry != null) {
        Path outputEntryPath = Paths.get(file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."))+".svg");
        if (entry.isDirectory() && !Files.exists(outputEntryPath)) {
          Files.createDirectory(outputEntryPath);
        } else if (!entry.isDirectory()) {
          try (FileOutputStream fos = new FileOutputStream(outputEntryPath.toFile())) {
            copy(zis, fos);
            fos.close();
            completeSVG(outputEntryPath);
            if (uploadInStorage(outputEntryPath, "DEV")) {
              insertBDcatPage(outputEntryPath);
            }

            if (outputEntryPath.toFile().delete()) {
              System.out.println("Delete successfuly");
            } else {
              System.out.println("Failed to delete");
            }
          }
        }
        entry = zis.getNextEntry();
      }
      zis.closeEntry();
    }



    public void uploadZip(MultipartFile file) throws IOException {
        unzip(file);
    }

    public void completeSVG(Path path){
        //browse the file and enter the new script
        String script = getClass().getResource("/script.txt").getPath();
        BufferedReader brscript = null;
       
        String oldFileName = path.toString();
        String tmpFileName = "tmp_" + path.toString();

        BufferedReader br = null;
        BufferedWriter bw = null;
        Boolean copy = false;
        try {
          br = new BufferedReader(new FileReader(oldFileName));
          bw = new BufferedWriter(new FileWriter(tmpFileName));
          brscript = new BufferedReader(new FileReader(script));

          String line;
          String lineTemp;

          while ((line = brscript.readLine()) != null) {
              bw.write(line+"\n");
          }

          while ((line = br.readLine()) != null) {
              if ((line.contains("</script>")) | copy) {
                copy = true;
                if (line.contains("onmousemove")) {
                  line = AddOnClick(line);
                }
                bw.write(line+"\n");
              }              
          }
        } catch (Exception e) {
          return;
        } finally {
          try {
              if(br != null)
                br.close();
          } catch (IOException e) {
              //
          }
          try {
              if(bw != null)
                bw.close();
          } catch (IOException e) {
              //
          }
        }

        //Once everything is complete, delete old file..
        File oldFile = new File(oldFileName);
        if (oldFile.delete()) 
          System.out.println("Fichier old deleted");
             
        //And rename tmp file's name to old file name
        File newFile = new File(tmpFileName);
        if (newFile.renameTo(oldFile))
          System.out.println("Rename successfully");

   }

   
   private String AddOnClick(String line) {
     String res = "";
     String lineEnd = line;
     String onmouse;
     String onclick;
     String r = "onmousemove";
     //onmousemove="ShowToolTip(evt,'0',&quot;BSA01150000HG&quot;)"
     while (lineEnd.contains(r)) {
        res = res + lineEnd.substring(0,lineEnd.indexOf(r));
        lineEnd = lineEnd.substring(lineEnd.indexOf(r));
        //search onmousmove params
        onmouse = lineEnd.substring(lineEnd.indexOf("("),lineEnd.indexOf(")"));
        onclick = " onclick=\"ActiveClick" + onmouse;
        //add onclick
        res = res + lineEnd.substring(0,lineEnd.indexOf(")\"")+2) + onclick;
        lineEnd = lineEnd.substring(lineEnd.indexOf(")\""));
     }
     res = res + lineEnd;
     //System.out.println(res);
     return res;
   }
    
}
