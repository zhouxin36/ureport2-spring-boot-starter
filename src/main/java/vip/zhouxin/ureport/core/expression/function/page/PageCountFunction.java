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
package vip.zhouxin.ureport.core.expression.function.page;

import vip.zhouxin.ureport.core.build.Context;
import vip.zhouxin.ureport.core.expression.model.data.ExpressionData;
import vip.zhouxin.ureport.core.expression.model.data.ObjectExpressionData;
import vip.zhouxin.ureport.core.expression.model.data.ObjectListExpressionData;
import vip.zhouxin.ureport.core.model.Cell;

import java.util.List;

/**
 * @author Jacky.gao
 * @since 2017年5月5日
 */
public class PageCountFunction extends PageFunction {

	@Override
	public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
		if(dataList==null){
			return 0;
		}
		int size=0;
		for(ExpressionData<?> data:dataList){
			if(data instanceof ObjectListExpressionData){
				ObjectListExpressionData listExpressionData=(ObjectListExpressionData)data;
				size+=listExpressionData.getData().size();
			}else if(data instanceof ObjectExpressionData){
				size++;
			}
		}
		return size;
	}

	@Override
	public String name() {
		return "pcount";
	}

}
