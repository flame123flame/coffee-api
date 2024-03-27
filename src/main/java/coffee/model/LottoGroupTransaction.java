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
@Table(name = "lotto_group_transaction")
public class LottoGroupTransaction implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1715529499614323320L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "lotto_group_transaction_id")
	private Long lottoGroupTransactionId;
	
	@Column(name = "lotto_group_transaction_code")
	private String lottoGroupTransactionCode;
	
	@Column(name = "lotto_class_code")
	private String lottoClassCode;
	
	@Column(name = "username")
	private String username;
	
	@Column(name = "sum_group_bet")
	private BigDecimal sumGroupBet;
	
	@Column(name = "sum_group_prize")
	private BigDecimal sumGroupPrize;
	
	@Column(name = "round_yeekee")
	private Integer roundYeekee;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "created_at")
	private Date createdAt = new Date();

	@Column(name = "status")
	private String status;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "installment")
	private String installment;
	
	@Column(name = "updated_by")
	private String updatedBy;
	
	@Column(name = "updated_at")
	private Date updatedAt;
}
