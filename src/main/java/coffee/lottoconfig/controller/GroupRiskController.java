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
import org.springframework.web.bind.annotation.RestController;

import coffee.lottoconfig.service.GroupRiskService;
import coffee.lottoconfig.vo.req.AddGroupRiskReq;
import coffee.lottoconfig.vo.res.GroupRiskRes;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE;
import framework.constant.ResponseConstant.RESPONSE_STATUS;
import framework.model.ResponseData;
import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = { "http://baiwa.ddns.net:9440", "http://172.19.93.12:8080", "http://192.168.1.142:4200" })
@RestController
@RequestMapping("/api/group-risk/")
@Slf4j
public class GroupRiskController {
	@Autowired
	private GroupRiskService groupRiskService;

	// @PostMapping("add-group-risk")
	// public ResponseData<String> addGroupRisk(@RequestBody AddGroupRiskReq req) {
	// ResponseData<String> response = new ResponseData<String>();
	// try {
	// response.setData(groupRiskService.addGroupRisk(req));
	// response.setMessage(RESPONSE_MESSAGE.SAVE.SUCCESS);
	// response.setStatus(RESPONSE_STATUS.SUCCESS);
	// log.info("Success Calling API GroupRiskController => addGroupRisk");
	// } catch (Exception e) {
	// response.setMessage(RESPONSE_MESSAGE.SAVE.FAILED);
	// response.setStatus(RESPONSE_STATUS.FAILED);
	// log.error("Error Calling API GroupRiskController => addGroupRisk :" + e);
	// }
	// return response;
	// }
	//
	// @GetMapping("get-group-risk")
	// public ResponseData<List<GroupRiskRes>> getGroupList() {
	// ResponseData<List<GroupRiskRes>> response = new
	// ResponseData<List<GroupRiskRes>>();
	// try {
	// response.setData(groupRiskService.getAll());
	// response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
	// response.setStatus(RESPONSE_STATUS.SUCCESS);
	// log.info("Success Calling API GroupRiskController => getGroupList");
	// } catch (Exception e) {
	// response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
	// response.setStatus(RESPONSE_STATUS.FAILED);
	// log.error("Error Calling API GroupRiskController => getGroupList :" + e);
	// }
	// return response;
	// }
	//
	// @GetMapping("get-group-risk-by-class-code/{lottoClassCode}")
	// public ResponseData<List<GroupRiskRes>>
	// getGroupListByClassCode(@PathVariable("lottoClassCode") String
	// lottoClassCode) {
	// ResponseData<List<GroupRiskRes>> response = new
	// ResponseData<List<GroupRiskRes>>();
	// try {
	// response.setData(groupRiskService.getGroupListByClassCode(lottoClassCode));
	// response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
	// response.setStatus(RESPONSE_STATUS.SUCCESS);
	// log.info("Success Calling API GroupRiskController =>
	// getGroupListByClassCode");
	// } catch (Exception e) {
	// response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
	// response.setStatus(RESPONSE_STATUS.FAILED);
	// log.error("Error Calling API GroupRiskController => getGroupListByClassCode
	// :" + e);
	// }
	// return response;
	// }
	//
	// @GetMapping("get-group-risk-by-code/{lottoGroupCode}")
	// public ResponseData<GroupRiskRes>
	// getGroupByCode(@PathVariable("lottoGroupCode") String lottoGroupCode) {
	// ResponseData<GroupRiskRes> response = new ResponseData<GroupRiskRes>();
	// try {
	// response.setData(groupRiskService.getGroupByCode(lottoGroupCode));
	// response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
	// response.setStatus(RESPONSE_STATUS.SUCCESS);
	// log.info("Success Calling API GroupRiskController => getGroupByCode");
	// } catch (Exception e) {
	// response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
	// response.setStatus(RESPONSE_STATUS.FAILED);
	// log.error("Error Calling API GroupRiskController => getGroupByCode :" + e);
	// }
	// return response;
	// }
	//
	// @DeleteMapping("delete-group-risk/{lottoGroupCode}")
	// public ResponseData<?> deleteGroupRisk(@PathVariable("lottoGroupCode") String
	// lottoGroupCode) {
	// ResponseData<?> responseData = new ResponseData<>();
	// try {
	// groupRiskService.deleteLottoGroup(lottoGroupCode);
	// responseData.setMessage(RESPONSE_MESSAGE.DELETE.SUCCESS);
	// responseData.setStatus(RESPONSE_STATUS.SUCCESS);
	// log.info("Success Calling API GroupRiskController => deleteGroupRisk");
	// } catch (Exception e) {
	// responseData.setMessage(RESPONSE_MESSAGE.DELETE.FAILED);
	// responseData.setStatus(RESPONSE_STATUS.FAILED);
	// log.error("Error Calling API GroupRiskController => deleteGroupRisk :" + e);
	// }
	// return responseData;
	// }
	//
	// @DeleteMapping("delete-prize-setting/{prizeSettingCode}")
	// public ResponseData<?> deletePrizeSetting(@PathVariable("prizeSettingCode")
	// String prizeSettingCode) {
	// ResponseData<?> responseData = new ResponseData<>();
	// try {
	// groupRiskService.deletePrizeSetting(prizeSettingCode);
	// responseData.setMessage(RESPONSE_MESSAGE.DELETE.SUCCESS);
	// responseData.setStatus(RESPONSE_STATUS.SUCCESS);
	// log.info("Success Calling API GroupRiskController => deletePrizeSetting");
	// } catch (Exception e) {
	// responseData.setMessage(RESPONSE_MESSAGE.DELETE.FAILED);
	// responseData.setStatus(RESPONSE_STATUS.FAILED);
	// log.error("Error Calling API GroupRiskController => deletePrizeSetting :" +
	// e);
	// }
	// return responseData;
	// }
}
