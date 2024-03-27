package coffee.lottoconfig.vo.res;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class GroupRiskRes {
	private Long lottoGroupId;
	private String lottoGroupCode;
	private String lottoClassCode;
	private String groupName;
	private BigDecimal groupMaxRisk;
	private BigDecimal groupMaxClose;
	private Integer groupEarningsPercent;
	private Integer percentForLimit;
	List<PrizeSettingRes> prizeSettingList;
}
