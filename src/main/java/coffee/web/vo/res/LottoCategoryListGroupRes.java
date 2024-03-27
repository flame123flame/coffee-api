package coffee.web.vo.res;

import java.util.Date;
import java.util.List;
import coffee.web.vo.res.LottoCategoryListRes.LottoCategoryListDetail;
import lombok.Data;

@Data
public class LottoCategoryListGroupRes {
    private Long timeStampServer = new Date().getTime();
    private List<LottoCategoryListDetail> lottoHit;
    private List<LottoCategoryListDetail> lottoGroupStocks;
    private List<LottoCategoryListDetail> lottoSet;
}
