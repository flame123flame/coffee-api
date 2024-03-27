package coffee.lottoreport.controller;

import java.math.BigDecimal;
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

import coffee.lottoreport.service.LottoReportService;
import coffee.lottoreport.vo.res.LottoInstallmentRes;
import coffee.lottoreport.vo.res.LottoReportRes;
import coffee.lottoreport.vo.res.LottoReportUpdatedResultYeekeeRes;
import coffee.lottoreport.vo.res.LottoSumWin;
import coffee.lottoreport.vo.res.LottoTransactionReportRes;
import coffee.model.LottoResult;
import framework.constant.ResponseConstant;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE;
import framework.constant.ResponseConstant.RESPONSE_STATUS;
import framework.model.DataTableResponse;
import framework.model.DatatableRequest;
import framework.model.ResponseData;
import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = { "http://baiwa.ddns.net:9440", "http://172.19.93.12:8080", "http://192.168.1.142:4200" })
@Slf4j
@RestController
@RequestMapping("/api/lotto-report/")
public class LottoReportController {

	@Autowired
	private LottoReportService lottoReportService;

	// @GetMapping("get-transaction-lotto-report-by-installment")
	// public ResponseData<List<LottoReportRes>>
	// getDetailByInstallment(@RequestParam String classCode,String installment) {
	// ResponseData<List<LottoReportRes>> response = new
	// ResponseData<List<LottoReportRes>>();
	// try {
	// response.setData(lottoReportService.getAllLottoReport(classCode,installment));
	// response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
	// response.setStatus(RESPONSE_STATUS.SUCCESS);
	// log.info("Success Calling API LottoReportService =>
	// getTransactionLottoReportByInstallment");
	// } catch (Exception e) {
	// response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
	// response.setStatus(RESPONSE_STATUS.FAILED);
	// log.error("Error Calling API LottoReportService =>
	// getTransactionLottoReportByInstallment :" + e);
	// }
	// return response;
	// }

	@GetMapping("get-all-lotto-installment/{classCode}")
	public ResponseData<List<LottoInstallmentRes>> getAllInstallment(@PathVariable String classCode) {
		ResponseData<List<LottoInstallmentRes>> response = new ResponseData<List<LottoInstallmentRes>>();
		try {
			response.setData(lottoReportService.getAllLottoInstallment(classCode));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API LottoReportService => getAllInstallment");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API LottoReportService => getAllInstallment :" + e);
		}
		return response;
	}

	@GetMapping("get-lotto-report-transaction-by-installment")
	public ResponseData<List<LottoReportRes>> getSumLottoTransaction(@RequestParam String classCode,
			String installment) {
		ResponseData<List<LottoReportRes>> response = new ResponseData<List<LottoReportRes>>();
		try {
			response.setData(lottoReportService.getSumLottoTransaction(classCode, installment));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API LottoReportService => getSumLottoTransaction");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API LottoReportService => getSumLottoTransaction :" + e);
		}
		return response;
	}

	@GetMapping("get-lotto-sum-win")
	public ResponseData<List<LottoSumWin>> getLottoSumWin(@RequestParam String classCode, String installment) {
		ResponseData<List<LottoSumWin>> response = new ResponseData<List<LottoSumWin>>();
		try {
			response.setData(lottoReportService.getLottoSumWin(classCode, installment));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API LottoReportService => getLottoSumWin");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API LottoReportService => getLottoSumWin :" + e);
		}
		return response;
	}

	@GetMapping("get-All-lotto-report-transaction-by-kindcode")
	public ResponseData<List<LottoReportRes>> getSumLottoTransaction(@RequestParam String classCode, String kindCode,
			String installment) {
		ResponseData<List<LottoReportRes>> response = new ResponseData<List<LottoReportRes>>();
		try {
			response.setData(lottoReportService.getAllLottoTransactionByKindCode(classCode, kindCode, installment));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API LottoReportService => getAllLottoTransactionByKindCode");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API LottoReportService => getAllLottoTransactionByKindCode :" + e);
		}
		return response;
	}

	@PostMapping("paginate-report-dashboard")
	public ResponseData<DataTableResponse<LottoInstallmentRes>> paginateDashboard(@RequestBody DatatableRequest req,
			@RequestParam String lottoClassCode) {
		ResponseData<DataTableResponse<LottoInstallmentRes>> response = new ResponseData<DataTableResponse<LottoInstallmentRes>>();
		try {
			response.setData(lottoReportService.paginateDashboard(req, lottoClassCode));
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SUCCESS);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API => FailedLoginController.paginateDashboard()");
		} catch (Exception e) {
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.ERROR500);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.FAILED);
			log.error("Error Calling API FailedLoginController.paginateDashboard() :" + e);
		}
		return response;
	}

	@PostMapping("paginate-report-round")
	public ResponseData<DataTableResponse<LottoInstallmentRes>> paginateRoundYeekee(@RequestBody DatatableRequest req,
			@RequestParam String lottoClassCode, @RequestParam String installment) {
		ResponseData<DataTableResponse<LottoInstallmentRes>> response = new ResponseData<DataTableResponse<LottoInstallmentRes>>();
		try {
			response.setData(lottoReportService.getReportRoundYeekee(req, lottoClassCode, installment));
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SUCCESS);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API => FailedLoginController.papaginateRoundYeekeeginate()");
		} catch (Exception e) {
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.ERROR500);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.FAILED);
			log.error("Error Calling API FailedLoginController.paginateRoundYeekee() :" + e);
		}
		return response;
	}

	// ====================== LOTTO REPORT YEEKEE ========================
	@PostMapping("paginate-sum-lotto-transaction-yeekee")
	public ResponseData<DataTableResponse<LottoReportRes>> paginateSumLottoTransactionYeekee( @RequestBody DatatableRequest req) {
		ResponseData<DataTableResponse<LottoReportRes>> response = new ResponseData<DataTableResponse<LottoReportRes>>();
		try {
			response.setData(lottoReportService.paginateSumLottoTransactionYeekee(req));
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SUCCESS);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API => LottoReportService.paginateSumLottoTransactionYeekee()");
		} catch (Exception e) {
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.ERROR500);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.FAILED);
			log.error("Error Calling API LottoReportService.paginateSumLottoTransactionYeekee() :" + e);
		}
		return response;
	}

	@GetMapping("get-total-sum-prize-correct")
	public ResponseData<BigDecimal> getLottoPrizeCorrectRes(@RequestParam String classCode,
			@RequestParam String installment, @RequestParam String roundYeekee) {
		ResponseData<BigDecimal> response = new ResponseData<BigDecimal>();
		try {
			response.setData(lottoReportService.getLottoPrizeCorrectRes(classCode, installment, roundYeekee));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API LottoReportService => getLottoPrizeCorrectRes");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API LottoReportService => getLottoPrizeCorrectRes :" + e);
		}
		return response;
	}

	@GetMapping("get-lotto-sum-win-yeekee")
	public ResponseData<List<LottoSumWin>> getLottoSumWinYeekee(@RequestParam String classCode,
			@RequestParam String installment, @RequestParam String roundYeekee) {
		ResponseData<List<LottoSumWin>> response = new ResponseData<List<LottoSumWin>>();
		try {
			response.setData(lottoReportService.getLottoSumWinYeekee(classCode, installment, roundYeekee));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API LottoReportService => getLottoSumWinYeekee");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API LottoReportService => getLottoSumWinYeekee :" + e);
		}
		return response;
	}

//	@GetMapping("get-All-lotto-report-yeekee-transaction-by-kindcode")
//	public ResponseData<List<LottoReportRes>> getLottoTransactionYeekeeByKindCode(@RequestParam String classCode,
//			@RequestParam String kindCode, @RequestParam String installment, @RequestParam String roundYeekee) {
//		ResponseData<List<LottoReportRes>> response = new ResponseData<List<LottoReportRes>>();
//		try {
//			response.setData(lottoReportService.getLottoTransactionYeekeeByKindCode(classCode, kindCode, installment,
//					roundYeekee));
//			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
//			response.setStatus(RESPONSE_STATUS.SUCCESS);
//			log.info("Success Calling API LottoReportService => getAllLottoTransactionByKindCode");
//		} catch (Exception e) {
//			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
//			response.setStatus(RESPONSE_STATUS.FAILED);
//			log.error("Error Calling API LottoReportService => getAllLottoTransactionByKindCode :" + e);
//		}
//		return response;
//	}

	@PostMapping("paginate-lotto-report-yeekee-transaction-by-kindcode")
	public ResponseData<DataTableResponse<LottoReportRes>> paginateLottoTransactionYeekeeByKindCode(@RequestBody DatatableRequest req) {
		ResponseData<DataTableResponse<LottoReportRes>> response = new ResponseData<DataTableResponse<LottoReportRes>>();
		try {
			response.setData(lottoReportService.paginateLottoTransactionYeekeeByKindCode(req));
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SUCCESS);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API => FailedLoginController.paginateLottoTransactionYeekeeByKindCode()");
		} catch (Exception e) {
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.ERROR500);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.FAILED);
			log.error("Error Calling API FailedLoginController.paginateLottoTransactionYeekeeByKindCode() :" + e);
		}
		return response;
	}
	
	@PostMapping("paginate-updated-lotto-result-yeekee-sum-number")
	public ResponseData<DataTableResponse<LottoReportUpdatedResultYeekeeRes>> paginateUpdatedLottoResultYeekeeSumNumber(@RequestBody DatatableRequest req) {
		ResponseData<DataTableResponse<LottoReportUpdatedResultYeekeeRes>> response = new ResponseData<DataTableResponse<LottoReportUpdatedResultYeekeeRes>>();
		try {
			response.setData(lottoReportService.paginateUpdatedLottoResultYeekeeSumNumber(req));
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SUCCESS);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API => FailedLoginController.paginateSolveLottoResultYeekeeSumNumber()");
		} catch (Exception e) {
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.ERROR500);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.FAILED);
			log.error("Error Calling API FailedLoginController.paginateSolveLottoResultYeekeeSumNumber() :" + e);
		}
		return response;
	}
	
	@PostMapping("paginate-report-dashboard-yeekee")
	public ResponseData<DataTableResponse<LottoInstallmentRes>> paginateDashboardYeekee(@RequestBody DatatableRequest req,
			@RequestParam String lottoClassCode) {
		ResponseData<DataTableResponse<LottoInstallmentRes>> response = new ResponseData<DataTableResponse<LottoInstallmentRes>>();
		try {
			response.setData(lottoReportService.paginateDashboardYeekee(req, lottoClassCode));
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SUCCESS);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API => FailedLoginController.paginateDashboard()");
		} catch (Exception e) {
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.ERROR500);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.FAILED);
			log.error("Error Calling API FailedLoginController.paginateDashboard() :" + e);
		}
		return response;
	}

	// ====================== LOTTO REPORT YEEKEE ========================
	
	

}
