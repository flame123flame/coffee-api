package framework.security.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import framework.security.model.FwUser;

@Repository
public interface FwUserRepo extends CrudRepository<FwUser, Integer> {
	FwUser findByUsername(String username);
}
