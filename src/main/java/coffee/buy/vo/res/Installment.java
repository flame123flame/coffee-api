package coffee.buy.vo.res;

import java.util.Date;

import lombok.Data;

@Data
public class Installment {

	private Date timeOpen;
	private Date timeClose;
	private String timeOpenStr;
	private String timeCloseStr;
	private Integer count = 0;
	private Integer lengthTimeSell = 0;
}
