package coffee.repo.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import coffee.model.LottoClass;

@Repository
public interface LottoClassRepository extends JpaRepository<LottoClass, Long>{
	public List<LottoClass> findByLottoCategoryCodeOrderByCreatedAtDesc(String lottoCategoryCode);
	public LottoClass findByLottoClassCode(String lottoClassCode);
	public LottoClass findByLottoClassIdAndLottoClassCode(Long id,String ClassCode);

	public List<LottoClass> findByLottoCategoryCode(String lottoCategoryCode);
	public LottoClass findByLottoCategoryCodeAndLottoClassCode(String lottoCategoryCode,String ClassCode);
	
	public void deleteByLottoClassCode(String code);
}
