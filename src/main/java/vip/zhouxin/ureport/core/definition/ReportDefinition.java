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
package vip.zhouxin.ureport.core.definition;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import vip.zhouxin.ureport.core.build.Dataset;
import vip.zhouxin.ureport.core.definition.datasource.DatasourceDefinition;
import vip.zhouxin.ureport.core.definition.searchform.RenderContext;
import vip.zhouxin.ureport.core.definition.searchform.SearchForm;
import vip.zhouxin.ureport.core.export.html.SearchFormData;
import vip.zhouxin.ureport.core.model.Cell;
import vip.zhouxin.ureport.core.model.Column;
import vip.zhouxin.ureport.core.model.Report;
import vip.zhouxin.ureport.core.model.Row;

import java.io.Serializable;
import java.util.*;

/**
 * @author Jacky.gao
 * @since 2016年11月1日
 */
public class ReportDefinition implements Serializable{
	private static final long serialVersionUID = 5934291400824773809L;
	private String reportFullName;
	private Paper paper;
	private CellDefinition rootCell;
	private HeaderFooterDefinition header;
	private HeaderFooterDefinition footer;
	private SearchForm searchForm;
	private List<CellDefinition> cells;
	private List<RowDefinition> rows;
	private List<ColumnDefinition> columns;
	private List<DatasourceDefinition> datasources;
	private String searchFormXml;
	@JsonIgnore(value = true)
	private String style;

	public Report newReport() {
		Report report = new Report();
		report.setReportFullName(reportFullName);
		report.setPaper(paper);
		report.setHeader(header);
		report.setFooter(footer);
		List<Row> reportRows = new ArrayList<>();
		List<Column> reportColumns = new ArrayList<>();
		report.setRows(reportRows);
		report.setColumns(reportColumns);
		Map<Integer, Row> rowMap= new HashMap<>();
		int headerRowsHeight=0,footerRowsHeight=0,titleRowsHeight=0,summaryRowsHeight=0;
		for (RowDefinition rowDef : rows) {
			Row newRow=rowDef.newRow(reportRows);
			report.insertRow(newRow, rowDef.getRowNumber());
			rowMap.put(rowDef.getRowNumber(), newRow);
			Band band=rowDef.getBand();
			if(band!=null){
				if(band.equals(Band.headerrepeat)){
					report.getHeaderRepeatRows().add(newRow);
					headerRowsHeight+=newRow.getRealHeight();
				}else if(band.equals(Band.footerrepeat)){
					report.getFooterRepeatRows().add(newRow);
					footerRowsHeight+=newRow.getRealHeight();
				}else if(band.equals(Band.title)){
					report.getTitleRows().add(newRow);
					titleRowsHeight+=newRow.getRealHeight();
				}else if(band.equals(Band.summary)){
					report.getSummaryRows().add(newRow);
					summaryRowsHeight+=newRow.getRealHeight();
				}
			}
		}
		report.setRepeatHeaderRowHeight(headerRowsHeight);
		report.setRepeatFooterRowHeight(footerRowsHeight);
		report.setTitleRowsHeight(titleRowsHeight);
		report.setSummaryRowsHeight(summaryRowsHeight);
		Map<Integer, Column> columnMap= new HashMap<>();
		for (ColumnDefinition columnDef : columns) {
			Column newColumn=columnDef.newColumn(reportColumns);
			report.insertColumn(newColumn, columnDef.getColumnNumber());
			columnMap.put(columnDef.getColumnNumber(), newColumn);
		}
		Map<CellDefinition, Cell> cellMap= new HashMap<>();
		for (CellDefinition cellDef : cells) {
			Cell cell = cellDef.newCell();
			cellMap.put(cellDef, cell);
			Row targetRow=rowMap.get(cellDef.getRowNumber());
			cell.setRow(targetRow);
			targetRow.getCells().add(cell);
			Column targetColumn=columnMap.get(cellDef.getColumnNumber());
			cell.setColumn(targetColumn);
			targetColumn.getCells().add(cell);
			
			if(cellDef.getLeftParentCell()==null && cellDef.getTopParentCell()==null){
				report.setRootCell(cell);
			}
			report.addCell(cell);
		}
		for (CellDefinition cellDef : cells) {
			Cell targetCell=cellMap.get(cellDef);
			CellDefinition leftParentCellDef=cellDef.getLeftParentCell();
			if(leftParentCellDef!=null){
				targetCell.setLeftParentCell(cellMap.get(leftParentCellDef));
			}else{
				targetCell.setLeftParentCell(null);
			}
			CellDefinition topParentCellDef=cellDef.getTopParentCell();
			if(topParentCellDef!=null){
				targetCell.setTopParentCell(cellMap.get(topParentCellDef));
			}else{
				targetCell.setTopParentCell(null);
			}
		}
		for (CellDefinition cellDef : cells) {
			Cell targetCell=cellMap.get(cellDef);
			
			List<CellDefinition> rowChildrenCellDefinitions=cellDef.getRowChildrenCells();
			for(CellDefinition childCellDef:rowChildrenCellDefinitions){
				Cell childCell=cellMap.get(childCellDef);
				targetCell.addRowChild(childCell);
			}
			List<CellDefinition> columnChildrenCellDefinitions=cellDef.getColumnChildrenCells();
			for(CellDefinition childCellDef:columnChildrenCellDefinitions){
				Cell childCell=cellMap.get(childCellDef);
				targetCell.addColumnChild(childCell);
			}
		}
		return report;
	}
	
	public String getStyle() {
		if(style==null){
			style=buildStyle();
		}
		return style;
	}
	
	private String buildStyle(){
		StringBuffer sb=new StringBuffer();
		for(CellDefinition cell:cells){
			CellStyle cellStyle=cell.getCellStyle();
			sb.append("._"+cell.getName()+"{");
			int colWidth=getColumnWidth(cell.getColumnNumber(),cell.getColSpan());
			sb.append("width:"+colWidth+"pt;");
			Alignment align=cellStyle.getAlign();
			if(align!=null){
				sb.append("text-align:"+align.name()+";");				
			}
			Alignment valign=cellStyle.getValign();
			if(valign!=null){
				sb.append("vertical-align:"+valign.name()+";");				
			}
			float lineHeight=cellStyle.getLineHeight();
			if(lineHeight>0){
				sb.append("line-height:"+lineHeight+";");
			}
			String bgcolor=cellStyle.getBgcolor();
			if(StringUtils.isNotBlank(bgcolor)){
				sb.append("background-color:rgb("+bgcolor+");");				
			}
			String fontFamilty=cellStyle.getFontFamily();
			if(StringUtils.isNotBlank(fontFamilty)){
				sb.append("font-family:"+fontFamilty+";");				
			}
			int fontSize=cellStyle.getFontSize();
			sb.append("font-size:"+fontSize+"pt;");
			String foreColor=cellStyle.getForecolor();
			if(StringUtils.isNotBlank(foreColor)){
				sb.append("color:rgb("+foreColor+");");				
			}
			Boolean bold=cellStyle.getBold(),italic=cellStyle.getItalic(),underline=cellStyle.getUnderline();
			if(bold!=null && bold){
				sb.append("font-weight:bold;");								
			}
			if(italic!=null && italic){
				sb.append("font-style:italic;");												
			}
			if(underline!=null && underline){
				sb.append("text-decoration:underline;");												
			}
			Border border=cellStyle.getLeftBorder();
			if(border!=null){
				sb.append("border-left:"+border.getStyle().name()+" "+border.getWidth()+"px rgb("+border.getColor()+");");				
			}
			border=cellStyle.getRightBorder();
			if(border!=null){
				sb.append("border-right:"+border.getStyle().name()+" "+border.getWidth()+"px rgb("+border.getColor()+");");				
			}
			border=cellStyle.getTopBorder();
			if(border!=null){
				sb.append("border-top:"+border.getStyle().name()+" "+border.getWidth()+"px rgb("+border.getColor()+");");				
			}
			border=cellStyle.getBottomBorder();
			if(border!=null){
				sb.append("border-bottom:"+border.getStyle().name()+" "+border.getWidth()+"px rgb("+border.getColor()+");");				
			}
			sb.append("}");
		}
		return sb.toString();
	}
	
	public SearchFormData buildSearchFormData(Map<String, Dataset> datasetMap, Map<String, Object> parameters){
		if(searchForm==null){
			return null;
		}
		RenderContext context=new RenderContext(datasetMap,parameters);
		SearchFormData data=new SearchFormData();
		data.setFormPosition(searchForm.getFormPosition());
		data.setHtml(searchForm.toHtml(context));
		data.setJs(searchForm.toJs(context));
		data.setSearchFormXml(searchFormXml);
		return data;
	}
	
	private int getColumnWidth(int columnNumber,int colSpan){
		int width=0;
		if(colSpan>0)colSpan--;
		int start=columnNumber,end=start+colSpan;
		for(int i=start;i<=end;i++){
			for(ColumnDefinition col:columns){
				if(col.getColumnNumber()==i){
					width+=col.getWidth();
				}
			}			
		}
		return width;
	}

	public String getReportFullName() {
		return reportFullName;
	}

	public void setReportFullName(String reportFullName) {
		this.reportFullName = reportFullName;
	}

	public Paper getPaper() {
		return paper;
	}

	public void setPaper(Paper paper) {
		this.paper = paper;
	}

	public CellDefinition getRootCell() {
		return rootCell;
	}

	public void setRootCell(CellDefinition rootCell) {
		this.rootCell = rootCell;
	}

	public HeaderFooterDefinition getHeader() {
		return header;
	}

	public void setHeader(HeaderFooterDefinition header) {
		this.header = header;
	}

	public HeaderFooterDefinition getFooter() {
		return footer;
	}

	public void setFooter(HeaderFooterDefinition footer) {
		this.footer = footer;
	}

	public SearchForm getSearchForm() {
		return searchForm;
	}

	public void setSearchForm(SearchForm searchForm) {
		this.searchForm = searchForm;
	}

	public List<RowDefinition> getRows() {
		return rows;
	}

	public void setRows(List<RowDefinition> rows) {
		this.rows = rows;
	}

	public List<ColumnDefinition> getColumns() {
		return columns;
	}

	public void setColumns(List<ColumnDefinition> columns) {
		this.columns = columns;
	}

	public List<CellDefinition> getCells() {
		return cells;
	}

	public void setCells(List<CellDefinition> cells) {
		this.cells = cells;
		Map<String, CellDefinition> cellsMap= new HashMap<>();
		Map<String, CellDefinition> cellsRowColMap= new HashMap<>();
		buildMap(cells,cellsMap,cellsRowColMap);
		Optional.ofNullable(cells).orElse(Collections.emptyList()).forEach(e->{
			e.buildLeftParentCell(cellsMap,cellsRowColMap,cells);
			e.buildTopParentCell(cellsMap,cellsRowColMap,cells);
		});
	}


	private void buildMap(List<CellDefinition> cells, Map<String, CellDefinition> cellsMap, Map<String, CellDefinition> cellsRowColMap) {
		for(CellDefinition cell: cells){
			cellsMap.put(cell.getName(), cell);
			int rowNum=cell.getRowNumber(),colNum=cell.getColumnNumber(),rowSpan=cell.getRowSpan(),colSpan=cell.getColSpan();
			rowSpan = rowSpan>0 ? rowSpan-- : 1;
			colSpan = colSpan>0 ? colSpan-- : 1;
			int rowEnd=rowNum+rowSpan,colEnd=colNum+colSpan;
			for(int i = rowNum; i<rowEnd; i++){
				cellsRowColMap.put(i+","+colNum, cell);
			}
			for(int i = colNum; i<colEnd; i++){
				cellsRowColMap.put(rowNum+","+i, cell);
			}
		}
	}
	public void setDatasources(List<DatasourceDefinition> datasources) {
		this.datasources = datasources;
	}
	public List<DatasourceDefinition> getDatasources() {
		return datasources;
	}
	public String getSearchFormXml() {
		return searchFormXml;
	}
	public void setSearchFormXml(String searchFormXml) {
		this.searchFormXml = searchFormXml;
	}
}
