package coffee.lottoconfig.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import coffee.buy.vo.req.BuyLottoReq;
import coffee.buy.vo.req.BuyLottoReq.LottoBuy;
import coffee.buy.vo.req.BuyLottoReq.PayNumber;
import coffee.buy.vo.res.BuyLottoRes.BuyDetail;
import coffee.buy.vo.res.BuyLottoRes.LottoBuyDetailRes;
import coffee.lottoconfig.vo.req.CloseNumberReq;
import coffee.lottoreport.vo.res.GetAllCloseNumber;
import coffee.model.CloseNumber;
import coffee.model.MsdLottoKind;
import coffee.repo.dao.CloseNumberDao;
import coffee.repo.dao.MsdLottoKindDao;
import coffee.repo.jpa.CloseNumberRepo;
import framework.constant.LottoConstant.LOTTO_KIND;
import framework.constant.ResponseConstant.RESPONSE_MESSAGE.SAVE;
import framework.utils.GenerateRandomString;
import framework.utils.TimeUtil;
import framework.utils.UserLoginUtil;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CloseNumberService {

	// public Map<String, List<CloseNumber>> closeNumberCache = new HashMap<>();

	@Autowired
	private CloseNumberDao closeNumberDao;

	@Autowired
	private CloseNumberRepo closeNumberRepo;

	@Autowired
	private MsdLottoKindDao msdLottoKindDao;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	// public synchronized void updateCloseNumberCache() {
	// List<CloseNumber> closeNumbers = closeNumberRepo.findByEnable(true);
	// closeNumberCache = new HashMap<>();
	// for (CloseNumber closeNumber : closeNumbers) {
	// List<CloseNumber> closeListInMap =
	// closeNumberCache.get(closeNumber.getLottoClassCode());
	// if (closeListInMap == null) {
	// List<CloseNumber> closeListInMapSet = new ArrayList<>();
	// closeListInMapSet.add(closeNumber);
	// closeNumberCache.put(closeNumber.getLottoClassCode(), closeListInMapSet);
	// } else {
	// closeListInMap.add(closeNumber);
	// closeNumberCache.put(closeNumber.getLottoClassCode(), closeListInMap);
	// }
	// }
	// }

	// public synchronized void updateCloseNumberCacheByClass(String classCode) {
	// List<CloseNumber> closeNumber =
	// closeNumberRepo.findByLottoClassCodeAndEnable(classCode, true);
	// if (closeNumber.size() == 0) {
	// return;
	// }
	// closeNumberCache = new HashMap<>();
	// List<CloseNumber> closeListInMap = closeNumberCache.get(classCode);
	// if (closeListInMap == null) {
	// List<CloseNumber> closeListInMapSet = new ArrayList<>();
	// closeListInMapSet.addAll(closeNumber);
	// closeNumberCache.put(classCode, closeListInMapSet);
	// } else {
	// closeListInMap.addAll(closeNumber);
	// closeNumberCache.put(classCode, closeListInMap);
	// }
	// }

	@Async
	private void setCloseNumberCache(String classCode) {
		TimeUtil.setTimeoutSync(() -> {
			// updateCloseNumberCacheByClass(classCode);
			this.simpMessagingTemplate.convertAndSend("/closeNumber/" + classCode, getAllListLimit(classCode));
		}, 100);
	}

	@Transactional
	public List<CloseNumber> getAllByMSDLottoKindAndLottoClass(String kind, String lottoClass) {
		List<CloseNumber> resData = closeNumberDao.getByKind(lottoClass, kind);
		return resData;
	}

	@Transactional
	public String insertOne(CloseNumberReq req) {
		if (LOTTO_KIND.DIGIT3_SWAPPED.equals(req.getMsdLottoKindCode())) {
			String swappedGroupCode = GenerateRandomString.generateUUID();
			List<String> listSwapped = createSwapped(req.getLottoNumber());
			for (String number : listSwapped) {
				CloseNumber item = closeNumberRepo.findByLottoNumberAndLottoClassCodeAndMsdLottoKindCode(number,
						req.getLottoClassCode(), req.getMsdLottoKindCode());
				if (item == null) {
					item = new CloseNumber();
					item.setCreatedBy(UserLoginUtil.getUsername());
				} else {
					item.setUpdatedBy(UserLoginUtil.getUsername());
					item.setUpdatedAt(new Date());
				}
				item.setLottoClassCode(req.getLottoClassCode());
				item.setLottoNumber(number);
				item.setMsdLottoKindCode(req.getMsdLottoKindCode());
				item.setSwappedGroupCode(swappedGroupCode);
				item.setIsManual(true);
				item.setEnable(true);
				closeNumberRepo.save(item);
			}
		} else {
			CloseNumber item = closeNumberRepo.findByCloseNumberId(req.getCloseNumberId());
			if (item != null) {
				item.setUpdatedBy(UserLoginUtil.getUsername());
				item.setUpdatedAt(new Date());
			} else {
				item = new CloseNumber();
				item.setCreatedBy(UserLoginUtil.getUsername());
			}
			item.setLottoClassCode(req.getLottoClassCode());
			item.setLottoNumber(req.getLottoNumber());
			item.setMsdLottoKindCode(req.getMsdLottoKindCode());
			item.setIsManual(true);
			item.setEnable(true);
			closeNumberRepo.save(item);
		}
		setCloseNumberCache(req.getLottoClassCode());

		return SAVE.SUCCESS;
	}

	private List<String> createSwapped(String lottoNumber) {
		Set<String> mapNum = new HashSet<String>();

		mapNum.add(lottoNumber);
		mapNum.add("" + lottoNumber.charAt(0) + lottoNumber.charAt(2) + lottoNumber.charAt(1));
		mapNum.add("" + lottoNumber.charAt(1) + lottoNumber.charAt(0) + lottoNumber.charAt(2));
		mapNum.add("" + lottoNumber.charAt(1) + lottoNumber.charAt(2) + lottoNumber.charAt(0));
		mapNum.add("" + lottoNumber.charAt(2) + lottoNumber.charAt(1) + lottoNumber.charAt(0));
		mapNum.add("" + lottoNumber.charAt(2) + lottoNumber.charAt(0) + lottoNumber.charAt(1));

		List<String> result3 = new ArrayList<String>(mapNum);
		Collections.sort(result3);
		return result3;
	}

	@Transactional
	public void deleteOne(Long id, String classCode, String swappedGroupCode) {
		if (StringUtils.isNotEmpty(swappedGroupCode)) {
			closeNumberRepo.deleteBySwappedGroupCode(swappedGroupCode);
		} else {
			closeNumberRepo.deleteById(id);
		}
		setCloseNumberCache(classCode);
	}

	public CloseNumber getDtlList(Long id) {
		CloseNumber dataRes = closeNumberRepo.findByCloseNumberId(id);
		return dataRes;
	}

	@Transactional
	public void setEnable(Boolean status, Long id, String classCode, String swappedGroupCode) {
		if (StringUtils.isNotEmpty(swappedGroupCode)) {
			closeNumberRepo.updateEnableByGroupSwappedCode(status, swappedGroupCode);
		} else {
			CloseNumber data = closeNumberRepo.findByCloseNumberId(id);
			data.setEnable(status);
			data.setUpdatedBy(UserLoginUtil.getUsername());
			data.setUpdatedAt(new Date());
			closeNumberRepo.save(data);
		}
		setCloseNumberCache(classCode);
	}

	public List<BuyDetail> checkCloseNumber(BuyLottoReq req) {
		List<BuyDetail> dataRes = new ArrayList<>();
		List<CloseNumber> closeNumbers = closeNumberRepo.findByLottoClassCodeAndEnable(req.getLottoClassCode(), true);
		if (closeNumbers == null) {
			closeNumbers = new ArrayList<>();
		}
		for (PayNumber kind : req.getPayNumber()) {
			BuyDetail errSet = new BuyDetail();
			errSet.setStatusKind("BUY_FAIL");
			errSet.setLottoKindCode(kind.getLottoKindCode());
			List<LottoBuyDetailRes> errBuyList = new ArrayList<>();
			LottoBuyDetailRes errBuySet;
			for (LottoBuy lottoBuy : kind.getLottoBuy()) {
				if (StringUtils.isNotEmpty(lottoBuy.getRefLottoNumber()))
					continue;
				Boolean hasInCloseNumber = checkHasInCloseNumber(closeNumbers, kind.getLottoKindCode(),
						lottoBuy.getLottoNumber());
				if (hasInCloseNumber) {
					errBuySet = new LottoBuyDetailRes();
					errBuySet.setLottoNumber(lottoBuy.getLottoNumber());
					errBuySet.setStatus("CLOSE_NUMBER");
					errBuyList.add(errBuySet);
				}
			}
			if (errBuyList.size() > 0) {
				errSet.setLottoBuy(errBuyList);
				dataRes.add(errSet);
			}
		}

		return dataRes;
	}

	private Boolean checkHasInCloseNumber(List<CloseNumber> listCloseNumber, String kindCode, String number) {
		for (CloseNumber closeNumber : listCloseNumber) {
			if (kindCode.equals(closeNumber.getMsdLottoKindCode()) && number.equals(closeNumber.getLottoNumber())) {
				return true;
			}
		}
		return false;
	}

	public List<GetAllCloseNumber> getAllListLimit(String classCode) {
		List<GetAllCloseNumber> dataRes = new ArrayList<GetAllCloseNumber>();

		List<MsdLottoKind> data = msdLottoKindDao.findMsdLottoInKindByClassCode(classCode);
		List<CloseNumber> listNumberFind = closeNumberRepo.findByLottoClassCodeAndEnable(classCode, true);
		if (listNumberFind == null) {
			listNumberFind = new ArrayList<CloseNumber>();
		}
		for (MsdLottoKind item : data) {
			GetAllCloseNumber dataSet = new GetAllCloseNumber();
			dataSet.setKindCode(item.getMsdLottoKindCode());
			dataSet.setListCloseNumber(listNumberFind.stream().filter((closeNumber) -> {
				return closeNumber.getMsdLottoKindCode().equals(item.getMsdLottoKindCode());
			}).collect(Collectors.toList()));
			dataRes.add(dataSet);
		}
		return dataRes;
	}
}
