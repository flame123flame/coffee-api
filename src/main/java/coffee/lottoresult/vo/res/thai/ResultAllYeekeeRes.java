package coffee.lottoresult.vo.res.thai;

import java.util.List;

import coffee.lottoresult.vo.res.thai.LottoAllResultRes.DigiList;
import lombok.Data;

@Data
public class ResultAllYeekeeRes {
	private String lottoClassName;
	private String lottoClassImg;
	private String lottoClassCode;
	private String installment;
	private String lottoCategoryCode;
	private List<DigiList> lottoList;
}
