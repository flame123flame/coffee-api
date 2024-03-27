package coffee.dashboard.vo.res;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class SumPrizeList {
	
	private Long sumPrizeId;
	private String sumPrizeCode;
	private String lottoClassCode;
	private String msdLottoKindCode;
	private BigDecimal sumPrizeCost = BigDecimal.ZERO;
	private BigDecimal justSumPrize = BigDecimal.ZERO;
	private BigDecimal sumPlusSwapped = BigDecimal.ZERO;
	private Date installment;
	private String lottoNumber;
	private String createdBy;
	private Date createdAt = new Date();
	private String updatedBy;
	private Date updatedAt;
	private BigDecimal sumPayCost = BigDecimal.ZERO;
	private String tierLevel;
}
