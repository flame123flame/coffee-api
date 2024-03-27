package coffee.webresult.vo;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class WebResultStocksRes {
	
	private String lottoCategoryName;
	private String lottoCategoryCode;
	private String installment;
	private Date dateStocks;
	private List<StocksWebRes> StocksList;
	
	@Data
	public static class StocksWebRes{
		
		private String stocksName;
		private String lottoClassCode;
		private String digit3TopLottoNumber;
		private String digit2BotLottoNumber;
		private String lottoFlag;

	}
}
