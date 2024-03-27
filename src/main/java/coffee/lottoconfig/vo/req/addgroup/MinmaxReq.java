package coffee.lottoconfig.vo.req.addgroup;

import java.math.BigDecimal;
import java.util.List;

import coffee.lottoconfig.vo.res.addgroup.Minmax;
import lombok.Data;

@Data
public class MinmaxReq {
	private List<Minmax> lottoMaxMinList;
	private String groupMaxMinMapCode;
	private String vipCode;
	private String msdLottoKindCode;
	private String lottoClassCode;
	private String lottoGroupCode;
	private BigDecimal minimumTrans;
	private BigDecimal maximumTrans;
	private BigDecimal maximumUsername;

}
