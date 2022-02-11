package vip.zhouxin.ureport.console.controller;

import vip.zhouxin.ureport.core.exception.ReportComputeException;
import vip.zhouxin.ureport.core.export.ExportConfigure;
import vip.zhouxin.ureport.core.export.ExportConfigureImpl;
import vip.zhouxin.ureport.core.export.ExportManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author xinxingzhou
 * @since 2022/1/23
 */
@Controller
public class ExportWordController extends AbstractController {

    public static final String PREFIX = CONTENT + "/word";

    private final ExportManager exportManager;

    public ExportWordController(ExportManager exportManager, ObjectMapper objectMapper) {
        super(objectMapper);
        this.exportManager = exportManager;
    }

    @RequestMapping(value = PREFIX, method = RequestMethod.GET)
    public void buildWord(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String file = req.getParameter(FILE_PARAM);
        if (StringUtils.isBlank(file)) {
            throw new ReportComputeException("Report file can not be null.");
        }
        String fileName = req.getParameter(FILE_NAME_PARAM);
        fileName = buildDownloadFileName(file, fileName, ".docx");
        fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        resp.setContentType("application/octet-stream;charset=ISO8859-1");
        resp.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
        Map<String, Object> parameters = buildParameters(req);
        OutputStream outputStream = resp.getOutputStream();
        ExportConfigure configure = new ExportConfigureImpl(file, parameters, outputStream);
        exportManager.exportWord(configure);
        outputStream.flush();
        outputStream.close();
    }
}
