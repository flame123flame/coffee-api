package coffee.transaction.vo.res;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class RefundRes {
	private String status = "SUCCESS";
	private BigDecimal money;
}
