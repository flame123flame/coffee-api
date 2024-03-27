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

@Entity
@Table(name = "prize_setting")
@Data
public class PrizeSetting implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -1744945046845900370L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "prize_setting_id")
	private Long prizeSettingId;

	@Column(name = "prize_setting_code", length = 40, nullable = false)
	private String prizeSettingCode;

	@Column(name = "lotto_group_code", length = 40)
	private String lottoGroupCode;

	@Column(name = "vip_code")
	private String vipCode;

	@Column(name = "msd_lotto_kind_code", length = 40, nullable = false)
	private String msdLottoKindCode;

	@Column(name = "prize")
	private BigDecimal prize;

	@Column(name = "prize_limit")
	private BigDecimal prizeLimit;

	@Column(name = "created_by", length = 255, nullable = false)
	private String createdBy;

	@Column(name = "created_at", nullable = false)
	private Date createdAt = new Date();

	@Column(name = "updated_by", length = 255)
	private String updatedBy;

	@Column(name = "updated_at")
	private Date updatedAt;

	@Column(name = "lotto_group_dtl_code")
	private String lottoGroupDtlCode;

	@Column(name = "seq_order")
	private Integer seqOrder;
	
	@Column(name = "class_code")
	private String classCode;
}
