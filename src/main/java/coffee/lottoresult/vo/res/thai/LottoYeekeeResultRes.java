package coffee.lottoresult.vo.res.thai;

import java.util.List;

import lombok.Data;

@Data
public class LottoYeekeeResultRes {
	private String lottoClassName;
	private String lottoClassCode;
	private String lottoClassImg;
	private String installment;
	private String lottoCategoryCode;
	private Integer totalNumberRound;
	private List<DigiListYeekee> lottoList;
	
    @Data
	public static class DigiListYeekee {
    	
		private String digit3TopLottoKindCode;
		private String digit3TopLottoNumber;
		private String digit3TopLottoKindName;
		
		private String digit2BotLottoKindCode;
		private String digit2BotLottoNumber;
		private String digit2BotLottoKindName;
		private Integer roundYeekee;
	}
}
