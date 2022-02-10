package vip.zhouxin.ureport.console.dto;

/**
 * @author xinxingzhou
 * @since 2022/1/25
 */
public class SaveReportFileReq {
    private String file;

    private String content;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "SaveReportFileReq{" +
                "file='" + file + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
