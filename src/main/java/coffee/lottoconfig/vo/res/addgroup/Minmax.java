package coffee.lottoconfig.vo.res.addgroup;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Minmax {
	private String groupMaxMinMapCode;
	private String vipCode;
	private String msdLottoKindCode;
	private String lottoClassCode;
	private String lottoGroupCode;
	private BigDecimal minimumTrans;
	private BigDecimal maximumTrans;
	private BigDecimal maximumUsername;
}
