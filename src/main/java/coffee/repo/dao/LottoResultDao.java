package coffee.repo.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import coffee.lottoresult.vo.res.thai.LottoResultCategoryRes;
import coffee.lottoresult.vo.res.thai.LottoResultRoundYeekeeRes;
import coffee.lottoresult.vo.res.thai.lottoResultStocksRes;
import coffee.model.LottoResult;
import framework.constant.LottoConstant.LOTTO_KIND;
import framework.utils.CommonJdbcTemplate;

@Repository
public class LottoResultDao {
	@Autowired
	private CommonJdbcTemplate commonJdbcTemplate;

	public List<LottoResult> getLottoInstallmentByClassCode(String lottoClassCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" WITH cte AS ");
		sqlBuilder.append(
				" ( SELECT *, ROW_NUMBER() OVER (PARTITION BY lotto_class_code, lotto_result_installment, status ,code_group ORDER BY created_at DESC) AS rn FROM lotto_result ) ");
		sqlBuilder.append(" SELECT * FROM cte WHERE rn = 1 AND lotto_class_code = ? ORDER BY created_at desc ");
		params.add(lottoClassCode);
		List<LottoResult> dataFind = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(LottoResult.class));
		return dataFind;
	}
	
	public List<LottoResult> getLottoStocksResultLast(String lottoClassCode,String status) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT lr.lotto_category_code ,lotto_class_code ,lotto_number ,MAX(created_at) as created_at ,lr.msd_lotto_kind_code,lr.lotto_result_installment ");
		sqlBuilder.append(" from lotto_result lr ");
		sqlBuilder.append(" where lr.lotto_class_code = ? AND lr.status = ? AND lr.msd_lotto_kind_code  IN ( ? , ? )");
		sqlBuilder.append(" GROUP BY lr.lotto_result_installment,lr.lotto_category_code,lr.lotto_class_code,lr.lotto_number,lr.msd_lotto_kind_code");
		sqlBuilder.append(" ORDER BY msd_lotto_kind_code DESC");
		params.add(lottoClassCode);
		params.add(status);
		params.add(LOTTO_KIND.DIGIT3_TOP);
		params.add(LOTTO_KIND.DIGIT2_BOT);
		
		List<LottoResult> dataFind = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(LottoResult.class));
		return dataFind;
	}
	
	

	public List<LottoResult> getLottoResultByInstallment(String installment, String lottoClassCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT  mlk.msd_lotto_kind_name, lr.* FROM lotto_result lr");
		sqlBuilder.append(" LEFT JOIN msd_lotto_kind mlk ON  mlk.msd_lotto_kind_code = lr.msd_lotto_kind_code");
		sqlBuilder.append(" WHERE 1=1");
		sqlBuilder.append(" AND lr.status = 'APPROVE'");
		if(!installment.isEmpty()) {
			sqlBuilder.append(" AND lr.lotto_class_code = ?");
			params.add(lottoClassCode);
		}
		if(!lottoClassCode.isEmpty()) {
			sqlBuilder.append(" AND lr.lotto_result_installment = ?");
			params.add(installment);
		}
		List<LottoResult> dataFind = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(LottoResult.class));
		return dataFind;
	}

	public List<LottoResult>getLottoResultByLottoCategoryCode(String lottoCategoryCode)
	{
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT TOP 20 mlk.msd_lotto_kind_name, lr.* FROM lotto_result lr");
		sqlBuilder.append(" LEFT JOIN msd_lotto_kind mlk ON  mlk.msd_lotto_kind_code = lr.msd_lotto_kind_code");
		sqlBuilder.append(" WHERE 1=1");
		sqlBuilder.append(" AND lr.status = 'APPROVE'");
		if(!lottoCategoryCode.isEmpty()) {
			sqlBuilder.append(" AND lr.lotto_category_code = ?");
			params.add(lottoCategoryCode);
		}
		sqlBuilder.append(" AND lr.lotto_result_installment in (SELECT TOP 20 lotto_result_installment FROM lotto_result GROUP BY lotto_result_installment ORDER BY lotto_result_installment desc)");
//		sqlBuilder.append(" AND lr.status = 'APPROVE'");
		
		List<LottoResult> dataFind = commonJdbcTemplate.executeQuery(sqlBuilder.toString(),
				params.toArray(), BeanPropertyRowMapper.newInstance(LottoResult.class));
		return dataFind;
	}
	
	public List<LottoResult> getLottoResultGovLast(String classCode){
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT lr.lotto_category_code ,lotto_class_code ,lotto_number ,MAX(created_at) as created_at ,lr.lotto_result_installment,lr.msd_lotto_kind_code ");
		sqlBuilder.append(" from lotto_result lr ");
		sqlBuilder.append(" where lr.lotto_class_code = ? AND lr.status = 'APPROVE' ");
		sqlBuilder.append(" GROUP BY lr.lotto_result_installment,lr.lotto_category_code,lr.lotto_class_code,lr.lotto_number,lr.msd_lotto_kind_code");
		params.add(classCode);
		List<LottoResult> dataFind = commonJdbcTemplate.executeQuery(sqlBuilder.toString(),
				params.toArray(), BeanPropertyRowMapper.newInstance(LottoResult.class));
		
		return dataFind;
	}

	public List<LottoResultCategoryRes> getAllClassListLottoResult() {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT lc.lotto_category_code, lc.lotto_class_code , lc.class_name as lotto_class_name ");
		sqlBuilder.append(" FROM lotto_class lc ");
		sqlBuilder.append(" ORDER BY lc.lotto_category_code asc");
		List<LottoResultCategoryRes> dataFind = commonJdbcTemplate.executeQuery(sqlBuilder.toString(),
				params.toArray(), BeanPropertyRowMapper.newInstance(LottoResultCategoryRes.class));
		return dataFind;
	}

	public List<LottoResultRoundYeekeeRes> getRoundYeekeeByClassCode(String lottoClassCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT ts.round_yeekee, ts.lotto_class_code, lc.class_name as lotto_class_name  ");
		sqlBuilder.append(" FROM time_sell ts ");
		sqlBuilder.append(" LEFT JOIN lotto_class lc ON lc.lotto_class_code = ts.lotto_class_code ");
		sqlBuilder.append(" WHERE ts.lotto_class_code = ?");
		params.add(lottoClassCode);
		sqlBuilder.append(" ORDER BY ts.round_yeekee asc");

		List<LottoResultRoundYeekeeRes> dataFind = commonJdbcTemplate.executeQuery(sqlBuilder.toString(),
				params.toArray(), BeanPropertyRowMapper.newInstance(LottoResultRoundYeekeeRes.class));
		return dataFind;
	}

	public List<LottoResult> getLottoResultByClassCode(String lottoClassCode, String installment) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT TOP 20 mlk.msd_lotto_kind_name, lr.* FROM lotto_result lr");
		sqlBuilder.append(" LEFT JOIN msd_lotto_kind mlk ON  mlk.msd_lotto_kind_code = lr.msd_lotto_kind_code");
		sqlBuilder.append(" WHERE 1=1");
		sqlBuilder.append(" AND lr.status = 'APPROVE'");
		if(!lottoClassCode.isEmpty()) {
			sqlBuilder.append(" AND lr.lotto_class_code = ?");
			params.add(lottoClassCode);
		}
		if(!lottoClassCode.isEmpty()) {
			sqlBuilder.append(" AND lr.lotto_result_installment = ?");
			params.add(installment);
		}
		sqlBuilder.append(" AND lr.lotto_result_installment in (SELECT TOP 20 lotto_result_installment FROM lotto_result GROUP BY lotto_result_installment ORDER BY lotto_result_installment desc)");
		
		List<LottoResult> dataFind = commonJdbcTemplate.executeQuery(sqlBuilder.toString(),
				params.toArray(), BeanPropertyRowMapper.newInstance(LottoResult.class));
		return dataFind;
	}

	public List<lottoResultStocksRes> getCategory(String stock) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT lc2.type_name as lotto_category_name");
		sqlBuilder.append(" FROM lotto_class lc ");
		sqlBuilder.append(" LEFT JOIN lotto_category lc2 ON lc2.lotto_category_code = lc.lotto_category_code  ");
		sqlBuilder.append(" WHERE lc.lotto_category_code = ?");
		params.add(stock);
		List<lottoResultStocksRes> dataFind = commonJdbcTemplate.executeQuery(sqlBuilder.toString(),
				params.toArray(), BeanPropertyRowMapper.newInstance(lottoResultStocksRes.class));
		return dataFind;
	}

}
