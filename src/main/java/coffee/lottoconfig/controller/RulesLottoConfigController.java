package coffee.lottoconfig.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import coffee.lottoconfig.service.RuleConfigService;
import coffee.lottoconfig.vo.req.AddLottoTimeReq;
import coffee.lottoconfig.vo.req.RuleDetailReq;
import coffee.lottoconfig.vo.res.RuleDetailRes;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE;
import framework.constant.ResponseConstant.RESPONSE_STATUS;
import framework.model.ResponseData;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/add-rule-lotto/")
@Slf4j
public class RulesLottoConfigController {
	
	@Autowired
	RuleConfigService ruleConfigService;
	
	@PostMapping("add-rule-detail")
	public ResponseData<String> addRuleDetail(@RequestBody RuleDetailReq req) {
		ResponseData<String> response = new ResponseData<String>();
		try {
			response.setData(ruleConfigService.addRulesDetail(req));
			response.setMessage(response.getData());
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API AddRuleDetailController => addRuleDetail");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.SAVE.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API AddRuleDetailController => addRuleDetail :" + e);
		}
		return response;
	}
	
	@GetMapping("get-rule-detail-by-class/{classCode}")
	public ResponseData<RuleDetailRes> getRuleDetail(@PathVariable String classCode) {
		ResponseData<RuleDetailRes> response = new ResponseData<RuleDetailRes>();
		try {
			response.setData(ruleConfigService.getRuleDetail(classCode));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API AddRuleDetailController => addRuleDetail");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API AddRuleDetailController => addRuleDetail :" + e);
		}
		return response;
	}
	
	
//	====================================================
	@PostMapping("update-rule-detail-by-class")
	public ResponseData<RuleDetailRes> updateRuleDetail(@RequestBody RuleDetailReq req) {
		ResponseData<RuleDetailRes> response = new ResponseData<RuleDetailRes>();
		try {
			response.setData(ruleConfigService.updateRuleDetail(req));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API AddRuleDetailController => updateRuleDetail");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API AddRuleDetailController => updateRuleDetail :" + e);
		}
		return response;
	}
	
	@DeleteMapping("/delete-rule-config-image-by-code")
	@ResponseBody
	private ResponseData<?> deleteRuleConfigImage(@RequestParam String ruleCode) {
		ResponseData<?> response = new ResponseData<>();
		try {
			ruleConfigService.deleteRuleConfigImage(ruleCode);
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API DashBoardController => deleteRuleConfigImage");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API DashBoardController => deleteRuleConfigImage :" + e);
		}
		return response;
	}
	
	
	@DeleteMapping("/delete-rule-config-by-code")
	@ResponseBody
	private ResponseData<?> deleteRuleConfig(@RequestParam String ruleCode) {
		ResponseData<?> response = new ResponseData<>();
		try {
			ruleConfigService.deleteRuleConfig(ruleCode);
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API DashBoardController => deleteRuleConfig");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API DashBoardController => deleteRuleConfig :" + e);
		}
		return response;
	}
}
