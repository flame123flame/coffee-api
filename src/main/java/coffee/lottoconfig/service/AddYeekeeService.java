package coffee.lottoconfig.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coffee.lottoconfig.vo.req.AddLottoTimeReq;
import coffee.lottoconfig.vo.req.AddLottoTimeReq.TimeSellReq;
import coffee.lottoconfig.vo.req.PrizeYeekeeReq;
import coffee.lottoconfig.vo.req.SeqPrizeWinReq;
import coffee.lottoconfig.vo.req.YeekeeMaxMinReq;
import coffee.lottoconfig.vo.res.PrizeYeekeeRes;
import coffee.lottoconfig.vo.res.YeekeeMaxMinRes;
import coffee.lottoconfig.vo.res.addgroup.Minmax;
import coffee.model.GroupMaxMinMap;
import coffee.model.LottoClass;
import coffee.model.PrizeSetting;
import coffee.model.TimeSell;
import coffee.model.YeekeePrizeSeqMapping;
import coffee.repo.dao.GroupMaxMinMapDao;
import coffee.repo.dao.PrizeSettingDao;
import coffee.repo.jpa.GroupMaxMinMapRepo;
import coffee.repo.jpa.LottoClassRepository;
import coffee.repo.jpa.PrizeSettingRepo;
import coffee.repo.jpa.TimeSellRepo;
import coffee.repo.jpa.YeekeePrizeSeqMappingRepo;
import framework.constant.LottoConstant.LOTTO_KIND;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE.DELETE;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE.EDIT;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE.SAVE;
import framework.utils.ConvertDateUtils;
import framework.utils.GenerateRandomString;
import framework.utils.UserLoginUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AddYeekeeService {

	@Autowired
	private LottoClassRepository lottoClassRepo;

	@Autowired
	private TimeSellRepo timeSellRepo;

	@Autowired
	private PrizeSettingRepo prizeSettingRepo;

	@Autowired
	private GroupMaxMinMapRepo groupMaxMinMapRepo;

	@Autowired
	private YeekeePrizeSeqMappingRepo yeekeePrizeSeqMappingRepo;

	@Autowired
	private PrizeSettingDao prizeSettingDao;
	
	@Autowired
	private GroupMaxMinMapDao groupMaxMinMapDao;
	

	@Transactional
	public String addLottoYeekee(AddLottoTimeReq req) {
		LottoClass lottoClass = lottoClassRepo.findByLottoCategoryCodeAndLottoClassCode(req.getLottoCategoryCode(),
				req.getLottoClassCode());
		List<LottoClass> lottoPrefix = lottoClassRepo.findAll();
		if (lottoClass == null) {
			lottoClass = new LottoClass();

		} else {
			log.info(SAVE.DUPLICATE_DATA + " => " + req.getLottoClassCode());
			return "ClassCode: " + req.getLottoClassCode() + SAVE.DUPLICATE_DATA;
		}

		for (LottoClass dataFind : lottoPrefix) {
			if (dataFind.getPrefixTransNumber().equals(req.getPrefixCode())) {
				log.info(SAVE.DUPLICATE_DATA + " => " + req.getPrefixCode());
				return "PreFixCode: " + SAVE.DUPLICATE_DATA;
			}
		}

		lottoClass.setLottoClassCode(req.getLottoClassCode());
		lottoClass.setCreatedBy(UserLoginUtil.getUsername());
		lottoClass.setClassName(req.getLottoClassName());
		lottoClass.setRuleDes(req.getRuleDes());
		lottoClass.setTypeInstallment(req.getTypeInstallment());
		lottoClass.setLottoCategoryCode(req.getLottoCategoryCode());
		lottoClass.setAffiliateList(req.getAffiliateList());
		lottoClass.setGroupList(req.getGroupList());
		lottoClass.setTimeAfterBuy(req.getTimeAfterBuy());
		lottoClass.setTimeBeforeLotto(req.getTimeBeforeLotto());
		lottoClass.setCloseMessage("ยังไม่เปิดรับแทง");
		lottoClass.setPrefixTransNumber(req.getPrefixCode());
		lottoClass.setCountRefund(req.getCountRefund());
		lottoClass.setAutoUpdateWallet(req.getAutoUpdateWallet());
		lottoClass.setIgnoreWeekly(req.getIgnoreWeekly());
		lottoClass.setCountTime(req.getCountTime());
		lottoClass.setRoundTime(req.getRoundTime());
		lottoClass.setStopTime(req.getStopTime());
		lottoClass.setLottoClassImg(req.getLottoClassImg());
		lottoClass.setLottoClassColor(req.getLottoClassColor());
		lottoClass.setStartTime(req.getStartTime());
		lottoClass.setHasBet(req.getHasBet());
		lottoClass.setEarningsPercent(req.getEarningsPercent());
		lottoClassRepo.save(lottoClass);

		log.info("save time sell LottoClassCode:" + req.getLottoClassCode());
		TimeSell dataSet;
		Integer i = 1 ;
		for (TimeSellReq timeSellReq : req.getTimeSell()) {
			dataSet = null;
			dataSet = timeSellRepo.findByTimeSellCode(timeSellReq.getTimeSellCode());
			if (dataSet == null) {
				dataSet = new TimeSell();
				dataSet.setTimeSellCode(GenerateRandomString.generateUUID());
				dataSet.setLottoClassCode(lottoClass.getLottoClassCode());
				dataSet.setCreatedBy(UserLoginUtil.getUsername());
			} else {
				dataSet.setUpdatedAt(new Date());
				dataSet.setUpdatedBy(UserLoginUtil.getUsername());
			}
			dataSet.setTimeOpen(ConvertDateUtils.parseStringToDate(timeSellReq.getTimeOpen(),
					ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
			dataSet.setTimeClose(ConvertDateUtils.parseStringToDate(timeSellReq.getTimeClose(),
					ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
			dataSet.setRoundYeekee(i);
			timeSellRepo.save(dataSet);
			i++;
		}

		return SAVE.SUCCESS;
	}

	@Transactional
	public String editLottoYeekee(AddLottoTimeReq req) {
		LottoClass lottoClass = lottoClassRepo.findByLottoCategoryCodeAndLottoClassCode(req.getLottoCategoryCode(),
				req.getLottoClassCode());
		if (lottoClass == null) {
			lottoClass = new LottoClass();
		} else {
			lottoClass.setUpdatedBy(UserLoginUtil.getUsername());
			lottoClass.setUpdatedAt(new Date());

		}
		lottoClass.setLottoClassCode(req.getLottoClassCode());
		lottoClass.setCreatedBy(UserLoginUtil.getUsername());
		lottoClass.setClassName(req.getLottoClassName());
		lottoClass.setRuleDes(req.getRuleDes());
		lottoClass.setTypeInstallment(req.getTypeInstallment());
		lottoClass.setLottoCategoryCode(req.getLottoCategoryCode());
		lottoClass.setAffiliateList(req.getAffiliateList());
		lottoClass.setGroupList(req.getGroupList());
		lottoClass.setTimeAfterBuy(req.getTimeAfterBuy());
		lottoClass.setTimeBeforeLotto(req.getTimeBeforeLotto());
		lottoClass.setCloseMessage("ยังไม่เปิดรับแทง");
		lottoClass.setPrefixTransNumber(req.getPrefixCode());
		lottoClass.setCountRefund(req.getCountRefund());
		lottoClass.setAutoUpdateWallet(req.getAutoUpdateWallet());
		lottoClass.setIgnoreWeekly(req.getIgnoreWeekly());
		lottoClass.setCountTime(req.getCountTime());
		lottoClass.setRoundTime(req.getRoundTime());
		lottoClass.setStopTime(req.getStopTime());
		lottoClass.setLottoClassImg(req.getLottoClassImg());
		lottoClass.setLottoClassColor(req.getLottoClassColor());
		lottoClass.setStartTime(req.getStartTime());
		lottoClass.setHasBet(req.getHasBet());
		lottoClass.setEarningsPercent(req.getEarningsPercent());
		lottoClassRepo.save(lottoClass);

		log.info("Update YEEKEE LottoClassCode:" + req.getLottoClassCode());
		TimeSell dataSet;
		List<TimeSell> dataFind;
		dataFind = timeSellRepo.findByLottoClassCode(req.getLottoClassCode());
		if (dataFind != null) {
			timeSellRepo.deleteYeekeeTime(req.getLottoClassCode());
		}
		Integer i = 1;
		for (TimeSellReq timeSellReq : req.getTimeSell()) {

			dataSet = new TimeSell();
			dataSet.setTimeSellCode(GenerateRandomString.generateUUID());
			dataSet.setLottoClassCode(lottoClass.getLottoClassCode());
			dataSet.setCreatedBy(UserLoginUtil.getUsername());
			dataSet.setUpdatedAt(new Date());
			dataSet.setUpdatedBy(UserLoginUtil.getUsername());
			dataSet.setTimeOpen(ConvertDateUtils.parseStringToDate(timeSellReq.getTimeOpen(),
					ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
			dataSet.setTimeClose(ConvertDateUtils.parseStringToDate(timeSellReq.getTimeClose(),
					ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
			dataSet.setRoundYeekee(i);
			timeSellRepo.save(dataSet);
			i++;
		}
		return EDIT.SUCCESS;
	}

	public String addPrizeYeekee(List<PrizeYeekeeReq> req) {
		PrizeSetting dataSet = null;
		for (PrizeYeekeeReq prizeData : req) {
			String groupCode = GenerateRandomString.generateUUID();
			if (prizeData.getDigit3top() != null) {
				PrizeSetting prizeSetting = prizeSettingRepo.findByClassCodeAndMsdLottoKindCodeAndLottoGroupDtlCode(prizeData.getLottoClassCode(),LOTTO_KIND.DIGIT3_TOP,prizeData.getLottoGroupDtlCode());
				if (prizeSetting != null) {
					prizeSetting.setPrize(prizeData.getDigit3top());
					prizeSetting.setVipCode(prizeData.getVipCode());
					prizeSetting.setUpdatedAt(new Date());
					prizeSetting.setUpdatedBy(UserLoginUtil.getUsername());
					prizeSettingRepo.save(prizeSetting);
				} else {
					dataSet = new PrizeSetting();
					dataSet.setPrize(prizeData.getDigit3top());
					dataSet.setPrizeSettingCode(GenerateRandomString.generateUUID());
					dataSet.setLottoGroupDtlCode(groupCode);
					dataSet.setCreatedAt(new Date());
					dataSet.setCreatedBy(UserLoginUtil.getUsername());
					dataSet.setMsdLottoKindCode(LOTTO_KIND.DIGIT3_TOP);
					dataSet.setSeqOrder(0);
					dataSet.setVipCode(prizeData.getVipCode());
					dataSet.setClassCode(prizeData.getLottoClassCode());
					prizeSettingRepo.save(dataSet);
				}
			}
			if (prizeData.getDigit3swap() != null) {
				PrizeSetting prizeSetting = prizeSettingRepo.findByClassCodeAndMsdLottoKindCodeAndLottoGroupDtlCode(prizeData.getLottoClassCode(),LOTTO_KIND.DIGIT3_SWAPPED,prizeData.getLottoGroupDtlCode());
				if (prizeSetting != null) {
					prizeSetting.setPrize(prizeData.getDigit3swap());
					prizeSetting.setVipCode(prizeData.getVipCode());
					prizeSetting.setUpdatedAt(new Date());
					prizeSetting.setUpdatedBy(UserLoginUtil.getUsername());
					prizeSettingRepo.save(prizeSetting);
				} else {
					dataSet = new PrizeSetting();
					dataSet.setPrize(prizeData.getDigit3swap());
					dataSet.setPrizeSettingCode(GenerateRandomString.generateUUID());
					dataSet.setCreatedAt(new Date());
					dataSet.setLottoGroupDtlCode(groupCode);
					dataSet.setCreatedBy(UserLoginUtil.getUsername());
					dataSet.setMsdLottoKindCode(LOTTO_KIND.DIGIT3_SWAPPED);
					dataSet.setSeqOrder(1);
					dataSet.setVipCode(prizeData.getVipCode());
					dataSet.setClassCode(prizeData.getLottoClassCode());
					prizeSettingRepo.save(dataSet);
				}
			}
			if (prizeData.getDigit2top() != null) {

				PrizeSetting prizeSetting = prizeSettingRepo.findByClassCodeAndMsdLottoKindCodeAndLottoGroupDtlCode(prizeData.getLottoClassCode(),LOTTO_KIND.DIGIT2_TOP,prizeData.getLottoGroupDtlCode());
				if (prizeSetting != null) {
					prizeSetting.setPrize(prizeData.getDigit2top());
					prizeSetting.setVipCode(prizeData.getVipCode());
					prizeSetting.setUpdatedAt(new Date());
					prizeSetting.setUpdatedBy(UserLoginUtil.getUsername());
					prizeSettingRepo.save(prizeSetting);
				} else {
					dataSet = new PrizeSetting();
					dataSet.setPrize(prizeData.getDigit2top());
					dataSet.setPrizeSettingCode(GenerateRandomString.generateUUID());
					dataSet.setCreatedAt(new Date());
					dataSet.setLottoGroupDtlCode(groupCode);
					dataSet.setCreatedBy(UserLoginUtil.getUsername());
					dataSet.setMsdLottoKindCode(LOTTO_KIND.DIGIT2_TOP);
					dataSet.setSeqOrder(2);
					dataSet.setVipCode(prizeData.getVipCode());
					dataSet.setClassCode(prizeData.getLottoClassCode());
					prizeSettingRepo.save(dataSet);
				}
			}
			if (prizeData.getDigit2bot() != null) {
				PrizeSetting prizeSetting = prizeSettingRepo.findByClassCodeAndMsdLottoKindCodeAndLottoGroupDtlCode(prizeData.getLottoClassCode(),LOTTO_KIND.DIGIT2_BOT,prizeData.getLottoGroupDtlCode());
				if (prizeSetting != null) {
					prizeSetting.setPrize(prizeData.getDigit2bot());
					prizeSetting.setVipCode(prizeData.getVipCode());
					prizeSetting.setUpdatedAt(new Date());
					prizeSetting.setUpdatedBy(UserLoginUtil.getUsername());
					prizeSettingRepo.save(prizeSetting);
				} else {
					dataSet = new PrizeSetting();
					dataSet.setPrize(prizeData.getDigit2bot());
					dataSet.setPrizeSettingCode(GenerateRandomString.generateUUID());
					dataSet.setCreatedAt(new Date());
					dataSet.setLottoGroupDtlCode(groupCode);
					dataSet.setCreatedBy(UserLoginUtil.getUsername());
					dataSet.setMsdLottoKindCode(LOTTO_KIND.DIGIT2_BOT);
					dataSet.setSeqOrder(3);
					dataSet.setVipCode(prizeData.getVipCode());
					dataSet.setClassCode(prizeData.getLottoClassCode());
					prizeSettingRepo.save(dataSet);
				}
			}
			if (prizeData.getDigit1top() != null) {
				PrizeSetting prizeSetting = prizeSettingRepo.findByClassCodeAndMsdLottoKindCodeAndLottoGroupDtlCode(prizeData.getLottoClassCode(),LOTTO_KIND.DIGIT1_TOP,prizeData.getLottoGroupDtlCode());
				if (prizeSetting != null) {
					prizeSetting.setPrize(prizeData.getDigit1top());
					prizeSetting.setVipCode(prizeData.getVipCode());
					prizeSetting.setUpdatedAt(new Date());
					prizeSetting.setUpdatedBy(UserLoginUtil.getUsername());
					prizeSettingRepo.save(prizeSetting);
				} else {
					dataSet = new PrizeSetting();
					dataSet.setPrize(prizeData.getDigit1top());
					dataSet.setPrizeSettingCode(GenerateRandomString.generateUUID());
					dataSet.setCreatedAt(new Date());
					dataSet.setLottoGroupDtlCode(groupCode);
					dataSet.setCreatedBy(UserLoginUtil.getUsername());
					dataSet.setMsdLottoKindCode(LOTTO_KIND.DIGIT1_TOP);
					dataSet.setSeqOrder(4);
					dataSet.setVipCode(prizeData.getVipCode());
					dataSet.setClassCode(prizeData.getLottoClassCode());
					prizeSettingRepo.save(dataSet);
				}
			}
			if (prizeData.getDigit1bot() != null) {
				PrizeSetting prizeSetting = prizeSettingRepo.findByClassCodeAndMsdLottoKindCodeAndLottoGroupDtlCode(prizeData.getLottoClassCode(),LOTTO_KIND.DIGIT1_BOT,prizeData.getLottoGroupDtlCode());
				if (prizeSetting != null) {
					prizeSetting.setPrize(prizeData.getDigit1bot());
					prizeSetting.setVipCode(prizeData.getVipCode());
					prizeSetting.setUpdatedAt(new Date());
					prizeSetting.setUpdatedBy(UserLoginUtil.getUsername());
					prizeSettingRepo.save(prizeSetting);
				} else {
					dataSet = new PrizeSetting();
					dataSet.setPrize(prizeData.getDigit1bot());
					dataSet.setPrizeSettingCode(GenerateRandomString.generateUUID());
					dataSet.setCreatedAt(new Date());
					dataSet.setLottoGroupDtlCode(groupCode);
					dataSet.setCreatedBy(UserLoginUtil.getUsername());
					dataSet.setMsdLottoKindCode(LOTTO_KIND.DIGIT1_BOT);
					dataSet.setSeqOrder(5);
					dataSet.setVipCode(prizeData.getVipCode());
					dataSet.setClassCode(prizeData.getLottoClassCode());
					prizeSettingRepo.save(dataSet);
				}
			}

		}

		return SAVE.SUCCESS;
	}

	public List<PrizeYeekeeRes> getPrizeByClassCode(String classCode) {
		List<PrizeYeekeeRes.VipCodeListRes> vipCodeList = prizeSettingDao.findVipCodeGroup(classCode);
		
		List<PrizeYeekeeRes> dataRes = new ArrayList<PrizeYeekeeRes>();

		for (PrizeYeekeeRes.VipCodeListRes vipCode : vipCodeList) {
			List<PrizeSetting> prizeSetting = prizeSettingRepo.findByClassCodeAndVipCode(classCode, vipCode.getVipCode());
			PrizeYeekeeRes dataSet = new PrizeYeekeeRes();
			List<Long> dataIdList = new ArrayList<Long>();
			for (PrizeSetting dataInfo : prizeSetting) {
				
				if (dataInfo.getMsdLottoKindCode().equals(LOTTO_KIND.DIGIT3_TOP)) {
					dataIdList.add(dataInfo.getPrizeSettingId());
					dataSet.setPrizeSettingCode(dataInfo.getPrizeSettingCode());
					dataSet.setVipCode(dataInfo.getVipCode());
					dataSet.setDigit3top(dataInfo.getPrize());
					dataSet.setLottoClassCode(dataInfo.getClassCode());

				}
				if (dataInfo.getMsdLottoKindCode().equals(LOTTO_KIND.DIGIT3_SWAPPED)) {
					dataIdList.add(dataInfo.getPrizeSettingId());
					dataSet.setPrizeSettingCode(dataInfo.getPrizeSettingCode());
					dataSet.setVipCode(dataInfo.getVipCode());
					dataSet.setDigit3swap(dataInfo.getPrize());
					dataSet.setLottoClassCode(dataInfo.getClassCode());

				}
				if (dataInfo.getMsdLottoKindCode().equals(LOTTO_KIND.DIGIT2_TOP)) {
					dataIdList.add(dataInfo.getPrizeSettingId());
					dataSet.setPrizeSettingCode(dataInfo.getPrizeSettingCode());
					dataSet.setVipCode(dataInfo.getVipCode());
					dataSet.setDigit2top(dataInfo.getPrize());
					dataSet.setLottoClassCode(dataInfo.getClassCode());

				}
				if (dataInfo.getMsdLottoKindCode().equals(LOTTO_KIND.DIGIT2_BOT)) {
					dataIdList.add(dataInfo.getPrizeSettingId());
					dataSet.setPrizeSettingCode(dataInfo.getPrizeSettingCode());
					dataSet.setVipCode(dataInfo.getVipCode());
					dataSet.setDigit2bot(dataInfo.getPrize());
					dataSet.setLottoClassCode(dataInfo.getClassCode());

				}
				if (dataInfo.getMsdLottoKindCode().equals(LOTTO_KIND.DIGIT1_TOP)) {
					dataIdList.add(dataInfo.getPrizeSettingId());
					dataSet.setPrizeSettingCode(dataInfo.getPrizeSettingCode());
					dataSet.setVipCode(dataInfo.getVipCode());
					dataSet.setDigit1top(dataInfo.getPrize());
					dataSet.setLottoClassCode(dataInfo.getClassCode());

				}
				if (dataInfo.getMsdLottoKindCode().equals(LOTTO_KIND.DIGIT1_BOT)) {
					dataIdList.add(dataInfo.getPrizeSettingId());
					dataSet.setPrizeSettingCode(dataInfo.getPrizeSettingCode());
					dataSet.setVipCode(dataInfo.getVipCode());
					dataSet.setDigit1bot(dataInfo.getPrize());
					dataSet.setLottoClassCode(dataInfo.getClassCode());

				}
				dataSet.setLottoGroupDtlCode(dataInfo.getLottoGroupDtlCode());
				dataSet.setPrizeSettingId(dataIdList);
			}
			dataRes.add(dataSet);
		}
		return dataRes;
	}

	public LottoClass getYeekeeDetail(Long id, String classCode) {
		LottoClass DataRes = lottoClassRepo.findByLottoClassIdAndLottoClassCode(id, classCode);
		return DataRes;
	}

	@Transactional
	public String addYeekeeMaxMin(YeekeeMaxMinReq req) {
		GroupMaxMinMap dataSet = null;
		for (Minmax dataDigit3top : req.getDigit3top()) {
			dataSet = groupMaxMinMapRepo.findByGroupMaxMinMapCode(dataDigit3top.getGroupMaxMinMapCode());
			if (dataSet == null) {
				dataSet = new GroupMaxMinMap();
				dataSet.setGroupMaxMinMapCode(GenerateRandomString.generateUUID());
				dataSet.setLottoClassCode(dataDigit3top.getLottoClassCode());
				dataSet.setCreatedBy(UserLoginUtil.getUsername());
			} else {
				dataSet.setUpdatedAt(new Date());
				dataSet.setUpdatedBy(UserLoginUtil.getUsername());
			}
			dataSet.setMaximumPerTrans(dataDigit3top.getMaximumTrans());
			dataSet.setMaximumPerUser(dataDigit3top.getMaximumUsername());
			dataSet.setMinimumPerTrans(dataDigit3top.getMinimumTrans());
			dataSet.setMsdLottoKindCode(dataDigit3top.getMsdLottoKindCode());
			dataSet.setCreatedAt(new Date());
			dataSet.setVipCode(dataDigit3top.getVipCode());
			groupMaxMinMapRepo.save(dataSet);
		}
		for (Minmax dataDigit3Swap : req.getDigit3swap()) {
			dataSet = groupMaxMinMapRepo.findByGroupMaxMinMapCode(dataDigit3Swap.getGroupMaxMinMapCode());
			if (dataSet == null) {
				dataSet = new GroupMaxMinMap();
				dataSet.setGroupMaxMinMapCode(GenerateRandomString.generateUUID());
				dataSet.setLottoClassCode(dataDigit3Swap.getLottoClassCode());
				dataSet.setCreatedBy(UserLoginUtil.getUsername());
			} else {
				dataSet.setUpdatedAt(new Date());
				dataSet.setUpdatedBy(UserLoginUtil.getUsername());
			}
			dataSet.setMaximumPerTrans(dataDigit3Swap.getMaximumTrans());
			dataSet.setMaximumPerUser(dataDigit3Swap.getMaximumUsername());
			dataSet.setMinimumPerTrans(dataDigit3Swap.getMinimumTrans());
			dataSet.setMsdLottoKindCode(dataDigit3Swap.getMsdLottoKindCode());
			dataSet.setCreatedAt(new Date());
			dataSet.setVipCode(dataDigit3Swap.getVipCode());
			groupMaxMinMapRepo.save(dataSet);
		}
		for (Minmax dataDigit2top : req.getDigit2top()) {
			dataSet = groupMaxMinMapRepo.findByGroupMaxMinMapCode(dataDigit2top.getGroupMaxMinMapCode());
			if (dataSet == null) {
				dataSet = new GroupMaxMinMap();
				dataSet.setGroupMaxMinMapCode(GenerateRandomString.generateUUID());
				dataSet.setLottoClassCode(dataDigit2top.getLottoClassCode());
				dataSet.setCreatedBy(UserLoginUtil.getUsername());
			} else {
				dataSet.setUpdatedAt(new Date());
				dataSet.setUpdatedBy(UserLoginUtil.getUsername());
			}
			dataSet.setMaximumPerTrans(dataDigit2top.getMaximumTrans());
			dataSet.setMaximumPerUser(dataDigit2top.getMaximumUsername());
			dataSet.setMinimumPerTrans(dataDigit2top.getMinimumTrans());
			dataSet.setMsdLottoKindCode(dataDigit2top.getMsdLottoKindCode());
			dataSet.setCreatedAt(new Date());
			dataSet.setVipCode(dataDigit2top.getVipCode());
			groupMaxMinMapRepo.save(dataSet);
		}
		for (Minmax dataDigit2bot : req.getDigit2bot()) {
			dataSet = groupMaxMinMapRepo.findByGroupMaxMinMapCode(dataDigit2bot.getGroupMaxMinMapCode());
			if (dataSet == null) {
				dataSet = new GroupMaxMinMap();
				dataSet.setGroupMaxMinMapCode(GenerateRandomString.generateUUID());
				dataSet.setLottoClassCode(dataDigit2bot.getLottoClassCode());
				dataSet.setCreatedBy(UserLoginUtil.getUsername());
			} else {
				dataSet.setUpdatedAt(new Date());
				dataSet.setUpdatedBy(UserLoginUtil.getUsername());
			}
			dataSet.setMaximumPerTrans(dataDigit2bot.getMaximumTrans());
			dataSet.setMaximumPerUser(dataDigit2bot.getMaximumUsername());
			dataSet.setMinimumPerTrans(dataDigit2bot.getMinimumTrans());
			dataSet.setMsdLottoKindCode(dataDigit2bot.getMsdLottoKindCode());
			dataSet.setCreatedAt(new Date());
			dataSet.setVipCode(dataDigit2bot.getVipCode());
			groupMaxMinMapRepo.save(dataSet);
		}
		for (Minmax dataDigit1top : req.getDigit1top()) {
			dataSet = groupMaxMinMapRepo.findByGroupMaxMinMapCode(dataDigit1top.getGroupMaxMinMapCode());
			if (dataSet == null) {
				dataSet = new GroupMaxMinMap();
				dataSet.setGroupMaxMinMapCode(GenerateRandomString.generateUUID());
				dataSet.setLottoClassCode(dataDigit1top.getLottoClassCode());
				dataSet.setCreatedBy(UserLoginUtil.getUsername());
			} else {
				dataSet.setUpdatedAt(new Date());
				dataSet.setUpdatedBy(UserLoginUtil.getUsername());
			}
			dataSet.setMaximumPerTrans(dataDigit1top.getMaximumTrans());
			dataSet.setMaximumPerUser(dataDigit1top.getMaximumUsername());
			dataSet.setMinimumPerTrans(dataDigit1top.getMinimumTrans());
			dataSet.setMsdLottoKindCode(dataDigit1top.getMsdLottoKindCode());
			dataSet.setCreatedAt(new Date());
			dataSet.setVipCode(dataDigit1top.getVipCode());
			groupMaxMinMapRepo.save(dataSet);
		}
		for (Minmax dataDigit1bot : req.getDigit1bot()) {
			dataSet = groupMaxMinMapRepo.findByGroupMaxMinMapCode(dataDigit1bot.getGroupMaxMinMapCode());
			if (dataSet == null) {
				dataSet = new GroupMaxMinMap();
				dataSet.setGroupMaxMinMapCode(GenerateRandomString.generateUUID());
				dataSet.setLottoClassCode(dataDigit1bot.getLottoClassCode());
				dataSet.setCreatedBy(UserLoginUtil.getUsername());
			} else {
				dataSet.setUpdatedAt(new Date());
				dataSet.setUpdatedBy(UserLoginUtil.getUsername());
			}
			dataSet.setMaximumPerTrans(dataDigit1bot.getMaximumTrans());
			dataSet.setMaximumPerUser(dataDigit1bot.getMaximumUsername());
			dataSet.setMinimumPerTrans(dataDigit1bot.getMinimumTrans());
			dataSet.setMsdLottoKindCode(dataDigit1bot.getMsdLottoKindCode());
			dataSet.setCreatedAt(new Date());
			dataSet.setVipCode(dataDigit1bot.getVipCode());
			groupMaxMinMapRepo.save(dataSet);
		}

		return SAVE.SUCCESS;
	}
	
	public YeekeeMaxMinRes getYeekeeMaxMin(String classCode)
	{  
		YeekeeMaxMinRes dataRes = new YeekeeMaxMinRes();
		dataRes.setDigit3top(groupMaxMinMapDao.getMaxMinByCode(classCode,LOTTO_KIND.DIGIT3_TOP));
		dataRes.setDigit3swap(groupMaxMinMapDao.getMaxMinByCode(classCode,LOTTO_KIND.DIGIT3_SWAPPED));
		dataRes.setDigit2top(groupMaxMinMapDao.getMaxMinByCode(classCode,LOTTO_KIND.DIGIT2_TOP));
		dataRes.setDigit2bot(groupMaxMinMapDao.getMaxMinByCode(classCode,LOTTO_KIND.DIGIT2_BOT));
		dataRes.setDigit1top(groupMaxMinMapDao.getMaxMinByCode(classCode,LOTTO_KIND.DIGIT1_TOP));
		dataRes.setDigit1bot(groupMaxMinMapDao.getMaxMinByCode(classCode,LOTTO_KIND.DIGIT1_BOT));
		return dataRes;
	}
	
	@Transactional
	public String deleteMaxMinById(Long groupMaxMinId)
	{
		groupMaxMinMapRepo.deleteByGroupMaxMinMapId(groupMaxMinId);
		return DELETE.SUCCESS;
	}

	@Transactional
	public String addSeqPrize(List<SeqPrizeWinReq> req) {
		YeekeePrizeSeqMapping dataSet = null;
		for (SeqPrizeWinReq dataReq : req) {
			dataSet = yeekeePrizeSeqMappingRepo.findByYeekeePrizeSeqMappingCode(dataReq.getYeekeePrizeSeqMappingCode());
			if(dataSet!=null)
			{
				dataSet.setUpdatedAt(new Date());
				dataSet.setUpdatedBy(UserLoginUtil.getUsername());
			}
			else
			{
				dataSet = new YeekeePrizeSeqMapping();
				dataSet.setYeekeePrizeSeqMappingCode(GenerateRandomString.generateUUID());
				dataSet.setCreatedAt(new Date());
				dataSet.setCreatedBy(UserLoginUtil.getUsername());
			}
			
			dataSet.setSeqOrder(dataReq.getSeqNumber());
			dataSet.setClassCode(dataReq.getLottoClassCode());
			dataSet.setPrize(dataReq.getPrizeWin());
			yeekeePrizeSeqMappingRepo.save(dataSet);

		}
		return SAVE.SUCCESS;
	}
	
	@Transactional
	public String deleteBySeqPrize(String code)
	{
		yeekeePrizeSeqMappingRepo.deleteByYeekeePrizeSeqMappingCode(code);
		return DELETE.SUCCESS;
	}
	
	@Transactional
	public List<YeekeePrizeSeqMapping> getSeqPrize(String classCode)
	{
		List<YeekeePrizeSeqMapping> dataRes = yeekeePrizeSeqMappingRepo.findByClassCode(classCode);
		return dataRes;
	}
	
	@Transactional
	public String deleteYeekeePrize(List<Long> prizeSettingId) {
		for(Long id:prizeSettingId)
		{
			prizeSettingRepo.deleteYeekeePrizeById(id);
		}
		return DELETE.SUCCESS;
	}
}
