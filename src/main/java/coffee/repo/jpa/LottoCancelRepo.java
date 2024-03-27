package coffee.repo.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import coffee.model.LottoCancel;

public interface LottoCancelRepo extends JpaRepository<LottoCancel, Long>{
	
	public LottoCancel findByLottoCancelCode(String lottoCancelCode);
	

}
