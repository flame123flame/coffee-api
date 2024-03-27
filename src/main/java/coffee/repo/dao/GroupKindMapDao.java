package coffee.repo.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import coffee.model.MsdLottoKind;

@Repository
@SuppressWarnings("deprecation")
public class GroupKindMapDao {
	
		@Autowired
		private JdbcTemplate jdbcTemplate;

		public List<MsdLottoKind> getByGroupCode(String groupCode) {
			StringBuilder sqlBuilder = new StringBuilder();
			List<Object> params = new ArrayList<Object>();
			sqlBuilder.append(" SELECT mlk.* FROM ");
			sqlBuilder.append(" group_kind_map gkm ");
			sqlBuilder.append(" inner join msd_lotto_kind mlk ");
			sqlBuilder.append(" on gkm.msd_lotto_kind_code = mlk.msd_lotto_kind_code ");
			sqlBuilder.append(" where gkm.lotto_group_code = ? ");
			params.add(groupCode);
			List<MsdLottoKind> res = this.jdbcTemplate.query(sqlBuilder.toString(), params.toArray(), BeanPropertyRowMapper.newInstance(MsdLottoKind.class));
			return res;
		}
}
