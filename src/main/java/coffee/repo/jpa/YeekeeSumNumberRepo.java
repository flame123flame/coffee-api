package coffee.repo.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import coffee.model.YeekeeSumNumber;

@Repository
public interface YeekeeSumNumberRepo extends CrudRepository<YeekeeSumNumber, Long> {
    YeekeeSumNumber findByInstallmentAndClassCodeAndRoundNumber(String installment, String classCode,
            Integer roundNumber);

    List<YeekeeSumNumber> findByStatusOrderByInstallment(String status);
    
    YeekeeSumNumber findFirst1ByClassCodeOrderByInstallmentDesc(String classCode);

    YeekeeSumNumber findByInstallmentAndClassCodeAndRoundNumberAndStatus(String installment, String classCode,
            Integer roundNumber, String status);

    @Query(value = "SELECT ysn.round_number FROM yeekee_sum_number ysn WHERE ysn.installment = ?1 GROUP BY ysn.round_number ORDER BY ysn.round_number asc;", nativeQuery = true)
    List<Integer> findByInstallment(String installment);

    @Query(value = "SELECT ysn.* FROM yeekee_sum_number ysn WHERE ysn.class_code = ?1 AND ysn.installment = ?2 ORDER BY ysn.round_number ASC", nativeQuery = true)
    List<YeekeeSumNumber> findByClassCodeAndInstallmentOrderByRoundNumberAsc(String classCode, String installment);

    @Query(value = "SELECT ysn.* FROM yeekee_sum_number ysn WHERE ysn.class_code = ?1 AND ysn.round_number = ?2 ORDER BY ysn.installment DESC", nativeQuery = true)
    List<YeekeeSumNumber> findByClassCodeAndRounYeekeeOrderByRoundNumberAsc(String classCode, Integer roundYeekee);
    
    Integer countByClassCodeAndInstallment(String classCode, String installment);

    Integer countByStatus(String status);

}
