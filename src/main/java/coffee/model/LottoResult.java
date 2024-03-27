package coffee.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import framework.constant.LottoConstant;
import framework.utils.GenerateRandomString;
import lombok.Data;

@Data
@Entity
@Table(name = "lotto_result")
public class LottoResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6831805400376746149L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "lotto_result_id")
	private Long lottoResultId;

	@Column(name = "lotto_result_code")
	private String lottoResultCode = GenerateRandomString.generateUUID();

	@Column(name = "msd_lotto_kind_code")
	private String msdLottoKindCode;

	@Column(name = "lotto_result_installment")
	private String lottoResultInstallment;
	
	@Column(name = "lotto_category_code")
	private String lottoCategoryCode;

	@Column(name = "lotto_class_code")
	private String lottoClassCode;

	@Column(name = "lotto_number")
	private String lottoNumber;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "type_installment")
	private String typeInstallment;

	@Column(name = "created_at")
	private Date createdAt = new Date();
	
	@Column(name = "updated_at")
	private Date updatedAt;

	@Column(name = "updated_by")
	private String updatedBy;
	
	@Column(name = "status")
	private String status = LottoConstant.LOTTO_STATUS.PENDING;
	
	@Column(name = "code_group")
	private String codeGroup;
}
