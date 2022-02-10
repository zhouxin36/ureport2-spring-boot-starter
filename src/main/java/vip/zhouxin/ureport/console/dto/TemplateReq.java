package vip.zhouxin.ureport.console.dto;

/**
 * @author xinxingzhou
 * @since 2022/1/25
 */
public class TemplateReq {
    private String template;

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    @Override
    public String toString() {
        return "FileReq{" +
                "template='" + template + '\'' +
                '}';
    }
}
