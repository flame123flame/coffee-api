package coffee.lottoresult.vo.req.thai;

import lombok.Data;

@Data
public class ApproveSaveReq {
	private String lottoClassCode;
	private String installment;
	private String codeGroup;
	private Boolean isApprove;
}
