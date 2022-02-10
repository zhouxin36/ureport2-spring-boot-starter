package vip.zhouxin.ureport.console.controller;

import vip.zhouxin.ureport.console.importexcel.ExcelParser;
import vip.zhouxin.ureport.console.importexcel.HSSFExcelParser;
import vip.zhouxin.ureport.console.importexcel.XSSFExcelParser;
import vip.zhouxin.ureport.core.definition.ReportDefinition;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xinxingzhou
 * @since 2022/1/21
 */
@RestController
public class ImportExcelController extends AbstractController {
    public static final String PREFIX = CONTENT + "/import";

    private final List<ExcelParser> excelParsers = new ArrayList<>();

    public ImportExcelController(ObjectMapper objectMapper) {
        super(objectMapper);
        excelParsers.add(new HSSFExcelParser());
        excelParsers.add(new XSSFExcelParser());
    }


    @RequestMapping(value = PREFIX, method = RequestMethod.POST)
    public Map<String, Object> loadMethods(HttpServletRequest req) {
        String tempDir = System.getProperty("java.io.tmpdir");
        FileItemFactory factory = new DiskFileItemFactory(1000240, new File(tempDir));
        ServletFileUpload upload = new ServletFileUpload(factory);
        ReportDefinition report = null;
        String errorInfo;
        try {
            List<FileItem> items = upload.parseRequest(req);
            for (FileItem item : items) {
                String fieldName = item.getFieldName();
                String name = item.getName().toLowerCase();
                if (fieldName.equals("_excel_file") && (name.endsWith(".xls") || name.endsWith(".xlsx"))) {
                    InputStream inputStream = item.getInputStream();
                    for (ExcelParser parser : excelParsers) {
                        if (parser.support(name)) {
                            report = parser.parse(inputStream);
                            break;
                        }
                    }
                    inputStream.close();
                    break;
                }
            }
            errorInfo = "请选择一个合法的Excel导入";
        } catch (Exception e) {
            e.printStackTrace();
            errorInfo = e.getMessage();
        }
        Map<String, Object> result = new HashMap<>();
        if (report != null) {
            result.put("result", true);
        } else {
            result.put("result", false);
            if (errorInfo != null) {
                result.put("errorInfo", errorInfo);
            }
        }
        return result;
    }
}
