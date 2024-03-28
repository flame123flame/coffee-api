package coffee.transaction.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import coffee.buy.service.BuyLottoService;
import coffee.buy.vo.res.Installment;
import coffee.model.LimitNumber;
import coffee.model.LottoClass;
import coffee.model.LottoGroup;
import coffee.model.LottoGroupDtl;
import coffee.model.LottoGroupTransaction;
import coffee.model.LottoTransaction;
import coffee.model.SumBetGroup;
import coffee.model.SumPrize;
import coffee.model.TimeSell;
import coffee.model.YeekeeGroupPrize;
import coffee.repo.dao.LottoGroupDao;
import coffee.repo.dao.LottoGroupDtlDao;
import coffee.repo.dao.LottoGroupTransactionDao;
import coffee.repo.dao.PrizeSettingDao;
import coffee.repo.dao.SumPrizeDao;
import coffee.repo.jpa.LimitNumberRepo;
import coffee.repo.jpa.LottoCategoryRepo;
import coffee.repo.jpa.LottoClassRepository;
import coffee.repo.jpa.LottoGroupDtlRepo;
import coffee.repo.jpa.LottoGroupTransactionRepo;
import coffee.repo.jpa.LottoTransactionRepo;
import coffee.repo.jpa.MsdLottoKindRepository;
import coffee.repo.jpa.SumBetGroupRepo;
import coffee.repo.jpa.SumPrizeRepo;
import coffee.repo.jpa.TimeSellRepo;
import coffee.repo.jpa.YeekeeGroupPrizeRepo;
import coffee.transaction.vo.req.GetTransBoReq;
import coffee.transaction.vo.res.RefundRes;
import coffee.transaction.vo.res.TransactionDatatableRes;
import coffee.transaction.vo.res.TransactionGroupDatatableRes;
import coffee.transaction.vo.res.TransactionGroupDetailRes;
import coffee.transaction.vo.res.TransactionGroupRes;
import coffee.transaction.vo.res.TransactionRes;
import coffee.transaction.vo.res.bo.GroupTranBoRes;
import coffee.web.service.WebTimeSellService;
import framework.constant.LottoConstant;
import framework.constant.LottoConstant.LOTTO_KIND;
import framework.constant.ProjectConstant;
import framework.model.DataTableResponse;
import framework.model.DatatableRequest;
import framework.utils.ConvertDateUtils;
import framework.utils.UserLoginUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransactionGroupService {
	@Autowired
	private LottoGroupTransactionDao lottoGroupTransactionDao;

	@Autowired
	private LottoGroupTransactionRepo lottoGroupTransactionRepo;

	@Autowired
	private LottoClassRepository lottoClassRepository;

	@Autowired
	private LottoCategoryRepo lottoCategoryRepo;

	@Autowired
	private TransactionService transactionService;

	@Autowired
	private BuyLottoService buyLottoService;

	@Autowired
	private LottoTransactionRepo lottoTransactionRepo;

	@Autowired
	private SumBetGroupRepo sumBetGroupRepo;

	@Autowired
	private SumPrizeRepo sumPrizeRepo;

	@Autowired
	private LottoGroupDao lottoGroupDao;

	@Autowired
	private LimitNumberRepo limitNumberRepo;

	@Autowired
	private PrizeSettingDao prizeSettingDao;

	@Autowired
	private LottoGroupDtlRepo lottoGroupDtlRepo;

	@Autowired
	private LottoGroupDtlDao lottoGroupDtlDao;

	@Autowired
	private SumPrizeDao sumPrizeDao;

	@Autowired
	private MsdLottoKindRepository msdLottoKindRepo;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	private WebTimeSellService webTimeSellService;

	@Autowired
	private TimeSellRepo timeSellRepo;

	@Autowired
	private YeekeeGroupPrizeRepo yeekeeGroupPrizeRepo;

	public DataTableResponse<TransactionGroupDatatableRes> paginate(DatatableRequest req) {
		DataTableResponse<TransactionGroupDatatableRes> paginateData = lottoGroupTransactionDao.paginate(req);
		DataTableResponse<TransactionGroupDatatableRes> dataTable = new DataTableResponse<>();
		List<TransactionGroupDatatableRes> data = paginateData.getData();
		// for (TransactionGroupDatatableRes item : data) {
		// item.setLottoClass(lottoClassRepository.findByLottoClassCode(item.getLottoClassCode()));
		// if (item.getLottoClass() != null) {
		// item.setLottoCategory(
		// lottoCategoryRepo.findByLottoCategoryCode(item.getLottoClass().getLottoCategoryCode()).get());
		// }
		// }

		dataTable.setRecordsTotal(paginateData.getRecordsTotal());
		dataTable.setDraw(paginateData.getDraw());
		dataTable.setData(data);
		dataTable.setPage(req.getPage());
		return paginateData;
	}

	public DataTableResponse<TransactionDatatableRes> paginateAllTransaction(DatatableRequest req) {
		DataTableResponse<TransactionDatatableRes> paginateData = lottoGroupTransactionDao.paginateAllTransaction(req);
		DataTableResponse<TransactionDatatableRes> dataTable = new DataTableResponse<>();
		List<TransactionDatatableRes> data = paginateData.getData();

		dataTable.setRecordsTotal(paginateData.getRecordsTotal());
		dataTable.setDraw(paginateData.getDraw());
		dataTable.setData(data);
		dataTable.setPage(req.getPage());
		return paginateData;
	}

	public List<LottoClass> getAllLottoClass() {
		List<LottoClass> dataRes = lottoClassRepository.findAll();
		return dataRes;
	}

	public List<TransactionGroupRes> getAllGroup() {
		List<TransactionGroupRes> dataTc = lottoGroupTransactionDao.getAllGroup();
		return dataTc;
	}

	public List<TransactionGroupRes> getLottoGroupByUser() {
		List<TransactionGroupRes> dataTc = lottoGroupTransactionDao.getLottoGroupByUser(UserLoginUtil.getUsername());
		return dataTc;
	}

	public List<TransactionGroupRes> getLottoGroupByUserPending() {
		List<TransactionGroupRes> dataTc = lottoGroupTransactionDao
				.getLottoGroupByUserPending(UserLoginUtil.getUsername());
		Map<String, Integer> mapTimeAfter = new HashMap<String, Integer>();
		Map<String, Integer> mapTimeBefore = new HashMap<String, Integer>();
		Map<String, Installment> mapInstllment = new HashMap<String, Installment>();

		for (TransactionGroupRes transactionGroupRes : dataTc) {
			if (mapTimeAfter.get(transactionGroupRes.getLottoClassCode()) == null) {
				LottoClass lottoClass = lottoClassRepository
						.findByLottoClassCode(transactionGroupRes.getLottoClassCode());
				mapTimeAfter.put(transactionGroupRes.getLottoClassCode(), lottoClass.getTimeAfterBuy());
				mapTimeBefore.put(transactionGroupRes.getLottoClassCode(), lottoClass.getTimeBeforeLotto());
				mapInstllment.put(transactionGroupRes.getLottoClassCode(),
						buyLottoService.checkInstallment(transactionGroupRes.getLottoClassCode(), null, lottoClass));
			}
		}

		for (TransactionGroupRes transactionGroupRes : dataTc) {
			Calendar createAtCalendar = Calendar.getInstance();
			createAtCalendar.setTime(transactionGroupRes.getCreateAtDate());
			createAtCalendar.add(Calendar.HOUR, mapTimeAfter.get(transactionGroupRes.getLottoClassCode()));

			Date thisDate = new Date();

			Calendar timeCloseCalendar = Calendar.getInstance();
			timeCloseCalendar.setTime(mapInstllment.get(transactionGroupRes.getLottoClassCode()).getTimeClose());
			timeCloseCalendar.add(Calendar.HOUR, -mapTimeBefore.get(transactionGroupRes.getLottoClassCode()));

			if (thisDate.before(createAtCalendar.getTime()) && thisDate.before(timeCloseCalendar.getTime())) {
				transactionGroupRes.setCanRefund(true);
			}
		}

		return dataTc;
	}

	public List<TransactionGroupRes> getLottoGroupByUserShow(String code) {
		List<TransactionGroupRes> dataTc = lottoGroupTransactionDao.getLottoGroupByUserShow(UserLoginUtil.getUsername(),
				code);
		return dataTc;
	}

//	public TransactionGroupDetailRes getLottoGroupDetailByCode(String groupCode) {
//		TransactionGroupDetailRes dataRes = lottoGroupTransactionDao.getGroupByCode(groupCode);
//		dataRes.setListTrantsaction(transactionService.getTransactionByGroup(groupCode));
//		BigDecimal sumPrizeWin = BigDecimal.ZERO;
//		for (TransactionRes item : dataRes.getListTrantsaction()) {
//			sumPrizeWin = sumPrizeWin.add(item.getPrizeResult());
//		}
//		dataRes.setSumPrizeWin(sumPrizeWin);
//		return dataRes;
//	}

	public TransactionGroupDetailRes getLottoGroupDetailByCode(String groupCode) {
		TransactionGroupDetailRes dataRes = lottoGroupTransactionDao.getGroupByCode(groupCode);
		if (dataRes != null) {
			List<TransactionRes> transactionByGroup = transactionService.getTransactionByGroup(groupCode);
			if (transactionByGroup != null) {
				dataRes.setListTrantsaction(transactionByGroup);
				BigDecimal sumPrizeWin = BigDecimal.ZERO;
				for (TransactionRes item : dataRes.getListTrantsaction()) {
					if (item != null && item.getPrizeResult() != null) {
						sumPrizeWin = sumPrizeWin.add(item.getPrizeResult());
					}
				}
				dataRes.setSumPrizeWin(sumPrizeWin);
			}
		}
		return dataRes;
	}

	@Transactional
	public RefundRes checkLottoRefundTimeOut(String groupCode, String classCode, Integer roundYeekee) throws Exception {
		Boolean isYeekee = false;
		if (roundYeekee != null) {
			isYeekee = true;
		}
		RefundRes dataRes = new RefundRes();
		LottoGroupTransaction lottoGroupTransaction = lottoGroupTransactionRepo
				.findByLottoGroupTransactionCode(groupCode);

		/**
		 * ถ้าสถานะไม่ใช่รอหวยออก
		 */
		if (!ProjectConstant.STATUS.PENDING.equals(lottoGroupTransaction.getStatus())) {
			dataRes.setStatus("NOT_PENDING_STATUS");
			return dataRes;
		}

		LottoClass lottoClass = lottoClassRepository.findByLottoClassCode(classCode);
		Installment installment = null;

		Calendar dateBefore = Calendar.getInstance();
		if (isYeekee) {
			List<TimeSell> timeFind = timeSellRepo.findByLottoClassCode(classCode);
			List<Installment> listInstall = webTimeSellService
					.getListInstallmentByType(LottoConstant.TYPE_INSTALLMENT.HR24, timeFind, false);
			installment = listInstall.get(roundYeekee - 1);
			dateBefore.setTime(installment.getTimeClose());
			dateBefore.add(Calendar.MINUTE, -lottoClass.getTimeBeforeLotto());
		} else {
			installment = buyLottoService.checkInstallment(classCode, null, lottoClass);
			dateBefore.setTime(installment.getTimeClose());
			dateBefore.add(Calendar.HOUR_OF_DAY, -lottoClass.getTimeBeforeLotto());
		}

		/**
		 * หมดเวลาตามประเภทหวย
		 */
		if (dateBefore.getTime().before(new Date())) {
			dataRes.setStatus("TIME_OUT_TYPE");
			return dataRes;
		}

		/**
		 * หมดเวลาหลังซื้อ
		 */
		Calendar dateAfter = Calendar.getInstance();
		dateAfter.setTime(lottoGroupTransaction.getCreatedAt());
		dateAfter.add(Calendar.HOUR, lottoClass.getTimeAfterBuy());
		if (new Date().after(dateAfter.getTime())) {
			dataRes.setStatus("TIME_OUT_TRANSACTION");
			return dataRes;
		}

		String installmentStr = webTimeSellService.getInstallmentStr(lottoClass.getTypeInstallment(),
				installment.getTimeClose(), installment.getLengthTimeSell());

		Integer countRefund = lottoGroupTransactionRepo.getCountRefund(installmentStr,
				LottoConstant.LOTTO_STATUS.REFUND, UserLoginUtil.getUsername(), classCode);
		if (countRefund >= lottoClass.getCountRefund()) {
			dataRes.setStatus("OVER_COUNT");
			return dataRes;
		}

		/**
		 * -- สามารถคืนได้--
		 */

		/**
		 * เซ็ตเงินคืน
		 */
		dataRes.setMoney(lottoGroupTransaction.getSumGroupBet());

		/**
		 * set status REFUND in DB
		 */
		lottoGroupTransaction.setStatus(LottoConstant.LOTTO_STATUS.REFUND);
		lottoGroupTransactionRepo.save(lottoGroupTransaction);

		List<LottoTransaction> listTransList = lottoTransactionRepo
				.findByLottoGroupTransactionCode(lottoGroupTransaction.getLottoGroupTransactionCode());
		if (isYeekee) {
			for (LottoTransaction lottoTrans : listTransList) {
				YeekeeGroupPrize yeekeeGroupPrize = yeekeeGroupPrizeRepo
						.findByLottoNumberAndRoundYeekeeAndInstallmentAndClassCode(lottoTrans.getLottoNumber(),
								lottoTrans.getRoundYeekee(), lottoTrans.getInstallment(),
								lottoTrans.getLottoClassCode());
				yeekeeGroupPrize.setSumPrize(yeekeeGroupPrize.getSumPrize().subtract(lottoTrans.getPrizeCorrect()));
				yeekeeGroupPrize.setSumBet(yeekeeGroupPrize.getSumBet().subtract(lottoTrans.getPayCost()));
				yeekeeGroupPrizeRepo.save(yeekeeGroupPrize);

				lottoTrans.setStatus(LottoConstant.LOTTO_STATUS.REFUND);
				lottoTrans.setUpdatedBy(UserLoginUtil.getUsername());
				lottoTrans.setUpdatedDate(new Date());
			}
			lottoTransactionRepo.saveAll(listTransList);
			return dataRes;
		}

		List<LottoTransaction> list3Swapped = new ArrayList<LottoTransaction>();
		Map<String, LottoGroup> lottoGroupMap = new HashMap<String, LottoGroup>();
		for (LottoTransaction lottoTrans : listTransList) {
			lottoTrans.setStatus(LottoConstant.LOTTO_STATUS.REFUND);
			// Remove SumPrize SumGroupBet
			SumPrize sumPrize = removeSumPrize(classCode, lottoTrans.getLottoKindCode(), lottoTrans.getLottoNumber(),
					lottoTrans.getPrizeCost().multiply(lottoTrans.getPayCost()), lottoTrans.getPayCost());

			LottoGroup lottoGroup = lottoGroupMap.get(lottoTrans.getLottoKindCode());
			if (lottoGroup == null) {
				lottoGroup = lottoGroupDao.getGroupByLottoKind(lottoTrans.getLottoKindCode(), classCode);
				lottoGroupMap.put(lottoTrans.getLottoKindCode(), lottoGroup);
			}
			SumBetGroup sumGroupBet = removeSumGroupBet(classCode, lottoGroup.getLottoGroupCode(),
					lottoTrans.getPayCost());

			// Update Limit Number
			if (lottoTrans.getLottoKindCode().equals(LOTTO_KIND.DIGIT3_SWAPPED)) {
				list3Swapped.addAll(createListSwapped(lottoTrans));
			}
			updateLimitNumber(lottoTrans.getLottoKindCode(), lottoTrans.getLottoNumber(), classCode,
					sumGroupBet.getSumBet(), sumPrize.getSumPrizeCost(), lottoGroup);
		}
		lottoTransactionRepo.saveAll(listTransList);

		for (LottoTransaction lottoTrans : list3Swapped) {
			SumPrize sumPrize = removeSumPrize(classCode, lottoTrans.getLottoKindCode(), lottoTrans.getLottoNumber(),
					lottoTrans.getPrizeCost().multiply(lottoTrans.getPayCost()), lottoTrans.getPayCost());
			if (sumPrize == null) {
				sumPrize = new SumPrize();
			}
			LottoGroup lottoGroup = lottoGroupMap.get(lottoTrans.getLottoKindCode());
			SumBetGroup sumBetGroup = sumBetGroupRepo.findByLottoGroupCodeAndLottoClassCode(groupCode, classCode);
			if (sumBetGroup == null) {
				sumBetGroup = new SumBetGroup();
			}
			updateLimitNumber(lottoTrans.getLottoKindCode(), lottoTrans.getLottoNumber(), classCode,
					sumBetGroup.getSumBet(), sumPrize.getSumPrizeCost(), lottoGroup);
		}
		return dataRes;
	}

	private List<LottoTransaction> createListSwapped(LottoTransaction req) {
		String lottoNumber = req.getLottoNumber();
		Set<String> mapNum = new HashSet<String>();

		mapNum.add(lottoNumber);
		mapNum.add("" + lottoNumber.charAt(0) + lottoNumber.charAt(2) + lottoNumber.charAt(1));
		mapNum.add("" + lottoNumber.charAt(1) + lottoNumber.charAt(0) + lottoNumber.charAt(2));
		mapNum.add("" + lottoNumber.charAt(1) + lottoNumber.charAt(2) + lottoNumber.charAt(0));
		mapNum.add("" + lottoNumber.charAt(2) + lottoNumber.charAt(1) + lottoNumber.charAt(0));
		mapNum.add("" + lottoNumber.charAt(2) + lottoNumber.charAt(0) + lottoNumber.charAt(1));

		List<LottoTransaction> dataRes = new ArrayList<LottoTransaction>();
		LottoTransaction dataSet;
		for (String numSwapped : mapNum) {
			if (!req.getLottoNumber().equals(numSwapped)) {
				dataSet = new LottoTransaction();
				BeanUtils.copyProperties(req, dataSet);
				dataSet.setLottoNumber(numSwapped);
				dataRes.add(dataSet);
			}
		}

		return dataRes;
	}

	private SumPrize removeSumPrize(String classCode, String kindCode, String lottoNumber, BigDecimal prize,
			BigDecimal payCost) {
		SumPrize sumprize = sumPrizeRepo.findByLottoNumberAndMsdLottoKindCodeAndLottoClassCode(lottoNumber, kindCode,
				classCode);
		sumprize.setSumPrizeCost(sumprize.getSumPrizeCost().subtract(prize));
		sumprize.setSumPayCost(sumprize.getSumPayCost().subtract(payCost));
		sumprize.setUpdatedAt(new Date());
		sumPrizeRepo.save(sumprize);
		return sumprize;
	}

	private SumBetGroup removeSumGroupBet(String classCode, String groupCode, BigDecimal bet) {
		SumBetGroup sumBetGroup = sumBetGroupRepo.findByLottoGroupCodeAndLottoClassCode(groupCode, classCode);
		sumBetGroup.setSumBet(sumBetGroup.getSumBet().subtract(bet));
		sumBetGroup.setUpdatedAt(new Date());
		sumBetGroupRepo.save(sumBetGroup);
		return sumBetGroup;
	}

	public void updateLimitNumber(String kindCode, String lottoNumber, String classCode, BigDecimal sumBet,
			BigDecimal sumPrize, LottoGroup lottoGroup) {
		LimitNumber limitNumber = limitNumberRepo
				.findAllByMsdLottoKindCodeAndLottoClassCodeAndLottoNumberAndIsManualAndEnable(kindCode, classCode,
						lottoNumber, true, true);
		String lottoGroupDtlCode = null;
		List<LottoGroupDtl> listDtl = lottoGroupDtlRepo.findByLottoGroupCode(lottoGroup.getLottoGroupCode());
		if (limitNumber != null) {
			// ถ้า Limit Number แบบ manual ไว้
			if (limitNumber != null) {
				listDtl = buyLottoService.generateListDtlManual(listDtl, limitNumber.getLottoGroupDtlCodeManual());
			}
		} else {
			BigDecimal earningPercent = BigDecimal.valueOf(lottoGroup.getGroupEarningsPercent());
			BigDecimal maxRisk = lottoGroup.getGroupMaxClose();

			int count = 0;
			if (sumBet.subtract(sumBet.multiply(earningPercent.divide(BigDecimal.valueOf(100))))
					.compareTo(maxRisk) < 0) {
				BigDecimal sumPercent = BigDecimal.ZERO;
				for (LottoGroupDtl riskRate : listDtl) {
					count++;
					sumPercent = sumPercent.add(BigDecimal.valueOf(riskRate.getPercentForLimit()));
					BigDecimal thisPoint = maxRisk.multiply(sumPercent.divide(BigDecimal.valueOf(100)));
					if ((sumPrize == null ? BigDecimal.ZERO : sumPrize).compareTo(thisPoint) <= 0) {
						lottoGroupDtlCode = riskRate.getLottoGroupDtlCode();
						break;
					}
				}
			}
			if (count == 1) {
				limitNumberRepo.deleteByMsdLottoKindCodeAndLottoClassCodeAndLottoNumber(kindCode, classCode,
						lottoNumber);
			}
			if (count > 1) {
				LimitNumber limitNumberAuto = limitNumberRepo
						.findByMsdLottoKindCodeAndLottoClassCodeAndLottoNumberAndIsManual(kindCode, classCode,
								lottoNumber, false);
				if (limitNumberAuto == null) {
					limitNumberAuto = new LimitNumber();
					limitNumberAuto.setCreatedBy(UserLoginUtil.getUsername());
					limitNumberAuto.setLottoClassCode(classCode);
					limitNumberAuto.setMsdLottoKindCode(kindCode);
					limitNumberAuto.setLottoNumber(lottoNumber);
					limitNumberAuto.setEnable(true);
					limitNumberAuto.setIsManual(false);
				}
				limitNumberAuto.setLottoGroupDtlCode(lottoGroupDtlCode);
				limitNumberRepo.save(limitNumberAuto);
			}

		}
	}

	public List<GroupTranBoRes> getTransactionBo(GetTransBoReq req) {
		List<GroupTranBoRes> dataRes = lottoGroupTransactionDao.getGroupTransBo(req);
		for (GroupTranBoRes groupTranBoRes : dataRes) {
			groupTranBoRes.setListTransaction(
					lottoGroupTransactionDao.getTransBo(groupTranBoRes.getLottoGroupTransactionCode()));
		}
		return dataRes;
	}
}
