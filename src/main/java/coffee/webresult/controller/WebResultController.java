package coffee.webresult.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import coffee.webresult.service.WebResultService;
import coffee.webresult.vo.LottoWebResultRes;
import coffee.webresult.vo.WebResultStocksRes;
import coffee.webresult.vo.WebResultYeekeeRes;
import framework.constant.LottoConstant;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE;
import framework.constant.ResponseConstant.RESPONSE_STATUS;
import framework.model.ResponseData;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = { "http://baiwa.ddns.net:9440", "http://192.168.1.117:4200", "http://192.168.1.123:4200" })
@RestController
@RequestMapping("/api/web-lotto-result/")
@Slf4j
public class WebResultController {
	
	@Autowired
	private WebResultService webResultService;
	
	@GetMapping("get-lotto-result")
	public ResponseData<List<LottoWebResultRes>> getWebResultLotto() {
		ResponseData<List<LottoWebResultRes>> response = new ResponseData<List<LottoWebResultRes>>();
		try {
			response.setData(webResultService.getlottoWebResultGovernment());
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GroupRiskController => getWebResultLotto");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GroupRiskController => getWebResultLotto :" + e);
		}
		return response;
	}
	
	@GetMapping("get-lotto-result-yeekee")
	public ResponseData<List<WebResultYeekeeRes>> getWebResultLottoYeekee() {
		ResponseData<List<WebResultYeekeeRes>> response = new ResponseData<List<WebResultYeekeeRes>>();
		try {
			response.setData(webResultService.getlottoWebResultYeekee());
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GroupRiskController => getWebResultLottoYeekee");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GroupRiskController => getWebResultLottoYeekee :" + e);
		}
		return response;
	}
	
	@GetMapping("get-lotto-result-stocks")
	public ResponseData<List<WebResultStocksRes>> getWebResultLottoStocks() {
		ResponseData<List<WebResultStocksRes>> response = new ResponseData<List<WebResultStocksRes>>();
		try {
			response.setData(webResultService.getWebResultStocks());
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GroupRiskController => getWebResultLottoStocks");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GroupRiskController => getWebResultLottoStocks :" + e);
		}
		return response;
	}
}
