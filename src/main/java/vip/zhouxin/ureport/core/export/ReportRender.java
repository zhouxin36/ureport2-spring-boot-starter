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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import vip.zhouxin.ureport.core.build.ReportBuilder;
import vip.zhouxin.ureport.core.cache.CacheUtils;
import vip.zhouxin.ureport.core.definition.CellDefinition;
import vip.zhouxin.ureport.core.definition.Expand;
import vip.zhouxin.ureport.core.definition.ReportDefinition;
import vip.zhouxin.ureport.core.exception.ReportException;
import vip.zhouxin.ureport.core.export.builder.down.DownCellbuilder;
import vip.zhouxin.ureport.core.export.builder.right.RightCellbuilder;
import vip.zhouxin.ureport.core.model.Report;
import vip.zhouxin.ureport.core.provider.report.ReportProvider;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Jacky.gao
 * @since 2016年12月4日
 */
public class ReportRender implements ApplicationContextAware{
	private final ReportBuilder reportBuilder;
	private Collection<ReportProvider> reportProviders;
	private final ObjectMapper objectMapper;
	private final DownCellbuilder downCellParentbuilder=new DownCellbuilder();
	private final RightCellbuilder rightCellParentbuilder=new RightCellbuilder();

	public ReportRender(ObjectMapper objectMapper, ReportBuilder reportBuilder) {
		this.objectMapper = objectMapper;
		this.reportBuilder = reportBuilder;
	}

	public Report render(String file, Map<String,Object> parameters){
		ReportDefinition reportDefinition=getReportDefinition(file);
		return reportBuilder.buildReport(reportDefinition,parameters);
	}
	
	public Report render(ReportDefinition reportDefinition, Map<String,Object> parameters){
		return reportBuilder.buildReport(reportDefinition,parameters);
	}
	
	public ReportDefinition getReportDefinition(String file){
		ReportDefinition reportDefinition= CacheUtils.getReportDefinition(file);
		if(reportDefinition==null){
			reportDefinition=parseReport(file);
			rebuildReportDefinition(reportDefinition);
			CacheUtils.cacheReportDefinition(file, reportDefinition);
		}
		return reportDefinition;
	}
	
	public void rebuildReportDefinition(ReportDefinition reportDefinition){
		List<CellDefinition> cells=reportDefinition.getCells();
		for(CellDefinition cell:cells){
			addRowChildCell(cell,cell);
			addColumnChildCell(cell,cell);
		}
		for(CellDefinition cell:cells){
			Expand expand=cell.getExpand();
			if(expand.equals(Expand.Down)){
				downCellParentbuilder.buildParentCell(cell,cells);
			}else if(expand.equals(Expand.Right)){
				rightCellParentbuilder.buildParentCell(cell,cells);
			}
		}
	}
	
	public ReportDefinition parseReport(String file){
		try(InputStream inputStream = buildReportFile(file)) {
			return objectMapper.readValue(Optional.of(IOUtils.readLines(inputStream
											, StandardCharsets.UTF_8.displayName())).map(e -> String.join("", e)).orElse("{}")
							, ReportDefinition.class);
		}catch (Exception ex){
			throw new ReportException(ex);
		}
	}
	
	private InputStream buildReportFile(String file){
		InputStream inputStream=null;
		for(ReportProvider provider:reportProviders){
			if(file.startsWith(provider.getPrefix())){
				inputStream=provider.loadReport(file);
			}
		}
		if(inputStream==null){
			throw new ReportException("Report ["+file+"] not support.");
		}
		return inputStream;
	}
	
	private void addRowChildCell(CellDefinition cell, CellDefinition childCell){
		CellDefinition leftCell=cell.getLeftParentCell();
		if(leftCell==null){
			return;
		}
		List<CellDefinition> childrenCells=leftCell.getRowChildrenCells();
		childrenCells.add(childCell);
		addRowChildCell(leftCell,childCell);
	}
	private void addColumnChildCell(CellDefinition cell, CellDefinition childCell){
		CellDefinition topCell=cell.getTopParentCell();
		if(topCell==null){
			return;
		}
		List<CellDefinition> childrenCells=topCell.getColumnChildrenCells();
		childrenCells.add(childCell);
		addColumnChildCell(topCell,childCell);
	}
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		reportProviders=applicationContext.getBeansOfType(ReportProvider.class).values();
	}
}
