package coffee.repo.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import coffee.model.LottoGroupDtl;
import framework.utils.CommonJdbcTemplate;

@Repository
public class LottoGroupDtlDao {
	@Autowired
	private CommonJdbcTemplate commonJdbcTemplate;

	public List<LottoGroupDtl> getGroupByLottoKind(String lottoKindCode, String classCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();

		sqlBuilder.append(" select lgd.* from lotto_group_dtl lgd  ");
		sqlBuilder.append(" inner join lotto_group lg ");
		sqlBuilder.append(" on lg.lotto_group_code = lgd.lotto_group_code ");
		sqlBuilder.append(" inner join group_kind_map gkm ");
		sqlBuilder.append(" on gkm.lotto_group_code = lg.lotto_group_code ");
		
		sqlBuilder.append(" AND gkm.msd_lotto_kind_code = ? ");
		params.add(lottoKindCode);
		
		sqlBuilder.append(" where lg.lotto_class_code = ? ");
		params.add(classCode);
		

		List<LottoGroupDtl> lottoGroup = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(), BeanPropertyRowMapper.newInstance(LottoGroupDtl.class));
		return lottoGroup;
	}

}
