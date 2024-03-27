package coffee.yeekeeResult.vo.res;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class YeekeeResultRoundSeqRes {
	@JsonProperty("seqOrder")
	private Integer seqOrder;

	@JsonProperty("numberSubmit")
	private Long numberSubmit;

	@JsonProperty("createdBy")
	private String createdBy;

	@JsonProperty("createdDate")
	private String createdDate;
	
	@JsonProperty("username")
	private String username;
	
	@JsonProperty("prize")
	private BigDecimal prize;

}
