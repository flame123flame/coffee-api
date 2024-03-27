package coffee.repo.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import coffee.model.MsdLottoKind;

@Repository
public class MsdLottoKindDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@SuppressWarnings("deprecation")
	public List<MsdLottoKind> findMsdLottoNotInKindByClassCode(String classCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT * from msd_lotto_kind mlk ");
		sqlBuilder.append(" where msd_lotto_kind_code not in ( ");
		sqlBuilder.append(" 	SELECT gkm.msd_lotto_kind_code from lotto_group lg ");
		sqlBuilder.append(" 		inner join group_kind_map gkm on ");
		sqlBuilder.append(" 		lg.lotto_group_code = gkm.lotto_group_code ");
		sqlBuilder.append(" 	where lg.lotto_class_code = ? ");
		params.add(classCode);
		sqlBuilder.append(" ) ");
		List<MsdLottoKind> dataFind = jdbcTemplate.query(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(MsdLottoKind.class));
		return dataFind;
	}

	public List<MsdLottoKind> findMsdLottoInKindByClassCode(String classCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT mlk.* from lotto_group lg ");
		sqlBuilder.append(" inner join group_kind_map gkm ");
		sqlBuilder.append(" 	on lg.lotto_group_code = gkm.lotto_group_code ");
		sqlBuilder.append(" inner join msd_lotto_kind mlk ");
		sqlBuilder.append(" 	on mlk.msd_lotto_kind_code = gkm.msd_lotto_kind_code ");
		sqlBuilder.append(" where lg.lotto_class_code = ? ");
		params.add(classCode);
		List<MsdLottoKind> dataFind = jdbcTemplate.query(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(MsdLottoKind.class));
		return dataFind;
	}

}
