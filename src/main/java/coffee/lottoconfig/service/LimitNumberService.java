package coffee.lottoconfig.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import coffee.lottoconfig.vo.req.LimitNumberReq;
import coffee.lottoconfig.vo.res.GetAllLimitRes;
import coffee.lottoconfig.vo.res.GroupDtlRes;
import coffee.lottoconfig.vo.res.LimitDtlRes;
import coffee.lottoconfig.vo.res.LimitNumberAllRes;
import coffee.lottoconfig.vo.res.LimitNumberRes;
import coffee.model.LimitNumber;
import coffee.model.LottoGroup;
import coffee.model.MsdLottoKind;
import coffee.model.PrizeSetting;
import coffee.model.SumBetGroup;
import coffee.model.SumPrize;
import coffee.repo.dao.LimitNumberDao;
import coffee.repo.dao.LottoGroupDao;
import coffee.repo.dao.MsdLottoKindDao;
import coffee.repo.jpa.LimitNumberRepo;
import coffee.repo.jpa.LottoGroupDtlRepo;
import coffee.repo.jpa.PrizeSettingRepo;
import coffee.repo.jpa.SumBetGroupRepo;
import coffee.repo.jpa.SumPrizeRepo;
import coffee.transaction.service.TransactionGroupService;
import framework.constant.LottoConstant.LOTTO_KIND;
import framework.utils.GenerateRandomString;
import framework.utils.TimeUtil;
import framework.utils.UserLoginUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LimitNumberService {
	@Autowired
	LimitNumberRepo limitNumberRepo;

	@Autowired
	private MsdLottoKindDao msdLottoKindDao;

	@Autowired
	private PrizeSettingRepo prizeSettingRepo;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private LottoGroupDao lottoGroupDao;

	@Autowired
	private LottoGroupDtlRepo lottoGroupDtlRepo;

	@Autowired
	private SumBetGroupRepo sumBetGroupRepo;

	@Autowired
	private SumPrizeRepo sumPrizeRepo;

	@Autowired
	private LimitNumberDao limitNumberDao;

	@Autowired
	private TransactionGroupService transactionGroupService;

	@Transactional
	public LimitNumberRes insertOne(LimitNumberReq req) {
		LimitNumber itemAuto = limitNumberRepo.findByMsdLottoKindCodeAndLottoClassCodeAndLottoNumberAndIsManual(
				req.getMsdLottoKindCode(), req.getLottoClassCode(), req.getLottoNumber(), false);
		if (itemAuto != null) {
			limitNumberRepo.delete(itemAuto);
		}
		LimitNumber item = limitNumberRepo.findByMsdLottoKindCodeAndLottoClassCodeAndLottoNumberAndIsManual(
				req.getMsdLottoKindCode(), req.getLottoClassCode(), req.getLottoNumber(), true);
		if (item != null) {
			item.setUpdatedBy(UserLoginUtil.getUsername());
			item.setUpdatedAt(new Date());
		} else {
			item = new LimitNumber();
			item.setCreatedBy(UserLoginUtil.getUsername());
		}

		// item.setLottoPrice(req.getLottoPrice());

		item.setLottoGroupDtlCode(req.getLottoPrice());
		item.setLottoGroupDtlCodeManual(req.getLottoPrice());
		item.setLottoClassCode(req.getLottoClassCode());
		item.setLottoNumber(req.getLottoNumber());
		item.setMsdLottoKindCode(req.getMsdLottoKindCode());
		item.setIsManual(true);
		item.setEnable(true);

		List<LimitNumber> listLim = new ArrayList<LimitNumber>();
		if (LOTTO_KIND.DIGIT3_SWAPPED.equals(req.getMsdLottoKindCode())) {
			listLim = createSwapped(item);
			for (LimitNumber limitNumber : listLim) {
				limitNumberRepo.deleteByMsdLottoKindCodeAndLottoClassCodeAndLottoNumberAndIsManual(
						req.getMsdLottoKindCode(), req.getLottoClassCode(), limitNumber.getLottoNumber(), true);
				/**
				 * update rate to setLottoGroupDtlCode
				 */
				limitNumberRepo.save(limitNumber);
			}
		} else {
			limitNumberRepo.save(item);
		}

		LimitNumberRes returnData = new LimitNumberRes();
		returnData.setEntityToRes(item);
		sendSocket(req.getLottoClassCode());
		updateLimitInsertUpdate(item, listLim);
		return returnData;
	}

	@Async
	private void updateLimitInsertUpdate(LimitNumber item, List<LimitNumber> req) {
		TimeUtil.setTimeoutSync(() -> {
			LottoGroup lottoGroup = lottoGroupDao.getGroupByLottoKind(item.getMsdLottoKindCode(),
					item.getLottoClassCode());
			SumBetGroup sumBetGroup = sumBetGroupRepo
					.findByLottoGroupCodeAndLottoClassCode(lottoGroup.getLottoGroupCode(), item.getLottoClassCode());
			SumPrize sumPrize = sumPrizeRepo.findByLottoNumberAndMsdLottoKindCodeAndLottoClassCode(
					item.getLottoNumber(), item.getMsdLottoKindCode(), item.getLottoClassCode());

			BigDecimal sumBetG = sumBetGroup == null ? BigDecimal.ZERO : sumBetGroup.getSumBet();
			BigDecimal sumPrizeCost = sumPrize == null ? BigDecimal.ZERO : sumPrize.getSumPrizeCost();

			transactionGroupService.updateLimitNumber(item.getMsdLottoKindCode(), item.getLottoNumber(),
					item.getLottoClassCode(), sumBetG, sumPrizeCost, lottoGroup);
			for (LimitNumber limitNumber : req) {
				transactionGroupService.updateLimitNumber(item.getMsdLottoKindCode(), limitNumber.getLottoNumber(),
						item.getLottoClassCode(), sumBetG, sumPrizeCost, lottoGroup);
			}
		}, 100);
	}

	List<LimitNumber> createSwapped(LimitNumber req) {
		String groupCodeSwapped = GenerateRandomString.generateUUID();
		req.setSwappedGroupCode(groupCodeSwapped);
		String lottoNumber = req.getLottoNumber();
		Set<String> mapNum = new HashSet<String>();

		mapNum.add(lottoNumber);
		mapNum.add("" + lottoNumber.charAt(0) + lottoNumber.charAt(2) + lottoNumber.charAt(1));
		mapNum.add("" + lottoNumber.charAt(1) + lottoNumber.charAt(0) + lottoNumber.charAt(2));
		mapNum.add("" + lottoNumber.charAt(1) + lottoNumber.charAt(2) + lottoNumber.charAt(0));
		mapNum.add("" + lottoNumber.charAt(2) + lottoNumber.charAt(1) + lottoNumber.charAt(0));
		mapNum.add("" + lottoNumber.charAt(2) + lottoNumber.charAt(0) + lottoNumber.charAt(1));

		List<LimitNumber> dataRes = new ArrayList<LimitNumber>();
		LimitNumber dataSet;

		List<String> result3 = new ArrayList<String>(mapNum);
		Collections.sort(result3);

		for (String num : result3) {
			dataSet = new LimitNumber();
			BeanUtils.copyProperties(req, dataSet);
			dataSet.setLimitNumberId(null);
			dataSet.setLimitNumberCode(GenerateRandomString.generateUUID());
			dataSet.setLottoNumber(num);
			dataRes.add(dataSet);
		}
		return dataRes;
	}

	public LimitNumberRes updateOne(LimitNumberReq req) {
		LimitNumber item = new LimitNumber();
		item.setReqToEntity(req);
		LimitNumber saveData = limitNumberRepo.save(item);
		LimitNumberRes returnData = new LimitNumberRes();
		returnData.setEntityToRes(saveData);

		sendSocket(req.getLottoClassCode());
		return returnData;
	}

	@Transactional
	public void deleteOne(Long id, String classCode) {
		LimitNumber limitNumber = limitNumberRepo.findById(id).get();

		LottoGroup lottoGroup = lottoGroupDao.getGroupByLottoKind(limitNumber.getMsdLottoKindCode(),
				limitNumber.getLottoClassCode());
		SumBetGroup sumBetGroup = sumBetGroupRepo.findByLottoGroupCodeAndLottoClassCode(lottoGroup.getLottoGroupCode(),
				limitNumber.getLottoClassCode());
		SumPrize sumPrize = sumPrizeRepo.findByLottoNumberAndMsdLottoKindCodeAndLottoClassCode(
				limitNumber.getLottoNumber(), limitNumber.getMsdLottoKindCode(), limitNumber.getLottoClassCode());

		BigDecimal sumBetG = sumBetGroup == null ? BigDecimal.ZERO : sumBetGroup.getSumBet();
		BigDecimal sumPrizeCost = sumPrize == null ? BigDecimal.ZERO : sumPrize.getSumPrizeCost();

		transactionGroupService.updateLimitNumber(limitNumber.getMsdLottoKindCode(), limitNumber.getLottoNumber(),
				limitNumber.getLottoClassCode(), sumBetG, sumPrizeCost, lottoGroup);

		if (LOTTO_KIND.DIGIT3_SWAPPED.equals(limitNumber.getMsdLottoKindCode())) {
			List<LimitNumber> listLim = createSwapped(limitNumber);
			for (LimitNumber limit : listLim) {
				limitNumberRepo.deleteByMsdLottoKindCodeAndLottoClassCodeAndLottoNumberAndIsManual(
						limit.getMsdLottoKindCode(), classCode, limit.getLottoNumber(), true);
				transactionGroupService.updateLimitNumber(limit.getMsdLottoKindCode(), limit.getLottoNumber(),
						limit.getLottoClassCode(), sumBetG, sumPrizeCost, lottoGroup);
			}
		}
		sendSocket(classCode);
	}

	@Transactional
	public List<LimitDtlRes> getAllByMSDLottoKindAndLottoClass(String kind, String lottoClass) {
		List<LimitDtlRes> resData = limitNumberDao.getByKind(lottoClass, kind);
		return resData;
	}

	@Transactional
	public void setEnable(Boolean status, Long id, String classCode) {
		LimitNumber data = limitNumberRepo.findById(id).get();
		if (status) {
			LimitNumber itemAuto = limitNumberRepo.findByMsdLottoKindCodeAndLottoClassCodeAndLottoNumberAndIsManual(
					data.getMsdLottoKindCode(), data.getLottoClassCode(), data.getLottoNumber(), false);
			if (itemAuto != null) {
				limitNumberRepo.delete(itemAuto);
			}
		} else {
			LottoGroup lottoGroup = lottoGroupDao.getGroupByLottoKind(data.getMsdLottoKindCode(),
					data.getLottoClassCode());
			SumBetGroup sumBetGroup = sumBetGroupRepo
					.findByLottoGroupCodeAndLottoClassCode(lottoGroup.getLottoGroupCode(), data.getLottoClassCode());
			SumPrize sumPrize = sumPrizeRepo.findByLottoNumberAndMsdLottoKindCodeAndLottoClassCode(
					data.getLottoNumber(), data.getMsdLottoKindCode(), data.getLottoClassCode());
			// Update Limit Number
			BigDecimal sumBetG = sumBetGroup == null ? BigDecimal.ZERO : sumBetGroup.getSumBet();
			BigDecimal sumPrizeCost = sumPrize == null ? BigDecimal.ZERO : sumPrize.getSumPrizeCost();
			transactionGroupService.updateLimitNumber(data.getMsdLottoKindCode(), data.getLottoNumber(),
					data.getLottoClassCode(), sumBetG, sumPrizeCost, lottoGroup);
		}

		data.setEnable(status);
		data.setUpdatedAt(new Date());
		data.setUpdatedBy(UserLoginUtil.getUsername());
		limitNumberRepo.save(data);

		if (LOTTO_KIND.DIGIT3_SWAPPED.equals(data.getMsdLottoKindCode())) {
			List<LimitNumber> listLim = createSwapped(data);
			for (LimitNumber limit : listLim) {
				if (status) {
					LimitNumber itemAuto = limitNumberRepo
							.findByMsdLottoKindCodeAndLottoClassCodeAndLottoNumberAndIsManual(
									limit.getMsdLottoKindCode(), limit.getLottoClassCode(), limit.getLottoNumber(),
									false);
					if (itemAuto != null) {
						limitNumberRepo.delete(itemAuto);
					}
				} else {
					LottoGroup lottoGroup = lottoGroupDao.getGroupByLottoKind(limit.getMsdLottoKindCode(),
							limit.getLottoClassCode());
					SumBetGroup sumBetGroup = sumBetGroupRepo.findByLottoGroupCodeAndLottoClassCode(
							lottoGroup.getLottoGroupCode(), limit.getLottoClassCode());
					SumPrize sumPrize = sumPrizeRepo.findByLottoNumberAndMsdLottoKindCodeAndLottoClassCode(
							limit.getLottoNumber(), limit.getMsdLottoKindCode(), limit.getLottoClassCode());
					// Update Limit Number
					BigDecimal sumBetG = sumBetGroup == null ? BigDecimal.ZERO : sumBetGroup.getSumBet();
					BigDecimal sumPrizeCost = sumPrize == null ? BigDecimal.ZERO : sumPrize.getSumPrizeCost();
					transactionGroupService.updateLimitNumber(limit.getMsdLottoKindCode(), limit.getLottoNumber(),
							limit.getLottoClassCode(), sumBetG, sumPrizeCost, lottoGroup);
				}
				LimitNumber limitFind = limitNumberRepo
						.findByMsdLottoKindCodeAndLottoClassCodeAndLottoNumberAndIsManual(data.getMsdLottoKindCode(),
								classCode, limit.getLottoNumber(), true);
				if (limitFind != null) {
					limitFind.setEnable(status);
					limitFind.setUpdatedAt(new Date());
					limitFind.setUpdatedBy(UserLoginUtil.getUsername());
					limitNumberRepo.save(limitFind);
				} else {
					limitNumberRepo.save(limit);
				}
			}
		}
		sendSocket(classCode);
	}

	public List<GetAllLimitRes> getAllListLimit(String classCode) {
		List<GetAllLimitRes> dataRes = new ArrayList<GetAllLimitRes>();
		List<MsdLottoKind> data = msdLottoKindDao.findMsdLottoInKindByClassCode(classCode);
		data.forEach(item -> {
			GetAllLimitRes dataSet = new GetAllLimitRes();
			dataSet.setKindCode(item.getMsdLottoKindCode());
			dataSet.setKindName(item.getMsdLottoKindName());
			List<LimitNumberAllRes> listNumber = new ArrayList<LimitNumberAllRes>();
			List<LimitNumber> listNumberFind = limitNumberRepo
					.findAllByMsdLottoKindCodeAndLottoClassCodeAndEnable(item.getMsdLottoKindCode(), classCode, true);
			LottoGroup lottoGroup = lottoGroupDao.getGroupByLottoKind(item.getMsdLottoKindCode(), classCode);
			List<PrizeSetting> listPrize = prizeSettingRepo.findByLottoGroupCodeAndMsdLottoKindCode(
					lottoGroup.getLottoGroupCode(), item.getMsdLottoKindCode());
			listNumberFind.forEach(limit -> {
				if (limit.getIsManual()) {
					// LimitNumberAllRes limitSet = new LimitNumberAllRes();
					// limitSet.setIsManual(true);
					// limitSet.setEnable(true);
					// limitSet.setLottoPrice(limit.getLottoPrice());
					// limitSet.setLottoNumber(limit.getLottoNumber());
					// listNumber.add(limitSet);

					List<PrizeSetting> listPrizeFilter = getListByDtl(listPrize, limit.getLottoGroupDtlCode());
					listPrizeFilter.forEach(prizeObj -> {
						LimitNumberAllRes limitSet = new LimitNumberAllRes();
						limitSet.setIsManual(false);
						limitSet.setEnable(true);
						limitSet.setLottoPrice(prizeObj.getPrize());
						limitSet.setLottoNumber(limit.getLottoNumber());
						limitSet.setVipCode(prizeObj.getVipCode());
						listNumber.add(limitSet);
					});
				} else {
					List<PrizeSetting> listPrizeFilter = getListByDtl(listPrize, limit.getLottoGroupDtlCode());
					listPrizeFilter.forEach(prizeObj -> {
						LimitNumberAllRes limitSet = new LimitNumberAllRes();
						limitSet.setIsManual(false);
						limitSet.setEnable(true);
						limitSet.setLottoPrice(prizeObj.getPrize());
						limitSet.setLottoNumber(limit.getLottoNumber());
						limitSet.setVipCode(prizeObj.getVipCode());
						listNumber.add(limitSet);
					});
				}
			});

			dataSet.setListLimit(listNumber);
			dataRes.add(dataSet);
		});
		return dataRes;
	}

	@Async
	public void sendSocket(String classCode) {
		TimeUtil.setTimeoutSync(() -> {
			this.simpMessagingTemplate.convertAndSend("/limit/" + classCode, getAllListLimit(classCode));
		}, 50);
	}

	private List<PrizeSetting> getListByDtl(List<PrizeSetting> listPrize, String dtlCode) {
		return listPrize.stream().filter(item -> item.getLottoGroupDtlCode().equals(dtlCode))
				.collect(Collectors.toList());
	}

	public List<GroupDtlRes> getDtlList(String classCode, String kindCode) {
		List<GroupDtlRes> dataRes = lottoGroupDao.getGrouDtl(kindCode, classCode);
		return dataRes;
	}
}
