package coffee.yeekeeResult.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import coffee.buy.vo.req.SubmitYeeKeeReq;
import coffee.model.YeekeeGroupPrize;
import coffee.model.YeekeeSubmitNumber;
import coffee.redis.model.YeekeeLastList;
import coffee.model.YeekeeSumNumber;
import coffee.repo.dao.YeekeeResultDao;
import coffee.yeekeeResult.service.YeekeeApproveService;
import coffee.yeekeeResult.service.YeekeeResultManualService;
import coffee.yeekeeResult.service.YeekeeResultService;
import coffee.yeekeeResult.vo.res.YeekeeResultRoundSeqRes;
import coffee.yeekeeResult.vo.req.YeekeeResultReq;
import coffee.yeekeeResult.vo.res.YeekeeApproveRes;
import coffee.yeekeeResult.vo.res.YeekeeResultRes;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE;
import framework.constant.ResponseConstant.RESPONSE_STATUS;
import framework.model.ResponseData;
import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = { "http://baiwa.ddns.net:9440", "http://172.19.93.12:8080", "http://192.168.1.142:4200" })
@Slf4j
@RestController
@RequestMapping("/api/yeekee-result/")
public class YeekeeResultController {

	@Autowired
	YeekeeResultService resultService;

	@Autowired
	YeekeeResultDao yeekeeResultDao;

	@Autowired
	private YeekeeResultManualService yeekeeResultManualService;

	@Autowired
	private YeekeeApproveService yeekeeApproveService;

	@GetMapping("get-yeekee-sum-number-by-code/{code}")
	public ResponseData<YeekeeApproveRes> getYeekeeSumNumberIsApproved(@PathVariable String code) {
		ResponseData<YeekeeApproveRes> response = new ResponseData<YeekeeApproveRes>();
		try {
			response.setData(yeekeeApproveService.getYeekeeSumNumberIsApproved(code));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info(
					"Success Calling API GetgetLotto Yeekee Result Controller => getAllLottoResultByLottoCategoryCode");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GetgetLotto Yeekee Result Controller=> getAllLottoResultByLottoCategoryCode :"
					+ e);
		}
		return response;
	}

	@Autowired
	private YeekeeResultService yeekeeResultService;

	@GetMapping("get-yeekee-result-by-lotto-category-code")
	public ResponseData<List<YeekeeGroupPrize>> getAllYeekeeResultByLottoCategoryCode(
			@RequestParam String lottoClassCode, @RequestParam int roundYeekee, @RequestParam String installment,
			@RequestParam String kindCode) {
		ResponseData<List<YeekeeGroupPrize>> response = new ResponseData<List<YeekeeGroupPrize>>();
		try {
			response.setData(resultService.getAllYeekeeResultByLottoCategoryCode(lottoClassCode, roundYeekee,
					installment, kindCode));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GetgetLotto Result Controller => getAllLottoResultByLottoCategoryCode");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GetgetLotto Result Controller => getAllLottoResultByLottoCategoryCode :" + e);
		}
		return response;
	}

	@GetMapping("get-yeekee-round-seq")
	public ResponseData<List<YeekeeResultRoundSeqRes>> getYeekeeRoundSeq(@RequestParam String lottoClassCode,
			@RequestParam Integer roundYeekee, @RequestParam String installment) {
		ResponseData<List<YeekeeResultRoundSeqRes>> response = new ResponseData<List<YeekeeResultRoundSeqRes>>();
		try {
			response.setData(resultService.getYeekeeRoundSeq(lottoClassCode, installment, roundYeekee));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GetgetLotto Result Controller => getAllLottoResultByLottoCategoryCode");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GetgetLotto Result Controller => getAllLottoResultByLottoCategoryCode :" + e);
		}
		return response;
	}

	@GetMapping("get-yeekee-number-16")
	public ResponseData<Long> getYeekeeNumberMinus16(@RequestParam String lottoClassCode,
			@RequestParam Integer roundYeekee, @RequestParam String installment) {
		ResponseData<Long> response = new ResponseData<Long>();
		try {
			response.setData(resultService.getYeekeeNumberMinus16(lottoClassCode, installment, roundYeekee));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GetgetLotto Result Controller => getYeekeeNumberMinus16");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GetgetLotto Result Controller => getYeekeeNumberMinus16 :" + e);
		}
		return response;
	}

	@PostMapping("submit-result-manual")
	public ResponseData<String> submitResultManual(@RequestBody YeekeeResultReq req) {
		ResponseData<String> response = new ResponseData<String>();
		try {
			response.setData(yeekeeResultManualService.submitResultManual(req));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GetgetLotto Result Controller => submitResultManual");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GetgetLotto Result Controller => submitResultManual :" + e);
		}
		return response;
	}

	@PostMapping("get-close-result")
	public ResponseData<YeekeeLastList> getCloseResult(@RequestBody SubmitYeeKeeReq req) {
		ResponseData<YeekeeLastList> response = new ResponseData<YeekeeLastList>();
		try {
			response.setData(yeekeeResultService.getCloseResult(req));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GetgetLotto Result Controller => getCloseResult");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GetgetLotto Result Controller => getCloseResult :" + e);
		}
		return response;
	}

	@PostMapping("update-cache")
	public ResponseData<YeekeeLastList> updateCache(@RequestBody SubmitYeeKeeReq req) {
		ResponseData<YeekeeLastList> response = new ResponseData<>();
		try {
			response.setData(
					yeekeeResultManualService.updateCacheRes(req.getClassCode(), req.getInstallment(), req.getRound()));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GetgetLotto Result Controller => updateCache");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GetgetLotto Result Controller => updateCache :" + e);
		}
		return response;
	}

}
