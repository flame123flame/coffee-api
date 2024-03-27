package coffee.repo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import coffee.lottoconfig.vo.res.LimitDtlRes;
import framework.constant.LottoConstant;
import framework.utils.ConvertDateUtils;

@Repository
@SuppressWarnings("deprecation")
public class LimitNumberDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Integer countLimitNumberMustEqual(String kindCode, String number, String classCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT COUNT(1) FROM limit_number lnb ");
		sqlBuilder.append(" where lnb.lotto_number = ? ");
		params.add(number);
		sqlBuilder.append(" and lnb.lotto_class_code = ? ");
		params.add(classCode);
		sqlBuilder.append(" and lnb.msd_lotto_kind_code = ? ");
		params.add(kindCode);
		Integer count = this.jdbcTemplate.queryForObject(sqlBuilder.toString(), params.toArray(), Integer.class);
		return count;
	}

	public Integer countLimitNumberSwapped(List<String> number, String classCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT COUNT(1) FROM limit_number lnb ");
		sqlBuilder.append(" where lnb.lotto_number in (?) ");
		params.add(String.join(",", number));
		sqlBuilder.append(" and lnb.lotto_class_code = ? ");
		params.add(classCode);

		sqlBuilder.append(" and ( lnb.msd_lotto_kind_code = ? ");
		params.add(LottoConstant.LOTTO_KIND.DIGIT3_TOP);
		sqlBuilder.append(" or lnb.msd_lotto_kind_code = ? ) ");
		params.add(LottoConstant.LOTTO_KIND.DIGIT3_SWAPPED);

		Integer count = this.jdbcTemplate.queryForObject(sqlBuilder.toString(), params.toArray(), Integer.class);
		return count;
	}

	public Integer countLimitNumber1DiGit(String kindCode, String number, String classCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT COUNT(1) FROM limit_number lnb ");
		sqlBuilder.append(" where lnb.lotto_number like ? ");
		params.add("%" + number.trim() + "%");
		sqlBuilder.append(" and lnb.lotto_class_code = ? ");
		params.add(classCode);
		sqlBuilder.append(" and lnb.msd_lotto_kind_code = ? ");
		params.add(kindCode);
		Integer count = this.jdbcTemplate.queryForObject(sqlBuilder.toString(), params.toArray(), Integer.class);
		return count;
	}

	public List<LimitDtlRes> getByKind(String classCode, String kindCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT ");
		sqlBuilder.append(" 	LN2.*, ");
		sqlBuilder.append(" 	LGD.TIER_LEVEL, ");
		sqlBuilder.append(" 	PS.PRIZE ");
		sqlBuilder.append(" FROM ");
		sqlBuilder.append(" 	LIMIT_NUMBER LN2 ");
		sqlBuilder.append(" JOIN LOTTO_GROUP_DTL LGD ON ");
		sqlBuilder.append(" 	LN2.LOTTO_GROUP_DTL_CODE_MANUAL = LGD.LOTTO_GROUP_DTL_CODE ");
		sqlBuilder.append(" JOIN PRIZE_SETTING PS ON ");
		sqlBuilder.append(" 	PS.MSD_LOTTO_KIND_CODE = LN2.MSD_LOTTO_KIND_CODE ");
		sqlBuilder.append(" 	AND PS.LOTTO_GROUP_DTL_CODE = LN2.LOTTO_GROUP_DTL_CODE ");
		sqlBuilder.append(" WHERE ");
		sqlBuilder.append(" 	LN2.LOTTO_CLASS_CODE = ? ");
		params.add(classCode);
		sqlBuilder.append(" 	AND LN2.MSD_LOTTO_KIND_CODE = ? ");
		params.add(kindCode);
		sqlBuilder.append(" 	AND PS.VIP_CODE = 'DEFAULT' ");
		sqlBuilder.append(" 	AND LN2.IS_MANUAL = 1 ");
		List<LimitDtlRes> dataRes = this.jdbcTemplate.query(sqlBuilder.toString(), params.toArray(), rowMapperLimitDtl);
		return dataRes;
	}

	private RowMapper<LimitDtlRes> rowMapperLimitDtl = new RowMapper<LimitDtlRes>() {
		@Override
		public LimitDtlRes mapRow(ResultSet rs, int arg1) throws SQLException {
			LimitDtlRes vo = new LimitDtlRes();

			vo.setEnable(rs.getBoolean("enable"));
			vo.setIsManual(rs.getBoolean("is_manual"));
			vo.setLimitNumberCode(rs.getString("limit_number_code"));
			vo.setLimitNumberId(rs.getLong("limit_number_id"));
			vo.setLottoClassCode(rs.getString("lotto_class_code"));
			vo.setLottoNumber(rs.getString("lotto_number"));
			vo.setLottoPrice(rs.getBigDecimal("lotto_price"));
			vo.setMsdLottoKindCode(rs.getString("msd_lotto_kind_code"));

			vo.setCreatedAt(ConvertDateUtils.formatDateToString(rs.getTimestamp("created_at"),
					ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
			vo.setCreatedBy(rs.getString("created_by"));
			vo.setUpdatedAt(ConvertDateUtils.formatDateToString(rs.getTimestamp("updated_at"),
					ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
			vo.setUpdatedBy(rs.getString("updated_by"));

			vo.setPrize(rs.getBigDecimal("prize"));
			vo.setTierLevel(rs.getInt("tier_level"));
			vo.setLottoGroupDtlCode(rs.getString("lotto_group_dtl_code"));

			return vo;
		}
	};
}
