package coffee.repo.jpa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import coffee.model.YeekeeSubmitNumber;

@Repository
public interface YeekeeSubmitNumberRepo extends CrudRepository<YeekeeSubmitNumber, Long> {
	YeekeeSubmitNumber findFirst1ByClassCodeAndInstallmentAndRoundNumberOrderByCreatedAtDesc(String classCode,
			String installment, Integer roundNumber);
	
	List<YeekeeSubmitNumber> findFirst20ByClassCodeAndInstallmentAndRoundNumberOrderByCreatedAtDesc(String classCode,
			String installment, Integer roundNumber);

	List<YeekeeSubmitNumber> findByClassCodeAndInstallmentAndRoundNumberOrderByCreatedAtDesc(String classCode,
			String installment, Integer roundNumber);

	List<YeekeeSubmitNumber> findByInstallmentAndClassCodeAndRoundNumberOrderBySeqOrderDesc(String installment,
			String classCode, Integer roundNumber);

	YeekeeSubmitNumber findFirst1ByInstallmentAndClassCodeAndRoundNumberAndNumberSubmitAndIsBot(String installment,
			String classCode, Integer roundNumber, Long numberSubmit, Boolean isBot);

	YeekeeSubmitNumber findFirst1ByInstallmentAndClassCodeAndRoundNumberAndNumberSubmitAndIsBotAndSeqOrderNot(
			String installment, String classCode, Integer roundNumber, Long numberSubmit, Boolean isBot,
			Integer seqOrder);
}
