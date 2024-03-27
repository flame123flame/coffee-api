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

import coffee.lottoconfig.service.GroupRiskService2;
import coffee.lottoconfig.vo.req.AddGroupRiskReq;
import coffee.lottoconfig.vo.req.addgroup.MinmaxReq;
import coffee.lottoconfig.vo.req.addgroup.PrizeReq;
import coffee.lottoconfig.vo.res.PrizeSettingData;
import coffee.lottoconfig.vo.res.PrizeSettingRes;
import coffee.lottoconfig.vo.res.addgroup.GroupRiskClassRes;
import coffee.lottoconfig.vo.res.addgroup.Minmax;
import framework.constant.ResponseConstant;
import framework.model.ResponseData;
import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = { "http://baiwa.ddns.net:9440", "http://172.19.93.12:8080", "http://192.168.1.142:4200" })
@RestController
@RequestMapping("/api/group-risk2/")
@Slf4j
public class GroupRiskController2 {

	@Autowired
	private GroupRiskService2 groupRiskService;

	@PostMapping("add-group-risk")
	public ResponseData<String> addGroupRisk(@RequestBody AddGroupRiskReq req) {
		ResponseData<String> response = new ResponseData<String>();
		try {
			response.setData(groupRiskService.addGroupRisk(req));
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.SUCCESS);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GroupRiskController => addGroupRisk");
		} catch (Exception e) {
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.FAILED);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GroupRiskController => addGroupRisk :" + e);
		}
		return response;
	}

	@GetMapping("get-group-risk-by-code")
	public ResponseData<AddGroupRiskReq> getGroupRiskByCode(@RequestParam String lottoClassCode,
			@RequestParam String lottoGroupCode) {
		ResponseData<AddGroupRiskReq> response = new ResponseData<AddGroupRiskReq>();
		try {
			response.setData(groupRiskService.getGroupRiskByCode(lottoClassCode, lottoGroupCode));
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.SUCCESS);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GroupRiskController => addGroupRisk");
		} catch (Exception e) {
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.FAILED);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GroupRiskController => addGroupRisk :" + e);
		}
		return response;
	}

	@PostMapping("get-by-class/{lottoClassCode}")
	public ResponseData<List<GroupRiskClassRes>> getByClass(@PathVariable String lottoClassCode) {
		ResponseData<List<GroupRiskClassRes>> response = new ResponseData<List<GroupRiskClassRes>>();
		try {
			response.setData(groupRiskService.getByClass(lottoClassCode));
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.SUCCESS);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GroupRiskController => getByClass");
		} catch (Exception e) {
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.FAILED);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GroupRiskController => getByClass :" + e);
		}
		return response;
	}

	@PostMapping("add-group-risk-max-min")
	public ResponseData<String> addGroupRiskMaxMin(@RequestBody MinmaxReq req) {
		ResponseData<String> response = new ResponseData<String>();
		try {
			response.setData(groupRiskService.addMinMax(req));
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.SUCCESS);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GroupRiskController => addGroupRisk");
		} catch (Exception e) {
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.FAILED);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GroupRiskController => addGroupRisk :" + e);
		}
		return response;
	}

	@PostMapping("add-group-risk-prize-setting")
	public ResponseData<String> addPrizeSetting(@RequestBody PrizeReq req) {
		ResponseData<String> response = new ResponseData<String>();
		try {
			response.setData(groupRiskService.addPrizeSetting(req));
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.SUCCESS);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GroupRiskController => addPrizeSetting");
		} catch (Exception e) {
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.FAILED);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GroupRiskController => addPrizeSetting :" + e);
		}
		return response;
	}

	@PostMapping("edit-group-risk-prize-setting")
	public ResponseData<String> editPrizeSetting(@RequestBody PrizeReq req) {
		ResponseData<String> response = new ResponseData<String>();
		try {
			response.setData(groupRiskService.editPrizeSetting(req));
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.SUCCESS);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GroupRiskController => EditPrizeSetting");
		} catch (Exception e) {
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.FAILED);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GroupRiskController => EditPrizeSetting :" + e);
		}
		return response;
	}

	@GetMapping("get-group-risk-prize-setting-by-id")
	public ResponseData<List<PrizeSettingRes>> getPrizeSettingById(@RequestParam String msdLottoKindCode,
			@RequestParam String lottoGroupCode) {
		ResponseData<List<PrizeSettingRes>> response = new ResponseData<List<PrizeSettingRes>>();
		try {
			response.setData(groupRiskService.getPrizeSettingById(msdLottoKindCode, lottoGroupCode));
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

	@GetMapping("get-group-risk-vip-prize-setting")
	public ResponseData<PrizeSettingData> getByKind(@RequestParam String lottoClassCode,
			@RequestParam String msdLottoKindCode, @RequestParam String lottoGroupCode) {
		ResponseData<PrizeSettingData> response = new ResponseData<PrizeSettingData>();
		try {
			response.setData(groupRiskService.getVipPrizeSetting(lottoClassCode, msdLottoKindCode, lottoGroupCode));
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

	@GetMapping("get-group-risk-max-min-by-code")
	public ResponseData<List<Minmax>> getGroupRiskMaxminByCode(@RequestParam String lottoClassCode,
			@RequestParam String msdLottoKindCode, @RequestParam String lottoGroupCode) {
		ResponseData<List<Minmax>> response = new ResponseData<List<Minmax>>();
		try {
			response.setData(groupRiskService.getGroupMinMaxByCode(lottoClassCode, msdLottoKindCode, lottoGroupCode));
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.SUCCESS);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API AddLottoController => getGroupRiskByCode");

		} catch (Exception e) {
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.SAVE.FAILED);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.FAILED);
			log.error("Error Calling API AddLottoController => getGroupRiskByCode :" + e);
		}
		return response;

	}

	@DeleteMapping("delete-group-risk/{kindCode}/{groupCode}/{classCode}")
	public ResponseData<?> deleteGroupKindByCode(@PathVariable String kindCode, @PathVariable String groupCode,
			@PathVariable String classCode) {
		ResponseData<?> response = new ResponseData<>();
		try {
			groupRiskService.deletelottoGroupKind(kindCode, groupCode, classCode);
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.DELETE.SUCCESS);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API delete-group-risk");
		} catch (Exception e) {
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.DELETE.FAILED);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.FAILED);
			log.error("Error Calling API delete-group-risk :" + e);
		}
		return response;
	}

	@DeleteMapping("delete-lotto-group-risk/{lottoGroupCode}")
	public ResponseData<?> deleteLottoGroupRiskByCode(@PathVariable String lottoGroupCode) {
		ResponseData<?> response = new ResponseData<>();
		try {
			groupRiskService.deleteLottoGroupRisk(lottoGroupCode);
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.DELETE.SUCCESS);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API delete-group-risk");
		} catch (Exception e) {
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.DELETE.FAILED);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.FAILED);
			log.error("Error Calling API delete-group-risk :" + e);
		}
		return response;
	}

	@DeleteMapping("delete-group-max-min/{kindCode}/{groupCode}/{groupMaxMinMapCode}")
	public ResponseData<?> deleteGroupMaxminByCode(@PathVariable String kindCode, @PathVariable String groupCode,
			@PathVariable String groupMaxMinMapCode) {
		ResponseData<?> response = new ResponseData<>();
		try {
			groupRiskService.deleteLottoGroupRisk(kindCode, groupCode, groupMaxMinMapCode);
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.DELETE.SUCCESS);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API delete-group-risk");
		} catch (Exception e) {
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.DELETE.FAILED);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.FAILED);
			log.error("Error Calling API delete-group-risk :" + e);
		}
		return response;
	}

	@DeleteMapping("delete-group-max-risk/{lottoGroupDtlCode}")
	public ResponseData<?> deleteGroupMaxRiskByCode(@PathVariable String lottoGroupDtlCode) {
		ResponseData<?> response = new ResponseData<>();
		try {
			groupRiskService.deleteGroupMaxrisk(lottoGroupDtlCode);
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.DELETE.SUCCESS);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API delete-group-risk");
		} catch (Exception e) {
			response.setMessage(ResponseConstant.RESPONSE_MESSAGE.DELETE.FAILED);
			response.setStatus(ResponseConstant.RESPONSE_STATUS.FAILED);
			log.error("Error Calling API delete-group-risk :" + e);
		}
		return response;
	}
}
