package coffee.lottoconfig.vo.res;

import java.util.List;

import lombok.Data;

@Data
public class RuleDetailRes {
	String classCode;

	List<RuleDesRes> ruleDesList;
	List<RuleDesRes> ruleImageList;
	
	@Data
	public static class RuleDesRes{
		Long ruleId;
		String ruleCode;
		String ruleDes;
		String imageRule;
	}
}
