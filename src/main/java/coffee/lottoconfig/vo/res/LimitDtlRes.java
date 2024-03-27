package coffee.lottoconfig.vo.res;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class LimitDtlRes {
	private String createdAt;
	private String createdBy;
	private Boolean enable;
	private Boolean isManual;
	private String limitNumberCode;
	private Long limitNumberId;
	private String lottoClassCode;
	private String lottoNumber;
	private BigDecimal lottoPrice;
	private String msdLottoKindCode;
	private String updatedAt;
	private String updatedBy;
	private BigDecimal prize;
	private Integer tierLevel;
	private String lottoGroupDtlCode;
}
