package coffee.lottoresult.vo.res.thai;
import java.util.List;

import lombok.Data;
@Data
public class LottoAllResultRes {
    private List<DigiList> digi3Top;
    private List<DigiList> digi1Top;
    private List<DigiList> digiSwap;
    private List<DigiList> digi3Front;
    private List<DigiList> digi3Bot;
    private List<DigiList> digi2Top;
    private List<DigiList> digi2Bot;
    private List<DigiList> digi1Bot;
   
    @Data
	public static class DigiList {
    	
		private String msdlottoKindCode;
		private String msdlottoKindName;
		private String lottoNumber;
		private Integer roundYekee;
	}
    
}
