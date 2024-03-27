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
@Data
@Table(name = "yeekee_prize_seq_mapping")
public class YeekeePrizeSeqMapping implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3333175948102924420L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "yeekee_prize_seq_mapping_id")
    private Long yeekeePrizeSeqMappingId;

    @Column(name = "yeekee_prize_seq_mapping_code", length = 40, nullable = false)
    private String yeekeePrizeSeqMappingCode = GenerateRandomString.generateUUID();

    @Column(name = "class_code", nullable = false)
    private String classCode;

    @Column(name = "seq_order")
    private Integer seqOrder = 0;

    @Column(name = "prize")
    private BigDecimal prize;

    @Column(name = "created_by", length = 255, nullable = false)
    private String createdBy;

    @Column(name = "created_at", nullable = false)
    private Date createdAt = new Date();

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    private Date updatedAt;

}