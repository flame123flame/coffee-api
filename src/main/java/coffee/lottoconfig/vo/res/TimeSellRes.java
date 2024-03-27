package coffee.lottoconfig.vo.res;

import java.util.Date;

import lombok.Data;

@Data
public class TimeSellRes {
	private Long timeSellId;
	private String timeSellCode;
	private String lottoClassCode;
	private Date timeOpen;
	private Date timeClose;
}
