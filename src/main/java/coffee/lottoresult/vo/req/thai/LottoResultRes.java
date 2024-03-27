package coffee.lottoresult.vo.req.thai;
import lombok.Data;

@Data
public class LottoResultRes {

    private String statusMessage;
    private Integer statusCode;
    private Object message;
    private Boolean status;
    private Response response;

}
