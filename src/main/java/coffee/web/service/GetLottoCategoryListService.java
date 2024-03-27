package coffee.web.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coffee.buy.service.BuyLottoService;
import coffee.buy.vo.res.Installment;
import coffee.model.GroupListMapping;
import coffee.model.LottoClass;
import coffee.model.TimeSell;
import coffee.repo.jpa.GroupListMappingRepo;
import coffee.repo.jpa.LottoClassRepository;
import coffee.repo.jpa.TimeSellRepo;
import coffee.web.vo.res.LottoCategoryListGroupRes;
import coffee.web.vo.res.LottoCategoryListRes;
import coffee.web.vo.res.LottoCategoryListRes.LottoCategoryListDetail;
import framework.constant.LottoConstant;

@Service
public class GetLottoCategoryListService {

	@Autowired
	private LottoClassRepository lottoClassRepo;

	@Autowired
	private TimeSellRepo timeSellRepo;

	@Autowired
	private WebTimeSellService webTimeSellService;

	@Autowired
	private BuyLottoService buyLottoService;

	@Autowired
	private GroupListMappingRepo groupListMappingRepo;

	public LottoCategoryListRes getLottoCategoryList() {
		LottoCategoryListRes dataRes = new LottoCategoryListRes();
		List<LottoClass> listGovernment = lottoClassRepo.findByLottoCategoryCode(LottoConstant.CATEGORY.GOVERNMENT);
		List<LottoClass> listStocks = lottoClassRepo.findByLottoCategoryCode(LottoConstant.CATEGORY.STOCKS);
		List<LottoClass> listYeeKee = lottoClassRepo.findByLottoCategoryCode(LottoConstant.CATEGORY.YEEKEE);
		dataRes.setGovernment(tranferClassToRes(listGovernment));
		dataRes.setStocks(tranferClassToRes(listStocks));
		dataRes.setYeekee(tranferClassToRes(listYeeKee));
		return dataRes;
	}

	private List<LottoCategoryListDetail> tranferClassToRes(List<LottoClass> req) {
		List<LottoCategoryListDetail> dataRes = new ArrayList<LottoCategoryListRes.LottoCategoryListDetail>();
		req.forEach((item) -> {
			LottoCategoryListDetail dataSet = new LottoCategoryListDetail();
			dataSet.setLottoCode(item.getLottoClassCode());
			dataSet.setCategoryCode(item.getLottoCategoryCode());
			dataSet.setLottoName(item.getClassName());
			dataSet.setLottoImg(item.getLottoClassImg());
			dataSet.setLottoColor(item.getLottoClassColor());
			dataSet.setStatus(item.getViewStatus());

			List<TimeSell> listTimeSell = timeSellRepo.findByLottoClassCode(item.getLottoClassCode());

			Installment installment = buyLottoService.checkInstallment(item.getLottoClassCode(), listTimeSell, null);
			dataSet.setInstallment(installment);
			dataSet.setTypeInstallment(item.getTypeInstallment());
			dataSet.setHideDesc(item.getCloseMessage());

			dataSet.setTimeSell(webTimeSellService.getListInstallmentByType(item.getTypeInstallment(), listTimeSell,
					item.getIgnoreWeekly()));
			dataRes.add(dataSet);
		});
		return dataRes;
	}

	public LottoCategoryListGroupRes getLottoCategoryListGroup() {
		LottoCategoryListGroupRes dataRes = new LottoCategoryListGroupRes();
		List<LottoClass> classCodeList = lottoClassRepo.findAll();

		List<GroupListMapping> lottoHit = groupListMappingRepo
				.findByGroupListMappingCode(LottoConstant.GROUP_LIST_MAPPING.LOTTO_HIT);
		List<GroupListMapping> lottoGroupStocks = groupListMappingRepo
				.findByGroupListMappingCode(LottoConstant.GROUP_LIST_MAPPING.LOTTO_GROUP_STOCKS);
		List<GroupListMapping> lottoSet = groupListMappingRepo
				.findByGroupListMappingCode(LottoConstant.GROUP_LIST_MAPPING.LOTTO_SET);

		List<LottoClass> lottoHitClass = new ArrayList<>();
		List<LottoClass> lottoGroupStocksClass = new ArrayList<>();
		List<LottoClass> lottoSetClass = new ArrayList<>();

		for (GroupListMapping lottoHitObj : lottoHit) {
			for (LottoClass lottoClass : classCodeList) {
				if (lottoClass.getLottoClassCode().equals(lottoHitObj.getClassCode())) {
					lottoHitClass.add(lottoClass);
				}
			}
		}
		for (GroupListMapping lottoGroupStocksObj : lottoGroupStocks) {
			for (LottoClass lottoClass : classCodeList) {
				if (lottoClass.getLottoClassCode().equals(lottoGroupStocksObj.getClassCode())) {
					lottoGroupStocksClass.add(lottoClass);
				}
			}
		}
		for (GroupListMapping lottoSetObj : lottoSet) {
			for (LottoClass lottoClass : classCodeList) {
				if (lottoClass.getLottoClassCode().equals(lottoSetObj.getClassCode())) {
					lottoSetClass.add(lottoClass);
				}
			}
		}
		dataRes.setLottoHit(tranferClassToRes(lottoHitClass));
		dataRes.setLottoGroupStocks(tranferClassToRes(lottoGroupStocksClass));
		dataRes.setLottoSet(tranferClassToRes(lottoSetClass));
		return dataRes;
	}
}
