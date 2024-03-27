package coffee.lottoresult.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import coffee.lottoresult.vo.req.ConfirmResultReq;
import coffee.lottoresult.vo.req.thai.SendKeyBoReq;
import coffee.lottoresult.vo.req.thai.SendKeyBoReq.SendBoReq;
import coffee.lottoresult.vo.req.thai.SubmitManualReq;
import coffee.lottoresult.vo.res.thai.LottoAllResultRes;
import coffee.lottoresult.vo.res.thai.LottoAllResultRes.DigiList;
import coffee.lottoresult.vo.res.thai.LottoResultByClassRes;
import coffee.lottoresult.vo.res.thai.LottoResultByClassRes.DigiListType;
import coffee.lottoresult.vo.res.thai.LottoResultCategoryRes;
import coffee.lottoresult.vo.res.thai.LottoResultRoundYeekeeRes;
import coffee.lottoresult.vo.res.thai.LottoResultYeekeeByClassRes;
import coffee.lottoresult.vo.res.thai.LottoYeekeeResultRes;
import coffee.lottoresult.vo.res.thai.LottoYeekeeResultRes.DigiListYeekee;
import coffee.lottoresult.vo.res.thai.lottoResultStocksRes.Stocks;
import coffee.lottoresult.vo.res.thai.ResultAllRes;
import coffee.lottoresult.vo.res.thai.ResultAllYeekeeRes;
import coffee.lottoresult.vo.res.thai.lottoResultStocksRes;
import coffee.masterdata.service.LottoClassService;
import coffee.masterdata.service.MsdLottoKindService;
import coffee.model.LottoClass;
import coffee.model.LottoResult;
import coffee.model.MsdLottoKind;
import coffee.model.TimeSell;
import coffee.model.YeekeeSumNumber;
import coffee.repo.dao.LottoResultDao;
import coffee.repo.dao.LottoTransactionDao;
import coffee.repo.jpa.LottoResultRepo;
import coffee.repo.jpa.LottoTransactionRepo;
import coffee.repo.jpa.MsdLottoKindRepository;
import coffee.repo.jpa.TimeSellRepo;
import coffee.repo.jpa.YeekeeSumNumberRepo;
import framework.constant.LottoConstant.CATEGORY;
import framework.constant.LottoConstant.LOTTO_KIND;
import framework.constant.LottoConstant.LOTTO_STATUS;
import framework.constant.ProjectConstant;
import framework.model.ResponseData;
import framework.utils.HttpClientUtil;
import framework.utils.UserLoginUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j

public class LottoResultService {

	@Autowired
	private LottoResultRepo lottoResultRepo;

	@Autowired
	private MsdLottoKindRepository msdLottoKindRepository;

	@Autowired
	private LottoResultDao lottoresultDao;

	@Autowired
	private SyncApiKeyThaiGovService syncApiKeyThaiGovService;

	@Autowired
	private UpdateTransactionService updateTransactionService;

	@Autowired
	private LottoTransactionDao lottoTransactionDao;

	@Autowired
	private LottoTransactionRepo lottoTransactionRepo;

	@Autowired
	MsdLottoKindService msdLottoKindService;

	@Autowired
	private LottoClassService lottoClassService;

	@Autowired
	private YeekeeSumNumberRepo yeekeeSumNumberRepo;

	@Autowired
	private TimeSellRepo timeSellRepo;

	@Value("${common-path.bo}")
	private String baseApiBo;

	public String syncAllLottoResult(String lottoClassCode) throws KeyManagementException, NoSuchAlgorithmException {
		log.info("Check LottoClassCode is : " + lottoClassCode);
		if (lottoClassCode != null) {
			if ("THAI".equals(lottoClassCode)) {
				return syncApiKeyThaiGovService.syncAllLottoResult();
			} else {
				return "NOT_HAS_CLASS";
			}
		} else {
			return ProjectConstant.STATUS.FAILED;
		}
	}

	public String submitManualResultLotto(SubmitManualReq req) {
		log.info("Check LottoClassCode is : " + req.getClassCode());
		if (req.getClassCode() != null) {
			return syncApiKeyThaiGovService.submitManualResultLotto(req);
		} else {
			return ProjectConstant.STATUS.FAILED;
		}
	}

	public LottoAllResultRes getLottoResult(String lottoClassCode) {
		List<LottoResult> dataRes = lottoResultRepo.findByLottoClassCode(lottoClassCode);
		LottoAllResultRes dataResp = new LottoAllResultRes();
		List<DigiList> digiList1 = new ArrayList<DigiList>();
		List<DigiList> digiList2 = new ArrayList<DigiList>();
		List<DigiList> digiList3 = new ArrayList<DigiList>();
		List<DigiList> digiList4 = new ArrayList<DigiList>();
		List<DigiList> digiList5 = new ArrayList<DigiList>();
		List<DigiList> digiList6 = new ArrayList<DigiList>();
		List<DigiList> digiList7 = new ArrayList<DigiList>();
		List<DigiList> digiList8 = new ArrayList<DigiList>();

		List<String> digi1List = new ArrayList<String>();
		List<String> digi2List = new ArrayList<String>();
		List<String> digi3List = new ArrayList<String>();
		List<String> digi4List = new ArrayList<String>();
		List<String> digi5List = new ArrayList<String>();
		List<String> digi6List = new ArrayList<String>();
		List<String> digi7List = new ArrayList<String>();
		List<String> digi8List = new ArrayList<String>();

		for (LottoResult dataSet : dataRes) {
			if (dataSet.getMsdLottoKindCode().equals(LOTTO_KIND.DIGIT3_TOP)) {
				MsdLottoKind dataKindCode = msdLottoKindRepository.findByMsdLottoKindCode(LOTTO_KIND.DIGIT3_TOP);

				DigiList digi3top = new DigiList();
				digi1List.add(dataSet.getLottoNumber());
				String result = String.join(",", digi1List);
				digi3top.setLottoNumber(result);
				digi3top.setLottoNumber(dataSet.getLottoNumber());
				digi3top.setMsdlottoKindCode(dataSet.getMsdLottoKindCode());
				digi3top.setMsdlottoKindName(dataKindCode.getMsdLottoKindName());
				digiList1.add(digi3top);
				dataResp.setDigi3Top(digiList1);
			}
			if (dataSet.getMsdLottoKindCode().equals(LOTTO_KIND.DIGIT3_SWAPPED)) {
				MsdLottoKind dataKindCode = msdLottoKindRepository.findByMsdLottoKindCode(LOTTO_KIND.DIGIT3_SWAPPED);
				DigiList digiSwap = new DigiList();
				digi2List.add(dataSet.getLottoNumber());
				String result = String.join(",", digi2List);
				digiSwap.setLottoNumber(dataSet.getLottoNumber());
				digiSwap.setMsdlottoKindCode(dataSet.getMsdLottoKindCode());
				digiSwap.setMsdlottoKindName(dataKindCode.getMsdLottoKindName());
				digiList2.add(digiSwap);
				digiSwap.setLottoNumber(result);
				dataResp.setDigiSwap(digiList2);
			}
			if (dataSet.getMsdLottoKindCode().equals(LOTTO_KIND.DIGIT1_TOP)) {
				MsdLottoKind dataKindCode = msdLottoKindRepository.findByMsdLottoKindCode(LOTTO_KIND.DIGIT1_TOP);
				DigiList digi1Top = new DigiList();
				digi3List.add(dataSet.getLottoNumber());
				String result = String.join(",", digi3List);
				digi1Top.setLottoNumber(result);
				digi1Top.setLottoNumber(dataSet.getLottoNumber());
				digi1Top.setMsdlottoKindCode(dataSet.getMsdLottoKindCode());
				digi1Top.setMsdlottoKindName(dataKindCode.getMsdLottoKindName());
				digiList3.add(digi1Top);
				dataResp.setDigi1Top(digiList3);
			}
			if (dataSet.getMsdLottoKindCode().equals(LOTTO_KIND.DIGIT2_TOP)) {
				MsdLottoKind dataKindCode = msdLottoKindRepository.findByMsdLottoKindCode(LOTTO_KIND.DIGIT2_TOP);
				DigiList digi2Top = new DigiList();
				digi4List.add(dataSet.getLottoNumber());
				String result = String.join(",", digi4List);
				digi2Top.setLottoNumber(result);
				digi2Top.setLottoNumber(dataSet.getLottoNumber());
				digi2Top.setMsdlottoKindCode(dataSet.getMsdLottoKindCode());
				digi2Top.setMsdlottoKindName(dataKindCode.getMsdLottoKindName());
				digiList4.add(digi2Top);
				dataResp.setDigi2Top(digiList4);
			}
			if (dataSet.getMsdLottoKindCode().equals(LOTTO_KIND.DIGIT2_BOT)) {
				MsdLottoKind dataKindCode = msdLottoKindRepository.findByMsdLottoKindCode(LOTTO_KIND.DIGIT2_BOT);
				DigiList digi2Bot = new DigiList();
				digi5List.add(dataSet.getLottoNumber());
				String result = String.join(",", digi5List);
				digi2Bot.setLottoNumber(result);
				digi2Bot.setLottoNumber(dataSet.getLottoNumber());
				digi2Bot.setMsdlottoKindCode(dataSet.getMsdLottoKindCode());
				digi2Bot.setMsdlottoKindName(dataKindCode.getMsdLottoKindName());
				digiList5.add(digi2Bot);
				dataResp.setDigi2Bot(digiList5);
			}
			if (dataSet.getMsdLottoKindCode().equals(LOTTO_KIND.DIGIT3_FRONT)) {
				MsdLottoKind dataKindCode = msdLottoKindRepository.findByMsdLottoKindCode(LOTTO_KIND.DIGIT3_FRONT);
				DigiList digi3Front = new DigiList();
				digi6List.add(dataSet.getLottoNumber());
				String result = String.join(",", digi6List);
				digi3Front.setLottoNumber(result);
				digi3Front.setLottoNumber(dataSet.getLottoNumber());
				digi3Front.setMsdlottoKindCode(dataSet.getMsdLottoKindCode());
				digi3Front.setMsdlottoKindName(dataKindCode.getMsdLottoKindName());
				digiList6.add(digi3Front);
				dataResp.setDigi3Front(digiList6);
			}
			if (dataSet.getMsdLottoKindCode().equals(LOTTO_KIND.DIGIT3_BOT)) {
				MsdLottoKind dataKindCode = msdLottoKindRepository.findByMsdLottoKindCode(LOTTO_KIND.DIGIT3_BOT);
				DigiList digi3Bot = new DigiList();
				digi7List.add(dataSet.getLottoNumber());
				String result = String.join(",", digi7List);
				digi3Bot.setLottoNumber(result);
				digi3Bot.setLottoNumber(dataSet.getLottoNumber());
				digi3Bot.setMsdlottoKindCode(dataSet.getMsdLottoKindCode());
				digi3Bot.setMsdlottoKindName(dataKindCode.getMsdLottoKindName());
				digiList7.add(digi3Bot);
				dataResp.setDigi3Bot(digiList7);
			}
			if (dataSet.getMsdLottoKindCode().equals(LOTTO_KIND.DIGIT1_BOT)) {
				MsdLottoKind dataKindCode = msdLottoKindRepository.findByMsdLottoKindCode(LOTTO_KIND.DIGIT1_BOT);
				DigiList digi1Bot = new DigiList();
				digi8List.add(dataSet.getLottoNumber());
				String result = String.join(",", digi8List);
				digi1Bot.setLottoNumber(result);
				digi1Bot.setLottoNumber(dataSet.getLottoNumber());
				digi1Bot.setMsdlottoKindCode(dataSet.getMsdLottoKindCode());
				digi1Bot.setMsdlottoKindName(dataKindCode.getMsdLottoKindName());
				digiList8.add(digi1Bot);
				dataResp.setDigi1Bot(digiList8);
			}

		}

		return dataResp;
	}

	public List<LottoResult> getAllLottoInstallment(String lottoClassCode) {
		List<LottoResult> dataRes = lottoresultDao.getLottoInstallmentByClassCode(lottoClassCode);

		return dataRes;
	}

	public List<LottoResult> getAllLottoResult(String codeGroup) {
		List<LottoResult> dataResp = lottoResultRepo.findByCodeGroup(codeGroup);
		// List<LottoResult> dataRes =
		// lottoResultRepo.findByLottoClassCode(lottoClassCode);
		return dataResp;
	}

	@Transactional
	public void submitAll(ConfirmResultReq req)
			throws InterruptedException, KeyManagementException, NoSuchAlgorithmException {
		Date setDate = new Date();
		if (req.getStatus()) {
			List<SendBoReq> sendBoReq = lottoTransactionDao.findSum2(req.getKindCode(), req.getClassCode(),
					req.getCategoryCode(), req.getUsername(), req.getTransactionCode());

			String sig = updateTransactionService.genarateSignature(sendBoReq);
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

			/**
			 * update Wallet column
			 */
			if (ProjectConstant.STATUS.SUCCESS.equals(dataResStr.getData())) {
				lottoTransactionDao.updateTransactionDb2(true, null, setDate, UserLoginUtil.getUsername(),
						req.getKindCode(), req.getClassCode(), req.getCategoryCode(), req.getUsername(),
						req.getTransactionCode());
			}
		} else {
			lottoTransactionDao.updateTransactionDb2(false, req.getRemark(), setDate, UserLoginUtil.getUsername(),
					req.getKindCode(), req.getClassCode(), req.getCategoryCode(), req.getUsername(),
					req.getTransactionCode());
		}
	}

	@Transactional
	public void submitSelect(ConfirmResultReq req)
			throws InterruptedException, KeyManagementException, NoSuchAlgorithmException {
		Date setDate = new Date();
		if (req.getStatus()) {
			for (String listIds : req.getTransactionIdList()) {

				List<SendBoReq> sendBoReq = lottoTransactionDao.findSum3(req.getKindCode(), req.getClassCode(),
						req.getCategoryCode(), req.getUsername(), req.getTransactionCode(), listIds);
				String sig = updateTransactionService.genarateSignature(sendBoReq);
				SendKeyBoReq body = new SendKeyBoReq(sig, sendBoReq);

				RestTemplate restTemplate = HttpClientUtil.getInstant();
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				HttpEntity<SendKeyBoReq> entity = new HttpEntity<>(body, headers);
				ResponseData<String> dataResStr = restTemplate.postForObject(
						baseApiBo + "/api/lotto-provider/update-wallet-prize", entity, ResponseData.class);

				int count = 0;
				while (!ProjectConstant.STATUS.SUCCESS.equals(dataResStr.getData())) {
					Thread.sleep(1000);
					dataResStr = restTemplate.postForObject(baseApiBo + "/api/lotto-provider/update-wallet-prize",
							entity, ResponseData.class);
					count++;
					if (count > 3) {
						break;
					}
				}
				/**
				 * update Wallet column
				 */
				if (ProjectConstant.STATUS.SUCCESS.equals(dataResStr.getData())) {
					lottoTransactionDao.updateTransactionDbByListId(true, null, setDate, UserLoginUtil.getUsername(),
							req.getKindCode(), req.getClassCode(), req.getCategoryCode(), req.getUsername(),
							req.getTransactionCode(), listIds);
				}
			}
		} else {
			for (String listIds : req.getTransactionIdList()) {
				lottoTransactionDao.updateTransactionDbByListId(false, req.getRemark(), setDate,
						UserLoginUtil.getUsername(), req.getKindCode(), req.getClassCode(), req.getCategoryCode(),
						req.getUsername(), req.getTransactionCode(), listIds);
			}
		}

	}

	@Transactional
	public void submitSelectById(ConfirmResultReq req)
			throws InterruptedException, KeyManagementException, NoSuchAlgorithmException {
		Date setDate = new Date();
		if (req.getStatus()) {

			List<SendBoReq> sendBoReq = lottoTransactionDao.findSum3(req.getKindCode(), req.getClassCode(),
					req.getCategoryCode(), req.getUsername(), req.getTransactionCode(), req.getTransactionId());
			String sig = updateTransactionService.genarateSignature(sendBoReq);
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
			/**
			 * update Wallet column
			 */
			if (ProjectConstant.STATUS.SUCCESS.equals(dataResStr.getData())) {
				lottoTransactionDao.updateTransactionDbByOneId(true, null, setDate, UserLoginUtil.getUsername(),
						req.getKindCode(), req.getClassCode(), req.getCategoryCode(), req.getUsername(),
						req.getTransactionCode(), req.getTransactionId());
			}
		} else {
			lottoTransactionDao.updateTransactionDbByOneId(false, req.getRemark(), setDate, UserLoginUtil.getUsername(),
					req.getKindCode(), req.getClassCode(), req.getCategoryCode(), req.getUsername(),
					req.getTransactionCode(), req.getTransactionId());
		}

	}

	@Transactional
	public void submitDeSelectId(ConfirmResultReq req)
			throws InterruptedException, KeyManagementException, NoSuchAlgorithmException {
		Date setDate = new Date();
		if (req.getStatus()) {

			List<SendBoReq> sendBoReq = lottoTransactionDao.findSum4(req.getKindCode(), req.getClassCode(),
					req.getCategoryCode(), req.getUsername(), req.getTransactionCode(), req.getDetransactionIdList());
			String sig = updateTransactionService.genarateSignature(sendBoReq);
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
			/**
			 * update Wallet column
			 */
			if (ProjectConstant.STATUS.SUCCESS.equals(dataResStr.getData())) {
				lottoTransactionDao.updateTransactionDbByDeSelectListId(true, null, setDate,
						UserLoginUtil.getUsername(), req.getKindCode(), req.getClassCode(), req.getCategoryCode(),
						req.getUsername(), req.getTransactionCode(), req.getDetransactionIdList());
			}
		} else {
			lottoTransactionDao.updateTransactionDbByDeSelectListId(false, req.getRemark(), setDate,
					UserLoginUtil.getUsername(), req.getKindCode(), req.getClassCode(), req.getCategoryCode(),
					req.getUsername(), req.getTransactionCode(), req.getDetransactionIdList());
		}

	}

	public List<ResultAllRes> getAllLottoResultByDate(String installment) {
		List<MsdLottoKind> msdList = msdLottoKindService.getAllMsd();
		List<LottoClass> lottoClassListX = lottoClassService.getByLottoCategoryCode(CATEGORY.GOVERNMENT);
		// lottoClassListX.addAll(lottoClassService.getByLottoCategoryCode(CATEGORY.STOCKS));
		List<ResultAllRes> dataListRes = new ArrayList<ResultAllRes>();
		for (LottoClass lottoClass : lottoClassListX) {
			List<LottoResult> lottoResult = new ArrayList<LottoResult>();

			lottoResult = lottoresultDao.getLottoResultByInstallment(installment, lottoClass.getLottoClassCode());

			ResultAllRes res = new ResultAllRes();
			res.setInstallment(installment);
			res.setLottoClassCode(lottoClass.getLottoClassCode());
			res.setLottoClassName(lottoClass.getClassName());
			res.setLottoCategoryCode(lottoClass.getLottoCategoryCode());
			res.setLottoClassImg(lottoClass.getLottoClassImg());
			dataListRes.add(res);
		}

		for (ResultAllRes item : dataListRes) {
			List<LottoResult> lottoResult = new ArrayList<LottoResult>();
			lottoResult = lottoresultDao.getLottoResultByInstallment(installment, item.getLottoClassCode());
			for (LottoResult dataSet : lottoResult) {
				if (item.getLottoClassCode().contentEquals(dataSet.getLottoClassCode())) {
					if (LOTTO_KIND.DIGIT3_FRONT.equals(dataSet.getMsdLottoKindCode())) {
						item.setDigit3Front(dataSet.getLottoNumber());
					}

					if (LOTTO_KIND.DIGIT3_TOP.equals(dataSet.getMsdLottoKindCode())) {
						item.setDigit3Top(dataSet.getLottoNumber());
					}
					if (LOTTO_KIND.DIGIT3_BOT.equals(dataSet.getMsdLottoKindCode())) {
						item.setDigit3Bot(dataSet.getLottoNumber());
					}
					if (LOTTO_KIND.DIGIT2_TOP.equals(dataSet.getMsdLottoKindCode())) {
						item.setDigit2Top(dataSet.getLottoNumber());
					}
					if (LOTTO_KIND.DIGIT2_BOT.equals(dataSet.getMsdLottoKindCode())) {
						item.setDigit2Bot(dataSet.getLottoNumber());
					}
				}
			}

		}

		return dataListRes;
	}

	public List<ResultAllYeekeeRes> getAllLottoResultByLottoCategoryCode(String lottoCategoryCode) {
		List<LottoClass> lottoClassList = lottoClassService.getByLottoCategoryCode(lottoCategoryCode);
		List<ResultAllYeekeeRes> dataListRes = new ArrayList<ResultAllYeekeeRes>();
		for (LottoClass lottoClass : lottoClassList) {
			List<LottoResult> lottoResult = new ArrayList<LottoResult>();
			ResultAllYeekeeRes res = new ResultAllYeekeeRes();
			lottoResult = lottoresultDao.getLottoResultByLottoCategoryCode(lottoCategoryCode);
			res.setLottoClassCode(lottoClass.getLottoClassCode());
			res.setLottoClassName(lottoClass.getClassName());
			res.setLottoCategoryCode(lottoClass.getLottoCategoryCode());
			res.setLottoClassImg(lottoClass.getLottoClassImg());
			List<DigiList> digiList = new ArrayList<DigiList>();
			for (LottoResult dataSet : lottoResult) {
				MsdLottoKind dataKindCode = msdLottoKindRepository
						.findByMsdLottoKindCode(dataSet.getMsdLottoKindCode());
				DigiList digit = new DigiList();
				digit.setLottoNumber(dataSet.getLottoNumber());
				digit.setMsdlottoKindCode(dataSet.getMsdLottoKindCode());
				digit.setMsdlottoKindName(dataKindCode.getMsdLottoKindName());
				digiList.add(digit);
			}
			res.setLottoList(digiList);
			dataListRes.add(res);
		}
		return dataListRes;
	}

	public List<LottoYeekeeResultRes> getAllLottoYeekeeResultByInstallment(String installment) {
		String tmp[] = installment.split("-");
		String transFormatInstallment = tmp[2] + "/" + tmp[1] + "/" + tmp[0];

		MsdLottoKind digit3TopKind = msdLottoKindRepository.findByMsdLottoKindCode(LOTTO_KIND.DIGIT3_TOP);
		MsdLottoKind digit2botKind = msdLottoKindRepository.findByMsdLottoKindCode(LOTTO_KIND.DIGIT2_BOT);

		List<LottoYeekeeResultRes> resData = new ArrayList<LottoYeekeeResultRes>();
		List<LottoClass> lottoClassList = lottoClassService.getByLottoCategoryCode(CATEGORY.YEEKEE);

		for (LottoClass lottoClass : lottoClassList) {

			LottoYeekeeResultRes res = new LottoYeekeeResultRes();
			List<YeekeeSumNumber> yeekeeSumNumberList = yeekeeSumNumberRepo
					.findByClassCodeAndInstallmentOrderByRoundNumberAsc(lottoClass.getLottoClassCode(),
							transFormatInstallment);
			TimeSell totalRound = timeSellRepo
					.findTop1ByLottoClassCodeOrderByRoundYeekeeDesc(lottoClass.getLottoClassCode());
			res.setInstallment(installment);
			res.setLottoClassImg(lottoClass.getLottoClassImg());
			res.setLottoClassCode(lottoClass.getLottoClassCode());
			res.setLottoClassName(lottoClass.getClassName());
			res.setLottoCategoryCode(lottoClass.getLottoCategoryCode());
			if (totalRound != null) {
				res.setTotalNumberRound(totalRound.getRoundYeekee());
			} else {
				res.setTotalNumberRound(0);
			}
			List<DigiListYeekee> digiList = new ArrayList<DigiListYeekee>();

			for (YeekeeSumNumber itemSumNumber : yeekeeSumNumberList) {

				DigiListYeekee digit = new DigiListYeekee();
				if (LOTTO_STATUS.SUCCESS.equals(itemSumNumber.getStatus())) {
					BigDecimal digit2bot = itemSumNumber.getSumNumber()
							.divide(new BigDecimal(1000), 0, RoundingMode.DOWN).remainder(new BigDecimal(100));
					BigDecimal digit3Top = itemSumNumber.getSumNumber().remainder(new BigDecimal(1000));
					digit.setDigit3TopLottoNumber(StringUtils.leftPad(digit3Top.toString(), 3, "0"));
					digit.setDigit3TopLottoKindCode(digit3TopKind.getMsdLottoKindCode());
					digit.setDigit3TopLottoKindName(digit3TopKind.getMsdLottoKindName());

					digit.setDigit2BotLottoNumber(StringUtils.leftPad(digit2bot.toString(), 2, "0"));
					digit.setDigit2BotLottoKindCode(digit2botKind.getMsdLottoKindCode());
					digit.setDigit2BotLottoKindName(digit2botKind.getMsdLottoKindName());
					digit.setRoundYeekee(itemSumNumber.getRoundNumber());
				} else {
					digit.setDigit3TopLottoNumber(null);
					digit.setDigit3TopLottoKindCode(digit3TopKind.getMsdLottoKindCode());
					digit.setDigit3TopLottoKindName(digit3TopKind.getMsdLottoKindName());

					digit.setDigit2BotLottoNumber(null);
					digit.setDigit2BotLottoKindCode(digit2botKind.getMsdLottoKindCode());
					digit.setDigit2BotLottoKindName(digit2botKind.getMsdLottoKindName());
					digit.setRoundYeekee(itemSumNumber.getRoundNumber());
				}
				digiList.add(digit);
			}
			res.setLottoList(digiList);
			resData.add(res);
		}
		return resData;
	}

	public List<LottoResultCategoryRes> getAllClassListLottoResult() {
		return lottoresultDao.getAllClassListLottoResult();
	}

	public List<LottoResultRoundYeekeeRes> getRoundYeekeeByClassCode(String lottoClassCode) {
		List<LottoResultRoundYeekeeRes> data = lottoresultDao.getRoundYeekeeByClassCode(lottoClassCode);
		return data;
	}

	// ================================================== type
	// =====================================================

	public List<LottoResultByClassRes> getAllLottoResultByLottoClassCode(String lottoClassCode) {
		LottoClass lottoClass = lottoClassService.getLottoClassByClassCode(lottoClassCode);
		List<LottoResultByClassRes> resData = new ArrayList<LottoResultByClassRes>();
		List<DigiListType> digiList = new ArrayList<DigiListType>();
		List<String> installmentList = lottoResultRepo.getInstallment();

		for (String installment : installmentList) {
			List<LottoResult> lottoResultByClassList = lottoresultDao.getLottoResultByClassCode(lottoClassCode,
					installment);
			System.out.println("****************> " + installment);
			LottoResultByClassRes res = new LottoResultByClassRes();
			res.setLottoClassCode(lottoClass.getLottoClassCode());
			res.setLottoClassName(lottoClass.getClassName());
			res.setLottoCategoryCode(lottoClass.getLottoCategoryCode());
			res.setLottoClassImg(lottoClass.getLottoClassImg());
			for (LottoResult lottoResult : lottoResultByClassList) {
				System.out.println("~~~~~~~~~~~~~~> " + lottoResult.getMsdLottoKindCode());

				if (LOTTO_KIND.DIGIT3_FRONT.equals(lottoResult.getMsdLottoKindCode())) {
					res.setDigit3Front(lottoResult.getLottoNumber());
				}

				if (LOTTO_KIND.DIGIT3_TOP.equals(lottoResult.getMsdLottoKindCode())) {
					res.setDigit3Top(lottoResult.getLottoNumber());
				}

				if (LOTTO_KIND.DIGIT3_BOT.equals(lottoResult.getMsdLottoKindCode())) {
					res.setDigit3Bot(lottoResult.getLottoNumber());
				}

				if (LOTTO_KIND.DIGIT2_BOT.equals(lottoResult.getMsdLottoKindCode())) {
					res.setDigit2Bot(lottoResult.getLottoNumber());
				}

			}
			res.setLottoResultInstallment(installment);
			resData.add(res);
		}

		return resData;
	}

	public List<LottoResultYeekeeByClassRes> getLottoResultYeekeeByRound(String lottoClassCode, Integer roundYeekee) {
		LottoClass lottoClass = lottoClassService.getLottoClassByClassCode(lottoClassCode);
		List<LottoResultYeekeeByClassRes> resData = new ArrayList<LottoResultYeekeeByClassRes>();

		List<YeekeeSumNumber> yeekeeSumNumberList = yeekeeSumNumberRepo
				.findByClassCodeAndRounYeekeeOrderByRoundNumberAsc(lottoClassCode, roundYeekee);

		for (YeekeeSumNumber itemSumNumber : yeekeeSumNumberList) {
			LottoResultYeekeeByClassRes res = new LottoResultYeekeeByClassRes();
			res.setLottoClassCode(lottoClass.getLottoClassCode());
			res.setLottoClassName(lottoClass.getClassName());
			res.setLottoCategoryCode(lottoClass.getLottoCategoryCode());
			res.setLottoClassImg(lottoClass.getLottoClassImg());
			String tmp[] = itemSumNumber.getInstallment().split("/");
			String transFormatInstallment = tmp[2] + "-" + tmp[1] + "-" + tmp[0];

			if (LOTTO_STATUS.SUCCESS.equals(itemSumNumber.getStatus())) {
				BigDecimal digit2bot = itemSumNumber.getNumberResult()
						.divide(new BigDecimal(1000), 0, RoundingMode.DOWN).remainder(new BigDecimal(100));
				BigDecimal digit3Top = itemSumNumber.getNumberResult().remainder(new BigDecimal(1000));

				res.setDigit2Bot(StringUtils.leftPad(digit2bot.toString(), 2, "0"));
				res.setDigit3Top(StringUtils.leftPad(digit3Top.toString(), 3, "0"));
				res.setRoundYeekee(itemSumNumber.getRoundNumber());
				res.setInstallment(transFormatInstallment);
			} else {

				res.setDigit2Bot(null);
				res.setDigit3Top(null);
				res.setRoundYeekee(itemSumNumber.getRoundNumber());
				res.setInstallment(transFormatInstallment);
			}
			resData.add(res);
		}

		return resData;
	}

	// ================================================== type
	// =====================================================

	// ================================================== lotto stocks
	// =====================================================

	public List<lottoResultStocksRes> getAllStock(String installment) {
		List<LottoClass> lottoClassList = lottoClassService.getByLottoCategoryCode(CATEGORY.STOCKS);
		List<lottoResultStocksRes> categoryName = lottoresultDao.getCategory(CATEGORY.STOCKS);
		// MsdLottoKind digit3TopKind =
		// msdLottoKindRepository.findByMsdLottoKindCode(LOTTO_KIND.DIGIT3_TOP);
		// MsdLottoKind digit2botKind =
		// msdLottoKindRepository.findByMsdLottoKindCode(LOTTO_KIND.DIGIT2_BOT);
		lottoResultStocksRes data = new lottoResultStocksRes();
		data.setInstallment(installment);
		data.setLottoCategoryCode(CATEGORY.STOCKS);
		data.setLottoCategoryName(categoryName.get(0).getLottoCategoryName());

		List<lottoResultStocksRes> resData = new ArrayList<lottoResultStocksRes>();
		List<Stocks> stockList = new ArrayList<Stocks>();
		for (LottoClass lottoClass : lottoClassList) {
			System.out.println("~~~~~~~~~~~~~~~>" + lottoClass.getClassName());
			List<LottoResult> lottoResult = lottoresultDao.getLottoResultByInstallment(installment,
					lottoClass.getLottoClassCode());
			System.out.println("~~~~~~~~~~~~~~~>" + lottoResult.size());
			lottoResultStocksRes.Stocks stock = new lottoResultStocksRes.Stocks();
			stock.setLottoClassCode(lottoClass.getLottoClassCode());
			stock.setLottoClassName(lottoClass.getClassName());
			stock.setLottoClassImg(lottoClass.getLottoClassImg());
			for (LottoResult dataSet : lottoResult) {

				if (LOTTO_KIND.DIGIT3_TOP.equals(dataSet.getMsdLottoKindCode())) {
					stock.setDigit3Top(dataSet.getLottoNumber());
					System.out.println(
							"~~~~~~~~~~~~~~~>" + dataSet.getMsdLottoKindCode() + " :: " + dataSet.getLottoNumber());
				}
				if (LOTTO_KIND.DIGIT2_TOP.equals(dataSet.getMsdLottoKindCode())) {
					stock.setDigit2Top(dataSet.getLottoNumber());
					System.out.println(
							"~~~~~~~~~~~~~~~>" + dataSet.getMsdLottoKindCode() + " :: " + dataSet.getLottoNumber());
				}

				if (LOTTO_KIND.DIGIT2_BOT.equals(dataSet.getMsdLottoKindCode())) {
					stock.setDigit2Bot(dataSet.getLottoNumber());
					System.out.println(
							"~~~~~~~~~~~~~~~>" + dataSet.getMsdLottoKindCode() + " :: " + dataSet.getLottoNumber());
				}

				if (LOTTO_KIND.DIGIT3_BOT.equals(dataSet.getMsdLottoKindCode())) {
					stock.setDigit3Bot(dataSet.getLottoNumber());
					System.out.println(
							"~~~~~~~~~~~~~~~>" + dataSet.getMsdLottoKindCode() + " :: " + dataSet.getLottoNumber());
				}

				if (LOTTO_KIND.DIGIT3_FRONT.equals(dataSet.getMsdLottoKindCode())) {
					stock.setDigit3Front(dataSet.getLottoNumber());
					System.out.println(
							"~~~~~~~~~~~~~~~>" + dataSet.getMsdLottoKindCode() + " :: " + dataSet.getLottoNumber());
				}
				// stockList.add(stock);

			}
			stockList.add(stock);
			data.setStockList(stockList);

		}
		resData.add(data);
		return resData;
	}

	// ================================================== lotto stocks
	// =====================================================

}
