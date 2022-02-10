package vip.zhouxin.ureport.console.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xinxingzhou
 * @since 2022/1/21
 */
public abstract class AbstractController {

    public static final String CONTENT = "/ureport";

    public static final String FILE_PARAM = "template";
    public static final String FILE_NAME_PARAM = "fileName";

    public static final String SUFFIX = ".ureport.xml";

    protected final ObjectMapper objectMapper;

    protected AbstractController(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    protected Throwable buildRootException(Throwable throwable) {
        if (throwable.getCause() == null) {
            return throwable;
        }
        return buildRootException(throwable.getCause());
    }

    protected String decodeContent(String content) {
        if (content == null) {
            return null;
        }
        try {
            content = URLDecoder.decode(content, StandardCharsets.UTF_8.displayName());
            return content;
        } catch (Exception ex) {
            return content;
        }
    }

    protected Map<String, Object> buildParameters(HttpServletRequest req) {
        Map<String, Object> parameters = new HashMap<>();
        Enumeration<?> enumeration = req.getParameterNames();
        while (enumeration.hasMoreElements()) {
            Object obj = enumeration.nextElement();
            if (obj == null) {
                continue;
            }
            String name = obj.toString();
            String value = req.getParameter(name);
            if (name == null || value == null || name.startsWith("_")) {
                continue;
            }
            parameters.put(name, value);
        }
        return parameters;
    }

    protected String buildDownloadFileName(String reportFileName, String fileName, String extName) {
        if (StringUtils.isNotBlank(fileName)) {
            if (!fileName.toLowerCase().endsWith(extName)) {
                fileName = fileName + extName;
            }
            return fileName;
        } else {
            int pos = reportFileName.indexOf(":");
            if (pos > 0) {
                reportFileName = reportFileName.substring(pos + 1);
            }
            pos = reportFileName.toLowerCase().indexOf(SUFFIX);
            if (pos > 0) {
                reportFileName = reportFileName.substring(0, pos);
            }
            return "ureport-" + reportFileName + extName;
        }
    }

}
