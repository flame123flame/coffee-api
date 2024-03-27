package coffee.lottoresult.vo.req;

import java.util.List;

import lombok.Data;

@Data
public class ConfirmResultReq {
	private Boolean status;
	private String remark;
	private String kindCode;
	private String classCode;
	private String categoryCode;
	private String username;
	private String transactionCode;
	private String transactionId;
	private List<String> detransactionIdList;
	private List<String> transactionIdList;
}
