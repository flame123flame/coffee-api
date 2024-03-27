package coffee.repo.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import coffee.model.CloseNumber;

public interface CloseNumberRepo extends JpaRepository<CloseNumber, Long> {
	CloseNumber findByCloseNumberId(Long closeId);

	CloseNumber findByLottoNumberAndLottoClassCodeAndMsdLottoKindCode(String lottoNumber, String classCode,
			String kindCode);

	List<CloseNumber> findByEnable(Boolean enable);

	List<CloseNumber> findByLottoClassCodeAndEnable(String lottoClassCode, Boolean enable);

	@Modifying
	void deleteBySwappedGroupCode(String swappedGroupCode);

	@Modifying
	void deleteByLottoClassCode(String lottoClassCode);

	@Modifying
	@Query(value = "UPDATE CLOSE_NUMBER SET ENABLE = ?1 WHERE SWAPPED_GROUP_CODE = ?2", nativeQuery = true)
	public void updateEnableByGroupSwappedCode(Boolean status, String swappedGroupCode);

}
