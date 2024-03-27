package coffee.repo.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import coffee.confirmtransaction.vo.ConfirmTransactionRes;
import framework.model.DataTableResponse;
import framework.model.DatatableRequest;
import framework.utils.CommonJdbcTemplate;
import framework.utils.DatatableUtils;

@Repository
public class ConfirmTransactionDao {
	
	@Autowired
	private CommonJdbcTemplate commonJdbcTemplate;
	
	public List<ConfirmTransactionRes> getAllConfirmTransaction() {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT lt.*,lc.class_name,mlk.msd_lotto_kind_name ");
		sqlBuilder.append(" FROM lotto_transaction lt ");
		sqlBuilder.append(" INNER JOIN msd_lotto_kind mlk ");
		sqlBuilder.append(" ON mlk.msd_lotto_kind_code = lt.lotto_kind_code ");
		sqlBuilder.append(" INNER JOIN lotto_class lc ON lt.lotto_class_code = lc.lotto_class_code ");		
		sqlBuilder.append(" WHERE lt.status = 'WIN' and lt.update_wallet IS NULL ");
		List<ConfirmTransactionRes> dataRes = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(), BeanPropertyRowMapper.newInstance(ConfirmTransactionRes.class));
		return dataRes;
	}
	
	public List<ConfirmTransactionRes> getAllTransaction() {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT lt.*,lc.class_name,mlk.msd_lotto_kind_name FROM lotto_transaction lt  ");
		sqlBuilder.append(" INNER JOIN msd_lotto_kind mlk ON mlk.msd_lotto_kind_code = lt.lotto_kind_code ");
		sqlBuilder.append(" INNER JOIN lotto_class lc ON lt.lotto_class_code = lc.lotto_class_code ");
		sqlBuilder.append(" WHERE lt.status = 'WIN' and lt.update_wallet IS NOT NULL ");
		List<ConfirmTransactionRes> dataRes = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(), BeanPropertyRowMapper.newInstance(ConfirmTransactionRes.class));
		return dataRes;
	}
	
	
	
	public DataTableResponse<ConfirmTransactionRes> getAllPaginateConfirmTransaction(DatatableRequest req) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT tb.* ,lc.class_name ,mlk.msd_lotto_kind_name");
		sqlBuilder.append(" FROM lotto_transaction tb ");
		sqlBuilder.append(" INNER JOIN msd_lotto_kind mlk ");
		sqlBuilder.append(" ON mlk.msd_lotto_kind_code = tb.lotto_kind_code ");
		sqlBuilder.append(" INNER JOIN lotto_class lc ON tb.lotto_class_code = lc.lotto_class_code ");		
//		sqlBuilder.append(" WHERE tb.status = 'WIN' and tb.update_wallet IS NULL ");	
		
		DataTableResponse<ConfirmTransactionRes> dataTable = new DataTableResponse<ConfirmTransactionRes>();
		String sqlCount = DatatableUtils.countForDatatable(sqlBuilder.toString(), req.getFilter(),
				sqlBuilder.toString());
		String sqlData = DatatableUtils.limitForDataTable(sqlBuilder.toString(), req.getPage(), req.getLength(),
				req.getSort(), req.getFilter());
		System.out.println(sqlData);
		System.out.println(sqlCount);
		List<Object> params = new ArrayList<>();
		Integer count = commonJdbcTemplate.executeQueryForObject(sqlCount, params.toArray(), Integer.class);
		
		List<ConfirmTransactionRes> data = commonJdbcTemplate.executeQuery(sqlData, params.toArray(), BeanPropertyRowMapper.newInstance(ConfirmTransactionRes.class));
		
		dataTable.setData(data);
		dataTable.setRecordsTotal(count);
		return dataTable;
	}
	
	public DataTableResponse<ConfirmTransactionRes> getPaginateTransaction(DatatableRequest req) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT tb.*,lc.class_name,mlk.msd_lotto_kind_name FROM lotto_transaction tb  ");
		sqlBuilder.append(" INNER JOIN msd_lotto_kind mlk ON mlk.msd_lotto_kind_code = tb.lotto_kind_code ");
		sqlBuilder.append(" INNER JOIN lotto_class lc ON tb.lotto_class_code = lc.lotto_class_code ");
//		sqlBuilder.append(" WHERE tb.status = 'WIN' and tb.update_wallet IS NOT NULL ");	
		
		DataTableResponse<ConfirmTransactionRes> dataTable = new DataTableResponse<ConfirmTransactionRes>();
		String sqlCount = DatatableUtils.countForDatatable(sqlBuilder.toString(), req.getFilter(),
				sqlBuilder.toString());
		String sqlData = DatatableUtils.limitForDataTable(sqlBuilder.toString(), req.getPage(), req.getLength(),
				req.getSort(), req.getFilter());
		System.out.println(sqlData);
		System.out.println(sqlCount);
		List<Object> params = new ArrayList<>();
		Integer count = commonJdbcTemplate.executeQueryForObject(sqlCount, params.toArray(), Integer.class);
		
		List<ConfirmTransactionRes> data = commonJdbcTemplate.executeQuery(sqlData, params.toArray(), BeanPropertyRowMapper.newInstance(ConfirmTransactionRes.class));
		dataTable.setData(data);
		dataTable.setRecordsTotal(count);
		return dataTable;
	}
	
	public Integer getCountConfirmTransaction()
	{
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		
		sqlBuilder.append(" SELECT COUNT(lt.status) ");
		sqlBuilder.append(" FROM lotto_transaction lt ");
		sqlBuilder.append(" INNER JOIN msd_lotto_kind mlk  ");
		sqlBuilder.append(" ON mlk.msd_lotto_kind_code = lt.lotto_kind_code   ");
		sqlBuilder.append(" INNER JOIN lotto_class lc ON lt.lotto_class_code = lc.lotto_class_code ");
		sqlBuilder.append(" WHERE lt.status ='WIN' AND update_wallet IS NULL AND lc.lotto_category_code !='YEEKEE'  ");
		Integer count = commonJdbcTemplate.executeQueryForObject(sqlBuilder.toString(), params.toArray(), Integer.class);
		
		return count;
	}

}
