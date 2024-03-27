package coffee.repo.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import coffee.model.GroupMaxMinMap;

public interface GroupMaxMinMapRepo extends JpaRepository<GroupMaxMinMap, Long> {
	public GroupMaxMinMap findByGroupMaxMinMapCode(String GroupMaxMinMapCode);
	
	public List<GroupMaxMinMap> findByLottoClassCodeAndMsdLottoKindCode(String lottoClassCode, String msdLottoKindCode);
//	public List<GroupMaxMinMap> findByLottoClassCodeAndMsdLottoKindCodeAndLottoGroupCode(String lottoClassCode, String msdLottoKindCode,String lottoGroupCode);
	public GroupMaxMinMap findByLottoClassCodeAndMsdLottoKindCodeAndVipCode(String lottoGroupCode, String msdLottoKindCode, String vipCode);
	public List<GroupMaxMinMap> findByLottoClassCodeAndMsdLottoKindCodeAndLottoGroupCode(String lottoClassCode, String msdLottoKindCode, String lottoGroupCode);
	public void deleteByMsdLottoKindCodeAndLottoGroupCodeAndLottoClassCode(String msdLottoKindCode, String lottoGroupCode,String lottoClassCode);
	public void deleteByLottoGroupCode(String lottoGroupCode);
	public void deleteByGroupMaxMinMapCode(String groupMaxMinMapCode);
	public void deleteByGroupMaxMinMapId(Long groupMaxMinMapId);
}
