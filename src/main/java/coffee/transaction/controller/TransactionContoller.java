package coffee.transaction.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import coffee.model.LottoTransaction;
import coffee.transaction.service.TransactionService;
import coffee.transaction.vo.req.GetTransBoReq;
import coffee.transaction.vo.res.TransactionPayCostRes;
import coffee.transaction.vo.res.TransactionRes;
import coffee.transaction.vo.res.bo.GroupTranBoRes;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE;
import framework.constant.ResponseConstant.RESPONSE_STATUS;
import framework.model.ResponseData;
import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = { "http://baiwa.ddns.net:9440", "http://172.19.93.12:8080", "http://192.168.1.142:4200" })
@RestController
@RequestMapping("/api/lotto-transaction")
@Slf4j
public class TransactionContoller {

	@Autowired
	private TransactionService transactionService;

	@GetMapping("get-all-transaction")
	public ResponseData<List<TransactionRes>> getAllTransaction() {
		ResponseData<List<TransactionRes>> response = new ResponseData<List<TransactionRes>>();
		try {
			response.setData(transactionService.getAllTransaction());
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API TransactionContoller => getAllTransaction");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API TransactionContoller => getAllTransaction :" + e);
		}
		return response;
	}

	@GetMapping("get-transaction-by-group/{groupCode}")
	public ResponseData<List<TransactionRes>> getTransactionByGroup(@PathVariable String groupCode) {
		ResponseData<List<TransactionRes>> response = new ResponseData<List<TransactionRes>>();
		try {
			response.setData(transactionService.getTransactionByGroup(groupCode));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API TransactionContoller => getTransactionByGroup");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API TransactionContoller => getTransactionByGroup :" + e);
		}
		return response;
	}

	@PostMapping("get-transaction-bo")
	public ResponseData<List<LottoTransaction>> getTransactionBo(@RequestBody GetTransBoReq req) {
		ResponseData<List<LottoTransaction>> response = new ResponseData<List<LottoTransaction>>();
		try {
			response.setData(transactionService.getTransactionBetween(req));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API TransactionContoller => getTransactionBo");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API TransactionContoller => getTransactionBo :" + e);
		}
		return response;
	}

	@GetMapping("get-lotto-transaction-by-class-code/{lottoClassCode}")
	public ResponseData<List<LottoTransaction>> getLottoGroupByLottoClassCodeAndInstallment(
			@PathVariable("lottoClassCode") String classCode) {
		ResponseData<List<LottoTransaction>> response = new ResponseData<List<LottoTransaction>>();
		try {
			response.setData(transactionService.getLottoGroupByLottoClassCode(classCode));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API TransactionGroupContoller => getLottoGroupByLottoClassCodeAndInstallment");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error(
					"Error Calling API TransactionGroupContoller => getLottoGroupByLottoClassCodeAndInstallment :" + e);
		}
		return response;
	}

	@GetMapping("get-yeekee-round-by-installment")
	public ResponseData<List<Integer>> getRoundByInstallment(@RequestParam String installment, @RequestParam String lottoClassCode) {
		ResponseData<List<Integer>> response = new ResponseData<List<Integer>>();
		try {
			response.setData(transactionService.getRoundByInstallment(installment, lottoClassCode));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API TransactionGroupContoller => getRoundByInstallment");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API TransactionGroupContoller => getRoundByInstallment :" + e);
		}
		return response;
	}

	@GetMapping("/lotto-cancel-update-transaction")
	private ResponseData<?> lottoCancelUpdateTransaction(@RequestParam String categoryCode,
			@RequestParam String classCode, @RequestParam String installment, @RequestParam Integer roundYeekee,  @RequestParam String cancelRemark) {
		ResponseData<?> response = new ResponseData<>();
		try {
			transactionService.cancelLotto(categoryCode, classCode, installment, roundYeekee, cancelRemark);
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API TransactionGroupContoller => lottoCancelUpdateTransaction");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API TransactionGroupContoller => lottoCancelUpdateTransaction :" + e);
		}
		return response;
	}

	// TEST API
	// @GetMapping("/get-all-lotto-transaction-pay-cost")
	// private ResponseData<List<TransactionPayCostRes>> getAllPayCost() {
	// ResponseData<List<TransactionPayCostRes>> response = new
	// ResponseData<List<TransactionPayCostRes>>();
	// try {
	// response.setData(transactionService.getAllPayCost());
	// response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
	// response.setStatus(RESPONSE_STATUS.SUCCESS);
	// log.info("Success Calling API TransactionGroupContoller =>
	// lottoTransactionPayCost");
	// } catch (Exception e) {
	// response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
	// response.setStatus(RESPONSE_STATUS.FAILED);
	// log.error("Error Calling API TransactionGroupContoller =>
	// lottoTransactionPayCost :" + e);
	// }
	// return response;
	// }
}
