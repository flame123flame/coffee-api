package coffee.confirmtransaction.vo;

import java.math.BigDecimal;
import java.util.Date;

import coffee.model.LottoCategory;
import coffee.model.LottoClass;
import lombok.Data;

@Data
public class ConfirmTransactionRes {
	private BigDecimal lottoTransactionId;
	private String lottoTransactionCode;
	private String className;
	private String username;
	private String lottoClassCode;
	private String lottoKindCode;
	private String lottoGroupTransactionCode;
	private String isLimit;
	private String updateWallet;
	private String rejectRemark;
	private String lottoNumber;
	private String installment;
	private String msdLottoKindName;
	private String countSeq;
	private BigDecimal payCost;
	private BigDecimal prizeCost;
	private BigDecimal prizeCorrect;
	private LottoClass lottoClass;
	private LottoCategory lottoCategory;
	private Date createdAt;
	private String paidBy;
	private Date paidAt;
	private String status;
	private String numberCorrect;
	
	

}
