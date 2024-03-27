package coffee.buy.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import coffee.buy.dao.BuyLottoDao;
import coffee.buy.vo.req.BuyLottoReq;
import coffee.buy.vo.req.BuyLottoReq.LottoBuy;
import coffee.buy.vo.req.BuyLottoReq.PayNumber;
import coffee.buy.vo.res.BuyLottoRes;
import coffee.buy.vo.res.BuyLottoRes.BuyDetail;
import coffee.buy.vo.res.BuyLottoRes.LottoBuyDetailRes;
import coffee.buy.vo.res.CheckCostUserRes;
import coffee.buy.vo.res.Installment;
import coffee.buy.vo.res.LoopPrizeRes;
import coffee.dashboard.service.DashboardService;
import coffee.lottoconfig.service.CloseNumberService;
import coffee.lottoconfig.service.LimitNumberService;
import coffee.model.GroupMaxMinMap;
import coffee.model.LimitNumber;
import coffee.model.LottoClass;
import coffee.model.LottoGroup;
import coffee.model.LottoGroupDtl;
import coffee.model.LottoGroupTransaction;
import coffee.model.LottoTransaction;
import coffee.model.PrizeSetting;
import coffee.model.SumBetGroup;
import coffee.model.SumPrize;
import coffee.model.TimeSell;
import coffee.repo.dao.LottoGroupDao;
import coffee.repo.dao.LottoGroupDtlDao;
import coffee.repo.dao.LottoGroupTransactionDao;
import coffee.repo.dao.PrizeSettingDao;
import coffee.repo.dao.SumPrizeDao;
import coffee.repo.jpa.GroupMaxMinMapRepo;
import coffee.repo.jpa.LimitNumberRepo;
import coffee.repo.jpa.LottoClassRepository;
import coffee.repo.jpa.LottoGroupTransactionRepo;
import coffee.repo.jpa.LottoTransactionRepo;
import coffee.repo.jpa.MsdLottoKindRepository;
import coffee.repo.jpa.SumBetGroupRepo;
import coffee.repo.jpa.SumPrizeRepo;
import coffee.repo.jpa.TimeSellRepo;
import coffee.web.service.WebTimeSellService;
import framework.constant.LottoConstant;
import framework.constant.LottoConstant.LOTTO_KIND;
import framework.constant.ProjectConstant;
import framework.utils.ConvertDateUtils;
import framework.utils.GenerateRandomString;
import framework.utils.UserLoginUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BuyLottoService {

	@Autowired
	private LottoGroupTransactionRepo groupTransactionRepo;

	@Autowired
	private LottoTransactionRepo lottoTransactionRepo;

	@Autowired
	private BuyLottoDao buyLottoDao;

	@Autowired
	private TimeSellRepo timeSellRepo;

	@Autowired
	private LottoClassRepository lottoClassRepo;

	@Autowired
	private WebTimeSellService webTimeSellService;

	@Autowired
	private GroupMaxMinMapRepo groupMaxMinMapRepo;

	@Autowired
	private SumPrizeRepo sumPrizeRepo;

	@Autowired
	private PrizeSettingDao prizeSettingDao;

	@Autowired
	private LottoGroupDao lottoGroupDao;

	@Autowired
	private LottoGroupDtlDao lottoGroupDtlDao;

	@Autowired
	private SumBetGroupRepo sumBetGroupRepo;

	@Autowired
	private LimitNumberRepo limitNumberRepo;

	@Autowired
	private LimitNumberService limitNumberService;

	@Autowired
	private SumPrizeDao sumPrizeDao;

	@Autowired
	private MsdLottoKindRepository msdLottoKindRepo;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private LottoGroupTransactionDao lottoGroupTransactionDao;

	@Autowired
	private LottoGroupTransactionRepo lottoGroupTransactionRepo;

	@Autowired
	private DashboardService dashboardService;

	@Autowired
	private DataSource dataSource;

	@Autowired
	private PlatformTransactionManager platformTransactionManager;

	@Autowired
	private CloseNumberService closeNumberService;

	@Autowired
	private BuyYeekeeService buyLottoService;

	public BuyLottoRes buyLotto(BuyLottoReq req) {
		LottoClass dataFind = lottoClassRepo.findByLottoClassCode(req.getLottoClassCode());
		if ("HIDE".equals(dataFind.getViewStatus())) {
			BuyLottoRes dataRes = new BuyLottoRes();
			dataRes.setStatus("BUY_TIME_OUT");
			return dataRes;
		}
		// YEEKEE
		if (LottoConstant.CATEGORY.YEEKEE.equals(dataFind.getLottoCategoryCode())) {
			return buyLottoService.buyYeeKee(req, dataFind);
		} else {
			return buyLottoGovStocks(req, dataFind);
		}
	}

	@Transactional
	public synchronized BuyLottoRes buyLottoGovStocks(BuyLottoReq req, LottoClass dataFind) {
		String username = UserLoginUtil.getUsername();
		BuyLottoRes dataRes = new BuyLottoRes();
		List<BuyDetail> errorList = new ArrayList<BuyLottoRes.BuyDetail>();
		Installment installment = checkInstallment(req.getLottoClassCode(), null, dataFind);

		/**
		 * 1. ไม่อยู่ในงวดนั้นๆ
		 */
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

		/**
		 * 3. ซื้อ MAX MIN ไม่ตรงตามเงื่อนไข return ไปบอก
		 */
		if (errorList.size() > 0) {
			dataRes.setStatus("BUY_FAIL");
			dataRes.setError(errorList);
			return dataRes;
		}

		/**
		 * 4. เช็ค Max Per Username
		 */
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
				CheckCostUserRes sumUser = buyLottoDao.checkSumUserPerNum(req.getUsername(), installment.getTimeOpen(),
						installment.getTimeClose(), numLottoBuy.getLottoNumber(), payNumber.getLottoKindCode(),
						req.getLottoClassCode());
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

		/**
		 * 5. ซื้อ MAX MIN ไม่ตรงตามเงื่อนไข return ไปบอก
		 */
		if (errorList.size() > 0) {
			dataRes.setStatus("BUY_FAIL");
			dataRes.setError(errorList);
			return dataRes;
		}

		/**
		 * loop เช็ค lotto number เช็คเลขโต็ด และและเพิ่มเลข Ref
		 */
		req.getPayNumber().forEach(kind -> {
			List<LottoBuy> listSet = new ArrayList<LottoBuy>();
			if (LOTTO_KIND.DIGIT3_SWAPPED.equals(kind.getLottoKindCode())) {
				kind.getLottoBuy().forEach(numBuy -> {
					String groupSwappedCode = GenerateRandomString.generateUUID();
					numBuy.setGroupSwappedCode(groupSwappedCode);
					List<LottoBuy> listSwapped = createListSwapped(numBuy);
					listSet.addAll(listSwapped);
				});
				kind.getLottoBuy().addAll(listSet);
			}
		});

		List<BuyDetail> closeNumbersErr = closeNumberService.checkCloseNumber(req);
		if (closeNumbersErr.size() > 0) {
			dataRes.setStatus("BUY_FAIL");
			dataRes.setError(closeNumbersErr);
			return dataRes;
		}

		/**
		 * loop เช็ค rate + max risk
		 */
		LoopPrizeRes loopCheck = loopPrize(req);
		if (loopCheck.getErrList().size() > 0) {
			dataRes.setStatus("BUY_FAIL");
			dataRes.setError(loopCheck.getErrList());
			return dataRes;
		} else {
			dataRes.setSuccessData(loopCheck.getSccList());
		}
		req.setPayNumber(loopCheck.getPayNumber());

		String installmentStr = webTimeSellService.getInstallmentStr(dataFind.getTypeInstallment(),
				installment.getTimeClose(), installment.getLengthTimeSell());

		/**
		 * save sum prize
		 */
		Runnable saveSumPrize = () -> {
			// loop type
			for (PayNumber payNumber : req.getPayNumber()) {
				// loop number
				for (LottoBuy lottoBuy : payNumber.getLottoBuy()) {
					SumPrize sumPrizeNumber = sumPrizeRepo.findByLottoNumberAndMsdLottoKindCodeAndLottoClassCode(
							lottoBuy.getLottoNumber(), payNumber.getLottoKindCode(), req.getLottoClassCode());
					if (sumPrizeNumber == null) {
						sumPrizeNumber = new SumPrize();
					}
					sumPrizeNumber.setSumPrizeCode(GenerateRandomString.generateUUID());
					sumPrizeNumber.setLottoClassCode(req.getLottoClassCode());
					sumPrizeNumber.setMsdLottoKindCode(payNumber.getLottoKindCode());
					sumPrizeNumber.setLottoNumber(lottoBuy.getLottoNumber());
					BigDecimal prize = lottoBuy.getPayCost().multiply(lottoBuy.getPrize());
					BigDecimal sumPrizeCost = sumPrizeNumber.getSumPrizeCost().add(prize);

					// .add(commission).setScale(2, BigDecimal.ROUND_CEILING);
					sumPrizeNumber.setSumPrizeCost(sumPrizeCost);

					BigDecimal sumPayCost = sumPrizeNumber.getSumPayCost().add(lottoBuy.getPayCost());
					if (LOTTO_KIND.DIGIT3_SWAPPED.equals(payNumber.getLottoKindCode())
							&& StringUtils.isNotEmpty(lottoBuy.getRefLottoNumber())) {
						sumPayCost = sumPrizeNumber.getSumPayCost();
					}
					sumPrizeNumber.setSumPayCost(sumPayCost);

					BigDecimal justSumPrize = sumPrizeNumber.getJustSumPrize();
					justSumPrize = justSumPrize.add(prize);
					sumPrizeNumber.setJustSumPrize(justSumPrize);

					sumPrizeNumber.setCreatedBy(username);
					sumPrizeNumber.setGroupSwappedCode(lottoBuy.getGroupSwappedCode());
					sumPrizeRepo.save(sumPrizeNumber);

					/**
					 * swapped เซ็ต sumprize เข้า สามตัวบน
					 */
					if (LOTTO_KIND.DIGIT3_SWAPPED.equals(payNumber.getLottoKindCode())) {
						SumPrize sumPrizeNumber3Top = sumPrizeRepo
								.findByLottoNumberAndMsdLottoKindCodeAndLottoClassCode(lottoBuy.getLottoNumber(),
										LOTTO_KIND.DIGIT3_TOP, req.getLottoClassCode());
						if (sumPrizeNumber3Top == null) {
							sumPrizeNumber3Top = new SumPrize();
						}
						sumPrizeNumber3Top.setSumPrizeCode(GenerateRandomString.generateUUID());
						sumPrizeNumber3Top.setLottoClassCode(req.getLottoClassCode());
						sumPrizeNumber3Top.setMsdLottoKindCode(LOTTO_KIND.DIGIT3_TOP);
						sumPrizeNumber3Top.setLottoNumber(lottoBuy.getLottoNumber());
						/*
						 * sum prize cost
						 */
						BigDecimal prize3Top = lottoBuy.getPayCost().multiply(lottoBuy.getPrize());
						BigDecimal sumPrizeCost3Top = sumPrizeNumber3Top.getSumPrizeCost().add(prize3Top);
						sumPrizeNumber3Top.setSumPrizeCost(sumPrizeCost3Top);
						sumPrizeNumber3Top.setJustSumPrize(sumPrizeNumber3Top.getJustSumPrize());
						sumPrizeNumber3Top.setSumPayCost(sumPrizeNumber.getSumPayCost());
						sumPrizeNumber3Top.setCreatedBy(username);
						sumPrizeRepo.save(sumPrizeNumber3Top);
					}
				}
			}
		};

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
			groupTransaction.setLottoGroupTransactionCode(generateGroupTransaction(dataFind.getPrefixTransNumber(),
					installment.getTimeClose(), installment.getCount()));
			groupTransaction.setUsername(req.getUsername());
			groupTransaction.setLottoClassCode(req.getLottoClassCode());
			groupTransaction.setCreatedBy(username);
			groupTransaction.setStatus(ProjectConstant.STATUS.PENDING);
			groupTransaction.setInstallment(installmentStr);
			groupTransactionRepo.save(groupTransaction);

			/**
			 * save Lotto Transaction | save Limit
			 */
			List<LottoTransaction> listPaySave = new ArrayList<LottoTransaction>();
			for (PayNumber buy : req.getPayNumber()) {

				int count = 1;
				for (LottoBuy item : buy.getLottoBuy()) {
					saveLimit(req.getLottoClassCode(), buy.getLottoKindCode(), item.getLottoNumber(), item.getPrize(),
							req.getUsername(), item.getSecondRiskCode(), item.getLottoGroupDtlCode(),
							item.getTierLevel());

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

		/**
		 * Save Sum Bet Group
		 */
		Runnable saveGroupBet = () -> {
			for (PayNumber buy : req.getPayNumber()) {
				SumBetGroup sumBetGroup = sumBetGroupRepo.findByLottoGroupCodeAndLottoClassCode(buy.getLottoGroupCode(),
						req.getLottoClassCode());
				if (sumBetGroup == null) {
					sumBetGroup = new SumBetGroup();
					sumBetGroup.setCreatedBy(req.getUsername());
				} else {
					sumBetGroup.setUpdatedBy(req.getUsername());
					sumBetGroup.setUpdatedAt(new Date());
				}
				sumBetGroup.setLottoClassCode(req.getLottoClassCode());
				sumBetGroup.setLottoGroupCode(buy.getLottoGroupCode());
				for (LottoBuy item : buy.getLottoBuy()) {
					sumBetGroup.setSumBet(sumBetGroup.getSumBet().add(item.getPayCost()));
				}
				sumBetGroup.setUpdatedAt(new Date());
				sumBetGroup.setUpdatedBy(req.getUsername());
				sumBetGroupRepo.save(sumBetGroup);
			}
		};

		List<Runnable> listRun = Arrays.asList(saveSumPrize, saveGroupTransaction, saveGroupBet);
		listRun.parallelStream().forEach(runnable -> {
			runnable.run();
		});

		/**
		 * บันทึกสำเร็จ commit transaction
		 */
		try (Connection con = dataSource.getConnection()) {
			con.commit();
		} catch (SQLException e) {
			// con.rollback();
			log.error("commit transaction but lotto error");
		}
		DefaultTransactionDefinition paramTransactionDefinition = new DefaultTransactionDefinition();
		TransactionStatus status = platformTransactionManager.getTransaction(paramTransactionDefinition);
		try {
			platformTransactionManager.commit(status);
		} catch (Exception e) {
			// platformTransactionManager.rollback(status);
		}

		/**
		 * Save SUCCESS return group trans-code
		 */

		/**
		 * ==> Find Socket Dashboard <==
		 */
		dashboardService.sendSocket(req.getLottoClassCode());
		/**
		 * ==> --- End Socket Dashboard --- <==
		 */

		/**
		 * ==> Find Socket Sum Dashboard <==
		 */
		dashboardService.sendSocketSum(req.getLottoClassCode());
		/**
		 * ==> --- End Socket Sum Dashboard --- <==
		 */

		limitNumberService.sendSocket(req.getLottoClassCode());

		return dataRes;
	}

	private String generateGroupTransaction(String prefixCode, Date timeClose, Integer installmentCount) {
		String installmentStr = ConvertDateUtils.formatDateToString(timeClose, ConvertDateUtils.YYMMDD,
				ConvertDateUtils.LOCAL_EN);

		String running = String.valueOf(lottoGroupTransactionDao.getNextIndent());
		String runningPad = StringUtils.leftPad(running, 6, "0");
		runningPad = runningPad.substring(runningPad.length() - 6, runningPad.length());

		String installmentCountStr = StringUtils.leftPad(Integer.toString(installmentCount), 2, "0");
		return prefixCode + installmentStr + installmentCountStr + "-" + runningPad;
	}

	// default to limit
	private void saveLimit(String lottoClass, String lottoKind, String number, BigDecimal prize, String username,
			String secondRiskCode, String lottoGroupDtlCode, int level) {
		LimitNumber limitNumberManual = limitNumberRepo
				.findAllByMsdLottoKindCodeAndLottoClassCodeAndLottoNumberAndIsManualAndEnable(lottoKind, lottoClass,
						number, true, true);
		if (limitNumberManual != null) {
			limitNumberManual.setLottoGroupDtlCode(lottoGroupDtlCode);
			limitNumberRepo.save(limitNumberManual);
			return;
		}
		LimitNumber limitNumber = limitNumberRepo
				.findByMsdLottoKindCodeAndLottoClassCodeAndLottoNumberAndIsManual(lottoKind, lottoClass, number, false);
		if (1 == level) {
			if (limitNumber != null) {
				limitNumberRepo.deleteById(limitNumber.getLimitNumberId());
			}
			return;
		}
		if (limitNumber == null) {
			limitNumber = new LimitNumber();
			limitNumber.setLimitNumberCode(GenerateRandomString.generateUUID());
			limitNumber.setCreatedBy(username);
			limitNumber.setIsManual(false);
			limitNumber.setEnable(true);
			limitNumber.setLottoClassCode(lottoClass);
			limitNumber.setLottoNumber(number);
			limitNumber.setLottoPrice(prize);
			limitNumber.setMsdLottoKindCode(lottoKind);
			limitNumber.setLottoGroupDtlCode(lottoGroupDtlCode);
			limitNumberRepo.save(limitNumber);
		} else {
			limitNumber.setUpdatedBy(username);
			limitNumber.setUpdatedAt(new Date());
			limitNumber.setIsManual(false);
			limitNumber.setEnable(true);
			limitNumber.setLottoClassCode(lottoClass);
			limitNumber.setLottoNumber(number);
			limitNumber.setLottoPrice(prize);
			limitNumber.setMsdLottoKindCode(lottoKind);
			limitNumber.setLottoGroupDtlCode(lottoGroupDtlCode);
			limitNumberRepo.save(limitNumber);
		}
	}

	public Installment checkInstallment(String classCode, List<TimeSell> timeFind, LottoClass dataFind) {
		Installment dataRes = new Installment();
		if (dataFind == null) {
			dataFind = lottoClassRepo.findByLottoClassCode(classCode);
		}
		if (timeFind == null) {
			timeFind = timeSellRepo.findByLottoClassCode(classCode);
		}

		List<Installment> listTimeSet = webTimeSellService.getListInstallmentByType(dataFind.getTypeInstallment(),
				timeFind, dataFind.getIgnoreWeekly());

		Date thisDate = new Date();
		Installment dataInsRes = null;
		Integer count = 1;
		for (Installment timeSet : listTimeSet) {
			timeSet.setCount(count);
			if (thisDate.after(timeSet.getTimeOpen()) && thisDate.before(timeSet.getTimeClose())) {
				dataInsRes = timeSet;
			}
			count++;
		}
		if (dataInsRes != null) {
			return dataInsRes;
		}

		if (dataRes.getTimeOpen() == null) {
			for (Installment timeSet : listTimeSet) {
				if (thisDate.after(timeSet.getTimeClose())) {
					continue;
				}
				if (thisDate.before(timeSet.getTimeOpen())) {
					return timeSet;
				}
			}
		}

		// ถ้าเป็นหลังจบเวลาขายสุดท้าย
		if (LottoConstant.TYPE_INSTALLMENT.MONTHLY.equals(dataFind.getTypeInstallment())) {
			listTimeSet.clear();
			listTimeSet.addAll(webTimeSellService.setDateMonthly(timeFind));
		} else if (LottoConstant.TYPE_INSTALLMENT.DAILY.equals(dataFind.getTypeInstallment())) {
			// timeFind.forEach((item) -> {
			// Installment timeSet = new Installment();
			// timeSet.setTimeOpen(webTimeSellService.setDateTimeAfter(item.getTimeOpen()));
			// timeSet.setTimeClose(webTimeSellService.setDateTimeAfter(item.getTimeClose()));
			// listTimeSet.add(timeSet);
			// });
			listTimeSet.addAll(webTimeSellService.getListInstallmentByType(LottoConstant.TYPE_INSTALLMENT.DAILY,
					timeFind, dataFind.getIgnoreWeekly()));
		} else if (LottoConstant.TYPE_INSTALLMENT.HR24.equals(dataFind.getTypeInstallment())) {
			int round = 0;
			int lengthTimeSell = timeFind.size();
			for (TimeSell item : timeFind) {
				Date timeClose = webTimeSellService.setDateTime(item.getTimeClose());
				/**
				 * เช็คคล่อมวัน วันใหม่ เวลาใหม่
				 */
				Date timeCloseRoundBefore;
				if (round == 0) {
					timeCloseRoundBefore = webTimeSellService.setDateTime(timeFind.get(round).getTimeOpen());
					timeFind.get(round)
							.setTimeClose(webTimeSellService.setDateTime(timeFind.get(round).getTimeClose()));
				} else {
					timeCloseRoundBefore = timeFind.get(round - 1).getTimeClose();
					timeFind.get(round - 1).setTimeClose(timeCloseRoundBefore);
				}
				/**
				 * ถ้าเป็นวันใหม่ ให้ + วัน 1;
				 */
				if (!timeCloseRoundBefore.before(timeClose)) {
					timeClose = webTimeSellService.setDateTime(item.getTimeClose(), 1);
					timeFind.get(round).setTimeClose(timeClose);
				}
				round++;
				Installment timeSet = new Installment();
				timeSet.setLengthTimeSell(lengthTimeSell);
				timeSet.setCount(round);
				timeSet.setTimeOpen(webTimeSellService.setDateTime(item.getTimeOpen()));
				timeSet.setTimeClose(webTimeSellService.setDateTime(timeClose));
				timeSet.setTimeOpenStr(ConvertDateUtils.formatDateToString(timeSet.getTimeOpen(),
						ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
				timeSet.setTimeCloseStr(ConvertDateUtils.formatDateToString(timeSet.getTimeClose(),
						ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
				listTimeSet.add(timeSet);
			}
			return listTimeSet.get(listTimeSet.size() - 1);
		}

		int lengthTimeSell = timeFind.size();
		for (Installment timeSet : listTimeSet) {
			timeSet.setLengthTimeSell(lengthTimeSell);
			if (thisDate.after(timeSet.getTimeOpen()) && thisDate.before(timeSet.getTimeClose())) {
				dataInsRes = timeSet;
			}
		}
		if (dataInsRes != null) {
			return dataInsRes;
		}

		if (dataRes.getTimeOpen() == null) {
			for (Installment timeSet : listTimeSet) {
				if (thisDate.after(timeSet.getTimeClose())) {
					continue;
				}
				if (thisDate.before(timeSet.getTimeOpen())) {
					return timeSet;
				}
			}
		}
		return dataRes;
	}

	public List<LottoBuy> createListSwapped(LottoBuy req) {
		String lottoNumber = req.getLottoNumber();
		Set<String> mapNum = new HashSet<String>();

		mapNum.add(lottoNumber);
		mapNum.add("" + lottoNumber.charAt(0) + lottoNumber.charAt(2) + lottoNumber.charAt(1));
		mapNum.add("" + lottoNumber.charAt(1) + lottoNumber.charAt(0) + lottoNumber.charAt(2));
		mapNum.add("" + lottoNumber.charAt(1) + lottoNumber.charAt(2) + lottoNumber.charAt(0));
		mapNum.add("" + lottoNumber.charAt(2) + lottoNumber.charAt(1) + lottoNumber.charAt(0));
		mapNum.add("" + lottoNumber.charAt(2) + lottoNumber.charAt(0) + lottoNumber.charAt(1));

		List<String> result3 = new ArrayList<String>(mapNum);
		Collections.sort(result3);
		List<LottoBuy> dataRes = new ArrayList<LottoBuy>();
		LottoBuy dataSet;
		for (String numSwapped : result3) {
			if (!req.getLottoNumber().equals(numSwapped)) {
				dataSet = new LottoBuy();
				BeanUtils.copyProperties(req, dataSet);
				dataSet.setLottoNumber(numSwapped);
				dataSet.setRefLottoNumber(req.getLottoNumber());
				dataRes.add(dataSet);
			}
		}

		return dataRes;
	}

	/**
	 * วนเช็คเลขอั้นหลังจากเช็ค Validateสูงสุด/ต่ำสุดต่อโพย|user เวลาซื้อขายแล้ว
	 */
	private LoopPrizeRes loopPrize(BuyLottoReq req) {
		List<BuyDetail> warningRes = new ArrayList<BuyDetail>();
		List<BuyDetail> successRes = new ArrayList<BuyDetail>();
		LoopPrizeRes dataRes = new LoopPrizeRes(warningRes, successRes, req.getPayNumber());

		Map<String, SumBetGroup> groupSumBetGroup = new HashMap<String, SumBetGroup>();
		/**
		 * loop เลขที่ซื้อ
		 */
		// loop Kind
		for (PayNumber payKind : req.getPayNumber()) {
			BuyDetail errorBuyDetail = new BuyDetail();
			BuyDetail successBuyDetail = new BuyDetail();
			errorBuyDetail.setLottoKindCode(payKind.getLottoKindCode());
			successBuyDetail.setLottoKindCode(payKind.getLottoKindCode());

			// Get Rate Prize This Kind
			/**
			 * 2.1/2.2 Get Rate Prize (Per VIP)
			 */
			List<PrizeSetting> prizeList = prizeSettingDao.getPrizeListSettingByClassCode(req.getLottoClassCode(),
					req.getVipCode(), payKind.getLottoKindCode());
			if (prizeList.size() == 0) {
				prizeList = prizeSettingDao.getPrizeListSettingByClassCode(req.getLottoClassCode(),
						ProjectConstant.STATUS.DEFAULT, payKind.getLottoKindCode());
				if (prizeList.size() == 0) {
					errorBuyDetail.setStatusKind("NOT_CONFIG");
					warningRes.add(errorBuyDetail);
					continue;
				}
			}
			List<LottoBuyDetailRes> lottoBuyListErr = new ArrayList<LottoBuyDetailRes>();
			List<LottoBuyDetailRes> lottoBuyListScc = new ArrayList<LottoBuyDetailRes>();

			List<LottoGroupDtl> listGroupDtl = lottoGroupDtlDao.getGroupByLottoKind(payKind.getLottoKindCode(),
					req.getLottoClassCode());
			List<LottoGroupDtl> listGroupDtlBU = copyListDtl(listGroupDtl);

			LottoGroup lottoGroup = lottoGroupDao.getGroupByLottoKind(payKind.getLottoKindCode(),
					req.getLottoClassCode());
			LottoBuyDetailRes lottoBuyErrSet = null;
			LottoBuyDetailRes lottoBuySccSet = null;

			if (lottoGroup == null) {
				lottoBuyErrSet = new LottoBuyDetailRes();
				lottoBuyErrSet.setLottoNumber(payKind.getLottoKindCode());
				lottoBuyErrSet.setStatus("NOT_CONFIG_GROUP");
				lottoBuyListErr.add(lottoBuyErrSet);
				continue;
			}
			SumBetGroup sumBetGroup = groupSumBetGroup.get(lottoGroup.getLottoGroupCode());
			if (sumBetGroup == null) {
				sumBetGroup = sumBetGroupRepo.findByLottoGroupCodeAndLottoClassCode(lottoGroup.getLottoGroupCode(),
						req.getLottoClassCode());
				groupSumBetGroup.put(lottoGroup.getLottoGroupCode(), sumBetGroup);
			}

			payKind.setLottoGroupCode(lottoGroup.getLottoGroupCode());
			BigDecimal earningPercent = BigDecimal.valueOf(lottoGroup.getGroupEarningsPercent());
			BigDecimal sumBet = sumBetGroup == null ? BigDecimal.ZERO : sumBetGroup.getSumBet();
			BigDecimal maxRisk = lottoGroup.getGroupMaxClose();
			BigDecimal maxClose = lottoGroup.getGroupMaxClose();
			Integer tierLevel = null;

			// loop Number
			for (LottoBuy lottoBuy : payKind.getLottoBuy()) {

				/**
				 * 1. Get Sum Bet/Earning Percent/Max Risk และเช็ค Sum Bet + Earning Percent >
				 * Max Risk
				 */
				SumPrize sumPrize = sumPrizeRepo.findByLottoNumberAndMsdLottoKindCodeAndLottoClassCode(
						lottoBuy.getLottoNumber(), payKind.getLottoKindCode(), req.getLottoClassCode());
				if (sumPrize == null) {
					sumPrize = new SumPrize();
				}

				LimitNumber limitNumber = limitNumberRepo
						.findAllByMsdLottoKindCodeAndLottoClassCodeAndLottoNumberAndIsManualAndEnable(
								payKind.getLottoKindCode(), req.getLottoClassCode(), lottoBuy.getLottoNumber(), true,
								true);

				listGroupDtl = copyListDtl(listGroupDtlBU);
				if (limitNumber != null) {
					/*
					 * Manual Limit Prize
					 */
					listGroupDtl = generateListDtlManual(listGroupDtl, limitNumber.getLottoGroupDtlCodeManual());
				}
				if (!(sumBet.subtract(sumBet.multiply(earningPercent.divide(BigDecimal.valueOf(100))))
						.compareTo(maxRisk) < 0)) {
					maxClose = sumBet.subtract(sumBet.multiply(earningPercent.divide(BigDecimal.valueOf(100))));
				}

				// comment old logic
				// if
				// (sumBet.subtract(sumBet.multiply(earningPercent.divide(BigDecimal.valueOf(100))))
				// .compareTo(maxRisk) < 0) {
				// Use Max Risk
				/**
				 * 3.1 Get This Rate
				 */
				BigDecimal currentPrize = null;
				BigDecimal secondPrize = null;
				BigDecimal prizeCost;
				String lottoGroupDtlCode = null;

				LimitNumber limitNumberAuto = limitNumberRepo
						.findByMsdLottoKindCodeAndLottoClassCodeAndLottoNumberAndIsManual(payKind.getLottoKindCode(),
								req.getLottoClassCode(), lottoBuy.getLottoNumber(), false);
				if (limitNumberAuto != null) {
					lottoGroupDtlCode = limitNumberAuto.getLottoGroupDtlCode();
					currentPrize = findFromGroupDtl(prizeList, limitNumberAuto.getLottoGroupDtlCode());
				} else {
					BigDecimal sumPercent = BigDecimal.ZERO;
					for (LottoGroupDtl riskRate : listGroupDtl) {
						if (riskRate.getPercentForLimit() == 0) {
							continue;
						}
						sumPercent = sumPercent.add(BigDecimal.valueOf(riskRate.getPercentForLimit()));
						BigDecimal thisPoint = maxRisk.multiply(sumPercent.divide(BigDecimal.valueOf(100)));
						if ((sumPrize == null ? BigDecimal.ZERO : sumPrize.getSumPrizeCost())
								.compareTo(thisPoint) <= 0) {
							lottoGroupDtlCode = riskRate.getLottoGroupDtlCode();
							currentPrize = findFromGroupDtl(prizeList, riskRate.getLottoGroupDtlCode());
							tierLevel = riskRate.getTierLevel();
							break;
						}
					}
				}

				prizeCost = currentPrize.multiply(lottoBuy.getPayCost()).add(sumPrize.getSumPrizeCost());
				BigDecimal sumPercent = BigDecimal.ZERO;
				for (LottoGroupDtl riskRate : listGroupDtl) {
					sumPercent = sumPercent.add(BigDecimal.valueOf(riskRate.getPercentForLimit()));
					BigDecimal thisPoint = maxRisk.multiply(sumPercent.divide(BigDecimal.valueOf(100)));
					if (prizeCost.compareTo(thisPoint) <= 0) {
						lottoGroupDtlCode = riskRate.getLottoGroupDtlCode();
						secondPrize = findFromGroupDtl(prizeList, riskRate.getLottoGroupDtlCode());
						lottoBuy.setSecondRiskCode(
								findObjFromGroupDtl(prizeList, riskRate.getLottoGroupDtlCode()).getLottoGroupDtlCode());
						tierLevel = riskRate.getTierLevel();
						break;
					}
				}

				if (secondPrize == null) {
					lottoBuyErrSet = new LottoBuyDetailRes();
					lottoBuyErrSet.setLottoNumber(lottoBuy.getLottoNumber());
					lottoBuyErrSet.setStatus("OVER_MAX_RISK");
					lottoBuyListErr.add(lottoBuyErrSet);
					continue;
				}

				if (currentPrize.compareTo(secondPrize) < 0) {
					secondPrize = currentPrize;
				}

				if (secondPrize.compareTo(currentPrize) < 0
						&& (lottoBuy.getConfirm() == null || !lottoBuy.getConfirm())) {
					lottoBuyErrSet = new LottoBuyDetailRes();
					lottoBuyErrSet.setLottoNumber(lottoBuy.getLottoNumber());
					lottoBuyErrSet.setStatus("HAS_NEW_LIMIT");
					lottoBuyErrSet.setNewPrize(secondPrize);
					lottoBuyErrSet.setOldPrize(currentPrize);
					lottoBuyErrSet.setPayCost(lottoBuy.getPayCost());
					lottoBuyListErr.add(lottoBuyErrSet);
					continue;
				}
				prizeCost = secondPrize.multiply(lottoBuy.getPayCost()).add(sumPrize.getSumPrizeCost());
				/**
				 * 4.1 Check Max Risk Close
				 */
				if (prizeCost.compareTo(maxClose) > 0) {
					lottoBuyErrSet = new LottoBuyDetailRes();
					lottoBuyErrSet.setLottoNumber(lottoBuy.getLottoNumber());
					lottoBuyErrSet.setStatus("OVER_MAX_RISK");
					lottoBuyListErr.add(lottoBuyErrSet);
					continue;
				}

				// check prize cost swapped
				if (LOTTO_KIND.DIGIT3_SWAPPED.equals(payKind.getLottoKindCode())) {
					SumPrize sumPrize3Top = sumPrizeRepo.findByLottoNumberAndMsdLottoKindCodeAndLottoClassCode(
							lottoBuy.getLottoNumber(), LOTTO_KIND.DIGIT3_SWAPPED, req.getLottoClassCode());
					if (sumPrize3Top == null) {
						sumPrize3Top = new SumPrize();
					}
					BigDecimal prizeCost3Top = secondPrize.multiply(lottoBuy.getPayCost())
							.add(sumPrize3Top.getSumPrizeCost());
					if (prizeCost3Top.compareTo(maxClose) > 0) {
						lottoBuyErrSet = new LottoBuyDetailRes();
						lottoBuyErrSet.setLottoNumber(lottoBuy.getRefLottoNumber());
						lottoBuyErrSet.setStatus("OVER_MAX_RISK");
						lottoBuyListErr.add(lottoBuyErrSet);
						continue;
					}
				}

				// Set Cost
				lottoBuy.setLottoGroupDtlCode(lottoGroupDtlCode);
				lottoBuy.setPrize(secondPrize);
				lottoBuy.setTierLevel(tierLevel);

				if (!(LOTTO_KIND.DIGIT3_SWAPPED.equals(payKind.getLottoKindCode())
						&& StringUtils.isNotEmpty(lottoBuy.getRefLottoNumber()))) {
					lottoBuySccSet = new LottoBuyDetailRes();
					lottoBuySccSet.setLottoNumber(lottoBuy.getLottoNumber());
					lottoBuySccSet.setStatus("SUCCESS");
					lottoBuySccSet.setNewPrize(secondPrize);
					lottoBuySccSet.setOldPrize(currentPrize);
					lottoBuySccSet.setPayCost(lottoBuy.getPayCost());
					lottoBuyListScc.add(lottoBuySccSet);
				}
				// end comment old logic
				// }
				sumBet = sumBet.add(lottoBuy.getPayCost());
			}
			if (lottoBuyErrSet != null) {
				errorBuyDetail.setLottoBuy(lottoBuyListErr);
				warningRes.add(errorBuyDetail);
			} else {
				successBuyDetail.setLottoBuy(lottoBuyListScc);
				successRes.add(successBuyDetail);
			}
		}
		return dataRes;
	}

	public List<LottoGroupDtl> generateListDtlManual(List<LottoGroupDtl> req, String dtlCode) {
		int sumPercent = 0;
		for (LottoGroupDtl dtl : req) {
			sumPercent += dtl.getPercentForLimit();
			if (dtlCode.equals(dtl.getLottoGroupDtlCode())) {
				dtl.setPercentForLimit(sumPercent);
				return req;
			} else {
				dtl.setPercentForLimit(0);
			}
		}
		return null;
	}

	private BigDecimal findFromGroupDtl(List<PrizeSetting> listPrize, String groupDtlCode) {
		// BigDecimal dataRes = listPrize.stream().filter(item ->
		// item.getLottoGroupDtlCode().equals(groupDtlCode)).findFirst().get().getPrize();
		BigDecimal dataRes = null;
		for (PrizeSetting prizeSetting : listPrize) {
			if (groupDtlCode.equals(prizeSetting.getLottoGroupDtlCode())) {
				dataRes = prizeSetting.getPrize();
				break;
			}
		}
		if (dataRes == null) {
			dataRes = BigDecimal.ZERO;
		}
		return dataRes;
	}

	private PrizeSetting findObjFromGroupDtl(List<PrizeSetting> listPrize, String groupDtlCode) {
		PrizeSetting dataRes = null;
		// PrizeSetting dataRes = listPrize.stream().filter(item ->
		// item.getLottoGroupDtlCode().equals(groupDtlCode)).findFirst().get();
		for (PrizeSetting prizeSetting : listPrize) {
			if (prizeSetting.getLottoGroupDtlCode().equals(groupDtlCode)) {
				dataRes = prizeSetting;
				break;
			}
		}
		return dataRes;
	}

	private List<LottoGroupDtl> copyListDtl(List<LottoGroupDtl> req) {
		List<LottoGroupDtl> dataRes = new ArrayList<LottoGroupDtl>();
		LottoGroupDtl dataSet;
		for (LottoGroupDtl lottoGroupDtl : req) {
			dataSet = new LottoGroupDtl();
			BeanUtils.copyProperties(lottoGroupDtl, dataSet);
			dataRes.add(dataSet);
		}
		return dataRes;
	}
}
