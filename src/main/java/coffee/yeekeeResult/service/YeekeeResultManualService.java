package coffee.yeekeeResult.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import coffee.buy.service.BuyYeekeeService;
import coffee.buy.vo.req.SubmitYeeKeeReq;
import coffee.buy.vo.res.YeeKeeNumberSubmitListRes;
import coffee.lottoresult.vo.req.thai.SendKeyBoReq;
import coffee.lottoresult.vo.req.thai.SendKeyBoReq.SendBoReq;
import coffee.model.LottoClass;
import coffee.model.LottoGroupTransaction;
import coffee.model.LottoTransaction;
import coffee.model.YeekeeSubmitNumber;
import coffee.model.YeekeeSumNumber;
import coffee.redis.model.YeekeeLastList;
import coffee.redis.repo.LastListYeekeeRedisRepo;
import coffee.redis.repo.UserBuyYeekeeRedisRepo;
import coffee.repo.dao.YeekeeResultDao;
import coffee.repo.jpa.LottoClassRepository;
import coffee.repo.jpa.LottoGroupTransactionRepo;
import coffee.repo.jpa.LottoTransactionRepo;
import coffee.repo.jpa.YeekeeSubmitNumberRepo;
import coffee.repo.jpa.YeekeeSumNumberRepo;
import coffee.yeekeeResult.vo.req.YeekeeResultReq;
import coffee.yeekeeResult.vo.res.YeekeeNumberRes;
import coffee.yeekeeResult.vo.res.YeekeeResultRoundSeqRes;
import framework.constant.LottoConstant.LOTTO_KIND;
import framework.constant.LottoConstant.LOTTO_STATUS;
import framework.constant.ProjectConstant;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE.SAVE;
import framework.model.ResponseData;
import framework.utils.ConvertDateUtils;
import framework.utils.HttpClientUtil;
import framework.utils.UserLoginUtil;

@Service
public class YeekeeResultManualService {

	@Autowired
	private YeekeeResultService resultService;

	@Autowired
	YeekeeSubmitNumberRepo yeekeeSubmitNumberRepo;

	@Autowired
	YeekeeSumNumberRepo yeekeeSumNumberRepo;

	@Autowired
	YeekeeResultDao yeekeeResultDao;

	@Autowired
	YeekeeResultService yeekeeResultService;

	@Autowired
	LottoTransactionRepo lottoTransactionRepo;

	@Autowired
	LottoGroupTransactionRepo lottoGroupTransactionRepo;

	@Autowired
	LottoClassRepository lottoClassRepository;

	@Autowired
	BuyYeekeeService buyYeekeeService;

	@Autowired
	private LastListYeekeeRedisRepo lastListYeekeeRedisRepo;

	@Value("${common-path.bo}")
	private String baseApiBo;

	@Transactional
	public String submitResultManual(YeekeeResultReq req) throws Exception {

		String botNumber = req.getDigitBot();
		String topNumber = req.getDigitTop();

		String changeNumber = req.getSumNumber().toString().substring(0, 2) + botNumber + topNumber;

		Long resultNumber = (new Long(changeNumber) + 100000) - req.getLottoResult();
		Long dummyNumber = new Long(resultNumber % 100000);

		YeekeeSumNumber dataFind2 = yeekeeSumNumberRepo.findByInstallmentAndClassCodeAndRoundNumberAndStatus(
				req.getInstallment(), req.getLottoClassCode(), req.getRoundNumber(), "CHECKING");

		if (dataFind2 == null) {
			YeekeeSumNumber dataFind3 = yeekeeSumNumberRepo.findByInstallmentAndClassCodeAndRoundNumberAndStatus(
					req.getInstallment(), req.getLottoClassCode(), req.getRoundNumber(), "SUCCESS");
			if (dataFind3 != null) {
				return "HAS_APPROVE";
			}
		}

		BigDecimal newSumNumber = new BigDecimal(req.getSumNumber()).add(new BigDecimal(dummyNumber));

		YeekeeSubmitNumber numberSeq16 = yeekeeResultDao.getYeekeeSubmitNumber(req.getLottoClassCode(),
				req.getInstallment(), req.getRoundNumber());

		if (numberSeq16 == null) {
			return "HAS_REJECT";
		}
		if (numberSeq16.getIsBot() && numberSeq16.getNumberSubmit().equals(0l)) {
			YeekeeSubmitNumber dataFind = yeekeeSubmitNumberRepo
					.findFirst1ByInstallmentAndClassCodeAndRoundNumberAndNumberSubmitAndIsBotAndSeqOrderNot(
							req.getInstallment(), req.getLottoClassCode(), req.getRoundNumber(), new Long(0), true,
							numberSeq16.getSeqOrder());

			if (dataFind != null) {
				dataFind.setNumberSubmit(new Long(dummyNumber));
				yeekeeSubmitNumberRepo.save(dataFind);
			}
		} else {
			YeekeeSubmitNumber dataFind = yeekeeSubmitNumberRepo
					.findFirst1ByInstallmentAndClassCodeAndRoundNumberAndNumberSubmitAndIsBot(req.getInstallment(),
							req.getLottoClassCode(), req.getRoundNumber(), new Long(0), true);

			if (dataFind != null) {
				dataFind.setNumberSubmit(new Long(dummyNumber));
				yeekeeSubmitNumberRepo.save(dataFind);
			}
		}

		Long numberResult = newSumNumber.longValue() - numberSeq16.getNumberSubmit().longValue();

		dataFind2.setSumNumber(newSumNumber);
		dataFind2.setNumberSeq16(new BigDecimal(numberSeq16.getNumberSubmit()));
		dataFind2.setNumberResult(new BigDecimal(numberResult));
		dataFind2.setUpdatedBy(UserLoginUtil.getUsername());
		dataFind2.setUpdatedAt(new Date());
		dataFind2.setStatus("SUCCESS");
		dataFind2.setChangeResult(true);

		yeekeeSumNumberRepo.save(dataFind2);

		String lottoNumber = StringUtils.leftPad(numberResult.toString(), 5, "0");

		lottoNumber = lottoNumber.substring(lottoNumber.length() - 5, lottoNumber.length());
		System.out.println(lottoNumber);
		List<YeekeeNumberRes> dataNumber = setResultNumber(lottoNumber);

		for (YeekeeNumberRes dataInfo : dataNumber) {
			for (String num : dataInfo.getNumber()) {
				yeekeeResultDao.updateTransaction(LOTTO_STATUS.WIN, dataInfo.getCollectNumber(), new Date(),
						LOTTO_STATUS.PENDING, num, req.getLottoClassCode(), dataInfo.getKindCode(),
						req.getRoundNumber());
			}
			yeekeeResultDao.updateTransactionLike(LOTTO_STATUS.LOSE, dataInfo.getCollectNumber(), new Date(),
					LOTTO_STATUS.PENDING, req.getLottoClassCode(), dataInfo.getKindCode(), req.getRoundNumber());
			yeekeeResultDao.updateGroupTransaction(LOTTO_STATUS.SHOW, LOTTO_STATUS.PENDING, req.getLottoClassCode(),
					req.getRoundNumber());
		}

		List<LottoTransaction> dataUpdate = lottoTransactionRepo.findTransactionYeekee(req.getInstallment(),
				req.getLottoClassCode(), req.getRoundNumber());

		List<SendBoReq> sendBoReq = new ArrayList<SendKeyBoReq.SendBoReq>();
		SendBoReq dataSet = new SendBoReq();

		for (LottoTransaction dataFor : dataUpdate) {
			dataSet.setUsername(dataFor.getUsername());
			dataSet.setPrize(String.valueOf(dataFor.getPrizeCorrect()));
			sendBoReq.add(dataSet);
		}

		List<YeekeeResultRoundSeqRes> prizeSeq = yeekeeResultService
				.getYeekeeRoundSeqNewCheckBot(req.getLottoClassCode(), req.getInstallment(), req.getRoundNumber());
		System.out.println(prizeSeq);

		Map<String, YeekeeResultRoundSeqRes> prizeMap = new HashMap<String, YeekeeResultRoundSeqRes>();

		for (YeekeeResultRoundSeqRes dataMap : prizeSeq) {
			prizeMap.put(dataMap.getUsername(), dataMap);
		}

		LottoClass lottoClass = lottoClassRepository.findByLottoClassCode(req.getLottoClassCode());
		for (Entry<String, YeekeeResultRoundSeqRes> dataInfo : prizeMap.entrySet()) {
			BigDecimal dataGroup = lottoGroupTransactionRepo.sumBetGroupTransactionYeekee(req.getLottoClassCode(),
					dataInfo.getValue().getUsername(), req.getInstallment(), req.getRoundNumber());
			System.out.println(dataGroup);
			if (dataGroup.compareTo(lottoClass.getHasBet()) >= 0) {
				String transactionGroupCode = buyYeekeeService.generateGroupTransaction(
						lottoClass.getPrefixTransNumber(), ConvertDateUtils.parseStringToDate(req.getInstallment(),
								ConvertDateUtils.DD_MM_YYYY, ConvertDateUtils.LOCAL_EN),
						req.getRoundNumber());
				LottoGroupTransaction dataGroupTrans = new LottoGroupTransaction();

				dataGroupTrans.setUsername(dataInfo.getValue().getUsername());
				dataGroupTrans.setLottoClassCode(req.getLottoClassCode());
				dataGroupTrans.setInstallment(req.getInstallment());
				dataGroupTrans.setRoundYeekee(req.getRoundNumber());
				dataGroupTrans.setSumGroupPrize(dataInfo.getValue().getPrize());
				dataGroupTrans.setSumGroupBet(BigDecimal.ZERO);
				dataGroupTrans.setCreatedBy("system");
				dataGroupTrans.setCreatedAt(new Date());
				dataGroupTrans.setLottoGroupTransactionCode(transactionGroupCode);
				dataGroupTrans.setStatus(LOTTO_STATUS.SEQWIN);
				dataGroupTrans.setRemark(dataInfo.getValue().getSeqOrder().toString());
				lottoGroupTransactionRepo.save(dataGroupTrans);

				dataSet.setUsername(dataInfo.getValue().getUsername());
				dataSet.setPrize(String.valueOf(dataInfo.getValue().getPrize()));
				sendBoReq.add(dataSet);
			}
		}

		String sig = "";
		SendKeyBoReq body = new SendKeyBoReq(sig, sendBoReq);
		RestTemplate restTemplate = HttpClientUtil.getInstant();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<SendKeyBoReq> entity = new HttpEntity<>(body, headers);
		ResponseData<String> dataResStr = restTemplate
				.postForObject(baseApiBo + "/api/lotto-provider/update-wallet-prize", entity, ResponseData.class);

		int count = 0;
		while (!ProjectConstant.STATUS.SUCCESS.equals(dataResStr.getData())) {
			Thread.sleep(1000);
			dataResStr = restTemplate.postForObject(baseApiBo + "/api/lotto-provider/update-wallet-prize", entity,
					ResponseData.class);
			count++;
			if (count > 3) {
				break;
			}
		}
		if (!ProjectConstant.STATUS.SUCCESS.equals(dataResStr.getData())) {
			new Throwable("Update Money Fail");
		}
		updateCache(req.getLottoClassCode(), req.getInstallment(), req.getRoundNumber());
		return SAVE.SUCCESS;
	}

	public List<YeekeeNumberRes> setResultNumber(String lottoNumber) {
		List<YeekeeNumberRes> dataRes = new ArrayList<YeekeeNumberRes>();
		String digit3Top = lottoNumber.substring(lottoNumber.length() - 3, lottoNumber.length());
		String digit2Top = lottoNumber.substring(lottoNumber.length() - 2, lottoNumber.length());
		String digit2Bot = lottoNumber.substring(0, 2);

		List<String> number1 = new ArrayList<String>();
		number1.add(digit3Top);
		YeekeeNumberRes digit3topSet = new YeekeeNumberRes();
		digit3topSet.setCollectNumber(digit3Top);
		digit3topSet.setKindCode(LOTTO_KIND.DIGIT3_TOP);
		digit3topSet.setNumber(number1);

		List<String> number2 = new ArrayList<String>();
		number2.add(digit2Top);
		YeekeeNumberRes digit2TopSet = new YeekeeNumberRes();
		digit2TopSet.setCollectNumber(digit2Top);
		digit2TopSet.setKindCode(LOTTO_KIND.DIGIT2_TOP);
		digit2TopSet.setNumber(number2);

		List<String> number3 = new ArrayList<String>();
		number3.add(digit2Bot);
		YeekeeNumberRes digit2BotSet = new YeekeeNumberRes();
		digit2BotSet.setCollectNumber(digit2Bot);
		digit2BotSet.setKindCode(LOTTO_KIND.DIGIT2_BOT);
		digit2BotSet.setNumber(number3);

		HashSet<String> digit3SwapList = new HashSet<String>();
		String digiSwap1 = digit3Top;
		String digiSwap2 = "" + digit3Top.charAt(0) + digit3Top.charAt(2) + digit3Top.charAt(1);
		String digiSwap3 = "" + digit3Top.charAt(1) + digit3Top.charAt(0) + digit3Top.charAt(2);
		String digiSwap4 = "" + digit3Top.charAt(1) + digit3Top.charAt(2) + digit3Top.charAt(0);
		String digiSwap5 = "" + digit3Top.charAt(2) + digit3Top.charAt(1) + digit3Top.charAt(0);
		String digiSwap6 = "" + digit3Top.charAt(2) + digit3Top.charAt(0) + digit3Top.charAt(1);

		HashSet<String> digit1List = new HashSet<String>();
		String digit1List1 = "" + digit3Top.charAt(0);
		String digit1List2 = "" + digit3Top.charAt(1);
		String digit1List3 = "" + digit3Top.charAt(2);

		HashSet<String> digit1BotList = new HashSet<String>();
		String digit1BotList1 = "" + digit2Bot.charAt(0);
		String digit1BotList2 = "" + digit2Bot.charAt(1);

		digit3SwapList.add(digiSwap1);
		digit3SwapList.add(digiSwap2);
		digit3SwapList.add(digiSwap3);
		digit3SwapList.add(digiSwap4);
		digit3SwapList.add(digiSwap5);
		digit3SwapList.add(digiSwap6);

		digit1List.add(digit1List1);
		digit1List.add(digit1List2);
		digit1List.add(digit1List3);

		digit1BotList.add(digit1BotList1);
		digit1BotList.add(digit1BotList2);

		List<String> listDigit3Swap = new ArrayList<String>(digit3SwapList);
		List<String> listDigit1Top = new ArrayList<String>(digit1List);
		List<String> listDigit1Bot = new ArrayList<String>(digit1BotList);

		YeekeeNumberRes digit3SwapSet = new YeekeeNumberRes();
		digit3SwapSet.setCollectNumber(digit3Top);
		digit3SwapSet.setKindCode(LOTTO_KIND.DIGIT3_SWAPPED);
		digit3SwapSet.setNumber(listDigit3Swap);

		YeekeeNumberRes digit1TopSet = new YeekeeNumberRes();
		digit1TopSet.setCollectNumber(digit3Top);
		digit1TopSet.setKindCode(LOTTO_KIND.DIGIT1_TOP);
		digit1TopSet.setNumber(listDigit1Top);

		YeekeeNumberRes digit1BotSet = new YeekeeNumberRes();
		digit1BotSet.setCollectNumber(digit2Bot);
		digit1BotSet.setKindCode(LOTTO_KIND.DIGIT1_BOT);
		digit1BotSet.setNumber(listDigit1Bot);

		dataRes.add(digit3topSet);
		dataRes.add(digit2TopSet);
		dataRes.add(digit2BotSet);
		dataRes.add(digit3SwapSet);
		dataRes.add(digit1TopSet);
		dataRes.add(digit1BotSet);

		return dataRes;
	}

	@Async
	private void updateCache(String classCode, String installment, int round) {
		updateCacheSync(classCode, installment, round);
	}

	public void updateCacheSync(String classCode, String installment, int round) {
		// get list seq
		List<YeekeeResultRoundSeqRes> list = resultService.getYeekeeRoundSeqNew(classCode, installment, round);

		// get all Submit
		List<YeekeeSubmitNumber> listFind = yeekeeSubmitNumberRepo
				.findByClassCodeAndInstallmentAndRoundNumberOrderByCreatedAtDesc(classCode, installment, round);
		List<YeeKeeNumberSubmitListRes> listAllSubmit = new ArrayList<YeeKeeNumberSubmitListRes>();

		YeeKeeNumberSubmitListRes dataSet;
		for (YeekeeSubmitNumber entity : listFind) {
			dataSet = new YeeKeeNumberSubmitListRes();
			dataSet.setCreatedDate(ConvertDateUtils.formatDateToString(entity.getCreatedAt(),
					ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
			dataSet.setNumberSubmit(String.valueOf(entity.getNumberSubmit()));
			dataSet.setOrder(entity.getSeqOrder());
			dataSet.setUsername(buyYeekeeService.maskUsername(entity.getCreatedBy()));
			listAllSubmit.add(dataSet);
		}

		SubmitYeeKeeReq req = new SubmitYeeKeeReq();
		req.setClassCode(classCode);
		req.setInstallment(installment);
		req.setRound(round);

		YeekeeSumNumber sumObjNumber = yeekeeSumNumberRepo.findByInstallmentAndClassCodeAndRoundNumber(installment,
				classCode, round);

		lastListYeekeeRedisRepo.setLastListDay(classCode, installment, round, sumObjNumber.getSumNumber(),
				listAllSubmit, list, sumObjNumber.getNumberResult(), sumObjNumber.getNumberSeq16());
	}

	public YeekeeLastList updateCacheRes(String classCode, String installment, int round) {
		updateCacheSync(classCode, installment, round);
		return lastListYeekeeRedisRepo.getLastList(classCode, installment, round);
	}
}