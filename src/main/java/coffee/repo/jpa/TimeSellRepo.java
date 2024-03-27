package coffee.repo.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import coffee.model.TimeSell;

@Repository
public interface TimeSellRepo extends CrudRepository<TimeSell, Long> {
	TimeSell findByTimeSellCode(String timeSellCode);

	List<TimeSell> findByLottoClassCode(String code);

	public void deleteByLottoClassCode(String code);

	public void deleteByTimeSellCode(String code);

	@Modifying
	@Query(value = "DELETE FROM time_sell WHERE lotto_class_code = ?1 ", nativeQuery = true)
	void deleteYeekeeTime(String code);

	TimeSell findByLottoClassCodeAndRoundYeekee(String lottoClassCode, int roundYeekee);

	TimeSell findTop1ByLottoClassCodeOrderByRoundYeekeeDesc(String lottoClassCode);
}
