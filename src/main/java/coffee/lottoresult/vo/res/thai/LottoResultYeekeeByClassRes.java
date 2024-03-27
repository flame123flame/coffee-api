package coffee.lottoresult.vo.res.thai;

import java.util.List;

import lombok.Data;

@Data
public class LottoResultYeekeeByClassRes {
	private String lottoClassName;
	private String lottoClassCode;
	private String lottoCategoryCode;
	private String lottoClassImg;
	private String digit2Bot;
	private String digit3Top;
	
	private Integer roundYeekee;
	private String installment;
//	private List<DigiListYeekeeType> lottoList;
	
    @Data
	public static class DigiListYeekeeType {
    	
		private String digit3TopLottoKindCode;
		private String digit3TopLottoNumber;
		private String digit3TopLottoKindName;
		
		private String digit2BotLottoKindCode;
		private String digit2BotLottoNumber;
		private String digit2BotLottoKindName;
		private Integer roundYeekee;
		private String yeekeeInstallment;
	}
}
