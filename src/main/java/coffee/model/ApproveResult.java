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
@Table(name = "approve_result")
public class ApproveResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7354018532753830613L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "approve_result_id")
	private Long approveResultId;

	@Column(name = "approve_result_code")
	private String approveResultCode = GenerateRandomString.generateUUID();

	@Column(name = "lotto_class_code")
	private String lottoClassCode;

	@Column(name = "installment")
	private String installment;

	@Column(name = "username")
	private String username;

	@Column(name = "is_approve")
	private Boolean isApprove;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_at")
	private Date createdAt = new Date();

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_at")
	private Date updatedAt;
	
	@Column(name = "code_group")
	private String codeGroup;

}
