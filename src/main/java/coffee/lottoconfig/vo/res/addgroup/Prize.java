package coffee.lottoconfig.vo.res.addgroup;

import java.util.List;

import lombok.Data;

@Data
public class Prize {
	private String vipCode;
	private List<PrizeList> prizeList;
}
