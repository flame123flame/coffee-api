package coffee.buy.vo.req;

import lombok.Data;

@Data
public class SendHttpModel<T> {
    final private String sigKey;
    final private T data;
}