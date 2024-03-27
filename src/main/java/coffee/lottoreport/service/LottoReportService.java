package coffee.lottoreport.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coffee.dashboard.vo.res.SumPrizeRes;
import coffee.lottoreport.vo.res.LottoInstallmentRes;
import coffee.lottoreport.vo.res.LottoReportRes;
import coffee.lottoreport.vo.res.LottoReportUpdatedResultYeekeeRes;
import coffee.lottoreport.vo.res.LottoSumWin;
import coffee.repo.dao.LottoReportDao;
import framework.model.DataTableResponse;
import framework.model.DatatableRequest;

@Service
public class LottoReportService {

	@Autowired
	private LottoReportDao lottoReportDao;

	public List<LottoInstallmentRes> getAllLottoInstallment(String classCode) {
		List<LottoInstallmentRes> dataSet = lottoReportDao.getAllInstallment(classCode);
		return dataSet;
	}

	public List<LottoReportRes> getSumLottoTransaction(String classCode, String installment) {
		List<LottoReportRes> dataSet = lottoReportDao.getReportTransactionLotto(classCode, installment);
		return dataSet;
	}

	public List<LottoSumWin> getLottoSumWin(String classCode, String installment) {
		List<LottoSumWin> dataSet = lottoReportDao.getLottoSumWin(classCode, installment);

		List<LottoSumWin> dataRes = new ArrayList<LottoSumWin>();
		for (LottoSumWin dataInfo : dataSet) {
			LottoSumWin dataSet1 = new LottoSumWin();
			dataSet1.setCountSeq(dataInfo.getCountSeq());
			dataSet1.setInstallment(dataInfo.getInstallment());
			dataSet1.setLottoClassCode(dataInfo.getLottoClassCode());
			dataSet1.setLottoKindCode(dataInfo.getLottoKindCode());
			dataSet1.setMsdLottoKindName(dataInfo.getMsdLottoKindName());
			dataSet1.setPrizeCorrect(dataInfo.getPrizeCorrect());
			dataSet1.setSumMsdWin(dataInfo.getSumMsdWin());
			dataSet1.setSumPayCorrectWin(dataInfo.getSumPayCorrectWin());
			dataSet1.setSumPrizeCorrectWin(dataInfo.getSumPrizeCorrectWin());
			dataSet1.setSumProfit(dataInfo.getSumPayCorrectWin().subtract(dataInfo.getSumPrizeCorrectWin()));
			dataRes.add(dataSet1);

		}
		return dataRes;
	}

	public List<LottoReportRes> getAllLottoTransactionByKindCode(String classCode, String kindCode,
			String installment) {
		List<LottoReportRes> dataSet = lottoReportDao.getSumLottoTransactionByClassCode(classCode, kindCode,
				installment);

		return dataSet;
	}

	public DataTableResponse<LottoInstallmentRes> paginateDashboard(DatatableRequest req, String lottoClassCode) {
		DataTableResponse<LottoInstallmentRes> paginateData = lottoReportDao.paginateDashboard(req, lottoClassCode);
		DataTableResponse<LottoInstallmentRes> dataTable = new DataTableResponse<>();
		List<LottoInstallmentRes> data = paginateData.getData();
		dataTable.setRecordsTotal(paginateData.getRecordsTotal());
		dataTable.setDraw(paginateData.getDraw());
		dataTable.setData(data);
		dataTable.setPage(req.getPage());
		return paginateData;
	}

	public DataTableResponse<LottoInstallmentRes> getReportRoundYeekee(DatatableRequest req, String lottoClassCode,
			String installment) {
		System.out.println("::::::::::::::::::::::::> " + installment + " ==" + lottoClassCode);
		DataTableResponse<LottoInstallmentRes> paginateData = lottoReportDao.getReportRoundYeekee(req, lottoClassCode,
				installment);
		DataTableResponse<LottoInstallmentRes> dataTable = new DataTableResponse<>();
		List<LottoInstallmentRes> data = paginateData.getData();
		dataTable.setRecordsTotal(paginateData.getRecordsTotal());
		dataTable.setDraw(paginateData.getDraw());
		dataTable.setData(data);
		dataTable.setPage(req.getPage());
		return paginateData;
	}

	// ====================== LOTTO REPORT YEEKEE ========================
	public DataTableResponse<LottoInstallmentRes> paginateDashboardYeekee(DatatableRequest req, String lottoClassCode) {
		DataTableResponse<LottoInstallmentRes> paginateData = lottoReportDao.paginateDashboardYeekee(req, lottoClassCode);
		DataTableResponse<LottoInstallmentRes> dataTable = new DataTableResponse<>();
		List<LottoInstallmentRes> data = paginateData.getData();
		dataTable.setRecordsTotal(paginateData.getRecordsTotal());
		dataTable.setDraw(paginateData.getDraw());
		dataTable.setData(data);
		dataTable.setPage(req.getPage());
		return paginateData;
	}

	
	
	public DataTableResponse<LottoReportRes> paginateSumLottoTransactionYeekee(DatatableRequest req) {
		DataTableResponse<LottoReportRes> paginateData = lottoReportDao.paginateSumLottoTransactionYeekee(req);
		DataTableResponse<LottoReportRes> dataTable = new DataTableResponse<>();
		List<LottoReportRes> data = paginateData.getData();
		dataTable.setRecordsTotal(paginateData.getRecordsTotal());
		dataTable.setDraw(paginateData.getDraw());
		dataTable.setData(data);
		dataTable.setPage(req.getPage());
		return paginateData;
	}

	public BigDecimal getLottoPrizeCorrectRes(String classCode, String installment, String roundYeekee) {
		BigDecimal sum = lottoReportDao.getLottoPrizeCorrectRes(classCode, installment, roundYeekee);
		return sum;
	}

	public List<LottoSumWin> getLottoSumWinYeekee(String classCode, String installment, String roundYeekee) {
		List<LottoSumWin> dataSet = lottoReportDao.getLottoSumWinYeekee(classCode, installment, roundYeekee);

		List<LottoSumWin> dataRes = new ArrayList<LottoSumWin>();
		for (LottoSumWin dataInfo : dataSet) {
			LottoSumWin dataSet1 = new LottoSumWin();
			dataSet1.setCountSeq(dataInfo.getCountSeq());
			dataSet1.setInstallment(dataInfo.getInstallment());
			dataSet1.setLottoClassCode(dataInfo.getLottoClassCode());
			dataSet1.setLottoKindCode(dataInfo.getLottoKindCode());
			dataSet1.setMsdLottoKindName(dataInfo.getMsdLottoKindName());
			dataSet1.setPrizeCorrect(dataInfo.getPrizeCorrect());
			dataSet1.setSumMsdWin(dataInfo.getSumMsdWin());
			dataSet1.setSumPayCorrectWin(dataInfo.getSumPayCorrectWin());
			dataSet1.setSumPrizeCorrectWin(dataInfo.getSumPrizeCorrectWin());
			dataSet1.setSumProfit(dataInfo.getSumPayCorrectWin().subtract(dataInfo.getSumPrizeCorrectWin()));
			dataRes.add(dataSet1);

		}
		return dataRes;
	}

//	public List<LottoReportRes> getLottoTransactionYeekeeByKindCode(String classCode, String kindCode, String installment,
//			String roundYeekee) {
//		List<LottoReportRes> dataSet = lottoReportDao.getSumLottoTransactionByClassCodeYeekee(classCode, kindCode, installment, roundYeekee);
//		return dataSet;
//	}
	
	public DataTableResponse<LottoReportRes> paginateLottoTransactionYeekeeByKindCode(DatatableRequest req) {
		DataTableResponse<LottoReportRes> paginateData = lottoReportDao.paginateLottoTransactionYeekeeByKindCode(req);
		DataTableResponse<LottoReportRes> dataTable = new DataTableResponse<>();
		List<LottoReportRes> data = paginateData.getData();
		dataTable.setRecordsTotal(paginateData.getRecordsTotal());
		dataTable.setDraw(paginateData.getDraw());
		dataTable.setData(data);
		dataTable.setPage(req.getPage());
		return paginateData;
	}

	public DataTableResponse<LottoReportUpdatedResultYeekeeRes> paginateUpdatedLottoResultYeekeeSumNumber(DatatableRequest req) {
		DataTableResponse<LottoReportUpdatedResultYeekeeRes> paginateData = lottoReportDao.paginateUpdatedLottoResultYeekeeSumNumber(req);
		DataTableResponse<LottoReportUpdatedResultYeekeeRes> dataTable = new DataTableResponse<>();
		List<LottoReportUpdatedResultYeekeeRes> data = paginateData.getData();
		dataTable.setRecordsTotal(paginateData.getRecordsTotal());
		dataTable.setDraw(paginateData.getDraw());
		dataTable.setData(data);
		dataTable.setPage(req.getPage());
		return paginateData;
	}

	// ====================== LOTTO REPORT YEEKEE ========================
}
