package coffee.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import framework.utils.GenerateRandomString;
import lombok.Data;

@Data
@Entity
@Table(name = "lotto_cancel")
public class LottoCancel implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = -8987635717361649080L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "lotto_cancel_id")
	private Long lottoCancelId;
	
	@Column(name = "lotto_cancel_code")
	private String lottoCancelCode = GenerateRandomString.generateUUID();
	
	@Column(name = "lotto_category_code")
	private String lottoCategoryCode;
	
	@Column(name = "lotto_class_code")
	private String lottoClassCode;
	
	@Column(name = "installment")
	private String installment;
	
	@Column(name = "round_yeekee")
	private Integer roundYeekee;
	
	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_at")
	private Date createdAt = new Date();

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_at")
	private Date updatedAt;

}
