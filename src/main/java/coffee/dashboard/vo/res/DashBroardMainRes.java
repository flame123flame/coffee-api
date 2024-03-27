package coffee.dashboard.vo.res;

import java.math.BigDecimal;
import java.util.List;

import coffee.model.SumPrize;
import lombok.Data;

@Data
public class DashBroardMainRes {
	private String lottoClassCode;
	private String className;
	private BigDecimal sumBet;
	private List<LottoListDashBroard> lottoList;
	private List<LottoListDashBoard> lottoListLv;

	@Data
	public static class LottoListDashBroard {
		private String kindCode;
		private String kindName;
		private BigDecimal sumBet;
		private List<SumPrize> prizeList;
	}
	
	@Data
	public static class LottoListDashBoard {
		private String kindCode;
		private String kindName;
		private BigDecimal sumBet;
		private List<SumPrizeList> prizeList;
	}
	
	@Data 
	public static class LottoSwappedSum{
		private String lottoNumber;
		private BigDecimal sumPrizeCost;
		private String msdLottoKindCode;
		private String tierLevel;
		private BigDecimal sumPayCost;
	}
}
