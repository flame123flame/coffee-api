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

@Entity
@Data
@Table(name = "yeekee_submit_number")
public class YeekeeSubmitNumber implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 7203070421401608602L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "yeekee_submit_number_id")
	private Long yeekeeSubmitNumberId;

	@Column(name = "yeekee_submit_number_code", length = 40, nullable = false)
	private String yeekeeSubmitNumberCode = GenerateRandomString.generateUUID();

	@Column(name = "number_submit", length = 40, nullable = false)
	private Long numberSubmit;

	@Column(name = "class_code", nullable = false)
	private String classCode;

	@Column(name = "is_bot", nullable = false)
	private Boolean isBot = false;

	@Column(name = "installment", nullable = false)
	private String installment;

	@Column(name = "round_number", nullable = false)
	private Integer roundNumber;

	@Column(name = "seq_order")
	private Integer seqOrder = 0;

	@Column(name = "created_by", length = 255, nullable = false)
	private String createdBy;

	@Column(name = "created_at", nullable = false)
	private Date createdAt = new Date();

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_at")
	private Date updatedAt;

}
