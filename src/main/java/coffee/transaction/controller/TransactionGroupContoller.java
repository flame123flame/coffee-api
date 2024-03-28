package coffee.transaction.controller;

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

import coffee.model.LottoClass;
import coffee.transaction.service.TransactionGroupService;
import coffee.transaction.vo.req.GetTransBoReq;
import coffee.transaction.vo.res.RefundRes;
import coffee.transaction.vo.res.TransactionDatatableRes;
import coffee.transaction.vo.res.TransactionGroupDatatableRes;
import coffee.transaction.vo.res.TransactionGroupDetailRes;
import coffee.transaction.vo.res.TransactionGroupRes;
import coffee.transaction.vo.res.bo.GroupTranBoRes;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE;
import framework.constant.ResponseConstant.RESPONSE_STATUS;
import framework.model.DataTableResponse;
import framework.model.DatatableRequest;
import framework.model.ResponseData;
import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = { "http://baiwa.ddns.net:9440", "http://172.19.93.12:8080", "http://192.168.1.142:4200" })
@RestController
@RequestMapping("/api/transaction-group")
@Slf4j
public class TransactionGroupContoller {

	@Autowired
	private TransactionGroupService lottoTransactionService;

	@GetMapping("get-all-transaction-group")
	public ResponseData<List<TransactionGroupRes>> getAll() {
		ResponseData<List<TransactionGroupRes>> response = new ResponseData<List<TransactionGroupRes>>();
		try {
			response.setData(lottoTransactionService.getAllGroup());
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API TransactionGroupContoller => getAll");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API TransactionGroupContoller => getAll :" + e);
		}
		return response;
	}

	@GetMapping("get-transaction-group-by-user")
	public ResponseData<List<TransactionGroupRes>> getLottoGroupByUser() {
		ResponseData<List<TransactionGroupRes>> response = new ResponseData<List<TransactionGroupRes>>();

		try {
			response.setData(lottoTransactionService.getLottoGroupByUser());
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API TransactionGroupContoller => getLottoGroupByUser");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API TransactionGroupContoller => getLottoGroupByUser :" + e);
		}
		return response;
	}

	@PostMapping("paginate")
	public ResponseData<DataTableResponse<TransactionGroupDatatableRes>> paginate(@RequestBody DatatableRequest param) {
		ResponseData<DataTableResponse<TransactionGroupDatatableRes>> response = new ResponseData<DataTableResponse<TransactionGroupDatatableRes>>();
		try {
			response.setData(lottoTransactionService.paginate(param));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API TransactionGroupContoller => paginate");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API TransactionGroupContoller => paginate :" + e);
		}
		return response;
	}

	@PostMapping("paginate-all-transaction")
	public ResponseData<DataTableResponse<TransactionDatatableRes>> paginateAllTransaction(
			@RequestBody DatatableRequest param) {
		ResponseData<DataTableResponse<TransactionDatatableRes>> response = new ResponseData<DataTableResponse<TransactionDatatableRes>>();
		try {
			response.setData(lottoTransactionService.paginateAllTransaction(param));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API TransactionGroupContoller => paginateAllTransaction");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API TransactionGroupContoller => paginateAllTransaction :" + e);
		}
		return response;
	}

	@GetMapping("get-all-dropdown-lotto-class")
	public ResponseData<List<LottoClass>> getAllLottoClass() {
		ResponseData<List<LottoClass>> response = new ResponseData<List<LottoClass>>();
		try {
			response.setData(lottoTransactionService.getAllLottoClass());
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API TransactionGroupContoller => getAllLottoClass");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API TransactionGroupContoller => getAllLottoClass :" + e);
		}
		return response;
	}

	@GetMapping("get-transaction-detail-by-code/{groupCode}")
	public ResponseData<TransactionGroupDetailRes> getLottoGroupDetailByCode(@PathVariable String groupCode) {
		ResponseData<TransactionGroupDetailRes> response = new ResponseData<TransactionGroupDetailRes>();
		try {
			response.setData(lottoTransactionService.getLottoGroupDetailByCode(groupCode));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API TransactionGroupContoller => getLottoGroupDetailByCode");
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API TransactionGroupContoller => getLottoGroupDetailByCode :" + e);
		}
		return response;
	}

	@GetMapping("get-refund-lotto-list/{groupCode}/{classCode}")
	public ResponseData<RefundRes> getRefundLottoList(@PathVariable String groupCode, @PathVariable String classCode,
			@RequestParam(required = false) Integer roundYeekee) {
		ResponseData<RefundRes> response = new ResponseData<RefundRes>();
		try {
			response.setData(lottoTransactionService.checkLottoRefundTimeOut(groupCode, classCode, roundYeekee));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API TransactionGroupContoller => getRefundLottoList");
		} catch (Exception e) {
			response.setMessage(e.toString());
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API TransactionGroupContoller => getRefundLottoList :" + e);
		}
		return response;
	}

	@GetMapping("get-transaction-group-by-user-pending")
	public ResponseData<List<TransactionGroupRes>> getLottoGroupByUserPending() {
		ResponseData<List<TransactionGroupRes>> response = new ResponseData<List<TransactionGroupRes>>();

		try {
			response.setData(lottoTransactionService.getLottoGroupByUserPending());
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API TransactionGroupContoller => getLottoGroupByUserPending");
		} catch (Exception e) {
			e.printStackTrace();
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API TransactionGroupContoller => getLottoGroupByUserPending :" + e);
		}
		return response;
	}

	@GetMapping("get-transaction-group-by-user-show/{categoryCode}")
	public ResponseData<List<TransactionGroupRes>> getLottoGroupByUserShow(@PathVariable("categoryCode") String code) {
		ResponseData<List<TransactionGroupRes>> response = new ResponseData<List<TransactionGroupRes>>();
		try {
			response.setData(lottoTransactionService.getLottoGroupByUserShow(code));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API TransactionGroupContoller => getLottoGroupByUserShow");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API TransactionGroupContoller => getLottoGroupByUserShow :" + e);
		}
		return response;
	}

	@PostMapping
	public ResponseData<List<GroupTranBoRes>> getTransactionGroupBo(@RequestBody GetTransBoReq req) {
		ResponseData<List<GroupTranBoRes>> response = new ResponseData<List<GroupTranBoRes>>();
		try {
			response.setData(lottoTransactionService.getTransactionBo(req));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API TransactionGroupContoller => getTransactionBo");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API TransactionGroupContoller => getTransactionBo :" + e);
		}
		return response;
	}
}
