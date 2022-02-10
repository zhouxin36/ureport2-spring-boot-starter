package vip.zhouxin.ureport.console.dto;

import vip.zhouxin.ureport.console.html.Tools;

import java.util.Map;

public class PreviewRes {

    private String title;
    private String content;
    private Boolean error;
    private String searchFormJs;
    private String downSearchFormHtml;
    private String upSearchFormHtml;
    private String style;
    private String reportAlign;
    private Integer totalPage;
    private Integer totalPageWithCol;
    private Integer pageIndex;
    private String chartDatas;
    private String file;
    private Integer intervalRefreshValue;
    private Map<String, Object> customParameters;
    private String toolsInfo;
    private Boolean hasTools;
    private Tools tools;
    private String contextPath;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSearchFormJs() {
        return searchFormJs;
    }

    public void setSearchFormJs(String searchFormJs) {
        this.searchFormJs = searchFormJs;
    }

    public String getDownSearchFormHtml() {
        return downSearchFormHtml;
    }

    public void setDownSearchFormHtml(String downSearchFormHtml) {
        this.downSearchFormHtml = downSearchFormHtml;
    }

    public String getUpSearchFormHtml() {
        return upSearchFormHtml;
    }

    public void setUpSearchFormHtml(String upSearchFormHtml) {
        this.upSearchFormHtml = upSearchFormHtml;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getReportAlign() {
        return reportAlign;
    }

    public void setReportAlign(String reportAlign) {
        this.reportAlign = reportAlign;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getTotalPageWithCol() {
        return totalPageWithCol;
    }

    public void setTotalPageWithCol(Integer totalPageWithCol) {
        this.totalPageWithCol = totalPageWithCol;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getChartDatas() {
        return chartDatas;
    }

    public void setChartDatas(String chartDatas) {
        this.chartDatas = chartDatas;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Integer getIntervalRefreshValue() {
        return intervalRefreshValue;
    }

    public void setIntervalRefreshValue(Integer intervalRefreshValue) {
        this.intervalRefreshValue = intervalRefreshValue;
    }

    public Map<String, Object> getCustomParameters() {
        return customParameters;
    }

    public void setCustomParameters(Map<String, Object> customParameters) {
        this.customParameters = customParameters;
    }

    public Tools getTools() {
        return tools;
    }

    public void setTools(Tools tools) {
        this.tools = tools;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getError() {
        return error;
    }

    public Boolean getHasTools() {
        return hasTools;
    }

    public void setHasTools(Boolean hasTools) {
        this.hasTools = hasTools;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getToolsInfo() {
        return toolsInfo;
    }

    public void setToolsInfo(String toolsInfo) {
        this.toolsInfo = toolsInfo;
    }

    @Override
    public String toString() {
        return "PreviewRes{" +
                "chartDatas='" + chartDatas + '\'' +
                ", content='" + content + '\'' +
                ", contextPath='" + contextPath + '\'' +
                ", customParameters='" + customParameters + '\'' +
                ", downSearchFormHtml='" + downSearchFormHtml + '\'' +
                ", error=" + error +
                ", file='" + file + '\'' +
                ", hasTools=" + hasTools +
                ", intervalRefreshValue=" + intervalRefreshValue +
                ", pageIndex=" + pageIndex +
                ", reportAlign='" + reportAlign + '\'' +
                ", searchFormJs='" + searchFormJs + '\'' +
                ", style='" + style + '\'' +
                ", title='" + title + '\'' +
                ", tool='" + toolsInfo + '\'' +
                ", tools=" + tools +
                ", totalPage=" + totalPage +
                ", totalPageWithCol=" + totalPageWithCol +
                ", upSearchFormHtml='" + upSearchFormHtml + '\'' +
                '}';
    }
}
