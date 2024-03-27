package coffee.draft.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coffee.draft.service.DraftApproveClassService;
import coffee.draft.vo.req.SubmitDraftReq;
import coffee.lottoconfig.vo.req.AddLottoTimeReq;
import coffee.model.ApproveClass;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE;
import framework.constant.ResponseConstant.RESPONSE_STATUS;
import framework.model.ResponseData;
import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = { "http://baiwa.ddns.net:9440", "http://172.19.93.12:8080", "http://192.168.1.142:4200" })
@RestController
@RequestMapping("/api/approve-draft-lotto-class/")
@Slf4j
public class DraftApproveClassController {

    @Autowired
    private DraftApproveClassService draftApproveClassService;

    @PostMapping("approve-config-class")
    public ResponseData<String> approveConfigClass(@RequestBody SubmitDraftReq req) {
        ResponseData<String> response = new ResponseData<String>();
        try {
            response.setData(draftApproveClassService.approveConfigClass(req));
            response.setMessage(RESPONSE_MESSAGE.SAVE.SUCCESS);
            response.setStatus(RESPONSE_STATUS.SUCCESS);
            log.info("Success Calling API DraftApproveClassController => addLottoTime");
        } catch (Exception e) {
            response.setMessage(RESPONSE_MESSAGE.SAVE.FAILED);
            response.setStatus(RESPONSE_STATUS.FAILED);
            log.error("Error Calling API DraftApproveClassController => addLottoTime :" + e);
        }
        return response;
    }

    @PostMapping("get-draft-approve-class")
    public ResponseData<List<ApproveClass>> getDraftApproveClass(@RequestBody SubmitDraftReq req) {
        ResponseData<List<ApproveClass>> response = new ResponseData<List<ApproveClass>>();
        try {
            response.setData(draftApproveClassService.getDraftApproveClass(req));
            response.setMessage(RESPONSE_MESSAGE.SAVE.SUCCESS);
            response.setStatus(RESPONSE_STATUS.SUCCESS);
            log.info("Success Calling API DraftApproveClassController => addLottoTime");
        } catch (Exception e) {
            response.setMessage(RESPONSE_MESSAGE.SAVE.FAILED);
            response.setStatus(RESPONSE_STATUS.FAILED);
            log.error("Error Calling API DraftApproveClassController => addLottoTime :" + e);
        }
        return response;
    }

}