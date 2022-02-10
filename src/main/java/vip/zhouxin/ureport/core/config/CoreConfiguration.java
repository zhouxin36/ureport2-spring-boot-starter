package vip.zhouxin.ureport.core.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vip.zhouxin.ureport.core.Utils;
import vip.zhouxin.ureport.core.build.HideRowColumnBuilder;
import vip.zhouxin.ureport.core.build.ReportBuilder;
import vip.zhouxin.ureport.core.cache.CacheUtils;
import vip.zhouxin.ureport.core.export.ExportManagerImpl;
import vip.zhouxin.ureport.core.export.ReportRender;
import vip.zhouxin.ureport.core.export.pdf.font.FontBuilder;
import vip.zhouxin.ureport.core.expression.ExpressionUtils;
import vip.zhouxin.ureport.core.expression.function.*;
import vip.zhouxin.ureport.core.expression.function.date.*;
import vip.zhouxin.ureport.core.expression.function.math.*;
import vip.zhouxin.ureport.core.expression.function.page.*;
import vip.zhouxin.ureport.core.expression.function.string.*;
import vip.zhouxin.ureport.core.provider.image.DefaultImageProvider;
import vip.zhouxin.ureport.core.provider.image.HttpImageProvider;
import vip.zhouxin.ureport.core.provider.image.HttpsImageProvider;
import vip.zhouxin.ureport.core.provider.report.classpath.ClasspathReportProvider;
import vip.zhouxin.ureport.core.provider.report.file.FileReportProvider;
import vip.zhouxin.ureport.core.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author xinxingzhou
 * @since 2022/1/19
 */
@Configuration
public class CoreConfiguration {

    @Bean
    public ExportManagerImpl exportManager(ReportRender reportRender) {
        return new ExportManagerImpl(reportRender);
    }

    @Bean
    public ReportRender reportRender(ObjectMapper objectMapper, ReportBuilder reportBuilder) {
        return new ReportRender(objectMapper, reportBuilder);
    }

    @Bean
    public DefaultImageProvider defaultImageProvider() {
        return new DefaultImageProvider();
    }

    @Bean
    public FileReportProvider fileReportProvider(UreportProperties ureportProperties) {
        return new FileReportProvider(ureportProperties.getFileStoreDir(), ureportProperties.getDisableFileProvider());
    }

    @Bean
    public HttpImageProvider httpImageProvider() {
        return new HttpImageProvider();
    }

    @Bean
    public HttpsImageProvider httpsImageProvider() {
        return new HttpsImageProvider();
    }

    @Bean
    public ReportBuilder reportBuilder(HideRowColumnBuilder hideRowColumnBuilder) {
        return new ReportBuilder(hideRowColumnBuilder);
    }

    @Bean
    public HideRowColumnBuilder hideRowColumnBuilder() {
        return new HideRowColumnBuilder();
    }


    @Bean
    public ClasspathReportProvider classpathReportProvider() {
        return new ClasspathReportProvider();
    }

    @Bean
    public FontBuilder fontBuilder() {
        return new FontBuilder();
    }

    @Bean
    public ExpressionUtils expressionUtils() {
        return new ExpressionUtils();
    }

    @Bean
    public Utils utils(UreportProperties ureportProperties) {
        return new Utils(ureportProperties.getDebug());
    }

    @Bean
    public CacheUtils cacheUtils() {
        return new CacheUtils();
    }

    @Bean
    public CountFunction countFunction() {
        return new CountFunction();
    }

    @Bean
    public SumFunction sumFunction() {
        return new SumFunction();
    }

    @Bean
    public MaxFunction maxFunction() {
        return new MaxFunction();
    }

    @Bean
    public MinFunction minFunction() {
        return new MinFunction();
    }

    @Bean
    public ListFunction listFunction() {
        return new ListFunction();
    }

    @Bean
    public AvgFunction avgFunction() {
        return new AvgFunction();
    }

    @Bean
    public OrderFunction orderFunction() {
        return new OrderFunction();
    }

    @Bean
    public WeekFunction weekFunction() {
        return new WeekFunction();
    }

    @Bean
    public DayFunction dayFunction() {
        return new DayFunction();
    }

    @Bean
    public MonthFunction monthFunction() {
        return new MonthFunction();
    }

    @Bean
    public YearFunction yearFunction() {
        return new YearFunction();
    }

    @Bean
    public DateFunction dateFunction() {
        return new DateFunction();
    }

    @Bean
    public FormatNumberFunction formatNumberFunction() {
        return new FormatNumberFunction();
    }

    @Bean
    public FormatDateFunction formatDateFunction() {
        return new FormatDateFunction();
    }

    @Bean
    public GetFunction getFunction() {
        return new GetFunction();
    }

    @Bean
    public AbsFunction absFunction() {
        return new AbsFunction();
    }

    @Bean
    public CeilFunction ceilFunction() {
        return new CeilFunction();
    }

    @Bean
    public ChnFunction chnFunction() {
        return new ChnFunction();
    }

    @Bean
    public ChnMoneyFunction chnMoneyFunction() {
        return new ChnMoneyFunction();
    }

    @Bean
    public CosFunction cosFunction() {
        return new CosFunction();
    }

    @Bean
    public ExpFunction expFunction() {
        return new ExpFunction();
    }

    @Bean
    public FloorFunction floorFunction() {
        return new FloorFunction();
    }

    @Bean
    public Log10Function log10Function() {
        return new Log10Function();
    }

    @Bean
    public LogFunction logFunction() {
        return new LogFunction();
    }

    @Bean
    public PowFunction powFunction() {
        return new PowFunction();
    }

    @Bean
    public RandomFunction randomFunction() {
        return new RandomFunction();
    }

    @Bean
    public RoundFunction roundFunction() {
        return new RoundFunction();
    }

    @Bean
    public SinFunction sinFunction() {
        return new SinFunction();
    }

    @Bean
    public SqrtFunction sqrtFunction() {
        return new SqrtFunction();
    }

    @Bean
    public TanFunction tanFunction() {
        return new TanFunction();
    }

    @Bean
    public StdevpFunction stdevpFunction() {
        return new StdevpFunction();
    }

    @Bean
    public VaraFunction varaFunction() {
        return new VaraFunction();
    }

    @Bean
    public ModeFunction modeFunction() {
        return new ModeFunction();
    }

    @Bean
    public MedianFunction medianFunction() {
        return new MedianFunction();
    }

    @Bean
    public LengthFunction lengthFunction() {
        return new LengthFunction();
    }

    @Bean
    public LowerFunction lowerFunction() {
        return new LowerFunction();
    }

    @Bean
    public IndexOfFunction indexOfFunction() {
        return new IndexOfFunction();
    }

    @Bean
    public ReplaceFunction replaceFunction() {
        return new ReplaceFunction();
    }

    @Bean
    public SubstringFunction substringFunction() {
        return new SubstringFunction();
    }

    @Bean
    public TrimFunction trimFunction() {
        return new TrimFunction();
    }

    @Bean
    public UpperFunction upperFunction() {
        return new UpperFunction();
    }

    @Bean
    public PageTotalFunction pageTotalFunction() {
        return new PageTotalFunction();
    }

    @Bean
    public PageNumberFunction pageNumberFunction() {
        return new PageNumberFunction();
    }

    @Bean
    public PageAvgFunction pageAvgFunction() {
        return new PageAvgFunction();
    }

    @Bean
    public PageCountFunction pageCountFunction() {
        return new PageCountFunction();
    }

    @Bean
    public PageMaxFunction pageMaxFunction() {
        return new PageMaxFunction();
    }

    @Bean
    public PageMinFunction pageMinFunction() {
        return new PageMinFunction();
    }

    @Bean
    public PageRowsFunction pageRowsFunction() {
        return new PageRowsFunction();
    }

    @Bean
    public PageSumFunction pageSumFunction() {
        return new PageSumFunction();
    }

    @Bean
    public ParameterFunction parameterFunction() {
        return new ParameterFunction();
    }

    @Bean
    public ParameterIsEmptyFunction parameterIsEmptyFunction() {
        return new ParameterIsEmptyFunction();
    }

    @Bean
    public JsonFunction jsonFunction(ObjectMapper objectMapper) {
        return new JsonFunction(objectMapper);
    }

    @Bean
    public RowFunction rowFunction() {
        return new RowFunction();
    }

    @Bean
    public ColumnFunction columnFunction() {
        return new ColumnFunction();
    }

    @Bean
    public SimpleModule NameModule(List<NamedType> parserMaps) {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.registerSubtypes(parserMaps.toArray(new NamedType[]{}));
        return simpleModule;
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customizer() {
        return builder -> {
            builder.serializationInclusion(JsonInclude.Include.NON_NULL);
            builder.dateFormat(new SimpleDateFormat(DateUtils.STANDARD_DATE_FORMAT));
        };
    }
}
