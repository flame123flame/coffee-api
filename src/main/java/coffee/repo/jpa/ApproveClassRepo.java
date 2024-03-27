package coffee.repo.jpa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import coffee.model.ApproveClass;

@Repository
public interface ApproveClassRepo extends CrudRepository<ApproveClass, Long> {
    public List<ApproveClass> findByDraftCode(String draftCode);
}