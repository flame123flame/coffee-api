package framework.utils;

import java.util.ArrayList;
import java.util.List;

import framework.model.DatatableFilter;
import framework.model.DatatableSort;

public class DatatableUtils {

	public static String countForDatatable(String sql, List<DatatableFilter> filter) {
        StringBuilder sqlBuilger = new StringBuilder();
        sqlBuilger.append("select count(1) from  ");
        if (sql.startsWith("select") || sql.startsWith("SELECT")) {
            sqlBuilger.append(" (");
            sqlBuilger.append(sql);
            sqlBuilger.append(createFilterString( filter));
            sqlBuilger.append("  ) as tb");
        } else {
            sqlBuilger.append(sql);
            sqlBuilger.append("  tb");
            sqlBuilger.append(createFilterString( filter));
        }

        return sqlBuilger.toString();
	}

	public static String countForDatatable(String sql, List<DatatableFilter> filter, String join) {
        StringBuilder sqlBuilger = new StringBuilder();
        sqlBuilger.append("select count(1) from  ");
        if (sql.startsWith("select") || sql.startsWith("SELECT")) {
            sqlBuilger.append(" (");
            sqlBuilger.append(sql);
            sqlBuilger.append(createFilterString( filter));
            sqlBuilger.append("  ) as tb");
        } else {
            sqlBuilger.append(sql);
            sqlBuilger.append("  tb");
            sqlBuilger.append(createFilterString( filter));
        }
        sqlBuilger.append(" ");
        sqlBuilger.append(join);
        sqlBuilger.append(" ");
        List<DatatableSort> fakeSort = new ArrayList<>();
        sqlBuilger.append(createSortFilterString(fakeSort, filter));
        return sqlBuilger.toString();

	}

	public static String limitForDataTable(String sql, int page, int length, List<DatatableSort> sort, List<DatatableFilter> filter) {
        page = page * length;
        StringBuilder sqlBuilger = new StringBuilder();
        sqlBuilger.append(" select * from   ");
        if (sql.startsWith("select") || sql.startsWith("SELECT")) {
            sqlBuilger.append(" (");
            sqlBuilger.append(sql);
            sqlBuilger.append(" ");
            sqlBuilger.append(createSortFilterString(sort, filter));
            sqlBuilger.append(" OFFSET " + page + " ROWS FETCH NEXT " + length + " ROWS ONLY ");
            sqlBuilger.append("  ) as tb");
        } else {
            sqlBuilger.append(sql);
            sqlBuilger.append("  tb");
            sqlBuilger.append(createSortFilterString(sort, filter));
            sqlBuilger.append(" OFFSET " + page + " ROWS FETCH NEXT " + length + " ROWS ONLY ");
        }
        return sqlBuilger.toString();
	}

	public static String limitForDataTable(String sql, int page, int length, List<DatatableSort> sort, List<DatatableFilter> filter, String join) {
        page = page * length;

        StringBuilder sqlBuilger = new StringBuilder();
        sqlBuilger.append(" select tb.* from   ");
        if (sql.startsWith("select") || sql.startsWith("SELECT")) {
            sqlBuilger.append(" (");
            sqlBuilger.append(sql);
            sqlBuilger.append("  ) as tb");
        } else {
            sqlBuilger.append(sql);
            sqlBuilger.append("  tb");
        }
        sqlBuilger.append(" ");
        sqlBuilger.append(join);
        sqlBuilger.append(" ");
        sqlBuilger.append(createSortFilterString(sort, filter));
        sqlBuilger.append(" OFFSET " + page + " ROWS FETCH NEXT " + length + " ROWS ONLY ");
        return sqlBuilger.toString();
	}

	private static String createSortFilterString(List<DatatableSort> sort, List<DatatableFilter> filter) {
        StringBuilder sqlBuilger = new StringBuilder();
        if (filter.size() != 0 && filter != null) {
            sqlBuilger.append("  where");
            int i = 0;
            for (DatatableFilter item : filter) {
                if (i > 0) {
                    sqlBuilger.append(" and ");
                }
                sqlBuilger.append(" ").append(item.getColumn()).append(" ");
                System.out.println(item.getOp());
                if (item.getOp().equals("startWith")) {
                    sqlBuilger.append(" LIKE ");
                    sqlBuilger.append(" '%").append(item.getValue()).append("' ");
                } else if (item.getOp().equals("endWith")) {
                    sqlBuilger.append(" LIKE ");
                    sqlBuilger.append(" '").append(item.getValue()).append("%' ");
                } else if (item.getOp().equals("contain")) {
                    sqlBuilger.append(" LIKE ");
                    sqlBuilger.append(" '%").append(item.getValue()).append("%' ");
                    
                }else if (item.getOp().equals("IS NULL")) {
                    sqlBuilger.append(" IS NULL ");
                    sqlBuilger.append(item.getValue().trim());
                }else if (item.getOp().equals("IS NOT NULL")) {
                    sqlBuilger.append(" IS NOT NULL ");
                } else {
                    sqlBuilger.append(item.getOp()).append(" '").append(item.getValue()).append("' ");
                }
                i++;
            }
        }

        sqlBuilger.append("  order by");
        if (sort.size() != 0 && sort != null) {
            int i = 0;
            for (DatatableSort item : sort) {
                if (i > 0) {
                    sqlBuilger.append(" , ");
                }
                sqlBuilger.append(" ").append(item.getColumn()).append(" ").append(item.getOrder());
                i++;
            }
        } else {
            sqlBuilger.append("  1 asc ");
        }
        return sqlBuilger.toString();
	}

    private static String createFilterString(List<DatatableFilter> filter) {
        StringBuilder sqlBuilger = new StringBuilder();
        if (filter.size() != 0 && filter != null) {
            sqlBuilger.append("  where");
            int i = 0;
            for (DatatableFilter item : filter) {
                if (i > 0) {
                    sqlBuilger.append(" and ");
                }
                sqlBuilger.append(" ").append(item.getColumn()).append(" ");
                System.out.println(item.getOp());
                if (item.getOp().equals("startWith")) {
                    sqlBuilger.append(" LIKE ");
                    sqlBuilger.append(" '%").append(item.getValue()).append("' ");
                } else if (item.getOp().equals("endWith")) {
                    sqlBuilger.append(" LIKE ");
                    sqlBuilger.append(" '").append(item.getValue()).append("%' ");
                } else if (item.getOp().equals("contain")) {
                    sqlBuilger.append(" LIKE ");
                    sqlBuilger.append(" '%").append(item.getValue()).append("%' ");

                }else if (item.getOp().equals("IS NULL")) {
                    sqlBuilger.append(" IS NULL ");
                }else if (item.getOp().equals("IS NOT NULL")) {
                    sqlBuilger.append(" IS NOT NULL ");
                } else {
                    sqlBuilger.append(item.getOp()).append(" '").append(item.getValue()).append("' ");
                }
                i++;
            }
        }
        return sqlBuilger.toString();
    }

	// ต้องไม่ส่ง order by ต่อท้าย
	public static String countForDatatable(String sql) {
		StringBuilder sqlBuilger = new StringBuilder();
		sqlBuilger.append("select count(1) from ( ");
		sqlBuilger.append(sql.toString());
		sqlBuilger.append(" ) counttb ");
		return sqlBuilger.toString();
	}

	// ต้องไม่ส่ง order by ต่อท้าย
	// ตัวอย่าง orderBy: tb.id asc, tb.column desc,
	// sql server 2012
	// https://docs.microsoft.com/en-us/sql/t-sql/queries/select-order-by-clause-transact-sql?view=sql-server-ver15#Offset
	public static String limitForDataTable(String sql, String orderBy, int page, int length, List<DatatableSort> sort) {
		page = page * length;
		StringBuilder sqlBuilger = new StringBuilder();
		sqlBuilger.append(" select * from (  ");
		sqlBuilger.append(sql);
		sqlBuilger.append(" ) tb order by ").append(orderBy).append(" 1 asc ");
		if (sort != null) {
			sort.forEach((item) -> {
				sqlBuilger.append(", tb.").append(item.getColumn()).append(" ").append(item.getOrder());
			});
		}
		sqlBuilger.append(" OFFSET " + page + " ROWS FETCH NEXT " + length + " ROWS ONLY ");
		return sqlBuilger.toString();
	}

	// ตัวอย่าง
	// WITH OrderedOrders AS
	// (
	// SELECT ROW_NUMBER() OVER (ORDER BY role_code) AS RowNumber, *
	// FROM user_page_dtl
	// )
	// SELECT *
	// FROM OrderedOrders
	// WHERE RowNumber BETWEEN 10 AND 20;
	//
	// https://docs.microsoft.com/en-us/sql/t-sql/functions/row-number-transact-sql?view=sql-server-ver15#c-returning-a-subset-of-rows
	public static String limitForDataTable2(String sql, String orderBy, int page, int length, List<DatatableSort> sort) {
		page = page * length;
		StringBuilder sqlBuilger = new StringBuilder();
		sqlBuilger.append(" WITH OrderedOrders AS ( ");
		sqlBuilger.append(sql);
		sqlBuilger.append(" ) SELECT RowNumber, * FROM OrderedOrders ");
		sqlBuilger.append(" WHERE RowNumber BETWEEN ");
		// Start
		sqlBuilger.append(page);
		sqlBuilger.append(" AND ");
		// End
		sqlBuilger.append(page + length);
		return sqlBuilger.toString();
	}

	public static String genarateOrderBy(List<DatatableSort> sort) {
		StringBuilder orderByBuilder = new StringBuilder();
		sort.forEach((item) -> {
			orderByBuilder.append(item.getColumn()).append(" ").append( item.getOrder()).append(", ");
		});
		orderByBuilder.append(" 1 asc ");
		return orderByBuilder.toString();
	}

	public static String genarateOrderBy(List<DatatableSort> sort, String tableName) {
		StringBuilder orderByBuilder = new StringBuilder();
		sort.forEach((item) -> {
			orderByBuilder
			.append(tableName)
			.append(".")
			.append(item.getColumn().replaceAll(" ", ""))
			.append(" ")
			.append(item.getOrder())
			.append(", ");
		});
		orderByBuilder.append(" 1 asc ");
		return orderByBuilder.toString();
	}
}
