package coffee.repo.jpa;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import coffee.model.DraftTimeSell;

@Repository
public interface DraftTimeSellRepo extends CrudRepository<DraftTimeSell, Long> {
    DraftTimeSell findByTimeSellCode(String timeSellCode);

    List<DraftTimeSell> findByLottoClassCode(String code);

    List<DraftTimeSell> findByDraftCode(String draftCode);

    public void deleteByLottoClassCode(String code);

    public void deleteByTimeSellCode(String code);
}