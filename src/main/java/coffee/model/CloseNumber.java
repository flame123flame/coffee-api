package coffee.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import framework.utils.GenerateRandomString;
import lombok.Data;

@Entity
@Table(name = "close_number")
@Data
public class CloseNumber implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2656248762253240959L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "close_number_id")
    private Long closeNumberId;

    @Column(name = "close_number_code")
    private String closeNumberCode = GenerateRandomString.generateUUID();

    @Column(name = "lotto_class_code")
    private String lottoClassCode;

    @Column(name = "lotto_number")
    private String lottoNumber;

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

    @Column(name = "swapped_group_code")
    private String swappedGroupCode;

}
