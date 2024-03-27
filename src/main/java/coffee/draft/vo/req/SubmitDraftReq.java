package coffee.draft.vo.req;

import lombok.Data;

@Data
public class SubmitDraftReq {
    private String draftCode;
    private Boolean isApprove;
    private String classCode;
}