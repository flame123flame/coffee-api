package coffee.web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import coffee.repo.dao.GetCloseNumberDao;
import coffee.web.vo.res.GetCloseNumberRes;

@Service
public class GetCloseNumberService {
	
	@Autowired
	private GetCloseNumberDao getCloseNumberDao;
	
	public List<GetCloseNumberRes> getEnable()
	{
		List<GetCloseNumberRes> dataSet = getCloseNumberDao.getEnable();
		return dataSet;
	}
	

}
