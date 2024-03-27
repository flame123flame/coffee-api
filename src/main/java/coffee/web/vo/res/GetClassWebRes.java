package coffee.web.vo.res;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import coffee.buy.vo.res.Installment;
import lombok.Data;

@Data
public class GetClassWebRes {
	private Long timeStampServer = new Date().getTime();
	private String classCode;
	private String className;
	private String ruleDes;
	private int commissionPercent;

	private String typeInstallment;
	private List<Installment> timeSell;
	private Installment installment;
	private String installmentStr;
	private List<GetClassWebPrice> prizeSetting;
	private List<GetClassWebRuleDes> ruleDesList;
	private List<GetClassWebRuleDes> ImageList;

	@Data
	public static class GetClassWebPrice {
		private String lottoKind;
		private String lottoName;
		private BigDecimal prize;
		private BigDecimal prizeLimit;
		private String vipCode;
		private Integer minimumPerTrans;
		private Integer maximumPerTrans;
		private Integer maximumPerUser;
	}
	
	@Data
	public static class GetClassWebRuleDes {
		private String ruleDes;
		private String imageRule;
	}
}
