package coffee.lottocancel.vo.req;


import lombok.Data;
@Data
public class LottoCancelReq {
	private String lottoCancelCode;
	private String lottoCategoryCode;
	private String lottoClassCode;
	private String installment;
	private Integer roundYeekee;

}
