package coffee.repo.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import coffee.web.vo.res.GetCloseNumberRes;
import framework.utils.CommonJdbcTemplate;

@Repository

public class GetCloseNumberDao {
	
	@Autowired
	private CommonJdbcTemplate commonJdbcTemplate;
	
	public List<GetCloseNumberRes> getEnable() {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT * FROM close_number cn  ");
		sqlBuilder.append(" WHERE cn.enable = '1' ");
		List<GetCloseNumberRes> dataRes = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(), BeanPropertyRowMapper.newInstance(GetCloseNumberRes.class));
		return dataRes;
	}

}
