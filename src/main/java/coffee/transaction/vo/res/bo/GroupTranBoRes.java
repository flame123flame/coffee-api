package coffee.transaction.vo.res.bo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class GroupTranBoRes {
	private Long lottoGroupTransactionId;
	private String lottoGroupTransactionCode;
	private String lottoClassCode;
	private String username;
	private BigDecimal sumGroupBet;
	private BigDecimal sumGroupPrize;
	private String createdBy;
	private Date createdAt;
	private String status;
	private String installment;
	List<TransBoRes> listTransaction;
}
