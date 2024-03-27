package coffee.customerDumy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import coffee.customerDumy.service.CustomerDumyService;
import coffee.customerDumy.vo.CustomerDumyReq;
import coffee.customerDumy.vo.CustomerDumyRes;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE;
import framework.constant.ResponseConstant.RESPONSE_STATUS;
import framework.model.ResponseData;
import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = { "http://baiwa.ddns.net:9440", "http://172.19.93.12:8080", "http://192.168.1.142:4200" })
@RestController
@RequestMapping("/api/CustomerDumy/")
@Slf4j
public class CustomerDumyController {
	@Autowired
	CustomerDumyService customerDumyService;

	@PostMapping("/save-customer-dumy")
	private ResponseData<?> saveCustomerDumy(@RequestBody CustomerDumyReq req) {
		ResponseData<?> response = new ResponseData<>();
		try {
			customerDumyService.saveCustomerDumy(req);
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API DashBoardController => getDashboard");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API DashBoardController => getDashboard :" + e);
		}
		return response;
	}

	@GetMapping("get-all-cuctomer-dumy")
	public ResponseData<List<CustomerDumyRes>> getAllCustomerDumy() {
		ResponseData<List<CustomerDumyRes>> response = new ResponseData<List<CustomerDumyRes>>();
		try {
			response.setData(customerDumyService.getAllCustomerDumy());
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API DashBoardController => getAllCustomerDumy");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API DashBoardController => getAllCustomerDumy :" + e);
		}
		return response;
	}

	@GetMapping("get-cuctomer-dumy/{code}")
	public ResponseData<CustomerDumyRes> getCustomerDumy(@PathVariable String code) {
		ResponseData<CustomerDumyRes> response = new ResponseData<CustomerDumyRes>();
		try {
			response.setData(customerDumyService.getCustomerDumy(code));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API DashBoardController => getCustomerDumy");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API DashBoardController => getCustomerDumy :" + e);
		}
		return response;
	}

	@PutMapping("/update-by-code/{code}")
	@ResponseBody
	private ResponseData<CustomerDumyRes> updateCustomerDumy(@PathVariable("code") String code,
			@RequestBody CustomerDumyReq request) {
		ResponseData<CustomerDumyRes> response = new ResponseData<>();
		try {
			customerDumyService.updateCustomerDumy(code);
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API DashBoardController => getCustomerDumy");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API DashBoardController => getCustomerDumy :" + e);
		}
		return response;
	}

	@DeleteMapping("/delete-by-code/{code}")
	@ResponseBody
	private ResponseData<?> deleteByCustomerDumy(@PathVariable("code") String code) {
		ResponseData<?> response = new ResponseData<>();
		try {
			customerDumyService.deleteByCustomerDumy(code);
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API DashBoardController => getCustomerDumy");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API DashBoardController => getCustomerDumy :" + e);
		}
		return response;
	}
}
