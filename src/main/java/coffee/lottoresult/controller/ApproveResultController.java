package coffee.lottoresult.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coffee.lottoresult.service.ApproveResultService;
import coffee.lottoresult.vo.req.thai.ApproveSaveReq;
import coffee.model.ApproveResult;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE;
import framework.constant.ResponseConstant.RESPONSE_STATUS;
import framework.model.ResponseData;
import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = { "http://baiwa.ddns.net:9440", "http://172.19.93.12:8080", "http://192.168.1.142:4200" })
@RestController
@RequestMapping("/api/approve-result/")
@Slf4j
public class ApproveResultController {

	@Autowired
	private ApproveResultService approveResultService;

	@PostMapping("submit")
	public ResponseData<String> submitAppr(@RequestBody ApproveSaveReq req) {
		ResponseData<String> response = new ResponseData<>();
		try {
			response.setData(approveResultService.saveApprove(req));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success ApproveResultController => submitAppr");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error ApproveResultController => submitAppr :" + e);
		}
		return response;
	}

	@PostMapping("list/{codeGroup}")
	public ResponseData<List<ApproveResult>> list(@PathVariable String codeGroup) {
		ResponseData<List<ApproveResult>> response = new ResponseData<>();
		try {
			response.setData(approveResultService.getList(codeGroup));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success ApproveResultController => submitAppr");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error ApproveResultController => submitAppr :" + e);
		}
		return response;
	}

}
