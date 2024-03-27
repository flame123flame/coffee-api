package coffee.lottoconfig.vo.res;

import coffee.model.LimitNumber;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class LimitNumberRes {
    private Long limitNumberId;
    private String limitNumberCode;
    private String lottoClassCode;
    private String lottoNumber;
    private BigDecimal lottoPrice;
    private Boolean enable;
    private Boolean isManual;
    private String createdBy;
    private Date createdAt;
    private String updatedBy;
    private Date updatedAt;
    private String msdLottoKindCode;

    public void setEntityToRes(LimitNumber entity) {
        this.limitNumberId = entity.getLimitNumberId();
        this.limitNumberCode = entity.getLimitNumberCode();
        this.lottoClassCode = entity.getLottoClassCode();
        this.lottoNumber = entity.getLottoNumber();
        this.enable = entity.getEnable();
        this.isManual = entity.getIsManual();
        this.createdBy = entity.getCreatedBy();
        this.createdAt = entity.getCreatedAt();
        this.updatedBy = entity.getUpdatedBy();
        this.updatedAt = entity.getUpdatedAt();
        this.lottoPrice = entity.getLottoPrice();
        this.msdLottoKindCode = entity.getMsdLottoKindCode();
    }
}
