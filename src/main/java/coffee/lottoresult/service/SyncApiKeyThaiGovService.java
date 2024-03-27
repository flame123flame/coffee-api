package coffee.lottoresult.service;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.collections4.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import coffee.lottoresult.vo.req.thai.LottoResultRes;
import coffee.lottoresult.vo.req.thai.Number;
import coffee.lottoresult.vo.req.thai.SubmitManualReq;
import coffee.model.LottoClass;
import coffee.model.LottoResult;
import coffee.repo.jpa.LottoClassRepository;
import coffee.repo.jpa.LottoResultRepo;
import framework.constant.LottoConstant;
import framework.constant.LottoConstant.LOTTO_STATUS;
import framework.constant.ProjectConstant;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE;
import framework.utils.ConvertDateUtils;
import framework.utils.GenerateRandomString;
import framework.utils.HttpClientUtil;
import framework.utils.UserLoginUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SyncApiKeyThaiGovService {

	@Autowired
	private LottoResultRepo lottoResultRepo;
	@Autowired
	private LottoClassRepository lottoClassRepository;

	@Value("${path.thaiGovLotto.api}")
	private String lottoPathAPI;

	@Transactional
	public String syncAllLottoResult() throws KeyManagementException, NoSuchAlgorithmException {
		String lottoClassCode = "THAI";
		log.info("LottoClassCode is : " + lottoClassCode + ": Set LottoResult");
		String url = lottoPathAPI;
		RestTemplate restTemplate = HttpClientUtil.getInstant();
		ResponseEntity<LottoResultRes> result;
		try {
			result = restTemplate.postForEntity(url, new HashedMap<String, String>(), LottoResultRes.class);
		} catch (Exception e) {
			log.error(" SyncApiKeyThaiGovService : FAILED_CALL_API", e.getMessage());
			return "FAILED_CALL_API";
		}

		/*
		 * API ไม่มีข้อมูล
		 */
		if (result == null) {
			return "FAILED_API_DATA";
		}

		LottoResultRes data = result.getBody();
		if (data.getResponse().getData() == null) {
			return "FAILED_API_DATA";
		}

		/**
		 * เช็คงวด
		 */
		String dateInstallment = data.getResponse().getDate();
		String thisInstallment = ConvertDateUtils.formatDateToString(new Date(), ConvertDateUtils.YYYY_MM_DD,
				ConvertDateUtils.LOCAL_EN);
		if (!thisInstallment.equals(dateInstallment)) {
			return "BAD_INSTALLMENT:" + dateInstallment;
		}

		List<LottoResult> hasApprove = lottoResultRepo.findByLottoClassCodeAndLottoResultInstallmentAndStatus(
				lottoClassCode, thisInstallment, ProjectConstant.STATUS.APPROVE);
		if (hasApprove.size() > 0) {
			return "HAS_APPROVED";
		}

		List<LottoResult> lottoResultCheck = lottoResultRepo.findByLottoClassCodeAndLottoResultInstallmentAndStatus(
				lottoClassCode, thisInstallment, ProjectConstant.STATUS.PENDING);
		if (lottoResultCheck.size() > 0) {
			return "HAS_PENDING";
		}

		String codeGroup = GenerateRandomString.generateUUID();
		if (data.getResponse().getData().getFirst() != null) {
			String lottoNumber = data.getResponse().getData().getFirst().getNumber().get(0).getValue();

			/**
			 * เซ็ต 3 ตัวบน
			 */
			log.info("Set Data Lotto DIGIT3 TOP");
			String last3Digit = lottoNumber.substring(3, 6);

			LottoResult lottoResult3Top = new LottoResult();
			lottoResult3Top.setLottoNumber(last3Digit);
			lottoResult3Top.setLottoCategoryCode(LottoConstant.CATEGORY.GOVERNMENT);
			lottoResult3Top.setLottoClassCode(lottoClassCode);
			lottoResult3Top.setMsdLottoKindCode(LottoConstant.LOTTO_KIND.DIGIT3_TOP);
			lottoResult3Top.setCreatedAt(new Date());
			lottoResult3Top.setLottoResultInstallment(data.getResponse().getDate());
			lottoResult3Top.setCreatedBy(UserLoginUtil.getUsername());
			lottoResult3Top.setTypeInstallment(LottoConstant.TYPE_INSTALLMENT.MONTHLY);
			lottoResult3Top.setCodeGroup(codeGroup);
			lottoResultRepo.save(lottoResult3Top);

			/**
			 * เซ็ต 3 ตัวโต๊ด
			 */
			log.info("Set Data Lotto DIGIT3 SWAP");
			HashSet<String> lottoDigiSwap = new HashSet<String>();
			String digiSwap1 = last3Digit;
			String digiSwap2 = "" + last3Digit.charAt(0) + last3Digit.charAt(2) + last3Digit.charAt(1);
			String digiSwap3 = "" + last3Digit.charAt(1) + last3Digit.charAt(0) + last3Digit.charAt(2);
			String digiSwap4 = "" + last3Digit.charAt(1) + last3Digit.charAt(2) + last3Digit.charAt(0);
			String digiSwap5 = "" + last3Digit.charAt(2) + last3Digit.charAt(1) + last3Digit.charAt(0);
			String digiSwap6 = "" + last3Digit.charAt(2) + last3Digit.charAt(0) + last3Digit.charAt(1);

			lottoDigiSwap.add(digiSwap1);
			lottoDigiSwap.add(digiSwap2);
			lottoDigiSwap.add(digiSwap3);
			lottoDigiSwap.add(digiSwap4);
			lottoDigiSwap.add(digiSwap5);
			lottoDigiSwap.add(digiSwap6);

			for (String swapNumber : lottoDigiSwap) {
				LottoResult lottoResult = new LottoResult();
				lottoResult.setLottoNumber(swapNumber);
				lottoResult.setLottoCategoryCode(LottoConstant.CATEGORY.GOVERNMENT);
				lottoResult.setLottoClassCode(lottoClassCode);
				lottoResult.setMsdLottoKindCode(LottoConstant.LOTTO_KIND.DIGIT3_SWAPPED);
				lottoResult.setCreatedAt(new Date());
				lottoResult.setLottoResultInstallment(data.getResponse().getDate());
				lottoResult.setCreatedBy(UserLoginUtil.getUsername());
				lottoResult.setTypeInstallment(LottoConstant.TYPE_INSTALLMENT.MONTHLY);
				lottoResult.setCodeGroup(codeGroup);
				lottoResultRepo.save(lottoResult);

			}
		} else {
			return "FAILED_API_DATA";
		}

		if (data.getResponse().getData().getFirst() != null) {
			log.info("Set Data Lotto DIGIT2 TOP");
			String lottoNumber = data.getResponse().getData().getFirst().getNumber().get(0).getValue();
			LottoResult lottoResult = new LottoResult();
			lottoResult.setLottoNumber(lottoNumber.substring(4, 6));
			lottoResult.setLottoCategoryCode(LottoConstant.CATEGORY.GOVERNMENT);
			lottoResult.setLottoClassCode(lottoClassCode);
			lottoResult.setMsdLottoKindCode(LottoConstant.LOTTO_KIND.DIGIT2_TOP);
			lottoResult.setCreatedAt(new Date());
			lottoResult.setLottoResultInstallment(data.getResponse().getDate());
			lottoResult.setCreatedBy(UserLoginUtil.getUsername());
			lottoResult.setTypeInstallment(LottoConstant.TYPE_INSTALLMENT.MONTHLY);
			lottoResult.setCodeGroup(codeGroup);
			lottoResultRepo.save(lottoResult);
		}
		if (data.getResponse().getData().getFirst() != null) {
			log.info("Set Data Lotto 1DIGIT TOP");
			String lottoNumber = data.getResponse().getData().getFirst().getNumber().get(0).getValue();
			HashSet<String> lottoDigitop = new HashSet<String>();
			String digitop1 = lottoNumber.substring(3, 4);
			String digitop2 = lottoNumber.substring(4, 5);
			String digitop3 = lottoNumber.substring(5, 6);

			lottoDigitop.add(digitop1);
			lottoDigitop.add(digitop2);
			lottoDigitop.add(digitop3);

			for (String digiTopNumber : lottoDigitop) {
				LottoResult lottoResult = new LottoResult();
				lottoResult.setLottoNumber(digiTopNumber);
				lottoResult.setLottoCategoryCode(LottoConstant.CATEGORY.GOVERNMENT);
				lottoResult.setLottoClassCode(lottoClassCode);
				lottoResult.setMsdLottoKindCode(LottoConstant.LOTTO_KIND.DIGIT1_TOP);
				lottoResult.setCreatedAt(new Date());
				lottoResult.setLottoResultInstallment(data.getResponse().getDate());
				lottoResult.setCreatedBy(UserLoginUtil.getUsername());
				lottoResult.setTypeInstallment(LottoConstant.TYPE_INSTALLMENT.MONTHLY);
				lottoResult.setCodeGroup(codeGroup);
				lottoResultRepo.save(lottoResult);
			}
		}
		if (data.getResponse().getData().getLast3f() != null) {
			log.info("Set Data Lotto DIGIT3_FRONT");
			for (Number number : data.getResponse().getData().getLast3f().getNumber()) {
				LottoResult lottoResult = new LottoResult();
				lottoResult.setLottoNumber(number.getValue());
				lottoResult.setLottoCategoryCode(LottoConstant.CATEGORY.GOVERNMENT);
				lottoResult.setLottoClassCode("THAI");
				lottoResult.setMsdLottoKindCode(LottoConstant.LOTTO_KIND.DIGIT3_FRONT);
				lottoResult.setCreatedAt(new Date());
				lottoResult.setLottoResultInstallment(data.getResponse().getDate());
				lottoResult.setCreatedBy(UserLoginUtil.getUsername());
				lottoResult.setTypeInstallment(LottoConstant.TYPE_INSTALLMENT.MONTHLY);
				lottoResult.setCodeGroup(codeGroup);
				lottoResultRepo.save(lottoResult);
			}
		}
		if (data.getResponse().getData().getLast3b() != null) {
			log.info("Set Data Lotto DIGIT3_BOT");
			for (Number number : data.getResponse().getData().getLast3b().getNumber()) {

				LottoResult lottoResult = new LottoResult();
				lottoResult.setLottoNumber(number.getValue());
				lottoResult.setLottoCategoryCode(LottoConstant.CATEGORY.GOVERNMENT);
				lottoResult.setLottoClassCode("THAI");
				lottoResult.setMsdLottoKindCode(LottoConstant.LOTTO_KIND.DIGIT3_BOT);
				lottoResult.setCreatedAt(new Date());
				lottoResult.setLottoResultInstallment(data.getResponse().getDate());
				lottoResult.setCreatedBy(UserLoginUtil.getUsername());
				lottoResult.setTypeInstallment(LottoConstant.TYPE_INSTALLMENT.MONTHLY);
				lottoResult.setCodeGroup(codeGroup);
				lottoResultRepo.save(lottoResult);
			}
		}
		if (data.getResponse().getData().getLast2() != null) {
			log.info("Set Data Lotto DIGIT2_BOT");
			for (Number number : data.getResponse().getData().getLast2().getNumber()) {
				LottoResult lottoResult = new LottoResult();
				lottoResult.setLottoNumber(number.getValue());
				lottoResult.setLottoCategoryCode(LottoConstant.CATEGORY.GOVERNMENT);
				lottoResult.setLottoClassCode("THAI");
				lottoResult.setMsdLottoKindCode(LottoConstant.LOTTO_KIND.DIGIT2_BOT);
				lottoResult.setCreatedAt(new Date());
				lottoResult.setLottoResultInstallment(data.getResponse().getDate());
				lottoResult.setCreatedBy(UserLoginUtil.getUsername());
				lottoResult.setTypeInstallment(LottoConstant.TYPE_INSTALLMENT.MONTHLY);
				lottoResult.setCodeGroup(codeGroup);
				lottoResultRepo.save(lottoResult);
			}
		}

		if (data.getResponse().getData().getLast2() != null) {
			log.info("Set Data Lotto 1DIGIT_BOT");
			String lottoNumber = data.getResponse().getData().getLast2().getNumber().get(0).getValue();

			HashSet<String> lottoDigiBot = new HashSet<String>();
			String digiBot1 = lottoNumber.substring(0, 1);
			String digiBot2 = lottoNumber.substring(1, 2);

			lottoDigiBot.add(digiBot1);
			lottoDigiBot.add(digiBot2);

			for (String digiBotNumber : lottoDigiBot) {
				LottoResult lottoResult = new LottoResult();
				lottoResult.setLottoNumber(digiBotNumber);
				lottoResult.setLottoCategoryCode(LottoConstant.CATEGORY.GOVERNMENT);
				lottoResult.setLottoClassCode(lottoClassCode);
				lottoResult.setMsdLottoKindCode(LottoConstant.LOTTO_KIND.DIGIT1_BOT);
				lottoResult.setCreatedAt(new Date());
				lottoResult.setLottoResultInstallment(data.getResponse().getDate());
				lottoResult.setCreatedBy(UserLoginUtil.getUsername());
				lottoResult.setTypeInstallment(LottoConstant.TYPE_INSTALLMENT.MONTHLY);
				lottoResult.setCodeGroup(codeGroup);
				lottoResultRepo.save(lottoResult);
			}
		}

		return codeGroup;
	}

	@Transactional
	public String submitManualResultLotto(SubmitManualReq req) {
		String lottoClassCode = req.getClassCode();
		
		LottoClass dataClassFind = lottoClassRepository.findByLottoClassCode(lottoClassCode);
		log.info("LottoClassCode is : " + lottoClassCode + ": Set LottoResult");

		/**
		 * เช็คงวด
		 */
		String dateInstallment = req.getInstallment();
		String thisInstallment = ConvertDateUtils.formatDateToString(new Date(), ConvertDateUtils.YYYY_MM_DD,
				ConvertDateUtils.LOCAL_EN);
		// if (!thisInstallment.equals(dateInstallment)) {
		// return "BAD_INSTALLMENT:" + dateInstallment;
		// }

		List<LottoResult> hasApprove = lottoResultRepo.findByLottoClassCodeAndLottoResultInstallmentAndStatus(
				lottoClassCode, thisInstallment, ProjectConstant.STATUS.APPROVE);
		if (hasApprove.size() > 0) {
			return "HAS_APPROVED";
		}

		List<LottoResult> lottoResultCheck = lottoResultRepo.findByLottoClassCodeAndLottoResultInstallmentAndStatus(
				lottoClassCode, thisInstallment, ProjectConstant.STATUS.PENDING);
		if (lottoResultCheck.size() > 0) {
			return "HAS_PENDING";
		}

		String codeGroup = GenerateRandomString.generateUUID();
		if (req.getDigit3Top() != null) {
			String lottoNumber = req.getDigit3Top();

			/**
			 * เซ็ต 3 ตัวบน
			 */
			log.info("Set Data Lotto DIGIT3 TOP");

			String last3Digit = lottoNumber;

			LottoResult lottoResult3Top = new LottoResult();
			lottoResult3Top.setLottoNumber(last3Digit);
			lottoResult3Top.setLottoCategoryCode(dataClassFind.getLottoCategoryCode());
			lottoResult3Top.setLottoClassCode(lottoClassCode);
			lottoResult3Top.setMsdLottoKindCode(LottoConstant.LOTTO_KIND.DIGIT3_TOP);
			lottoResult3Top.setCreatedAt(new Date());
			lottoResult3Top.setLottoResultInstallment(dateInstallment);
			lottoResult3Top.setCreatedBy(UserLoginUtil.getUsername());
			lottoResult3Top.setTypeInstallment(LottoConstant.TYPE_INSTALLMENT.MONTHLY);
			lottoResult3Top.setCodeGroup(codeGroup);
			lottoResultRepo.save(lottoResult3Top);

			/**
			 * เซ็ต 3 ตัวโต๊ด
			 */
			log.info("Set Data Lotto DIGIT3 SWAP");
			HashSet<String> lottoDigiSwap = new HashSet<String>();
			String digiSwap1 = last3Digit;
			String digiSwap2 = "" + last3Digit.charAt(0) + last3Digit.charAt(2) + last3Digit.charAt(1);
			String digiSwap3 = "" + last3Digit.charAt(1) + last3Digit.charAt(0) + last3Digit.charAt(2);
			String digiSwap4 = "" + last3Digit.charAt(1) + last3Digit.charAt(2) + last3Digit.charAt(0);
			String digiSwap5 = "" + last3Digit.charAt(2) + last3Digit.charAt(1) + last3Digit.charAt(0);
			String digiSwap6 = "" + last3Digit.charAt(2) + last3Digit.charAt(0) + last3Digit.charAt(1);

			lottoDigiSwap.add(digiSwap1);
			lottoDigiSwap.add(digiSwap2);
			lottoDigiSwap.add(digiSwap3);
			lottoDigiSwap.add(digiSwap4);
			lottoDigiSwap.add(digiSwap5);
			lottoDigiSwap.add(digiSwap6);

			for (String swapNumber : lottoDigiSwap) {
				LottoResult lottoResult = new LottoResult();
				lottoResult.setLottoNumber(swapNumber);
				lottoResult.setLottoCategoryCode(dataClassFind.getLottoCategoryCode());
				lottoResult.setLottoClassCode(lottoClassCode);
				lottoResult.setMsdLottoKindCode(LottoConstant.LOTTO_KIND.DIGIT3_SWAPPED);
				lottoResult.setCreatedAt(new Date());
				lottoResult.setLottoResultInstallment(dateInstallment);
				lottoResult.setCreatedBy(UserLoginUtil.getUsername());
				lottoResult.setTypeInstallment(LottoConstant.TYPE_INSTALLMENT.MONTHLY);
				lottoResult.setCodeGroup(codeGroup);
				lottoResultRepo.save(lottoResult);

			}
		} else {
			return "FAILED_API_DATA";
		}

		if (req.getDigit3Top() != null) {
			log.info("Set Data Lotto DIGIT2 TOP");
			String lottoNumber = req.getDigit3Top().substring(1);
			LottoResult lottoResult = new LottoResult();
			lottoResult.setLottoNumber(lottoNumber);
			lottoResult.setLottoCategoryCode(dataClassFind.getLottoCategoryCode());
			lottoResult.setLottoClassCode(lottoClassCode);
			lottoResult.setMsdLottoKindCode(LottoConstant.LOTTO_KIND.DIGIT2_TOP);
			lottoResult.setCreatedAt(new Date());
			lottoResult.setLottoResultInstallment(dateInstallment);
			lottoResult.setCreatedBy(UserLoginUtil.getUsername());
			lottoResult.setTypeInstallment(LottoConstant.TYPE_INSTALLMENT.MONTHLY);
			lottoResult.setCodeGroup(codeGroup);
			lottoResultRepo.save(lottoResult);
		}
		if (req.getDigit3Top() != null) {
			log.info("Set Data Lotto 1DIGIT TOP");
			String lottoNumber = req.getDigit3Top();
			HashSet<String> lottoDigitop = new HashSet<String>();
			String digitop1 = lottoNumber.substring(0, 1);
			String digitop2 = lottoNumber.substring(1, 2);
			String digitop3 = lottoNumber.substring(2, 3);

			lottoDigitop.add(digitop1);
			lottoDigitop.add(digitop2);
			lottoDigitop.add(digitop3);

			for (String digiTopNumber : lottoDigitop) {
				LottoResult lottoResult = new LottoResult();
				lottoResult.setLottoNumber(digiTopNumber);
				lottoResult.setLottoCategoryCode(dataClassFind.getLottoCategoryCode());
				lottoResult.setLottoClassCode(lottoClassCode);
				lottoResult.setMsdLottoKindCode(LottoConstant.LOTTO_KIND.DIGIT1_TOP);
				lottoResult.setCreatedAt(new Date());
				lottoResult.setLottoResultInstallment(dateInstallment);
				lottoResult.setCreatedBy(UserLoginUtil.getUsername());
				lottoResult.setTypeInstallment(LottoConstant.TYPE_INSTALLMENT.MONTHLY);
				lottoResult.setCodeGroup(codeGroup);
				lottoResultRepo.save(lottoResult);
			}
		}
		if (req.getDigit3Front() != null) {
			log.info("Set Data Lotto DIGIT3_FRONT");
			for (String number : req.getDigit3Front()) {
				if(number!=null)
				{
					LottoResult lottoResult = new LottoResult();
					lottoResult.setLottoNumber(number);
					lottoResult.setLottoCategoryCode(dataClassFind.getLottoCategoryCode());
					lottoResult.setLottoClassCode(lottoClassCode);
					lottoResult.setMsdLottoKindCode(LottoConstant.LOTTO_KIND.DIGIT3_FRONT);
					lottoResult.setCreatedAt(new Date());
					lottoResult.setLottoResultInstallment(dateInstallment);
					lottoResult.setCreatedBy(UserLoginUtil.getUsername());
					lottoResult.setTypeInstallment(LottoConstant.TYPE_INSTALLMENT.MONTHLY);
					lottoResult.setCodeGroup(codeGroup);
					lottoResultRepo.save(lottoResult);
				}
				
			}
		}
		if (req.getDigit3Bot() != null) {
			log.info("Set Data Lotto DIGIT3_BOT");
			for (String number : req.getDigit3Bot()) {
				if(number!=null)
				{
					LottoResult lottoResult = new LottoResult();
					lottoResult.setLottoNumber(number);
					lottoResult.setLottoCategoryCode(dataClassFind.getLottoCategoryCode());
					lottoResult.setLottoClassCode(lottoClassCode);
					lottoResult.setMsdLottoKindCode(LottoConstant.LOTTO_KIND.DIGIT3_BOT);
					lottoResult.setCreatedAt(new Date());
					lottoResult.setLottoResultInstallment(dateInstallment);
					lottoResult.setCreatedBy(UserLoginUtil.getUsername());
					lottoResult.setTypeInstallment(LottoConstant.TYPE_INSTALLMENT.MONTHLY);
					lottoResult.setCodeGroup(codeGroup);
					lottoResultRepo.save(lottoResult);
				}
				
			}
		}
		if (req.getDigit2Bot() != null) {
			log.info("Set Data Lotto DIGIT2_BOT");
			LottoResult lottoResult = new LottoResult();
			lottoResult.setLottoNumber(req.getDigit2Bot());
			lottoResult.setLottoCategoryCode(dataClassFind.getLottoCategoryCode());
			lottoResult.setLottoClassCode(lottoClassCode);
			lottoResult.setMsdLottoKindCode(LottoConstant.LOTTO_KIND.DIGIT2_BOT);
			lottoResult.setCreatedAt(new Date());
			lottoResult.setLottoResultInstallment(dateInstallment);
			lottoResult.setCreatedBy(UserLoginUtil.getUsername());
			lottoResult.setTypeInstallment(LottoConstant.TYPE_INSTALLMENT.MONTHLY);
			lottoResult.setCodeGroup(codeGroup);
			lottoResultRepo.save(lottoResult);

		}

		if (req.getDigit2Bot() != null) {
			log.info("Set Data Lotto 1DIGIT_BOT");
			String lottoNumber = req.getDigit2Bot();

			HashSet<String> lottoDigiBot = new HashSet<String>();
			String digiBot1 = lottoNumber.substring(0, 1);
			String digiBot2 = lottoNumber.substring(1, 2);

			lottoDigiBot.add(digiBot1);
			lottoDigiBot.add(digiBot2);

			for (String digiBotNumber : lottoDigiBot) {
				LottoResult lottoResult = new LottoResult();
				lottoResult.setLottoNumber(digiBotNumber);
				lottoResult.setLottoCategoryCode(dataClassFind.getLottoCategoryCode());
				lottoResult.setLottoClassCode(lottoClassCode);
				lottoResult.setMsdLottoKindCode(LottoConstant.LOTTO_KIND.DIGIT1_BOT);
				lottoResult.setCreatedAt(new Date());
				lottoResult.setLottoResultInstallment(dateInstallment);
				lottoResult.setCreatedBy(UserLoginUtil.getUsername());
				lottoResult.setTypeInstallment(LottoConstant.TYPE_INSTALLMENT.MONTHLY);
				lottoResult.setCodeGroup(codeGroup);
				lottoResultRepo.save(lottoResult);
			}
		}

		return codeGroup;
	}
}
