package coffee.transaction.vo.req;

import java.math.BigDecimal;

import lombok.Data;
@Data
public class TransactionPayCostReq {
	private BigDecimal payCost;
	private String username;
	private String installment;
	private Integer roundYeekee;
	private String lottoClassCode;
}
