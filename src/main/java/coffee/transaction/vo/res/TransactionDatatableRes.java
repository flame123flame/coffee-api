package coffee.transaction.vo.res;

import java.math.BigDecimal;
import java.util.Date;

import coffee.model.LottoCategory;
import coffee.model.LottoClass;
import framework.constant.ProjectConstant;
import lombok.Data;

@Data
public class TransactionDatatableRes {
	private String username;
	private String lottoTransactionId;
	private String lottoTransactionCode;
	private String lottoGroupTransactionCode;
	private String lottoClassCode;
	private String lottoCategoryCode;
	private String typeName;
	private String lottoClassName;
	private String  lottoKindCode;
	private String  msdLottoKindName;
	private BigDecimal  payCost;
	private BigDecimal  prizeCost;
	private BigDecimal  hasWon;
	private Date  createdAt;
	private String createdBy;
	private String updateWallet;
	private Date paidAt;
	private String  paidBy;
	private String  isLimit;
	private String  lottoNumber;
	private BigDecimal  prize;
	private String  correctNumber;
	private BigDecimal  prizeResult;
	private String installment;
	private String numberCorrect;
	private BigDecimal prizeCorrect;
	private String countSeq;
	private String status = ProjectConstant.STATUS.PENDING;
	private LottoClass lottoClass;
    private LottoCategory lottoCategory;
    private Integer roundYeekee;
}
