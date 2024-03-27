package coffee.repo.jpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import coffee.model.SumBetGroup;

@Repository
public interface SumBetGroupRepo extends CrudRepository<SumBetGroup, Long> {
	public SumBetGroup findByLottoGroupCodeAndLottoClassCode(String lottoGroupCode, String lottoClassCode);

	public void deleteByLottoClassCode(String classCode);
}
