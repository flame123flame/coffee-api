package coffee.web.vo.res;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class LottoWebTransactionRes {
	
	private BigDecimal sumAllGroupBet;
	private BigDecimal sumShowLotto;
	private BigDecimal sumNotShowLotto;
	private Date day;

	@Data
	public static class dateList{
		private Date transactionDate;
	}
	
	@Data
	public static class LottoGroupList{
		private BigDecimal sumGroupBet;
		private Integer countStatus;
		private String groupStatus;
	}

}
