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

@Entity
@Table(name = "rule_config")
@Data
public class RuleConfig implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 8021767605764966318L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "rule_config_id")
	private Long ruleConfigId;
	
	@Column(name = "rule_config_code")
	private String ruleConfigCode;
	
	@Column(name = "class_code")
	private String classCode;
	
	@Column(name = "ruleDes")
	private String ruleDes;
	
	@Column(name = "image_path")
	private String imagePath;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "created_at")
	private Date createdAt;
	
	@Column(name = "updated_by")
	private String updatedBy;
	
	@Column(name = "updated_at")
	private Date updatedAt;
	
	
}
