package coffee.transaction.vo.req;

import java.util.Date;

import lombok.Data;

@Data
public class GetTransBoReq {
	private String username;
	private Date timeStart;
	private Date timeEnd;
}
