package coffee.grouplist.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coffee.grouplist.vo.GroupListRes;
import coffee.lottocancel.vo.res.LottoCancelRes;
import coffee.model.GroupListMapping;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE;
import framework.constant.ResponseConstant.RESPONSE_STATUS;
import framework.model.ResponseData;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/group-list/")
@Slf4j
public class GroupListController {

    @Autowired
    private GroupListService groupListService;

    @PostMapping("add/{classCode}/{groupCode}")
    public ResponseData<String> add(@PathVariable("classCode") String classCode,
            @PathVariable("groupCode") String groupCode) {
        ResponseData<String> response = new ResponseData<String>();
        try {
            groupListService.add(classCode, groupCode);
            response.setMessage(RESPONSE_MESSAGE.SAVE.SUCCESS);
            response.setStatus(RESPONSE_STATUS.SUCCESS);
            log.info("Success Calling API GroupListController => add");
        } catch (Exception e) {
            response.setMessage(RESPONSE_MESSAGE.SAVE.FAILED);
            response.setStatus(RESPONSE_STATUS.FAILED);
            log.error("Error Calling API GroupListController => add :" + e);
        }
        return response;
    }

    @GetMapping("list/{groupCode}")
    public ResponseData<List<GroupListRes>> getListByGroup(@PathVariable("groupCode") String groupCode) {
        ResponseData<List<GroupListRes>> response = new ResponseData<List<GroupListRes>>();
        try {
            response.setData(groupListService.get(groupCode));
            response.setMessage(RESPONSE_MESSAGE.SAVE.SUCCESS);
            response.setStatus(RESPONSE_STATUS.SUCCESS);
            log.info("Success Calling API GroupListController => getListByGroup");
        } catch (Exception e) {
            response.setMessage(RESPONSE_MESSAGE.SAVE.FAILED);
            response.setStatus(RESPONSE_STATUS.FAILED);
            log.error("Error Calling API GroupListController => getListByGroup :" + e);
        }
        return response;
    }
    @GetMapping("get-all")
    public ResponseData<List<GroupListRes>> getAll() {
    	ResponseData<List<GroupListRes>> response = new ResponseData<List<GroupListRes>>();
    	try {
    		response.setData(groupListService.getAll());
    		response.setMessage(RESPONSE_MESSAGE.SAVE.SUCCESS);
    		response.setStatus(RESPONSE_STATUS.SUCCESS);
    		log.info("Success Calling API GroupListController => getAllGroupList");
    	} catch (Exception e) {
    		response.setMessage(RESPONSE_MESSAGE.SAVE.FAILED);
    		response.setStatus(RESPONSE_STATUS.FAILED);
    		log.error("Error Calling API GroupListController => getAllGroupList :" + e);
    	}
    	return response;
    }

    @DeleteMapping("delete/{id}")
    public ResponseData<?> delete(@PathVariable("id") Long id) {
        ResponseData<?> response = new ResponseData<>();
        try {
            groupListService.delete(id);
            response.setMessage(RESPONSE_MESSAGE.SAVE.SUCCESS);
            response.setStatus(RESPONSE_STATUS.SUCCESS);
            log.info("Success Calling API GroupListController => delete");
        } catch (Exception e) {
            response.setMessage(RESPONSE_MESSAGE.SAVE.FAILED);
            response.setStatus(RESPONSE_STATUS.FAILED);
            log.error("Error Calling API GroupListController => delete :" + e);
        }
        return response;
    }
}