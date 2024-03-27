package coffee.repo.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import coffee.transaction.vo.req.GetTransBoReq;
import coffee.transaction.vo.res.TransactionDatatableRes;
import coffee.transaction.vo.res.TransactionGroupDatatableRes;
import coffee.transaction.vo.res.TransactionGroupDetailRes;
import coffee.transaction.vo.res.TransactionGroupRes;
import coffee.transaction.vo.res.TransactionTimeOutRes;
import coffee.transaction.vo.res.bo.GroupTranBoRes;
import coffee.transaction.vo.res.bo.TransBoRes;
import framework.constant.LottoConstant.LOTTO_STATUS;
import framework.model.DataTableResponse;
import framework.model.DatatableRequest;
import framework.utils.CommonJdbcTemplate;
import framework.utils.ConvertDateUtils;
import framework.utils.DatatableUtils;

@Repository
public class LottoGroupTransactionDao {

	@Autowired
	private CommonJdbcTemplate commonJdbcTemplate;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public DataTableResponse<TransactionGroupDatatableRes> paginate(DatatableRequest req) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append("select tb.* ,lct.type_name ,lcl.class_name from lotto_group_transaction tb");
		sqlBuilder.append(" inner join lotto_class lcl ");
		sqlBuilder.append(" on lcl.lotto_class_code = tb.lotto_class_code ");
		sqlBuilder.append(" inner join lotto_category lct ");
		sqlBuilder.append(" on lcl.lotto_category_code = lct.lotto_category_code ");
		DataTableResponse<TransactionGroupDatatableRes> dataTable = new DataTableResponse<TransactionGroupDatatableRes>();
		String sqlCount = DatatableUtils.countForDatatable(sqlBuilder.toString(), req.getFilter(),
				sqlBuilder.toString());
		String sqlData = DatatableUtils.limitForDataTable(sqlBuilder.toString(), req.getPage(), req.getLength(),
				req.getSort(), req.getFilter());
		System.out.println(sqlData);
		System.out.println(sqlCount);
		List<Object> params = new ArrayList<>();
		Integer count = commonJdbcTemplate.executeQueryForObject(sqlCount, params.toArray(), Integer.class);
		List<TransactionGroupDatatableRes> data = commonJdbcTemplate.executeQuery(sqlData, params.toArray(),
				BeanPropertyRowMapper.newInstance(TransactionGroupDatatableRes.class));
		dataTable.setData(data);
		dataTable.setRecordsTotal(count);
		return dataTable;
	}

	public DataTableResponse<TransactionDatatableRes> paginateAllTransaction(DatatableRequest req) {
		StringBuilder sqlBuilder = new StringBuilder();
		sqlBuilder.append(
				"select tb.*,mlk.msd_lotto_kind_code,mlk.msd_lotto_kind_name ,lc.lotto_category_code,lcl.type_name , lc.class_name as lotto_class_name   from lotto_transaction tb");
		sqlBuilder.append(" inner join msd_lotto_kind mlk on mlk.msd_lotto_kind_code = tb.lotto_kind_code");
		sqlBuilder.append(" inner join lotto_class lc on lc.lotto_class_code = tb.lotto_class_code");
		sqlBuilder.append(" inner join lotto_category lcl on lc.lotto_category_code = lcl.lotto_category_code ");
		DataTableResponse<TransactionDatatableRes> dataTable = new DataTableResponse<TransactionDatatableRes>();
		String sqlCount = DatatableUtils.countForDatatable(sqlBuilder.toString(), req.getFilter());
		String sqlData = DatatableUtils.limitForDataTable(sqlBuilder.toString(), req.getPage(), req.getLength(),
				req.getSort(), req.getFilter());
		System.out.println(sqlData);
		System.out.println(sqlCount);
		List<Object> params = new ArrayList<>();
		Integer count = commonJdbcTemplate.executeQueryForObject(sqlCount, params.toArray(), Integer.class);
		List<TransactionDatatableRes> data = commonJdbcTemplate.executeQuery(sqlData, params.toArray(),
				BeanPropertyRowMapper.newInstance(TransactionDatatableRes.class));
		dataTable.setData(data);
		dataTable.setRecordsTotal(count);
		return dataTable;
	}

	public TransactionGroupDetailRes getGroupByCode(String groupCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT ");
		sqlBuilder.append(" 	lgt.lotto_group_transaction_code, ");
		sqlBuilder.append(" 	lgt.username, ");
		sqlBuilder.append(" 	lgt.sum_group_bet, ");
		sqlBuilder.append(" 	lgt.created_at, ");
		sqlBuilder.append(" 	lgt.lotto_class_code, ");
		sqlBuilder.append(" 	lcl.class_name, ");
		sqlBuilder.append(" 	lct.lotto_category_code, ");
		sqlBuilder.append(" 	lct.type_name ");
		sqlBuilder.append(" from lotto_group_transaction lgt ");
		sqlBuilder.append("     inner join lotto_class lcl ");
		sqlBuilder.append("         on lcl.lotto_class_code = lgt.lotto_class_code ");
		sqlBuilder.append("     inner join lotto_category lct ");
		sqlBuilder.append("         on lcl.lotto_category_code = lct.lotto_category_code ");
		sqlBuilder.append(" where lgt.lotto_group_transaction_code = ? ");
		params.add(groupCode);
		TransactionGroupDetailRes dataFind = commonJdbcTemplate.executeQueryForObject(sqlBuilder.toString(),
				params.toArray(), rowMapperTransactionGroupDetailRes);
		return dataFind;
	}

	public List<TransactionGroupRes> getAllGroup() {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT ");
		sqlBuilder.append(" 	lgt.lotto_group_transaction_code, ");
		sqlBuilder.append(" 	lgt.username, ");
		sqlBuilder.append(" 	lgt.sum_group_bet, ");
		sqlBuilder.append(" 	lgt.created_at, ");
		sqlBuilder.append(" 	lgt.lotto_class_code, ");
		sqlBuilder.append(" 	lgt.status, ");
		sqlBuilder.append(" 	lcl.class_name, ");
		sqlBuilder.append(" 	lct.lotto_category_code, ");
		sqlBuilder.append(" 	lct.type_name ");
		sqlBuilder.append(" from lotto_group_transaction lgt ");
		sqlBuilder.append("     inner join lotto_class lcl ");
		sqlBuilder.append("         on lcl.lotto_class_code = lgt.lotto_class_code ");
		sqlBuilder.append("     inner join lotto_category lct ");
		sqlBuilder.append("         on lcl.lotto_category_code = lct.lotto_category_code ORDER BY lgt.created_at ASC");
		List<TransactionGroupRes> dataFind = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				rowMapperTransactionGroupRes);
		return dataFind;
	}

	public List<TransactionGroupRes> getLottoGroupByUser(String username) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT ");
		sqlBuilder.append(" 	lgt.lotto_group_transaction_code, ");
		sqlBuilder.append(" 	lgt.username, ");
		sqlBuilder.append(" 	lgt.sum_group_bet, ");
		sqlBuilder.append(" 	lgt.created_at, ");
		sqlBuilder.append(" 	lgt.lotto_class_code, ");
		sqlBuilder.append(" 	lgt.status, ");
		sqlBuilder.append(" 	lcl.class_name, ");
		sqlBuilder.append(" 	lct.lotto_category_code, ");
		sqlBuilder.append("		lct.type_name ");
		sqlBuilder.append(" from lotto_group_transaction lgt ");
		sqlBuilder.append("     inner join lotto_class lcl ");
		sqlBuilder.append("         on lcl.lotto_class_code = lgt.lotto_class_code ");
		sqlBuilder.append("     inner join lotto_category lct ");
		sqlBuilder.append("         on lcl.lotto_category_code = lct.lotto_category_code ");
		sqlBuilder.append(" where lgt.username = ? ");
		params.add(username);
		List<TransactionGroupRes> dataFind = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				rowMapperTransactionGroupRes);
		return dataFind;
	}

	public List<TransactionTimeOutRes> getLottoGroupByUserForCheckTimeOut(String lottoGroupTransactionCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append("SELECT ");
		sqlBuilder.append("		lgt.lotto_group_transaction_code, ");
		sqlBuilder.append("		lgt.username, ");
		sqlBuilder.append("		lgt.sum_group_bet, ");
		sqlBuilder.append("		lgt.created_at, ");
		sqlBuilder.append("		lgt.lotto_class_code, ");
		sqlBuilder.append("		lgt.created_at, ");
		sqlBuilder.append("		lgt.status, ");
		sqlBuilder.append("		ts.time_close, ");
		sqlBuilder.append("		lcl.class_name, ");
		sqlBuilder.append("		lct.lotto_category_code, ");
		sqlBuilder.append("		lcl.time_after_buy, ");
		sqlBuilder.append("		lcl.time_before_lotto, ");
		sqlBuilder.append("		lct.type_name ");
		sqlBuilder.append("	From lotto_group_transaction lgt ");
		sqlBuilder.append("		inner join lotto_class lcl");
		sqlBuilder.append("			on lcl.lotto_class_code = lgt.lotto_class_code");
		sqlBuilder.append("		inner join lotto_category lct");
		sqlBuilder.append("			on lcl.lotto_category_code = lct.lotto_category_code");
		sqlBuilder.append("		inner join time_sell ts ");
		sqlBuilder.append("			on lgt.lotto_class_code = ts.lotto_class_code");
		sqlBuilder.append("	where lgt.lotto_group_transaction_code = ? ");
		params.add(lottoGroupTransactionCode);

		List<TransactionTimeOutRes> dataFind = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				rowMapperTransactionTimeOutRes);
		return dataFind;

	}

	private RowMapper<TransactionGroupRes> rowMapperTransactionGroupRes = new RowMapper<TransactionGroupRes>() {
		@Override
		public TransactionGroupRes mapRow(ResultSet rs, int arg1) throws SQLException {
			TransactionGroupRes vo = new TransactionGroupRes();
			vo.setLottoTransactionGroupCode(rs.getString("lotto_group_transaction_code"));
			vo.setLottoCategoryCode(rs.getString("lotto_category_code"));
			vo.setLottoCategoryName(rs.getString("type_name"));
			vo.setLottoClassCode(rs.getString("lotto_class_code"));
			vo.setLottoClassName(rs.getString("class_name"));
			vo.setUsername(rs.getString("username"));
			vo.setStatus(rs.getString("status"));
			vo.setCreateAt(ConvertDateUtils.formatDateToString(rs.getTimestamp("created_at"),
					ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
			vo.setSumBet(rs.getBigDecimal("sum_group_bet"));
			return vo;
		}
	};

	private RowMapper<TransactionGroupDetailRes> rowMapperTransactionGroupDetailRes = new RowMapper<TransactionGroupDetailRes>() {
		@Override
		public TransactionGroupDetailRes mapRow(ResultSet rs, int arg1) throws SQLException {
			TransactionGroupDetailRes vo = new TransactionGroupDetailRes();
			vo.setLottoTransactionGroupCode(rs.getString("lotto_group_transaction_code"));
			vo.setLottoCategoryCode(rs.getString("lotto_category_code"));
			vo.setLottoCategoryName(rs.getString("type_name"));
			vo.setLottoClassCode(rs.getString("lotto_class_code"));
			vo.setLottoClassName(rs.getString("class_name"));
			vo.setUsername(rs.getString("username"));
			vo.setCreateAt(ConvertDateUtils.formatDateToString(rs.getTimestamp("created_at"),
					ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
			vo.setSumBet(rs.getBigDecimal("sum_group_bet"));
			return vo;
		}
	};

	private RowMapper<TransactionTimeOutRes> rowMapperTransactionTimeOutRes = new RowMapper<TransactionTimeOutRes>() {
		@Override
		public TransactionTimeOutRes mapRow(ResultSet rs, int arg1) throws SQLException {
			TransactionTimeOutRes vo = new TransactionTimeOutRes();
			vo.setLottoTransactionGroupCode(rs.getString("lotto_group_transaction_code"));
			vo.setLottoCategoryCode(rs.getString("lotto_category_code"));
			vo.setLottoCategoryName(rs.getString("type_name"));
			vo.setLottoClassCode(rs.getString("lotto_class_code"));
			vo.setLottoClassName(rs.getString("class_name"));
			vo.setUsername(rs.getString("username"));
			vo.setStatus(rs.getString("status"));
			vo.setTimeAfterBuy(rs.getInt("time_after_buy"));
			vo.setTimeBeforeLotto(rs.getInt("time_before_lotto"));
			vo.setTimeClose(rs.getTimestamp("time_close"));
			vo.setCreateAt(rs.getTimestamp("created_at"));
			vo.setSumBet(rs.getBigDecimal("sum_group_bet"));
			return vo;
		}
	};

	public List<TransactionGroupRes> getLottoGroupByUserPending(String username) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(
				" select lgt.*,lc.class_name,lct.lotto_category_code,lct.type_name, lgt.installment, lgt.round_yeekee  ");
		sqlBuilder.append(" from lotto_group_transaction lgt ");
		sqlBuilder.append(" left join ( select * from lotto_class ) lc ");
		sqlBuilder.append(" on lc.lotto_class_code = lgt.lotto_class_code ");
		sqlBuilder.append(" left join (select * from lotto_category ) lct ");
		sqlBuilder.append(" on lct.lotto_category_code = lc.lotto_category_code ");
		sqlBuilder.append(" where lgt.username = ? ");
		params.add(username);
		sqlBuilder.append(" and status = ? order by created_at desc ");
		params.add(LOTTO_STATUS.PENDING);
		List<TransactionGroupRes> dataFind = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				rowMapperTransactionPendingGroupRes);
		return dataFind;
	}

	private RowMapper<TransactionGroupRes> rowMapperTransactionPendingGroupRes = new RowMapper<TransactionGroupRes>() {
		@Override
		public TransactionGroupRes mapRow(ResultSet rs, int arg1) throws SQLException {
			TransactionGroupRes vo = new TransactionGroupRes();
			vo.setLottoTransactionGroupCode(rs.getString("lotto_group_transaction_code"));
			vo.setLottoCategoryCode(rs.getString("lotto_category_code"));
			vo.setLottoCategoryName(rs.getString("type_name"));
			vo.setLottoClassCode(rs.getString("lotto_class_code"));
			vo.setLottoClassName(rs.getString("class_name"));
			vo.setUsername(rs.getString("username"));
			vo.setInstallment(rs.getString("installment"));
			vo.setStatus(rs.getString("status"));
			vo.setRoundYeekee(rs.getInt("round_yeekee"));
			vo.setCreateAt(ConvertDateUtils.formatDateToString(rs.getTimestamp("created_at"),
					ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
			vo.setCreateAtDate(rs.getTimestamp("created_at"));
			vo.setSumBet(rs.getBigDecimal("sum_group_bet"));
			return vo;
		}
	};

	public List<TransactionGroupRes> getLottoGroupByUserShow(String username, String code) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append("  select lgt.*,lc.class_name,lct.lotto_category_code,lct.type_name,lgt.remark as remark, ");
		sqlBuilder.append("  ( ");
		sqlBuilder.append("  select ");
		sqlBuilder.append("  	Sum(prize_correct) ");
		sqlBuilder.append("  from ");
		sqlBuilder.append("  	lotto_transaction ");
		sqlBuilder.append("  where ");
		sqlBuilder.append("  	lotto_group_transaction_code = lgt.lotto_group_transaction_code ");
		sqlBuilder.append("  	and status = 'WIN' ) as sum_prize_win ");
		sqlBuilder.append("   ");
		sqlBuilder.append("  		 from lotto_group_transaction lgt  ");
		sqlBuilder.append("  		 left join lotto_class lc  ");
		sqlBuilder.append("  		 on lc.lotto_class_code = lgt.lotto_class_code  ");
		sqlBuilder.append("  		 left join lotto_category  lct  ");
		sqlBuilder.append("  		 on lct.lotto_category_code = lc.lotto_category_code  ");
		sqlBuilder.append("  		 where lgt.username = ? ");
		sqlBuilder.append("  		 and status != ? ");
		params.add(username);
		params.add(LOTTO_STATUS.PENDING);
		if (StringUtils.isNotBlank(code) && !"ALL_CLASS_CODE".equals(code)) {
			sqlBuilder.append(" and lct.lotto_category_code = ? ");
			params.add(code);
		}
		sqlBuilder.append(" order by created_at desc ");
		List<TransactionGroupRes> dataFind = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				rowmapperLottoGroupByUserShow);
		return dataFind;
	}

	private RowMapper<TransactionGroupRes> rowmapperLottoGroupByUserShow = new RowMapper<TransactionGroupRes>() {
		@Override
		public TransactionGroupRes mapRow(ResultSet rs, int arg1) throws SQLException {
			TransactionGroupRes vo = new TransactionGroupRes();
			vo.setLottoTransactionGroupCode(rs.getString("lotto_group_transaction_code"));
			vo.setLottoCategoryCode(rs.getString("lotto_category_code"));
			vo.setLottoCategoryName(rs.getString("type_name"));
			vo.setLottoClassCode(rs.getString("lotto_class_code"));
			vo.setLottoClassName(rs.getString("class_name"));
			vo.setUsername(rs.getString("username"));
			vo.setStatus(rs.getString("status"));
			vo.setRemark(rs.getString("remark"));
			vo.setRoundYeekee(rs.getInt("round_yeekee"));
			vo.setSumPrizeWin(
					rs.getBigDecimal("sum_prize_win") != null ? rs.getBigDecimal("sum_prize_win") : BigDecimal.ZERO);
			if (LOTTO_STATUS.SEQWIN.equals(vo.getStatus())) {
				vo.setSumPrizeWin(rs.getBigDecimal("sum_group_prize"));
			}
			vo.setCreateAt(ConvertDateUtils.formatDateToString(rs.getTimestamp("created_at"),
					ConvertDateUtils.DD_MM_YYYY_HHMMSS, ConvertDateUtils.LOCAL_EN));
			vo.setSumBet(rs.getBigDecimal("sum_group_bet"));
			return vo;
		}
	};

	public Long getNextIndent() {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT lgt.lotto_group_transaction_id ");
		sqlBuilder.append(" FROM lotto_group_transaction lgt ");
		sqlBuilder.append(" WHERE lgt.lotto_group_transaction_id = IDENT_CURRENT('lotto_group_transaction') ");
		Long dataFind = 0l;
		try {
			@SuppressWarnings("deprecation")
			Long dataFind1 = jdbcTemplate.queryForObject(sqlBuilder.toString(), params.toArray(), Long.class);
			dataFind = dataFind1;
			dataFind++;
		} catch (Exception e) {
			System.out.println("Null Transaction");
		}
		if (dataFind == null) {
			return 0L;
		}
		return dataFind;
	}

	public List<GroupTranBoRes> getGroupTransBo(GetTransBoReq req) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT lgt.* FROM lotto_group_transaction lgt where 1=1 ");
		if (req.getTimeStart() != null) {
			sqlBuilder.append(" and lgt.created_at < Convert(datetime, ? ) ");
			params.add(req.getTimeStart());
		}
		if (req.getTimeEnd() != null) {
			sqlBuilder.append(" and lgt.created_at < Convert(datetime, ? ) ");
			params.add(req.getTimeEnd());
		}
		if (StringUtils.isNotEmpty(req.getUsername())) {
			sqlBuilder.append(" and lgt.username = ? ");
			params.add(req.getUsername());
		}
		return commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(GroupTranBoRes.class));
	}

	public List<TransBoRes> getTransBo(String transactionGroupCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT lt.* FROM lotto_transaction lt where lt.lotto_group_transaction_code = ? ");
		params.add(transactionGroupCode);
		return commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(TransBoRes.class));
	}
}
