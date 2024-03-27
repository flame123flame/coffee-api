package coffee.lottoconfig.vo.res;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class LimitNumberAllRes {
    private String lottoNumber;
    private BigDecimal lottoPrice;
    private Boolean enable;
    private Boolean isManual;
    private String vipCode;
}
