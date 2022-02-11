package vip.zhouxin.ureport.console.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vip.zhouxin.ureport.console.importexcel.ExcelParser;
import vip.zhouxin.ureport.console.importexcel.HSSFExcelParser;
import vip.zhouxin.ureport.console.importexcel.XSSFExcelParser;
import vip.zhouxin.ureport.core.definition.ReportDefinition;
import vip.zhouxin.ureport.core.provider.report.file.CacheReportProvider;

import java.io.InputStream;
import java.util.*;

/**
 * @author xinxingzhou
 * @since 2022/1/21
 */
@RestController
public class ImportExcelController extends AbstractController {
    public static final String PREFIX = CONTENT + "/import";

    private final List<ExcelParser> excelParsers = new ArrayList<>();
    private final CacheReportProvider reportProvider;

    public ImportExcelController(ObjectMapper objectMapper, CacheReportProvider reportProvider) {
        super(objectMapper);
        this.reportProvider = reportProvider;
        excelParsers.add(new HSSFExcelParser());
        excelParsers.add(new XSSFExcelParser());
    }


    @RequestMapping(value = PREFIX, method = RequestMethod.POST)
    public Map<String, Object> loadMethods(@RequestParam("_excel_file") MultipartFile file) {
        ReportDefinition report = null;
        String errorInfo;
        String name = file.getOriginalFilename();
        try {
            if (name != null && (name.endsWith(".xls") || name.endsWith(".xlsx"))) {
                InputStream inputStream = file.getInputStream();
                for (ExcelParser parser : excelParsers) {
                    if (parser.support(name)) {
                        report = parser.parse(inputStream);
                        String fileName = UUID.randomUUID().toString().toLowerCase(Locale.ROOT).replaceAll("-","")+".json";
                        report.setReportFullName(fileName);
                        reportProvider.saveReport(fileName, objectMapper.writeValueAsString(report));
                        break;
                    }
                }
                inputStream.close();
            }
            errorInfo = "请选择一个合法的Excel导入";
        } catch (Exception e) {
            e.printStackTrace();
            errorInfo = e.getMessage();
        }

        Map<String, Object> result = new HashMap<>();
        if (report != null) {
            result.put("result", true);
            result.put("message", report.getReportFullName());
        } else {
            result.put("result", false);
            if (errorInfo != null) {
                result.put("errorInfo", errorInfo);
            }
        }
        return result;
    }
}
