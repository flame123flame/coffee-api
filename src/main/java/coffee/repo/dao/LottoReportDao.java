package coffee.repo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import coffee.lottoreport.vo.res.LottoInstallmentRes;
import coffee.lottoreport.vo.res.LottoReportRes;
import coffee.lottoreport.vo.res.LottoReportUpdatedResultYeekeeRes;
import coffee.lottoreport.vo.res.LottoSumWin;
import framework.model.DataTableResponse;
import framework.model.DatatableRequest;
import framework.utils.CommonJdbcTemplate;
import framework.utils.DatatableUtils;

@Repository
public class LottoReportDao {

	@Autowired
	private CommonJdbcTemplate commonJdbcTemplate;

	public DataTableResponse<LottoInstallmentRes> paginateDashboard(DatatableRequest req, String lottoClassCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sqlBuilder.append("SELECT lr2.lotto_result_installment as installment,lr2.lotto_class_code , lc.class_name as lotto_class_name from ( SELECT max(lr.lotto_result_id) as id ,  lr.lotto_result_installment from lotto_result lr ");
		sqlBuilder.append(" group by lr.lotto_result_installment ) as ab");
		sqlBuilder.append(" LEFT join lotto_result lr2 on ab.id = lr2.lotto_result_id " );
		sqlBuilder.append(" INNER JOIN lotto_class lc on lc.lotto_class_code  = lr2.lotto_class_code " );
		sqlBuilder.append(" WHERE lr2.lotto_class_code = ? and lr2.status ='APPROVE'");
		params.add(lottoClassCode);
		DataTableResponse<LottoInstallmentRes> dataTable = new DataTableResponse<LottoInstallmentRes>();
		String sqlCount = DatatableUtils.countForDatatable(sqlBuilder.toString(), req.getFilter());
		String sqlData = DatatableUtils.limitForDataTable(sqlBuilder.toString(), req.getPage(), req.getLength(),
				req.getSort(), req.getFilter());
		System.out.println(sqlCount);
		System.out.println(sqlData);
		
		Integer count = commonJdbcTemplate.executeQueryForObject(sqlCount, params.toArray(), Integer.class);
		List<LottoInstallmentRes> data = commonJdbcTemplate.executeQuery(sqlData, params.toArray(),
				BeanPropertyRowMapper.newInstance(LottoInstallmentRes.class));
		dataTable.setData(data);
		dataTable.setRecordsTotal(count);
		return dataTable;
	}

	public List<LottoInstallmentRes> getAllInstallment(String classCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT * FROM lotto_result lr ");
		sqlBuilder.append("WHERE lr.status ='APPROVE' AND lr.lotto_class_code = ?");
		params.add(classCode);
		List<LottoInstallmentRes> dataRes = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(LottoInstallmentRes.class));
		return dataRes;
	}

	public List<LottoReportRes> getReportTransactionLotto(String classCode, String installment) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(
				" SELECT lt.username,lt.lotto_group_transaction_code,lt.lotto_kind_code,mlk.msd_lotto_kind_name, ");
		sqlBuilder.append(" lt.count_seq,lt.pay_cost,lt.prize_cost,lt.prize_correct,lt.installment  ");
		sqlBuilder.append(" FROM lotto_transaction lt  ");
		sqlBuilder.append(" INNER JOIN msd_lotto_kind mlk ON msd_lotto_kind_code = lt.lotto_kind_code  ");
		sqlBuilder.append("WHERE lt.status = 'WIN' and lt.lotto_class_code = ? and lt.installment = ?");
		params.add(classCode);
		params.add(installment);

		List<LottoReportRes> dataRes = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(LottoReportRes.class));
		return dataRes;
	}

	public List<LottoSumWin> getLottoSumWin(String classCode, String installment) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(
				" SELECT mlk.msd_lotto_kind_name,COUNT(CASE WHEN lt.status ='WIN' THEN lt.lotto_kind_code ELSE null END) AS sum_msd_win , ");
		sqlBuilder.append(
				" SUM(CASE WHEN lt.status = 'WIN' THEN lt.prize_correct ELSE 0 END ) AS sum_prize_correct_win,  ");
		sqlBuilder.append(" SUM(CASE WHEN lt.status = 'WIN' OR lt.status ='LOSE' THEN lt.pay_cost ELSE 0 END ) AS sum_pay_correct_win,  ");
//		sqlBuilder.append(" SUM(CASE WHEN lt.status = 'LOSE' THEN lt.prize_correct ELSE 0 END ) AS sum_prize_correct_lose,  ");
		sqlBuilder.append(
				" SUM(CASE WHEN lt.status = 'WIN' OR lt.status = 'LOSE' THEN lt.prize_correct ELSE 0 END) as sumtest  ");
		sqlBuilder.append(" FROM lotto_transaction lt INNER JOIN msd_lotto_kind mlk  ");
		sqlBuilder.append(" ON mlk.msd_lotto_kind_code = lt.lotto_kind_code ");
		sqlBuilder.append(
				" WHERE ( lt.status = 'WIN' OR lt.status = 'LOSE') and lotto_class_code = ? and lt.installment = ? ");
		sqlBuilder.append(" GROUP BY mlk.msd_lotto_kind_name ");
		params.add(classCode);
		params.add(installment);

		List<LottoSumWin> dataRes = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(LottoSumWin.class));
		return dataRes;
	}

	public List<LottoReportRes> getSumLottoTransactionByClassCode(String classCode, String kindCode,
			String installment) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(
				" SELECT lt.username , lt.lotto_group_transaction_code, mlk.msd_lotto_kind_name,lt.count_seq,lt.pay_cost,lt.prize_cost,lt.prize_correct,lt.installment,lt.lotto_kind_code   ");
		sqlBuilder.append(" FROM lotto_transaction lt INNER JOIN msd_lotto_kind mlk   ");
		sqlBuilder.append(" ON mlk.msd_lotto_kind_code = lt.lotto_kind_code   ");
		sqlBuilder.append(
				" WHERE lt.status = 'WIN' AND lt. lotto_class_code = ? AND lt.lotto_kind_code = ? AND lt.installment = ? ");
		params.add(classCode);
		params.add(kindCode);
		params.add(installment);
		
		List<LottoReportRes> dataRes = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(LottoReportRes.class));
		return dataRes;
	}

	public DataTableResponse<LottoInstallmentRes> getReportRoundYeekee(DatatableRequest req, String lottoClassCode,
			String installment) {
		System.out.println("::::::::::::::::::::::::> " + installment + " ==" + lottoClassCode);
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<>();
//		sqlBuilder.append("SELECT lt.installment, lt.round_yeekee, lt.lotto_class_code, lcy.lotto_category_code ");
//		sqlBuilder.append(" FROM lotto_transaction lt ");
//		sqlBuilder.append(" LEFT JOIN lotto_class lc ON lc.lotto_class_code = lt.lotto_class_code ");
//		sqlBuilder.append(" LEFT JOIN lotto_category lcy ON lcy.lotto_category_code = lc.lotto_category_code ");
//
//		sqlBuilder.append(" WHERE 1=1");
//		if (StringUtils.isNotBlank(installment)) {
//			sqlBuilder.append(" AND lt.installment = " + "'" + installment + "'");
//		}
//		if (StringUtils.isNotBlank(lottoClassCode)) {
//			sqlBuilder.append(" AND lt.lotto_class_code = " + "'" + lottoClassCode + "'");
//		}
//		sqlBuilder.append(" GROUP BY lt.installment, lt.round_yeekee, lt.lotto_class_code, lcy.lotto_category_code");
		
		
		sqlBuilder.append("SELECT ysn.installment , ysn.class_code as lotto_class_code, lcy.lotto_category_code, ysn.round_number as round_yeekee ");
		sqlBuilder.append(" FROM yeekee_sum_number ysn ");
		sqlBuilder.append(" LEFT JOIN lotto_class lc ON lc.lotto_class_code = ysn.class_code ");
		sqlBuilder.append(" LEFT JOIN lotto_category lcy ON lcy.lotto_category_code = lc.lotto_category_code ");

		sqlBuilder.append(" WHERE 1=1");
		sqlBuilder.append(" AND status = 'SUCCESS'");
		if (StringUtils.isNotBlank(installment)) {
			sqlBuilder.append(" AND ysn.installment = " + "'" + installment + "'");
		}
		if (StringUtils.isNotBlank(lottoClassCode)) {
			sqlBuilder.append(" AND ysn.class_code = " + "'" + lottoClassCode + "'");
		}		
		DataTableResponse<LottoInstallmentRes> dataTable = new DataTableResponse<LottoInstallmentRes>();
		String sqlCount = DatatableUtils.countForDatatable(sqlBuilder.toString(), req.getFilter(),
				sqlBuilder.toString());
		String sqlData = DatatableUtils.limitForDataTable(sqlBuilder.toString(), req.getPage(), req.getLength(),
				req.getSort(), req.getFilter());
		System.out.println(sqlData);
		System.out.println(sqlCount);

		Integer count = commonJdbcTemplate.executeQueryForObject(sqlCount, params.toArray(), Integer.class);
		List<LottoInstallmentRes> data = commonJdbcTemplate.executeQuery(sqlData, params.toArray(),
				BeanPropertyRowMapper.newInstance(LottoInstallmentRes.class));
		dataTable.setData(data);
		dataTable.setRecordsTotal(count);
		return dataTable;
	}

	// ====================== LOTTO REPORT YEEKEE ========================
	public DataTableResponse<LottoReportRes> paginateSumLottoTransactionYeekee(DatatableRequest req) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sqlBuilder.append(
				"SELECT tb.username, tb.lotto_group_transaction_code, tb.lotto_kind_code, mlk.msd_lotto_kind_name,");
		sqlBuilder.append(" tb.count_seq, tb.pay_cost, tb.prize_cost, tb.prize_correct, tb.installment  ");
		sqlBuilder.append(" FROM lotto_transaction tb  ");
		sqlBuilder.append(" INNER JOIN msd_lotto_kind mlk ON msd_lotto_kind_code = tb.lotto_kind_code ");
		DataTableResponse<LottoReportRes> dataTable = new DataTableResponse<LottoReportRes>();
		String sqlCount = DatatableUtils.countForDatatable(sqlBuilder.toString(), req.getFilter(),
				sqlBuilder.toString());
		String sqlData = DatatableUtils.limitForDataTable(sqlBuilder.toString(), req.getPage(), req.getLength(),
				req.getSort(), req.getFilter());
		System.out.println(sqlData);
		System.out.println(sqlCount);

		Integer count = commonJdbcTemplate.executeQueryForObject(sqlCount, params.toArray(), Integer.class);
		List<LottoReportRes> data = commonJdbcTemplate.executeQuery(sqlData, params.toArray(),
				BeanPropertyRowMapper.newInstance(LottoReportRes.class));
		dataTable.setData(data);
		dataTable.setRecordsTotal(count);
		return dataTable;
	}

	public BigDecimal getLottoPrizeCorrectRes(String classCode, String installment, String roundYeekee) {
		System.out.println("(ノಠ益ಠ)ノ彡┻━┻ ~~> PRIZE " + classCode + " :: " + installment + " :: " + roundYeekee);
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append("SELECT sum(lt.prize_correct ) ");
		sqlBuilder.append(" FROM lotto_transaction lt  ");
		sqlBuilder.append(" WHERE lt.status = 'WIN' ");
		sqlBuilder.append(" AND lt.lotto_class_code = ? ");
		params.add(classCode);
		sqlBuilder.append(" AND lt.installment = ? ");
		params.add(installment);
		sqlBuilder.append(" AND lt.round_yeekee = ? ");
		params.add(roundYeekee);
		System.out.println(params.toArray());
		BigDecimal sum = commonJdbcTemplate.executeQueryForObject(sqlBuilder.toString(), params.toArray(),
				BigDecimal.class);
		return sum;
	}

	public List<LottoSumWin> getLottoSumWinYeekee(String classCode, String installment, String roundYeekee) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(
				" SELECT mlk.msd_lotto_kind_name,COUNT(CASE WHEN lt.status ='WIN' THEN lt.lotto_kind_code ELSE null END) AS sum_msd_win , ");
		sqlBuilder.append(
				" SUM(CASE WHEN lt.status = 'WIN' THEN lt.prize_correct ELSE 0 END ) AS sum_prize_correct_win,  ");
		sqlBuilder.append(" SUM(CASE WHEN lt.status = 'WIN' OR lt.status ='LOSE' OR  lt.status = 'PENDING' THEN lt.pay_cost ELSE 0 END ) AS sum_pay_correct_win,  ");
		sqlBuilder.append(
				" SUM(CASE WHEN lt.status = 'WIN' OR lt.status = 'LOSE' OR  lt.status = 'PENDING' THEN lt.prize_correct ELSE 0 END) as sumtest  ");
		sqlBuilder.append(" FROM lotto_transaction lt INNER JOIN msd_lotto_kind mlk  ");
		sqlBuilder.append(" ON mlk.msd_lotto_kind_code = lt.lotto_kind_code ");
		sqlBuilder.append(
				" WHERE ( lt.status = 'WIN' OR lt.status = 'LOSE' OR  lt.status = 'PENDING') and lotto_class_code = ? and lt.installment = ? AND lt.round_yeekee = ?");
		sqlBuilder.append(" GROUP BY mlk.msd_lotto_kind_name ");
		params.add(classCode);
		params.add(installment);
		params.add(roundYeekee);

		List<LottoSumWin> dataRes = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(LottoSumWin.class));
		return dataRes;
	}

//	public List<LottoReportRes> getSumLottoTransactionByClassCodeYeekee(String classCode, String kindCode,
//			String installment, String roundYeekee) {
//		StringBuilder sqlBuilder = new StringBuilder();
//		List<Object> params = new ArrayList<Object>();
//		sqlBuilder.append(
//				" SELECT lt.username , lt.lotto_group_transaction_code, mlk.msd_lotto_kind_name,lt.count_seq,lt.pay_cost,lt.prize_cost,lt.prize_correct,lt.installment,lt.lotto_kind_code   ");
//		sqlBuilder.append(" FROM lotto_transaction lt INNER JOIN msd_lotto_kind mlk   ");
//		sqlBuilder.append(" ON mlk.msd_lotto_kind_code = lt.lotto_kind_code   ");
//		sqlBuilder.append(
//				" WHERE lt.status = 'WIN' AND lt. lotto_class_code = ? AND lt.lotto_kind_code = ? AND lt.installment = ? AND lt.round_yeekee = ? ");
//		params.add(classCode);
//		params.add(kindCode);
//		params.add(installment);
//		params.add(roundYeekee);
//		List<LottoReportRes> dataRes = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
//				BeanPropertyRowMapper.newInstance(LottoReportRes.class));
//		return dataRes;
//	}

	public DataTableResponse<LottoReportRes> paginateLottoTransactionYeekeeByKindCode(DatatableRequest req) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sqlBuilder.append(
				"SELECT tb.username , tb.lotto_group_transaction_code, mlk.msd_lotto_kind_name, tb.count_seq, tb.pay_cost, tb.prize_cost, tb.prize_correct, tb.installment, tb.lotto_kind_code ");
		sqlBuilder.append(" FROM lotto_transaction tb INNER JOIN msd_lotto_kind mlk  ");
		sqlBuilder.append(" ON mlk.msd_lotto_kind_code = tb.lotto_kind_code ");
		DataTableResponse<LottoReportRes> dataTable = new DataTableResponse<LottoReportRes>();
		String sqlCount = DatatableUtils.countForDatatable(sqlBuilder.toString(), req.getFilter(),
				sqlBuilder.toString());
		String sqlData = DatatableUtils.limitForDataTable(sqlBuilder.toString(), req.getPage(), req.getLength(),
				req.getSort(), req.getFilter());
		System.out.println(sqlData);
		System.out.println(sqlCount);

		Integer count = commonJdbcTemplate.executeQueryForObject(sqlCount, params.toArray(), Integer.class);
		List<LottoReportRes> data = commonJdbcTemplate.executeQuery(sqlData, params.toArray(),
				BeanPropertyRowMapper.newInstance(LottoReportRes.class));
		dataTable.setData(data);
		dataTable.setRecordsTotal(count);
		return dataTable;
	}

	public DataTableResponse<LottoReportUpdatedResultYeekeeRes> paginateUpdatedLottoResultYeekeeSumNumber(
			DatatableRequest req) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sqlBuilder
				.append("SELECT tb.sum_number, tb.number_result, tb.number_seq16, tb.updated_at, tb.updated_by, tb.change_result ");
		sqlBuilder.append(" FROM yeekee_sum_number tb ");
//		sqlBuilder.append(" WHERE tb.change_result IS NOT NULL");
		DataTableResponse<LottoReportUpdatedResultYeekeeRes> dataTable = new DataTableResponse<LottoReportUpdatedResultYeekeeRes>();
		String sqlCount = DatatableUtils.countForDatatable(sqlBuilder.toString(), req.getFilter(),
				sqlBuilder.toString());
		String sqlData = DatatableUtils.limitForDataTable(sqlBuilder.toString(), req.getPage(), req.getLength(),
				req.getSort(), req.getFilter());

		Integer count = commonJdbcTemplate.executeQueryForObject(sqlCount, params.toArray(), Integer.class);
		List<LottoReportUpdatedResultYeekeeRes> data = commonJdbcTemplate.executeQuery(sqlData, params.toArray(),
				BeanPropertyRowMapper.newInstance(LottoReportUpdatedResultYeekeeRes.class));
		dataTable.setData(data);
		dataTable.setRecordsTotal(count);
		return dataTable;
	}
	
	
	public DataTableResponse<LottoInstallmentRes> paginateDashboardYeekee(DatatableRequest req, String lottoClassCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<>();
		sqlBuilder.append("SELECT ysn.installment , lc.class_name as lotto_class_name, ysn.class_code as lotto_class_code ");
		sqlBuilder.append(" FROM yeekee_sum_number ysn ");
		sqlBuilder.append(" LEFT JOIN lotto_class lc ON lc.lotto_class_code = ysn.class_code ");
		sqlBuilder.append(" WHERE 1=1");
		if (StringUtils.isNotBlank(lottoClassCode)) {
			sqlBuilder.append(" AND ysn.class_code = " + "'" + lottoClassCode + "'");
		}
		sqlBuilder.append(" GROUP BY ysn.installment, lc.class_name, ysn.class_code");
		DataTableResponse<LottoInstallmentRes> dataTable = new DataTableResponse<LottoInstallmentRes>();
		String sqlCount = DatatableUtils.countForDatatable(sqlBuilder.toString(), req.getFilter(),
				sqlBuilder.toString());
		String sqlData = DatatableUtils.limitForDataTable(sqlBuilder.toString(), req.getPage(), req.getLength(),
				req.getSort(), req.getFilter());
		System.out.println(sqlData);
		System.out.println(sqlCount);

		Integer count = commonJdbcTemplate.executeQueryForObject(sqlCount, params.toArray(), Integer.class);
		List<LottoInstallmentRes> data = commonJdbcTemplate.executeQuery(sqlData, params.toArray(),
				BeanPropertyRowMapper.newInstance(LottoInstallmentRes.class));
		dataTable.setData(data);
		dataTable.setRecordsTotal(count);
		return dataTable;
	}

	// ====================== LOTTO REPORT YEEKEE ========================
}
