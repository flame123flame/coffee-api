package coffee.confirmtransaction.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coffee.confirmtransaction.service.ConfirmTransactionService;
import coffee.confirmtransaction.vo.ConfirmTransactionRes;
import coffee.lottoconfig.vo.req.RemarkReq;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE;
import framework.constant.ResponseConstant.RESPONSE_STATUS;
import framework.model.DataTableResponse;
import framework.model.DatatableRequest;
import framework.model.ResponseData;
import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = { "http://baiwa.ddns.net:9440", "http://172.19.93.12:8080", "http://192.168.1.142:4200" })
@Slf4j
@RestController
@RequestMapping("/api/confirm-transaction/")

public class ConfirmTransactionController {
	@Autowired
	private ConfirmTransactionService confirmTransactionService;

	@GetMapping("get-all-confirm-transaction")
	public ResponseData<List<ConfirmTransactionRes>> getAllConfirmTransaction() {
		ResponseData<List<ConfirmTransactionRes>> response = new ResponseData<List<ConfirmTransactionRes>>();
		try {
			response.setData(confirmTransactionService.getAllConfirmTransaction());
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API ConfirmTransactionService => getConfirmTransaction");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API ConfirmTransactionService => getConfirmTransaction :" + e);
		}
		return response;
	}

	@GetMapping("get-all-transaction")
	public ResponseData<List<ConfirmTransactionRes>> getAllTransaction() {
		ResponseData<List<ConfirmTransactionRes>> response = new ResponseData<List<ConfirmTransactionRes>>();
		try {
			response.setData(confirmTransactionService.getAllTransaction());
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API ConfirmTransactionService => getTransaction");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API ConfirmTransactionService => getTransaction :" + e);
		}
		return response;
	}

	@PostMapping("confirm-lotto-transaction/{status}/{id}")
	public ResponseData<String> confirmTransaction(@PathVariable Boolean status, @PathVariable Long id,
			@RequestBody RemarkReq req) {
		ResponseData<String> response = new ResponseData<String>();
		try {
			confirmTransactionService.confirmTransaction(status, id, req.getRemark());
			response.setMessage(RESPONSE_MESSAGE.EDIT.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API AddLottoController => confirmTransaction");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.EDIT.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API AddLottoController => confirmTransaction :" + e);
		}
		return response;
	}

	@PostMapping("get-all-paginate-confirm-transaction")
	public ResponseData<DataTableResponse<ConfirmTransactionRes>> getAllPaginateConfirmTransaction(
			@RequestBody DatatableRequest param) {
		ResponseData<DataTableResponse<ConfirmTransactionRes>> response = new ResponseData<DataTableResponse<ConfirmTransactionRes>>();
		try {
			response.setData(confirmTransactionService.getAllPaginateConfirmTransaction(param));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API ConfirmTransactionController => getAllPaginateConfirmTransaction");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API ConfirmTransactionController => getAllPaginateConfirmTransaction :" + e);
		}
		return response;
	}
	
	@GetMapping("get-count-confirm-transaction")
	public ResponseData<Integer> getCountCheckingYeekee() {
		ResponseData<Integer> response = new ResponseData<Integer>();
		try {
			response.setData(confirmTransactionService.getCountCheckingConfirmTransaction());
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API Yeekee Result Controller => getCountCheckingYeekee");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API Yeekee Result Controller => getCountCheckingYeekee :" + e);
		}
		return response;
	}
	

	@PostMapping("get-paginate-transaction")
	public ResponseData<DataTableResponse<ConfirmTransactionRes>> getPaginateTransaction(
			@RequestBody DatatableRequest param) {
		ResponseData<DataTableResponse<ConfirmTransactionRes>> response = new ResponseData<DataTableResponse<ConfirmTransactionRes>>();
		try {
			response.setData(confirmTransactionService.getPaginateTransaction(param));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API ConfirmTransactionController => getPaginateTransaction");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API ConfirmTransactionController => getPaginateTransaction :" + e);
		}
		return response;
	}

}
