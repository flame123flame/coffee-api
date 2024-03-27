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

import coffee.lottoconfig.service.CloseNumberService;
import coffee.lottoconfig.vo.req.CloseNumberReq;
import coffee.lottoconfig.vo.res.GroupDtlRes;
import coffee.lottoconfig.vo.res.LimitNumberRes;
import coffee.lottoreport.vo.res.GetAllCloseNumber;
import coffee.model.CloseNumber;
import framework.constant.ResponseConstant;
import framework.model.ResponseData;
import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = { "http://baiwa.ddns.net:9440", "http://172.19.93.12:8080", "http://192.168.1.142:4200" })
@RestController
@RequestMapping("/api/close-number/")
@Slf4j
public class CloseNumberController {

	@Autowired
	CloseNumberService closeNumberService;

	@GetMapping("get-close-number/{kind}/{classCode}")
	public ResponseData<List<CloseNumber>> getByKind(@PathVariable String kind, @PathVariable String classCode) {
		ResponseData<List<CloseNumber>> response = new ResponseData<List<CloseNumber>>();
		try {
			response.setData(closeNumberService.getAllByMSDLottoKindAndLottoClass(kind, classCode));
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.SUCCESS);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API CloseNumberController => getByKind");
		} catch (Exception e) {
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.FAILED);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.FAILED);
			log.error("Error Calling API CloseNumberController => getByKind :" + e);
		}
		return response;
	}

	@PostMapping("add-lotto-close-number")
	public ResponseData<String> insertOne(@RequestBody CloseNumberReq req) {
		ResponseData<String> response = new ResponseData<String>();
		try {
			closeNumberService.insertOne(req);
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.SUCCESS);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API CloseNumberController => addCloseNumber");
		} catch (Exception e) {
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.FAILED);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.FAILED);
			log.error("Error Calling API CloseNumberController => addCloseNumber :" + e);
		}
		return response;
	}

	@DeleteMapping("delete-close-number/{id}/{classCode}")
	public ResponseData<String> deleteOne(@PathVariable Long id, @PathVariable String classCode,
			@RequestParam(required = false) String swappedGroupCode) {
		ResponseData<String> response = new ResponseData<String>();
		try {
			closeNumberService.deleteOne(id, classCode, swappedGroupCode);
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.SUCCESS);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API CloseNumberController => deleteCloseNumber");
		} catch (Exception e) {
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.FAILED);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.FAILED);
			log.error("Error Calling API CloseNumberController => deleteCloseNumber :" + e);
		}
		return response;
	}

	@GetMapping("get-list-detail/{id}")
	public ResponseData<CloseNumber> getListDtl(@PathVariable Long id) {
		ResponseData<CloseNumber> response = new ResponseData<CloseNumber>();
		try {
			response.setData(closeNumberService.getDtlList(id));
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.SUCCESS);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API CloseNumberController => getListDtl");
		} catch (Exception e) {
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.FAILED);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.FAILED);
			log.error("Error Calling API CloseNumberController => getListDtl :" + e);
		}
		return response;
	}

	@GetMapping("setEnable/{id}/{bool}/{classCode}")
	public ResponseData<String> setStatus(@PathVariable(value = "bool") Boolean bool,
			@PathVariable(value = "id") Long id, @PathVariable String classCode,
			@RequestParam(required = false) String swappedGroupCode) {
		ResponseData<String> response = new ResponseData<String>();
		try {
			closeNumberService.setEnable(bool, id, classCode, swappedGroupCode);
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.SUCCESS);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API CloseNumberController => setStatus");
		} catch (Exception e) {
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.FAILED);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.FAILED);
			log.error("Error Calling API CloseNumberController => setStatus :" + e);
		}
		return response;
	}

	@GetMapping("get-all/{classCode}")
	public ResponseData<List<GetAllCloseNumber>> getAll(@PathVariable String classCode) {
		ResponseData<List<GetAllCloseNumber>> response = new ResponseData<List<GetAllCloseNumber>>();
		try {
			response.setData(closeNumberService.getAllListLimit(classCode));
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.SUCCESS);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API CloseNumberController => getAll");
		} catch (Exception e) {
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.FAILED);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.FAILED);
			log.error("Error Calling API CloseNumberController => getAll :" + e);
		}
		return response;
	}
}
