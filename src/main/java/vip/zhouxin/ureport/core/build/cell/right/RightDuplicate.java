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
package vip.zhouxin.ureport.core.build.cell.right;

import vip.zhouxin.ureport.core.build.Context;
import vip.zhouxin.ureport.core.model.Cell;
import vip.zhouxin.ureport.core.model.Column;
import vip.zhouxin.ureport.core.model.Report;

import java.util.*;

/**
 * @author Jacky.gao
 * @since 2016年11月10日
 */
public class RightDuplicate {
	private int index;
	private final int columnSize;
	private final Context context;
	private final Cell mainCell;
	private int minColNumber=-1;
	private final Map<Column, Column> colMap= new HashMap<>();
	private final List<Column> newColList= new ArrayList<>();
	public RightDuplicate(Cell mainCell, int columnSize, Context context) {
		this.mainCell=mainCell;
		this.columnSize=columnSize;
		this.context=context;
	}
	
	public Column newColumn(Column col, int colNumber){
		if(colMap.containsKey(col)){
			return colMap.get(col);
		}else{
			Column newCol=col.newColumn();
			colNumber=colNumber+columnSize*(index-1)+columnSize;
			if(minColNumber==-1 || minColNumber>colNumber){
				minColNumber=colNumber;
			}
			newCol.setTempColumnNumber(colNumber);
			newColList.add(newCol);
			colMap.put(col, newCol);
			return newCol;
		}
	}
	
	public int getColSize() {
		return columnSize;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public Context getContext() {
		return context;
	}
	
	public Cell getMainCell() {
		return mainCell;
	}
	
	public void complete(){
		if(minColNumber<1){
			return;
		}
		Report report=context.getReport();
		Collections.sort(newColList, Comparator.comparingInt(Column::getTempColumnNumber));
		report.insertColumns(minColNumber, newColList);
	}
	public void reset(){
		colMap.clear();
	}
}
