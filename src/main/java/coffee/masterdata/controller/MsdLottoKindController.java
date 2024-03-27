package coffee.masterdata.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coffee.masterdata.service.MsdLottoKindService;
import coffee.model.MsdLottoKind;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE;
import framework.constant.ResponseConstant.RESPONSE_STATUS;
import framework.model.ResponseData;
import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = { "http://baiwa.ddns.net:9440", "http://172.19.93.12:8080", "http://192.168.1.142:4200" })
@RestController
@RequestMapping("/api/msd-lotto-kind")
@Slf4j
public class MsdLottoKindController {

	@Autowired
	private MsdLottoKindService msdLottoKindService;

	@GetMapping("get-all-msd")
	public ResponseData<List<MsdLottoKind>> getAllMsd() {
		ResponseData<List<MsdLottoKind>> response = new ResponseData<List<MsdLottoKind>>();
		try {
			response.setData(msdLottoKindService.getAllMsd());
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API MsdLottoKindController => getAllMsd");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API MsdLottoKindController => getAllMsd :" + e);
		}
		return response;
	}

	@GetMapping("get-by-class-code/{classCode}")
	public ResponseData<List<MsdLottoKind>> getByClassCode(@PathVariable String classCode) {
		ResponseData<List<MsdLottoKind>> response = new ResponseData<List<MsdLottoKind>>();
		try {
			response.setData(msdLottoKindService.getByClassCodeNotIn(classCode));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API MsdLottoKindController => getAllMsd");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API MsdLottoKindController => getAllMsd :" + e);
		}
		return response;
	}

	@GetMapping("get-by-class-code-in/{classCode}")
	public ResponseData<List<MsdLottoKind>> getByClassCodeIn(@PathVariable String classCode) {
		ResponseData<List<MsdLottoKind>> response = new ResponseData<List<MsdLottoKind>>();
		try {
			response.setData(msdLottoKindService.getByClassCodeIn(classCode));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API MsdLottoKindController => getAllMsd");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API MsdLottoKindController => getAllMsd :" + e);
		}
		return response;
	}
}
