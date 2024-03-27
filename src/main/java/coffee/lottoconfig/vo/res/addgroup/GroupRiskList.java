package coffee.lottoconfig.vo.res.addgroup;

import java.util.List;

import coffee.model.GroupMaxMinMap;
import lombok.Data;

@Data
public class GroupRiskList {
	private String kindCode;
	private String kindName;
	private List<GroupMaxMinMap> minmax;
	private List<Prize> prize;
}
