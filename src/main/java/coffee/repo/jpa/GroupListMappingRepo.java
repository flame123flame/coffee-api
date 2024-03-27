package coffee.repo.jpa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import coffee.model.GroupListMapping;

@Repository
public interface GroupListMappingRepo extends CrudRepository<GroupListMapping, Long> {
    List<GroupListMapping> findByGroupListMappingCode(String groupListMappingCode);
    
    List<GroupListMapping> findAll();
}