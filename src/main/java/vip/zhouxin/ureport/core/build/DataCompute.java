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
package vip.zhouxin.ureport.core.build;

import vip.zhouxin.ureport.core.build.compute.*;
import vip.zhouxin.ureport.core.definition.value.Value;
import vip.zhouxin.ureport.core.exception.ReportException;
import vip.zhouxin.ureport.core.model.Cell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jacky.gao
 * @since 2016年12月21日
 */
public class DataCompute {
	private static final Map<String, ValueCompute> valueComputesMap = new HashMap<>();

	static{
		SimpleValueCompute simpleValueCompute=new SimpleValueCompute();
		valueComputesMap.put(simpleValueCompute.type(), simpleValueCompute);
		DatasetValueCompute datasetValueCompute=new DatasetValueCompute();
		valueComputesMap.put(datasetValueCompute.type(), datasetValueCompute);
		ExpressionValueCompute expressionValueCompute=new ExpressionValueCompute();
		valueComputesMap.put(expressionValueCompute.type(), expressionValueCompute);
		ImageValueCompute imageValueCompute=new ImageValueCompute();
		valueComputesMap.put(imageValueCompute.type(), imageValueCompute);
		SlashValueCompute slashValueCompute=new SlashValueCompute();
		valueComputesMap.put(slashValueCompute.type(), slashValueCompute);
		ZxingValueCompute zxingValueCompute=new ZxingValueCompute();
		valueComputesMap.put(zxingValueCompute.type(), zxingValueCompute);
		ChartValueCompute chartValueCompute=new ChartValueCompute();
		valueComputesMap.put(chartValueCompute.type(), chartValueCompute);
		
	}

	public static List<BindData> buildCellData(Cell cell, Context context) {
		context.resetVariableMap();
		Value value = cell.getValue();
		ValueCompute valueCompute=valueComputesMap.get(value.getType());
		if(valueCompute!=null){
			return valueCompute.compute(cell, context);
		}
		throw new ReportException("Unsupport value: "+value);
	}
}
