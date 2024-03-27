package coffee.lottoconfig.vo.res.addgroup;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class GroupRiskClassRes {
	private String groupName;
	private String groupCode;
	private BigDecimal groupMaxClose;
	private Integer groupEarningsPercent;
	private Integer groupEarningsPercentClose;
	private List<GroupRiskList> groupRiskList;
}
