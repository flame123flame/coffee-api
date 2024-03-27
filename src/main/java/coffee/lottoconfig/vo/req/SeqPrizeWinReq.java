package coffee.lottoconfig.vo.req;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class SeqPrizeWinReq {
	private String lottoClassCode;
	private Integer seqNumber;
	private BigDecimal prizeWin;
	private Long yeekeePrizeSeqMappingId;
	private String yeekeePrizeSeqMappingCode;
}
