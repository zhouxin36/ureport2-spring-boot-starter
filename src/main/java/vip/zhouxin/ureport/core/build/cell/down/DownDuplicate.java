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
package vip.zhouxin.ureport.core.build.cell.down;

import vip.zhouxin.ureport.core.build.Context;
import vip.zhouxin.ureport.core.model.Cell;
import vip.zhouxin.ureport.core.model.Report;
import vip.zhouxin.ureport.core.model.Row;

import java.util.*;

/**
 * @author Jacky.gao
 * @since 2016年11月10日
 */
public class DownDuplicate {
	private int index;
	private final Cell mainCell;
	private final int rowSize;
	private final Context context;
	private int minRowNumber=-1;
	private final Map<Row, Row> rowMap= new HashMap<>();
	private final List<Row> newRowList= new ArrayList<>();
	public DownDuplicate(Cell mainCell, int rowSize, Context context) {
		this.mainCell=mainCell;
		this.rowSize=rowSize;
		this.context=context;
	}
	
	public Row newRow(Row row, int currentRowNumber){
		if(rowMap.containsKey(row)){
			return rowMap.get(row);
		}else{
			int rowNumber=currentRowNumber;
			Row newRow=row.newRow();
			rowNumber=rowNumber+rowSize*(index-1)+rowSize;
			if(minRowNumber==-1 || minRowNumber>rowNumber){
				minRowNumber=rowNumber;
			}
			newRow.setTempRowNumber(rowNumber); 
			newRowList.add(newRow);
			rowMap.put(row, newRow);
			return newRow;
		}
	}
	
	public Cell getMainCell() {
		return mainCell;
	}
	
	public int getRowSize() {
		return rowSize;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public Context getContext() {
		return context;
	}
	public void complete(){
		if(minRowNumber<1){
			return;
		}
		Report report=context.getReport();
		Collections.sort(newRowList, Comparator.comparingInt(Row::getTempRowNumber));
		report.insertRows(minRowNumber, newRowList);
	}
	public void reset(){
		rowMap.clear();
	}
}
