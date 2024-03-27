package coffee.lottoconfig.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coffee.lottoconfig.vo.req.AddGroupRiskReq;
import coffee.lottoconfig.vo.req.AddGroupRiskReq.LottoGroupKindReq;
import coffee.lottoconfig.vo.req.addgroup.MinmaxReq;
import coffee.lottoconfig.vo.req.addgroup.PrizeReq;
import coffee.lottoconfig.vo.res.PrizeSettingData;
import coffee.lottoconfig.vo.res.PrizeSettingRes;
import coffee.lottoconfig.vo.res.addgroup.GroupRiskClassRes;
import coffee.lottoconfig.vo.res.addgroup.GroupRiskList;
import coffee.lottoconfig.vo.res.addgroup.Minmax;
import coffee.lottoconfig.vo.res.addgroup.Prize;
import coffee.lottoconfig.vo.res.addgroup.PrizeList;
import coffee.model.GroupKindMap;
import coffee.model.GroupMaxMinMap;
import coffee.model.LottoGroup;
import coffee.model.LottoGroupDtl;
import coffee.model.MsdLottoKind;
import coffee.model.PrizeSetting;
import coffee.repo.dao.GroupKindMapDao;
import coffee.repo.dao.PrizeSettingDao;
import coffee.repo.jpa.GroupKindMapRepo;
import coffee.repo.jpa.GroupMaxMinMapRepo;
import coffee.repo.jpa.LottoGroupDtlRepo;
import coffee.repo.jpa.LottoGroupRepo;
import coffee.repo.jpa.PrizeSettingRepo;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE.SAVE;
import framework.utils.GenerateRandomString;
import framework.utils.UserLoginUtil;

@Service
public class GroupRiskService2 {

	@Autowired
	private LottoGroupRepo lottoGroupRepo;

	@Autowired
	private LottoGroupDtlRepo lottoGroupDtlRepo;

	@Autowired
	private GroupKindMapRepo groupKindMapRepo;

	@Autowired
	private PrizeSettingRepo prizeSettingRepo;

	@Autowired
	private GroupMaxMinMapRepo groupMaxMinMapRepo;

	@Autowired
	private PrizeSettingDao prizeSettingDao;

	@Autowired
	private GroupKindMapDao groupKindMapDao;

	@Transactional
	public String addGroupRisk(AddGroupRiskReq req) {

		// Lotto Group
		LottoGroup lottoGroup = lottoGroupRepo.findByLottoGroupCode(req.getGroupCode());
		if (lottoGroup != null) {
			lottoGroup.setUpdatedBy(UserLoginUtil.getUsername());
			lottoGroup.setUpdatedAt(new Date());
		} else {
			lottoGroup = new LottoGroup();
			lottoGroup.setLottoGroupCode(GenerateRandomString.generateUUID());
			lottoGroup.setCreatedBy(UserLoginUtil.getUsername());
		}
		lottoGroup.setGroupMaxClose(req.getGroupMaxClose());
		lottoGroup.setGroupEarningsPercent(req.getGroupEarningsPercent());
		// lottoGroup.setGroupEarningsPercentClose(req.getGroupEarningsPercentClose());
		lottoGroup.setGroupName(req.getGroupName());
		lottoGroup.setLottoClassCode(req.getLottoClassCode());
		lottoGroupRepo.save(lottoGroup);

		// Lotto Group Kind Map
		for (String item : req.getKindCode()) {
			GroupKindMap groupKindMap = groupKindMapRepo
					.findByLottoGroupCodeAndMsdLottoKindCode(lottoGroup.getLottoGroupCode(), item);
			if (groupKindMap == null) {
				groupKindMap = new GroupKindMap();
				groupKindMap.setGroupKindMapCode(GenerateRandomString.generateUUID());
				groupKindMap.setCreatedBy(UserLoginUtil.getUsername());
			} else {
				groupKindMap.setUpdatedAt(new Date());
				groupKindMap.setUpdatedBy(UserLoginUtil.getUsername());
			}
			groupKindMap.setMsdLottoKindCode(item);
			groupKindMap.setLottoGroupCode(lottoGroup.getLottoGroupCode());
			groupKindMapRepo.save(groupKindMap);
		}

		// Lotto Group Dtl
		int level = 1;
		for (LottoGroupKindReq item : req.getGroupRiskList()) {
			LottoGroupDtl lottoGroupDtl = null;
			if (item.getGroupDtlCode() != null) {
				lottoGroupDtl = lottoGroupDtlRepo.findByLottoGroupDtlCode(item.getGroupDtlCode());
				if (lottoGroupDtl == null) {
					lottoGroupDtl = new LottoGroupDtl();
					lottoGroupDtl.setLottoGroupDtlCode(GenerateRandomString.generateUUID());
					lottoGroupDtl.setCreatedBy(UserLoginUtil.getUsername());
				} else {
					lottoGroupDtl.setUpdatedAt(new Date());
					lottoGroupDtl.setUpdatedBy(UserLoginUtil.getUsername());
				}
			} else {
				lottoGroupDtl = new LottoGroupDtl();
				lottoGroupDtl.setLottoGroupDtlCode(GenerateRandomString.generateUUID());
				lottoGroupDtl.setCreatedBy(UserLoginUtil.getUsername());
			}
			lottoGroupDtl.setLottoGroupCode(lottoGroup.getLottoGroupCode());
			lottoGroupDtl.setGroupMaxRisk(item.getGroupMaxRisk());
			lottoGroupDtl.setPercentForLimit(item.getPercentForLimit());
			lottoGroupDtl.setTierLevel(level);
			lottoGroupDtlRepo.save(lottoGroupDtl);
			level++;
		}
		return null;
	}

	public AddGroupRiskReq getGroupRiskByCode(String lottoClassCode, String lottoGroupCode) {
		AddGroupRiskReq dataRes = new AddGroupRiskReq();
		LottoGroup lottoGroup = lottoGroupRepo.findByLottoGroupCode(lottoGroupCode);
		dataRes.setGroupMaxClose(lottoGroup.getGroupMaxClose());
		dataRes.setGroupEarningsPercent(lottoGroup.getGroupEarningsPercent());
		dataRes.setGroupName(lottoGroup.getGroupName());
		dataRes.setLottoClassCode(lottoGroup.getLottoClassCode());
		dataRes.setGroupCode(lottoGroup.getLottoGroupCode());
		dataRes.setGroupEarningsPercentClose(lottoGroup.getGroupEarningsPercentClose());
		List<GroupKindMap> groupKindMapList = groupKindMapRepo.findByLottoGroupCode(lottoGroupCode);
		List<String> groupKindRes = new ArrayList<String>();
		// Lotto Group Kind Map
		for (GroupKindMap item : groupKindMapList) {
			groupKindRes.add(item.getMsdLottoKindCode());
		}
		dataRes.setKindCode(groupKindRes);

		List<LottoGroupDtl> lottoGroupDtl = lottoGroupDtlRepo.findByLottoGroupCode(lottoGroupCode);
		List<LottoGroupKindReq> lottoGroupList = new ArrayList<LottoGroupKindReq>();
		// Lotto Group Dtl
		for (LottoGroupDtl item : lottoGroupDtl) {
			LottoGroupKindReq lottoGroupDtlData = new LottoGroupKindReq();
			lottoGroupDtlData.setGroupDtlCode(item.getLottoGroupDtlCode());
			lottoGroupDtlData.setGroupMaxRisk(item.getGroupMaxRisk());
			lottoGroupDtlData.setPercentForLimit(item.getPercentForLimit());
			lottoGroupList.add(lottoGroupDtlData);
		}
		dataRes.setGroupRiskList(lottoGroupList);

		return dataRes;
	}

	public List<GroupRiskClassRes> getByClass(String lottoClassCode) {
		List<GroupRiskClassRes> dataRes = new ArrayList<GroupRiskClassRes>();
		List<LottoGroup> lottoGroupList = lottoGroupRepo.findAllByLottoClassCode(lottoClassCode);
		for (LottoGroup lottoGroup : lottoGroupList) {
			GroupRiskClassRes groupRiskClassRes = new GroupRiskClassRes();
			groupRiskClassRes.setGroupCode(lottoGroup.getLottoGroupCode());
			groupRiskClassRes.setGroupName(lottoGroup.getGroupName());
			groupRiskClassRes.setGroupMaxClose(lottoGroup.getGroupMaxClose());
			groupRiskClassRes.setGroupEarningsPercent(lottoGroup.getGroupEarningsPercent());
			groupRiskClassRes.setGroupEarningsPercentClose(lottoGroup.getGroupEarningsPercentClose());
			groupRiskClassRes.setGroupRiskList(setGroupRiskList(groupRiskClassRes.getGroupCode(), lottoClassCode));
			dataRes.add(groupRiskClassRes);
		}
		return dataRes;
	}

	private List<GroupRiskList> setGroupRiskList(String lottoGroupCode, String lottoClassCode) {
		List<GroupRiskList> dataRes = new ArrayList<GroupRiskList>();
		List<MsdLottoKind> groupKindMapList = groupKindMapDao.getByGroupCode(lottoGroupCode);
		for (MsdLottoKind groupKindMap : groupKindMapList) {
			GroupRiskList dataSet = new GroupRiskList();
			dataSet.setKindCode(groupKindMap.getMsdLottoKindCode());
			dataSet.setKindName(groupKindMap.getMsdLottoKindName());
			GroupRiskList getMaxMinListPrize = getMaxMinListPrize(lottoGroupCode, groupKindMap.getMsdLottoKindCode(),
					lottoClassCode);
			dataSet.setMinmax(getMaxMinListPrize.getMinmax());
			dataSet.setPrize(getMaxMinListPrize.getPrize());
			dataRes.add(dataSet);
		}
		return dataRes;
	}

	private GroupRiskList getMaxMinListPrize(String lottoGroupCode, String msdLottoKindCode, String lottoClassCode) {
		GroupRiskList dataRes = new GroupRiskList();

		List<GroupMaxMinMap> maxmin = groupMaxMinMapRepo.findByLottoClassCodeAndMsdLottoKindCode(lottoClassCode,
				msdLottoKindCode);
		List<Prize> prize = new ArrayList<Prize>();

		List<Prize> vipCodeList = prizeSettingDao.getPrizeListForGroupRiskByClassCode(lottoGroupCode, msdLottoKindCode);

		for (Prize vipCode : vipCodeList) {
			Prize prizeSet = new Prize();
			List<PrizeList> prizeSettingData = prizeSettingDao.getPrizeListForGroupRiskByVipCode(lottoGroupCode,
					msdLottoKindCode, vipCode.getVipCode());
			prizeSet.setVipCode(vipCode.getVipCode());
			prizeSet.setPrizeList(prizeSettingData);
			prize.add(prizeSet);
		}
		dataRes.setMinmax(maxmin);
		dataRes.setPrize(prize);

		return dataRes;
	}

	public String addMinMax(MinmaxReq req) {

		GroupMaxMinMap dataSet;
		for (Minmax MinmaxReq : req.getLottoMaxMinList()) {
			dataSet = null;
			dataSet = groupMaxMinMapRepo.findByGroupMaxMinMapCode(MinmaxReq.getGroupMaxMinMapCode());
			if (dataSet == null) {
				dataSet = new GroupMaxMinMap();
				dataSet.setGroupMaxMinMapCode(GenerateRandomString.generateUUID());
				dataSet.setLottoClassCode(MinmaxReq.getLottoClassCode());
				dataSet.setCreatedBy(UserLoginUtil.getUsername());
			} else {
				dataSet.setUpdatedAt(new Date());
				dataSet.setUpdatedBy(UserLoginUtil.getUsername());
			}
			dataSet.setMaximumPerTrans(MinmaxReq.getMaximumTrans());
			dataSet.setMaximumPerUser(MinmaxReq.getMaximumUsername());
			dataSet.setMinimumPerTrans(MinmaxReq.getMinimumTrans());
			dataSet.setMsdLottoKindCode(MinmaxReq.getMsdLottoKindCode());
			dataSet.setLottoGroupCode(MinmaxReq.getLottoGroupCode());
			dataSet.setCreatedAt(new Date());
			dataSet.setVipCode(MinmaxReq.getVipCode());
			groupMaxMinMapRepo.save(dataSet);
		}

		return SAVE.SUCCESS;
	}

	public String addPrizeSetting(PrizeReq req) {
		PrizeSetting dataSet;

		for (PrizeSettingRes prizeData : req.getPrizeListSetting()) {
			for (PrizeSettingRes prizeListData : prizeData.getList()) {
				PrizeSetting prizeSetting = prizeSettingRepo
						.findFirstByLottoGroupCodeOrderBySeqOrderDesc(prizeListData.getLottoGroupCode());
				int i = 0;
				if (prizeSetting != null) {
					PrizeSetting prizeSetting1 = prizeSettingRepo.findFirstByLottoGroupCodeAndLottoGroupDtlCode(
							prizeListData.getLottoGroupCode(), prizeListData.getLottoGroupDtlCode());
					if (prizeSetting1 != null) {
						i = prizeSetting1.getSeqOrder();
					} else {
						i = prizeSetting.getSeqOrder() + 1;
					}
				}
				dataSet = null;
				dataSet = prizeSettingRepo.findByPrizeSettingId(prizeListData.getPrizeSettingId());
				if (dataSet == null) {
					dataSet = new PrizeSetting();
					dataSet.setPrizeSettingCode(GenerateRandomString.generateUUID());
					dataSet.setLottoGroupCode(prizeListData.getLottoGroupCode());
					dataSet.setCreatedBy(UserLoginUtil.getUsername());
				} else {
					dataSet.setUpdatedAt(new Date());
					dataSet.setUpdatedBy(UserLoginUtil.getUsername());
					dataSet.setPrize(prizeListData.getPrize());
					dataSet.setLottoGroupDtlCode(prizeListData.getLottoGroupDtlCode());
					dataSet.setMsdLottoKindCode(prizeListData.getMsdLottoKindCode());
					dataSet.setCreatedAt(new Date());
					dataSet.setVipCode(prizeData.getVipCode());
				}
				dataSet.setPrize(prizeListData.getPrize());
				dataSet.setLottoGroupDtlCode(prizeListData.getLottoGroupDtlCode());
				dataSet.setMsdLottoKindCode(prizeListData.getMsdLottoKindCode());
				dataSet.setCreatedAt(new Date());
				dataSet.setVipCode(prizeData.getVipCode());
				dataSet.setSeqOrder(i);
				prizeSettingRepo.save(dataSet);
				i++;
			}

		}

		return SAVE.SUCCESS;
	}

	public String editPrizeSetting(PrizeReq req) {
		PrizeSetting dataSet;

		for (PrizeSettingRes prizeData : req.getPrizeListSetting()) {
			int i = 0;
			for (PrizeSettingRes prizeListData : prizeData.getList()) {
				dataSet = null;
				dataSet = prizeSettingRepo.findByPrizeSettingId(prizeListData.getPrizeSettingId());
				if (dataSet == null) {
					dataSet = new PrizeSetting();
					dataSet.setPrizeSettingCode(GenerateRandomString.generateUUID());
					dataSet.setLottoGroupCode(prizeListData.getLottoGroupCode());
					dataSet.setCreatedBy(UserLoginUtil.getUsername());
				} else {
					dataSet.setUpdatedAt(new Date());
					dataSet.setUpdatedBy(UserLoginUtil.getUsername());
					dataSet.setPrize(prizeListData.getPrize());
					dataSet.setLottoGroupDtlCode(prizeListData.getLottoGroupDtlCode());
					dataSet.setMsdLottoKindCode(prizeListData.getMsdLottoKindCode());
					dataSet.setCreatedAt(new Date());
					dataSet.setVipCode(prizeData.getVipCode());
				}
				prizeSettingRepo.save(dataSet);
				i++;
			}

		}

		return SAVE.SUCCESS;
	}

	public List<PrizeSettingRes> getPrizeSettingById(String msdLottoKindCode, String lottoGroupCode) {
		List<PrizeSetting> dataPs = prizeSettingRepo.findByLottoGroupCodeAndMsdLottoKindCode(lottoGroupCode,
				msdLottoKindCode);

		List<PrizeSettingRes> dataList = new ArrayList<PrizeSettingRes>();
		for (PrizeSetting dataSet : dataPs) {
			PrizeSettingRes dataRes = new PrizeSettingRes();
			dataRes.setPrizeSettingId(dataSet.getPrizeSettingId());
			dataRes.setPrizeSettingCode(dataSet.getPrizeSettingCode());
			dataRes.setLottoGroupCode(dataSet.getLottoGroupCode());
			dataRes.setMsdLottoKindCode(dataSet.getMsdLottoKindCode());
			dataRes.setLottoGroupDtlCode(dataSet.getLottoGroupDtlCode());
			dataRes.setPrize(dataSet.getPrize());
			dataRes.setPrizeLimit(dataSet.getPrizeLimit());
			dataRes.setVipCode(dataSet.getVipCode());
			dataRes.setSeqOrder(dataSet.getSeqOrder());
			dataList.add(dataRes);
		}

		return dataList;
	}

	public PrizeSettingData getVipPrizeSetting(String lottoClassCode, String msdLottoKindCode, String lottoGroupCode) {
		PrizeSettingData dataSet;
		List<GroupMaxMinMap> maxmin = groupMaxMinMapRepo.findByLottoClassCodeAndMsdLottoKindCode(lottoClassCode,
				msdLottoKindCode);
		List<LottoGroupDtl> groupRiks = lottoGroupDtlRepo.findByLottoGroupCode(lottoGroupCode);
		dataSet = new PrizeSettingData();
		dataSet.setGroupMaxmin(maxmin);
		dataSet.setLottoGroup(groupRiks);

		return dataSet;
	}

	public List<Minmax> getGroupMinMaxByCode(String lottoClassCode, String msdLottoKindCode, String lottoGroupCode) {
		List<Minmax> minmaxList = new ArrayList<Minmax>();
		List<GroupMaxMinMap> maxmin = groupMaxMinMapRepo.findByLottoClassCodeAndMsdLottoKindCode(lottoClassCode,
				msdLottoKindCode);
		for (GroupMaxMinMap dataRes : maxmin) {
			Minmax dataSet = new Minmax();
			dataSet.setVipCode(dataRes.getVipCode());
			dataSet.setMinimumTrans(dataRes.getMinimumPerTrans());
			dataSet.setMaximumTrans(dataRes.getMaximumPerTrans());
			dataSet.setMaximumUsername(dataRes.getMaximumPerUser());
			dataSet.setGroupMaxMinMapCode(dataRes.getGroupMaxMinMapCode());
			dataSet.setMsdLottoKindCode(dataRes.getMsdLottoKindCode());
			dataSet.setLottoClassCode(dataRes.getLottoClassCode());
			minmaxList.add(dataSet);
		}
		return minmaxList;
	}

	@Transactional
	public void deletelottoGroupKind(String msdLottoKindCode, String lottoGroupCode, String lottoClassCode) {
		System.out.println("\\\\\\\\\\\\\\" + msdLottoKindCode + " \\\\\\\\\\\\\\ " + lottoGroupCode + "\\\\\\\\\\\\\\ "
				+ lottoClassCode);
		groupKindMapRepo.deleteByMsdLottoKindCodeAndLottoGroupCode(msdLottoKindCode, lottoGroupCode);
		groupMaxMinMapRepo.deleteByMsdLottoKindCodeAndLottoGroupCodeAndLottoClassCode(msdLottoKindCode, lottoGroupCode,
				lottoClassCode);
		prizeSettingRepo.deleteByMsdLottoKindCodeAndLottoGroupCode(msdLottoKindCode, lottoGroupCode);
	}

	@Transactional
	public void deleteLottoGroupRisk(String lottoGroupCode) {
		lottoGroupRepo.deleteByLottoGroupCode(lottoGroupCode);
		lottoGroupDtlRepo.deleteBylottoGroupCode(lottoGroupCode);
		groupKindMapRepo.deleteByLottoGroupCode(lottoGroupCode);
		groupMaxMinMapRepo.deleteByLottoGroupCode(lottoGroupCode);
		prizeSettingRepo.deleteByLottoGroupCode(lottoGroupCode);
	}

	@Transactional
	public void deleteLottoGroupRisk(String msdLottoKindCode, String lottoGroupCode, String groupMaxMinMapCode) {
		groupMaxMinMapRepo.deleteByGroupMaxMinMapCode(groupMaxMinMapCode);
		prizeSettingRepo.deleteByMsdLottoKindCodeAndLottoGroupCode(msdLottoKindCode, lottoGroupCode);
	}

	@Transactional
	public void deleteGroupMaxrisk(String lottoGroupDtlCode) {
		lottoGroupDtlRepo.deleteByLottoGroupDtlCode(lottoGroupDtlCode);
		prizeSettingRepo.deleteByLottoGroupDtlCode(lottoGroupDtlCode);

	}

}
