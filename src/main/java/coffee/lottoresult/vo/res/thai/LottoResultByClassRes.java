package coffee.lottoresult.vo.res.thai;

import java.util.List;

import lombok.Data;

@Data
public class LottoResultByClassRes {
	private String lottoClassName;
	private String lottoClassCode;
	private String lottoCategoryCode;
	private String lottoResultInstallment;
	private String lottoClassImg;
	
	private String digit3Front;
	private String digit3Bot;
	private String digit3Top;
	private String digit2Bot;
	
//	private List<DigiListType> lottoList;
	@Data
	public static class DigiListType {
			
		private String digitLottoKindCode;
		private String digitLottoNumber;
		private String digitLottoKindName;
		private String installment;
	}
}
