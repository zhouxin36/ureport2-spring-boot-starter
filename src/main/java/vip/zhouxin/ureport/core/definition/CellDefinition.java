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
import vip.zhouxin.ureport.core.Range;
import vip.zhouxin.ureport.core.definition.value.SimpleValue;
import vip.zhouxin.ureport.core.definition.value.Value;
import vip.zhouxin.ureport.core.exception.ReportException;
import vip.zhouxin.ureport.core.expression.ExpressionUtils;
import vip.zhouxin.ureport.core.expression.model.Expression;
import vip.zhouxin.ureport.core.model.Cell;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Jacky.gao
 * @since 2016年11月1日
 */
public class CellDefinition implements Serializable{
	private static final long serialVersionUID = -2667510071560936139L;
	private int rowNumber;
	private int columnNumber;
	private int rowSpan;
	private int colSpan;
	private String name;
	private Value value = new SimpleValue("");
	private CellStyle cellStyle=new CellStyle();
	
	private String linkUrl;
	private String linkTargetWindow;
	private List<LinkParameter> linkParameters;
	
	@JsonIgnore(value = true)
	private Expression linkUrlExpression;
	
	private boolean fillBlankRows;
	/**
	 * 允许填充空白行时fillBlankRows=true，要求当前数据行数必须是multiple定义的行数的倍数，否则就补充空白行
	 */
	private int multiple;
	
	private Expand expand= Expand.None;
	
	@JsonIgnore(value = true)
	private Range duplicateRange;
	@JsonIgnore(value = true)
	private List<String> increaseSpanCellNames= new ArrayList<>();
	@JsonIgnore(value = true)
	private Map<String, BlankCellInfo> newBlankCellsMap= new HashMap<>();
	@JsonIgnore(value = true)
	private List<String> newCellNames= new ArrayList<>();
	
	/**
	 * 当前单元格左父格名
	 */
	private String leftParentCellName;
	/**
	 * 当前单元格上父格名
	 */
	private String topParentCellName;
	/**
	 * 当前单元格左父格
	 */
	@JsonIgnore(value = true)
	private CellDefinition leftParentCell;
	/**
	 * 当前单元格上父格
	 */
	@JsonIgnore(value = true)
	private CellDefinition topParentCell;
	/**
	 * 当前单无格所在行的所有子格
	 */
	@JsonIgnore(value = true)
	private List<CellDefinition> rowChildrenCells= new ArrayList<>();
	/**
	 * 当前单无格所在列的所有子格
	 */
	@JsonIgnore(value = true)
	private List<CellDefinition> columnChildrenCells= new ArrayList<>();
	
	private List<ConditionPropertyItem> conditionPropertyItems;
	
	protected Cell newCell(){
		Cell cell=new Cell();
		cell.setValue(value);
		cell.setName(name);
		cell.setRowSpan(rowSpan);
		cell.setColSpan(colSpan);
		cell.setExpand(expand);
		cell.setCellStyle(cellStyle);
		cell.setNewBlankCellsMap(newBlankCellsMap);
		cell.setIncreaseSpanCellNames(increaseSpanCellNames);
		cell.setNewCellNames(newCellNames);
		cell.setDuplicateRange(duplicateRange);
		cell.setLinkParameters(linkParameters);
		cell.setLinkTargetWindow(linkTargetWindow);
		cell.setLinkUrl(linkUrl);
		cell.setConditionPropertyItems(conditionPropertyItems);
		cell.setFillBlankRows(fillBlankRows);
		cell.setMultiple(multiple);
		cell.setLinkUrlExpression(linkUrlExpression);
		return cell;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public int getColumnNumber() {
		return columnNumber;
	}

	public void setColumnNumber(int columnNumber) {
		this.columnNumber = columnNumber;
	}

	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}
	
	public int getRowSpan() {
		return rowSpan;
	}

	public void setRowSpan(int rowSpan) {
		this.rowSpan = rowSpan;
	}

	public int getColSpan() {
		return colSpan;
	}

	public void setColSpan(int colSpan) {
		this.colSpan = colSpan;
	}

	public Expand getExpand() {
		return expand;
	}

	public void setExpand(Expand expand) {
		this.expand = expand;
	}
	
	public String getLeftParentCellName() {
		return leftParentCellName;
	}

	public void setLeftParentCellName(String leftParentCellName) {
		this.leftParentCellName = leftParentCellName;
	}

	public String getTopParentCellName() {
		return topParentCellName;
	}

	public void setTopParentCellName(String topParentCellName) {
		this.topParentCellName = topParentCellName;
	}

	public CellDefinition getLeftParentCell() {
		return leftParentCell;
	}

	public void buildLeftParentCell(Map<String,CellDefinition> cellsMap,Map<String,CellDefinition> cellsRowColMap,List<CellDefinition> cells){
		int rowNumber=getRowNumber();
		int colNumber=getColumnNumber();
		String leftParentCellName=getLeftParentCellName();
		if(StringUtils.isNotBlank(leftParentCellName)){
			if(!leftParentCellName.equals("root")){
				CellDefinition targetCell=cellsMap.get(leftParentCellName);
				if(targetCell==null){
					throw new ReportException("Cell ["+getName()+"] 's left parent cell ["+leftParentCellName+"] not exist.");
				}
				setLeftParentCell(targetCell);
			}
		}else{
			if(colNumber>1){
				CellDefinition targetCell=cellsRowColMap.get(rowNumber+","+(colNumber-1));
				setLeftParentCell(targetCell);
			}
		}
	}

	public void setLeftParentCell(CellDefinition leftParentCell) {
		this.leftParentCell = leftParentCell;
	}

	public CellDefinition getTopParentCell() {
		return topParentCell;
	}


	public void buildTopParentCell(Map<String,CellDefinition> cellsMap,Map<String,CellDefinition> cellsRowColMap,List<CellDefinition> cells){
		int rowNumber=getRowNumber();
		int colNumber=getColumnNumber();
		String topParentCellName=getTopParentCellName();
		if(StringUtils.isNotBlank(topParentCellName)){
			if(!topParentCellName.equals("root")){
				CellDefinition targetCell=cellsMap.get(topParentCellName);
				if(targetCell==null){
					throw new ReportException("Cell ["+getName()+"] 's top parent cell ["+topParentCellName+"] not exist.");
				}
				setTopParentCell(targetCell);
			}
		}else{
			if(rowNumber>1){
				CellDefinition targetCell=cellsRowColMap.get((rowNumber-1)+","+colNumber);
				setTopParentCell(targetCell);
			}
		}
	}


	public void setTopParentCell(CellDefinition topParentCell) {
		this.topParentCell = topParentCell;
	}

	public CellStyle getCellStyle() {
		return cellStyle;
	}
	
	public boolean isFillBlankRows() {
		return fillBlankRows;
	}

	public void setFillBlankRows(boolean fillBlankRows) {
		this.fillBlankRows = fillBlankRows;
	}

	public int getMultiple() {
		return multiple;
	}

	public void setMultiple(int multiple) {
		this.multiple = multiple;
	}

	public Range getDuplicateRange() {
		return duplicateRange;
	}
	
	public void setDuplicateRange(Range duplicateRange) {
		this.duplicateRange = duplicateRange;
	}

	public void setCellStyle(CellStyle cellStyle) {
		this.cellStyle = cellStyle;
	}

	public List<CellDefinition> getRowChildrenCells() {
		return rowChildrenCells;
	}
	public List<CellDefinition> getColumnChildrenCells() {
		return columnChildrenCells;
	}
	public List<String> getIncreaseSpanCellNames() {
		return increaseSpanCellNames;
	}
	public Map<String, BlankCellInfo> getNewBlankCellsMap() {
		return newBlankCellsMap;
	}
	public List<String> getNewCellNames() {
		return newCellNames;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
		if(StringUtils.isNotBlank(linkUrl)){
			if(linkUrl.startsWith(ExpressionUtils.EXPR_PREFIX) && linkUrl.endsWith(ExpressionUtils.EXPR_SUFFIX)){
				String expr=linkUrl.substring(2,linkUrl.length()-1);
				Expression urlExpression= ExpressionUtils.parseExpression(expr);
				setLinkUrlExpression(urlExpression);
			}
		}
	}

	public String getLinkTargetWindow() {
		return linkTargetWindow;
	}

	public void setLinkTargetWindow(String linkTargetWindow) {
		this.linkTargetWindow = linkTargetWindow;
	}

	public List<LinkParameter> getLinkParameters() {
		return linkParameters;
	}

	public void setLinkParameters(List<LinkParameter> linkParameters) {
		this.linkParameters = linkParameters;
	}
	public List<ConditionPropertyItem> getConditionPropertyItems() {
		return conditionPropertyItems;
	}
	public void setConditionPropertyItems(
			List<ConditionPropertyItem> conditionPropertyItems) {
		this.conditionPropertyItems = conditionPropertyItems;
	}
	public Expression getLinkUrlExpression() {
		return linkUrlExpression;
	}
	public void setLinkUrlExpression(Expression linkUrlExpression) {
		this.linkUrlExpression = linkUrlExpression;
	}
}
