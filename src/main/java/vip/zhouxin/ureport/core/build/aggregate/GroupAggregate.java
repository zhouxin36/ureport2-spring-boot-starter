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
import vip.zhouxin.ureport.core.definition.Order;
import vip.zhouxin.ureport.core.expression.model.expr.dataset.DatasetExpression;
import vip.zhouxin.ureport.core.model.Cell;
import vip.zhouxin.ureport.core.utils.DataUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jacky.gao
 * @since 2016年12月22日
 */
public class GroupAggregate extends Aggregate {
	@Override
	public List<BindData> aggregate(DatasetExpression expr, Cell cell, Context context) {
		List<?> objList= DataUtils.fetchData(cell, context, expr.getDatasetName());
		return doAggregate(expr, cell, context, objList);
	}

	protected List<BindData> doAggregate(DatasetExpression expr, Cell cell, Context context, List<?> objList) {
		String property=expr.getProperty();
		Map<String,String> mappingMap=context.getMapping(expr);
		List<BindData> list=new ArrayList<BindData>();
		if(objList.size()==0){
			list.add(new BindData(""));
			return list;
		}else if(objList.size()==1){
			Object o=objList.get(0);
			boolean conditionResult=doCondition(expr.getCondition(),cell,o,context);
			if(!conditionResult){
				list.add(new BindData(""));
				return list;
			}
			Object data= Utils.getProperty(o, property);
			Object mappingData=mappingData(mappingMap,data);
			List<Object> rowList= new ArrayList<>();
			rowList.add(o);
			if(mappingData==null){
				list.add(new BindData(data,rowList));
			}else{
				list.add(new BindData(data,mappingData,rowList));
			}
			return list;
		}
		Map<Object,List<Object>> map= new HashMap<>();
		for(Object o:objList){
			boolean conditionResult=doCondition(expr.getCondition(),cell,o,context);
			if(!conditionResult){
				continue;
			}
			Object data= Utils.getProperty(o, property);
			Object mappingData=mappingData(mappingMap,data);
			List<Object> rowList;
			if(map.containsKey(data)){
				rowList=map.get(data);
			}else{
				rowList= new ArrayList<>();
				map.put(data, rowList);
				if(mappingData==null){
					list.add(new BindData(data,rowList));
				}else{
					list.add(new BindData(data,mappingData,rowList));
				}
			}
			rowList.add(o);				
		}
		if(list.size()==0){
			List<Object> rowList= new ArrayList<>();
			rowList.add(new HashMap<String,Object>());
			list.add(new BindData("",rowList));
		}
		if(list.size()>1){
			Order order=expr.getOrder();
			orderBindDataList(list, order);			
		}
		return list;
	}
}
