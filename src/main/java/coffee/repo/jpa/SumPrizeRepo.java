package coffee.repo.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import coffee.model.SumPrize;

@Repository
public interface SumPrizeRepo extends CrudRepository<SumPrize, Long> {
	public SumPrize findByLottoNumberAndMsdLottoKindCodeAndLottoClassCode(String lottoNumber, String MsdLottoKindCodeCode, String lottoClassCode);
	public void deleteByLottoClassCode(String classCode);
}
