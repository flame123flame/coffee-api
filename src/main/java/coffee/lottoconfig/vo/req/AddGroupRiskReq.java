package coffee.lottoconfig.vo.req;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class AddGroupRiskReq {
	private String groupCode;
    private String groupName;
    private String lottoClassCode;
    private BigDecimal groupMaxClose;
    private Integer groupEarningsPercent;
    private Integer groupEarningsPercentClose;
    // ตัวอย่าง ["3DIGIT_TOP", "3DIGIT_SWAPPED"]
    private List<String> kindCode;

    private List<LottoGroupKindReq> groupRiskList;
	
	@Data
	public static class LottoGroupKindReq {
	    private String groupDtlCode;

		private BigDecimal groupMaxRisk;
		private Integer percentForLimit;
	}
}
