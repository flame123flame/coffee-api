package coffee.confirmtransaction.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import coffee.confirmtransaction.vo.ConfirmTransactionRes;
import coffee.lottoresult.vo.req.thai.SendKeyBoReq;
import coffee.lottoresult.vo.req.thai.SendKeyBoReq.SendBoReq;
import coffee.model.LottoTransaction;
import coffee.repo.dao.ConfirmTransactionDao;
import coffee.repo.jpa.LottoCategoryRepo;
import coffee.repo.jpa.LottoClassRepository;
import coffee.repo.jpa.LottoTransactionRepo;
import framework.constant.ProjectConstant;
import framework.model.DataTableResponse;
import framework.model.DatatableRequest;
import framework.model.ResponseData;
import framework.utils.HttpClientUtil;
import framework.utils.UserLoginUtil;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Service
public class ConfirmTransactionService {

	@Autowired
	private ConfirmTransactionDao confirmTransactionDao;

	@Autowired
	private LottoTransactionRepo lottoTransactionRepo;

	@Autowired
	private LottoClassRepository lottoClassRepository;

	@Autowired
	private LottoCategoryRepo lottoCategoryRepo;

	@Value("${common-path.bo}")
	private String baseApiBo;

	public List<ConfirmTransactionRes> getAllConfirmTransaction() {
		List<ConfirmTransactionRes> dataSet = confirmTransactionDao.getAllConfirmTransaction();
		return dataSet;
	}

	public List<ConfirmTransactionRes> getAllTransaction() {
		List<ConfirmTransactionRes> dataSet = confirmTransactionDao.getAllTransaction();
		return dataSet;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public String confirmTransaction(Boolean status, Long id, String remark) throws Exception {
		LottoTransaction data = lottoTransactionRepo.findById(id).get();
		System.out.println(status);
		if (status) {
			data.setUpdateWallet(status);
			data.setRejectRemark(null);
			data.setPaidAt(new Date());
			data.setPaidBy(UserLoginUtil.getUsername());
		} else {
			data.setPaidAt(new Date());
			data.setPaidBy(UserLoginUtil.getUsername());
			data.setUpdateWallet(status);
			data.setRejectRemark(remark);
		}
		lottoTransactionRepo.save(data);

		if (status) {
			List<SendBoReq> sendBoReq = new ArrayList<SendKeyBoReq.SendBoReq>();
			SendBoReq dataSet = new SendBoReq();
			dataSet.setUsername(data.getUsername());
			dataSet.setPrize(String.valueOf(data.getPrizeCorrect()));
			sendBoReq.add(dataSet);
			String sig = "";
			SendKeyBoReq body = new SendKeyBoReq(sig, sendBoReq);
			RestTemplate restTemplate = HttpClientUtil.getInstant();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<SendKeyBoReq> entity = new HttpEntity<>(body, headers);
			ResponseData<String> dataResStr = restTemplate
					.postForObject(baseApiBo + "/api/lotto-provider/update-wallet-prize", entity, ResponseData.class);

			int count = 0;
			while (!ProjectConstant.STATUS.SUCCESS.equals(dataResStr.getData())) {
				Thread.sleep(1000);
				dataResStr = restTemplate.postForObject(baseApiBo + "/api/lotto-provider/update-wallet-prize", entity,
						ResponseData.class);
				count++;
				if (count > 3) {
					break;
				}
			}
			if (!ProjectConstant.STATUS.SUCCESS.equals(dataResStr.getData())) {
				new Throwable("Update Money Fail");
			}
		}

		return null;
	}

	public DataTableResponse<ConfirmTransactionRes> getAllPaginateConfirmTransaction(DatatableRequest req) {
		DataTableResponse<ConfirmTransactionRes> paginateData = confirmTransactionDao
				.getAllPaginateConfirmTransaction(req);
		DataTableResponse<ConfirmTransactionRes> dataTable = new DataTableResponse<>();
		List<ConfirmTransactionRes> data = paginateData.getData();
		dataTable.setRecordsTotal(paginateData.getRecordsTotal());
		dataTable.setDraw(paginateData.getDraw());
		dataTable.setData(data);
		dataTable.setPage(req.getPage());
		return paginateData;
	}

	public DataTableResponse<ConfirmTransactionRes> getPaginateTransaction(DatatableRequest req) {
		DataTableResponse<ConfirmTransactionRes> paginateData = confirmTransactionDao.getPaginateTransaction(req);
		DataTableResponse<ConfirmTransactionRes> dataTable = new DataTableResponse<>();
		List<ConfirmTransactionRes> data = paginateData.getData();
		dataTable.setRecordsTotal(paginateData.getRecordsTotal());
		dataTable.setDraw(paginateData.getDraw());
		dataTable.setData(data);
		dataTable.setPage(req.getPage());
		return paginateData;
	}
	
	public Integer getCountCheckingConfirmTransaction() {
		Integer count = confirmTransactionDao.getCountConfirmTransaction();
		if (count != null) {
			return count;
		}
		return 0;
	}

}
