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
package vip.zhouxin.ureport.core.expression.function;

import vip.zhouxin.ureport.core.build.BindData;
import vip.zhouxin.ureport.core.build.Context;
import vip.zhouxin.ureport.core.exception.ReportComputeException;
import vip.zhouxin.ureport.core.expression.model.data.BindDataListExpressionData;
import vip.zhouxin.ureport.core.expression.model.data.ExpressionData;
import vip.zhouxin.ureport.core.expression.model.data.ObjectExpressionData;
import vip.zhouxin.ureport.core.expression.model.data.ObjectListExpressionData;
import vip.zhouxin.ureport.core.model.Cell;
import vip.zhouxin.ureport.core.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Jacky.gao
 * @since 2017年5月23日
 */
public class FormatDateFunction implements Function {
	@Override
	public Object execute(List<ExpressionData<?>> dataList, Context context, Cell currentCell) {
		if(dataList==null){
			return "";
		}
		Object obj=null;
		String pattern= DateUtils.STANDARD_DATE_FORMAT;
		for(ExpressionData<?> data:dataList){
			if(data instanceof ObjectListExpressionData){
				ObjectListExpressionData listExpressionData=(ObjectListExpressionData)data;
				List<?> list=listExpressionData.getData();
				if(list.size()>0){
					obj=list.get(0);
				}
				if(list.size()>1){
					pattern=list.get(1).toString();
				}
			}else if(data instanceof ObjectExpressionData){
				obj=((ObjectExpressionData)data).getData();
			}else if(data instanceof BindDataListExpressionData){
				BindDataListExpressionData bindDataList=(BindDataListExpressionData)data;
				List<BindData> list=bindDataList.getData();
				if(list.size()>0){
					obj=list.get(0).getValue();
				}
				if(list.size()>1){
					pattern=list.get(1).getValue().toString();
				}
			}
		}
		if(obj==null){
			throw new ReportComputeException("Function [formatdate] need a Date type parameter at least");
		}else{
			if(obj instanceof Date){
				SimpleDateFormat sd=new SimpleDateFormat(pattern);
				return sd.format((Date)obj);
			}else{
				throw new ReportComputeException("Function [formatdate] first parameter is Date type");
			}
		}
	}

	@Override
	public String name() {
		return "formatdate";
	}
}
