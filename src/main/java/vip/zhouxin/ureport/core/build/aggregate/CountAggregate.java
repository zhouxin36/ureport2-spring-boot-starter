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
package vip.zhouxin.ureport.core.build.aggregate;

import vip.zhouxin.ureport.core.Utils;
import vip.zhouxin.ureport.core.build.BindData;
import vip.zhouxin.ureport.core.build.Context;
import vip.zhouxin.ureport.core.definition.value.Value;
import vip.zhouxin.ureport.core.exception.ReportComputeException;
import vip.zhouxin.ureport.core.expression.model.Condition;
import vip.zhouxin.ureport.core.expression.model.expr.dataset.DatasetExpression;
import vip.zhouxin.ureport.core.model.Cell;
import vip.zhouxin.ureport.core.utils.DataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jacky.gao
 * @since 2017年1月20日
 */
public class CountAggregate extends Aggregate {
	@Override
	public List<BindData> aggregate(DatasetExpression expr, Cell cell, Context context) {
		String datasetName=expr.getDatasetName();
		Cell leftCell= DataUtils.fetchLeftCell(cell, context, datasetName);
		Cell topCell= DataUtils.fetchTopCell(cell, context, datasetName);
		List<Object> leftList=null,topList=null;
		if(leftCell!=null){
			leftList=leftCell.getBindData();
		}
		if(topCell!=null){
			topList=topCell.getBindData();
		}
		int count=0;
		if(leftList==null && topList==null){
			List<?> data=context.getDatasetData(datasetName);
			count = doCondition(data, cell, expr,context);
		}else if(leftList==null){
			count = doCondition(topList, cell, expr,context);
		}else if(topList==null){
			count = doCondition(leftList, cell, expr,context);
		}else{
			List<Object> list;
			Object data;
			String prop;
			if(leftList.size()>topList.size()){
				list=topList;
				data=leftCell.getData();
				Value value=leftCell.getValue();
				DatasetExpression de= DataUtils.fetchDatasetExpression(value);
				if(de==null){
					throw new ReportComputeException("Unsupport value : "+value);
				}
				prop=de.getProperty();
			}else{
				list=leftList;
				data=topCell.getData();
				Value value=topCell.getValue();
				DatasetExpression de= DataUtils.fetchDatasetExpression(value);
				if(de==null){
					throw new ReportComputeException("Unsupport value : "+value);
				}
				prop=de.getProperty();
			}
			Condition condition=getCondition(cell);
			if(condition==null){
				condition=expr.getCondition();
			}
			for(Object obj:list){
				if(condition!=null && !condition.filter(cell, cell, obj, context)){
					continue;
				}
				Object o= Utils.getProperty(obj, prop);
				if(o!=null && data!=null && (o==data || o.equals(data))){
					count++;
				}else if(o==null && data==null){
					count++;
				}
			}
		}
		List<BindData> list= new ArrayList<>();
		list.add(new BindData(count,null));
		return list;
	}
	private int doCondition(List<?> dataList, Cell cell, DatasetExpression expr, Context context){
		Condition condition=getCondition(cell);
		if(condition==null){
			condition=expr.getCondition();
		}
		if(condition==null){
			return dataList.size();
		}
		int size=0;
		for(Object obj:dataList){
			boolean result=condition.filter(cell, cell, obj, context);
			if(result)size++;
		}
		return size;
	}
}