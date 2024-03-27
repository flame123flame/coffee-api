package coffee.buy.vo.res;

import java.util.List;

import coffee.buy.vo.req.BuyLottoReq.PayNumber;
import coffee.buy.vo.res.BuyLottoRes.BuyDetail;
import lombok.Data;

@Data
public class LoopPrizeRes {
	private final List<BuyDetail> errList;
	private final List<BuyDetail> sccList;
	private final List<PayNumber> payNumber;
}
