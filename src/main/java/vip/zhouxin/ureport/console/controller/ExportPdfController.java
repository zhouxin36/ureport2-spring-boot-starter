package vip.zhouxin.ureport.console.controller;

import vip.zhouxin.ureport.console.dto.NewPagingReq;
import vip.zhouxin.ureport.core.definition.ReportDefinition;
import vip.zhouxin.ureport.core.exception.ReportComputeException;
import vip.zhouxin.ureport.core.export.ExportConfigure;
import vip.zhouxin.ureport.core.export.ExportConfigureImpl;
import vip.zhouxin.ureport.core.export.ExportManager;
import vip.zhouxin.ureport.core.export.ReportRender;
import vip.zhouxin.ureport.core.model.Report;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author xinxingzhou
 * @since 2022/1/21
 */
@Controller
public class ExportPdfController extends AbstractController {

    public static final String PREFIX = CONTENT + "/pdf";
    private final ExportManager exportManager;
    private final ReportRender reportRender;

    public ExportPdfController(ExportManager exportManager, ReportRender reportRender, ObjectMapper objectMapper) {
        super(objectMapper);
        this.exportManager = exportManager;
        this.reportRender = reportRender;
    }

    @RequestMapping(value = PREFIX, method = RequestMethod.GET)
    public void buildPdf(HttpServletRequest req, HttpServletResponse resp, boolean forPrint) throws IOException {
        String file = req.getParameter(FILE_PARAM);
        if (StringUtils.isBlank(file)) {
            throw new ReportComputeException("Report file can not be null.");
        }
        String fileName = req.getParameter(FILE_NAME_PARAM);
        fileName = buildDownloadFileName(file, fileName, ".pdf");
        fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        if (forPrint) {
            resp.setContentType("application/pdf");
            resp.setHeader("Content-Disposition", "inline;filename=\"" + fileName + "\"");
        } else {
            resp.setContentType("application/octet-stream;charset=ISO8859-1");
            resp.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
        }
        Map<String, Object> parameters = buildParameters(req);
        OutputStream outputStream = resp.getOutputStream();
        ExportConfigure configure = new ExportConfigureImpl(file, parameters, outputStream);
        exportManager.exportPdf(configure);
        outputStream.flush();
        outputStream.close();
    }

    @RequestMapping(value = PREFIX + "/newPaging", method = RequestMethod.POST)
    @ResponseBody
    public void newPaging(@RequestBody NewPagingReq req) {
        String file = req.getTemplate();
        if (StringUtils.isBlank(file)) {
            throw new ReportComputeException("Report file can not be null.");
        }
        Report report;
        Map<String, Object> parameters = req.getCustomParameters();

        ReportDefinition reportDefinition = reportRender.getReportDefinition(file);
        report = reportRender.render(reportDefinition, parameters);

        report.rePaging(req.getPaper());
    }

    @RequestMapping(value = PREFIX + "/show", method = RequestMethod.GET)
    public void show(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        buildPdf(req, resp, true);
    }

}
