package coffee.repo.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import coffee.model.PrizeSetting;

@Repository
public interface PrizeSettingRepo extends JpaRepository<PrizeSetting, Long> {
	public PrizeSetting findByPrizeSettingCode(String PrizeSettingCode);

	public PrizeSetting findByPrizeSettingId(Long id);

	public PrizeSetting findFirstByLottoGroupCodeOrderBySeqOrderDesc(String code);

	public PrizeSetting findFirstByLottoGroupCodeAndLottoGroupDtlCode(String code1, String code2);

	List<PrizeSetting> findByClassCodeAndVipCode(String classCode, String vipCode);

	List<PrizeSetting> findByLottoGroupCode(String code);

	List<PrizeSetting> findByLottoGroupCodeAndMsdLottoKindCode(String lottoGroupCode, String msdLottoKindCode);
	PrizeSetting findByClassCodeAndMsdLottoKindCodeAndVipCode(String classCode,String msdCode,String vipCode);
	PrizeSetting findByClassCodeAndMsdLottoKindCodeAndLottoGroupDtlCode(String classCode,String msdCode,String dtlCode);
	PrizeSetting findByClassCodeAndMsdLottoKindCodeAndPrizeSettingCode(String classCode,String msdCode,String prizeSettingCode);
	List<PrizeSetting> findByClassCode(String classCode);
	List<PrizeSetting> findByLottoGroupCodeAndMsdLottoKindCodeAndLottoGroupDtlCode(String lottoGroupCode, String msdLottoKindCode,String lottoGroupDtlCode);
	
	public void deleteByPrizeSettingCode(String code);

	public void deleteByLottoGroupCode(String code);

	public void deleteByMsdLottoKindCodeAndLottoGroupCode(String msdLottoKindCode, String lottoGroupCode);

	public void deleteByLottoGroupDtlCode(String lottoGroupDtlCode);
	
	@Modifying
	@Query(value = "DELETE FROM prize_setting WHERE prize_setting_id = ?1 ",nativeQuery = true)
	void deleteYeekeePrizeById(Long id);

}
