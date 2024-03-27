package coffee.lottoconfig.vo.req;

import java.util.List;

import lombok.Data;

@Data
public class RuleDetailReq {
	String classCode;
	String prefix;
	List<RuleDes> ruleDesList;
	List<RuleDes> ruleImageList;
	
	@Data
	public static class RuleDes{
		String ruleDes;
		String imageRule;
		String ruleCode;
		String oldImage;
	}
}
