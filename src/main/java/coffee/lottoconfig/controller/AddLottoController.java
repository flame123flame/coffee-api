package coffee.lottoconfig.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coffee.lottoconfig.service.AddLottoService;
import coffee.lottoconfig.vo.req.AddLottoTimeReq;
import coffee.lottoconfig.vo.req.RemarkReq;
import coffee.lottoconfig.vo.res.LottoTimeRes;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE;
import framework.constant.ResponseConstant.RESPONSE_STATUS;
import framework.model.ResponseData;
import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = { "http://baiwa.ddns.net:9440", "http://172.19.93.12:8080", "http://192.168.1.142:4200" })
@RestController
@RequestMapping("/api/add-lotto/")
@Slf4j
public class AddLottoController {

	@Autowired
	private AddLottoService addLottoService;

	@PostMapping("add-lotto-time")
	public ResponseData<String> addLottoTime(@RequestBody AddLottoTimeReq req) {
		ResponseData<String> response = new ResponseData<String>();
		try {
			response.setData(addLottoService.addLottoTime(req));
			response.setMessage(response.getData());
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API AddLottoController => addLottoTime");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.SAVE.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API AddLottoController => addLottoTime :" + e);
		}
		return response;
	}

	@PostMapping("edit-lotto-time")
	public ResponseData<String> editLottoTime(@RequestBody AddLottoTimeReq req) {
		ResponseData<String> response = new ResponseData<String>();
		try {
			response.setData(addLottoService.editLottoTime(req));
			response.setMessage(RESPONSE_MESSAGE.EDIT.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API AddLottoController => addLottoTime");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.EDIT.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API AddLottoController => addLottoTime :" + e);
		}
		return response;
	}

	@GetMapping("get-lotto-time-by-code/{lottoClassCode}")
	public ResponseData<LottoTimeRes> getLottoTimeByCode(@PathVariable("lottoClassCode") String lottoClassCode) {
		ResponseData<LottoTimeRes> response = new ResponseData<LottoTimeRes>();
		try {
			response.setData(addLottoService.getLottoTimeByCode(lottoClassCode));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API AddLottoController => getLottoTimeByCode");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API AddLottoController => getLottoTimeByCode :" + e);
		}
		return response;
	}

	@DeleteMapping("delete-prize-setting/{timeSellCode}")
	public ResponseData<?> deleteTimeSell(@PathVariable("timeSellCode") String code) {
		ResponseData<?> responseData = new ResponseData<>();
		try {
			addLottoService.deleteTimeSellByCode(code);
			responseData.setMessage(RESPONSE_MESSAGE.DELETE.SUCCESS);
			responseData.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API AddLottoController => deleteTimeSell");
		} catch (Exception e) {
			responseData.setMessage(RESPONSE_MESSAGE.DELETE.FAILED);
			responseData.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API AddLottoController => deleteTimeSell :" + e);
		}
		return responseData;
	}

	@DeleteMapping("delete-lotto-class/{lottoClassCode}")
	public ResponseData<?> deleteLottoClass(@PathVariable("lottoClassCode") String code) {
		ResponseData<?> responseData = new ResponseData<>();
		try {
			addLottoService.deleteLottoClass(code);
			responseData.setMessage(RESPONSE_MESSAGE.DELETE.SUCCESS);
			responseData.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API AddLottoController => deleteLottoClass");
		} catch (Exception e) {
			responseData.setMessage(RESPONSE_MESSAGE.DELETE.FAILED);
			responseData.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API AddLottoController => deleteLottoClass :" + e);
		}
		return responseData;
	}

	@PostMapping("change-status-lotto/{status}/{id}")
	public ResponseData<String> ChangeLotto(@PathVariable Boolean status, @PathVariable Long id,
			@RequestBody RemarkReq req) {
		ResponseData<String> response = new ResponseData<String>();
		try {
			addLottoService.changeStatusLotto(status, id, req.getRemark());
			response.setMessage(RESPONSE_MESSAGE.EDIT.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API AddLottoController => addLottoTime");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.EDIT.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API AddLottoController => addLottoTime :" + e);
		}
		return response;
	}

}
