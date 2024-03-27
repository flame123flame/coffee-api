package coffee.web.vo.res;

import java.util.Date;
import java.util.List;

import coffee.buy.vo.res.Installment;
import lombok.Data;

@Data
public class LottoCategoryListRes {
	private Long timeStampServer = new Date().getTime();
	private List<LottoCategoryListDetail> government;
	private List<LottoCategoryListDetail> stocks;
	private List<LottoCategoryListDetail> yeekee;

	@Data
	public static class LottoCategoryListDetail {
		private String lottoName;
		private String lottoCode;
		private String categoryCode;
		private String typeInstallment;
		private String lottoImg;
		private String lottoColor;
		private String status;
		private String hideDesc;
		private Installment installment;
		private List<Installment> timeSell;
	}
}
