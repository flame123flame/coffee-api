package coffee.lottoreport.vo.res;

import java.util.List;

import coffee.model.CloseNumber;
import lombok.Data;

@Data
public class GetAllCloseNumber {
    private String kindCode;
    private List<CloseNumber> listCloseNumber;
}