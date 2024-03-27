package coffee.lottoconfig.vo.res;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class LottoTimeRes {
	private Long lottoClassId;
	private String lottoClassCode;
	private String lottoClassName;
	private String typeInstallment;
	private String lottoCategoryCode;
	private Integer timeAfterBuy;
	private Integer timeBeforeLotto;
	private int commissionPercent;
	private String ruleDes;
	private String prefixCode;
	private Integer countRefund;
	private List<TimeSellRes> timeSell;
	private String groupList;
	private String affiliateList;
	private String lottoClassImg;
	private String lottoClassColor;
	private Boolean autoUpdateWallet;
	private Boolean ignoreWeekly;
	private String draftCode;
	private String remarkVersion;
	private String createdBy;
	private Date createdAt;
}
