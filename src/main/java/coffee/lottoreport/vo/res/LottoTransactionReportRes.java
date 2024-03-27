package coffee.lottoreport.vo.res;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class LottoTransactionReportRes {
	
	private String username;
	private String lottoGroupTransactionCode;
	private String lottoKindCode;
	private String msdLottoKindName;
	private String countSeq;
	private BigDecimal payCost;
	private BigDecimal prizeCost;
	private BigDecimal prizeCorrect;
	private String Installment;
}
