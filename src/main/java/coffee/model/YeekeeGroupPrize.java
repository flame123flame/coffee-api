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

import framework.utils.GenerateRandomString;
import lombok.Data;

@Entity
@Table(name = "yeekee_group_prize")
@Data
public class YeekeeGroupPrize implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2478708189435265637L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "yeekee_group_prize_id")
	private Long yeekeeGroupPrizeId;

	@Column(name = "yeekee_group_prize_code", length = 40, nullable = false)
	private String yeekeeGroupPrizeCode = GenerateRandomString.generateUUID();

	@Column(name = "class_code", nullable = false)
	private String classCode;

	@Column(name = "sum_prize", nullable = false)
	private BigDecimal sumPrize = BigDecimal.ZERO;

	@Column(name = "lotto_number", nullable = false)
	private String lottoNumber;

	@Column(name = "kind_code", nullable = false)
	private String kindCode;

	@Column(name = "round_yeekee", nullable = false)
	private Integer roundYeekee;

	@Column(name = "installment", nullable = false)
	private String installment;

	@Column(name = "created_by", length = 255, nullable = false)
	private String createdBy;

	@Column(name = "created_at", nullable = false)
	private Date createdAt = new Date();

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_at")
	private Date updatedAt;

	@Column(name = "sum_bet")
	private BigDecimal sumBet = BigDecimal.ZERO;

	@Column(name = "count_user")
	private Integer countUser = 1;

}
