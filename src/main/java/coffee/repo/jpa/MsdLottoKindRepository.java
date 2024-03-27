package coffee.repo.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import coffee.model.MsdLottoKind;

@Repository
public interface MsdLottoKindRepository extends JpaRepository<MsdLottoKind, Long>{
	MsdLottoKind findByMsdLottoKindCode(String msdLottoKindCode);
}
