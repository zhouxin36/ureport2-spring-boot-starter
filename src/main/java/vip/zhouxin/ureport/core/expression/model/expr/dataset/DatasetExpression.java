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
package vip.zhouxin.ureport.core.expression.model.expr.dataset;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.util.CollectionUtils;
import vip.zhouxin.ureport.core.build.BindData;
import vip.zhouxin.ureport.core.build.Context;
import vip.zhouxin.ureport.core.build.DatasetUtils;
import vip.zhouxin.ureport.core.definition.Order;
import vip.zhouxin.ureport.core.definition.mapping.MappingItem;
import vip.zhouxin.ureport.core.definition.mapping.MappingType;
import vip.zhouxin.ureport.core.definition.value.AggregateType;
import vip.zhouxin.ureport.core.definition.value.GroupItem;
import vip.zhouxin.ureport.core.expression.model.Condition;
import vip.zhouxin.ureport.core.expression.model.data.BindDataListExpressionData;
import vip.zhouxin.ureport.core.expression.model.data.ExpressionData;
import vip.zhouxin.ureport.core.expression.model.expr.BaseExpression;
import vip.zhouxin.ureport.core.model.Cell;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jacky.gao
 * @since 2016年11月18日
 */
public class DatasetExpression extends BaseExpression {
	private static final long serialVersionUID = -8794866509447790340L;
	private String datasetName;
	private AggregateType aggregate;
	private String property;
	/**
	 * 当aggregate类型为自定义分组时，采用此属性来存储自定义分组各个项目
	 */
	private List<GroupItem> groupItems;
	
	private MappingType mappingType= MappingType.simple;
	
	private String mappingDataset;
	private String mappingKeyProperty;
	private String mappingValueProperty;
	
	private List<MappingItem> mappingItems;
	
	@JsonIgnore(value = true)
	private Condition condition;
	
	@JsonIgnore(value = true)
	private Map<String,String> mapping=null;
	
	/**
	 * 此属性给设计器使用，引擎不使用该属性
	 */
	private List<Condition> conditions;
	private Order order;
	
	@Override
	public ExpressionData<?> compute(Cell cell, Cell currentCell, Context context) {
		List<BindData> bindDataList= DatasetUtils.computeDatasetExpression(this, cell, context);
		return new BindDataListExpressionData(bindDataList);
	}
	
	public String getDatasetName() {
		return datasetName;
	}

	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}

	public AggregateType getAggregate() {
		return aggregate;
	}

	public void setAggregate(AggregateType aggregate) {
		this.aggregate = aggregate;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public List<GroupItem> getGroupItems() {
		return groupItems;
	}

	public void setGroupItems(List<GroupItem> groupItems) {
		this.groupItems = groupItems;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public List<Condition> getConditions() {
		return conditions;
	}

	public void setConditions(List<Condition> conditions) {
		this.conditions = conditions;
		if (CollectionUtils.isEmpty(conditions)){
			return;
		}
		if (conditions.size()>1){
			Condition pre = conditions.get(0);
			for (int i = 1; i < conditions.size(); i++) {
				Condition next = conditions.get(i);
				pre.setNextCondition(next);
				pre.setJoin(next.getJoin());
				pre = next;
			}
		}
		this.condition = conditions.get(0);
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public List<MappingItem> getMappingItems() {
		return mappingItems;
	}
	public void setMappingItems(List<MappingItem> mappingItems) {
		this.mappingItems = mappingItems;
		if(mappingItems!=null){
			mapping= new HashMap<>();
			for(MappingItem item:mappingItems){
				mapping.put(item.getValue(),item.getLabel());
			}				
		}
	}
	
	public MappingType getMappingType() {
		return mappingType;
	}

	public void setMappingType(MappingType mappingType) {
		this.mappingType = mappingType;
	}

	public String getMappingDataset() {
		return mappingDataset;
	}

	public void setMappingDataset(String mappingDataset) {
		this.mappingDataset = mappingDataset;
	}

	public String getMappingKeyProperty() {
		return mappingKeyProperty;
	}

	public void setMappingKeyProperty(String mappingKeyProperty) {
		this.mappingKeyProperty = mappingKeyProperty;
	}

	public String getMappingValueProperty() {
		return mappingValueProperty;
	}

	public void setMappingValueProperty(String mappingValueProperty) {
		this.mappingValueProperty = mappingValueProperty;
	}

	public Map<String, String> getMapping() {
		return mapping;
	}
}
