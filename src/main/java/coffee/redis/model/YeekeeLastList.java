package coffee.redis.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import coffee.buy.vo.res.YeeKeeNumberSubmitListRes;
import coffee.yeekeeResult.vo.res.YeekeeResultRoundSeqRes;
import lombok.Data;

@Data
public class YeekeeLastList {
    private BigDecimal num16;
    private BigDecimal numberResult;

    @JsonProperty("numberSum")
    private final BigDecimal numberSum;
    @JsonProperty("listLast20")
    private final List<YeeKeeNumberSubmitListRes> listLast20;
    @JsonProperty("listSeq")
    private final List<YeekeeResultRoundSeqRes> listSeq;
}