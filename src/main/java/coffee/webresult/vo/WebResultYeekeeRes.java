package coffee.webresult.vo;

import java.util.List;

import lombok.Data;

@Data
public class WebResultYeekeeRes {
	
	private String lottoClassName;
	private String lottoClassCode;
	private String lottoCategoryCode;
	private String installment;
	private String round;
	private String lottoFlag;
	private List<YeekeeWebRes> yeekeeList;
	
	@Data
	public static class YeekeeWebRes{
		private String digit3TopLottoNumber;
		private String digit2BotLottoNumber;
		private String roundYeekee;
	}
}
