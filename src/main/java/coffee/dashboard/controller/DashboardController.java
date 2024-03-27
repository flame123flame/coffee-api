package coffee.dashboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coffee.dashboard.service.DashboardService;
import coffee.dashboard.vo.res.DashBroardMainRes;
import coffee.dashboard.vo.res.SumDashboardRes;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE;
import framework.constant.ResponseConstant.RESPONSE_STATUS;
import framework.model.ResponseData;
import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = { "http://baiwa.ddns.net:9440", "http://172.19.93.12:8080", "http://192.168.1.142:4200" })
@RestController
@RequestMapping("/api/dashboard/")
@Slf4j
public class DashboardController {

	@Autowired
	private DashboardService dashboardService;

	@GetMapping("get-dashboard/{classCode}")
	public ResponseData<DashBroardMainRes> getDashboard(@PathVariable String classCode) {
		ResponseData<DashBroardMainRes> response = new ResponseData<DashBroardMainRes>();
		try {
			response.setData(dashboardService.getDashboard(classCode));
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

	@GetMapping("get-dashboard-sum-prize/{classCode}")
	public ResponseData<SumDashboardRes> getDashboardSumPrize(@PathVariable String classCode) {
		ResponseData<SumDashboardRes> response = new ResponseData<SumDashboardRes>();
		try {
			response.setData(dashboardService.getAllSumDasboard(classCode));
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
}
