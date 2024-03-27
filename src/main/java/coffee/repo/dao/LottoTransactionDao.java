package coffee.repo.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import coffee.lottoresult.vo.req.thai.SendKeyBoReq.SendBoReq;
import coffee.model.LottoTransaction;
import coffee.transaction.vo.res.TransactionPayCostRes;
import coffee.transaction.vo.res.TransactionRes;
import coffee.web.vo.res.LottoWebTransactionRes.LottoGroupList;
import coffee.web.vo.res.LottoWebTransactionRes.dateList;
import framework.constant.LottoConstant;
import framework.utils.CommonJdbcTemplate;

@Repository
public class LottoTransactionDao {

	@Autowired
	private CommonJdbcTemplate commonJdbcTemplate;

	public List<TransactionRes> getAllTransaction() {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT  ");
		sqlBuilder.append(" 	lts.lotto_transaction_code, ");
		sqlBuilder.append(" 	lts.lotto_group_transaction_code, ");
		sqlBuilder.append(" 	lts.lotto_kind_code as kind_code,  ");
		sqlBuilder.append(" 	mlk.msd_lotto_kind_name as kind_name, ");
		sqlBuilder.append(" 	lts.lotto_number, ");
		sqlBuilder.append(" 	lts.prize_cost as prize, ");
		sqlBuilder.append(" 	lts.pay_cost, ");
		sqlBuilder.append(" 	'PENDING' as correct_number, ");
		sqlBuilder.append(" 	0.00 as prize_result, ");
		sqlBuilder.append(" 	'PENDING' as status ");
		sqlBuilder.append(" FROM lotto_transaction lts  ");
		sqlBuilder.append("     inner join msd_lotto_kind mlk ");
		sqlBuilder.append("         on lts.lotto_kind_code = mlk.msd_lotto_kind_code ");
		List<TransactionRes> dataFind = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(TransactionRes.class));
		return dataFind;
	}

	public List<TransactionRes> getTransactionByGroup(String groupCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append("  SELECT ");
		sqlBuilder.append("  	lts.lotto_transaction_code, ");
		sqlBuilder.append("  	lts.lotto_group_transaction_code, ");
		sqlBuilder.append("  	lts.lotto_kind_code as kind_code, ");
		sqlBuilder.append("  	mlk.msd_lotto_kind_name as kind_name, ");
		sqlBuilder.append("  	lts.lotto_number, ");
		sqlBuilder.append("  	lts.prize_cost as prize, ");
		sqlBuilder.append("  	lts.pay_cost, ");
		sqlBuilder.append("  	CASE ");
		sqlBuilder.append("  		WHEN lts.status = 'PENDING' THEN 'PENDING' ");
		sqlBuilder.append("  		ELSE lts.number_correct ");
		sqlBuilder.append("  	END AS correct_number, ");
		sqlBuilder.append("  	lts.status, ");
		sqlBuilder.append("  	CASE ");
		sqlBuilder.append("  		WHEN lts.status = 'WIN' THEN lts.prize_correct  ");
		sqlBuilder.append("  		ELSE 0.00 ");
		sqlBuilder.append("  	END AS prize_result, ");
		sqlBuilder.append("  	lts.status ");
		sqlBuilder.append("  FROM ");
		sqlBuilder.append("  	lotto_transaction lts ");
		sqlBuilder.append("  inner join msd_lotto_kind mlk on ");
		sqlBuilder.append("  	lts.lotto_kind_code = mlk.msd_lotto_kind_code ");
		sqlBuilder.append("  where ");
		sqlBuilder.append("  	lts.lotto_group_transaction_code = ? ");
		params.add(groupCode);
		List<TransactionRes> dataFind = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(TransactionRes.class));
		return dataFind;
	}

	public List<LottoGroupList> getTransactionWeb(String username, Date date) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT ");
		sqlBuilder.append(
				"	SUM(lgt.sum_group_bet) as sumGroupBet,count(lgt.status) as countStatus,lgt.status as groupStatus,CAST(lgt.created_at as DATE) ");
		sqlBuilder.append(" from lotto_group_transaction lgt ");
		sqlBuilder.append(
				" WHERE lgt.username = ? AND lgt.status !='REFUND' AND CAST(lgt.created_at as DATE) = ? GROUP BY CAST(lgt.created_at as DATE),lgt.status");
		params.add(username);
		params.add(date);

		List<LottoGroupList> dataFind = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(LottoGroupList.class));
		return dataFind;

	}

	public List<dateList> getTransactionWebDate(String username) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT CAST(lgt.created_at AS DATE) as transactionDate ");
		sqlBuilder.append(" from lotto_group_transaction lgt ");
		sqlBuilder.append(
				" WHERE lgt.username = ? AND lgt.status !='REFUND' AND cast(lgt.created_at as Date) BETWEEN  DateAdd(DD,-4,GETDATE()) and GETDATE() ");
		sqlBuilder.append(" GROUP BY CAST(lgt.created_at AS DATE) ORDER BY CAST(lgt.created_at AS DATE) DESC");
		params.add(username);
		List<dateList> dataFind = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(dateList.class));
		return dataFind;
	}

	public List<SendBoReq> findSum(String classCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT lt.username, SUM(lt.prize_correct) as prize from lotto_transaction lt ");
		sqlBuilder.append(" where lt.update_wallet is NULL and lt.status = 'WIN' and lt.lotto_class_code = ? ");
		params.add(classCode);
		sqlBuilder.append(" GROUP by lt.username ");
		List<SendBoReq> dataFind = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(SendBoReq.class));
		return dataFind;
	}

	public List<SendBoReq> findSum2(String kindCode, String classCode, String categoryCode, String username,
			String transactionCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT lt.username, lt.prize_correct as prize from lotto_transaction lt ");

		// sqlBuilder.append(" SELECT lt.username, SUM(lt.prize_correct) as prize from
		// lotto_transaction lt ");
		// sqlBuilder.append(" INNER JOIN msd_lotto_kind mlk ON mlk.msd_lotto_kind_code
		// = lt.lotto_kind_code ");
		// sqlBuilder.append(" INNER JOIN lotto_class lc ON lt.lotto_class_code =
		// lc.lotto_class_code ");
		sqlBuilder.append(" AND lc.lotto_class_code LIKE ? ");
		sqlBuilder.append(" AND lt.lotto_kind_code LIKE ? ");
		sqlBuilder.append(" AND lt.username LIKE ? ");
		sqlBuilder.append(" AND lt.lotto_group_transaction_code LIKE ? ");
		sqlBuilder.append(" where lt.update_wallet is NULL and lt.status = 'WIN' ");
		sqlBuilder.append(" AND lc.lotto_category_code LIKE ? ");
		sqlBuilder.append(" GROUP by lt.username ");

		params.add(classCode.trim());
		params.add(kindCode.trim());
		params.add(username.trim());
		params.add(transactionCode.trim());
		params.add(categoryCode.trim());
		List<SendBoReq> dataFind = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(SendBoReq.class));
		return dataFind;
	}

	public List<SendBoReq> findSum3(String kindCode, String classCode, String categoryCode, String username,
			String transactionCode, String transactionId) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT lt.username, SUM(lt.prize_correct) as prize from lotto_transaction lt ");
		sqlBuilder.append(" INNER JOIN msd_lotto_kind mlk ON mlk.msd_lotto_kind_code = lt.lotto_kind_code ");
		sqlBuilder.append(" INNER JOIN lotto_class lc ON lt.lotto_class_code = lc.lotto_class_code ");
		sqlBuilder.append(" WHERE lt.update_wallet is NULL AND lt.status = 'WIN' ");
		sqlBuilder.append(" AND lt.lotto_kind_code LIKE ? ");
		sqlBuilder.append(" AND lc.lotto_class_code LIKE ? ");
		sqlBuilder.append(" AND lc.lotto_category_code LIKE ? ");
		sqlBuilder.append(" AND lt.username LIKE ? ");
		sqlBuilder.append(" AND lt.lotto_group_transaction_code LIKE ? ");
		sqlBuilder.append(" AND lt.lotto_transaction_id = ? ");
		sqlBuilder.append(" GROUP by lt.username");
		params.add("%" + kindCode.trim() + "%");
		params.add("%" + classCode.trim() + "%");
		params.add("%" + categoryCode.trim() + "%");
		params.add("%" + username.trim() + "%");
		params.add("%" + transactionCode.trim() + "%");
		params.add(transactionId.trim());
		List<SendBoReq> dataFind = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(SendBoReq.class));
		return dataFind;
	}

	public List<SendBoReq> findSum4(String kindCode, String classCode, String categoryCode, String username,
			String transactionCode, List<String> detransactionId) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" SELECT lt.username, SUM(lt.prize_correct) as prize from lotto_transaction lt ");
		sqlBuilder.append(" INNER JOIN msd_lotto_kind mlk ON mlk.msd_lotto_kind_code = lt.lotto_kind_code ");
		sqlBuilder.append(" INNER JOIN lotto_class lc ON lt.lotto_class_code = lc.lotto_class_code ");
		sqlBuilder.append(" WHERE lt.update_wallet is NULL AND lt.status = 'WIN' ");
		sqlBuilder.append(" AND lt.lotto_kind_code LIKE ? ");
		sqlBuilder.append(" AND lc.lotto_class_code LIKE ? ");
		sqlBuilder.append(" AND lc.lotto_category_code LIKE ? ");
		sqlBuilder.append(" AND lt.username LIKE ? ");
		sqlBuilder.append(" AND lt.lotto_group_transaction_code LIKE ? ");
		params.add("%" + kindCode.trim() + "%");
		params.add("%" + classCode.trim() + "%");
		params.add("%" + categoryCode.trim() + "%");
		params.add("%" + username.trim() + "%");
		params.add("%" + transactionCode.trim() + "%");
		for (String delistId : detransactionId) {
			sqlBuilder.append(" AND lt.lotto_transaction_id != ? ");
			params.add(delistId.trim());
		}
		sqlBuilder.append(" GROUP by lt.username");

		System.out.println(sqlBuilder.toString());
		List<SendBoReq> dataFind = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(SendBoReq.class));
		return dataFind;
	}

	public void updateTransactionDb2(Boolean updateWallet, String remark, Date date, String paidBy, String kindCode,
			String classCode, String categoryCode, String username, String transactionCode) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" UPDATE lt");
		sqlBuilder.append(" SET lt.update_wallet = ?, lt.reject_remark = ?, lt.paid_at = ?, lt.paid_by = ?");
		sqlBuilder.append(" FROM lotto_transaction lt ");
		sqlBuilder.append(" INNER JOIN msd_lotto_kind mlk ON mlk.msd_lotto_kind_code = lt.lotto_kind_code ");
		sqlBuilder.append(" INNER JOIN lotto_class lc ON lt.lotto_class_code = lc.lotto_class_code ");
		sqlBuilder.append(" where lt.update_wallet is null ");
		sqlBuilder.append(" AND mlk.msd_lotto_kind_code LIKE ? ");
		sqlBuilder.append(" AND lc.lotto_class_code LIKE ? ");
		sqlBuilder.append(" AND lc.lotto_category_code LIKE ? ");
		sqlBuilder.append(" AND lt.username LIKE ? ");
		sqlBuilder.append(" AND lt.lotto_group_transaction_code LIKE ?");
		params.add(updateWallet);
		params.add(remark);
		params.add(date);
		params.add(paidBy);
		params.add("%" + kindCode.trim() + "%");
		params.add("%" + classCode.trim() + "%");
		params.add("%" + categoryCode.trim() + "%");
		params.add("%" + username.trim() + "%");
		params.add("%" + transactionCode.trim() + "%");
		commonJdbcTemplate.executeUpdate(sqlBuilder.toString(), params.toArray());

	}

	public void updateTransactionDbByListId(Boolean updateWallet, String remark, Date date, String paidBy,
			String kindCode, String classCode, String categoryCode, String username, String transactionCode,
			String transactionId) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" UPDATE lt");
		sqlBuilder.append(" SET lt.update_wallet = ?, lt.reject_remark = ?, lt.paid_at = ?, lt.paid_by = ?");
		sqlBuilder.append(" FROM lotto_transaction lt ");
		sqlBuilder.append(" INNER JOIN msd_lotto_kind mlk ON mlk.msd_lotto_kind_code = lt.lotto_kind_code ");
		sqlBuilder.append(" INNER JOIN lotto_class lc ON lt.lotto_class_code = lc.lotto_class_code ");
		sqlBuilder.append(" where lt.update_wallet is null ");
		sqlBuilder.append(" AND mlk.msd_lotto_kind_code LIKE ? ");
		sqlBuilder.append(" AND lc.lotto_class_code LIKE ? ");
		sqlBuilder.append(" AND lc.lotto_category_code LIKE ? ");
		sqlBuilder.append(" AND lt.username LIKE ? ");
		sqlBuilder.append(" AND lt.lotto_group_transaction_code LIKE ?");
		sqlBuilder.append(" AND lt.lotto_transaction_id = ? ");
		params.add(updateWallet);
		params.add(remark);
		params.add(date);
		params.add(paidBy);
		params.add("%" + kindCode.trim() + "%");
		params.add("%" + classCode.trim() + "%");
		params.add("%" + categoryCode.trim() + "%");
		params.add("%" + username.trim() + "%");
		params.add("%" + transactionCode.trim() + "%");
		params.add(transactionId);
		commonJdbcTemplate.executeUpdate(sqlBuilder.toString(), params.toArray());

	}

	public void updateTransactionDbByOneId(Boolean updateWallet, String remark, Date date, String paidBy,
			String kindCode, String classCode, String categoryCode, String username, String transactionCode,
			String transactionId) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" UPDATE lt");
		sqlBuilder.append(" SET lt.update_wallet = ?, lt.reject_remark = ?, lt.paid_at = ?, lt.paid_by = ?");
		sqlBuilder.append(" FROM lotto_transaction lt ");
		sqlBuilder.append(" INNER JOIN msd_lotto_kind mlk ON mlk.msd_lotto_kind_code = lt.lotto_kind_code ");
		sqlBuilder.append(" INNER JOIN lotto_class lc ON lt.lotto_class_code = lc.lotto_class_code ");
		sqlBuilder.append(" where lt.update_wallet is null ");
		sqlBuilder.append(" AND mlk.msd_lotto_kind_code LIKE ? ");
		sqlBuilder.append(" AND lc.lotto_class_code LIKE ? ");
		sqlBuilder.append(" AND lc.lotto_category_code LIKE ? ");
		sqlBuilder.append(" AND lt.username LIKE ? ");
		sqlBuilder.append(" AND lt.lotto_group_transaction_code LIKE ?");
		sqlBuilder.append(" AND lt.lotto_transaction_id = ? ");
		params.add(updateWallet);
		params.add(remark);
		params.add(date);
		params.add(paidBy);
		params.add("%" + kindCode.trim() + "%");
		params.add("%" + classCode.trim() + "%");
		params.add("%" + categoryCode.trim() + "%");
		params.add("%" + username.trim() + "%");
		params.add("%" + transactionCode.trim() + "%");
		params.add(transactionId);
		commonJdbcTemplate.executeUpdate(sqlBuilder.toString(), params.toArray());

	}

	public void updateTransactionDbByDeSelectListId(Boolean updateWallet, String remark, Date date, String paidBy,
			String kindCode, String classCode, String categoryCode, String username, String transactionCode,
			List<String> detransactionId) {
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(" UPDATE lt");
		sqlBuilder.append(" SET lt.update_wallet = ?, lt.reject_remark = ?, lt.paid_at = ?, lt.paid_by = ?");
		sqlBuilder.append(" FROM lotto_transaction lt ");
		sqlBuilder.append(" INNER JOIN msd_lotto_kind mlk ON mlk.msd_lotto_kind_code = lt.lotto_kind_code ");
		sqlBuilder.append(" INNER JOIN lotto_class lc ON lt.lotto_class_code = lc.lotto_class_code ");
		sqlBuilder.append(" where lt.update_wallet is null ");
		sqlBuilder.append(" AND mlk.msd_lotto_kind_code LIKE ? ");
		sqlBuilder.append(" AND lc.lotto_class_code LIKE ? ");
		sqlBuilder.append(" AND lc.lotto_category_code LIKE ? ");
		sqlBuilder.append(" AND lt.username LIKE ? ");
		sqlBuilder.append(" AND lt.lotto_group_transaction_code LIKE ?");

		params.add(updateWallet);
		params.add(remark);
		params.add(date);
		params.add(paidBy);
		params.add("%" + kindCode.trim() + "%");
		params.add("%" + classCode.trim() + "%");
		params.add("%" + categoryCode.trim() + "%");
		params.add("%" + username.trim() + "%");
		params.add("%" + transactionCode.trim() + "%");
		for (String delistIds : detransactionId) {
			sqlBuilder.append(" AND lt.lotto_transaction_id != ? ");
			params.add(delistIds);
		}

		commonJdbcTemplate.executeUpdate(sqlBuilder.toString(), params.toArray());
	}

	public LottoTransaction findHasUser(String classCode, String installment, int roundYeekee, String lottoKindCode,
			String username, String LottoNumber) {

		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();

		sqlBuilder.append(" SELECT TOP(1) * FROM LOTTO_TRANSACTION ");
		sqlBuilder.append(" 	WHERE LOTTO_CLASS_CODE = ? ");
		params.add(classCode);
		sqlBuilder.append(" 	AND INSTALLMENT = ? ");
		params.add(installment);
		sqlBuilder.append(" 	AND ROUND_YEEKEE = ? ");
		params.add(roundYeekee);
		sqlBuilder.append(" 	AND LOTTO_KIND_CODE = ? ");
		params.add(lottoKindCode);
		sqlBuilder.append(" 	AND USERNAME = ? ");
		params.add(username);
		sqlBuilder.append(" 	AND LOTTO_NUMBER = ? ");
		params.add(LottoNumber);

		LottoTransaction dataRes = commonJdbcTemplate.executeQueryForObject(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(LottoTransaction.class));
		return dataRes;
	}

	public List<TransactionPayCostRes> findAllPayCost(String lottoCategory, String lottoClassCode, String installment,
			Integer roundYeekee) {
		System.out.println("findAllPayCost() :::" + lottoCategory + ":::" + lottoClassCode + ":::" + installment + ":::"
				+ roundYeekee);
		StringBuilder sqlBuilder = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		sqlBuilder.append(
				" SELECT lt.lotto_group_transaction_code, lt.username, SUM(lt.pay_cost) as pay_cost, lt.installment, lt.round_yeekee , lt.lotto_class_code ");
		sqlBuilder.append(" FROM lotto_transaction lt ");
		sqlBuilder.append(" WHERE 1=1");
		sqlBuilder.append(" AND lt.lotto_class_code = ?");
		params.add(lottoClassCode);
		sqlBuilder.append(" AND lt.installment = ?");
		params.add(installment);
		if (lottoCategory.equals(LottoConstant.CATEGORY.YEEKEE)) {
			sqlBuilder.append(" AND lt.round_yeekee = ?");
			params.add(roundYeekee);
		}
		sqlBuilder.append(" AND lt.status = 'CANCEL'");
		sqlBuilder.append(
				" GROUP by lt.lotto_group_transaction_code, lt.username, lt.installment, lt.round_yeekee, lt.lotto_class_code ");
		List<TransactionPayCostRes> dataFind = commonJdbcTemplate.executeQuery(sqlBuilder.toString(), params.toArray(),
				BeanPropertyRowMapper.newInstance(TransactionPayCostRes.class));
		return dataFind;
	}
}
