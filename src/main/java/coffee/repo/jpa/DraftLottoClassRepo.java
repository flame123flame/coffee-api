package coffee.repo.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import coffee.model.DraftLottoClass;

@Repository
public interface DraftLottoClassRepo extends CrudRepository<DraftLottoClass, Long> {
    public List<DraftLottoClass> findByLottoCategoryCodeOrderByCreatedAtDesc(String lottoCategoryCode);

    public DraftLottoClass findByLottoClassCode(String lottoClassCode);

    public DraftLottoClass findByDraftCode(String draftCode);

    public List<DraftLottoClass> findByLottoCategoryCode(String lottoCategoryCode);

    public void deleteByLottoClassCode(String code);

    @Modifying
    @Query(value = "UPDATE DRAFT_LOTTO_CLASS SET CREATE_STATUS = 'APPROVE' WHERE DRAFT_CODE = ?1", nativeQuery = true)
    public void updateStatusApprove(String draftCode);

    @Modifying
    @Query(value = "UPDATE DRAFT_LOTTO_CLASS SET CREATE_STATUS = 'REJECT' WHERE DRAFT_CODE = ?1", nativeQuery = true)
    public void updateStatusReject(String draftCode);

    @Query(value = "SELECT COUNT(1) from draft_lotto_class dlc   where create_status = 'INIT' and lotto_category_code  =  ?1", nativeQuery = true)
    public Integer getCountInit(String categoryCode);
}