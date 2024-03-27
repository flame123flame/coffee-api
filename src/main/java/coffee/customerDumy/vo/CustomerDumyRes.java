package coffee.customerDumy.vo;

import java.util.Date;

import lombok.Data;

@Data
public class CustomerDumyRes {
	private String dumyCode;
	private String username;
	private String createdBy;
	private Date createdDate;

}
