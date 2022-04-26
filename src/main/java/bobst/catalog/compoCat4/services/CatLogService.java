package bobst.catalog.compoCat4.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import bobst.catalog.compoCat4.models.CatLog;
import bobst.catalog.compoCat4.repositories.CatLogRepository;

@Service
public class CatLogService {
    @Autowired
    CatLogRepository catLogRepository;

    public void update(MultipartFile file, String status) {
        catLogRepository.save(new CatLog(new Date(), file.getOriginalFilename(), status));
    }
    

}
