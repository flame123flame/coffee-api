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
@Table(name = "sum_bet_group")
@Data
public class SumBetGroup implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4023031061359324638L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "sum_bet_group_id")
	private Long sumBetGroupId;

	@Column(name = "sum_bet_group_code")
	private String sumBetGroupCode = GenerateRandomString.generateUUID();

	@Column(name = "lotto_class_code")
	private String lottoClassCode;

	@Column(name = "lotto_group_code")
	private String lottoGroupCode;
	
	@Column(name = "installments")
	private String installments;

	@Column(name = "sum_bet")
	private BigDecimal sumBet = BigDecimal.ZERO;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_at")
	private Date createdAt = new Date();
	
	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "updated_at")
	private Date updatedAt;

}
