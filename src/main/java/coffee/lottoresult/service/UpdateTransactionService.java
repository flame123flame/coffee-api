package coffee.lottoresult.service;

import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import coffee.lottoresult.vo.req.thai.SendKeyBoReq;
import coffee.lottoresult.vo.req.thai.SendKeyBoReq.SendBoReq;
import coffee.lottoresult.vo.req.thai.UpdateTransactionBoReq;
import coffee.model.LottoResult;
import coffee.repo.dao.LottoTransactionDao;
import coffee.repo.jpa.LottoGroupTransactionRepo;
import coffee.repo.jpa.LottoResultRepo;
import coffee.repo.jpa.LottoTransactionRepo;
import framework.constant.LottoConstant.LOTTO_KIND;
import framework.constant.LottoConstant.LOTTO_STATUS;
import framework.model.ResponseData;
import framework.utils.HttpClientUtil;
import framework.constant.ProjectConstant;
import framework.constant.ResponseConstant.RESPONSE_STATUS;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UpdateTransactionService {

	@Value("${common-path.bo}")
	private String baseApiBo;

	@Value("${secert-key.update-lotto}")
	private String secertKey;

	@Autowired
	private LottoResultRepo lottoResultRepo;

	@Autowired
	private LottoTransactionRepo lottoTransactionRepo;

	@Autowired
	private LottoGroupTransactionRepo lottoGroupTransactionRepo;

	@Autowired
	private LottoTransactionDao lottoTransactionDao;

	@Autowired
	private ClearInstallmentService clearInstallmentService;

	@Transactional
	@SuppressWarnings("unchecked")
	public synchronized void updateMoneyDb(String classCode, Boolean autoUpdateWallet) throws Exception {
		List<LottoResult> listResult = lottoResultRepo.findByLottoClassCodeAndStatus(classCode,
				ProjectConstant.STATUS.APPROVE);
		if (listResult.size() == 0) {
			return;
		}

		Map<String, String> correctNumberMap = new HashMap<String, String>();

		for (LottoResult result : listResult) {
			String correctNumber = correctNumberMap.get(result.getMsdLottoKindCode());
			if (LOTTO_KIND.DIGIT3_SWAPPED.equals(result.getMsdLottoKindCode())) {
				correctNumberMap.put(LOTTO_KIND.DIGIT3_SWAPPED, findCorrect(listResult, LOTTO_KIND.DIGIT3_TOP));
				continue;
			}
			if (LOTTO_KIND.DIGIT1_TOP.equals(result.getMsdLottoKindCode())) {
				correctNumberMap.put(LOTTO_KIND.DIGIT1_TOP, findCorrect(listResult, LOTTO_KIND.DIGIT3_TOP));
				continue;
			}
			if (LOTTO_KIND.DIGIT1_BOT.equals(result.getMsdLottoKindCode())) {
				correctNumberMap.put(LOTTO_KIND.DIGIT1_BOT, findCorrect(listResult, LOTTO_KIND.DIGIT2_BOT));
				continue;
			}
			if (LOTTO_KIND.DIGIT3_FRONT.equals(result.getMsdLottoKindCode())
					|| LOTTO_KIND.DIGIT3_BOT.equals(result.getMsdLottoKindCode())) {
				if (StringUtils.isNotEmpty(correctNumber)) {
					correctNumberMap.put(result.getMsdLottoKindCode(), correctNumber + ", " + result.getLottoNumber());
				} else {
					correctNumberMap.put(result.getMsdLottoKindCode(), result.getLottoNumber());
				}
				continue;
			} else {
				correctNumberMap.put(result.getMsdLottoKindCode(), result.getLottoNumber());
				continue;
			}
		}

		for (LottoResult lottoResult : listResult) {
			lottoTransactionRepo.updateTransaction(LOTTO_STATUS.WIN,
					correctNumberMap.get(lottoResult.getMsdLottoKindCode()), new Date(), LOTTO_STATUS.PENDING,
					lottoResult.getLottoNumber(), classCode, lottoResult.getMsdLottoKindCode());
		}

		for (LottoResult lottoResult : listResult) {
			lottoTransactionRepo.updateTransactionLike(LOTTO_STATUS.LOSE,
					correctNumberMap.get(lottoResult.getMsdLottoKindCode()), new Date(), LOTTO_STATUS.PENDING,
					classCode, lottoResult.getMsdLottoKindCode());
			lottoGroupTransactionRepo.updateTransaction(LOTTO_STATUS.SHOW, LOTTO_STATUS.PENDING, classCode);
		}

		clearInstallmentService.clearInstallment(classCode);

		if (autoUpdateWallet) {

			List<SendBoReq> sendBoReq = lottoTransactionDao.findSum(classCode);
			String sig = genarateSignature(sendBoReq);
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
				lottoTransactionRepo.updateTransactionDb(true, classCode, new Date(), "SYSTEM_AUTO");
			}
		}

	}

	// @Async
	@SuppressWarnings("unchecked")
	public void triggerToUpdateTransaction() throws Exception {
		Date currentTime = new Date();
		Calendar pastTime = Calendar.getInstance();
		pastTime.setTime(currentTime);
		pastTime.add(Calendar.HOUR_OF_DAY, -1);

		UpdateTransactionBoReq body = new UpdateTransactionBoReq();
		body.setData(lottoTransactionRepo.findByUpdatedDateBetween(pastTime.getTime(), currentTime));

		RestTemplate restTemplate = HttpClientUtil.getInstant();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<UpdateTransactionBoReq> entity = new HttpEntity<>(body, headers);
		ResponseData<String> dataResStr = restTemplate
				.postForObject(baseApiBo + "/api/lotto-provider/update-lotto-transaction", entity, ResponseData.class);

		if (dataResStr.getStatus().equals(RESPONSE_STATUS.FAILED)) {
			throw new Exception(dataResStr.getStatus() + " :::: " + dataResStr.getMessage());
		}
	}

	private String findCorrect(List<LottoResult> resultList, String kindCode) {
		for (LottoResult lottoResult : resultList) {
			if (kindCode.equals(lottoResult.getMsdLottoKindCode())) {
				return lottoResult.getLottoNumber();
			}
		}
		return null;
	}

	public String genarateSignature(List<SendBoReq> req) {
		/**
		 * Object "Must" String Only
		 */
		ObjectMapper mapper = new ObjectMapper();
		StringBuilder rawData = new StringBuilder();

		req.forEach(item -> {
			String json = "";
			try {
				json = mapper.writeValueAsString(item);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			rawData.append(json);
		});

		/**
		 * Just Hard Code secretKey
		 */
		String secretKeyJoker = "∆@∂ßπ≠‘“πø¬æ≥≤µ∫Ω≈ç√ƒ∂ß∑†¥¨ˆø£";
		String signature = null;

		try {
			signature = getHMACSHA1Signature(rawData.toString(), secretKeyJoker);
		} catch (Exception e) {
			log.error("Error Genarate Signature => ", e);
		}
		return signature;
	}

	private String getHMACSHA1Signature(String rawData, String secretKey) throws Exception {
		SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(), "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(signingKey);
		byte[] hashedValue = mac.doFinal(rawData.getBytes());
		return Base64.getEncoder().encodeToString(hashedValue);
	}

}
