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
package vip.zhouxin.ureport.core.definition.searchform;


/**
 * @author Jacky.gao
 * @since 2016年1月11日
 */
public abstract class InputComponent implements Component {
	private String label;
	private String bindParameter;
	private String type;
	private Boolean refreshOnChange = false;
	protected LabelPosition labelPosition= LabelPosition.top;
	
	abstract String inputHtml(RenderContext context,Object pValue);
	@Override
	public String toHtml(RenderContext context) {
		StringBuilder sb=new StringBuilder();
		String name = getBindParameter();
		Object pValue = context.getParameter(name) == null ? "" : context.getParameter(name);
		if(this.labelPosition.equals(LabelPosition.top)){
			sb.append("<div>");			
		}else{
			sb.append("<div class='form-horizontal'>");			
		}
		sb.append("<div class='form-group' style='margin:0px 0px 10px 0px'>");
		if(this.labelPosition.equals(LabelPosition.top)){
			sb.append("<span style='font-size:13px'>").append(this.label).append("</span>");
			sb.append(inputHtml(context,pValue));
		}else{					
			sb.append("<span class='col-md-3' style='text-align:right;padding-right:1px;font-size:13px'>").append(this.label).append("</span>");
			sb.append("<div class='col-md-9' style='padding-left:1px;'>");
			sb.append(inputHtml(context,pValue));
			sb.append("</div>");
		}
		sb.append("</div>");
		sb.append("</div>");
		return sb.toString();
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getLabel() {
		return label;
	}
	public void setLabelPosition(LabelPosition labelPosition) {
		this.labelPosition = labelPosition;
	}
	public LabelPosition getLabelPosition() {
		return labelPosition;
	}
	public String getBindParameter() {
		return bindParameter;
	}
	public void setBindParameter(String bindParameter) {
		this.bindParameter = bindParameter;
	}
	@Override
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public Boolean getRefreshOnChange() {
		return refreshOnChange;
	}

	public void setRefreshOnChange(Boolean refreshOnChange) {
		this.refreshOnChange = refreshOnChange;
	}

	public String refreshHtml(){
		return getRefreshOnChange() ? " onchange=\"loadSearchForm()\" " : "";
	}
}
