package bobst.catalog.compoCat4.controllers;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import bobst.catalog.compoCat4.services.CatBomService;
import bobst.catalog.compoCat4.services.CatE43Service;
import bobst.catalog.compoCat4.services.CatLogService;
import bobst.catalog.compoCat4.services.CatPageContentService;
import bobst.catalog.compoCat4.services.CatPageService;

@Controller
public class CaptureController {
    @Autowired
    private CatLogService catLogService;

    @Autowired
    private CatBomService catBomService;

    @Autowired
    private CatPageService catPageService;

    @Autowired
    private CatPageContentService catPageContentService;

    @Autowired CatE43Service catE43Service;

    @Value ("${dirCapture}")
    private String dirCapture;

    private String status_error = "error";


    @GetMapping("/capturer")
    public String uploadFile(Model model) {
        return "uploadForm";
    }

    @PostMapping("/captureBom")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) throws IllegalStateException, IOException {
        String status = status_error;
        try {
            catBomService.uploadBom(file);             
            status = "done";
        } catch (Exception e) { 
            //
        }
        informLog(file,status);
        return "redirect:/capture";       
    }
    

    @PostMapping("/captureZip")
    public String processZip(@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {
        String status = status_error;
        try {
            catPageService.uploadZip(file);
            status = "done";
        } catch (Exception e) {
            //
        }
        informLog(file,status);
        return "redirect:/capture";
    }

    @PostMapping("/captureXml")
    public String processXml(@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {
        String status = status_error;
        try {
            catPageContentService.uploadXml(file);             
            status = "done";
        } catch (Exception e) {
            //
        }
        informLog(file,status);
        return "redirect:/capture";
    }
    
    @PostMapping("/captureE43")
    public String processE43(@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {
        String status = status_error;
        try {
            catE43Service.uploadE43(file);             
            status = "done";
        } catch (Exception e) {
            //
        }
        informLog(file,status);
        return "redirect:/capture";
    }
    

    public void informLog(MultipartFile file, String status) throws IllegalStateException, IOException {
        //move file , add log to table Cat_log
        catLogService.update(file,status);
        File dest = new File(dirCapture + status + "\\" + file.getOriginalFilename());
        file.transferTo(dest); 
        File f = new File(dirCapture + "entry\\" + file.getOriginalFilename());
        if (f.exists())
            f.delete();       
    }

}
