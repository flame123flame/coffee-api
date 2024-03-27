package coffee.repo.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import coffee.model.LottoResult;

@Repository
public interface LottoResultRepo extends JpaRepository<LottoResult, Long> {

	LottoResult findByLottoClassCodeAndLottoResultInstallmentAndLottoNumber(String lottoClassCode, String lottoResultInstallment, String lottoNumber);

	LottoResult findByLottoClassCodeAndLottoResultInstallmentAndLottoNumberAndMsdLottoKindCodeAndStatus(String lottoClassCode, String lottoResultInstallment, String lottoNumber, String msdLottoKindCode, String status);

	List<LottoResult> findByLottoClassCodeAndLottoResultInstallmentAndStatus(String lottoClassCode, String lottoResultInstallment, String status);

	List<LottoResult> findByLottoClassCode(String lottoClassCode);
	

	List<LottoResult> findByLottoClassCodeAndStatus(String lottoClassCode, String status);

	@Modifying
	@Query(value = "update lotto_result set status = ?1 where status = ?2 and lotto_result_installment = ?3 and lotto_class_code = ?4 ", nativeQuery = true)
	void updateToApprove(String statusSet, String statusFind, String installment, String lottoClassCode);

	List<LottoResult> findByLottoClassCodeAndMsdLottoKindCode(String lottoClassCode, String msdLottoKindCode);

	List<LottoResult> findByLottoClassCodeAndLottoResultInstallment(String lottoClassCode, String installment);
	
	List<LottoResult> findByCodeGroup(String codeGroup);
	
	List<LottoResult> findByLottoClassCodeAndLottoCategoryCodeAndStatus(String classCode,String categoryCode,String status);
	
	@Query(value = "SELECT TOP 20 lr.lotto_result_installment FROM lotto_result lr WHERE lr.status = 'APPROVE' GROUP BY lr.lotto_result_installment ", nativeQuery = true)
	List<String> getInstallment();
	
}
