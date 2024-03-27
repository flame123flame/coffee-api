package coffee.lottoconfig.vo.req;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class AddLottoTimeReq {
	private Long lottoClassId;
	private String lottoClassCode;
	private String lottoClassName;
	private String typeInstallment;
	private String lottoCategoryCode;
	private Integer commissionPercent;
	private String ruleDes;
	private Integer timeAfterBuy;
	private Integer timeBeforeLotto;
	private List<TimeSellReq> timeSell;
	private String groupList;
	private String affiliateList;
	private String lottoClassImg;
	private String lottoClassColor;
	private Boolean autoUpdateWallet;
	private Boolean ignoreWeekly;
	
	private Date startTime;
	private String countTime;
	private String roundTime;
	private String stopTime;
	private BigDecimal hasBet;
	private Integer earningsPercent;
	private String prefixCode;
	private Integer countRefund;
	private String remarkVersion;

	@Data
	public static class TimeSellReq {
		private String timeSellCode;
		// dd/MM/yyyy HH:mm:ss
		private String timeOpen;
		// dd/MM/yyyy HH:mm:ss
		private String timeClose;
	}
}
