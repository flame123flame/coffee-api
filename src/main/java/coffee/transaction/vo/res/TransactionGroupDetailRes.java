package coffee.transaction.vo.res;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class TransactionGroupDetailRes extends TransactionGroupRes {
	private List<TransactionRes> listTrantsaction;
}
