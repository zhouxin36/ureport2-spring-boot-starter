package vip.zhouxin.ureport.console.controller;

import vip.zhouxin.ureport.core.exception.ReportComputeException;
import vip.zhouxin.ureport.core.export.ExportConfigure;
import vip.zhouxin.ureport.core.export.ExportConfigureImpl;
import vip.zhouxin.ureport.core.export.ExportManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * @author xinxingzhou
 * @since 2022/1/21
 */
@Controller
public class ExportExcel97Controller extends AbstractController {

    public static final String PREFIX = CONTENT + "/excel97";
    private final ExportManager exportManager;

    public ExportExcel97Controller(ExportManager exportManager, ObjectMapper objectMapper) {
        super(objectMapper);
        this.exportManager = exportManager;
    }

    @RequestMapping(value = PREFIX, method = RequestMethod.GET)
    public void buildExcel(HttpServletRequest req, HttpServletResponse resp, boolean withPage, boolean withSheet) throws IOException {
        String file = req.getParameter(FILE_PARAM);
        if (StringUtils.isBlank(file)) {
            throw new ReportComputeException("Report file can not be null.");
        }
        String fileName = req.getParameter(FILE_NAME_PARAM);
        if (StringUtils.isBlank(fileName)) {
            fileName = "ureport.xls";
        }
        resp.setContentType("application/octet-stream;charset=UTF-8");
        resp.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
        Map<String, Object> parameters = buildParameters(req);
        OutputStream outputStream = resp.getOutputStream();
        ExportConfigure configure = new ExportConfigureImpl(file, parameters, outputStream);
        if (withPage) {
            exportManager.exportExcelWithPaging(configure);
        } else if (withSheet) {
            exportManager.exportExcelWithPagingSheet(configure);
        } else {
            exportManager.exportExcel(configure);
        }
        outputStream.flush();
        outputStream.close();
    }

    @RequestMapping(value = PREFIX + "/paging", method = RequestMethod.GET)
    public void paging(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        buildExcel(req, resp, true, false);
    }

    @RequestMapping(value = PREFIX + "/sheet", method = RequestMethod.GET)
    public void sheet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        buildExcel(req, resp, false, true);
    }

}
