package coffee.buy.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import coffee.buy.vo.res.CheckCostUserRes;
import framework.utils.CommonJdbcTemplate;
import framework.utils.ConvertDateUtils;

@Repository
public class BuyLottoDao {

	@Autowired
	private CommonJdbcTemplate commonJdbcTemplate;

	public List<CheckCostUserRes> checkSumUser(String uesrname, Date startDate, Date endDate) {
		String startDateStr = ConvertDateUtils.formatDateToString(startDate, ConvertDateUtils.YYYY_MM_DD_HH_MM_SS,
				ConvertDateUtils.LOCAL_EN);
		String endDateStr = ConvertDateUtils.formatDateToString(endDate, ConvertDateUtils.YYYY_MM_DD_HH_MM_SS,
				ConvertDateUtils.LOCAL_EN);
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT ");
		sqlBuilder.append(" 	lt.lotto_kind_code, ");
		sqlBuilder.append(" 	sum(lt.pay_cost) as sum_cost ");
		sqlBuilder.append(" from ");
		sqlBuilder.append(" 	lotto_transaction lt ");
		sqlBuilder.append(" where ");
		sqlBuilder.append(" 	lt.username  = ? ");
		params.add(uesrname);

		sqlBuilder.append(" 	and lt.created_at between ?  ");
		params.add(startDate);
		sqlBuilder.append("         and ? ");
		params.add(endDate);

		sqlBuilder.append(" group by lt.lotto_kind_code ");
		List<CheckCostUserRes> res = this.commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(CheckCostUserRes.class));
		return res;
	}

	public CheckCostUserRes checkSumUserPerNum(String username, Date startDate, Date endDate, String lottoNumber,
			String lottoKind, String classCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT ");
		sqlBuilder.append(" 	lt.lotto_kind_code, ");
		sqlBuilder.append(" 	sum(lt.pay_cost) as sum_cost ");
		sqlBuilder.append(" from ");
		sqlBuilder.append(" 	lotto_transaction lt ");
		sqlBuilder.append(" where ");

		sqlBuilder.append(" 	 lt.lotto_class_code  = ? ");
		params.add(classCode);

		sqlBuilder.append("		and lt.lotto_kind_code = ? ");
		params.add(lottoKind);

		sqlBuilder.append(" 	and lt.created_at between ? ");
		params.add(startDate);
		sqlBuilder.append("         and ? ");
		params.add(endDate);

		sqlBuilder.append(" 	and lt.username  = ? ");
		params.add(username);

		sqlBuilder.append(" 	and lt.lotto_number  = ? ");
		params.add(lottoNumber);

		sqlBuilder.append(" group by lt.lotto_kind_code ");
		CheckCostUserRes res = this.commonJdbcTemplate.executeQueryForObject(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(CheckCostUserRes.class));
		return res;
	}

	public CheckCostUserRes checkSumUserPerNumYeekee(String username, Date startDate, Date endDate, String lottoNumber,
			String lottoKind, String classCode, Integer round) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT ");
		sqlBuilder.append(" 	lt.lotto_kind_code, ");
		sqlBuilder.append(" 	sum(lt.pay_cost) as sum_cost ");
		sqlBuilder.append(" from ");
		sqlBuilder.append(" 	lotto_transaction lt ");
		sqlBuilder.append(" where ");

		sqlBuilder.append(" 	lt.lotto_class_code  = ? ");
		params.add(classCode);

		sqlBuilder.append("		and lt.lotto_kind_code = ? ");
		params.add(lottoKind);

		sqlBuilder.append(" 	and lt.lotto_number  = ? ");
		params.add(lottoNumber);

		sqlBuilder.append("  and	 lt.created_at between ? ");
		params.add(startDate);
		sqlBuilder.append("         and ? ");
		params.add(endDate);

		sqlBuilder.append(" 	and lt.username  = ? ");
		params.add(username);

		sqlBuilder.append(" 	and lt.round_yeekee  = ? ");
		params.add(round);

		sqlBuilder.append(" group by lt.lotto_kind_code ");
		CheckCostUserRes res = this.commonJdbcTemplate.executeQueryForObject(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(CheckCostUserRes.class));
		return res;
	}

}
