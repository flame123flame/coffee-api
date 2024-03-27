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
@Table(name = "msd_lotto_kind")
public class MsdLottoKind implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 5502773414928076733L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "msd_lotto_kind_id")
	private Long msdLottoKindId;

	@Column(name = "msd_lotto_kind_Code",unique = true,length = 40,nullable = false)
	private String msdLottoKindCode;

	@Column(name = "msd_lotto_kind_name")
	private String msdLottoKindName;
 
	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_at")
	private Date createdAt = new Date();

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_at")
	private Date updatedAt;
	
	@Column(name = "require_digit")
	private Long requireDigit;


}
