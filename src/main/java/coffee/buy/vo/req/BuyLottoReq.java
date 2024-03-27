package coffee.buy.vo.req;

import java.math.BigDecimal;
import java.util.List;

import coffee.model.YeekeeGroupPrize;
import lombok.Data;

@Data
public class BuyLottoReq {
	private String username;
	private String lottoClassCode;
	private String vipCode;
	private Integer commissionPercent;
	private List<PayNumber> payNumber;
	private int roundYeeKee;

	@Data
	static public class PayNumber {
		private String lottoKindCode;
		private String lottoGroupCode;
		private List<LottoBuy> lottoBuy;
	}

	@Data
	static public class LottoBuy {
		private String lottoNumber;
		private String refLottoNumber;
		private String groupSwappedCode;

		private BigDecimal payCost;
		private BigDecimal prize;
		private Boolean confirm;
		private String lottoGroupDtlCode;
		private int tierLevel;
		private String secondRiskCode;
		private Integer countUser;
		private YeekeeGroupPrize yeekeeGroupPrize;
	}

}
