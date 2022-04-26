package bobst.catalog.compoCat4.repositories;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import bobst.catalog.compoCat4.models.CatLog;

public interface CatLogRepository extends CrudRepository<CatLog,UUID>{
    
}
