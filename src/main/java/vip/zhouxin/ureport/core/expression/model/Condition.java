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
package vip.zhouxin.ureport.core.expression.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import vip.zhouxin.ureport.core.build.Context;
import vip.zhouxin.ureport.core.expression.model.condition.Join;
import vip.zhouxin.ureport.core.expression.model.condition.PropertyExpressionCondition;
import vip.zhouxin.ureport.core.model.Cell;

/**
 * @author Jacky.gao
 * @since 2016年11月18日
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type",defaultImpl = PropertyExpressionCondition.class)
public interface Condition {
	/**
	 * @param cell 当前Condition所在的单元格
	 * @param currentCell 当前Condition正在处理的单元格
	 * @param obj 要判断的对象
	 * @param context 上下文对象
	 * @return 返回是否符合条件
	 */
	boolean filter(Cell cell, Cell currentCell, Object obj, Context context);

	void setNextCondition(Condition nextCondition);
	void setJoin(Join join);
	Join getJoin();
}
