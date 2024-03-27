package coffee.repo.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import coffee.model.GroupKindMap;

@Repository
public interface GroupKindMapRepo extends JpaRepository<GroupKindMap, Long> {
	
	GroupKindMap findByLottoGroupCodeAndMsdLottoKindCode(String lottoGroupCode, String msdLottoKindCode);
	
	List<GroupKindMap> findByLottoGroupCode(String lottoGroupCode);

	public void deleteByMsdLottoKindCodeAndLottoGroupCode(String msdLottoKindCode, String lottoGroupCode);
	public void deleteByLottoGroupCode(String lottoGroupCode);
}
