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
@Table(name = "lotto_transaction")
public class LottoTransaction implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1369109124553418526L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	@Column(name = "lotto_transaction_id")
	private Long lottoTransactionId;

	@Column(name = "lotto_transaction_code")
	private String lottoTransactionCode;

	@Column(name = "lotto_group_transaction_code")
	private String lottoGroupTransactionCode;

	@Column(name = "username")
	private String username;

	@Column(name = "lotto_class_code")
	private String lottoClassCode;

	@Column(name = "lotto_kind_code")
	private String lottoKindCode;

	@Column(name = "pay_cost")
	private BigDecimal payCost;

	@Column(name = "prize_cost")
	private BigDecimal prizeCost;

	@Column(name = "is_limit")
	private Boolean isLimit = false;

	@Column(name = "has_won")
	private Boolean hasWon;

	@Column(name = "lotto_number")
	private String lottoNumber;
	
	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_at")
	private Date createdAt = new Date();

	@Column(name = "paid_by")
	private String paidBy;

	@Column(name = "paid_at")
	private Date paidAt;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "update_wallet")
	private Boolean updateWallet;
	
	@Column(name = "installment")
	private String installment;
	
	@Column(name = "number_correct")
	private String numberCorrect;
	
	@Column(name = "prize_correct")
	private BigDecimal prizeCorrect;
	
	@Column(name = "count_seq")
	private Integer countSeq;
	
	@Column(name = "round_yeekee")
	private Integer roundYeekee;
	
	@Column(name = "reject_remark")
	private String rejectRemark;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "updated_by")
	private String updatedBy;
}
