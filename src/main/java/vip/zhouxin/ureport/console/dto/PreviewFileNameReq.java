package vip.zhouxin.ureport.console.dto;

/**
 * @author xinxingzhou
 * @since 2022/1/26
 */
public class PreviewFileNameReq extends PreviewReq{

    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "PreviewFileNameReq{" +
                "customParameters=" + getCustomParameters() +
                ", fileName='" + fileName + '\'' +
                ", pageIndex='" + getPageIndex() + '\'' +
                ", template='" + getTemplate() + '\'' +
                ", title='" + getTitle() + '\'' +
                ", tools='" + getTools() + '\'' +
                '}';
    }
}
