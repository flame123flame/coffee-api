package coffee.transaction.vo.res;

import java.math.BigDecimal;
import java.util.Date;

import framework.constant.ProjectConstant;
import lombok.Data;

@Data
public class TransactionTimeOutRes {
	private String lottoTransactionGroupCode;
	private String lottoCategoryCode;
	private String lottoCategoryName;
	private String lottoClassCode;
	private String lottoClassName;
	private String username;
	private Date createAt;
	private String timeOpen;
	private Date timeClose;
	private Integer timeAfterBuy;
	private Integer timeBeforeLotto;
	private BigDecimal sumBet;
	private BigDecimal sumPrizeWin = BigDecimal.ZERO;
	private String status = ProjectConstant.STATUS.PENDING;

}
