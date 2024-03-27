package coffee.lottoconfig.vo.res;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class PrizeSettingRes {
	
	private List<PrizeSettingRes> list;
	private Long prizeSettingId;
	private String prizeSettingCode;
	private String lottoGroupCode;
	private String msdLottoKindCode;
	private String msdLottoKindName;
	private String lottoGroupDtlCode;
	private String vipCode;
	private Integer seqOrder;
	private BigDecimal prize;
	private BigDecimal prizeLimit;
}
