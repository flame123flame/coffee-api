package coffee.lottoconfig.vo.res;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class PrizeYeekeeRes {
	
	private List<Long> prizeSettingId;
	private String prizeSettingCode;
	private String vipCode;
	private String lottoClassCode;
	private String lottoGroupDtlCode;
	private BigDecimal digit3top;
	private BigDecimal digit3swap;
	private BigDecimal digit2top;
	private BigDecimal digit2bot;
	private BigDecimal digit1top;
	private BigDecimal digit1bot;
	
	@Data
	public static class VipCodeListRes {
		private String vipCode;
	}

}
