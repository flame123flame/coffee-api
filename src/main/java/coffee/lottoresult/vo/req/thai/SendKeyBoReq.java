package coffee.lottoresult.vo.req.thai;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class SendKeyBoReq {

	private final String key;
	private final List<SendBoReq> data;

	@Data
	public static class SendBoReq {
		private String username;
		private String prize;
	}

}