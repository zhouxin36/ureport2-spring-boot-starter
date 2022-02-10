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

import org.antlr.v4.runtime.tree.TerminalNode;
import vip.zhouxin.ureport.core.definition.Order;
import vip.zhouxin.ureport.core.definition.value.AggregateType;
import vip.zhouxin.ureport.core.expression.model.condition.BaseCondition;
import vip.zhouxin.ureport.core.expression.model.expr.BaseExpression;
import vip.zhouxin.ureport.core.expression.model.expr.dataset.DatasetExpression;
import vip.zhouxin.ureport.core.dsl.ReportParserParser.ConditionsContext;
import vip.zhouxin.ureport.core.dsl.ReportParserParser.DatasetContext;
import vip.zhouxin.ureport.core.dsl.ReportParserParser.UnitContext;

/**
 * @author Jacky.gao
 * @since 2016年12月26日
 */
public class DatasetExpressionBuilder extends BaseExpressionBuilder {
	@Override
	public BaseExpression build(UnitContext unitContext) {
		DatasetContext context=(DatasetContext)unitContext.dataset();
		DatasetExpression expr=new DatasetExpression();
		expr.setExpr(context.getText());
		expr.setDatasetName(context.Identifier().getText());
		expr.setAggregate(AggregateType.valueOf(context.aggregate().getText()));
		if(context.property()!=null){
			expr.setProperty(context.property().getText());			
		}
		ConditionsContext conditionsContext=context.conditions();
		if(conditionsContext!=null){
			BaseCondition condition=buildConditions(conditionsContext);
			expr.setCondition(condition);
		}
		TerminalNode orderNode=context.ORDER();
		if(orderNode!=null){
			expr.setOrder(Order.valueOf(orderNode.getText()));
		}
		return expr;
	}

	@Override
	public boolean support(UnitContext unitContext) {
		return unitContext.dataset()!=null;
	}
}
