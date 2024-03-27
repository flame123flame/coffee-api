package coffee.repo.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import coffee.yeekeeResult.vo.res.YeekeeApproveRes;
import framework.model.DataTableResponse;
import framework.model.DatatableRequest;
import framework.utils.CommonJdbcTemplate;
import framework.utils.DatatableUtils;

@Repository
public class YeekeeApproveDao {

	@Autowired
	private CommonJdbcTemplate commonJdbcTemplate;

	public DataTableResponse<YeekeeApproveRes> getYeekeePaginate(DatatableRequest req) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("SELECT tb.*,lc.class_name FROM yeekee_sum_number tb");
		sqlBuilder.append(" INNER JOIN lotto_class lc ON tb.class_code = lc.lotto_class_code  ");
		sqlBuilder.append(" WHERE tb.status LIKE '%CHECKING%' ");
		DataTableResponse<YeekeeApproveRes> dataTable = new DataTableResponse<YeekeeApproveRes>();
		String sqlCount = DatatableUtils.countForDatatable(sqlBuilder.toString(), req.getFilter(),
				sqlBuilder.toString());
		String sqlData = DatatableUtils.limitForDataTable(sqlBuilder.toString(), req.getPage(), req.getLength(),
				req.getSort(), req.getFilter());
		System.out.println(sqlData);
		System.out.println(sqlCount);
		List<Object> params = new ArrayList<>();
		Integer count = commonJdbcTemplate.executeQueryForObject(sqlCount, params.toArray(), Integer.class);
		List<YeekeeApproveRes> data = commonJdbcTemplate.executeQuery(sqlData, params.toArray(),
				BeanPropertyRowMapper.newInstance(YeekeeApproveRes.class));
		dataTable.setData(data);
		dataTable.setRecordsTotal(count);
		return dataTable;
	}
	
	public YeekeeApproveRes getYeekeeDetail(String code) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sqlBuilder.append("SELECT ysn.*,lc.class_name FROM yeekee_sum_number ysn");
		sqlBuilder.append(" INNER JOIN lotto_class lc ON ysn.class_code = lc.lotto_class_code ");
		sqlBuilder.append(" WHERE ysn.yeekee_sum_number_code LIKE ? ");
		params.add("%" + code.trim() + "%");
		YeekeeApproveRes data = commonJdbcTemplate.executeQueryForObject(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(YeekeeApproveRes.class));
		return data;
	}
}
