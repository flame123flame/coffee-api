package coffee.transaction.vo.res.bo;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class TransBoRes {
	private Long lottoTransactionId;
	private String lottoTransactionCode;
	private String lottoGroupTransactionCode;
	private String username;
	private String lottoClassCode;
	private String lottoKindCode;
	private BigDecimal payCost;
	private BigDecimal prizeCost;
	private String lottoNumber;
	private String createdBy;
	private Date createdAt;
	private String paidBy;
	private Date paidAt;
	private String status;
	private Boolean updateWallet;
	private String installment;
	private String numberCorrect;
	private BigDecimal prizeCorrect;
	private Integer countSeq;
	private String rejectRemark;
}
