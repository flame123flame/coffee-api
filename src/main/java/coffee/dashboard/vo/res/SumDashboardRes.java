package coffee.dashboard.vo.res;

import java.util.List;

import lombok.Data;

@Data
public class SumDashboardRes {
	private String classCode;
	private List<SumPrizeRes> list;
}
