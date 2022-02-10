package vip.zhouxin.ureport.console.dto;

import vip.zhouxin.ureport.core.definition.Paper;

/**
 * @author xinxingzhou
 * @since 2022/1/26
 */
public class NewPagingReq extends PreviewReq{
    private Paper paper;

    public Paper getPaper() {
        return paper;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }

    @Override
    public String toString() {
        return "NewPagingReq{" +
                "customParameters=" + getCustomParameters() +
                ", pageIndex='" + getPageIndex() + '\'' +
                ", paper=" + paper +
                ", template='" + getTemplate() + '\'' +
                ", title='" + getTitle() + '\'' +
                ", tools='" + getTools() + '\'' +
                '}';
    }
}
