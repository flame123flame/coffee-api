package coffee.yeekeeResult.vo.res;

import java.util.List;

import lombok.Data;

@Data
public class YeekeeNumberRes {
	String collectNumber;
	String kindCode;
	List<String> number;
}
