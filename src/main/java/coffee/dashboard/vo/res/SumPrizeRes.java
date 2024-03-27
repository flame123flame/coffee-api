package coffee.dashboard.vo.res;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class SumPrizeRes {
	
	private String msdLottoKindName;
	private String msdLottoKindCode;
	private BigDecimal sumPrizeCost;
	private BigDecimal sumPayCost;
	private BigDecimal sumProfit;
	
}
