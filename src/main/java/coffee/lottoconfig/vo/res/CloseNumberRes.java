package coffee.lottoconfig.vo.res;

import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;

@Data
public class CloseNumberRes {

    private Long closeNumberId;
    private String closeNumberCode;
    private String lottoClassCode;
    private String lottoNumber;
    private Boolean enable;
    private Boolean isManual;
    private String createdBy;
    private Date createdAt;
    private String updatedBy;
    private Date updatedAt;
    private String msdLottoKindCode;
}
