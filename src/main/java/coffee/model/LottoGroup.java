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
@Table(name = "lotto_group")
public class LottoGroup  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2556047926948420953L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "lotto_group_id")
	private Long lottoGroupId;

	@Column(name = "lotto_group_code", length = 40, nullable = false)
	private String lottoGroupCode;

	@Column(name = "lotto_class_code", length = 40, nullable = false)
	private String lottoClassCode;
	
	@Column(name = "group_name", length = 255, nullable = false)
	private String groupName;

	@Column(name = "created_by", length = 255, nullable = false)
	private String createdBy;

	@Column(name = "created_at")
	private Date createdAt = new Date();

	@Column(name = "updated_by", length = 255)
	private String updatedBy;

	@Column(name = "updated_at")
	private Date updatedAt;
	
	
	@Column(name = "group_max_close")
	private BigDecimal groupMaxClose;
	
	@Column(name = "group_earnings_percent")
	private Integer groupEarningsPercent;
	
	@Column(name = "group_earnings_percent_close")
	private Integer groupEarningsPercentClose;
	
}
