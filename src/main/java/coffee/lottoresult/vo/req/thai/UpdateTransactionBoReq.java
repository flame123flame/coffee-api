package coffee.lottoresult.vo.req.thai;

import java.util.Date;
import java.util.List;

import coffee.model.LottoTransaction;
import lombok.Data;

@Data
public class UpdateTransactionBoReq {

	private String message;
	private String status;
	private List<LottoTransaction> data;

}