package coffee.buy.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coffee.buy.service.BuyLottoService;
import coffee.buy.vo.req.BuyLottoReq;
import coffee.buy.vo.res.BuyLottoRes;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE;
import framework.constant.ResponseConstant.RESPONSE_STATUS;
import framework.model.ResponseData;
import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = { "http://baiwa.ddns.net:9440", "http://172.19.93.12:8080", "http://192.168.1.142:4200" })
@RestController
@RequestMapping("/api/buy")
@Slf4j
public class BuyLottoController {

    @Autowired
    private BuyLottoService buyLottoService;

    @PostMapping
    public ResponseData<BuyLottoRes> buyLotto(@RequestBody BuyLottoReq req) {
        ResponseData<BuyLottoRes> response = new ResponseData<BuyLottoRes>();
        try {
            response.setData(buyLottoService.buyLotto(req));
            response.setMessage(RESPONSE_MESSAGE.SAVE.SUCCESS);
            response.setStatus(RESPONSE_STATUS.SUCCESS);
            log.info("Success Calling API BuyLottoController => buyLotto");
        } catch (Exception e) {
            response.setMessage(RESPONSE_MESSAGE.SAVE.FAILED);
            response.setStatus(RESPONSE_STATUS.FAILED);
            log.error("Error Calling API BuyLottoController => buyLotto :" + e);
        }
        return response;
    }

}
