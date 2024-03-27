package coffee.repo.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import coffee.model.LimitNumber;

@Repository
public interface LimitNumberRepo extends JpaRepository<LimitNumber, Long> {
	LimitNumber findAllByMsdLottoKindCodeAndLottoClassCodeAndLottoNumberAndIsManualAndEnable(String code,
			String classCode, String lottoNumber, Boolean isManual, Boolean enable);

	LimitNumber findByMsdLottoKindCodeAndLottoClassCodeAndLottoNumberAndIsManual(String code, String classCode,
			String lottoNumber, Boolean isManual);

	List<LimitNumber> findAllByMsdLottoKindCodeAndLottoClassCodeAndIsManual(String kindCode, String classCode,
			Boolean isManual);

	List<LimitNumber> findAllByMsdLottoKindCodeAndLottoClassCodeAndEnable(String kindCode, String classCode,
			Boolean enable);

	public void deleteByLottoClassCode(String classCode);

	void deleteByMsdLottoKindCodeAndLottoClassCodeAndLottoNumberAndIsManual(String code, String classCode,
			String lottoNumber, Boolean isManual);

	void deleteByMsdLottoKindCodeAndLottoClassCodeAndLottoNumber(String code, String classCode, String lottoNumber);
	// List<LimitNumber>
	// findAllByMsdLottoKindCodeAndLottoClassCodeAndIsManual(String code, String
	// classCode, Boolean isManual);
	// List<LimitNumber> findAllByMsdLottoKindCodeAndLottoClassCode(String code,
	// String classCode);
	// LimitNumber
	// findAllByMsdLottoKindCodeAndLottoClassCodeAndLottoNumberAndIsManual(String
	// msdLottoKindCode, String classCode, String lottoNumber, Boolean isManual);
}
