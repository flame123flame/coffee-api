package coffee.transaction.vo.res;

import java.math.BigDecimal;
import java.util.Date;

import framework.constant.ProjectConstant;
import lombok.Data;

@Data
public class TransactionGroupRes {
	private String lottoTransactionGroupCode;
	private String lottoCategoryCode;
	private String lottoCategoryName;
	private String lottoClassCode;
	private String lottoClassName;
	private String username;
	private String createAt;
	private Date createAtDate;
	private String installment;
	private boolean canRefund = false;
	private BigDecimal sumBet;
	private BigDecimal sumPrizeWin = BigDecimal.ZERO;
	private String status = ProjectConstant.STATUS.PENDING;
	private Integer roundYeekee;
	private String remark;
}
