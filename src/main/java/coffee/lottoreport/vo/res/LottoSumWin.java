package coffee.lottoreport.vo.res;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class LottoSumWin {
	private String lottoClassCode;
	private String lottoKindCode;
	private String msdLottoKindName;
	private BigDecimal sumMsdWin;
	private String countSeq;
	private BigDecimal prizeCorrect;
	private String Installment;
	private BigDecimal sumPrizeCorrectWin;
	private BigDecimal sumPrizeCorrectLose;
	private BigDecimal sumTest;
	private BigDecimal sumProfit;
	private BigDecimal sumPayCorrectWin;
	

}
