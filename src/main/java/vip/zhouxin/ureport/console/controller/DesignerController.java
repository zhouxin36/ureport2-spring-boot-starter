package vip.zhouxin.ureport.console.controller;

import vip.zhouxin.ureport.core.cache.CacheUtils;
import vip.zhouxin.ureport.console.designer.ReportDefinitionWrapper;
import vip.zhouxin.ureport.console.dto.ContentReq;
import vip.zhouxin.ureport.console.dto.ParseDatasetNameReq;
import vip.zhouxin.ureport.console.dto.ParseDatasetNameRes;
import vip.zhouxin.ureport.console.dto.TemplateReq;
import vip.zhouxin.ureport.console.exception.ReportDesignException;
import vip.zhouxin.ureport.core.definition.ReportDefinition;
import vip.zhouxin.ureport.core.dsl.ReportParserLexer;
import vip.zhouxin.ureport.core.dsl.ReportParserParser;
import vip.zhouxin.ureport.core.export.ReportRender;
import vip.zhouxin.ureport.core.expression.ErrorInfo;
import vip.zhouxin.ureport.core.expression.ScriptErrorListener;
import vip.zhouxin.ureport.core.provider.report.ReportProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author xinxingzhou
 * @since 2022/1/21
 */
@RestController
public class DesignerController extends AbstractController implements ApplicationContextAware {

  public static final String PREFIX = CONTENT + "/designer";
  private final ReportRender reportRender;
  private final List<ReportProvider> reportProviders = new ArrayList<>();

  public DesignerController(ReportRender reportRender, ObjectMapper objectMapper) {
    super(objectMapper);
    this.reportRender = reportRender;
  }

  @RequestMapping(value = PREFIX + "/scriptValidation", method = RequestMethod.POST)
  public List<ErrorInfo> scriptValidation(@RequestBody ContentReq req) {
    return scriptValidation(req, ReportParserParser::expression);
  }


  @RequestMapping(value = PREFIX + "/conditionScriptValidation", method = RequestMethod.POST)
  public List<ErrorInfo> conditionScriptValidation(@RequestBody ContentReq req) {
    return scriptValidation(req, ReportParserParser::expr);
  }

  private List<ErrorInfo> scriptValidation(ContentReq req, Consumer<ReportParserParser> consumer) {
    ReportParserLexer lexer = new ReportParserLexer(CharStreams.fromString(req.getContent()));
    CommonTokenStream tokenStream = new CommonTokenStream(lexer);
    ReportParserParser parser = new ReportParserParser(tokenStream);
    ScriptErrorListener errorListener = new ScriptErrorListener();
    parser.removeErrorListeners();
    parser.addErrorListener(errorListener);
    consumer.accept(parser);
    return errorListener.getInfos();
  }


  @RequestMapping(value = PREFIX + "/parseDatasetName", method = RequestMethod.POST)
  public ParseDatasetNameRes parseDatasetName(@RequestBody ParseDatasetNameReq req) {
    ReportParserLexer lexer = new ReportParserLexer(CharStreams.fromString(req.getExpr()));
    CommonTokenStream tokenStream = new CommonTokenStream(lexer);
    ReportParserParser parser = new ReportParserParser(tokenStream);
    parser.removeErrorListeners();
    ReportParserParser.DatasetContext ctx = parser.dataset();
    return new ParseDatasetNameRes(ctx.Identifier().getText());
  }


  @RequestMapping(value = PREFIX + "/loadReport", method = RequestMethod.POST)
  public ReportDefinitionWrapper loadReport(@RequestBody TemplateReq templateReq) {
    String file = templateReq.getTemplate();
    if (file == null) {
      throw new ReportDesignException("Report file can not be null.");
    }

    return new ReportDefinitionWrapper(reportRender.parseReport(file));
  }


  @RequestMapping(value = PREFIX + "/deleteReportFile", method = RequestMethod.POST)
  public void deleteReportFile(@RequestBody TemplateReq templateReq) {
    String file = templateReq.getTemplate();
    if (file == null) {
      throw new ReportDesignException("Report file can not be null.");
    }
    ReportProvider targetReportProvider = findProvider(file);
    targetReportProvider.deleteReport(file);
  }

  private ReportProvider findProvider(String file) {
    ReportProvider targetReportProvider = null;
    for (ReportProvider provider : reportProviders) {
      if (file.startsWith(provider.getPrefix())) {
        targetReportProvider = provider;
        break;
      }
    }
    if (targetReportProvider == null) {
      throw new ReportDesignException("File [" + file + "] not found available report provider.");
    }
    return targetReportProvider;
  }


  @RequestMapping(value = PREFIX + "/saveReportFile", method = RequestMethod.POST)
  public void saveReportFile(@RequestBody ReportDefinition reportDefinition) throws Exception {
    ReportProvider provider = findProvider(reportDefinition.getReportFullName());
    reportRender.rebuildReportDefinition(reportDefinition);
    CacheUtils.cacheReportDefinition(reportDefinition.getReportFullName(), reportDefinition);
    provider.saveReport(reportDefinition.getReportFullName(), objectMapper.writeValueAsString(reportDefinition));
  }


  @RequestMapping(value = PREFIX + "/loadReportProviders", method = RequestMethod.GET)
  public List<ReportProvider> loadReportProviders() {
    return reportProviders;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    Collection<ReportProvider> providers = applicationContext.getBeansOfType(ReportProvider.class).values();
    for (ReportProvider provider : providers) {
      if (provider.disabled() || provider.getName() == null) {
        continue;
      }
      reportProviders.add(provider);
    }
  }

}
