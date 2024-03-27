package coffee.lottocancel.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coffee.lottocancel.vo.req.LottoCancelReq;
import coffee.lottocancel.vo.res.LottoCancelRes;
import coffee.model.LottoCancel;
import coffee.repo.jpa.LottoCancelRepo;
import framework.utils.UserLoginUtil;

@Service
public class LottoCancelService {
	
	@Autowired
	LottoCancelRepo lottoCancelRepo;
	
	public LottoCancelRes getLottoCancelByLottoCancelCode(String lottoCancelCode) {
		LottoCancel item = lottoCancelRepo.findByLottoCancelCode(lottoCancelCode);
		LottoCancelRes resData = new LottoCancelRes(); 
		resData.setLottoCancelId(item.getLottoCancelId());
		resData.setLottoCancelCode(item.getLottoCancelCode());
		resData.setLottoCategoryCode(item.getLottoCategoryCode());
		resData.setLottoClassCode(item.getLottoClassCode());
		resData.setInstallment(item.getInstallment());
		resData.setRoundYeekee(item.getRoundYeekee());
		resData.setCreatedAt(item.getCreatedAt());
		resData.setCreatedBy(item.getCreatedBy());
		resData.setUpdatedAt(item.getUpdatedAt());
		resData.setUpdatedBy(item.getUpdatedBy());
		
		return resData;
	}
	
	public void createLottoCancel(LottoCancelReq req) {
		System.out.println("createLottoCancel() ::::" + req);
		LottoCancel item = new LottoCancel();
		item.setLottoCategoryCode(req.getLottoCategoryCode());
		item.setLottoClassCode(req.getLottoClassCode());
		item.setInstallment(req.getInstallment());
		item.setRoundYeekee(req.getRoundYeekee());
		item.setCreatedBy(UserLoginUtil.getUsername());
		lottoCancelRepo.save(item);
	}
}
