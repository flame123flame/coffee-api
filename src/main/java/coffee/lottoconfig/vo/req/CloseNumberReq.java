package coffee.lottoconfig.vo.req;

import java.util.Date;

import lombok.Data;
@Data
public class CloseNumberReq {
	private Long closeNumberId;
    private String closeNumberCode;
    private String lottoClassCode;
    private String lottoNumber;
    private Boolean enable;
    private Boolean isManual;
    private String createdBy;
    private Date createdAt;
    private String updatedBy;
    private Date updatedAt;
    private String msdLottoKindCode;
}
