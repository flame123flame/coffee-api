package coffee.model;

import java.io.Serializable;
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
@Table(name = "group_kind_map")
public class GroupKindMap  implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -7354018532753830613L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "group_kind_map_id")
	private Long lottoGroupDtlId;

	@Column(name = "group_kind_map_code")
	private String groupKindMapCode;

	@Column(name = "lotto_group_code")
	private String lottoGroupCode;

	@Column(name = "msd_lotto_kind_code")
	private String msdLottoKindCode;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_at")
	private Date createdAt = new Date();

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_at")
	private Date updatedAt;


}
