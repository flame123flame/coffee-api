package coffee.lottoconfig.vo.req;

import java.util.List;

import coffee.lottoconfig.vo.res.addgroup.Minmax;
import lombok.Data;

@Data
public class YeekeeMaxMinReq {
	
	private List<Minmax> digit1bot;
	private List<Minmax> digit1top;
	private List<Minmax> digit2bot;
	private List<Minmax> digit2top;
	private List<Minmax> digit3swap;
	private List<Minmax> digit3top;
}
