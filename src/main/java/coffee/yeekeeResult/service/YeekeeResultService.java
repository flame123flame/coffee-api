package coffee.yeekeeResult.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import coffee.buy.vo.req.SubmitYeeKeeReq;
import coffee.masterdata.service.LottoClassService;
import coffee.model.TimeSell;
import coffee.model.YeekeeGroupPrize;
import coffee.model.YeekeePrizeSeqMapping;
import coffee.model.YeekeeSubmitNumber;
import coffee.redis.model.YeekeeLastList;
import coffee.redis.repo.LastListYeekeeRedisRepo;
import coffee.repo.dao.YeekeeResultDao;
import coffee.repo.jpa.LottoTransactionRepo;
import coffee.repo.jpa.MsdLottoKindRepository;
import coffee.repo.jpa.TimeSellRepo;
import coffee.repo.jpa.YeekeeGroupPrizeRepo;
import coffee.repo.jpa.YeekeePrizeSeqMappingRepo;
import coffee.repo.jpa.YeekeeSubmitNumberRepo;
import coffee.repo.jpa.YeekeeSumNumberRepo;
import coffee.yeekeeResult.vo.req.YeekeeResultReq;
import coffee.yeekeeResult.vo.res.YeekeeResultRoundSeqRes;
import framework.utils.ConvertDateUtils;
import framework.utils.UserLoginUtil;

@Service
public class YeekeeResultService {

	@Autowired
	LottoClassService lottoClassService;

	@Autowired
	YeekeeResultDao yeekeeResultDao;

	@Autowired
	YeekeeGroupPrizeRepo yeekeeGroupPrizeRepo;

	@Autowired
	MsdLottoKindRepository msdLottoKindRepository;

	@Autowired
	YeekeeSubmitNumberRepo yeekeeSubmitNumberRepo;

	@Autowired
	YeekeeSumNumberRepo yeekeeSumNumberRepo;

	@Autowired
	LottoTransactionRepo lottoTransactionRepo;

	@Autowired
	YeekeePrizeSeqMappingRepo yeekeePrizeSeqMappingRepo;

	@Autowired
	private LastListYeekeeRedisRepo lastListYeekeeRedisRepo;

	@Autowired
	private TimeSellRepo timeSellRepo;

	@Autowired
	private YeekeeResultManualService yeekeeResultManualService;

	@Value("${close-time-result-yeekee.min}")
	private Integer minToCloseYeekee;

	public List<YeekeeGroupPrize> getAllYeekeeResultByLottoCategoryCode(String lottoClassCode, Integer roundYeekee,
			String installment, String kindCode) {
		List<YeekeeGroupPrize> yeekeeGroupPrizeList = yeekeeGroupPrizeRepo
				.findByClassCodeAndRoundYeekeeAndInstallmentAndKindCodeOrderBySumPrizeDesc(lottoClassCode, roundYeekee,
						installment, kindCode);
		return yeekeeGroupPrizeList;
	}

	public Long getYeekeeNumberMinus16(String classCode, String installment, Integer roundNumber) {
		YeekeeSubmitNumber dataRes = new YeekeeSubmitNumber();
		dataRes = yeekeeResultDao.getYeekeeSubmitNumber(classCode, installment, roundNumber);
		if (dataRes == null) {
			return 0l;
		}
		return dataRes.getNumberSubmit();
	}

	public List<YeekeeResultRoundSeqRes> getYeekeeRoundSeqNewCheckBot(String classCode, String installment,
			Integer roundNumber) {
		List<YeekeePrizeSeqMapping> prizeSeqMapping = yeekeePrizeSeqMappingRepo.findByClassCode(classCode);
		List<YeekeeResultRoundSeqRes> resData = new ArrayList<YeekeeResultRoundSeqRes>();

		for (YeekeePrizeSeqMapping dataFor : prizeSeqMapping) {
			YeekeeSubmitNumber yeekeeResult = yeekeeResultDao.getYeekeeSubmitNumberSeqWin(classCode, installment,
					roundNumber, dataFor.getSeqOrder() - 1);

			if (yeekeeResult != null) {
				if (yeekeeResult.getIsBot()) {
					continue;
				}
				YeekeeResultRoundSeqRes dataSet = new YeekeeResultRoundSeqRes();
				dataSet.setCreatedDate(yeekeeResult.getCreatedAt().toString());
				dataSet.setUsername(yeekeeResult.getCreatedBy());
				dataSet.setSeqOrder(dataFor.getSeqOrder());
				dataSet.setNumberSubmit(yeekeeResult.getNumberSubmit());
				dataSet.setPrize(dataFor.getPrize());
				resData.add(dataSet);
			}

		}
		return resData;
	}

	public List<YeekeeResultRoundSeqRes> getYeekeeRoundSeqNew(String classCode, String installment,
			Integer roundNumber) {
		List<YeekeePrizeSeqMapping> prizeSeqMapping = yeekeePrizeSeqMappingRepo.findByClassCode(classCode);
		List<YeekeeResultRoundSeqRes> resData = new ArrayList<YeekeeResultRoundSeqRes>();

		for (YeekeePrizeSeqMapping dataFor : prizeSeqMapping) {
			YeekeeSubmitNumber yeekeeResult = yeekeeResultDao.getYeekeeSubmitNumberSeqWin(classCode, installment,
					roundNumber, dataFor.getSeqOrder() - 1);

			if (yeekeeResult != null) {
				YeekeeResultRoundSeqRes dataSet = new YeekeeResultRoundSeqRes();
				dataSet.setCreatedDate(yeekeeResult.getCreatedAt().toString());
				dataSet.setUsername(yeekeeResult.getCreatedBy());
				dataSet.setSeqOrder(dataFor.getSeqOrder());
				dataSet.setNumberSubmit(yeekeeResult.getNumberSubmit());
				dataSet.setPrize(dataFor.getPrize());
				resData.add(dataSet);
			}

		}
		return resData;
	}

	public List<YeekeeResultRoundSeqRes> getYeekeeRoundSeq(String classCode, String installment, Integer roundNumber) {
		YeekeeLastList lastRedis = lastListYeekeeRedisRepo.getLastList(classCode, installment, roundNumber);
		if (lastRedis != null) {
			return lastRedis.getListSeq();
		}
		return getYeekeeRoundSeqNew(classCode, installment, roundNumber);
	}

	public YeekeeLastList getCloseResult(SubmitYeeKeeReq req) {
		// TimeSell timeSell =
		// timeSellRepo.findByLottoClassCodeAndRoundYeekee(req.getClassCode(),
		// req.getRound());
		// Calendar timeSellCalendar = Calendar.getInstance();
		// timeSellCalendar.setTime(timeSell.getTimeClose());

		// Date date = ConvertDateUtils.parseStringToDate(req.getInstallment(),
		// ConvertDateUtils.DD_MM_YYYY,
		// ConvertDateUtils.LOCAL_EN);
		// Calendar calendar = Calendar.getInstance();
		// calendar.setTime(date);
		// calendar.set(Calendar.HOUR, timeSellCalendar.get(Calendar.HOUR));
		// calendar.set(Calendar.MINUTE, timeSellCalendar.get(Calendar.MINUTE));
		// calendar.add(Calendar.MINUTE, minToCloseYeekee);

		// Calendar thisTime = Calendar.getInstance();

		// if (thisTime.after(calendar)) {
		// calendar.add(Calendar.DAY_OF_MONTH, 1);
		// }

		// if (thisTime.after(calendar)) {
		// return null;
		// }
		YeekeeLastList listInCache = lastListYeekeeRedisRepo.getLastList(req.getClassCode(), req.getInstallment(),
				req.getRound());
		// if (listInCache == null) {
		// yeekeeResultManualService.updateCacheSync(req.getClassCode(),
		// req.getInstallment(), req.getRound());
		// listInCache = lastListYeekeeRedisRepo.getLastList(req.getClassCode(),
		// req.getInstallment(), req.getRound());
		// }
		return listInCache;
	}
}
