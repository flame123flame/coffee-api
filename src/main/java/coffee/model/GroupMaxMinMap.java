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
@Table(name = "group_max_min_map")
@Data
public class GroupMaxMinMap implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = -8108025103342522367L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "group_max_min_map_id")
	private Long groupMaxMinMapId;
	
	@Column(name = "group_max_min_map_code", length = 40, nullable = false)
	private String groupMaxMinMapCode;

	@Column(name = "lotto_class_code", length = 40, nullable = false)
	private String lottoClassCode;

	@Column(name = "vip_code")
	private String vipCode;

	@Column(name = "msd_lotto_kind_code", length = 40, nullable = false)
	private String msdLottoKindCode;

	@Column(name = "minimum_per_trans")
	private BigDecimal minimumPerTrans;

	@Column(name = "maximum_per_trans")
	private BigDecimal maximumPerTrans;
	
	@Column(name = "maximum_per_user")
	private BigDecimal maximumPerUser;
	
	@Column(name = "lotto_group_code")
	private String lottoGroupCode;

	@Column(name = "created_by", length = 255, nullable = false)
	private String createdBy;

	@Column(name = "created_at", nullable = false)
	private Date createdAt = new Date();

	@Column(name = "updated_by", length = 255)
	private String updatedBy;

	@Column(name = "updated_at")
	private Date updatedAt;

}
