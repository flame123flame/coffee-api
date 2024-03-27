package coffee.yeekeeResult.vo.req;

import lombok.Data;

@Data
public class YeekeeResultReq {
	private Long sumNumber;
	private Long numberNo16;
	private Long lottoResult;
	private String digitBot;
    private String digitTop;
    private String installment;
    private String lottoClassCode;
    private Integer roundNumber;
}
