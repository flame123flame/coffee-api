package coffee.dashboard.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import coffee.dashboard.vo.res.DashBroardMainRes;
import coffee.dashboard.vo.res.DashBroardMainRes.LottoListDashBoard;
import coffee.dashboard.vo.res.DashBroardMainRes.LottoSwappedSum;
import coffee.dashboard.vo.res.SumDashboardRes;
import coffee.dashboard.vo.res.SumPrizeKindCode;
import coffee.dashboard.vo.res.SumPrizeList;
import coffee.dashboard.vo.res.SumPrizeRes;
import coffee.model.MsdLottoKind;
import coffee.repo.dao.MsdLottoKindDao;
import coffee.repo.dao.SumPrizeDao;
import coffee.repo.jpa.MsdLottoKindRepository;
import framework.constant.LottoConstant.LOTTO_KIND;
import framework.utils.TimeUtil;

@Service
public class DashboardService {

	@Autowired
	private SumPrizeDao sumPrizeDao;

	@Autowired
	private MsdLottoKindRepository msdLottoKindRepo;

	@Autowired
	private MsdLottoKindDao msdLottoKindDao;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Transactional
	public DashBroardMainRes getDashboard(String classCode) {
		DashBroardMainRes dataRes = sumPrizeDao.getDashboard(classCode);
		List<LottoListDashBoard> listSet = new ArrayList<LottoListDashBoard>();
		if (dataRes == null) {
			return dataRes;
		}
		// List<MsdLottoKind> listMsd = msdLottoKindRepo.findAll();
		List<MsdLottoKind> listMsd = msdLottoKindDao.findMsdLottoInKindByClassCode(classCode);
		for (MsdLottoKind msdLottoKind : listMsd) {
			LottoListDashBoard dataSet = new LottoListDashBoard();
			dataSet.setKindCode(msdLottoKind.getMsdLottoKindCode());
			dataSet.setKindName(msdLottoKind.getMsdLottoKindName());
			List<SumPrizeList> listSumprize = new ArrayList<SumPrizeList>();
			if (msdLottoKind.getMsdLottoKindCode().equals(LOTTO_KIND.DIGIT3_SWAPPED)) {
				List<LottoSwappedSum> swappedSum = sumPrizeDao.getPrizeLottoSwapped(LOTTO_KIND.DIGIT3_SWAPPED,
						classCode);
				for (LottoSwappedSum dataInfor : swappedSum) {
					SumPrizeList sumPrizeSet = new SumPrizeList();
					sumPrizeSet.setLottoNumber(dataInfor.getLottoNumber());
					sumPrizeSet.setSumPrizeCost(dataInfor.getSumPrizeCost());
					sumPrizeSet.setSumPayCost(dataInfor.getSumPayCost());
					sumPrizeSet.setTierLevel(dataInfor.getTierLevel());
					listSumprize.add(sumPrizeSet);
				}
			} else if (LOTTO_KIND.DIGIT3_TOP.equals(msdLottoKind.getMsdLottoKindCode())) {
				listSumprize = sumPrizeDao.getTopPrizeAndlimit3Top(msdLottoKind.getMsdLottoKindCode(), classCode);
			} else {
				listSumprize = sumPrizeDao.getTopPrizeAndlimit(msdLottoKind.getMsdLottoKindCode(), classCode);
			}
			dataSet.setPrizeList(listSumprize);
			if (listSumprize.size() > 0) {
				dataSet.setSumBet(sumPrizeDao.sumBetKind(msdLottoKind.getMsdLottoKindCode(), classCode));
			}
			listSet.add(dataSet);
		}

		LottoListDashBoard dataSets = new LottoListDashBoard();
		dataSets.setKindCode("DIGI3_MIX");
		dataSets.setKindName("DIGI3MIX");
		List<SumPrizeList> listSumprize = new ArrayList<SumPrizeList>();
		listSumprize = sumPrizeDao.getTopPrizeAndlimit(LOTTO_KIND.DIGIT3_TOP, classCode);
		dataSets.setPrizeList(listSumprize);
		if (listSumprize.size() > 0) {
			dataSets.setSumBet(sumPrizeDao.sumBetKind(LOTTO_KIND.DIGIT3_TOP, classCode));
		}
		listSet.add(dataSets);

		dataRes.setLottoListLv(listSet);
		return dataRes;
	}

	@Transactional
	public SumDashboardRes getAllSumDasboard(String classCode) {
		SumDashboardRes dataRes = new SumDashboardRes();
		dataRes.setClassCode(classCode);
		List<SumPrizeRes> sumData = sumPrizeDao.getSumPrize(classCode);

		List<SumPrizeRes> list = new ArrayList<SumPrizeRes>();
		// sumData.setSumProfit(sumData.getSumPrizeCost().subtract(sumData.getSumPayCost()));

		for (SumPrizeRes dataInfo : sumData) {
			BigDecimal total1 = BigDecimal.ZERO;
			if (dataInfo.getMsdLottoKindCode().equals(LOTTO_KIND.DIGIT3_BOT)) {
				List<SumPrizeKindCode> dataList = sumPrizeDao.getSumPrizeByCode(classCode, LOTTO_KIND.DIGIT3_BOT);
				SumPrizeRes dataSet = new SumPrizeRes();
				dataSet.setMsdLottoKindCode(dataInfo.getMsdLottoKindCode());
				dataSet.setMsdLottoKindName(dataInfo.getMsdLottoKindName());
				dataSet.setSumPayCost(dataInfo.getSumPayCost());
				for (SumPrizeKindCode dataKindCode : dataList) {
					total1 = total1.add(dataKindCode.getSumPrizeCost());
				}
				dataSet.setSumPrizeCost(total1);
				dataSet.setSumProfit(dataInfo.getSumPayCost().subtract(total1));
				list.add(dataSet);
			} else if (dataInfo.getMsdLottoKindCode().equals(LOTTO_KIND.DIGIT3_FRONT)) {
				List<SumPrizeKindCode> dataList = sumPrizeDao.getSumPrizeByCode(classCode, LOTTO_KIND.DIGIT3_FRONT);
				SumPrizeRes dataSet = new SumPrizeRes();
				dataSet.setMsdLottoKindCode(dataInfo.getMsdLottoKindCode());
				dataSet.setMsdLottoKindName(dataInfo.getMsdLottoKindName());
				dataSet.setSumPayCost(dataInfo.getSumPayCost());
				for (SumPrizeKindCode dataKindCode : dataList) {
					total1 = total1.add(dataKindCode.getSumPrizeCost());
				}
				dataSet.setSumPrizeCost(total1);
				dataSet.setSumProfit(dataInfo.getSumPayCost().subtract(total1));
				list.add(dataSet);
			} else {
				SumPrizeRes dataSet = new SumPrizeRes();
				dataSet.setMsdLottoKindCode(dataInfo.getMsdLottoKindCode());
				dataSet.setMsdLottoKindName(dataInfo.getMsdLottoKindName());
				dataSet.setSumPayCost(dataInfo.getSumPayCost());
				dataSet.setSumPrizeCost(dataInfo.getSumPrizeCost());
				dataSet.setSumProfit(dataInfo.getSumPayCost().subtract(dataInfo.getSumPrizeCost()));
				list.add(dataSet);
			}

		}
		dataRes.setList(list);
		return dataRes;
	}

	@Async
	public void sendSocketSum(String classCode) {
		TimeUtil.setTimeoutSync(() -> {
			SumDashboardRes dataRes = getAllSumDasboard(classCode);
			this.simpMessagingTemplate.convertAndSend("/dashboard/sum", dataRes);
		}, 50);
	}

	@Async
	public void sendSocket(String classCode) {
		TimeUtil.setTimeoutSync(() -> {
			DashBroardMainRes dataRes = getDashboard(classCode);
			this.simpMessagingTemplate.convertAndSend("/dashboard", dataRes);
		}, 50);
	}

}
