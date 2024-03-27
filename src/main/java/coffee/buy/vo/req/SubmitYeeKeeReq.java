package coffee.buy.vo.req;

import lombok.Data;

@Data
public class SubmitYeeKeeReq {
    private int round;
    private Long value;
    private String installment;
    private String classCode;
    private String username;
}