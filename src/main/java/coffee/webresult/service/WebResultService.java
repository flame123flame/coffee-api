package coffee.webresult.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coffee.buy.service.BuyLottoService;
import coffee.buy.vo.res.Installment;
import coffee.lottoconfig.vo.res.TimeSellRes;
import coffee.model.LottoCategory;
import coffee.model.LottoClass;
import coffee.model.LottoResult;
import coffee.model.TimeSell;
import coffee.model.YeekeeSumNumber;
import coffee.repo.dao.LottoClassDao;
import coffee.repo.dao.LottoResultDao;
import coffee.repo.dao.YeekeeResultDao;
import coffee.repo.jpa.LottoCategoryRepo;
import coffee.repo.jpa.LottoClassRepository;
import coffee.repo.jpa.LottoResultRepo;
import coffee.repo.jpa.MsdLottoKindRepository;
import coffee.repo.jpa.TimeSellRepo;
import coffee.repo.jpa.YeekeeSumNumberRepo;
import coffee.web.service.WebTimeSellService;
import coffee.webresult.vo.LottoWebResultRes;
import coffee.webresult.vo.WebResultStocksRes;
import coffee.webresult.vo.WebResultStocksRes.StocksWebRes;
import coffee.webresult.vo.WebResultYeekeeRes;
import framework.constant.LottoConstant;
import framework.constant.LottoConstant.CATEGORY;
import framework.constant.LottoConstant.LOTTO_KIND;
import framework.utils.ConvertDateUtils;

@Service
public class WebResultService {

	@Autowired
	LottoResultRepo lottoResultRepo;

	@Autowired
	LottoCategoryRepo lottoCategoryRepo;

	@Autowired
	LottoClassRepository lottoClassRepo;

	@Autowired
	LottoClassDao lottoClassDao;

	@Autowired
	TimeSellRepo timeSellRepo;

	@Autowired
	MsdLottoKindRepository msdlottoKindRepo;

	@Autowired
	LottoResultDao lottoResultDao;

	@Autowired
	YeekeeResultDao yeekeeResultDao;

	@Autowired
	YeekeeSumNumberRepo yeekeeSumNumberRepo;

	@Autowired
	private WebTimeSellService webTimeSellService;

	@Autowired
	private BuyLottoService buyLottoService;

	public List<LottoWebResultRes> getlottoWebResultGovernment() {

		List<LottoClass> dataLottoClassList = lottoClassRepo.findByLottoCategoryCode(LottoConstant.CATEGORY.GOVERNMENT);
		List<LottoWebResultRes> dataLottoGovRes = new ArrayList<LottoWebResultRes>();

		for (LottoClass dataLottoClassInfo : dataLottoClassList)
		{
			LottoWebResultRes dataLottoGovSet =  new LottoWebResultRes();
			dataLottoGovSet.setLottoClassName(dataLottoClassInfo.getClassName());
			dataLottoGovSet.setLottoFlag(dataLottoClassInfo.getLottoClassImg());
			dataLottoGovSet.setLottoCategoryCode(dataLottoClassInfo.getLottoCategoryCode());
			dataLottoGovSet.setLottoClassCode(dataLottoClassInfo.getLottoClassCode());
			
			
			List<LottoResult> dataLottoResult = null;
			
			List<TimeSell> timeFind = timeSellRepo.findByLottoClassCode(dataLottoClassInfo.getLottoClassCode());
			Installment installment = buyLottoService.checkInstallment(dataLottoClassInfo.getLottoClassCode(), timeFind, dataLottoClassInfo);
			Date timeIns;
			if (LottoConstant.TYPE_INSTALLMENT.HR24.equals(dataLottoClassInfo.getTypeInstallment())) {
				timeIns = installment.getTimeOpen();
			} else {
				timeIns = installment.getTimeClose();
			}

			String installmentStr = webTimeSellService.getInstallmentStr(dataLottoClassInfo.getTypeInstallment(), timeIns,
					installment.getLengthTimeSell());

			installment.setTimeOpenStr(ConvertDateUtils.formatDateToString(installment.getTimeOpen(),
					ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
			installment.setTimeCloseStr(ConvertDateUtils.formatDateToString(installment.getTimeClose(),
					ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
			
			dataLottoGovSet.setTimeOpenStr(installment.getTimeOpen());
			Date installmentStrSet = ConvertDateUtils.parseStringToDate(installmentStr,
					ConvertDateUtils.DD_MM_YYYY, ConvertDateUtils.LOCAL_EN);
			dataLottoGovSet.setInstallment(ConvertDateUtils.formatDateToString(installmentStrSet,
					ConvertDateUtils.YYYY_MM_DD, ConvertDateUtils.LOCAL_EN));
			
			List<Installment> listTimeSet = webTimeSellService.getListInstallmentByType(dataLottoClassInfo.getTypeInstallment(),
					timeFind, dataLottoClassInfo.getIgnoreWeekly());
			
			dataLottoResult = lottoResultRepo.findByLottoClassCodeAndLottoResultInstallmentAndStatus(dataLottoClassInfo.getLottoClassCode(), installmentStr, "APPROVE");
			List<String> listFront = new ArrayList<String>();
			List<String> listBot = new ArrayList<String>();
			
			if(dataLottoResult == null)
			{
				dataLottoResult = lottoResultDao.getLottoResultGovLast(dataLottoClassInfo.getLottoClassCode());
			}
			if(dataLottoResult.size()==0)
			{
				dataLottoResult = lottoResultDao.getLottoResultGovLast(dataLottoClassInfo.getLottoClassCode());
			}
			
			
			for (LottoResult dataResultInfor : dataLottoResult) {
				if (LOTTO_KIND.DIGIT3_TOP.equals(dataResultInfor.getMsdLottoKindCode())) {
					dataLottoGovSet.setDigit3Top(dataResultInfor.getLottoNumber());
				}
				if (LOTTO_KIND.DIGIT3_FRONT.equals(dataResultInfor.getMsdLottoKindCode())) {
					listFront.add(dataResultInfor.getLottoNumber());
					dataLottoGovSet.setDigit3Front(listFront);
				}
				if (LOTTO_KIND.DIGIT3_BOT.equals(dataResultInfor.getMsdLottoKindCode())) {

					listBot.add(dataResultInfor.getLottoNumber());
					dataLottoGovSet.setDigit3Bot(listBot);
				}
				if (LOTTO_KIND.DIGIT2_BOT.equals(dataResultInfor.getMsdLottoKindCode())) {

					dataLottoGovSet.setDigit2Bot(dataResultInfor.getLottoNumber());
				}
				if (LOTTO_KIND.DIGIT2_TOP.equals(dataResultInfor.getMsdLottoKindCode())) {

					dataLottoGovSet.setDigit2Bot(dataResultInfor.getLottoNumber());
				}

			}
			dataLottoGovRes.add(dataLottoGovSet);
		}

		return dataLottoGovRes;
	}

	public List<WebResultYeekeeRes> getlottoWebResultYeekee() {

		List<LottoClass> dataFind = lottoClassRepo.findByLottoCategoryCode(LottoConstant.CATEGORY.YEEKEE);
		List<WebResultYeekeeRes> dataRes = new ArrayList<WebResultYeekeeRes>();

		for (LottoClass dataClass : dataFind) {

			WebResultYeekeeRes dataSetlotto = new WebResultYeekeeRes();
			dataSetlotto.setLottoCategoryCode(dataClass.getLottoCategoryCode());
			dataSetlotto.setLottoClassCode(dataClass.getLottoClassCode());
			dataSetlotto.setLottoClassName(dataClass.getClassName());
			dataSetlotto.setLottoFlag(dataClass.getLottoClassImg());
			dataSetlotto.setRound(dataClass.getCountTime());

			YeekeeSumNumber dataSearch = yeekeeSumNumberRepo
					.findFirst1ByClassCodeOrderByInstallmentDesc(dataClass.getLottoClassCode());
			Date installmentSet =  ConvertDateUtils.parseStringToDate(dataSearch.getInstallment(), ConvertDateUtils.DD_MM_YYYY, ConvertDateUtils.LOCAL_EN);
			dataSetlotto.setInstallment(ConvertDateUtils.formatDateToString(installmentSet, ConvertDateUtils.YYYY_MM_DD, ConvertDateUtils.LOCAL_EN));

			List<YeekeeSumNumber> yeekeeList = yeekeeResultDao
					.getYeekeeSumNumberResultLast(dataClass.getLottoClassCode(), dataSearch.getInstallment());

			List<WebResultYeekeeRes.YeekeeWebRes> yeekeeResList = new ArrayList<WebResultYeekeeRes.YeekeeWebRes>();

			for (YeekeeSumNumber dataInfor : yeekeeList) {
				WebResultYeekeeRes.YeekeeWebRes yeekeeSet = new WebResultYeekeeRes.YeekeeWebRes();

				if (dataInfor.getNumberResult() != null) {
					String digitNumber = dataInfor.getNumberResult().toString();
					String resultNumber = StringUtils.leftPad(digitNumber, 5, "0");
					resultNumber = resultNumber.substring(resultNumber.length() - 5, resultNumber.length());
					yeekeeSet.setDigit3TopLottoNumber(resultNumber.substring(2, 5));
					yeekeeSet.setDigit2BotLottoNumber(resultNumber.substring(0, 2));
					yeekeeSet.setRoundYeekee(dataInfor.getRoundNumber().toString());
				} else {
					yeekeeSet.setRoundYeekee(dataInfor.getRoundNumber().toString());
				}
				yeekeeResList.add(yeekeeSet);
			}
			dataSetlotto.setYeekeeList(yeekeeResList);

			dataRes.add(dataSetlotto);
		}

		return dataRes;
	}

	public List<WebResultStocksRes> getWebResultStocks() {
		List<WebResultStocksRes> dataStocksResList = new ArrayList<WebResultStocksRes>();
		WebResultStocksRes dataStocksRes = new WebResultStocksRes();

		LottoCategory categoryData = lottoCategoryRepo.findByLottoCategoryCode(CATEGORY.STOCKS).get();
		
		dataStocksRes.setLottoCategoryName(categoryData.getTypeName());
		dataStocksRes.setLottoCategoryCode(categoryData.getLottoCategoryCode());
		dataStocksRes.setDateStocks(new Date());

		String installmentFind = ConvertDateUtils.formatDateToString(new Date(), ConvertDateUtils.YYYY_MM_DD,
				ConvertDateUtils.LOCAL_EN);

		List<LottoClass> stocksDataList = lottoClassRepo.findByLottoCategoryCode(CATEGORY.STOCKS);

		List<LottoResult> stocksResultList = null;

		dataStocksRes.setInstallment(installmentFind);

		List<WebResultStocksRes.StocksWebRes> stocksDataResList = new ArrayList<WebResultStocksRes.StocksWebRes>();
		for (LottoClass stockDataInfo : stocksDataList) {
			StocksWebRes dataStocksSet = new StocksWebRes();
			dataStocksSet.setLottoFlag(stockDataInfo.getLottoClassImg());
			dataStocksSet.setStocksName(stockDataInfo.getClassName());
			dataStocksSet.setLottoClassCode(stockDataInfo.getLottoClassCode());

			List<TimeSell> timeFind = timeSellRepo.findByLottoClassCode(stockDataInfo.getLottoClassCode());
			Installment installment = buyLottoService.checkInstallment(stockDataInfo.getLottoClassCode(), timeFind, stockDataInfo);
			Date timeIns;
			if (LottoConstant.TYPE_INSTALLMENT.HR24.equals(stockDataInfo.getTypeInstallment())) {
				timeIns = installment.getTimeOpen();
			} else {
				timeIns = installment.getTimeClose();
			}

			String installmentStr = webTimeSellService.getInstallmentStr(stockDataInfo.getTypeInstallment(), timeIns,
					installment.getLengthTimeSell());

			installment.setTimeOpenStr(ConvertDateUtils.formatDateToString(installment.getTimeOpen(),
					ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
			installment.setTimeCloseStr(ConvertDateUtils.formatDateToString(installment.getTimeClose(),
					ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
			
			Date installmentStrSet = ConvertDateUtils.parseStringToDate(installmentStr,
					ConvertDateUtils.DD_MM_YYYY, ConvertDateUtils.LOCAL_EN);
			dataStocksRes.setInstallment(ConvertDateUtils.formatDateToString(installmentStrSet,
					ConvertDateUtils.YYYY_MM_DD, ConvertDateUtils.LOCAL_EN));
			
			stocksResultList = lottoResultRepo.findByLottoClassCodeAndLottoResultInstallmentAndStatus(
					stockDataInfo.getLottoClassCode(), installmentStr, "APPROVE");

			if (stocksResultList.size()==0) {
				stocksResultList = lottoResultDao.getLottoStocksResultLast(stockDataInfo.getLottoClassCode(),
						"APPROVE");
				if(stocksResultList.size()>0)
				{
					dataStocksRes.setInstallment(stocksResultList.get(0).getLottoResultInstallment());
				}
				
			}

			for (LottoResult stocksResultInfo : stocksResultList) {
				if (LottoConstant.LOTTO_KIND.DIGIT3_TOP.contains(stocksResultInfo.getMsdLottoKindCode())) {
					dataStocksSet.setDigit3TopLottoNumber(stocksResultInfo.getLottoNumber());
				}
				if (LottoConstant.LOTTO_KIND.DIGIT2_BOT.contains(stocksResultInfo.getMsdLottoKindCode())) {
					dataStocksSet.setDigit2BotLottoNumber(stocksResultInfo.getLottoNumber());
				}

			}
			stocksDataResList.add(dataStocksSet);
		}
		dataStocksRes.setStocksList(stocksDataResList);
		dataStocksResList.add(dataStocksRes);
		return dataStocksResList;
	}

}
