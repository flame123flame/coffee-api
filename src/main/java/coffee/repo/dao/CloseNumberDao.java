package coffee.repo.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import coffee.model.CloseNumber;
import framework.utils.CommonJdbcTemplate;

@Repository
public class CloseNumberDao {

	@Autowired
	private CommonJdbcTemplate commonJdbcTemplate;
	
	public List<CloseNumber> getByKind(String classCode, String kindCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT * FROM close_number cn ");
		sqlBuilder.append(" where cn.lotto_class_code = ? AND cn.msd_lotto_kind_code = ?");
		params.add(classCode);
		params.add(kindCode);
		List<CloseNumber> dataRes = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(), BeanPropertyRowMapper.newInstance(CloseNumber.class));
		return dataRes;
	}

}
