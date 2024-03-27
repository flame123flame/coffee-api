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
@Table(name = "sum_prize")
@Data
public class SumPrize implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = -1744945046845900370L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sum_prize_id")
	private Long sumPrizeId;

	@Column(name = "sum_prize_code")
	private String sumPrizeCode = GenerateRandomString.generateUUID();

	@Column(name = "lotto_class_code")
	private String lottoClassCode;

	@Column(name = "msd_lotto_kind_code")
	private String msdLottoKindCode;

	@Column(name = "sum_prize_cost")
	private BigDecimal sumPrizeCost = BigDecimal.ZERO;

	@Column(name = "just_sum_prize")
	private BigDecimal justSumPrize = BigDecimal.ZERO;

	@Column(name = "sum_plus_swapped")
	private BigDecimal sumPlusSwapped = BigDecimal.ZERO;

	@Column(name = "installment")
	private Date installment;

	@Column(name = "lotto_number")
	private String lottoNumber;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_at")
	private Date createdAt = new Date();
	
	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_at")
	private Date updatedAt;

	@Column(name = "sum_pay_cost")
	private BigDecimal sumPayCost = BigDecimal.ZERO;
	
	@Column(name = "group_swapped_code")
	private String groupSwappedCode;
}
