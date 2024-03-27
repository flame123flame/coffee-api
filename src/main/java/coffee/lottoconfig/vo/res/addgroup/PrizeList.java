package coffee.lottoconfig.vo.res.addgroup;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PrizeList {
	private String prizeCode;
	private BigDecimal groupMaxRisk;
	private Integer percentForLimit;
	private BigDecimal prize;
}
