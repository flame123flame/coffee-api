package coffee.repo.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import coffee.model.LottoGroup;

@Repository
public interface LottoGroupRepo extends JpaRepository<LottoGroup, Long> {
	public LottoGroup findByLottoGroupCode(String lottoGroupCode);
	public List<LottoGroup> findAllByLottoClassCode(String lottoClassCode);
	public void deleteByLottoGroupCode(String code);
}
