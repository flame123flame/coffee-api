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

import coffee.lottoconfig.service.LimitNumberService;
import coffee.lottoconfig.vo.req.LimitNumberReq;
import coffee.lottoconfig.vo.res.GetAllLimitRes;
import coffee.lottoconfig.vo.res.GroupDtlRes;
import coffee.lottoconfig.vo.res.LimitDtlRes;
import coffee.lottoconfig.vo.res.LimitNumberRes;
import framework.constant.ResponseConstant;
import framework.model.ResponseData;
import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = { "http://baiwa.ddns.net:9440", "http://172.19.93.12:8080", "http://192.168.1.142:4200" })
@RestController
@RequestMapping("/api/limit-number")
@Slf4j
public class LimitNumberController {
	@Autowired
	LimitNumberService limitNumberService;

	@PostMapping
	public ResponseData<LimitNumberRes> insertOne(@RequestBody LimitNumberReq req) {
		ResponseData<LimitNumberRes> response = new ResponseData<LimitNumberRes>();
		try {
			response.setData(limitNumberService.insertOne(req));
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.SUCCESS);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API AddLottoController => addGroupRisk");
		} catch (Exception e) {
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.FAILED);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.FAILED);
			log.error("Error Calling API AddLottoController => addGroupRisk :" + e);
		}
		return response;
	}

	@GetMapping
	public ResponseData<List<LimitDtlRes>> getByKind(@RequestParam String kind, @RequestParam String classCode) {
		ResponseData<List<LimitDtlRes>> response = new ResponseData<List<LimitDtlRes>>();
		try {
			response.setData(limitNumberService.getAllByMSDLottoKindAndLottoClass(kind, classCode));
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.SUCCESS);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API AddLottoController => addGroupRisk");
		} catch (Exception e) {
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.FAILED);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.FAILED);
			log.error("Error Calling API AddLottoController => addGroupRisk :" + e);
		}
		return response;
	}

	@DeleteMapping("/{id}/{classCode}")
	public ResponseData<List<LimitNumberRes>> deleteOne(@PathVariable(value = "id") Long id,
			@PathVariable String classCode) {
		ResponseData<List<LimitNumberRes>> response = new ResponseData<List<LimitNumberRes>>();
		try {
			limitNumberService.deleteOne(id, classCode);
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.SUCCESS);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API AddLottoController => addGroupRisk");
		} catch (Exception e) {
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.FAILED);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.FAILED);
			log.error("Error Calling API AddLottoController => addGroupRisk :" + e);
		}
		return response;
	}

	@GetMapping("setEnable/{id}/{bool}/{classCode}")
	public ResponseData<List<LimitNumberRes>> deleteOne(@PathVariable(value = "bool") Boolean bool,
			@PathVariable(value = "id") Long id, @PathVariable String classCode) {
		ResponseData<List<LimitNumberRes>> response = new ResponseData<List<LimitNumberRes>>();
		try {
			limitNumberService.setEnable(bool, id, classCode);
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.SUCCESS);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API AddLottoController => addGroupRisk");
		} catch (Exception e) {
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.FAILED);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.FAILED);
			log.error("Error Calling API AddLottoController => addGroupRisk :" + e);
		}
		return response;
	}

	@GetMapping("get-all/{classCode}")
	public ResponseData<List<GetAllLimitRes>> getAll(@PathVariable String classCode) {
		ResponseData<List<GetAllLimitRes>> response = new ResponseData<List<GetAllLimitRes>>();
		try {
			response.setData(limitNumberService.getAllListLimit(classCode));
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.SUCCESS);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API AddLottoController => getAll");
		} catch (Exception e) {
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.FAILED);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.FAILED);
			log.error("Error Calling API AddLottoController => getAll :" + e);
		}
		return response;
	}

	@GetMapping("get-list-detail/{classCode}/{kindCode}")
	public ResponseData<List<GroupDtlRes>> getListDtl(@PathVariable String classCode, @PathVariable String kindCode) {
		ResponseData<List<GroupDtlRes>> response = new ResponseData<List<GroupDtlRes>>();
		try {
			response.setData(limitNumberService.getDtlList(classCode, kindCode));
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.SUCCESS);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API AddLottoController => getListDtl");
		} catch (Exception e) {
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.FAILED);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.FAILED);
			log.error("Error Calling API AddLottoController => getListDtl :" + e);
		}
		return response;
	}
}
