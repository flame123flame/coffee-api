package coffee.lottocancel.vo.res;

import java.util.Date;

import lombok.Data;

@Data
public class LottoCancelRes {
	
	private Long lottoCancelId;
	private String lottoCancelCode;
	private String lottoCategoryCode;
	private String lottoClassCode;
	private String installment;
	private Integer roundYeekee;
	private String createdBy;
	private Date createdAt;
	private String updatedBy;
	private Date updatedAt;

}
