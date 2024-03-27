package coffee.yeekeeResult.vo.res;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class YeekeeApproveRes {

	private Long yeekeeSumNumberId;
	private String yeekeeSumNumberCode;
	private String installment;
	private Integer roundNumber;
	private BigDecimal sumNumber;
	private String classCode;
	private String createdBy;
	private Date createdAt;
	private String updatedBy;
	private Date updatedAt;
	private String className;
}
