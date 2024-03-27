package coffee.repo.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import coffee.lottoresult.vo.req.thai.SendKeyBoReq.SendBoReq;
import coffee.model.LottoTransaction;

@Repository
public interface LottoTransactionRepo extends JpaRepository<LottoTransaction, Long> {
	public List<LottoTransaction> findByLottoGroupTransactionCode(String lottoGroupTransactionCode);

	@Modifying
	@Query(value = "UPDATE lotto_transaction set status = ?1 ,number_correct = ?2 ,updated_date = ?3 where status = ?4 and lotto_number = ?5 and lotto_class_code = ?6 and lotto_kind_code = ?7", nativeQuery = true)
	public void updateTransaction(String statusSet, String numberSet, Date updatedDate, String statusFind,
			String numberFind, String classCode, String kindCode);

	@Modifying
	@Query(value = "UPDATE lotto_transaction set status = ?1 ,number_correct = ?2 ,updated_date = ?3 where status = ?4 and lotto_class_code = ?5 and lotto_kind_code = ?6 ", nativeQuery = true)
	public void updateTransactionLike(String statusSet, String numberSet, Date updatedDate, String statusFind,
			String classCode, String kindCode);

	@Modifying
	@Query(value = "UPDATE lotto_transaction set update_wallet = ?1 ,paid_at = ?3 ,paid_by = ?4  where update_wallet is null and lotto_class_code = ?2 ", nativeQuery = true)
	public void updateTransactionDb(Boolean updateWallet, String classCode, Date date, String paidBy);

	@Modifying
	@Query(value = "UPDATE lotto_transaction set update_wallet = ?1  ,reject_remark = ?2 ,paid_at = ?3 ,paid_by = ?4 where update_wallet is null ", nativeQuery = true)
	public void updateTransactionDb2(Boolean updateWallet, String remark, Date date, String paidBy);

	public List<LottoTransaction> findByUpdatedDateBetween(Date start, Date end);

	@Query(value = "SELECT lt.installment FROM lotto_transaction lt WHERE lt.lotto_class_code = ?1 AND lt.status = ?2 AND lt.status is not NULL GROUP BY lt.installment ", nativeQuery = true)
	public List<String> findInstallmentByLottoClassCode(String classCode, String status);

	@Query(value = "SELECT lt.round_yeekee FROM lotto_transaction lt WHERE lt.installment = ?1 AND lt.status = ?2 AND lt.lotto_class_code = ?3 AND lt.status is not NULL GROUP BY lt.round_yeekee ORDER BY lt.round_yeekee asc", nativeQuery = true)
	public List<Integer> findRoundByInstallment(String installment, String status, String lottoClassCode);

	@Modifying
	@Query(value = "UPDATE lotto_transaction SET status = ?1, updated_date = ?2, updated_by = ?6 WHERE Lotto_class_code = ?3 AND installment = ?4 AND round_yeekee = ?5", nativeQuery = true)
	public void updateTransactionLottoYeekee(String status, Date date, String classCode, String installment, Integer roundYeekee, String createdBy);

	@Modifying
	@Query(value = "UPDATE lotto_transaction SET status = ?1, updated_date = ?2, updated_by = ?5 WHERE Lotto_class_code = ?3 AND installment = ?4", nativeQuery = true)
	public void updateTransactionLotto(String status, Date date, String classCode, String installment, String createdBy);
	
	@Modifying
	@Query(value = "SELECT * from lotto_transaction lt where lt.installment = ?1 and lt.lotto_class_code = ?2 and lt.round_yeekee = ?3 and lt.status = 'WIN'", nativeQuery = true)
	public List<LottoTransaction> findTransactionYeekee(String installment ,String classCode,Integer roundNumber);
	
}