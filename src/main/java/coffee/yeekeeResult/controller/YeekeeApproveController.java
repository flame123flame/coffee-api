package coffee.yeekeeResult.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coffee.yeekeeResult.service.YeekeeApproveService;
import coffee.yeekeeResult.vo.res.YeekeeApproveRes;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE;
import framework.constant.ResponseConstant.RESPONSE_STATUS;
import framework.model.DataTableResponse;
import framework.model.DatatableRequest;
import framework.model.ResponseData;
import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = { "http://baiwa.ddns.net:9440", "http://172.19.93.12:8080", "http://192.168.1.142:4200" })
@Slf4j
@RestController
@RequestMapping("/api/yeekee-approve/")
public class YeekeeApproveController {
	@Autowired
	YeekeeApproveService yeekeeApproveService;

	@PostMapping("get-yeekee-paginate")
	public ResponseData<DataTableResponse<YeekeeApproveRes>> getYeekeePaginate(@RequestBody DatatableRequest param) {
		ResponseData<DataTableResponse<YeekeeApproveRes>> response = new ResponseData<DataTableResponse<YeekeeApproveRes>>();
		try {
			response.setData(yeekeeApproveService.paginate(param));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API Yeekee Result Controller => getYeekeePaginate");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API Yeekee Result Controller => getYeekeePaginate :" + e);
		}
		return response;
	}

	@GetMapping("get-count-checking-yeekee")
	public ResponseData<Integer> getCountCheckingYeekee() {
		ResponseData<Integer> response = new ResponseData<Integer>();
		try {
			response.setData(yeekeeApproveService.getCountCheckingYeekee());
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

}
