package vip.zhouxin.ureport.console.dto;

import java.util.Collections;
import java.util.Map;

/**
 * @author xinxingzhou
 * @since 2022/1/25
 */
public class PreviewReq {

    private String template;
    private String pageIndex;
    private String title;
    private String tools;
    private Map<String,Object> customParameters = Collections.emptyMap();

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(String pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTools() {
        return tools;
    }

    public void setTools(String tools) {
        this.tools = tools;
    }

    public Map<String, Object> getCustomParameters() {
        return customParameters;
    }

    public void setCustomParameters(Map<String, Object> customParameters) {
        this.customParameters = customParameters;
    }

    @Override
    public String toString() {
        return "PreviewReq{" +
                "customParameters=" + customParameters +
                ", pageIndex='" + pageIndex + '\'' +
                ", template='" + template + '\'' +
                ", title='" + title + '\'' +
                ", tools='" + tools + '\'' +
                '}';
    }
}
