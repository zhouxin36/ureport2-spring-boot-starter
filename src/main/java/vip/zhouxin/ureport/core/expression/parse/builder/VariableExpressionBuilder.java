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
package vip.zhouxin.ureport.core.expression.parse.builder;

import vip.zhouxin.ureport.core.expression.model.expr.BaseExpression;
import vip.zhouxin.ureport.core.expression.model.expr.VariableExpression;
import vip.zhouxin.ureport.core.dsl.ReportParserParser.UnitContext;

/**
 * @author Jacky.gao
 * @since 2018年7月15日
 */
public class VariableExpressionBuilder implements ExpressionBuilder {
	@Override
	public BaseExpression build(UnitContext unitContext) {
		String text=unitContext.variable().Identifier().getText();
		VariableExpression varExpr=new VariableExpression(text);
		return varExpr;
	}
	@Override
	public boolean support(UnitContext unitContext) {
		return unitContext.variable()!=null;
	}
}