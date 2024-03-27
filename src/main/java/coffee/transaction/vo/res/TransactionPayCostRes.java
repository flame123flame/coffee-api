package coffee.transaction.vo.res;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class TransactionPayCostRes {
	private String lottoGroupTransactionCode;
	private BigDecimal payCost;
	private String username;
	private String installment;
	private Integer roundYeekee;
	private String lottoClassCode;
	
}
