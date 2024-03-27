
package coffee.lottoresult.vo.req.thai;

import java.util.List;

import lombok.Data;
@Data
public class Response {

    private String youtubeUrl;
    private String pdfUrl;
    private String date;
    private List<Integer> period = null;
    private Object remark;
    private Integer status;
    private String sheetId;
    private Data1 data;
    private DisplayDate displayDate;
    
}
