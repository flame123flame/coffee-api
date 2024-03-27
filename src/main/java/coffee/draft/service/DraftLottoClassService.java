package coffee.draft.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coffee.lottoconfig.vo.req.AddLottoTimeReq;
import coffee.lottoconfig.vo.req.AddLottoTimeReq.TimeSellReq;
import coffee.lottoconfig.vo.res.LottoTimeRes;
import coffee.lottoconfig.vo.res.TimeSellRes;
import coffee.model.DraftLottoClass;
import coffee.model.DraftTimeSell;
import coffee.repo.jpa.DraftLottoClassRepo;
import coffee.repo.jpa.DraftTimeSellRepo;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE.SAVE;
import framework.utils.ConvertDateUtils;
import framework.utils.GenerateRandomString;
import framework.utils.UserLoginUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DraftLottoClassService {

    @Autowired
    private DraftLottoClassRepo lottoClassRepo;

    @Autowired
    private DraftTimeSellRepo timeSellRepo;

    @Transactional
    public String addLottoTime(AddLottoTimeReq req) {
        DraftLottoClass lottoClass = new DraftLottoClass();
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
        lottoClass.setRemarkVersion(req.getRemarkVersion());
        lottoClassRepo.save(lottoClass);

        log.info("save time sell LottoClassCode:" + req.getLottoClassCode());
        DraftTimeSell dataSet;
        for (TimeSellReq timeSellReq : req.getTimeSell()) {
            dataSet = new DraftTimeSell();
            dataSet.setTimeSellCode(GenerateRandomString.generateUUID());
            dataSet.setLottoClassCode(lottoClass.getLottoClassCode());
            dataSet.setCreatedBy(UserLoginUtil.getUsername());
            dataSet.setTimeOpen(ConvertDateUtils.parseStringToDate(timeSellReq.getTimeOpen(),
                    ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
            dataSet.setTimeClose(ConvertDateUtils.parseStringToDate(timeSellReq.getTimeClose(),
                    ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
            dataSet.setDraftCode(lottoClass.getDraftCode());
            timeSellRepo.save(dataSet);
        }
        return SAVE.SUCCESS;
    }

    public LottoTimeRes getLottoTimeByCode(String draftCode) {
        DraftLottoClass dataLc = lottoClassRepo.findByDraftCode(draftCode);
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
        dataRes.setDraftCode(dataLc.getDraftCode());
        dataRes.setRemarkVersion(dataLc.getRemarkVersion());
        dataRes.setCreatedBy(dataLc.getCreatedBy());
        dataRes.setCreatedAt(dataLc.getCreatedAt());

        List<DraftTimeSell> dataTsFind = timeSellRepo.findByDraftCode(draftCode);
        List<TimeSellRes> rsList = new ArrayList<TimeSellRes>();
        TimeSellRes setTs;
        for (DraftTimeSell ts : dataTsFind) {
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

    public List<DraftLottoClass> findByLottoCategoryCode(String lottoCategoryCode) {
        return lottoClassRepo.findByLottoCategoryCodeOrderByCreatedAtDesc(lottoCategoryCode);
    }

    public Integer getCountInit(String categoryCode) {
        return lottoClassRepo.getCountInit(categoryCode);
    }

}