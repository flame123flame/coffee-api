package coffee.repo.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import coffee.confirmtransaction.vo.ConfirmTransactionRes;
import coffee.model.CustomerDumy;
import framework.utils.CommonJdbcTemplate;

@Repository
public class CustomerDumyDao {
	@Autowired
	private CommonJdbcTemplate commonJdbcTemplate;
	
	public List<CustomerDumy> getAll() {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT * FROM customer_dumy cd   ");
		sqlBuilder.append(" ORDER BY created_date ASC ");
		List<CustomerDumy> dataRes = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(), BeanPropertyRowMapper.newInstance(CustomerDumy.class));
		return dataRes;
	}
	
}
