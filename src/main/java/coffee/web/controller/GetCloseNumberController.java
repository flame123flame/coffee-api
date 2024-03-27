package coffee.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coffee.web.service.GetCloseNumberService;
import coffee.web.vo.res.GetCloseNumberRes;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE;
import framework.constant.ResponseConstant.RESPONSE_STATUS;
import framework.model.ResponseData;
import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = { "http://baiwa.ddns.net:9440", "http://172.19.93.12:8080", "http://192.168.1.142:4200" })
@RestController
@RequestMapping("/api/close-number/")
@Slf4j
public class GetCloseNumberController {

	@Autowired
	private GetCloseNumberService getCloseNumberService;

	@GetMapping("get-enable")
	public ResponseData<List<GetCloseNumberRes>> getEnable() {
		ResponseData<List<GetCloseNumberRes>> response = new ResponseData<List<GetCloseNumberRes>>();
		try {
			response.setData(getCloseNumberService.getEnable());
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GetCloseNumberService => getCloseNumberService");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GetCloseNumberService => getCloseNumberService :" + e);
		}
		return response;
	}

}
