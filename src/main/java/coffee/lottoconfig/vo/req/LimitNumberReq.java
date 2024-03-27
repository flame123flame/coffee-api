package coffee.lottoconfig.vo.req;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class LimitNumberReq {
    private Long limitNumberId;
    private String limitNumberCode;
    private String lottoClassCode;
    private String lottoNumber;
    private String lottoPrice;
    private Boolean enable;
    private Boolean isManual;
    private String createdBy;
    private Date createdAt;
    private String updatedBy;
    private Date updatedAt;
    private String msdLottoKindCode;

}
