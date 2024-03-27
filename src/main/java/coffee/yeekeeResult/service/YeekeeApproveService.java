package coffee.yeekeeResult.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coffee.masterdata.service.LottoClassService;
import coffee.model.YeekeeSumNumber;
import coffee.repo.dao.YeekeeApproveDao;
import coffee.repo.jpa.LottoClassRepository;
import coffee.repo.jpa.YeekeeSumNumberRepo;
import coffee.yeekeeResult.vo.res.YeekeeApproveRes;
import framework.model.DataTableResponse;
import framework.model.DatatableRequest;

@Service
public class YeekeeApproveService {

	@Autowired
	private YeekeeApproveDao yeekeeApproveDao;

	@Autowired
	private YeekeeSumNumberRepo yeekeeSumNumberRepo;

	public YeekeeApproveRes getYeekeeSumNumberIsApproved(String code) {
		YeekeeApproveRes dataRes = yeekeeApproveDao.getYeekeeDetail(code);
		return dataRes;
	}

	public DataTableResponse<YeekeeApproveRes> paginate(DatatableRequest req) {
		DataTableResponse<YeekeeApproveRes> paginateData = yeekeeApproveDao.getYeekeePaginate(req);
		DataTableResponse<YeekeeApproveRes> dataTable = new DataTableResponse<>();
		List<YeekeeApproveRes> data = paginateData.getData();
		dataTable.setRecordsTotal(paginateData.getRecordsTotal());
		dataTable.setDraw(paginateData.getDraw());
		dataTable.setData(data);
		dataTable.setPage(req.getPage());
		return paginateData;
	}

	public Integer getCountCheckingYeekee() {
		Integer count = yeekeeSumNumberRepo.countByStatus("CHECKING");
		if (count != null) {
			return count;
		}
		return 0;
	}

}
