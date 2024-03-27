package coffee.repo.jpa;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import coffee.model.LottoGroupTransaction;

@Repository
public interface LottoGroupTransactionRepo extends CrudRepository<LottoGroupTransaction, Long> {
	List<LottoGroupTransaction> findByUsername(String username);

	@Query(value = "SELECT SUM(sum_group_bet ) from lotto_group_transaction where lotto_class_code = ?1 and username = ?2 and installment = ?3 and round_yeekee = ?4 and status = 'SHOW' ", nativeQuery = true)
	public BigDecimal sumBetGroupTransactionYeekee(String classCode, String username, String installment,
			Integer roundNumber);

	LottoGroupTransaction findByLottoGroupTransactionCode(String lottoGroupTransactionCode);

	@Modifying
	@Query(value = "UPDATE lotto_group_transaction set status = ?1 where status = ?2 and lotto_class_code = ?3 ", nativeQuery = true)
	public void updateTransaction(String statusSet, String statusFind, String classCode);

	@Query(value = "SELECT COUNT(1) FROM LOTTO_GROUP_TRANSACTION LGT WHERE INSTALLMENT = ?1 AND STATUS = ?2 AND USERNAME  = ?3 AND LOTTO_CLASS_CODE = ?4", nativeQuery = true)
	public Integer getCountRefund(String installment, String status, String username, String classCode);

	@Modifying
	@Query(value = "UPDATE lotto_group_transaction SET status = ?5, remark = ?4, updated_at = GETDATE(), updated_by = ?4 WHERE Lotto_class_code = ?1 AND installment = ?2 AND round_yeekee = ?3", nativeQuery = true)
	void updateGroupTransactionLottoYeekee(String classCode, String installment, Integer roundYeekee, String createdBy,
			String remark, String status);

	@Modifying
	@Query(value = "UPDATE lotto_group_transaction SET status = ?5, remark = ?4, updated_at = GETDATE(), updated_by = ?3 WHERE Lotto_class_code = ?1 AND installment = ?2", nativeQuery = true)
	void updateGroupTransactionLotto(String classCode, String installment, String createdBy, String remark,
			String status);

}
