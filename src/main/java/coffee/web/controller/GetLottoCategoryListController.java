package coffee.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coffee.web.service.GetLottoCategoryListService;
import coffee.web.vo.res.LottoCategoryListGroupRes;
import coffee.web.vo.res.LottoCategoryListRes;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE;
import framework.constant.ResponseConstant.RESPONSE_STATUS;
import framework.model.ResponseData;
import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = { "http://baiwa.ddns.net:9440", "http://172.19.93.12:8080", "http://192.168.1.142:4200" })
@RestController
@RequestMapping("/api/lotto-list/")
@Slf4j
public class GetLottoCategoryListController {

	@Autowired
	private GetLottoCategoryListService getLottoCategoryListService;

	@GetMapping("lotto-category-list")
	public ResponseData<LottoCategoryListRes> getLottoCategoryList() {
		ResponseData<LottoCategoryListRes> response = new ResponseData<LottoCategoryListRes>();
		try {
			response.setData(getLottoCategoryListService.getLottoCategoryList());
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GetLottoCategoryListController => getLottoCategoryList");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GetLottoCategoryListController => getLottoCategoryList :" + e);
		}
		return response;
	}

	@GetMapping("lotto-category-list-group")
	public ResponseData<LottoCategoryListGroupRes> getLottoCategoryListGroup() {
		ResponseData<LottoCategoryListGroupRes> response = new ResponseData<LottoCategoryListGroupRes>();
		try {
			response.setData(getLottoCategoryListService.getLottoCategoryListGroup());
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GetLottoCategoryListController => getLottoCategoryListGroup");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GetLottoCategoryListController => getLottoCategoryListGroup :" + e);
		}
		return response;
	}
}
