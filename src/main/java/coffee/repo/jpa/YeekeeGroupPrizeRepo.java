package coffee.repo.jpa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import coffee.model.YeekeeGroupPrize;

@Repository
public interface YeekeeGroupPrizeRepo extends CrudRepository<YeekeeGroupPrize, Long> {
        YeekeeGroupPrize findFirst1ByClassCodeAndKindCodeAndLottoNumberAndInstallmentAndRoundYeekee(String classCode,
                        String lottoNumber, String kindCode, String installment, Integer roundYeekee);

        YeekeeGroupPrize findByLottoNumberAndRoundYeekeeAndInstallmentAndClassCode(String lottoNumber,
                        Integer roundYeekee, String installment, String classCode);

        List<YeekeeGroupPrize> findByClassCode(String classCode);

        List<YeekeeGroupPrize> findByClassCodeAndRoundYeekeeAndInstallmentAndKindCodeOrderBySumPrizeDesc(
                        String classCode, Integer roundYeekee, String installment, String kindCode);

        YeekeeGroupPrize findFrist1ByClassCodeAndInstallmentAndKindCodeAndRoundYeekeeAndCreatedByAndLottoNumber(
                        String classCode, String installment, String kindCode, Integer roundYeekee, String createdBy,
                        String lottoNumber);
}
