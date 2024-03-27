package coffee.lottoconfig.vo.req.addgroup;

import java.util.List;

import coffee.lottoconfig.vo.res.PrizeSettingRes;
import lombok.Data;

@Data
public class PrizeReq {
	
	private List<PrizeSettingRes> prizeListSetting;
}
