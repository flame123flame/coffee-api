package coffee.web.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import coffee.repo.dao.LottoTransactionDao;
import coffee.web.vo.res.LottoWebTransactionRes;
import coffee.web.vo.res.LottoWebTransactionRes.LottoGroupList;
import coffee.web.vo.res.LottoWebTransactionRes.dateList;
import framework.constant.LottoConstant.LOTTO_STATUS;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GetTransactionWebService {
	
	@Autowired LottoTransactionDao lottoTransactionDao;
	
	public List<LottoWebTransactionRes> getWebTransaction(String username) {
		
		List<LottoWebTransactionRes> lottoList = new ArrayList<LottoWebTransactionRes>();
		List<dateList> dateList = lottoTransactionDao.getTransactionWebDate(username);
		
		for(dateList datedata : dateList)
		{
			BigDecimal SumBet = new BigDecimal(0);
			List<LottoGroupList> transactionData = lottoTransactionDao.getTransactionWeb(username,datedata.getTransactionDate());
			LottoWebTransactionRes dataSet = new LottoWebTransactionRes();
			dataSet.setSumShowLotto((BigDecimal.valueOf(0)));
			dataSet.setSumNotShowLotto((BigDecimal.valueOf(0)));
			for(LottoGroupList dataRes : transactionData)
			{
				
				SumBet = SumBet.add(dataRes.getSumGroupBet());
				dataSet.setSumAllGroupBet(SumBet);
				if(dataRes.getGroupStatus().equals(LOTTO_STATUS.PENDING))
				{
					if(dataRes.getCountStatus()==null)
					{
						dataSet.setSumNotShowLotto((BigDecimal.valueOf(0)));
					}
					else
					{
						dataSet.setSumNotShowLotto((BigDecimal.valueOf(dataRes.getCountStatus())));
					}
				}
				if(dataRes.getGroupStatus().equals(LOTTO_STATUS.SHOW))
				{
					if(dataRes.getCountStatus()==null)
					{
						dataSet.setSumShowLotto((BigDecimal.valueOf(0)));
					}
					else
					{
						dataSet.setSumShowLotto((BigDecimal.valueOf(dataRes.getCountStatus())));
					}
					
				}
				dataSet.setDay(datedata.getTransactionDate());
			}
			lottoList.add(dataSet);
		}
		
		return lottoList;
	}
	
}
