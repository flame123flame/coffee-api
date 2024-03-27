package framework.model;


import lombok.Data;

@Data
public class ImgUploadReq   {
    private String data;
    private String prefix = "img";
    private String path;
}
