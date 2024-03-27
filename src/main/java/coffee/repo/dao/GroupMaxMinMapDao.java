package coffee.repo.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import coffee.model.GroupMaxMinMap;
import framework.utils.CommonJdbcTemplate;

@Repository
public class GroupMaxMinMapDao {

	@Autowired
	private CommonJdbcTemplate commonJdbcTemplate;
	
	public List<GroupMaxMinMap> getMaxMinByCode(String classCode,String msdKindCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" select * from group_max_min_map gmmm where lotto_class_code = ? and msd_lotto_kind_code = ? ORDER BY LEN(vip_code),vip_code asc");
		params.add(classCode);
		params.add(msdKindCode);
		List<GroupMaxMinMap>  res = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(), BeanPropertyRowMapper.newInstance(GroupMaxMinMap.class));
		return res;
	}
}
