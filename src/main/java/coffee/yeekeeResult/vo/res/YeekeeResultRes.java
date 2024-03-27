package coffee.yeekeeResult.vo.res;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class YeekeeResultRes {
	private List<DigiListYeekee> digi3Top;
	private List<DigiListYeekee> digiSwap;
    private List<DigiListYeekee> digi1Top;
    private List<DigiListYeekee> digi2Top;
    private List<DigiListYeekee> digi1Bot;
    private List<DigiListYeekee> digi2Bot;
   
    @Data
	public static class DigiListYeekee {
		private String lottoNumber;
		private BigDecimal prize; 
	}
}
