package coffee.repo.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import coffee.model.YeekeeSubmitNumber;
import coffee.model.YeekeeSumNumber;
import framework.utils.CommonJdbcTemplate;

@Repository
public class YeekeeResultDao {

	@Autowired
	CommonJdbcTemplate commonJdbcTemplate;
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	public YeekeeSubmitNumber getYeekeeSubmitNumber(String classCode, String installment, Integer roundNumber) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sqlBuilder.append(" SELECT * FROM yeekee_submit_number ysn");
		sqlBuilder.append(" WHERE ysn.class_code = ? AND ysn.installment = ? AND ysn.round_number = ? ");
		params.add(classCode);
		params.add(installment);
		params.add(roundNumber);
		sqlBuilder.append(" AND ysn.seq_order = (");
		sqlBuilder.append(" SELECT Max(ysn.seq_order)");
		sqlBuilder.append(" FROM yeekee_submit_number ysn");
		sqlBuilder.append(" WHERE ysn.class_code = ? AND ysn.installment = ? AND ysn.round_number = ?) - 15 ");
		params.add(classCode);
		params.add(installment);
		params.add(roundNumber);
		YeekeeSubmitNumber dataRes = commonJdbcTemplate.executeQueryForObject(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(YeekeeSubmitNumber.class));
		return dataRes;
	}
	
	public List<YeekeeSumNumber> getYeekeeSumNumberResultLast(String classCode,String installment)
	{
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sqlBuilder.append(" SELECT ysn.round_number,ysn.number_result,ysn.class_code from yeekee_sum_number ysn ");
		sqlBuilder.append(" INNER JOIN time_sell ts on ysn.class_code = ts.lotto_class_code ");
		sqlBuilder.append(" WHERE ysn.installment = ? AND ysn.class_code = ? ");
		sqlBuilder.append(" GROUP BY ysn.round_number,ysn.number_result,ysn.sum_number,ysn.class_code ");
		params.add(installment);
		params.add(classCode);
		List<YeekeeSumNumber> dataRes = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(YeekeeSumNumber.class));
		
		return dataRes;
	}
	
	public YeekeeSubmitNumber getYeekeeSubmitNumberSeqWin(String classCode, String installment, Integer roundNumber,Integer seqUser) {
		
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sqlBuilder.append(" SELECT * FROM yeekee_submit_number ysn");
		sqlBuilder.append(" WHERE ysn.class_code = ? AND ysn.installment = ? AND ysn.round_number = ? ");
		params.add(classCode);
		params.add(installment);
		params.add(roundNumber);
		sqlBuilder.append(" AND ysn.seq_order = (");
		sqlBuilder.append(" SELECT Max(ysn.seq_order)");
		sqlBuilder.append(" FROM yeekee_submit_number ysn");
		sqlBuilder.append(" WHERE ysn.class_code = ? AND ysn.installment = ? AND ysn.round_number = ?) - ? ");
		params.add(classCode);
		params.add(installment);
		params.add(roundNumber);
		params.add(seqUser);
		YeekeeSubmitNumber dataRes = commonJdbcTemplate.executeQueryForObject(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(YeekeeSubmitNumber.class));
		return dataRes;
	}

	public void updateTransaction(String statusSet, String numberSet, Date updatedDate, String statusFind,
			String numberFind, String classCode, String kindCode,Integer roundYeekee) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" UPDATE ");
		sqlBuilder.append("  lotto_transaction ");
		sqlBuilder.append(" set ");
		sqlBuilder.append("  status =  ? , ");
		params.add(statusSet);
		sqlBuilder.append("  number_correct = ? ,");
		params.add(numberSet);
		sqlBuilder.append("  updated_date = ? ");
		params.add(updatedDate);
		sqlBuilder.append(" where ");
		sqlBuilder.append("  status = ? ");
		params.add(statusFind);
		sqlBuilder.append("  and lotto_number = ? ");
		params.add(numberFind);
		sqlBuilder.append("  and lotto_class_code = ? ");
		params.add(classCode);
		sqlBuilder.append("  and lotto_kind_code = ? ");
		params.add(kindCode);
		sqlBuilder.append("  and round_yeekee = ? ");
		params.add(roundYeekee);
		jdbcTemplate.update(sqlBuilder.toString(), params.toArray());
	}

	public void updateTransactionLike(String statusSet, String numberSet, Date updatedDate, String statusFind,
			String classCode, String kindCode,Integer roundYeekee) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" UPDATE lotto_transaction ");
		sqlBuilder.append(" set ");
		sqlBuilder.append("     status = ? ");
		params.add(statusSet);
		sqlBuilder.append("     ,number_correct = ? ");
		params.add(numberSet);
		sqlBuilder.append("     ,updated_date = ? ");
		params.add(updatedDate);
		sqlBuilder.append(" where ");
		sqlBuilder.append("     status = ? ");
		params.add(statusFind);
		sqlBuilder.append("     and lotto_class_code = ? ");
		params.add(classCode);
		sqlBuilder.append("     and lotto_kind_code = ? ");
		params.add(kindCode);
		sqlBuilder.append("  and round_yeekee = ? ");
		params.add(roundYeekee);
		jdbcTemplate.update(sqlBuilder.toString(), params.toArray());
	}

	public void updateGroupTransaction(String statusSet, String statusFind, String classCode,Integer roundYeekee) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" UPDATE lotto_group_transaction ");
		sqlBuilder.append(" set ");
		sqlBuilder.append("     status = ?  ");
		params.add(statusSet);
		sqlBuilder.append(" where ");
		sqlBuilder.append("     status = ? ");
		params.add(statusFind);
		sqlBuilder.append("     and lotto_class_code = ? ");
		params.add(classCode);
		sqlBuilder.append("  and round_yeekee = ? ");
		params.add(roundYeekee);
		jdbcTemplate.update(sqlBuilder.toString(), params.toArray());
	}

}
