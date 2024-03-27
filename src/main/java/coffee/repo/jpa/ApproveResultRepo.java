package coffee.repo.jpa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import coffee.model.ApproveResult;

@Repository
public interface ApproveResultRepo extends CrudRepository<ApproveResult, Long> {
	List<ApproveResult> findByCodeGroup(String codeGroup);
	ApproveResult findByInstallmentAndLottoClassCodeAndUsername(String installment, String classCode, String username);
}
