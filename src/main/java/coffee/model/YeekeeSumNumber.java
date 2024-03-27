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
@Table(name = "yeekee_sum_number")
@Data
public class YeekeeSumNumber implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 9004909665711519380L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "yeekee_sum_number_id")
    private Long yeekeeSumNumberId;

    @Column(name = "yeekee_sum_number_code", length = 40, nullable = false)
    private String yeekeeSumNumberCode = GenerateRandomString.generateUUID();

    @Column(name = "installment", nullable = false)
    private String installment;

    @Column(name = "round_number", nullable = false)
    private Integer roundNumber;

    @Column(name = "sum_number", nullable = false)
    private BigDecimal sumNumber = BigDecimal.ZERO;

    @Column(name = "class_code", nullable = false)
    private String classCode;

    @Column(name = "created_by", length = 255, nullable = false)
    private String createdBy;

    @Column(name = "created_at", nullable = false)
    private Date createdAt = new Date();

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "status")
    private String status;
    
    @Column(name = "number_result")
    private BigDecimal numberResult;
    
    @Column(name = "number_seq16")
    private BigDecimal numberSeq16;
    
    @Column(name = "change_result")
    private Boolean changeResult;
    
    
}