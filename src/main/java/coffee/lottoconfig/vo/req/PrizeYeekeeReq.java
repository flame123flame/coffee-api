package coffee.lottoconfig.vo.req;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PrizeYeekeeReq {
	
	private String vipCode;
	private String lottoClassCode;
	private String prizeSettingCode;
	private String lottoGroupDtlCode;
	private BigDecimal digit3top;
	private BigDecimal digit3swap;
	private BigDecimal digit2top;
	private BigDecimal digit2bot;
	private BigDecimal digit1top;
	private BigDecimal digit1bot;
	
}
