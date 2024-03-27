package coffee.lottoconfig.vo.res;

import java.util.List;

import coffee.model.LimitNumber;
import lombok.Data;

@Data
public class GetAllLimitRes {
	private String kindCode;
	private String KindName;
	
	private List<LimitNumberAllRes> listLimit;
}
