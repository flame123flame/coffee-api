package framework.security.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "fw_user")
@Data
public class FwUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -725000429402385783L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = " fw_users_id")
	private Long fwUsersId;

	@Column(name = "username", unique = true, nullable = false)
	private String username;

	@Column(name = "password", nullable = false)
	private String password;
	
}
