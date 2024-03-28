package coffee.transaction.vo.res;

import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper=false)
public class TransactionGroupDetailRes extends TransactionGroupRes {
	private List<TransactionRes> listTrantsaction;
}
