package coffee.lottoreport.vo.res;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class LottoReportUpdatedResultYeekeeRes {
	private BigDecimal numberResult;
	private BigDecimal numberSeq16;
	private BigDecimal sumNumber;
	private Date updatedAt;
	private String updatedBy;
	private Boolean changeResult;
}
