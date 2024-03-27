package coffee.lottoresult.vo.req.thai;

import java.util.List;

import lombok.Data;

@Data
public class SubmitManualReq {
	private String classCode;
	private String installment;
	private String digit3Top;
	private List<String> digit3Front;
	private List<String> digit3Bot;
	private String digit2Bot;
}
