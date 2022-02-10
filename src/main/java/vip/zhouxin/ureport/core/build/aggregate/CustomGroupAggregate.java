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

import vip.zhouxin.ureport.core.build.BindData;
import vip.zhouxin.ureport.core.build.Context;
import vip.zhouxin.ureport.core.definition.Order;
import vip.zhouxin.ureport.core.definition.value.GroupItem;
import vip.zhouxin.ureport.core.expression.model.Condition;
import vip.zhouxin.ureport.core.expression.model.expr.dataset.DatasetExpression;
import vip.zhouxin.ureport.core.model.Cell;
import vip.zhouxin.ureport.core.utils.DataUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jacky.gao
 * @since 2017年3月29日
 */
public class CustomGroupAggregate extends Aggregate {

	@Override
	public List<BindData> aggregate(DatasetExpression expr, Cell cell, Context context) {
		List<?> objList= DataUtils.fetchData(cell, context, expr.getDatasetName());
		return doAggregate(expr, cell, context, objList);
	}
	protected List<BindData> doAggregate(DatasetExpression expr, Cell cell, Context context, List<?> objList) {
		List<BindData> list= new ArrayList<>();
		List<GroupItem> groupItems=expr.getGroupItems();
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
			String itemName=groupData(groupItems, cell, context, o);
			if(itemName==null){
				list.add(new BindData(""));
				return list;
			}
			List<Object> rowList= new ArrayList<>();
			rowList.add(o);
			list.add(new BindData(itemName,rowList));
			return list;
		}
		Map<Object,List<Object>> map= new HashMap<>();
		for(Object o:objList){
			boolean conditionResult=doCondition(expr.getCondition(),cell,o,context);
			if(!conditionResult){
				continue;
			}
			String itemName=groupData(groupItems, cell, context, o);
			if(itemName==null){
				continue;
			}
			List<Object> rowList;
			if(map.containsKey(itemName)){
				rowList=map.get(itemName);
			}else{
				rowList= new ArrayList<>();
				map.put(itemName, rowList);
				list.add(new BindData(itemName,rowList));
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
	private String groupData(List<GroupItem> groupItems, Cell cell, Context context, Object o){
		for(GroupItem item:groupItems){
			Condition condition=item.getCondition();
			boolean doCondition=doCondition(condition, cell, o, context);
			if(doCondition){
				return item.getName();
			}
		}
		return null;
	}
}
