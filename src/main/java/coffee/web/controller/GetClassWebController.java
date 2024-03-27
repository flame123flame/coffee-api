package coffee.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coffee.web.service.GetClassWebService;
import coffee.web.vo.res.GetClassWebRes;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE;
import framework.constant.ResponseConstant.RESPONSE_STATUS;
import framework.model.ResponseData;
import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = { "http://baiwa.ddns.net:9440", "http://172.19.93.12:8080", "http://192.168.1.142:4200" })
@RestController
@RequestMapping("/api/lotto-list/")
@Slf4j
public class GetClassWebController {

	@Autowired
	private GetClassWebService getClassWebService;

	@GetMapping("get-class-web/{classCode}/{vipCode}")
	public ResponseData<GetClassWebRes> getClassWeb(@PathVariable String classCode, @PathVariable String vipCode) {
		ResponseData<GetClassWebRes> response = new ResponseData<GetClassWebRes>();
		try {
			response.setData(getClassWebService.getClassWeb(classCode, vipCode));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GetClassWebController => getClassWeb");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GetClassWebController => getClassWeb :" + e);
		}
		return response;
	}

}
