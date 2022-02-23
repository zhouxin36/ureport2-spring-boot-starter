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

import java.util.Arrays;
import java.util.List;

/**
 * @author Jacky.gao
 * @since 2017年10月23日
 */
public class RadioInputComponent extends DatasetComponent {
	public static final String TYPE = "Radio";
	private boolean optionsInline;
	@Override
	String inputHtml(RenderContext context,Object pValue) {
		StringBuilder sb=new StringBuilder();
		String[] data=pValue.toString().split(",");
		List<String> list=Arrays.asList(data);
		sb.append("<div>");
		for(Option option:generateOption(context)){
			String value=option.getValue();
			String label=option.getLabel();
			String checked=list.contains(value) ? "checked" : "";
			if(this.optionsInline){
				sb.append("<span class='checkbox-inline' style='padding-top:0px;padding-left:2px;padding-top:0px'><input value='").append(value).append("' ").append(refreshHtml()).append(checked).append(" type='radio' name='").append(getBindParameter()).append("'> ").append(label).append("</span>");
			}else{
				sb.append("<span class='checkbox'><input value='").append(value).append("' type='radio' ").append(refreshHtml()).append(checked).append(" name='").append(getBindParameter()).append("' style='margin-left: auto'> <span style=\"margin-left:15px\">").append(label).append("</span></span>");
			}
		}
		sb.append("</div>");
		return sb.toString();
	}
	@Override
	public String initJs(RenderContext context) {
		String name=getBindParameter();
		return "formElements.push(" +
				"function(){" +
				"if(''==='" + name + "'){" +
				"alert('单选框未绑定查询参数名，不能进行查询操作!');" +
				"throw '单选框未绑定查询参数名，不能进行查询操作!'" +
				"}" +
				"return {" +
				"\"" + name + "\":" +
				"$(\"input[name='" + getBindParameter() + "']:checked\").val()" +
				"}" +
				"}" +
				");";
	}
	public void setOptionsInline(boolean optionsInline) {
		this.optionsInline = optionsInline;
	}
	public boolean isOptionsInline() {
		return optionsInline;
	}
}
