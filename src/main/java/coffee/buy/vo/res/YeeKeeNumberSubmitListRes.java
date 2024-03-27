package coffee.buy.vo.res;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class YeeKeeNumberSubmitListRes {
    @JsonProperty("order")
    private Integer order;

    @JsonProperty("username")
    private String username;

    @JsonProperty("numberSubmit")
    private String numberSubmit;

    @JsonProperty("createdDate")
    private String createdDate;
}