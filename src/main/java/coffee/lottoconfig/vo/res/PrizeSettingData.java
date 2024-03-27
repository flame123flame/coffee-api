package coffee.lottoconfig.vo.res;

import java.math.BigDecimal;
import java.util.List;

import coffee.model.GroupMaxMinMap;
import coffee.model.LottoGroupDtl;
import lombok.Data;

@Data
public class PrizeSettingData {
	
	List<GroupMaxMinMap> groupMaxmin;
	List<LottoGroupDtl> lottoGroup;

}
