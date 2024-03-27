package coffee.draft.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coffee.draft.vo.req.SubmitDraftReq;
import coffee.model.ApproveClass;
import coffee.model.DraftLottoClass;
import coffee.model.DraftTimeSell;
import coffee.model.LottoClass;
import coffee.model.TimeSell;
import coffee.repo.jpa.ApproveClassRepo;
import coffee.repo.jpa.DraftLottoClassRepo;
import coffee.repo.jpa.DraftTimeSellRepo;
import coffee.repo.jpa.LottoClassRepository;
import coffee.repo.jpa.TimeSellRepo;
import framework.utils.GenerateRandomString;
import framework.utils.UserLoginUtil;

@Service
public class DraftApproveClassService {

    @Autowired
    private ApproveClassRepo approveClassRepo;

    @Autowired
    private LottoClassRepository lottoClassRepo;

    @Autowired
    private DraftLottoClassRepo draftLottoClassRepo;

    @Autowired
    private DraftTimeSellRepo draftTimeSellRepo;

    @Autowired
    private TimeSellRepo timeSellRepo;

    @Transactional
    public String approveConfigClass(SubmitDraftReq req) {
        List<ApproveClass> approveClass = approveClassRepo.findByDraftCode(req.getDraftCode());

        List<ApproveClass> approveClassApproved = approveClass.stream().filter(item -> item.getIsApprove())
                .collect(Collectors.toList());
        List<ApproveClass> approveClassReject = approveClass.stream().filter(item -> !item.getIsApprove())
                .collect(Collectors.toList());

        if (approveClassApproved.size() >= 3) {
            return "SUCCESS_APPROVED";
        }
        if (approveClassReject.size() >= 1) {
            return "HAS_REJECT";
        }
        ApproveClass myApproveClass = approveClass.stream()
                .filter(item -> item.getCreatedBy().equals(UserLoginUtil.getUsername())).findFirst().orElse(null);
        if (myApproveClass != null) {
            return "HAS_APPROVED";
        }
        ApproveClass dataSave = new ApproveClass();
        dataSave.setLottoClassCode(req.getClassCode());
        dataSave.setIsApprove(req.getIsApprove());
        dataSave.setDraftCode(req.getDraftCode());
        dataSave.setCreatedBy(UserLoginUtil.getUsername());
        approveClassRepo.save(dataSave);

        if (approveClassApproved.size() == 2 && req.getIsApprove()) {
            setDraftToConfig(req.getDraftCode(), req.getClassCode());
            draftLottoClassRepo.updateStatusApprove(req.getDraftCode());
        }
        if (!req.getIsApprove()) {
            draftLottoClassRepo.updateStatusReject(req.getDraftCode());
        }
        return "SUCCESS";
    }

    private void setDraftToConfig(String draftCode, String classCode) {
        LottoClass configClass = lottoClassRepo.findByLottoClassCode(classCode);
        DraftLottoClass draftClass = draftLottoClassRepo.findByDraftCode(draftCode);
        if (configClass == null) {
            configClass = new LottoClass();
            configClass.setCreatedBy(UserLoginUtil.getUsername());
            configClass.setCloseMessage("ยังไม่เปิดรับแทง");
        } else {
            configClass.setUpdatedAt(new Date());
            configClass.setUpdatedBy(UserLoginUtil.getUsername());
        }
        configClass.setLottoClassCode(draftClass.getLottoClassCode());
        configClass.setCreatedBy(UserLoginUtil.getUsername());
        configClass.setClassName(draftClass.getClassName());
        configClass.setRuleDes(draftClass.getRuleDes());
        configClass.setTypeInstallment(draftClass.getTypeInstallment());
        configClass.setLottoCategoryCode(draftClass.getLottoCategoryCode());
        configClass.setAffiliateList(draftClass.getAffiliateList());
        configClass.setGroupList(draftClass.getGroupList());
        configClass.setTimeAfterBuy(draftClass.getTimeAfterBuy());
        configClass.setTimeBeforeLotto(draftClass.getTimeBeforeLotto());
        configClass.setPrefixTransNumber(draftClass.getPrefixTransNumber());
        configClass.setCountRefund(draftClass.getCountRefund());
        configClass.setAutoUpdateWallet(draftClass.getAutoUpdateWallet());
        configClass.setIgnoreWeekly(draftClass.getIgnoreWeekly());
        configClass.setLottoClassImg(draftClass.getLottoClassImg());
        configClass.setLottoClassColor(draftClass.getLottoClassColor());
        lottoClassRepo.save(configClass);

        timeSellRepo.deleteByLottoClassCode(classCode);

        List<DraftTimeSell> dataTsFind = draftTimeSellRepo.findByDraftCode(draftCode);
        TimeSell dataSet;
        for (DraftTimeSell timeSellReq : dataTsFind) {
            dataSet = new TimeSell();
            dataSet.setTimeSellCode(GenerateRandomString.generateUUID());
            dataSet.setLottoClassCode(classCode);
            dataSet.setCreatedBy(UserLoginUtil.getUsername());
            dataSet.setTimeOpen(timeSellReq.getTimeOpen());
            dataSet.setTimeClose(timeSellReq.getTimeClose());
            timeSellRepo.save(dataSet);
        }
    }

    public List<ApproveClass> getDraftApproveClass(SubmitDraftReq req) {
        List<ApproveClass> dataRes = approveClassRepo.findByDraftCode(req.getDraftCode());
        return dataRes;
    }
}