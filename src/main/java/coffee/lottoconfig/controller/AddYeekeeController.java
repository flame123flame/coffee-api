package coffee.lottoconfig.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import coffee.lottoconfig.service.AddYeekeeService;
import coffee.lottoconfig.vo.req.AddLottoTimeReq;
import coffee.lottoconfig.vo.req.PrizeYeekeeReq;
import coffee.lottoconfig.vo.req.SeqPrizeWinReq;
import coffee.lottoconfig.vo.req.YeekeeMaxMinReq;
import coffee.lottoconfig.vo.res.PrizeYeekeeRes;
import coffee.lottoconfig.vo.res.YeekeeMaxMinRes;
import coffee.model.LottoClass;
import coffee.model.YeekeePrizeSeqMapping;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE;
import framework.constant.ResponseConstant.RESPONSE_STATUS;
import framework.model.ResponseData;
import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = { "http://baiwa.ddns.net:9440", "http://172.19.93.12:8080", "http://192.168.1.142:4200" })
@RestController
@RequestMapping("/api/add-yeekee/")
@Slf4j
public class AddYeekeeController {

	@Autowired
	AddYeekeeService addYeekeeService;

	@PostMapping("add-lotto-yeekee")
	public ResponseData<String> addLottoYeekee(@RequestBody AddLottoTimeReq req) {
		ResponseData<String> response = new ResponseData<String>();
		try {
			response.setData(addYeekeeService.addLottoYeekee(req));
			response.setMessage(response.getData());
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API AddYeekeeController => addLottoYeekee");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.SAVE.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API AddYeekeeController => addLottoYeekee :" + e);
		}
		return response;
	}

	@PostMapping("edit-lotto-yeekee")
	public ResponseData<String> editLottoYeekee(@RequestBody AddLottoTimeReq req) {
		ResponseData<String> response = new ResponseData<String>();
		try {
			response.setData(addYeekeeService.editLottoYeekee(req));
			response.setMessage(response.getData());
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API AddYeekeeController => editLottoYeekee");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.SAVE.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API AddYeekeeController => editLottoYeekee :" + e);
		}
		return response;
	}

	@PostMapping("add-lotto-yeekee-prize")
	public ResponseData<String> addPrizeYeekee(@RequestBody List<PrizeYeekeeReq> req) {
		ResponseData<String> response = new ResponseData<String>();
		try {
			response.setData(addYeekeeService.addPrizeYeekee(req));
			response.setMessage(response.getData());
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API AddYeekeeController => addPrizeYeekee");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.SAVE.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API AddYeekeeController => addPrizeYeekee :" + e);
		}
		return response;
	}

	@GetMapping("get-yeekee-prize-setting")
	public ResponseData<List<PrizeYeekeeRes>> getYeekeePrizeSetting(@RequestParam String lottoClassCode) {
		ResponseData<List<PrizeYeekeeRes>> response = new ResponseData<List<PrizeYeekeeRes>>();
		try {
			response.setData(addYeekeeService.getPrizeByClassCode(lottoClassCode));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API AddYeekeeController => getYeekeePrizeSetting");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.SAVE.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API AddYeekeeController => getYeekeePrizeSetting :" + e);
		}
		return response;
	}

	@GetMapping("get-lotto-yeekee-detail")
	public ResponseData<LottoClass> getYeekeeDetail(@RequestParam Long lottoClassId, String lottoClassCode) {
		ResponseData<LottoClass> response = new ResponseData<LottoClass>();
		try {
			response.setData(addYeekeeService.getYeekeeDetail(lottoClassId, lottoClassCode));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API getYeekeeDetail => getYeekeeDetail");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.SAVE.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API getYeekeeDetail => getYeekeeDetail :" + e);
		}
		return response;
	}

	@PostMapping("save-lotto-yeekee-max-min")
	public ResponseData<String> saveYeekeeMaxMin(@RequestBody YeekeeMaxMinReq req) {
		ResponseData<String> response = new ResponseData<String>();
		try {
			response.setData(addYeekeeService.addYeekeeMaxMin(req));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API getYeekeeDetail => saveYeekeeMaxMin");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.SAVE.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API getYeekeeDetail => saveYeekeeMaxMin :" + e);
		}
		return response;
	}

	@PostMapping("save-lotto-yeekee-seq-prize")
	public ResponseData<String> saveSeqPrizeWin(@RequestBody List<SeqPrizeWinReq> req) {
		ResponseData<String> response = new ResponseData<String>();
		try {
			response.setData(addYeekeeService.addSeqPrize(req));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API getYeekeeDetail => saveSeqPrizeWin");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.SAVE.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API getYeekeeDetail => saveSeqPrizeWin :" + e);
		}
		return response;
	}

	@GetMapping("get-lotto-yeekee-seq-prize/{classCode}")
	public ResponseData<List<YeekeePrizeSeqMapping>> getSeqPrizeWin(@PathVariable String classCode) {
		ResponseData<List<YeekeePrizeSeqMapping>> response = new ResponseData<List<YeekeePrizeSeqMapping>>();
		try {
			response.setData(addYeekeeService.getSeqPrize(classCode));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API getYeekeeDetail => getSeqPrizeWin");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.SAVE.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API getYeekeeDetail => getSeqPrizeWin :" + e);
		}
		return response;
	}

	@DeleteMapping("delete-seq-prize-by-code/{code}")
	public ResponseData<String> deleteSeqPrizeWin(@PathVariable String code) {
		ResponseData<String> response = new ResponseData<String>();
		try {
			response.setData(addYeekeeService.deleteBySeqPrize(code));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API getYeekeeDetail => deleteSeqPrizeWin");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.SAVE.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API getYeekeeDetail => deleteSeqPrizeWin :" + e);
		}
		return response;
	}

	@PostMapping("delete-yeekee-prize-by-id")
	public ResponseData<String> deleteYeekeePrize(@RequestBody List<Long> prizeSettingId) {
		ResponseData<String> response = new ResponseData<String>();
		try {
			response.setData(addYeekeeService.deleteYeekeePrize(prizeSettingId));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API getYeekeeDetail => deleteYeekeePrize");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.SAVE.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API getYeekeeDetail => deleteYeekeePrize :" + e);
		}
		return response;
	}

	@GetMapping("get-max-min-yeekee/{classCode}")
	public ResponseData<YeekeeMaxMinRes> getYeekeeMaxMin(@PathVariable String classCode) {
		ResponseData<YeekeeMaxMinRes> response = new ResponseData<YeekeeMaxMinRes>();
		try {
			response.setData(addYeekeeService.getYeekeeMaxMin(classCode));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API getYeekeeDetail => getYeekeeMaxMin");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.SAVE.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API getYeekeeDetail => getYeekeeMaxMin :" + e);
		}
		return response;
	}

	@DeleteMapping("delete-max-min-yeekee-by-id/{id}")
	public ResponseData<String> deleteMaxMinById(@PathVariable Long id) {
		ResponseData<String> response = new ResponseData<String>();
		try {
			response.setData(addYeekeeService.deleteMaxMinById(id));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API getYeekeeDetail => deleteMaxMinById");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.SAVE.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API getYeekeeDetail => deleteMaxMinById :" + e);
		}
		return response;
	}

}
