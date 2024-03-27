package coffee.buy.vo.res;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class BuyLottoRes {
	private String status;
	private String code;
	private List<BuyDetail> error;
	private List<BuyDetail> successData;

	@Data
	public static class BuyDetail {
		private String lottoKindCode;
		private String statusKind = "SUCCESS";
		private List<LottoBuyDetailRes> lottoBuy;
	}

	@Data
	public static class LottoBuyDetailRes {
		private String lottoNumber;
		private String status;
		private BigDecimal hasBalanceToBuy;
		private BigDecimal payCost;
		private BigDecimal oldPrize;
		private BigDecimal newPrize;
	}

}
