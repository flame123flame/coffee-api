package coffee.repo.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import coffee.model.LottoGroupDtl;

@Repository
public interface LottoGroupDtlRepo extends JpaRepository<LottoGroupDtl, Long> {
	LottoGroupDtl findByLottoGroupDtlCode(String lottoGroupDtlCode);
	List<LottoGroupDtl> findByLottoGroupCode(String lottoGroupCode);
	public void deleteBylottoGroupCode(String lottoGroupCode);
	public void deleteByLottoGroupDtlCode(String lottoGroupDtlCode);
}
