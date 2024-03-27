package coffee.lottoresult.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coffee.repo.jpa.CloseNumberRepo;
import coffee.repo.jpa.LimitNumberRepo;
import coffee.repo.jpa.SumBetGroupRepo;
import coffee.repo.jpa.SumPrizeRepo;

@Service
public class ClearInstallmentService {
	@Autowired
	private SumPrizeRepo sumprizeRepo;

	@Autowired
	private SumBetGroupRepo sumBetGroupRepo;

	@Autowired
	private LimitNumberRepo limitNumberRepo;

	@Autowired
	private CloseNumberRepo closeNumberRepo;

	public void clearInstallment(String classCode) {
		sumprizeRepo.deleteByLottoClassCode(classCode);
		sumBetGroupRepo.deleteByLottoClassCode(classCode);
		limitNumberRepo.deleteByLottoClassCode(classCode);
		closeNumberRepo.deleteByLottoClassCode(classCode);
	}

}
