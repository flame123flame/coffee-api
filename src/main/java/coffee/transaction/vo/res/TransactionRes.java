package coffee.transaction.vo.res;

import java.math.BigDecimal;

import framework.constant.ProjectConstant;
import lombok.Data;

@Data
public class TransactionRes {
	private String lottoTransactionCode;
	private String lottoGroupTransactionCode;
	private String  kindCode;
	private String  kindName;
	private String  lottoNumber;
	private BigDecimal  prize;
	private BigDecimal  payCost;
	private String  correctNumber;
	private BigDecimal  prizeResult;
	private Integer roundYeekee;
	
	private String status = ProjectConstant.STATUS.PENDING;
}
