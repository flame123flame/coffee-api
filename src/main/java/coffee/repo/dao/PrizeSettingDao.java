package coffee.repo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import coffee.lottoconfig.vo.res.PrizeSettingRes;
import coffee.lottoconfig.vo.res.PrizeYeekeeRes;
import coffee.lottoconfig.vo.res.addgroup.Prize;
import coffee.lottoconfig.vo.res.addgroup.PrizeList;
import coffee.model.PrizeSetting;
import coffee.web.vo.res.GetClassWebRes.GetClassWebPrice;
import framework.utils.CommonJdbcTemplate;

@Repository
public class PrizeSettingDao {

	@Autowired
	private CommonJdbcTemplate commonJdbcTemplate;

	public List<PrizeSettingRes> findByLottoGroupCode(String code) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sqlBuilder.append(" select ps.*,mlk.msd_lotto_kind_name from prize_setting ps ");
		sqlBuilder.append(" left join (select * from msd_lotto_kind) mlk ");
		sqlBuilder.append(" on mlk.msd_lotto_kind_code = ps.msd_lotto_kind_code  ");
		sqlBuilder.append(" where ps.lotto_group_code = ? ");
		params.add(code);
		List<PrizeSettingRes> dataRes = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				rowMapperPrizeSettingList);
		return dataRes;
	}
	
	public List<PrizeYeekeeRes.VipCodeListRes> findVipCodeGroup(String code) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sqlBuilder.append(" SELECT prize_setting.vip_code FROM prize_setting WHERE class_code = ? GROUP BY vip_code ORDER BY LEN(vip_code) asc");
		params.add(code);
		List<PrizeYeekeeRes.VipCodeListRes> dataRes = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),BeanPropertyRowMapper.newInstance(PrizeYeekeeRes.VipCodeListRes.class));
		return dataRes;
	}

	public List<Prize> getPrizeListForGroupRiskByClassCode(String groupCode, String msdLottoKindCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT ps.vip_code,ps.prize");
		sqlBuilder.append(" from prize_setting ps ");
		sqlBuilder.append(" left join lotto_group_dtl lgd ");
		sqlBuilder.append(" on ps.lotto_group_dtl_code = lgd.lotto_group_dtl_code ");
		sqlBuilder.append(
				"	WHERE ps.lotto_group_code = ? AND ps.msd_lotto_kind_code = ? AND ps.seq_order = 0 GROUP BY ps.vip_code,ps.prize ORDER BY ps.prize ");
		params.add(groupCode);
		params.add(msdLottoKindCode);
		List<Prize> list = this.commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(Prize.class));
		return list;
	}

	public List<PrizeList> getPrizeListForGroupRiskByVipCode(String groupCode, String msdLottoKindCode,
			String vipCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(
				" SELECT ps.prize_setting_code as pizeCode, ps.prize ,lgd.group_max_risk ,lgd.percent_for_limit");
		sqlBuilder.append(" from prize_setting ps ");
		sqlBuilder.append(" left join lotto_group_dtl lgd ");
		sqlBuilder.append(" on ps.lotto_group_dtl_code = lgd.lotto_group_dtl_code ");
		sqlBuilder.append(" WHERE ps.lotto_group_code = ? AND ps.msd_lotto_kind_code = ? AND ps.vip_code = ? ");
		params.add(groupCode);
		params.add(msdLottoKindCode);
		params.add(vipCode);
		List<PrizeList> list = this.commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				rowMapperPrizeSettingDataList);
		return list;
	}

	private RowMapper<PrizeSettingRes> rowMapperPrizeSettingList = new RowMapper<PrizeSettingRes>() {
		@Override
		public PrizeSettingRes mapRow(ResultSet rs, int arg1) throws SQLException {
			PrizeSettingRes vo = new PrizeSettingRes();
			vo.setPrizeSettingId(rs.getLong("prize_setting_id"));
			vo.setPrizeSettingCode(rs.getString("prize_setting_code"));
			vo.setLottoGroupCode(rs.getString("lotto_group_code"));
			vo.setMsdLottoKindCode(rs.getString("msd_lotto_kind_code"));
			vo.setMsdLottoKindName(rs.getString("msd_lotto_kind_name"));
			vo.setPrize(rs.getBigDecimal("prize"));
			vo.setPrizeLimit(rs.getBigDecimal("prize_limit"));
			return vo;
		}
	};

	private RowMapper<PrizeList> rowMapperPrizeSettingDataList = new RowMapper<PrizeList>() {
		@Override
		public PrizeList mapRow(ResultSet rs, int arg1) throws SQLException {
			PrizeList vo = new PrizeList();
			vo.setPrizeCode(rs.getString("pizeCode"));
			vo.setPrize(rs.getBigDecimal("prize"));
			vo.setGroupMaxRisk(rs.getBigDecimal("group_max_risk"));
			vo.setPercentForLimit(rs.getInt("percent_for_limit"));
			return vo;
		}
	};

	public List<GetClassWebPrice> getPrizeListByClassCode(String codeClass, String vipCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(
				" WITH ps AS (Select *, ROW_NUMBER() OVER (PARTITION BY lotto_group_code, msd_lotto_kind_code ORDER BY seq_order ASC) AS rn FROM prize_setting where vip_code = 'DEFAULT') ");
		sqlBuilder.append(
				" SELECT mlk.msd_lotto_kind_name as lotto_name, ps.msd_lotto_kind_code as lotto_kind ,ps.prize , ");
		sqlBuilder.append(
				"     ps.prize_limit,ps.vip_code, gmmm.maximum_per_trans, gmmm.maximum_per_user, gmmm.minimum_per_trans from lotto_class lc ");
		sqlBuilder.append(" inner join lotto_group lg  ");
		sqlBuilder.append(" on lc.lotto_class_code = lg.lotto_class_code  ");
		sqlBuilder.append(" inner join ps ");
		sqlBuilder.append(" on lg.lotto_group_code = ps.lotto_group_code  ");
		sqlBuilder.append(" inner join msd_lotto_kind mlk  ");
		sqlBuilder.append(" on ps.msd_lotto_kind_code = mlk.msd_lotto_kind_code  ");
		sqlBuilder.append(" inner join group_max_min_map gmmm  ");
		sqlBuilder.append(
				" on ps.vip_code = gmmm.vip_code AND ps.msd_lotto_kind_code = gmmm.msd_lotto_kind_code AND lc.lotto_class_code = gmmm.lotto_class_code ");
		sqlBuilder.append(" WHERE lc.lotto_class_code = ? AND ps.vip_code = ? and ps.rn = 1 ");
		params.add(codeClass);
		params.add(vipCode);
		List<GetClassWebPrice> list = this.commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(GetClassWebPrice.class));
		return list;
	}

	public List<GetClassWebPrice> getPrizeListByClassCode24Hr(String codeClass, String vipCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" select ");
		sqlBuilder.append(" 	prz.msd_lotto_kind_code as lotto_kind, ");
		sqlBuilder.append(" 	mlk.msd_lotto_kind_name as lotto_name, ");
		sqlBuilder.append(" 	prz.prize, ");
		sqlBuilder.append(" 	prz.prize_limit, ");
		sqlBuilder.append(" 	prz.vip_code, ");
		sqlBuilder.append(" 	gmm.minimum_per_trans, ");
		sqlBuilder.append(" 	gmm.maximum_per_trans, ");
		sqlBuilder.append(" 	gmm.maximum_per_user ");
		sqlBuilder.append(" FROM ");
		sqlBuilder.append(" 	( ");
		sqlBuilder.append(" 	SELECT ");
		sqlBuilder.append(" 		* ");
		sqlBuilder.append(" 	from ");
		sqlBuilder.append(" 		prize_setting ps ");
		sqlBuilder.append(" 	where ");
		sqlBuilder.append(" 		class_code = ? ");
		sqlBuilder.append(" 		and vip_code = ? ) prz ");
		sqlBuilder.append(" left join group_max_min_map gmm on ");
		sqlBuilder.append(" 	gmm.lotto_class_code = prz.class_code ");
		sqlBuilder.append(" 	and gmm.msd_lotto_kind_code = prz.msd_lotto_kind_code ");
		sqlBuilder.append(" 	and gmm.vip_code  = prz.vip_code ");
		sqlBuilder.append(" left join msd_lotto_kind mlk on ");
		sqlBuilder.append(" 	mlk.msd_lotto_kind_code = prz.msd_lotto_kind_code ");
		params.add(codeClass);
		params.add(vipCode);
		List<GetClassWebPrice> list = this.commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(GetClassWebPrice.class));
		return list;
	}

	public List<PrizeSetting> getPrizeListSettingByClassCode(String codeClass, String vipCode, String kindCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT ps.* from lotto_class lc  ");

		sqlBuilder.append(" inner join lotto_group lg  ");
		sqlBuilder.append(" on lc.lotto_class_code = lg.lotto_class_code  ");

		sqlBuilder.append(" inner join prize_setting ps ");
		sqlBuilder.append(" on ps.lotto_group_code = lg.lotto_group_code ");

		sqlBuilder.append(" where lc.lotto_class_code = ? ");
		params.add(codeClass);

		sqlBuilder.append(" AND ps.vip_code = ? ");
		params.add(vipCode);

		sqlBuilder.append(" AND ps.msd_lotto_kind_code = ? ");
		params.add(kindCode);
		List<PrizeSetting> list = this.commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(PrizeSetting.class));
		return list;
	}
}
