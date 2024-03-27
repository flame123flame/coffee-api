package coffee.repo.jpa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import coffee.model.CustomerDumy;

@Repository
public interface CustomerDumyRepo extends CrudRepository<CustomerDumy, Long>{
	
	public List<CustomerDumy> findAll();
	public CustomerDumy findByDumyCode(String code);
	public void deleteByDumyCode(String code);
}
