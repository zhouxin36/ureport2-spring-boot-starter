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
package vip.zhouxin.ureport.core.expression.model.expr;

import vip.zhouxin.ureport.core.build.Context;
import vip.zhouxin.ureport.core.expression.model.data.ExpressionData;
import vip.zhouxin.ureport.core.expression.model.data.ObjectExpressionData;
import vip.zhouxin.ureport.core.model.Cell;

/**
 * @author Jacky.gao
 * @since 2016年12月23日
 */
public class IntegerExpression extends BaseExpression {
	private static final long serialVersionUID = -8903608366537307519L;
	private Integer value;
	public IntegerExpression(Integer value) {
		this.value=value;
	}
	@Override
	public ExpressionData<?> compute(Cell cell, Cell currentCell, Context context) {
		return new ObjectExpressionData(value);
	}
}