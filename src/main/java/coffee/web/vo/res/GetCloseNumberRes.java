package coffee.web.vo.res;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class GetCloseNumberRes {
	
	private BigDecimal closeNumberId;
	private String closeNumberCode;
	private String lottoClassCode;
	private String lottoNumber;
	private BigDecimal enable;
	private BigDecimal isManual;
	private String createdBy;
	private String updatedBy;
	private String msdLottoKindCode;
}
