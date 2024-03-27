package coffee.webresult.vo;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class LottoWebResultRes {
	
	private String lottoClassName;
	private String lottoClassCode;
	private String lottoCategoryCode;
	private String installment;
	private Date timeOpenStr;
	private Date timeCloseStr;
	private String lottoFlag;
	private String digit3Top;
	private List<String> digit3Front;
	private List<String> digit3Bot;
	private String digit2Bot;
	
	
}
