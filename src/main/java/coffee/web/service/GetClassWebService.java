package coffee.web.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coffee.buy.service.BuyLottoService;
import coffee.buy.vo.res.Installment;
import coffee.model.LottoClass;
import coffee.model.RuleConfig;
import coffee.model.RuleConfigImage;
import coffee.model.TimeSell;
import coffee.repo.dao.PrizeSettingDao;
import coffee.repo.jpa.LottoClassRepository;
import coffee.repo.jpa.RuleConfigImageRepo;
import coffee.repo.jpa.RuleConfigRepo;
import coffee.repo.jpa.TimeSellRepo;
import coffee.web.vo.res.GetClassWebRes;
import coffee.web.vo.res.GetClassWebRes.GetClassWebPrice;
import coffee.web.vo.res.GetClassWebRes.GetClassWebRuleDes;
import framework.constant.LottoConstant;
import framework.utils.ConvertDateUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GetClassWebService {

	@Autowired
	private LottoClassRepository lottoClassRepo;

	@Autowired
	private PrizeSettingDao prizeSettingDao;

	@Autowired
	private TimeSellRepo timeSellRepo;

	@Autowired
	private WebTimeSellService webTimeSellService;

	@Autowired
	private BuyLottoService buyLottoService;
	
	@Autowired
	private RuleConfigRepo ruleConfigRepo;
	
	@Autowired
	private RuleConfigImageRepo ruleConfigImageRepo;

	public GetClassWebRes getClassWeb(String lottoClassCode, String vipCode) {

		LottoClass dataFind = lottoClassRepo.findByLottoClassCode(lottoClassCode);
		if (dataFind == null) {
			return null;
		}
		GetClassWebRes dataRes = new GetClassWebRes();
		dataRes.setClassCode(dataFind.getLottoClassCode());
		dataRes.setClassName(dataFind.getClassName());
		dataRes.setRuleDes(dataFind.getRuleDes());
		dataRes.setTypeInstallment(dataFind.getTypeInstallment());

		List<TimeSell> timeFind = timeSellRepo.findByLottoClassCode(lottoClassCode);
		Installment installment = buyLottoService.checkInstallment(lottoClassCode, timeFind, dataFind);
		Date timeIns;
		if (LottoConstant.TYPE_INSTALLMENT.HR24.equals(dataFind.getTypeInstallment())) {
			timeIns = installment.getTimeOpen();
		} else {
			timeIns = installment.getTimeClose();
		}

		String installmentStr = webTimeSellService.getInstallmentStr(dataFind.getTypeInstallment(), timeIns,
				installment.getLengthTimeSell());

		installment.setTimeOpenStr(ConvertDateUtils.formatDateToString(installment.getTimeOpen(),
				ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
		installment.setTimeCloseStr(ConvertDateUtils.formatDateToString(installment.getTimeClose(),
				ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
		dataRes.setInstallment(installment);
		dataRes.setInstallmentStr(installmentStr);

		/**
		 * set Time
		 */
		List<Installment> listTimeSet = webTimeSellService.getListInstallmentByType(dataFind.getTypeInstallment(),
				timeFind, dataFind.getIgnoreWeekly());
		dataRes.setTimeSell(listTimeSet);

		/**
		 * set Prize
		 */
		List<GetClassWebPrice> prizeSettingData;
		if (LottoConstant.TYPE_INSTALLMENT.HR24.equals(dataFind.getTypeInstallment())) {
			prizeSettingData = prizeSettingDao.getPrizeListByClassCode24Hr(lottoClassCode, vipCode);
		} else {
			prizeSettingData = prizeSettingDao.getPrizeListByClassCode(lottoClassCode, vipCode);
		}
		System.out.println(prizeSettingData);
		if (prizeSettingData.isEmpty()) {
			if (LottoConstant.TYPE_INSTALLMENT.HR24.equals(dataFind.getTypeInstallment())) {
				prizeSettingData = prizeSettingDao.getPrizeListByClassCode24Hr(lottoClassCode, "DEFAULT");
			} else {
				prizeSettingData = prizeSettingDao.getPrizeListByClassCode(lottoClassCode, "DEFAULT");
			}
			System.out.println(prizeSettingData);
		}
		dataRes.setPrizeSetting(prizeSettingData);
		
		/**
		 * set RULE DESCRIPTION
		 */
		List<RuleConfig> ruleDesList = ruleConfigRepo.findByClassCode(lottoClassCode);
		List<GetClassWebRuleDes> ruleData = new ArrayList<GetClassWebRes.GetClassWebRuleDes>();
		for(RuleConfig ruleInfo : ruleDesList)
		{
			GetClassWebRuleDes ruleSet = new GetClassWebRuleDes();
			ruleSet.setRuleDes(ruleInfo.getRuleDes());
			ruleSet.setImageRule(ruleInfo.getImagePath());
			ruleData.add(ruleSet);
		}
		
		List<RuleConfigImage> ruleImageList = ruleConfigImageRepo.findByClassCode(lottoClassCode);
		List<GetClassWebRuleDes> imageData = new ArrayList<GetClassWebRes.GetClassWebRuleDes>();
		for(RuleConfigImage ruleImageInfo : ruleImageList)
		{
			GetClassWebRuleDes imageSet = new GetClassWebRuleDes();
			imageSet.setImageRule(ruleImageInfo.getImagePath());
			imageData.add(imageSet);
		}
		dataRes.setRuleDesList(ruleData);
		dataRes.setImageList(imageData);
		
		return dataRes;
	}

}
