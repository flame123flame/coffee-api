package coffee.masterdata.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coffee.masterdata.service.LottoClassService;
import coffee.masterdata.vo.BoSyncRes;
import coffee.model.LottoClass;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE;
import framework.constant.ResponseConstant.RESPONSE_STATUS;
import framework.model.ResponseData;
import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = { "http://baiwa.ddns.net:9440", "http://172.19.93.12:8080", "http://192.168.1.142:4200" })
@Slf4j
@RestController
@RequestMapping("/api/lotto-class")
public class LottoClassController {
	public static final String[] ORIGINS = { "DOMAINONE", "DOMAINTWO" };

	@Autowired
	private LottoClassService getLottoClassService;

	@GetMapping("get-lotto-class/{lottoCategoryCode}")
	public ResponseData<List<LottoClass>> getLottoClass(@PathVariable String lottoCategoryCode) {
		ResponseData<List<LottoClass>> response = new ResponseData<List<LottoClass>>();
		try {
			System.out.print(lottoCategoryCode);
			response.setData(getLottoClassService.findByLottoCategoryCode(lottoCategoryCode));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GetLottoClassController => getLottoClass");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GetLottoClassController => getLottoClass :" + e);
		}
		return response;
	}

	@GetMapping("get-lotto-class-by-class-code/{lottoClassCode}")
	public ResponseData<LottoClass> getLottoClassByClassCode(@PathVariable String lottoClassCode) {
		ResponseData<LottoClass> response = new ResponseData<LottoClass>();
		try {
			response.setData(getLottoClassService.getLottoClassByClassCode(lottoClassCode));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GetLottoClassController => getLottoClass");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GetLottoClassController => getLottoClass :" + e);
		}
		return response;
	}

	@GetMapping("get-all-lotto-class")
	public ResponseData<List<BoSyncRes>> getAllLottoClass() {
		ResponseData<List<BoSyncRes>> response = new ResponseData<List<BoSyncRes>>();
		try {
			response.setData(getLottoClassService.getAllLottoClass());
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GetLottoClassController => getAllLottoClass");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GetLottoClassController => getAllLottoClass :" + e);
		}
		return response;
	}
}
