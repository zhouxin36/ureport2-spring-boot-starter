package vip.zhouxin.ureport.console.controller;

import vip.zhouxin.ureport.core.build.Context;
import vip.zhouxin.ureport.core.build.ReportBuilder;
import vip.zhouxin.ureport.core.build.paging.Page;
import vip.zhouxin.ureport.core.cache.CacheUtils;
import vip.zhouxin.ureport.core.chart.ChartData;
import vip.zhouxin.ureport.console.MobileUtils;
import vip.zhouxin.ureport.console.dto.PreviewReq;
import vip.zhouxin.ureport.console.dto.PreviewRes;
import vip.zhouxin.ureport.console.exception.ReportDesignException;
import vip.zhouxin.ureport.console.html.Tools;
import vip.zhouxin.ureport.core.definition.Paper;
import vip.zhouxin.ureport.core.definition.searchform.FormPosition;
import vip.zhouxin.ureport.core.exception.ReportComputeException;
import vip.zhouxin.ureport.core.export.ExportManager;
import vip.zhouxin.ureport.core.export.FullPageData;
import vip.zhouxin.ureport.core.export.PageBuilder;
import vip.zhouxin.ureport.core.export.ReportRender;
import vip.zhouxin.ureport.core.export.html.HtmlProducer;
import vip.zhouxin.ureport.core.export.html.HtmlReport;
import vip.zhouxin.ureport.core.export.html.SearchFormData;
import vip.zhouxin.ureport.core.model.Report;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xinxingzhou
 * @since 2022/1/21
 */
@RestController
public class HtmlPreviewController extends AbstractController {

    public static final String PREFIX = CONTENT + "/preview";

    private final ExportManager exportManager;
    private final ReportBuilder reportBuilder;
    private final ReportRender reportRender;
    private final HtmlProducer htmlProducer = new HtmlProducer();

    public HtmlPreviewController(ExportManager exportManager, ReportBuilder reportBuilder, ReportRender reportRender, ObjectMapper objectMapper) {
        super(objectMapper);
        this.exportManager = exportManager;
        this.reportBuilder = reportBuilder;
        this.reportRender = reportRender;
    }

    @RequestMapping(value = PREFIX, method = RequestMethod.POST)
    public PreviewRes preview(@RequestBody PreviewReq req, HttpServletRequest request) {
        PreviewRes res = new PreviewRes();
        HtmlReport htmlReport = null;
        String errorMsg = null;
        try {
            htmlReport = loadReport(req);
        } catch (Exception ex) {
            if (!(ex instanceof ReportDesignException)) {
                ex.printStackTrace();
            }
            errorMsg = buildExceptionMessage(ex);
        }
        String title = buildTitle(req);
        res.setTitle(title);
        if (htmlReport == null) {
            res.setContent("<div style='color:red'><strong>报表计算出错，错误信息如下：</strong><br><div style=\"margin:10px\">" + errorMsg + "</div></div>");
            res.setError(true);
            res.setSearchFormJs("");
            res.setDownSearchFormHtml("");
            res.setUpSearchFormHtml("");
        } else {
            SearchFormData formData = htmlReport.getSearchFormData();
            if (formData != null) {
                res.setSearchFormJs(formData.getJs());
                if (formData.getFormPosition().equals(FormPosition.up)) {
                    res.setUpSearchFormHtml(formData.getHtml());
                    res.setDownSearchFormHtml("");
                } else {
                    res.setDownSearchFormHtml(formData.getHtml());
                    res.setUpSearchFormHtml("");
                }
            } else {
                res.setSearchFormJs("");
                res.setDownSearchFormHtml("");
                res.setUpSearchFormHtml("");
            }
            res.setContent(htmlReport.getContent());
            res.setStyle(htmlReport.getStyle());
            res.setReportAlign(htmlReport.getReportAlign());
            res.setTotalPage(htmlReport.getTotalPage());
            res.setTotalPageWithCol(htmlReport.getTotalPageWithCol());
            res.setPageIndex(htmlReport.getPageIndex());
            res.setChartDatas(convertJson(htmlReport.getChartDatas()));
            res.setError(false);
            res.setFile(req.getTemplate());
            res.setIntervalRefreshValue(htmlReport.getHtmlIntervalRefreshValue());
            res.setCustomParameters(htmlReport.getCustomParameters());
            res.setToolsInfo("");
            Tools tools;
            if (MobileUtils.isMobile(request)) {
                tools = new Tools(false);
                tools.setShow(false);
            } else {
                String toolsInfo = req.getTools();
                if (StringUtils.isNotBlank(toolsInfo)) {
                    tools = new Tools(false);
                    if (toolsInfo.equals("0")) {
                        tools.setShow(false);
                    } else {
                        String[] infos = toolsInfo.split(",");
                        for (String name : infos) {
                            tools.doInit(name);
                        }
                    }
                    res.setToolsInfo(toolsInfo);
                    res.setHasTools(true);
                } else {
                    tools = new Tools(true);
                }
            }
            res.setTools(tools);
        }
        return res;
    }

    @RequestMapping(value = PREFIX + "/loadData", method = RequestMethod.POST)
    public HtmlReport loadData(@RequestBody PreviewReq req) {
        return loadReport(req);
    }

    @RequestMapping(value = PREFIX + "/loadPrintPages", method = RequestMethod.POST)
    public Map<String, String> loadPrintPages(@RequestBody PreviewReq req) {
        String file = req.getTemplate();
        if (StringUtils.isBlank(file)) {
            throw new ReportComputeException("Report file can not be null.");
        }
        Map<String, Object> parameters = req.getCustomParameters();
        Report report = reportBuilder.buildReport(reportRender.getReportDefinition(file), parameters);
        Map<String, ChartData> chartMap = report.getContext().getChartDataMap();
        if (chartMap.size() > 0) {
            CacheUtils.storeChartDataMap(chartMap);
        }
        FullPageData pageData = PageBuilder.buildFullPageData(report);
        StringBuilder sb = new StringBuilder();
        List<List<Page>> list = pageData.getPageList();
        Context context = report.getContext();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                List<Page> columnPages = list.get(i);
                String html;
                if (i == 0) {
                    html = htmlProducer.produce(context, columnPages, pageData.getColumnMargin(), false);
                } else {
                    html = htmlProducer.produce(context, columnPages, pageData.getColumnMargin(), true);
                }
                sb.append(html);
            }
        } else {
            List<Page> pages = report.getPages();
            for (int i = 0; i < pages.size(); i++) {
                Page page = pages.get(i);
                String html;
                if (i == 0) {
                    html = htmlProducer.produce(context, page, false);
                } else {
                    html = htmlProducer.produce(context, page, true);
                }
                sb.append(html);
            }
        }
        Map<String, String> map = new HashMap<>();
        map.put("html", sb.toString());
        return map;
    }

    @RequestMapping(value = PREFIX + "/loadPagePaper", method = RequestMethod.GET)
    public Paper loadPagePaper(@RequestParam(FILE_PARAM) String template) {
        String file = template;
        if (StringUtils.isBlank(file)) {
            throw new ReportComputeException("Report file can not be null.");
        }
        return reportRender.getReportDefinition(file).getPaper();
    }


    private String buildTitle(PreviewReq req) {
        String title = req.getTitle();
        if (StringUtils.isBlank(title)) {
            title = req.getTemplate();
            int point = title.lastIndexOf(SUFFIX);
            if (point > -1) {
                title = title.substring(0, point);
            }
            if (title.equals("p")) {
                title = "设计中报表";
            }
        }
        return title + "-ureport";
    }

    private String convertJson(Collection<ChartData> data) {
        if (data == null || data.size() == 0) {
            return "[]";
        }
        try {
            return objectMapper.writeValueAsString(data);
        } catch (Exception e) {
            throw new ReportComputeException(e);
        }
    }

    private HtmlReport loadReport(PreviewReq req) {
        Map<String, Object> parameters = req.getCustomParameters();
        HtmlReport htmlReport;
        String file = req.getTemplate();
        String pageIndex = req.getPageIndex();
        if (StringUtils.isBlank(file)) {
            throw new ReportComputeException("Report file can not be null.");
        }
        if (StringUtils.isNotBlank(pageIndex) && !pageIndex.equals("0")) {
            int index = Integer.parseInt(pageIndex);
            htmlReport = exportManager.exportHtml(file, "req.getContextPath()", parameters, index);
        } else {
            htmlReport = exportManager.exportHtml(file, "req.getContextPath()", parameters);
        }
        return htmlReport;
    }


    private String buildExceptionMessage(Throwable throwable) {
        Throwable root = buildRootException(throwable);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        root.printStackTrace(pw);
        String trace = sw.getBuffer().toString();
        trace = trace.replaceAll("\n", "<br>");
        pw.close();
        return trace;
    }
}
