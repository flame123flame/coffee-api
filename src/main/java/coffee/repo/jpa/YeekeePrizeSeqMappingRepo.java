package coffee.repo.jpa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import coffee.model.YeekeePrizeSeqMapping;

@Repository
public interface YeekeePrizeSeqMappingRepo extends CrudRepository<YeekeePrizeSeqMapping, Long>{
	List<YeekeePrizeSeqMapping> findByClassCode(String classCode);
	
	YeekeePrizeSeqMapping findByYeekeePrizeSeqMappingCode(String code);
	
	public void deleteByYeekeePrizeSeqMappingCode(String code);
}
