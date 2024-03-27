package coffee.lottoresult.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coffee.lottoresult.vo.req.thai.ApproveSaveReq;
import coffee.model.ApproveResult;
import coffee.model.LottoClass;
import coffee.repo.jpa.ApproveResultRepo;
import coffee.repo.jpa.LottoClassRepository;
import coffee.repo.jpa.LottoResultRepo;
import framework.constant.ProjectConstant.STATUS;
import framework.utils.UserLoginUtil;

@Service
public class ApproveResultService {
	@Autowired
	private ApproveResultRepo approveResultRepo;

	@Autowired
	private UpdateTransactionService updateTransactionService;

	@Autowired
	private LottoResultRepo lottoResultRepo;

	@Autowired
	private LottoClassRepository lottoClassRepo;

	@Transactional
	public String saveApprove(ApproveSaveReq req) throws Exception {
		List<ApproveResult> approveSave = approveResultRepo.findByCodeGroup(req.getCodeGroup());
		int approve = 0;
		int reject = 0;
		for (ApproveResult item : approveSave) {

			if (item.getIsApprove())
				approve++;
			else
				reject++;
		}

		if (approve >= 2) {
			return "HAS_2_APPROVED";
		} else if (reject > 0) {
			return "HAS_1_REJECTED";
		}

		String username = UserLoginUtil.getUsername();
		ApproveResult myAppr = approveResultRepo.findByInstallmentAndLottoClassCodeAndUsername(req.getInstallment(),
				req.getLottoClassCode(), username);
		if (myAppr != null) {
			return "YOU_HAS_APPROVED";
		}
		ApproveResult dataSave = new ApproveResult();
		dataSave.setCreatedBy(username);
		dataSave.setLottoClassCode(req.getLottoClassCode());
		dataSave.setInstallment(req.getInstallment());
		dataSave.setUsername(username);
		dataSave.setIsApprove(req.getIsApprove());
		dataSave.setCodeGroup(req.getCodeGroup());
		approveResultRepo.save(dataSave);

		if (approve == 1 && req.getIsApprove()) {
			lottoResultRepo.updateToApprove(STATUS.APPROVE, STATUS.PENDING, req.getInstallment(),
					req.getLottoClassCode());
			LottoClass dataFind = lottoClassRepo.findByLottoClassCode(req.getLottoClassCode());
			updateTransactionService.updateMoneyDb(req.getLottoClassCode(), dataFind.getAutoUpdateWallet());
//			updateTransactionService.triggerToUpdateTransaction();
		} else if (!req.getIsApprove()) {
			lottoResultRepo.updateToApprove(STATUS.REJECT, STATUS.PENDING, req.getInstallment(),
					req.getLottoClassCode());
		}
		return "SUCCESS";
	}

	public List<ApproveResult> getList(String codeGroup) {
		List<ApproveResult> approveSave = approveResultRepo.findByCodeGroup(codeGroup);
		return approveSave;
	}
}
