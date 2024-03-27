package coffee.lottoconfig.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coffee.lottoconfig.vo.req.AddGroupRiskReq;
import coffee.lottoconfig.vo.req.AddGroupRiskReq.LottoGroupKindReq;
import coffee.lottoconfig.vo.res.GroupRiskRes;
import coffee.lottoconfig.vo.res.PrizeSettingRes;
import coffee.model.LottoGroup;
import coffee.model.PrizeSetting;
import coffee.repo.dao.PrizeSettingDao;
import coffee.repo.jpa.LottoGroupRepo;
import coffee.repo.jpa.PrizeSettingRepo;
import framework.utils.GenerateRandomString;
import framework.utils.UserLoginUtil;

@Service
public class GroupRiskService {

	@Autowired
	private LottoGroupRepo lottoGroupRepo;

	@Autowired
	private PrizeSettingRepo prizeSettingRepo;
	
	@Autowired 
	private PrizeSettingDao prizeSettingDao;
	
//	public String addGroupRisk(AddGroupRiskReq req) {
//		LottoGroup lottoGroup = lottoGroupRepo.findByLottoGroupCode(req.getLottoGroupCode());
////		if (lottoGroup == null) {
////			lottoGroup = new LottoGroup();
////			lottoGroup.setLottoGroupCode(GenerateRandomString.generateUUID());
////			lottoGroup.setLottoClassCode(req.getLottoClassCode());
////			lottoGroup.setCreatedBy(UserLoginUtil.getUsername());
////		} else {
////			lottoGroup.setUpdatedBy(UserLoginUtil.getUsername());
////			lottoGroup.setUpdatedAt(new Date());
////		}
////		lottoGroup.setGroupName(req.getGroupName());
////		lottoGroup.setGroupMaxRisk(req.getGroupMaxRisk());
////		lottoGroup.setGroupMaxClose(req.getGroupMaxClose());
////		lottoGroup.setGroupEarningsPercent(req.getGroupEarningsPercent());
////		lottoGroup.setPercentForLimit(req.getPercentForLimit());
////		lottoGroupRepo.save(lottoGroup);
////
////		PrizeSetting prizeSetting = null;
////		for (LottoGroupKindReq kindReq : req.getPrizeSettingList()) {
////			prizeSetting=null;
////			if (StringUtils.isNotBlank(kindReq.getPrizeSettingCode())) {
////				prizeSetting = prizeSettingRepo.findByPrizeSettingCode(kindReq.getPrizeSettingCode());
////			}
////			if (prizeSetting == null) {
////				prizeSetting = new PrizeSetting();
////				prizeSetting.setPrizeSettingCode(GenerateRandomString.generateUUID());
////				prizeSetting.setCreatedBy(UserLoginUtil.getUsername());
////			} else {
////				prizeSetting.setUpdatedBy(UserLoginUtil.getUsername());
////				prizeSetting.setUpdatedAt(new Date());
////			}
////			prizeSetting.setPrize(kindReq.getPrize());
////			prizeSetting.setPrizeLimit(kindReq.getPrizeLimit());
////			prizeSetting.setLottoGroupCode(lottoGroup.getLottoGroupCode());
////			prizeSetting.setMsdLottoKindCode(kindReq.getMsdLottoKindCode());
////			prizeSettingRepo.save(prizeSetting);
////		}
//
//		return lottoGroup.getLottoGroupCode();
//	}
//
//	public List<GroupRiskRes> getAll() {
//		List<GroupRiskRes> dataRes = new ArrayList<GroupRiskRes>();
//		GroupRiskRes setGr;
//		List<PrizeSettingRes> psList;
//		PrizeSettingRes setPs;
//
//		List<LottoGroup> dataGrFind = lottoGroupRepo.findAll();
//		for (LottoGroup lg : dataGrFind) {
//			setGr = new GroupRiskRes();
//			setGr.setLottoGroupId(lg.getLottoGroupId());
//			setGr.setLottoGroupCode(lg.getLottoGroupCode());
//			setGr.setLottoClassCode(lg.getLottoClassCode());
////			setGr.setGroupName(lg.getGroupName());
////			setGr.setGroupMaxRisk(lg.getGroupMaxRisk());
////			setGr.setGroupMaxClose(lg.getGroupMaxClose());
////			setGr.setGroupEarningsPercent(lg.getGroupEarningsPercent());
////			setGr.setPercentForLimit(lg.getPercentForLimit());
//
//			List<PrizeSettingRes> dataPsFind = prizeSettingDao.findByLottoGroupCode(setGr.getLottoGroupCode());
////			psList = new ArrayList<PrizeSettingRes>();
////			for (PrizeSetting ps : dataPsFind) {
////				setPs = new PrizeSettingRes();
////				setPs.setPrizeSettingId(ps.getPrizeSettingId());
////				setPs.setPrizeSettingCode(ps.getPrizeSettingCode());
////				setPs.setLottoGroupCode(ps.getLottoGroupCode());
////				setPs.setMsdLottoKindCode(ps.getMsdLottoKindCode());
////				setPs.setPrize(ps.getPrize());
////				setPs.setPrizeLimit(ps.getPrizeLimit());
////				psList.add(setPs);
////
////			}
//			setGr.setPrizeSettingList(dataPsFind);
//			dataRes.add(setGr);
//		}
//
//		return dataRes;
//	}
//	
//	public List<GroupRiskRes> getGroupListByClassCode(String lottoClassCode) {
//		List<GroupRiskRes> dataRes = new ArrayList<GroupRiskRes>();
//		GroupRiskRes setGr;
//		List<PrizeSettingRes> psList;
//		PrizeSettingRes setPs;
//
//		List<LottoGroup> dataGrFind = lottoGroupRepo.findAllByLottoClassCode(lottoClassCode);
//		for (LottoGroup lg : dataGrFind) {
//			setGr = new GroupRiskRes();
//			setGr.setLottoGroupId(lg.getLottoGroupId());
//			setGr.setLottoGroupCode(lg.getLottoGroupCode());
//			setGr.setLottoClassCode(lg.getLottoClassCode());
////			setGr.setGroupName(lg.getGroupName());
////			setGr.setGroupMaxRisk(lg.getGroupMaxRisk());
////			setGr.setGroupMaxClose(lg.getGroupMaxClose());
////			setGr.setGroupEarningsPercent(lg.getGroupEarningsPercent());
////			setGr.setPercentForLimit(lg.getPercentForLimit());
//
//			List<PrizeSettingRes> dataPsFind = prizeSettingDao.findByLottoGroupCode(setGr.getLottoGroupCode());
////			psList = new ArrayList<PrizeSettingRes>();
////			for (PrizeSetting ps : dataPsFind) {
////				setPs = new PrizeSettingRes();
////				setPs.setPrizeSettingId(ps.getPrizeSettingId());
////				setPs.setPrizeSettingCode(ps.getPrizeSettingCode());
////				setPs.setLottoGroupCode(ps.getLottoGroupCode());
////				setPs.setMsdLottoKindCode(ps.getMsdLottoKindCode());
////				setPs.setPrize(ps.getPrize());
////				setPs.setPrizeLimit(ps.getPrizeLimit());
////				psList.add(setPs);
////
////			}
//			setGr.setPrizeSettingList(dataPsFind);
//			dataRes.add(setGr);
//		}
//
//		return dataRes;
//	}
//
//	public GroupRiskRes getGroupByCode(String lottoGroupCode) {
//		LottoGroup dataLg = lottoGroupRepo.findByLottoGroupCode(lottoGroupCode);
//		GroupRiskRes dataRes = new GroupRiskRes();
//		dataRes.setLottoGroupId(dataLg.getLottoGroupId());
//		dataRes.setLottoGroupCode(dataLg.getLottoGroupCode());
//		dataRes.setLottoClassCode(dataLg.getLottoClassCode());
////		dataRes.setGroupName(dataLg.getGroupName());
////		dataRes.setGroupMaxRisk(dataLg.getGroupMaxRisk());
////		dataRes.setGroupMaxClose(dataLg.getGroupMaxClose());
////		dataRes.setGroupEarningsPercent(dataLg.getGroupEarningsPercent());
////		dataRes.setPercentForLimit(dataLg.getPercentForLimit());
//
//		List<PrizeSettingRes> dataPsFind = prizeSettingDao.findByLottoGroupCode(dataLg.getLottoGroupCode());
////		List<PrizeSettingRes> psList = new ArrayList<PrizeSettingRes>();
////		PrizeSettingRes setPs;
////		for (PrizeSetting ps : dataPsFind) {
////			setPs = new PrizeSettingRes();
////			setPs.setPrizeSettingId(ps.getPrizeSettingId());
////			setPs.setPrizeSettingCode(ps.getPrizeSettingCode());
////			setPs.setLottoGroupCode(ps.getLottoGroupCode());
////			setPs.setMsdLottoKindCode(ps.getMsdLottoKindCode());
////			setPs.setPrize(ps.getPrize());
////			setPs.setPrizeLimit(ps.getPrizeLimit());
////			psList.add(setPs);
////		}
//		dataRes.setPrizeSettingList(dataPsFind);
//
//		return dataRes;
//	}
//	@Transactional
//	public void deleteLottoGroup(String code) {
//		lottoGroupRepo.deleteByLottoGroupCode(code);
//		prizeSettingRepo.deleteByLottoGroupCode(code);
//	}
//	@Transactional
//	public void deletePrizeSetting(String code) {
//		prizeSettingRepo.deleteByPrizeSettingCode(code);
//	}
}
