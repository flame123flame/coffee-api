package coffee.repo.jpa;

import coffee.model.LottoCategory;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LottoCategoryRepo extends CrudRepository<LottoCategory , Long> {
    Optional<LottoCategory> findByLottoCategoryCode(String str);
}
