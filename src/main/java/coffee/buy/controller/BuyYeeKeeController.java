package coffee.buy.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coffee.buy.service.BuyYeekeeService;
import coffee.buy.vo.req.SendHttpModel;
import coffee.buy.vo.req.SubmitYeeKeeReq;
import coffee.buy.vo.res.YeeKeeNumberSubmitListRes;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE;
import framework.constant.ResponseConstant.RESPONSE_STATUS;
import framework.model.ResponseData;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/buy-yeekee")
@Slf4j
public class BuyYeeKeeController {

    @Autowired
    private BuyYeekeeService buyYeekeeService;

    @PostMapping("/submit-value-sum")
    public ResponseData<String> submitValueSum(@RequestBody SubmitYeeKeeReq req) {
        ResponseData<String> response = new ResponseData<String>();
        try {
            response.setData(buyYeekeeService.submitValueSum(req));
            response.setMessage(RESPONSE_MESSAGE.SAVE.SUCCESS);
            response.setStatus(RESPONSE_STATUS.SUCCESS);
            log.info("Success Calling API BuyYeeKeeController => submitValueSum");
        } catch (Exception e) {
            response.setMessage(RESPONSE_MESSAGE.SAVE.FAILED);
            response.setStatus(RESPONSE_STATUS.FAILED);
            log.error("Error Calling API BuyYeeKeeController => submitValueSum :" + e);
        }
        return response;
    }

    @PostMapping("/submit-value-sum-dummy")
    public ResponseData<?> submitValueSumDummy(@RequestBody SendHttpModel<SubmitYeeKeeReq> req) {
        ResponseData<?> response = new ResponseData<>();
        try {
            buyYeekeeService.submitValueSumDummy(req);
            response.setMessage(RESPONSE_MESSAGE.SAVE.SUCCESS);
            response.setStatus(RESPONSE_STATUS.SUCCESS);
            log.info("Success Calling API BuyYeeKeeController => submitValueSumDummy");
        } catch (Exception e) {
            response.setMessage(RESPONSE_MESSAGE.SAVE.FAILED);
            response.setStatus(RESPONSE_STATUS.FAILED);
            log.error("Error Calling API BuyYeeKeeController => submitValueSumDummy :" + e);
        }
        return response;
    }

    @PostMapping("/get-list-submit")
    public ResponseData<List<YeeKeeNumberSubmitListRes>> getYeeKeeNumberSubmitList(@RequestBody SubmitYeeKeeReq req) {
        ResponseData<List<YeeKeeNumberSubmitListRes>> response = new ResponseData<List<YeeKeeNumberSubmitListRes>>();
        try {
            response.setData(buyYeekeeService.getYeeKeeNumberSubmitList(req));
            response.setMessage(RESPONSE_MESSAGE.SAVE.SUCCESS);
            response.setStatus(RESPONSE_STATUS.SUCCESS);
            log.info("Success Calling API BuyYeeKeeController => getYeeKeeNumberSubmitList");
        } catch (Exception e) {
            response.setMessage(RESPONSE_MESSAGE.SAVE.FAILED);
            response.setStatus(RESPONSE_STATUS.FAILED);
            log.error("Error Calling API BuyYeeKeeController => getYeeKeeNumberSubmitList :" + e);
        }
        return response;
    }

    @PostMapping("/get-sum-submit")
    public ResponseData<BigDecimal> getSumSubmit(@RequestBody SubmitYeeKeeReq req) {
        ResponseData<BigDecimal> response = new ResponseData<BigDecimal>();
        try {
            response.setData(buyYeekeeService.getSumSubmit(req));
            response.setMessage(RESPONSE_MESSAGE.SAVE.SUCCESS);
            response.setStatus(RESPONSE_STATUS.SUCCESS);
            log.info("Success Calling API BuyYeeKeeController => getSumSubmit");
        } catch (Exception e) {
            response.setMessage(RESPONSE_MESSAGE.SAVE.FAILED);
            response.setStatus(RESPONSE_STATUS.FAILED);
            log.error("Error Calling API BuyYeeKeeController => getSumSubmit :" + e);
        }
        return response;
    }
}