package coffee.lottoconfig.vo.res;

import java.util.List;

import coffee.model.GroupMaxMinMap;
import lombok.Data;

@Data
public class YeekeeMaxMinRes {

	private List<GroupMaxMinMap> digit1bot;
	private List<GroupMaxMinMap> digit1top;
	private List<GroupMaxMinMap> digit2bot;
	private List<GroupMaxMinMap> digit2top;
	private List<GroupMaxMinMap> digit3swap;
	private List<GroupMaxMinMap> digit3top;
}
