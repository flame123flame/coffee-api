package coffee.lottoconfig.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coffee.lottoconfig.vo.req.AddLottoTimeReq;
import coffee.lottoconfig.vo.req.AddLottoTimeReq.TimeSellReq;
import coffee.lottoconfig.vo.res.LottoTimeRes;
import coffee.lottoconfig.vo.res.TimeSellRes;
import coffee.model.LottoClass;
import coffee.model.TimeSell;
import coffee.repo.jpa.LottoClassRepository;
import coffee.repo.jpa.TimeSellRepo;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE.EDIT;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE.SAVE;
import framework.utils.ConvertDateUtils;
import framework.utils.GenerateRandomString;
import framework.utils.UserLoginUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AddLottoService {

	@Autowired
	private LottoClassRepository lottoClassRepo;

	@Autowired
	private TimeSellRepo timeSellRepo;

	@Transactional
	public String addLottoTime(AddLottoTimeReq req) {
		LottoClass lottoClass = lottoClassRepo.findByLottoClassCode(req.getLottoClassCode());
		if (lottoClass == null) {
			lottoClass = new LottoClass();
		} else {
			log.info(SAVE.DUPLICATE_DATA + " => " + req.getLottoClassCode());
			return SAVE.DUPLICATE_DATA;
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

		lottoClass.setLottoClassImg(req.getLottoClassImg());
		lottoClass.setLottoClassColor(req.getLottoClassColor());
		lottoClassRepo.save(lottoClass);

		log.info("save time sell LottoClassCode:" + req.getLottoClassCode());
		TimeSell dataSet;
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
			timeSellRepo.save(dataSet);
		}
		return SAVE.SUCCESS;
	}

	public String editLottoTime(AddLottoTimeReq req) {
		LottoClass lottoClass = lottoClassRepo.findByLottoClassCode(req.getLottoClassCode());
		lottoClass.setUpdatedAt(new Date());
		lottoClass.setUpdatedBy(UserLoginUtil.getUsername());
		lottoClass.setClassName(req.getLottoClassName());
		lottoClass.setRuleDes(req.getRuleDes());
		lottoClass.setTypeInstallment(req.getTypeInstallment());
		lottoClass.setLottoCategoryCode(req.getLottoCategoryCode());
		lottoClass.setTimeAfterBuy(req.getTimeAfterBuy());
		lottoClass.setTimeBeforeLotto(req.getTimeBeforeLotto());
		lottoClass.setAffiliateList(req.getAffiliateList());
		lottoClass.setGroupList(req.getGroupList());
		lottoClass.setLottoClassImg(req.getLottoClassImg());
		lottoClass.setLottoClassColor(req.getLottoClassColor());
		lottoClass.setAutoUpdateWallet(req.getAutoUpdateWallet());
		lottoClass.setIgnoreWeekly(req.getIgnoreWeekly());
		lottoClassRepo.save(lottoClass);

		lottoClass.setPrefixTransNumber(req.getPrefixCode());
		lottoClass.setCountRefund(req.getCountRefund());

		log.info("save time sell LottoClassCode:" + req.getLottoClassCode());
		TimeSell dataSet;
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
			timeSellRepo.save(dataSet);
		}
		return EDIT.SUCCESS;
	}

	public LottoTimeRes getLottoTimeByCode(String code) {
		LottoClass dataLc = lottoClassRepo.findByLottoClassCode(code);
		LottoTimeRes dataRes = new LottoTimeRes();
		dataRes.setLottoClassId(dataLc.getLottoClassId());
		dataRes.setLottoClassCode(dataLc.getLottoClassCode());
		dataRes.setLottoClassName(dataLc.getClassName());
		dataRes.setRuleDes(dataLc.getRuleDes());
		dataRes.setTypeInstallment(dataLc.getTypeInstallment());
		dataRes.setLottoCategoryCode(dataLc.getLottoCategoryCode());
		dataRes.setTimeAfterBuy(dataLc.getTimeAfterBuy());
		dataRes.setTimeBeforeLotto(dataLc.getTimeBeforeLotto());
		dataRes.setAffiliateList(dataLc.getAffiliateList());
		dataRes.setGroupList(dataLc.getGroupList());
		dataRes.setLottoClassImg(dataLc.getLottoClassImg());
		dataRes.setLottoClassColor(dataLc.getLottoClassColor());
		dataRes.setCountRefund(dataLc.getCountRefund());
		dataRes.setPrefixCode(dataLc.getPrefixTransNumber());
		dataRes.setAutoUpdateWallet(dataLc.getAutoUpdateWallet());
		dataRes.setIgnoreWeekly(dataLc.getIgnoreWeekly());

		List<TimeSell> dataTsFind = timeSellRepo.findByLottoClassCode(dataRes.getLottoClassCode());
		List<TimeSellRes> rsList = new ArrayList<TimeSellRes>();
		TimeSellRes setTs;
		for (TimeSell ts : dataTsFind) {
			setTs = new TimeSellRes();
			setTs.setTimeSellId(ts.getTimeSellId());
			setTs.setTimeSellCode(ts.getTimeSellCode());
			setTs.setLottoClassCode(ts.getLottoClassCode());
			setTs.setTimeOpen(ts.getTimeOpen());
			setTs.setTimeClose(ts.getTimeClose());
			rsList.add(setTs);
		}
		dataRes.setTimeSell(rsList);
		return dataRes;
	}

	public void changeStatusLotto(Boolean status, Long id, String remark) {
		LottoClass data = lottoClassRepo.findById(id).get();
		System.out.println(status);
		if (status) {
			data.setViewStatus("SHOW");
			data.setCloseMessage(null);
		} else {
			data.setViewStatus("HIDE");
			data.setCloseMessage(remark);
		}
		lottoClassRepo.save(data);

	}

	@Transactional
	public void deleteLottoClass(String code) {
		lottoClassRepo.deleteByLottoClassCode(code);
		timeSellRepo.deleteByLottoClassCode(code);
	}

	@Transactional
	public void deleteTimeSellByCode(String code) {
		timeSellRepo.deleteByTimeSellCode(code);
	}

}
