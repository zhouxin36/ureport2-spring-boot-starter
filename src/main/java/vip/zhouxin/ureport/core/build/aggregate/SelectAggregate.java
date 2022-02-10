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
public class SelectAggregate extends Aggregate {
	@Override
	public List<BindData> aggregate(DatasetExpression expr, Cell cell, Context context) {
		List<?> objList= DataUtils.fetchData(cell, context, expr.getDatasetName());
		return doAggregate(expr, cell, context, objList);
	}

	protected List<BindData> doAggregate(DatasetExpression expr, Cell cell, Context context, List<?> objList) {
		List<BindData> list= new ArrayList<>();
		Map<String,String> mappingMap=context.getMapping(expr);
		String property=expr.getProperty();
		for(Object o:objList){
			boolean conditionResult=doCondition(expr.getCondition(),cell,o,context);
			if(!conditionResult){
				continue;
			}
			List<Object> bindList= new ArrayList<>();
			bindList.add(o);
			Object data= Utils.getProperty(o, property);
			Object mappingData=mappingData(mappingMap,data);
			if(mappingData==null){
				list.add(new BindData(data,bindList));
			}else{
				list.add(new BindData(data,mappingData,bindList));
			}
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
