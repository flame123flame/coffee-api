package coffee.draft.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coffee.draft.service.DraftLottoClassService;
import coffee.lottoconfig.vo.req.AddLottoTimeReq;
import coffee.lottoconfig.vo.res.LottoTimeRes;
import coffee.model.DraftLottoClass;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE;
import framework.constant.ResponseConstant.RESPONSE_STATUS;
import framework.model.ResponseData;
import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = { "http://baiwa.ddns.net:9440", "http://172.19.93.12:8080", "http://192.168.1.142:4200" })
@RestController
@RequestMapping("/api/draft-lotto-class/")
@Slf4j
public class DraftLottoClassController {

    @Autowired
    private DraftLottoClassService addLottoService;

    @PostMapping("add-lotto-time")
    public ResponseData<String> addLottoTime(@RequestBody AddLottoTimeReq req) {
        ResponseData<String> response = new ResponseData<String>();
        try {
            response.setData(addLottoService.addLottoTime(req));
            response.setMessage(response.getData());
            response.setStatus(RESPONSE_STATUS.SUCCESS);
            log.info("Success Calling API DraftLottoClassController => addLottoTime");
        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage(RESPONSE_MESSAGE.SAVE.FAILED);
            response.setStatus(RESPONSE_STATUS.FAILED);
            log.error("Error Calling API DraftLottoClassController => addLottoTime :" + e);
        }
        return response;
    }

    @GetMapping("get-lotto-time-by-code/{draftCode}")
    public ResponseData<LottoTimeRes> getLottoTimeByCode(@PathVariable("draftCode") String draftCode) {
        ResponseData<LottoTimeRes> response = new ResponseData<LottoTimeRes>();
        try {
            response.setData(addLottoService.getLottoTimeByCode(draftCode));
            response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
            response.setStatus(RESPONSE_STATUS.SUCCESS);
            log.info("Success Calling API DraftLottoClassController => getLottoTimeByCode");
        } catch (Exception e) {
            response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
            response.setStatus(RESPONSE_STATUS.FAILED);
            log.error("Error Calling API DraftLottoClassController => getLottoTimeByCode :" + e);
        }
        return response;
    }

    @GetMapping("get-lotto-class/{lottoCategoryCode}")
    public ResponseData<List<DraftLottoClass>> getLottoClass(@PathVariable String lottoCategoryCode) {
        ResponseData<List<DraftLottoClass>> response = new ResponseData<List<DraftLottoClass>>();
        try {
            response.setData(addLottoService.findByLottoCategoryCode(lottoCategoryCode));
            response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
            response.setStatus(RESPONSE_STATUS.SUCCESS);
            log.info("Success Calling API DraftLottoClassController => getLottoClass");
        } catch (Exception e) {
            response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
            response.setStatus(RESPONSE_STATUS.FAILED);
            log.error("Error Calling API DraftLottoClassController => getLottoClass :" + e);
        }
        return response;
    }

    @GetMapping("get-count-init/{categoryCode}")
    public ResponseData<Integer> getCountInit(@PathVariable String categoryCode) {
        ResponseData<Integer> response = new ResponseData<Integer>();
        try {
            response.setData(addLottoService.getCountInit(categoryCode));
            response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
            response.setStatus(RESPONSE_STATUS.SUCCESS);
            log.info("Success Calling API DraftLottoClassController => getLottoClass");
        } catch (Exception e) {
            response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
            response.setStatus(RESPONSE_STATUS.FAILED);
            log.error("Error Calling API DraftLottoClassController => getLottoClass :" + e);
        }
        return response;
    }
}