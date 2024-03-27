package coffee.repo.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import coffee.lottoconfig.vo.res.GroupDtlRes;
import coffee.model.LottoGroup;
import framework.utils.CommonJdbcTemplate;

@Repository
public class LottoGroupDao {

	@Autowired
	private CommonJdbcTemplate commonJdbcTemplate;

	public LottoGroup getGroupByLottoKind(String lottoKindCode, String classCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT lg.* from group_kind_map gkm  ");
		sqlBuilder.append(" right join lotto_group lg  ");
		sqlBuilder.append(" on gkm.lotto_group_code = lg.lotto_group_code ");

		sqlBuilder.append(" where lg.lotto_class_code = ? ");
		params.add(classCode);

		sqlBuilder.append(" AND gkm.msd_lotto_kind_code = ? ");
		params.add(lottoKindCode);

		LottoGroup lottoGroup = commonJdbcTemplate.executeQueryForObject(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(LottoGroup.class));
		return lottoGroup;
	}

	public List<GroupDtlRes> getGrouDtl(String lottoKindCode, String classCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append("  SELECT ");
		sqlBuilder.append("  	LGD.LOTTO_GROUP_DTL_CODE AS DTL_CODE, ");
		sqlBuilder.append("  	LGD.PERCENT_FOR_LIMIT, ");
		sqlBuilder.append("  	LGD.TIER_LEVEL, ");
		sqlBuilder.append("  	PS.PRIZE  ");
		sqlBuilder.append("  FROM ");
		sqlBuilder.append("  	LOTTO_GROUP LG ");
		sqlBuilder.append("  JOIN GROUP_KIND_MAP GKM ON ");
		sqlBuilder.append("  	LG.LOTTO_GROUP_CODE = GKM.LOTTO_GROUP_CODE ");
		sqlBuilder.append("  JOIN LOTTO_GROUP_DTL LGD ON ");
		sqlBuilder.append("  	GKM.LOTTO_GROUP_CODE = LGD.LOTTO_GROUP_CODE ");
		sqlBuilder.append("  JOIN PRIZE_SETTING PS ON ");
		sqlBuilder.append("  	LGD.LOTTO_GROUP_DTL_CODE = PS.LOTTO_GROUP_DTL_CODE ");
		sqlBuilder.append("  	AND GKM.MSD_LOTTO_KIND_CODE = PS.MSD_LOTTO_KIND_CODE ");
		sqlBuilder.append("  WHERE ");
		sqlBuilder.append("  	LG.LOTTO_CLASS_CODE = ? ");
		sqlBuilder.append("  	AND GKM.MSD_LOTTO_KIND_CODE = ? ");
		sqlBuilder.append("  	AND PS.VIP_CODE = 'DEFAULT' ");

		params.add(classCode);
		params.add(lottoKindCode);

		List<GroupDtlRes> lottoGroup = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(GroupDtlRes.class));
		return lottoGroup;
	}
}
