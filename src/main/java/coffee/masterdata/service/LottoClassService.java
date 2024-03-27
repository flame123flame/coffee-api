package coffee.masterdata.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coffee.masterdata.vo.BoSyncRes;
import coffee.model.LottoClass;
import coffee.repo.dao.LottoClassDao;
import coffee.repo.jpa.LottoClassRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LottoClassService {

	@Autowired
	private LottoClassRepository getLottoClassRepository;

	@Autowired
	private LottoClassDao lottoClassDao;

	public List<LottoClass> findByLottoCategoryCode(String lottoCategoryCode) {
		return getLottoClassRepository.findByLottoCategoryCodeOrderByCreatedAtDesc(lottoCategoryCode);
	}

	public List<LottoClass> getAll() {
		return getLottoClassRepository.findAll();
	}

	public List<LottoClass> getByLottoCategoryCode(String lottoCategoryCode) {
		return getLottoClassRepository.findByLottoCategoryCode(lottoCategoryCode);
	}

	public LottoClass getLottoClassByClassCode(String lottoClassCode) {
		System.out.println(">>>>>>>>>>>>>>>>>>" + lottoClassCode);
		return getLottoClassRepository.findByLottoClassCode(lottoClassCode);
	}

	public List<BoSyncRes> getAllLottoClass() {
		return lottoClassDao.getAllLottoClass();
	}

}
