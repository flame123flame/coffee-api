package coffee.model;

import coffee.lottoconfig.vo.req.LimitNumberReq;
import framework.utils.GenerateRandomString;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@Table(name = "limit_number")
public class LimitNumber implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -2671775413786048374L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "limit_number_id")
    private Long limitNumberId;

    @Column(name = "limit_number_code")
    private String limitNumberCode = GenerateRandomString.generateUUID();

    @Column(name = "lotto_class_code")
    private String lottoClassCode;

    @Column(name = "lotto_number")
    private String lottoNumber;

    @Column(name = "lotto_price")
    private BigDecimal lottoPrice;

    @Column(name = "enable")
    private Boolean enable;

    @Column(name = "is_manual")
    private Boolean isManual;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_at")
    private Date createdAt = new Date();

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "msd_lotto_kind_code")
    private String msdLottoKindCode;

    @Column(name = "lotto_group_dtl_code")
    private String lottoGroupDtlCode;

    @Column(name = "lotto_group_dtl_code_manual")
    private String lottoGroupDtlCodeManual;

    @Column(name = "swapped_group_code")
    private String swappedGroupCode;

    public void setReqToEntity(LimitNumberReq req) {
        this.limitNumberId = req.getLimitNumberId();
        this.limitNumberCode = req.getLimitNumberCode();
        this.lottoClassCode = req.getLottoClassCode();
        this.lottoNumber = req.getLottoNumber();
        this.enable = req.getEnable();
        this.isManual = req.getIsManual();
        this.createdBy = req.getCreatedBy();
        this.createdAt = req.getCreatedAt();
        this.updatedBy = req.getUpdatedBy();
        this.updatedAt = req.getUpdatedAt();
        this.msdLottoKindCode = req.getMsdLottoKindCode();
    }

}
