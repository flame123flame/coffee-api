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

import lombok.Data;

@Data
@Entity
@Table(name = "lotto_group_dtl")
public class LottoGroupDtl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3189212579136924744L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "lotto_group_dtl_id")
	private Long lottoGroupDtlId;

	@Column(name = "lotto_group_dtl_code", length = 40, nullable = false)
	private String lottoGroupDtlCode;

	@Column(name = "lotto_group_code", length = 40, nullable = false)
	private String lottoGroupCode;

	@Column(name = "group_max_risk")
	private BigDecimal groupMaxRisk;

	@Column(name = "percent_for_limit")
	private Integer percentForLimit;

	@Column(name = "created_by", length = 255, nullable = false)
	private String createdBy;

	@Column(name = "created_at")
	private Date createdAt = new Date();

	@Column(name = "updated_by", length = 255)
	private String updatedBy;

	@Column(name = "updated_at")
	private Date updatedAt;

	@Column(name = "tier_level")
	private Integer tierLevel;

}
