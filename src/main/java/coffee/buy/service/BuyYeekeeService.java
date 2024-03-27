package coffee.buy.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transaction;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coffee.buy.dao.BuyLottoDao;
import coffee.buy.vo.req.BuyLottoReq;
import coffee.buy.vo.req.SendHttpModel;
import coffee.buy.vo.req.BuyLottoReq.LottoBuy;
import coffee.buy.vo.req.BuyLottoReq.PayNumber;
import coffee.buy.vo.req.SubmitYeeKeeReq;
import coffee.buy.vo.res.BuyLottoRes;
import coffee.buy.vo.res.BuyLottoRes.BuyDetail;
import coffee.buy.vo.res.BuyLottoRes.LottoBuyDetailRes;
import coffee.buy.vo.res.CheckCostUserRes;
import coffee.buy.vo.res.Installment;
import coffee.buy.vo.res.LoopPrizeRes;
import coffee.buy.vo.res.YeeKeeNumberSubmitListRes;
import coffee.model.GroupMaxMinMap;
import coffee.model.LottoClass;
import coffee.model.LottoGroupTransaction;
import coffee.model.LottoTransaction;
import coffee.model.PrizeSetting;
import coffee.model.TimeSell;
import coffee.model.YeekeeGroupPrize;
import coffee.model.YeekeeSubmitNumber;
import coffee.model.YeekeeSumNumber;
import coffee.redis.model.YeekeeLastList;
import coffee.redis.repo.LastListYeekeeRedisRepo;
import coffee.redis.repo.UserBuyYeekeeRedisRepo;
import coffee.repo.dao.LottoGroupTransactionDao;
import coffee.repo.dao.LottoTransactionDao;
import coffee.repo.dao.PrizeSettingDao;
import coffee.repo.jpa.GroupMaxMinMapRepo;
import coffee.repo.jpa.LottoClassRepository;
import coffee.repo.jpa.LottoGroupTransactionRepo;
import coffee.repo.jpa.LottoTransactionRepo;
import coffee.repo.jpa.PrizeSettingRepo;
import coffee.repo.jpa.SumPrizeRepo;
import coffee.repo.jpa.TimeSellRepo;
import coffee.repo.jpa.YeekeeGroupPrizeRepo;
import coffee.repo.jpa.YeekeeSubmitNumberRepo;
import coffee.repo.jpa.YeekeeSumNumberRepo;
import coffee.web.service.WebTimeSellService;
import coffee.yeekeeResult.service.YeekeeResultService;
import coffee.yeekeeResult.vo.res.YeekeeResultRoundSeqRes;
import framework.constant.LottoConstant;
import framework.constant.LottoConstant.LOTTO_KIND;
import framework.constant.ProjectConstant;
import framework.utils.ConvertDateUtils;
import framework.utils.GenerateRandomString;
import framework.utils.UserLoginUtil;

@Service
public class BuyYeekeeService {

    @Autowired
    private UserBuyYeekeeRedisRepo userBuyYeekeeRedisRepo;

    @Autowired
    private YeeKeeSteamsCalculation yeeKeeSteamsCalculation;

    @Autowired
    private YeekeeSubmitNumberRepo yeekeeSubmitNumberRepo;

    @Autowired
    private TimeSellRepo timeSellRepo;

    @Autowired
    private WebTimeSellService webTimeSellService;

    @Autowired
    private GroupMaxMinMapRepo groupMaxMinMapRepo;

    @Autowired
    private BuyLottoDao buyLottoDao;

    @Autowired
    private BuyLottoService buyLottoService;

    @Autowired
    private PrizeSettingDao prizeSettingDao;

    @Autowired
    private PrizeSettingRepo prizeSettingRepo;

    @Autowired
    private LottoTransactionDao lottoTransactionDao;

    @Autowired
    private LottoTransactionRepo lottoTransactionRepo;

    @Autowired
    private SumPrizeRepo sumPrizeRepo;

    @Autowired
    private LottoGroupTransactionDao lottoGroupTransactionDao;

    @Autowired
    private LottoGroupTransactionRepo groupTransactionRepo;

    @Autowired
    private YeekeeSumNumberRepo yeekeeSumNumberRepo;

    @Autowired
    private LottoClassRepository lottoClassRepo;

    @Autowired
    private YeekeeGroupPrizeRepo yeekeeGroupPrizeRepo;

    @Autowired
    CheckSecertDummyService checkSecertDummyService;

    @Autowired
    private LastListYeekeeRedisRepo lastListYeekeeRedisRepo;

    @Autowired
    private YeekeeResultService resultService;

    public void submitValueSumDummy(SendHttpModel<SubmitYeeKeeReq> req) {
        if (checkSecertDummyService.iskSecertDummy(req.getData(), req.getSigKey())) {
            submitValueSum(req.getData());
        }
    }

    public synchronized String submitValueSum(SubmitYeeKeeReq req) {
        String username = UserLoginUtil.getUsername();
        boolean isDummy = false;
        if ("NO LOGIN".equals(username) || StringUtils.isEmpty(username)) {
            isDummy = true;
            username = req.getUsername();
        }
        /**
         * validate value
         */
        if (req.getValue() == null || req.getValue() >= 100000 || req.getValue() < 0) {
            return "BAD_VALUE";
        }

        /**
         * check delay
         */
        if (isNotPassDelayBuy(username, req.getRound())) {
            return "IS_DELAY";
        }

        /**
         * Check installment
         */
        List<TimeSell> timeFind = timeSellRepo.findByLottoClassCode(req.getClassCode());
        LottoClass classCode = lottoClassRepo.findByLottoClassCode(req.getClassCode());
        Installment installment = checkInstallment(timeFind, classCode, req.getRound());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(installment.getTimeClose());

        Date thisDate = new Date();
        if (thisDate.after(calendar.getTime()) || thisDate.before(installment.getTimeOpen())) {
            return "BUY_TIME_OUT";
        }

        /**
         * save to yeekeeSubmitNumber
         */
        YeekeeSubmitNumber dataFind = yeekeeSubmitNumberRepo
                .findFirst1ByClassCodeAndInstallmentAndRoundNumberOrderByCreatedAtDesc(req.getClassCode(),
                        req.getInstallment(), req.getRound());
        if (dataFind == null) {
            dataFind = new YeekeeSubmitNumber();
        }
        int seq = dataFind.getSeqOrder() + 1;

        YeekeeSubmitNumber dataSave = new YeekeeSubmitNumber();
        dataSave.setRoundNumber(req.getRound());
        dataSave.setInstallment(req.getInstallment());
        dataSave.setCreatedBy(username);
        dataSave.setNumberSubmit(req.getValue());
        dataSave.setSeqOrder(seq);
        if (isDummy) {
            dataSave.setIsBot(true);
        }
        dataSave.setClassCode(req.getClassCode());
        yeekeeSubmitNumberRepo.save(dataSave);

        /**
         * put to Kafka steaming calculator
         */
        // yeeKeeSteamsCalculation.putInput();
        YeekeeSumNumber yeekeeSumNumber = yeekeeSumNumberRepo
                .findByInstallmentAndClassCodeAndRoundNumber(req.getInstallment(), req.getClassCode(), req.getRound());
        if (yeekeeSumNumber == null) {
            yeekeeSumNumber = new YeekeeSumNumber();
        }
        yeekeeSumNumber.setClassCode(req.getClassCode());
        yeekeeSumNumber.setInstallment(req.getInstallment());
        yeekeeSumNumber.setRoundNumber(req.getRound());
        yeekeeSumNumber.setSumNumber(yeekeeSumNumber.getSumNumber().add(BigDecimal.valueOf(req.getValue())));
        yeekeeSumNumber.setCreatedBy(username);
        yeekeeSumNumberRepo.save(yeekeeSumNumber);

        /**
         * if pass mark delay user
         */
        userBuyYeekeeRedisRepo.setUser(username, req.getRound());

        BigDecimal sumNumber = getSumSubmitNew(req);
        List<YeekeeResultRoundSeqRes> list = resultService.getYeekeeRoundSeqNew(req.getClassCode(),
                req.getInstallment(), req.getRound());
        List<YeeKeeNumberSubmitListRes> dataRes = getYeeKeeNumberSubmitListNew(req);
        lastListYeekeeRedisRepo.setLastList(req.getClassCode(), req.getInstallment(), req.getRound(), sumNumber,
                dataRes, list);

        return "SUCCESS";
    }

    public BigDecimal getSumSubmit(SubmitYeeKeeReq req) {

        YeekeeLastList lastRedis = lastListYeekeeRedisRepo.getLastList(req.getClassCode(), req.getInstallment(),
                req.getRound());
        if (lastRedis != null) {
            return lastRedis.getNumberSum();
        }
        return getSumSubmitNew(req);
    }

    public BigDecimal getSumSubmitNew(SubmitYeeKeeReq req) {
        YeekeeSumNumber yeekeeSumNumber = yeekeeSumNumberRepo
                .findByInstallmentAndClassCodeAndRoundNumber(req.getInstallment(), req.getClassCode(), req.getRound());
        // if (yeekeeSumNumber != null && yeekeeSumNumber.getStatus() == null) {
        // return null;
        // }
        if (yeekeeSumNumber == null) {
            yeekeeSumNumber = new YeekeeSumNumber();
        }
        return yeekeeSumNumber.getSumNumber();
    }

    private boolean isNotPassDelayBuy(String username, int round) {
        return userBuyYeekeeRedisRepo.hasUsername(username, round);
    }

    public List<YeeKeeNumberSubmitListRes> getYeeKeeNumberSubmitList(SubmitYeeKeeReq req) {
        YeekeeLastList lastRedis = lastListYeekeeRedisRepo.getLastList(req.getClassCode(), req.getInstallment(),
                req.getRound());
        if (lastRedis != null) {
            return lastRedis.getListLast20();
        }
        List<YeekeeSubmitNumber> listFind = yeekeeSubmitNumberRepo
                .findFirst20ByClassCodeAndInstallmentAndRoundNumberOrderByCreatedAtDesc(req.getClassCode(),
                        req.getInstallment(), req.getRound());
        List<YeeKeeNumberSubmitListRes> dataRes = new ArrayList<YeeKeeNumberSubmitListRes>();

        YeeKeeNumberSubmitListRes dataSet;
        for (YeekeeSubmitNumber entity : listFind) {
            dataSet = new YeeKeeNumberSubmitListRes();

            dataSet.setCreatedDate(ConvertDateUtils.formatDateToString(entity.getCreatedAt(),
                    ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
            dataSet.setNumberSubmit(String.valueOf(entity.getNumberSubmit()));
            dataSet.setOrder(entity.getSeqOrder());
            dataSet.setUsername(maskUsername(entity.getCreatedBy()));
            dataRes.add(dataSet);
        }

        BigDecimal sumNumber = getSumSubmitNew(req);
        List<YeekeeResultRoundSeqRes> list = resultService.getYeekeeRoundSeqNew(req.getClassCode(),
                req.getInstallment(), req.getRound());
        lastListYeekeeRedisRepo.setLastList(req.getClassCode(), req.getInstallment(), req.getRound(), sumNumber,
                dataRes, list);
        return dataRes;
    }

    public List<YeeKeeNumberSubmitListRes> getYeeKeeNumberSubmitListNew(SubmitYeeKeeReq req) {
        List<YeekeeSubmitNumber> listFind = yeekeeSubmitNumberRepo
                .findFirst20ByClassCodeAndInstallmentAndRoundNumberOrderByCreatedAtDesc(req.getClassCode(),
                        req.getInstallment(), req.getRound());
        List<YeeKeeNumberSubmitListRes> dataRes = new ArrayList<YeeKeeNumberSubmitListRes>();

        YeeKeeNumberSubmitListRes dataSet;
        for (YeekeeSubmitNumber entity : listFind) {
            dataSet = new YeeKeeNumberSubmitListRes();

            dataSet.setCreatedDate(ConvertDateUtils.formatDateToString(entity.getCreatedAt(),
                    ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
            dataSet.setNumberSubmit(String.valueOf(entity.getNumberSubmit()));
            dataSet.setOrder(entity.getSeqOrder());
            dataSet.setUsername(maskUsername(entity.getCreatedBy()));
            dataRes.add(dataSet);
        }
        return dataRes;
    }

    public String maskUsername(String username) {
        username = StringUtils.leftPad(username, 8, " ");
        int length = username.length();
        return username.substring(0, 2) + "****" + username.substring(5, length);
    }

    @Transactional
    public synchronized BuyLottoRes buyYeeKee(BuyLottoReq req, LottoClass lottoClass) {
        String username = UserLoginUtil.getUsername();
        BuyLottoRes dataRes = new BuyLottoRes();
        List<BuyDetail> errorList = new ArrayList<BuyDetail>();
        List<BuyDetail> successList = new ArrayList<BuyDetail>();
        dataRes.setError(errorList);
        dataRes.setSuccessData(successList);

        if (req.getRoundYeeKee() == 0) {
            dataRes.setStatus("NOT_HAS_ROUND");
            return dataRes;
        }
        List<TimeSell> timeFind = timeSellRepo.findByLottoClassCode(req.getLottoClassCode());

        Installment installment = checkInstallment(timeFind, lottoClass, req.getRoundYeeKee());
        Date thisDate = new Date();
        if (thisDate.after(installment.getTimeClose()) || thisDate.before(installment.getTimeOpen())) {
            dataRes.setStatus("BUY_TIME_OUT");
            return dataRes;
        }

        /**
         * 2. เช็ค max / min ตามโพย
         */
        for (PayNumber payNumber : req.getPayNumber()) {
            GroupMaxMinMap groupMaxMinMap = groupMaxMinMapRepo.findByLottoClassCodeAndMsdLottoKindCodeAndVipCode(
                    req.getLottoClassCode(), payNumber.getLottoKindCode(), req.getVipCode());
            if (groupMaxMinMap == null) {
                groupMaxMinMap = groupMaxMinMapRepo.findByLottoClassCodeAndMsdLottoKindCodeAndVipCode(
                        req.getLottoClassCode(), payNumber.getLottoKindCode(), ProjectConstant.STATUS.DEFAULT);
            }
            if (groupMaxMinMap == null) {
                dataRes.setStatus("NOT_HAS_CONFIG");
                return dataRes;
            }
            if (LottoConstant.LOTTO_KIND.DIGIT3_FRONT.equals(payNumber.getLottoKindCode())
                    || LottoConstant.LOTTO_KIND.DIGIT3_BOT.equals(payNumber.getLottoKindCode())) {
                dataRes.setStatus("NOT_HAS_CONFIG");
                return dataRes;
            }
            BuyDetail errorBuyDetail = new BuyDetail();
            List<LottoBuyDetailRes> errList = new ArrayList<BuyLottoRes.LottoBuyDetailRes>();
            errorBuyDetail.setLottoKindCode(groupMaxMinMap.getMsdLottoKindCode());

            for (LottoBuy lottoBuy : payNumber.getLottoBuy()) {
                LottoBuyDetailRes errSet = new LottoBuyDetailRes();
                errSet.setLottoNumber(lottoBuy.getLottoNumber());
                Boolean minCheck = lottoBuy.getPayCost().compareTo(groupMaxMinMap.getMinimumPerTrans()) < 0;
                if (minCheck) {
                    errSet.setStatus("OVER_MIN_PER_TRANS");
                    errList.add(errSet);
                }
                Boolean maxCheck = lottoBuy.getPayCost().compareTo(groupMaxMinMap.getMaximumPerTrans()) > 0;
                if (maxCheck) {
                    errSet.setStatus("OVER_MAX_PER_TRANS");
                    errList.add(errSet);
                }
            }
            if (errList.size() > 0) {
                errorBuyDetail.setLottoBuy(errList);
                errorList.add(errorBuyDetail);
            }
        }

        if (errorList.size() > 0) {
            dataRes.setStatus("BUY_FAIL");
            dataRes.setError(errorList);
            return dataRes;
        }

        for (PayNumber payNumber : req.getPayNumber()) {
            BuyDetail errorBuyDetail = new BuyDetail();
            List<LottoBuyDetailRes> errList = new ArrayList<BuyLottoRes.LottoBuyDetailRes>();
            errorBuyDetail.setLottoKindCode(payNumber.getLottoKindCode());

            GroupMaxMinMap groupMaxMinMap = groupMaxMinMapRepo.findByLottoClassCodeAndMsdLottoKindCodeAndVipCode(
                    req.getLottoClassCode(), payNumber.getLottoKindCode(), req.getVipCode());
            if (groupMaxMinMap == null) {
                groupMaxMinMap = groupMaxMinMapRepo.findByLottoClassCodeAndMsdLottoKindCodeAndVipCode(
                        req.getLottoClassCode(), payNumber.getLottoKindCode(), ProjectConstant.STATUS.DEFAULT);
            }
            if (groupMaxMinMap == null) {
                dataRes.setStatus("NOT_HAS_CONFIG");
                return dataRes;
            }
            for (LottoBuy numLottoBuy : payNumber.getLottoBuy()) {
                CheckCostUserRes sumUser = buyLottoDao.checkSumUserPerNumYeekee(req.getUsername(),
                        installment.getTimeOpen(), installment.getTimeClose(), numLottoBuy.getLottoNumber(),
                        payNumber.getLottoKindCode(), req.getLottoClassCode(), req.getRoundYeeKee());
                LottoBuyDetailRes errSet = new LottoBuyDetailRes();
                errSet.setLottoNumber(numLottoBuy.getLottoNumber());
                if (sumUser == null) {
                    sumUser = new CheckCostUserRes();
                    sumUser.setSumCost(BigDecimal.ZERO);
                }
                if (sumUser.getSumCost().add(numLottoBuy.getPayCost())
                        .compareTo(groupMaxMinMap.getMaximumPerUser()) > 0) {
                    errSet.setHasBalanceToBuy(groupMaxMinMap.getMaximumPerUser().subtract(sumUser.getSumCost()));
                    errSet.setStatus("OVER_MAX_PER_USER");
                    errList.add(errSet);
                }
            }
            if (errList.size() > 0) {
                errorBuyDetail.setLottoBuy(errList);
                errorList.add(errorBuyDetail);
            }
        }

        if (errorList.size() > 0) {
            dataRes.setStatus("BUY_FAIL");
            dataRes.setError(errorList);
            return dataRes;
        }

        if (errorList.size() > 0) {
            dataRes.setStatus("BUY_FAIL");
            dataRes.setError(errorList);
            return dataRes;
        }
        String installmentStr = webTimeSellService.getInstallmentStr(LottoConstant.TYPE_INSTALLMENT.HR24,
                installment.getTimeOpen(), 0);

        Set<String> listTop = new HashSet<String>();
        Set<String> list2Top = new HashSet<String>();
        Set<String> list2Bot = new HashSet<String>();
        req.getPayNumber().forEach(kind -> {
            kind.getLottoBuy().forEach(lottoBuy -> {
                switch (kind.getLottoKindCode()) {
                    case LOTTO_KIND.DIGIT3_TOP:
                        listTop.add(lottoBuy.getLottoNumber());
                        break;
                    case LOTTO_KIND.DIGIT3_SWAPPED:
                        listTop.add(lottoBuy.getLottoNumber());
                        break;
                    case LOTTO_KIND.DIGIT2_TOP:
                        list2Top.add(lottoBuy.getLottoNumber());
                        break;
                    case LOTTO_KIND.DIGIT2_BOT:
                        list2Bot.add(lottoBuy.getLottoNumber());
                        break;
                }
            });
        });

        // save Count
        List<YeekeeGroupPrize> listSaveCount = new ArrayList<YeekeeGroupPrize>();
        for (String number : listTop) {
            YeekeeGroupPrize sumPrizeNumber = yeekeeGroupPrizeRepo
                    .findFrist1ByClassCodeAndInstallmentAndKindCodeAndRoundYeekeeAndCreatedByAndLottoNumber(
                            req.getLottoClassCode(), LOTTO_KIND.DIGIT3_TOP, installmentStr, req.getRoundYeeKee(),
                            number, username);
            if (sumPrizeNumber == null) {
                sumPrizeNumber = new YeekeeGroupPrize();
            } else {
                sumPrizeNumber.setCountUser(sumPrizeNumber.getCountUser() + 1);
            }
            sumPrizeNumber.setClassCode(req.getLottoClassCode());
            sumPrizeNumber.setInstallment(installmentStr);
            sumPrizeNumber.setKindCode(LOTTO_KIND.DIGIT3_TOP);
            sumPrizeNumber.setCreatedBy(username);
            sumPrizeNumber.setLottoNumber(number);
            sumPrizeNumber.setRoundYeekee(req.getRoundYeeKee());
        }
        for (String number : list2Bot) {
            YeekeeGroupPrize sumPrizeNumber = yeekeeGroupPrizeRepo
                    .findFrist1ByClassCodeAndInstallmentAndKindCodeAndRoundYeekeeAndCreatedByAndLottoNumber(
                            req.getLottoClassCode(), LOTTO_KIND.DIGIT2_BOT, installmentStr, req.getRoundYeeKee(),
                            number, username);
            if (sumPrizeNumber == null) {
                sumPrizeNumber = new YeekeeGroupPrize();
            } else {
                sumPrizeNumber.setCountUser(sumPrizeNumber.getCountUser() + 1);
            }
            sumPrizeNumber.setClassCode(req.getLottoClassCode());
            sumPrizeNumber.setInstallment(installmentStr);
            sumPrizeNumber.setKindCode(LOTTO_KIND.DIGIT2_BOT);
            sumPrizeNumber.setCreatedBy(username);
            sumPrizeNumber.setLottoNumber(number);
            sumPrizeNumber.setRoundYeekee(req.getRoundYeeKee());
        }
        yeekeeGroupPrizeRepo.saveAll(listSaveCount);

        Set<String> top2Top3 = new HashSet<String>();
        for (String number : list2Bot) {
            for (int i = 0; i <= 9; i++) {
                top2Top3.add(String.valueOf(i) + number);
            }
        }

        List<YeekeeGroupPrize> listSaveCountNew = new ArrayList<YeekeeGroupPrize>();
        for (String number : listTop) {
            YeekeeGroupPrize sumPrizeNumber = yeekeeGroupPrizeRepo
                    .findFrist1ByClassCodeAndInstallmentAndKindCodeAndRoundYeekeeAndCreatedByAndLottoNumber(
                            req.getLottoClassCode(), LOTTO_KIND.DIGIT3_TOP, installmentStr, req.getRoundYeeKee(),
                            number, username);
            if (sumPrizeNumber == null) {
                sumPrizeNumber = new YeekeeGroupPrize();
            } else {
                sumPrizeNumber.setCountUser(sumPrizeNumber.getCountUser() + 1);
            }
            sumPrizeNumber.setClassCode(req.getLottoClassCode());
            sumPrizeNumber.setInstallment(installmentStr);
            sumPrizeNumber.setKindCode(LOTTO_KIND.DIGIT3_TOP);
            sumPrizeNumber.setCreatedBy(username);
            sumPrizeNumber.setLottoNumber(number);
            sumPrizeNumber.setRoundYeekee(req.getRoundYeeKee());
        }
        yeekeeGroupPrizeRepo.saveAll(listSaveCountNew);

        for (PayNumber payNumber : req.getPayNumber()) {
            for (LottoBuy lottoBuy : payNumber.getLottoBuy()) {
                YeekeeGroupPrize sumPrizeNumber = yeekeeGroupPrizeRepo
                        .findFirst1ByClassCodeAndKindCodeAndLottoNumberAndInstallmentAndRoundYeekee(
                                req.getLottoClassCode(), payNumber.getLottoKindCode(), lottoBuy.getLottoNumber(),
                                installmentStr, req.getRoundYeeKee());
                if (sumPrizeNumber == null) {
                    sumPrizeNumber = new YeekeeGroupPrize();
                }

                sumPrizeNumber.setClassCode(req.getLottoClassCode());
                sumPrizeNumber.setKindCode(payNumber.getLottoKindCode());
                sumPrizeNumber.setLottoNumber(lottoBuy.getLottoNumber());
                sumPrizeNumber.setInstallment(installmentStr);
                sumPrizeNumber.setRoundYeekee(req.getRoundYeeKee());
                BigDecimal prize = lottoBuy.getPayCost().multiply(lottoBuy.getPrize());
                if (StringUtils.isEmpty(lottoBuy.getRefLottoNumber())) {
                    BigDecimal sumPrizeCost = sumPrizeNumber.getSumPrize().add(prize);
                    sumPrizeNumber.setSumPrize(sumPrizeCost);
                }
                if (StringUtils.isEmpty(lottoBuy.getRefLottoNumber())) {
                    BigDecimal sumBet = sumPrizeNumber.getSumBet().add(lottoBuy.getPayCost());
                    sumPrizeNumber.setSumBet(sumBet);
                }
                sumPrizeNumber.setCreatedBy(username);
                // set count
                sumPrizeNumber.setCountUser(sumPrizeNumber.getCountUser());
                yeekeeGroupPrizeRepo.save(sumPrizeNumber);
            }
        }

        req.getPayNumber().forEach(kind -> {
            List<LottoBuy> listSet = new ArrayList<LottoBuy>();
            if (LOTTO_KIND.DIGIT3_SWAPPED.equals(kind.getLottoKindCode())) {
                kind.getLottoBuy().forEach(numBuy -> {
                    String groupSwappedCode = GenerateRandomString.generateUUID();
                    numBuy.setGroupSwappedCode(groupSwappedCode);
                    List<LottoBuy> listSwapped = buyLottoService.createListSwapped(numBuy);

                    numBuy.setGroupSwappedCode(null);
                    listSet.addAll(listSwapped);
                });
                kind.getLottoBuy().addAll(listSet);
            }
        });

        LoopPrizeRes loopCheck = loopPrize(req);
        if (loopCheck.getErrList().size() > 0) {
            dataRes.setStatus("BUY_FAIL");
            dataRes.setError(loopCheck.getErrList());
            return dataRes;
        } else {
            dataRes.setSuccessData(loopCheck.getSccList());
            dataRes.setError(null);
        }
        req.setPayNumber(loopCheck.getPayNumber());

        /**
         * save sum prize
         */

        for (PayNumber payNumber : req.getPayNumber()) {

            for (LottoBuy lottoBuy : payNumber.getLottoBuy()) {
                YeekeeGroupPrize sumPrizeNumber = yeekeeGroupPrizeRepo
                        .findFirst1ByClassCodeAndKindCodeAndLottoNumberAndInstallmentAndRoundYeekee(
                                req.getLottoClassCode(), payNumber.getLottoKindCode(), lottoBuy.getLottoNumber(),
                                installmentStr, req.getRoundYeeKee());
                if (sumPrizeNumber == null) {
                    sumPrizeNumber = new YeekeeGroupPrize();
                }
                Boolean hasUser = hasUser(req.getLottoClassCode(), installmentStr, req.getRoundYeeKee(),
                        payNumber.getLottoKindCode(), username, lottoBuy.getLottoNumber());

                Integer count = sumPrizeNumber.getCountUser();

                if (hasUser || StringUtils.isNotEmpty(lottoBuy.getRefLottoNumber())) {
                    sumPrizeNumber.setCountUser(count);
                } else {
                    sumPrizeNumber.setCountUser(count + 1);
                }

                sumPrizeNumber.setClassCode(req.getLottoClassCode());
                sumPrizeNumber.setKindCode(payNumber.getLottoKindCode());
                sumPrizeNumber.setLottoNumber(lottoBuy.getLottoNumber());
                sumPrizeNumber.setInstallment(installmentStr);
                sumPrizeNumber.setRoundYeekee(req.getRoundYeeKee());
                BigDecimal prize = lottoBuy.getPayCost().multiply(lottoBuy.getPrize());
                if (StringUtils.isEmpty(lottoBuy.getRefLottoNumber())) {
                    BigDecimal sumPrizeCost = sumPrizeNumber.getSumPrize().add(prize);
                    sumPrizeNumber.setSumPrize(sumPrizeCost);
                }
                if (StringUtils.isEmpty(lottoBuy.getRefLottoNumber())) {
                    BigDecimal sumBet = sumPrizeNumber.getSumBet().add(lottoBuy.getPayCost());
                    sumPrizeNumber.setSumBet(sumBet);
                }
                sumPrizeNumber.setCreatedBy(username);
                // set count
                sumPrizeNumber.setCountUser(sumPrizeNumber.getCountUser());
                yeekeeGroupPrizeRepo.save(sumPrizeNumber);
            }
        }
        System.out.println("end save sum prize");
        // };

        /**
         * save Group Transaction
         */
        Runnable saveGroupTransaction = () -> {
            BigDecimal sumPrize = BigDecimal.ZERO;
            for (PayNumber buy : req.getPayNumber()) {
                for (LottoBuy item : buy.getLottoBuy()) {
                    if (!(LOTTO_KIND.DIGIT3_SWAPPED.equals(buy.getLottoKindCode())
                            && StringUtils.isNotEmpty(item.getRefLottoNumber()))) {
                        sumPrize = sumPrize.add(item.getPayCost());
                    }
                }
            }
            LottoGroupTransaction groupTransaction = new LottoGroupTransaction();
            groupTransaction.setSumGroupBet(sumPrize);
            groupTransaction.setLottoGroupTransactionCode(generateGroupTransaction(lottoClass.getPrefixTransNumber(),
                    installment.getTimeClose(), req.getRoundYeeKee()));
            groupTransaction.setUsername(req.getUsername());
            groupTransaction.setLottoClassCode(req.getLottoClassCode());
            groupTransaction.setCreatedBy(username);
            groupTransaction.setStatus(ProjectConstant.STATUS.PENDING);
            groupTransaction.setInstallment(installmentStr);
            groupTransaction.setRoundYeekee(req.getRoundYeeKee());
            groupTransactionRepo.save(groupTransaction);

            /**
             * save Lotto Transaction | save Limit
             */
            List<LottoTransaction> listPaySave = new ArrayList<LottoTransaction>();
            for (PayNumber buy : req.getPayNumber()) {

                int count = 1;
                for (LottoBuy item : buy.getLottoBuy()) {
                    if (LOTTO_KIND.DIGIT3_SWAPPED.equals(buy.getLottoKindCode())
                            && StringUtils.isNotEmpty(item.getRefLottoNumber())) {
                        continue;
                    }
                    LottoTransaction tranSet = new LottoTransaction();
                    tranSet.setLottoTransactionCode(GenerateRandomString.generateUUID());
                    tranSet.setLottoGroupTransactionCode(groupTransaction.getLottoGroupTransactionCode());
                    tranSet.setUsername(req.getUsername());
                    tranSet.setLottoClassCode(req.getLottoClassCode());
                    tranSet.setLottoKindCode(buy.getLottoKindCode());
                    tranSet.setLottoNumber(item.getLottoNumber());
                    tranSet.setPayCost(item.getPayCost());
                    tranSet.setPrizeCost(item.getPrize());
                    tranSet.setCreatedBy(username);
                    tranSet.setStatus(ProjectConstant.STATUS.PENDING);
                    tranSet.setInstallment(installmentStr);
                    tranSet.setRoundYeekee(req.getRoundYeeKee());
                    tranSet.setPrizeCorrect(item.getPayCost().multiply(item.getPrize()));
                    tranSet.setCountSeq(count);
                    count++;
                    if (!ProjectConstant.STATUS.DEFAULT.equals(item.getSecondRiskCode()))
                        tranSet.setIsLimit(true);
                    listPaySave.add(tranSet);
                }
            }
            lottoTransactionRepo.saveAll(listPaySave);
            dataRes.setStatus(ProjectConstant.STATUS.SUCCESS);
            dataRes.setCode(groupTransaction.getLottoGroupTransactionCode());
        };

        List<Runnable> listRun = Arrays.asList(
                // saveSumPrize,
                saveGroupTransaction);
        listRun.parallelStream().forEach(runnable -> {
            runnable.run();
        });

        return dataRes;
    }

    @Transactional
    private boolean hasUser(String classCode, String installment, Integer round, String kindCode, String username,
            String numberBuy) {
        LottoTransaction trans = lottoTransactionDao.findHasUser(classCode, installment, round, kindCode, username,
                numberBuy);
        if (trans != null) {
            return true;
        }
        return false;
    }

    public Installment checkInstallment(List<TimeSell> timeFind, LottoClass dataFind, Integer round) {
        List<Installment> listTimeSet = webTimeSellService.getListInstallmentByType(dataFind.getTypeInstallment(),
                timeFind, false);
        return listTimeSet.get(round - 1);
    }

    private LoopPrizeRes loopPrize(BuyLottoReq req) {
        List<BuyDetail> warningRes = new ArrayList<BuyDetail>();
        List<BuyDetail> successRes = new ArrayList<BuyDetail>();
        LoopPrizeRes dataRes = new LoopPrizeRes(warningRes, successRes, req.getPayNumber());

        List<PrizeSetting> prizeList = prizeSettingRepo.findByClassCodeAndVipCode(req.getLottoClassCode(),
                req.getVipCode());

        if (prizeList.size() == 0) {
            prizeList = prizeSettingRepo.findByClassCodeAndVipCode(req.getLottoClassCode(),
                    ProjectConstant.STATUS.DEFAULT);
            if (prizeList.size() == 0) {
                BuyDetail errorBuyDetail = new BuyDetail();
                errorBuyDetail.setStatusKind("NOT_CONFIG");
                warningRes.add(errorBuyDetail);
                return dataRes;
            }
        }
        for (PayNumber payKind : req.getPayNumber()) {
            BuyDetail errorBuyDetail = new BuyDetail();
            BuyDetail successBuyDetail = new BuyDetail();
            errorBuyDetail.setLottoKindCode(payKind.getLottoKindCode());
            successBuyDetail.setLottoKindCode(payKind.getLottoKindCode());

            BigDecimal prize = getPrize(prizeList, payKind.getLottoKindCode());
            List<LottoBuyDetailRes> lottoBuyListErr = new ArrayList<LottoBuyDetailRes>();
            List<LottoBuyDetailRes> lottoBuyListScc = new ArrayList<LottoBuyDetailRes>();

            LottoBuyDetailRes lottoBuyErrSet = null;
            LottoBuyDetailRes lottoBuySccSet = null;
            for (LottoBuy lottoBuy : payKind.getLottoBuy()) {
                lottoBuy.setPrize(prize);
                if (!(LOTTO_KIND.DIGIT3_SWAPPED.equals(payKind.getLottoKindCode())
                        && StringUtils.isNotEmpty(lottoBuy.getRefLottoNumber()))) {
                    lottoBuySccSet = new LottoBuyDetailRes();
                    lottoBuySccSet.setLottoNumber(lottoBuy.getLottoNumber());
                    lottoBuySccSet.setStatus("SUCCESS");
                    lottoBuySccSet.setNewPrize(prize);
                    lottoBuySccSet.setOldPrize(prize);
                    lottoBuySccSet.setPayCost(lottoBuy.getPayCost());
                    lottoBuyListScc.add(lottoBuySccSet);
                }
            }
            successBuyDetail.setLottoBuy(lottoBuyListScc);
            successRes.add(successBuyDetail);

        }
        dataRes = new LoopPrizeRes(warningRes, successRes, req.getPayNumber());
        return dataRes;
    }

    private BigDecimal getPrize(List<PrizeSetting> prizeList, String kind) {
        BigDecimal prize = prizeList.stream().filter(item -> item.getMsdLottoKindCode().equals(kind)).findFirst()
                .orElse(new PrizeSetting()).getPrize();
        return prize;
    }

    public String generateGroupTransaction(String prefixCode, Date timeClose, Integer installmentCount) {
        String installmentStr = ConvertDateUtils.formatDateToString(timeClose, ConvertDateUtils.YYMMDD,
                ConvertDateUtils.LOCAL_EN);

        String running = String.valueOf(lottoGroupTransactionDao.getNextIndent());
        String runningPad = StringUtils.leftPad(running, 6, "0");
        runningPad = runningPad.substring(runningPad.length() - 6, runningPad.length());

        String installmentCountStr = StringUtils.leftPad(Integer.toString(installmentCount), 2, "0");
        return prefixCode + installmentStr + installmentCountStr + "-" + runningPad;
    }

}