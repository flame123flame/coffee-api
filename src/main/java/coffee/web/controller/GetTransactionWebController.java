package coffee.web.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coffee.web.service.GetClassWebService;
import coffee.web.service.GetTransactionWebService;
import coffee.web.vo.res.GetClassWebRes;
import coffee.web.vo.res.LottoWebTransactionRes;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE;
import framework.constant.ResponseConstant.RESPONSE_STATUS;
import framework.model.ResponseData;
import lombok.extern.slf4j.Slf4j;

//@CrossOrigin(origins = { "http://baiwa.ddns.net:9440", "http://172.19.93.12:8080", "http://192.168.1.142:4200" })
@RestController
@RequestMapping("/api/lotto-web-transaction/")
@Slf4j
public class GetTransactionWebController {

	@Autowired
	private GetTransactionWebService getTransactionWebService;

	@GetMapping("get-transaction-web/{username}")
	public ResponseData<List<LottoWebTransactionRes>> getWebTransaction(@PathVariable String username) {
		ResponseData<List<LottoWebTransactionRes>> response = new ResponseData<List<LottoWebTransactionRes>>();
		try {
			response.setData(getTransactionWebService.getWebTransaction(username));
			response.setMessage(RESPONSE_MESSAGE.GET.SUCCESS);
			response.setStatus(RESPONSE_STATUS.SUCCESS);
			log.info("Success Calling API GetClassWebController => getWebTransaction");
		} catch (Exception e) {
			response.setMessage(RESPONSE_MESSAGE.GET.FAILED);
			response.setStatus(RESPONSE_STATUS.FAILED);
			log.error("Error Calling API GetClassWebController => getWebTransaction :" + e);
		}
		return response;
	}

}
