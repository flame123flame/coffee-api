package coffee.repo.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import coffee.dashboard.vo.res.DashBroardMainRes;
import coffee.dashboard.vo.res.DashBroardMainRes.LottoSwappedSum;
import coffee.dashboard.vo.res.SumPrizeKindCode;
import coffee.dashboard.vo.res.SumPrizeList;
import coffee.dashboard.vo.res.SumPrizeRes;
import coffee.model.SumPrize;
import framework.utils.CommonJdbcTemplate;

@Repository
public class SumPrizeDao {

	@Autowired
	private CommonJdbcTemplate commonJdbcTemplate;

	public DashBroardMainRes getDashboard(String classCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sqlBuilder.append(" SELECT SUM(SP.SUM_PAY_COST) AS SUM_BET, SP.LOTTO_CLASS_CODE, LC.CLASS_NAME ");
		sqlBuilder.append(" 	FROM SUM_PRIZE SP JOIN LOTTO_CLASS LC ");
		sqlBuilder.append(" ON SP.LOTTO_CLASS_CODE = LC.LOTTO_CLASS_CODE ");
		sqlBuilder.append(" 	WHERE SP.LOTTO_CLASS_CODE = ? GROUP BY  SP.LOTTO_CLASS_CODE, LC.CLASS_NAME ");
		params.add(classCode);
		DashBroardMainRes dataRes = commonJdbcTemplate.executeQueryForObject(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(DashBroardMainRes.class));
		return dataRes;
	}

	public BigDecimal sumBetKind(String kindCode, String classCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sqlBuilder.append(" SELECT SUM(SP.SUM_PAY_COST) ");
		sqlBuilder.append(" 	FROM SUM_PRIZE SP ");
		sqlBuilder.append(" WHERE SP.LOTTO_CLASS_CODE = ? ");
		params.add(classCode);

		sqlBuilder.append(" AND SP.MSD_LOTTO_KIND_CODE = ? ");
		params.add(kindCode);

		BigDecimal dataRes = commonJdbcTemplate.executeQueryForObject(sqlBuilder.toString(), params.toArray(),
				BigDecimal.class);
		return dataRes;
	}

	public List<SumPrize> getTopSumPrize(String kindCode, String classCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sqlBuilder.append(" SELECT TOP 10 SP.* FROM SUM_PRIZE SP ");
		sqlBuilder.append(" WHERE SP.MSD_LOTTO_KIND_CODE = ? ");
		params.add(kindCode);

		sqlBuilder.append(" AND SP.LOTTO_CLASS_CODE = ? ");
		params.add(classCode);

		sqlBuilder.append(" ORDER BY SP.SUM_PRIZE_COST DESC ");
		List<SumPrize> dataRes = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(SumPrize.class));
		return dataRes;
	}

	public List<SumPrizeList> getTopPrizeAndlimit(String kindCode, String classCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sqlBuilder.append(
				" SELECT TOP 10 SP.*,lgd.tier_level from sum_prize sp left join limit_number lmn on sp.lotto_number = lmn.lotto_number    ");
		sqlBuilder.append(" and  sp.msd_lotto_kind_code = lmn.msd_lotto_kind_code  ");
		sqlBuilder.append(" and  sp.lotto_class_code = lmn.lotto_class_code   ");
		sqlBuilder.append(" left join lotto_group_dtl lgd on lmn.lotto_group_dtl_code = lgd.lotto_group_dtl_code ");
		sqlBuilder.append(" where sp.msd_lotto_kind_code = ?  ");
		sqlBuilder.append(" and SP.lotto_class_code = ?  ");
		sqlBuilder.append(" ORDER BY SP.SUM_PRIZE_COST DESC  ");

		params.add(kindCode);
		params.add(classCode);
		List<SumPrizeList> dataRes = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(SumPrizeList.class));
		return dataRes;
	}

	public List<SumPrizeList> getTopPrizeAndlimit3Top(String kindCode, String classCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sqlBuilder.append("  SELECT ");
		sqlBuilder.append("  	TOP 10 sp.sum_prize_id, ");
		sqlBuilder.append("  	sp.sum_prize_code, ");
		sqlBuilder.append("  	sp.lotto_class_code, ");
		sqlBuilder.append("  	sp.msd_lotto_kind_code, ");
		sqlBuilder.append("  	sp.just_sum_prize as sum_prize_cost, ");
		sqlBuilder.append("  	sp.sum_plus_swapped, ");
		sqlBuilder.append("  	sp.installment, ");
		sqlBuilder.append("  	sp.lotto_number, ");
		sqlBuilder.append("  	sp.created_by, ");
		sqlBuilder.append("  	sp.created_at, ");
		sqlBuilder.append("  	sp.updated_by, ");
		sqlBuilder.append("  	sp.updated_at, ");
		sqlBuilder.append("  	sp.sum_pay_cost, ");
		sqlBuilder.append("  	sp.group_swapped_code, ");
		sqlBuilder.append("  	lgd.tier_level ");
		sqlBuilder.append("  from ");
		sqlBuilder.append("  	sum_prize sp ");
		sqlBuilder.append("  left join limit_number lmn on ");
		sqlBuilder.append("  	sp.lotto_number = lmn.lotto_number ");
		sqlBuilder.append("  	and sp.msd_lotto_kind_code = lmn.msd_lotto_kind_code ");
		sqlBuilder.append("  	and sp.lotto_class_code = lmn.lotto_class_code ");
		sqlBuilder.append("  left join lotto_group_dtl lgd on ");
		sqlBuilder.append("  	lmn.lotto_group_dtl_code = lgd.lotto_group_dtl_code ");
		sqlBuilder.append("  where ");
		sqlBuilder.append("  	sp.msd_lotto_kind_code = ? ");
		sqlBuilder.append("  	and SP.lotto_class_code = ? ");
		sqlBuilder.append("  ORDER BY ");
		sqlBuilder.append("  	SP.just_sum_prize DESC ");
		params.add(kindCode);
		params.add(classCode);
		List<SumPrizeList> dataRes = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(SumPrizeList.class));
		return dataRes;
	}

	public SumPrizeList getPrizeByNumber(String kindCode, String classCode, String number) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sqlBuilder.append(" SELECT SP.*,lgd.tier_level    ");
		sqlBuilder.append(" from sum_prize sp  ");
		sqlBuilder.append(" left join limit_number lmn on sp.lotto_number = lmn.lotto_number  ");
		sqlBuilder.append(" and sp.msd_lotto_kind_code = lmn.msd_lotto_kind_code ");
		sqlBuilder.append(" and sp.lotto_class_code = lmn.lotto_class_code  ");
		sqlBuilder.append(" left join lotto_group_dtl lgd on lmn.lotto_group_dtl_code = lgd.lotto_group_dtl_code ");
		sqlBuilder.append(" where sp.msd_lotto_kind_code = ? ");
		sqlBuilder.append(" and SP.lotto_class_code = ? ");
		sqlBuilder.append(" and SP.lotto_number = ? ");
		sqlBuilder.append(" ORDER BY SP.SUM_PRIZE_COST DESC ");

		params.add(kindCode);
		params.add(classCode);
		params.add(number);
		SumPrizeList dataRes = commonJdbcTemplate.executeQueryForObject(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(SumPrizeList.class));
		return dataRes;
	}

	public List<LottoSwappedSum> getPrizeLottoSwapped(String kindCode, String classCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sqlBuilder.append(
				" SELECT TOP 10 sp.group_swapped_code, sp.msd_lotto_kind_code, lgd.tier_level,sp.sum_prize_cost, sum(sp.sum_pay_cost) as sum_pay_cost, STRING_AGG(sp.lotto_number,',') as lotto_number ");
		sqlBuilder.append(" from sum_prize sp  ");
		sqlBuilder.append(
				" left join limit_number lmn on sp.lotto_number = lmn.lotto_number and sp.msd_lotto_kind_code = lmn.msd_lotto_kind_code and sp.lotto_class_code = lmn.lotto_class_code  ");
		sqlBuilder.append(" left join lotto_group_dtl lgd on lmn.lotto_group_dtl_code = lgd.lotto_group_dtl_code  ");
		sqlBuilder.append(" where sp.msd_lotto_kind_code = ? and sp.lotto_class_code = ? ");
		sqlBuilder.append(
				" group by sp.group_swapped_code,sp.sum_prize_cost,lgd.tier_level,sp.msd_lotto_kind_code ORDER BY sp.sum_prize_cost DESC");
		params.add(kindCode);
		params.add(classCode);
		List<LottoSwappedSum> dataRes = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(LottoSwappedSum.class));
		return dataRes;
	}

	public List<SumPrizeRes> getSumPrize(String lottoClassCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sqlBuilder.append(
				" SELECT MAX(sp.sum_prize_cost) as sum_prize_cost,sp.msd_lotto_kind_code,SUM(sum_pay_cost) as sum_pay_cost, mlk.msd_lotto_kind_name  ");
		sqlBuilder.append(" FROM sum_prize sp ");
		sqlBuilder.append(" inner join msd_lotto_kind mlk on mlk.msd_lotto_kind_code = sp.msd_lotto_kind_code  ");
		sqlBuilder.append(
				" where lotto_class_code = ? group by sp.msd_lotto_kind_code,mlk.msd_lotto_kind_name ORDER By msd_lotto_kind_code desc");
		params.add(lottoClassCode);

		List<SumPrizeRes> dataRes = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				rowMapperSumPrize);
		return dataRes;
	}

	public List<SumPrizeKindCode> getSumPrizeByCode(String lottoClassCode, String kindCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sqlBuilder.append(
				" SELECT TOP 2 sp.sum_prize_cost,mlk.msd_lotto_kind_name,mlk.msd_lotto_kind_code,sp.sum_pay_cost FROM sum_prize sp ");
		sqlBuilder.append(" inner join msd_lotto_kind mlk on mlk.msd_lotto_kind_code = sp.msd_lotto_kind_code ");
		sqlBuilder
				.append(" where lotto_class_code = ? AND sp.msd_lotto_kind_code = ? ORDER BY sp.sum_prize_cost DESC ");
		params.add(lottoClassCode);
		params.add(kindCode);
		List<SumPrizeKindCode> dataRes = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(SumPrizeKindCode.class));
		return dataRes;
	}

	private RowMapper<SumPrizeRes> rowMapperSumPrize = new RowMapper<SumPrizeRes>() {
		@Override
		public SumPrizeRes mapRow(ResultSet rs, int arg1) throws SQLException {
			SumPrizeRes vo = new SumPrizeRes();
			vo.setSumPayCost(rs.getBigDecimal("sum_pay_cost"));
			vo.setSumPrizeCost(rs.getBigDecimal("sum_prize_cost"));
			vo.setMsdLottoKindName(rs.getString("msd_lotto_kind_name"));
			vo.setMsdLottoKindCode(rs.getString("msd_lotto_kind_code"));
			return vo;
		}
	};
}
