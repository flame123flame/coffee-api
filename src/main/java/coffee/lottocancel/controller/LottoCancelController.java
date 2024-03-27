package coffee.lottocancel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coffee.lottocancel.service.LottoCancelService;
import coffee.lottocancel.vo.req.LottoCancelReq;
import coffee.lottocancel.vo.res.LottoCancelRes;
import coffee.model.LottoCancel;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE;
import framework.constant.ResponseConstant.RESPONSE_STATUS;
import framework.model.ResponseData;
import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = { "http://baiwa.ddns.net:9440", "http://172.19.93.12:8080", "http://192.168.1.142:4200" })
@RestController
@RequestMapping("/api/lotto-cancel/")
@Slf4j
public class LottoCancelController {
	@Autowired
	LottoCancelService lottoCancelService;

	// @GetMapping("get-lotto-cancel/{lottoCancelCode}")
	// public ResponseData<LottoCancelRes>
	// approveConfigClass(@PathVariable("lottoCancelCode") String lottoCancelCode) {
	// ResponseData<LottoCancelRes> response = new ResponseData<LottoCancelRes>();
	// try {
	// response.setData(lottoCancelService.getLottoCancelByLottoCancelCode(lottoCancelCode));
	// response.setMessage(RESPONSE_MESSAGE.SAVE.SUCCESS);
	// response.setStatus(RESPONSE_STATUS.SUCCESS);
	// log.info("Success Calling API DraftApproveClassController => addLottoTime");
	// } catch (Exception e) {
	// response.setMessage(RESPONSE_MESSAGE.SAVE.FAILED);
	// response.setStatus(RESPONSE_STATUS.FAILED);
	// log.error("Error Calling API DraftApproveClassController => addLottoTime :" +
	// e);
	// }
	// return response;
	// }

}
