package framework.model;

import lombok.Data;

@Data
public class ImgUploadRes {
    private String status;
    private String massage;
    private imgRes data;

    @Data
    public static class imgRes {
        private String savedPath;
    }
}
