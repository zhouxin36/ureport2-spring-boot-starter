package vip.zhouxin.ureport.console.config;

import vip.zhouxin.ureport.core.build.ReportBuilder;
import vip.zhouxin.ureport.console.controller.*;
import vip.zhouxin.ureport.core.export.ExportManager;
import vip.zhouxin.ureport.core.export.ReportRender;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vip.zhouxin.ureport.core.provider.report.file.CacheReportProvider;

/**
 * @author xinxingzhou
 * @since 2022/1/21
 */
@Configuration
public class ControllerConfiguration {

    private final ObjectMapper objectMapper;

    public ControllerConfiguration(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public ChartController chartController() {
        return new ChartController(objectMapper);
    }

    @Bean
    public DatasourceController datasourceController(ApplicationContext applicationContext) {
        return new DatasourceController(applicationContext, objectMapper);
    }

    @Bean
    public DesignerController designerController(ReportRender reportRender) {
        return new DesignerController(reportRender, objectMapper);
    }

    @Bean
    public ExportExcel97Controller exportExcel97Controller(ExportManager exportManager) {
        return new ExportExcel97Controller(exportManager, objectMapper);
    }

    @Bean
    public ExportExcelController exportExcelController(ExportManager exportManager) {
        return new ExportExcelController(exportManager, objectMapper);
    }

    @Bean
    public ExportPdfController exportPdfController(ExportManager exportManager, ReportRender reportRender) {
        return new ExportPdfController(exportManager, reportRender, objectMapper);
    }

    @Bean
    public ExportWordController exportWordController(ExportManager exportManager) {
        return new ExportWordController(exportManager, objectMapper);
    }

    @Bean
    public ImageController imageController() {
        return new ImageController(objectMapper);
    }

    @Bean
    public ImportExcelController importExcelController(CacheReportProvider reportProvider) {
        return new ImportExcelController(objectMapper,reportProvider);
    }

    @Bean
    public HtmlPreviewController htmlPreviewController(ExportManager exportManager, ReportBuilder reportBuilder, ReportRender reportRender) {
        return new HtmlPreviewController(exportManager, reportBuilder, reportRender, objectMapper);
    }
}
