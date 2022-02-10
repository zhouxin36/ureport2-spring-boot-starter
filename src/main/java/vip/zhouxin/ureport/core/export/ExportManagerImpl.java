/*******************************************************************************
 * Copyright 2017 Bstek
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package vip.zhouxin.ureport.core.export;

import vip.zhouxin.ureport.core.build.paging.Page;
import vip.zhouxin.ureport.core.cache.CacheUtils;
import vip.zhouxin.ureport.core.chart.ChartData;
import vip.zhouxin.ureport.core.definition.ReportDefinition;
import vip.zhouxin.ureport.core.definition.dataset.Parameter;
import vip.zhouxin.ureport.core.definition.dataset.SqlDatasetDefinition;
import vip.zhouxin.ureport.core.export.excel.high.ExcelProducer;
import vip.zhouxin.ureport.core.export.excel.low.Excel97Producer;
import vip.zhouxin.ureport.core.export.html.HtmlProducer;
import vip.zhouxin.ureport.core.export.html.HtmlReport;
import vip.zhouxin.ureport.core.export.pdf.PdfProducer;
import vip.zhouxin.ureport.core.export.word.high.WordProducer;
import vip.zhouxin.ureport.core.model.Report;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Jacky.gao
 * @since 2016年12月4日
 */
public class ExportManagerImpl implements ExportManager {
    private final ReportRender reportRender;
    private final HtmlProducer htmlProducer = new HtmlProducer();
    private final WordProducer wordProducer = new WordProducer();
    private final ExcelProducer excelProducer = new ExcelProducer();
    private final Excel97Producer excel97Producer = new Excel97Producer();
    private final PdfProducer pdfProducer = new PdfProducer();

    public ExportManagerImpl(ReportRender reportRender) {
        this.reportRender = reportRender;
    }


    @Override
    public HtmlReport exportHtml(String file, String contextPath, Map<String, Object> parameters) {
        ReportDefinition reportDefinition = reportRender.getReportDefinition(file);
        parameters.putAll(handlerParameters(parameters, reportDefinition));
        Report report = reportRender.render(reportDefinition, parameters);
        Map<String, ChartData> chartMap = report.getContext().getChartDataMap();
        if (chartMap.size() > 0) {
            CacheUtils.storeChartDataMap(chartMap);
        }
        HtmlReport htmlReport = new HtmlReport();
        String content = htmlProducer.produce(report);
        htmlReport.setContent(content);
        if (reportDefinition.getPaper().isColumnEnabled()) {
            htmlReport.setColumn(reportDefinition.getPaper().getColumnCount());
        }
        htmlReport.setStyle(reportDefinition.getStyle());
        htmlReport.setSearchFormData(reportDefinition.buildSearchFormData(report.getContext().getDatasetMap(), parameters));
        htmlReport.setReportAlign(report.getPaper().getHtmlReportAlign().name());
        htmlReport.setChartDatas(report.getContext().getChartDataMap().values());
        htmlReport.setHtmlIntervalRefreshValue(report.getPaper().getHtmlIntervalRefreshValue());
        htmlReport.setCustomParameters(parameters);
        return htmlReport;
    }

    private Map<String, Object> handlerParameters(Map<String, Object> parameters, ReportDefinition reportDefinition) {
        Map<String, Object> param = new HashMap<>();
        List<Parameter> ps = Optional.ofNullable(reportDefinition)
                .map(ReportDefinition::getDatasources)
                .map(e -> e.stream().flatMap(definition -> Optional.ofNullable(definition.getDatasets()).orElse(Collections.emptyList()).stream().flatMap(datasetDefinition -> {
                    if (datasetDefinition instanceof SqlDatasetDefinition) {
                        return ((SqlDatasetDefinition) datasetDefinition).getParameters().stream();
                    } else {
                        return Stream.empty();
                    }
                })).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
        for (Parameter p : ps) {
            if (parameters.containsKey(p.getName())) {
                continue;
            }
            param.put(p.getName(), p.getDefaultValue());
        }
        return param;
    }

    @Override
    public HtmlReport exportHtml(String file, String contextPath, Map<String, Object> parameters, int pageIndex) {
        ReportDefinition reportDefinition = reportRender.getReportDefinition(file);
        parameters.putAll(handlerParameters(parameters, reportDefinition));
        Report report = reportRender.render(reportDefinition, parameters);
        Map<String, ChartData> chartMap = report.getContext().getChartDataMap();
        if (chartMap.size() > 0) {
            CacheUtils.storeChartDataMap(chartMap);
        }
        SinglePageData pageData = PageBuilder.buildSinglePageData(pageIndex, report);
        List<Page> pages = pageData.getPages();
        String content = null;
        if (pages.size() == 1) {
            content = htmlProducer.produce(report.getContext(), pages.get(0), false);
        } else {
            content = htmlProducer.produce(report.getContext(), pages, pageData.getColumnMargin(), false);
        }
        HtmlReport htmlReport = new HtmlReport();
        htmlReport.setContent(content);
        if (reportDefinition.getPaper().isColumnEnabled()) {
            htmlReport.setColumn(reportDefinition.getPaper().getColumnCount());
        }
        htmlReport.setStyle(reportDefinition.getStyle());
        htmlReport.setSearchFormData(reportDefinition.buildSearchFormData(report.getContext().getDatasetMap(), parameters));
        htmlReport.setPageIndex(pageIndex);
        htmlReport.setTotalPage(pageData.getTotalPages());
        htmlReport.setReportAlign(report.getPaper().getHtmlReportAlign().name());
        htmlReport.setChartDatas(report.getContext().getChartDataMap().values());
        htmlReport.setHtmlIntervalRefreshValue(report.getPaper().getHtmlIntervalRefreshValue());
        htmlReport.setCustomParameters(parameters);
        return htmlReport;
    }

    @Override
    public void exportPdf(ExportConfigure config) {
        String file = config.getFile();
        Map<String, Object> parameters = config.getParameters();
        ReportDefinition reportDefinition = reportRender.getReportDefinition(file);
        Report report = reportRender.render(reportDefinition, parameters);
        pdfProducer.produce(report, config.getOutputStream());
    }

    @Override
    public void exportWord(ExportConfigure config) {
        String file = config.getFile();
        Map<String, Object> parameters = config.getParameters();
        ReportDefinition reportDefinition = reportRender.getReportDefinition(file);
        Report report = reportRender.render(reportDefinition, parameters);
        wordProducer.produce(report, config.getOutputStream());
    }

    @Override
    public void exportExcel(ExportConfigure config) {
        String file = config.getFile();
        Map<String, Object> parameters = config.getParameters();
        ReportDefinition reportDefinition = reportRender.getReportDefinition(file);
        Report report = reportRender.render(reportDefinition, parameters);
        excelProducer.produce(report, config.getOutputStream());
    }

    @Override
    public void exportExcel97(ExportConfigure config) {
        String file = config.getFile();
        Map<String, Object> parameters = config.getParameters();
        ReportDefinition reportDefinition = reportRender.getReportDefinition(file);
        Report report = reportRender.render(reportDefinition, parameters);
        excel97Producer.produce(report, config.getOutputStream());
    }

    @Override
    public void exportExcelWithPaging(ExportConfigure config) {
        String file = config.getFile();
        Map<String, Object> parameters = config.getParameters();
        ReportDefinition reportDefinition = reportRender.getReportDefinition(file);
        Report report = reportRender.render(reportDefinition, parameters);
        excelProducer.produceWithPaging(report, config.getOutputStream());
    }

    @Override
    public void exportExcel97WithPaging(ExportConfigure config) {
        String file = config.getFile();
        Map<String, Object> parameters = config.getParameters();
        ReportDefinition reportDefinition = reportRender.getReportDefinition(file);
        Report report = reportRender.render(reportDefinition, parameters);
        excel97Producer.produceWithPaging(report, config.getOutputStream());
    }

    @Override
    public void exportExcelWithPagingSheet(ExportConfigure config) {
        String file = config.getFile();
        Map<String, Object> parameters = config.getParameters();
        ReportDefinition reportDefinition = reportRender.getReportDefinition(file);
        Report report = reportRender.render(reportDefinition, parameters);
        excelProducer.produceWithSheet(report, config.getOutputStream());
    }

    @Override
    public void exportExcel97WithPagingSheet(ExportConfigure config) {
        String file = config.getFile();
        Map<String, Object> parameters = config.getParameters();
        ReportDefinition reportDefinition = reportRender.getReportDefinition(file);
        Report report = reportRender.render(reportDefinition, parameters);
        excel97Producer.produceWithSheet(report, config.getOutputStream());
    }

}
