package coffee.lottoresult.vo.res.thai;

import java.util.List;

import lombok.Data;

@Data
public class lottoResultStocksRes {
	private String installment;
	private String lottoCategoryCode;
	private String lottoCategoryName;
	private List<Stocks> stockList;
	@Data
	public static class Stocks {
		private String lottoClassCode;
		private String lottoClassName;
		private String lottoClassImg;
		private String digit3Top;
		private String digit2Top;
		private String digit2Bot;
		private String digit3Front;
		private String digit3Bot;
		
	}
}
