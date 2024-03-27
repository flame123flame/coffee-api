package coffee.transaction.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.transaction.Transactional;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import coffee.lottocancel.service.LottoCancelService;
import coffee.lottocancel.vo.req.LottoCancelReq;
import coffee.model.LottoTransaction;
import coffee.repo.dao.LottoTransactionDao;
import coffee.repo.jpa.LottoGroupTransactionRepo;
import coffee.repo.jpa.LottoTransactionRepo;
import coffee.transaction.vo.req.GetTransBoReq;
import coffee.transaction.vo.res.TransactionPayCostRes;
import coffee.transaction.vo.res.TransactionRes;
import framework.constant.LottoConstant;
import framework.utils.UserLoginUtil;
@Service
public class TransactionService {
	
	@Value("${common-path.bo}")
	private String baseApiBo;

	@Autowired
	private LottoTransactionDao lottoTransactionDao;
	
	@Autowired
	private LottoTransactionRepo lottoTransactionRepo;
	
	@Autowired
	LottoGroupTransactionRepo lottoGroupTransactionRepo;
	
	@Autowired
	LottoCancelService lottoCancelService;
	
	public List<TransactionRes> getAllTransaction() {
		return lottoTransactionDao.getAllTransaction();
	}
	
	public List<TransactionRes> getTransactionByGroup(String groupCode) {
		return lottoTransactionDao.getTransactionByGroup(groupCode);
	}
	
	public List<LottoTransaction> getTransactionBetween(GetTransBoReq req){
		return lottoTransactionRepo.findByUpdatedDateBetween(req.getTimeStart() , req.getTimeEnd());
	}
	
	
	public List<LottoTransaction> getLottoGroupByLottoClassCode(String classCode){
		List<LottoTransaction> resData = new ArrayList<LottoTransaction>();
		List<String> dataList = lottoTransactionRepo.findInstallmentByLottoClassCode(classCode, LottoConstant.LOTTO_STATUS.PENDING);
		for(String item: dataList) {
			LottoTransaction resItem = new LottoTransaction();
			resItem.setInstallment(item);
			resData.add(resItem);
		}
		return resData;
		
	}
	
	public List<Integer> getRoundByInstallment(String installment, String lottoClassCode) {
		System.out.println("getRoundByInstallment() ::::"+installment);
		List<Integer> resData = lottoTransactionRepo.findRoundByInstallment(installment, LottoConstant.LOTTO_STATUS.PENDING, lottoClassCode);
		System.out.println("getRoundByInstallment() ::::"+resData);
		return resData;
	}

	@Transactional
	public void cancelLotto(String categoryCode, String classCode, String installment, Integer roundYeekee, String cancelRemark) throws Exception {
//		System.out.println("cancelLotto() ::::");
		Date date = new Date();
		String createdBy = UserLoginUtil.getUsername();
		List<TransactionPayCostRes> resData = null;
		
		LottoCancelReq  LottoCancelReq = new LottoCancelReq();
		LottoCancelReq.setLottoCategoryCode(categoryCode);
		LottoCancelReq.setLottoClassCode(classCode);
		LottoCancelReq.setInstallment(installment);
		LottoCancelReq.setRoundYeekee(roundYeekee);
		lottoCancelService.createLottoCancel(LottoCancelReq);
		
		if(categoryCode.equals(LottoConstant.CATEGORY.YEEKEE)) {
			System.out.println(classCode +":::" +installment+ ":::" +roundYeekee+ ":::" +createdBy);
			lottoTransactionRepo.updateTransactionLottoYeekee(LottoConstant.LOTTO_STATUS.CANCEL, date, classCode, installment, roundYeekee, createdBy);
			lottoGroupTransactionRepo.updateGroupTransactionLottoYeekee(classCode, installment, roundYeekee, createdBy, cancelRemark, LottoConstant.LOTTO_STATUS.CANCEL);
			resData= getAllPayCost(categoryCode, classCode, installment, roundYeekee);
//			System.out.println("Pay Cost :::" + resData);
		}
		else {
			System.out.println("cancelLotto() :::" + classCode +":::" +installment+ ":::" +createdBy);
			lottoTransactionRepo.updateTransactionLotto(LottoConstant.LOTTO_STATUS.CANCEL, date, classCode, installment, createdBy);
			lottoGroupTransactionRepo.updateGroupTransactionLotto(classCode, installment, createdBy, cancelRemark, LottoConstant.LOTTO_STATUS.CANCEL);
			resData = getAllPayCost(categoryCode, classCode, installment, roundYeekee);
//			System.out.println("Pay Cost :::" + resData);
		}
	}

	public List<TransactionPayCostRes> getAllPayCost(String lottoCategoryCode, String lottoClassCode, String installment, Integer roundYeekee) throws Exception{
		
		System.out.println("getAllPayCost() :::" + lottoCategoryCode + ":::" +lottoClassCode+ ":::" +installment+":::" + roundYeekee);
		List<TransactionPayCostRes> resData = lottoTransactionDao.findAllPayCost(lottoCategoryCode, lottoClassCode, installment, roundYeekee);

		SSLContext context = SSLContext.getInstance("TLSv1.2");
		context.init(null, null, null);
		CloseableHttpClient httpClient = HttpClientBuilder.create().setSSLContext(context).build();
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
		RestTemplate restTemplate = new RestTemplate(factory);
		HttpHeaders headers = new HttpHeaders();
//		String url = "http://localhost:8080/COFFEE" +"/api/lotto-provider/update-wallet-lotto-cancel"; 
		String url = baseApiBo +"/api/lotto-provider/update-wallet-lotto-cancel"; 
		HttpEntity<List<TransactionPayCostRes>> reqEntity = new HttpEntity<List<TransactionPayCostRes>>(resData, headers);
//		System.out.println("::::::::::::::::::::::::::::"+reqEntity);
//		ResponseEntity<TransactionPayCostRes> dataResStr = 
		restTemplate.exchange(url, HttpMethod.POST, reqEntity, TransactionPayCostRes.class);
//		System.out.println("::::::::::::::CCCCC::::::::::::::" + dataResStr);
		
		return resData;
	}
}
