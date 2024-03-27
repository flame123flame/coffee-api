package coffee.lottoconfig.vo.res;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class GroupDtlRes {
	    private String dtlCode;
	    private Integer tierLevel;
	    private BigDecimal prize;
}
