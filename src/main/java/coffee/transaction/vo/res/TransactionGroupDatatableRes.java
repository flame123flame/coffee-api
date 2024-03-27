package coffee.transaction.vo.res;

import coffee.model.LottoCategory;
import coffee.model.LottoClass;
import coffee.web.vo.res.LottoCategoryListRes;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TransactionGroupDatatableRes {
    private Long lottoGroupTransactionId;
    private String lottoGroupTransactionCode;
    private String lottoClassCode;
    private String username;
    private BigDecimal sumGroupBet;
    private String createdBy;
    private Date createdAt;
    private String installment;
    private String className;
    private String typeName;
    private String status;
    private Integer roundYeekee;
}
