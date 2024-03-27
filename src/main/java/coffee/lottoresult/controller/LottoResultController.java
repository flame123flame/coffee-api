package coffee.lottoresult.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import coffee.lottoresult.service.LottoResultService;
import coffee.lottoresult.vo.req.ConfirmResultReq;
import coffee.lottoresult.vo.req.thai.SubmitManualReq;
import coffee.lottoresult.vo.res.thai.LottoResultByClassRes;
import coffee.lottoresult.vo.res.thai.LottoResultCategoryRes;
import coffee.lottoresult.vo.res.thai.LottoResultRoundYeekeeRes;
import coffee.lottoresult.vo.res.thai.LottoResultYeekeeByClassRes;
import coffee.lottoresult.vo.res.thai.LottoYeekeeResultRes;
import coffee.lottoresult.vo.res.thai.ResultAllRes;
import coffee.lottoresult.vo.res.thai.ResultAllYeekeeRes;
import coffee.lottoresult.vo.res.thai.lottoResultStocksRes;
import coffee.model.LottoResult;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE;
import framework.constant.ResponseConstant.RESPONSE_STATUS;
import framework.model.ResponseData;
import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = { "http://baiwa.ddns.net:9440", "http://172.19.93.12:8080", "http://192.168.1.142:4200" })
@Slf4j
@RestController
@RequestMapping("/api/lotto-result/")
public class LottoResultController {

	@Autowired
	private LottoResultService lottoResultService;

	@GetMapping("sync-lotto-result")
	public ResponseData<String> syncLottoClass(@RequestParam String lottoClassCode) {
		ResponseData<String> response = new ResponseData<String>();
		try {
			response.setData(lottoResultService.syncAllLottoResult(lottoClassCode));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GetgetLotto Result Controller => getLotto Result");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GetgetLotto Result Controller => getLotto Result :" + e);
		}
		return response;
	}

	@PostMapping("submit-manual-lotto-result")
	public ResponseData<String> submitManualResultLotto(@RequestBody SubmitManualReq req) {
		ResponseData<String> response = new ResponseData<String>();
		try {
			response.setData(lottoResultService.submitManualResultLotto(req));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GetgetLotto Result Controller => getLotto Result");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GetgetLotto Result Controller => getLotto Result :" + e);
		}
		return response;
	}

	@PostMapping("submit-all-confirm-transaction")
	public ResponseData<String> approveAll(@RequestBody ConfirmResultReq req) {
		ResponseData<String> response = new ResponseData<String>();
		try {
			lottoResultService.submitAll(req);
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GetgetLotto Result Controller => approveAll");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GetgetLotto Result Controller => approveAll :" + e);
		}
		return response;
	}

	@PostMapping("submit-select-confirm-transaction")
	public ResponseData<String> approveSelect(@RequestBody ConfirmResultReq req) {
		ResponseData<String> response = new ResponseData<String>();
		try {
			lottoResultService.submitSelect(req);
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GetgetLotto Result Controller => approveSelect");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GetgetLotto Result Controller => approveSelect :" + e);
		}
		return response;
	}

	@PostMapping("submit-de-select-confirm-transaction")
	public ResponseData<String> approveDeSelect(@RequestBody ConfirmResultReq req) {
		ResponseData<String> response = new ResponseData<String>();
		try {
			lottoResultService.submitDeSelectId(req);
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GetgetLotto Result Controller => approveDeSelect");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GetgetLotto Result Controller => approveDeSelect :" + e);
		}
		return response;
	}

	@PostMapping("submit-by-id-confirm-transaction")
	public ResponseData<String> approveSelectById(@RequestBody ConfirmResultReq req) {
		ResponseData<String> response = new ResponseData<String>();
		try {
			lottoResultService.submitSelectById(req);
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GetgetLotto Result Controller => approveSelectById");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GetgetLotto Result Controller => approveSelectById :" + e);
		}
		return response;
	}

	// @GetMapping("get-lotto-result")
	// public ResponseData<LottoAllResultRes> getLottoClass(@RequestParam String
	// lottoClassCode) {
	// ResponseData<LottoAllResultRes> response = new
	// ResponseData<LottoAllResultRes>();
	// try {
	// response.setData(lottoResultService.getLottoResult(lottoClassCode));
	// response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
	// response.setStatus(RESPONSE_STATUS.SUCCESS);
	// log.info("Success Calling API GetgetLotto Result Controller => getLotto
	// Result");
	// } catch (Exception e) {
	// response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
	// response.setStatus(RESPONSE_STATUS.FAILED);
	// log.error("Error Calling API GetgetLotto Result Controller => getLotto Result
	// :" + e);
	// }
	// return response;
	// }

	@GetMapping("get-all-lotto-result/{codeGroup}")
	public ResponseData<List<LottoResult>> getAllLottoResult(@PathVariable String codeGroup) {
		ResponseData<List<LottoResult>> response = new ResponseData<List<LottoResult>>();
		try {
			response.setData(lottoResultService.getAllLottoResult(codeGroup));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GetgetLotto Result Controller => getLotto Result");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GetgetLotto Result Controller => getLotto Result :" + e);
		}
		return response;
	}

	@GetMapping("get-all-lotto-installment")
	public ResponseData<List<LottoResult>> getAllLottoInstallment(@RequestParam String lottoClassCode) {
		ResponseData<List<LottoResult>> response = new ResponseData<List<LottoResult>>();
		try {
			response.setData(lottoResultService.getAllLottoInstallment(lottoClassCode));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GetgetLotto Result Controller => getAllLottoInstallment");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GetgetLotto Result Controller => getAllLottoInstallment :" + e);
		}
		return response;
	}

	@GetMapping("get-lotto-result-by-date")
	public ResponseData<List<ResultAllRes>> getAllLottoResultByDate(@RequestParam String installment) {
		ResponseData<List<ResultAllRes>> response = new ResponseData<List<ResultAllRes>>();
		try {
			response.setData(lottoResultService.getAllLottoResultByDate(installment));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GetgetLotto Result Controller => getAllLottoResultByDate");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GetgetLotto Result Controller => getAllLottoResultByDate :" + e);
		}
		return response;
	}

	@GetMapping("get-lotto-result-by-lotto-category-code")
	public ResponseData<List<ResultAllYeekeeRes>> getAllLottoResultByLottoCategoryCode(
			@RequestParam String lottoCategoryCode) {
		ResponseData<List<ResultAllYeekeeRes>> response = new ResponseData<List<ResultAllYeekeeRes>>();
		try {
			response.setData(lottoResultService.getAllLottoResultByLottoCategoryCode(lottoCategoryCode));
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

	@GetMapping("get-lotto-yeekee-result-by-installment")
	public ResponseData<List<LottoYeekeeResultRes>> getAllLottoYeekeeResultByInstallment(
			@RequestParam String installament) {
		ResponseData<List<LottoYeekeeResultRes>> response = new ResponseData<List<LottoYeekeeResultRes>>();
		try {
			response.setData(lottoResultService.getAllLottoYeekeeResultByInstallment(installament));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GetgetLotto Result Controller => getAllLottoYeekeeResultByInstallment");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GetgetLotto Result Controller => getAllLottoYeekeeResultByInstallment :" + e);
		}
		return response;
	}
	
	@GetMapping("get-all-class-list-lotto-result")
	public ResponseData<List<LottoResultCategoryRes>> getAllClassListLottoResult() {
		ResponseData<List<LottoResultCategoryRes>> response = new ResponseData<List<LottoResultCategoryRes>>();
		try {
			response.setData(lottoResultService.getAllClassListLottoResult());
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GetgetLotto Result Controller => getAllClassListLottoResult");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GetgetLotto Result Controller => getAllClassListLottoResult :" + e);
		}
		return response;
	}
	
	@GetMapping("get-round-yeekee-by-class-code")
	public ResponseData<List<LottoResultRoundYeekeeRes>> getRoundYeekeeByClassCode(@RequestParam String lottoClassCode) {
		ResponseData<List<LottoResultRoundYeekeeRes>> response = new ResponseData<List<LottoResultRoundYeekeeRes>>();
		try {
			response.setData(lottoResultService.getRoundYeekeeByClassCode(lottoClassCode));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GetgetLotto Result Controller => getRoundYeekeeByClassCode");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GetgetLotto Result Controller => getRoundYeekeeByClassCode :" + e);
		}
		return response;
	}
	
	
//	================================================== type =====================================================
	@GetMapping("get-lotto-result-by-lotto-class-code/{lottoClassCode}")
	public ResponseData<List<LottoResultByClassRes>> getAllLottoResultByLottoClassCode(
			@PathVariable String lottoClassCode) {
		ResponseData<List<LottoResultByClassRes>> response = new ResponseData<List<LottoResultByClassRes>>();
		try {
			response.setData(lottoResultService.getAllLottoResultByLottoClassCode(lottoClassCode));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GetgetLotto Result Controller => getAllLottoResultByLottoClassCode");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GetgetLotto Result Controller => getAllLottoResultByLottoClassCode :" + e);
		}
		return response;
	}
	
	@GetMapping("get-lotto-result-yeekee-by-round/{lottoClassCode}/{roundYeekee}")
	public ResponseData<List<LottoResultYeekeeByClassRes>> getLottoResultYeekeeByRound(
			@PathVariable String lottoClassCode, @PathVariable Integer roundYeekee) {
		ResponseData<List<LottoResultYeekeeByClassRes>> response = new ResponseData<List<LottoResultYeekeeByClassRes>>();
		try {
			response.setData(lottoResultService.getLottoResultYeekeeByRound(lottoClassCode, roundYeekee));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GetgetLotto Result Controller => getLottoResultYeekeeByRound");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GetgetLotto Result Controller => getLottoResultYeekeeByRound :" + e);
		}
		return response;
	}
//	================================================== type =====================================================

//	================================================== lotto stocks =====================================================
	@GetMapping("get-lotto-result-stocks")
	public ResponseData<List<lottoResultStocksRes>> getAllStock(@RequestParam String installment) {
		ResponseData<List<lottoResultStocksRes>> response = new ResponseData<List<lottoResultStocksRes>>();
		try {
			response.setData(lottoResultService.getAllStock(installment));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GetgetLotto Result Controller => getAllLottoResultByDate");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GetgetLotto Result Controller => getAllLottoResultByDate :" + e);
		}
		return response;
	}
//	================================================== lotto stocks =====================================================

}
